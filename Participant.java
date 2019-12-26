package homework2;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/*
 * This class implements a participant in a filters-pipes system.
 * The participant requires specific amounts of specific items, and will donate
 * any unneeded items to other participants through any viable channels he is connected to.
 * The participant can receive any items from channels leading to him, even if he does not need the item.
 */
public class Participant implements Simulatable<String>{

	// Abs. Function:
	// represents a participant with:
	// 	a storageBuffer containing all items to be donated
	// 	an itemsNeeded map, where for each entry:
	// 		the participants needs item "entry.getKey()" with amount "entry.getValue()"
	// Rep. Invariant:
	// 	for each pair: (entry1 in storageBuffer), (entry2 in itemsNeeded)
	//		entry1.getKey() != entry2.getKey()

	private Map<String, Integer> storageBuffer;
	private Map<String, Integer> itemsNeeded;
	private String id;
	
	
	/**
	 * @modifies this
	 * @effects initializes a new participant with id,
	 * 			and specific item type and amount needed.
	 */
	public Participant(String participantId, String itemNeeded, int amntNeeded) {
		storageBuffer = new HashMap<>();
		itemsNeeded = new HashMap<>();
		id = participantId;
		itemsNeeded.put(itemNeeded, amntNeeded);
		checkRep();
	}
	
	
	/**
	 * @modifies this, graph
	 * @effects performs a simulation step for this.
	 * 			if the participant has an item to donate,
	 * 			it will find the biggest channel available, and send
	 * 			the items there.
	 */
	@Override
	public void simulate(BipartiteGraph<String> graph) {
		checkRep();
		if (storageBuffer.isEmpty()) {
			return;
		}
		Map.Entry<String, Integer> toDonate = storageBuffer.entrySet().iterator().next();
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
			storageBuffer.remove(toDonate.getKey());
		}
		checkRep();
	}
	
	/**
	 * @modifies this
	 * @effects receive a transaction from a channel
	 */
	public void receiveTransaction(Transaction tx) {
		checkRep();
		int txAmnt = tx.getAmount();
		String txProduct = tx.getProduct();
		// if item is needed first try to fill in the need
		if ( itemsNeeded.containsKey(txProduct) ) {
			int amntNeeded = itemsNeeded.get(txProduct);
			if (amntNeeded < txAmnt) {
				itemsNeeded.put(txProduct, amntNeeded - txAmnt);
			}
			else {
				txAmnt -= amntNeeded;
				itemsNeeded.remove(txProduct);
			}
		}
		
		// if more is left after filling the needs, add to buffer
		if ( txAmnt > 0) {
			if (storageBuffer.containsKey(txProduct)) {
				txAmnt += storageBuffer.get(txProduct);
			}
			storageBuffer.put(txProduct, txAmnt);
		}
		checkRep();
	}

	private void checkRep() {
		Set<String> storedItems = storageBuffer.keySet();	
		Set<String> neededItems = itemsNeeded.keySet();
		for (String neededItem : neededItems) {
			assert !storedItems.contains(neededItem) : "Item both in storage and is required";
		}
	}
}
