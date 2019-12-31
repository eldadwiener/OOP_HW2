package homework2;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/*
 * This class implements a participant in a filters-pipes system.
 * The participant requires specific amounts of specific items, and will donate
 * any unneeded items to other participants through any viable channels he is connected to.
 * The participant can receive any items from channels leading to him, even if he does not need the item.
 */
public class Participant implements Simulatable<String>{

	// Abs. Function:
	// represents a participant with:
	// 	a donationsBuffer containing all items to be donated
	// 	a storageBuffer map, where for each entry:
	// 		the participants needs item "entry.getKey()" with amount "entry.getValue()"
	// Rep. Invariant:
	//	if itemNeededAmnt > 0, then donationsBuffer.containskey(itemNeeded) == false
	//	itemNeededAmnt >= 0
	//	storageBuffer  >= 0
	//	storageBuffer + itemNeededAmnt > 0
	//	itemNeeded != null
	

	private Map<String, Integer> donationsBuffer;
	private int storageBuffer;
	private int itemNeededAmnt;
	private String itemNeeded;
	private String id;
	
	
	/**
	 * @requires participantId != null, itemNeeded != null, amntNeeded > 0
	 * @modifies this
	 * @effects initializes a new participant with id,
	 * 			and specific item type and amount needed.
	 */
	public Participant(String participantId, String itemNeeded, int amntNeeded) {
		donationsBuffer = new HashMap<>();
		id = participantId;
		storageBuffer = 0;
		itemNeededAmnt = amntNeeded;
		this.itemNeeded = itemNeeded;
		checkRep();
	}
	
	
	/**
	 * @requires graph != null
	 * @modifies this, graph
	 * @effects performs a simulation step for this.
	 * 			if the participant has an item to donate,
	 * 			it will find the biggest channel available, and send
	 * 			the items there.
	 */
	@Override
	public void simulate(BipartiteGraph<String> graph) {
		checkRep();
		if (donationsBuffer.isEmpty() || (graph.getListChildren(id) == null)) {
			return;
		}
		Map.Entry<String, Integer> toDonate = donationsBuffer.entrySet().iterator().next();
		Collection<String> channels = graph.getListChildren(id);
		Channel biggestChannel = null; 
		for (String channel : channels) {
			Channel currentChannel = (Channel)graph.getNodeObj(channel);
			if (biggestChannel == null) {
				biggestChannel = currentChannel;
			}
			else if (currentChannel.storageAvailable() > biggestChannel.storageAvailable() ) {
				biggestChannel = currentChannel;
			}
		}

		// if the biggest channel found has room, send the donation to it.
		if (biggestChannel.storageAvailable() != 0) {
			biggestChannel.receiveTransaction(new Transaction(toDonate.getKey(), toDonate.getValue()));
			donationsBuffer.remove(toDonate.getKey());
		}
		checkRep();
	}
	
	/**
	 * @requires pipeId != null, pipeObj != null
	 * @modifies this
	 * @effects receive a transaction from a channel
	 */
	public void receiveTransaction(Transaction tx) {
		checkRep();
		if (tx == null) {
			return;
		}
		int txAmnt = tx.getAmount();
		String txProduct = tx.getProduct();
		// if item is needed first try to fill in the need
		if ( txProduct.equals(itemNeeded) && itemNeededAmnt > 0) {
			if (itemNeededAmnt < txAmnt) {
				storageBuffer += itemNeededAmnt;
				txAmnt -= itemNeededAmnt;
				itemNeededAmnt = 0;
			}
			else {
				storageBuffer += txAmnt;
				itemNeededAmnt -= txAmnt;
				txAmnt = 0;
			}
		}
		
		// if more is left after filling the needs, add to buffer
		if ( txAmnt > 0) {
			if (donationsBuffer.containsKey(txProduct)) {
				txAmnt += donationsBuffer.get(txProduct);
			}
			donationsBuffer.put(txProduct, txAmnt);
		}
		checkRep();
	}

	/**
	 * @effects get a map of all items required by this participant.
	 * @return map of needed items and needed amounts.
	 */
	public Map<String, Integer> getStorageMap() {
		checkRep();
		Map<String,Integer> storageMap = new HashMap<>();
		storageMap.put(itemNeeded, storageBuffer);
		checkRep();
		return storageMap;
	}

	/**
	 * @effects get a map of all items ready to be donated by this participant.
	 * @return map of donation items and amounts.
	 */
	public Map<String, Integer> getDonationsMap() {
		checkRep();
		Map<String,Integer> newList = new HashMap<>(donationsBuffer);
		checkRep();
		return newList;
	}

	private void checkRep() {
		assert (itemNeededAmnt >= 0) : "Negative item amnt needed";
		assert (storageBuffer >= 0) : "Negative storageBuffer";
		assert (itemNeededAmnt + storageBuffer > 0) : "Participant item requirement not positive";
		assert (itemNeeded != null) : "itemNeeded is null";
		if (itemNeededAmnt > 0) {
			assert !donationsBuffer.containsKey(itemNeeded) : "Item in donationsBuffer but is required";
		}
	}
}
