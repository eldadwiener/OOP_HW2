package homework2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Channel is a pipe for transporting donations between Participants,
 * the channel can hold transactions until pre-defined max capacity
 * the channel can pass a transaction to one of his children
 */
public class Channel implements Simulatable<String> {
	
	// Abs. Function:
	// represents a Channel with:
	//		transaction who received but not sent yet: at this.transList
	//		the channel's max capacity at this.limitWeight
	//		the channel's current capacity at this.currentWeight	
	//		the channel's id at this.channelId
	// Rep. Invariant:
	// 		for every Transaction in transList:
	// 			amount > 0 and product != null
	//		limitWeight > 0
	//		limitWeight >= currentWeight >=0 
	//		currentWeight = sum(Transaction.amount)
	//		channelId != null
	
	private List<Transaction> transList ;
	private final int limitWeight;
	private int currentWeight;
	private String channelId;
	
	/**
	 * @modifies this
	 * @effects creates a new Channel with: id, limit-Weight, current amount = 0 and empty transactions list
	 */
	public Channel(String id, int limit) {
		channelId = id;
		limitWeight = limit;
		currentWeight = 0;
		transList = new ArrayList<>();
		checkRep();
	}
	
	/**
	* @modifies this
	* @effects chose a (randomly) transaction and child and pass the transaction to the child
	*/
	@Override
	public void simulate(BipartiteGraph<String> graph) {
		checkRep();
		// no transactions to deliver
		if (transList.size() == 0) {
			checkRep();
			return;
		}
		// no children
		if (graph.getListChildren(channelId) == null) {
			checkRep();
			return;
		}
		//choose randomly one child
		Collection<String> childrenList =  graph.getListChildren(channelId);

		Iterator<String> itr = childrenList.iterator();
		Random rand = new Random();
		String childName = itr.next();
		int idx = rand.nextInt(childrenList.size());
		for (int i = 1; i < idx; ++i) {
			if (itr.hasNext() == false) {
				break;
			}
			childName = itr.next();
		}
		
		Participant child = (Participant) graph.getNodeObj(childName);
		
		//deliver the transaction to the chosen child
		
		Transaction deliverdTrans = transList.get(0);
		transList.remove(deliverdTrans);
		currentWeight -= deliverdTrans.getAmount();
		child.receiveTransaction(deliverdTrans);
		checkRep();

	}
	
	/**
	 * @modifies this
	 * @effects add new transaction to the transactions list of the channel
	 * @return the true amount that consumed from the transaction 
	 */
	public int receiveTransaction (Transaction trans) {
		checkRep();
		int freeWeight = limitWeight - currentWeight;
		if (freeWeight <= 0) {
			checkRep();
			return 0;
		}
		int transAmount =  trans.getAmount();
		if (transAmount >  freeWeight) {
			trans.setAmount(freeWeight);
			transAmount = freeWeight;
		}
		currentWeight += transAmount;
		transList.add(trans);
		checkRep();
		return transAmount;
	}
	
	/**
	 * @effects get the available amount of the channel
	 * @return the available amount of the channel
	 */
	public int storageAvailable() {
		checkRep();
		return (limitWeight - currentWeight);
	}
	
	/**
	 * @effects get all of the channel's transactions 
	 * @return a list of copies of all transactions contained in the channel.
	 */
	public List<Transaction> getTXList() {
		checkRep();
		List<Transaction> transactions = new ArrayList<>(transList);
		checkRep();
		return transactions;
	}
	
	private void checkRep() {
		assert limitWeight > 0 : "limitWeight <= 0";
		assert limitWeight >=  currentWeight : "limitWeight < currentWeight ";
		assert currentWeight >= 0 : "currentWeight < 0";
		assert channelId != null : "channelId == null" ;
		int totalAmount = 0;
		for (Transaction trans : transList) {
			assert trans.getProduct() != null :"trans.getProduct() == null";
			assert trans.getAmount() > 0 : "trans.getAmount() <= 0";
			totalAmount += trans.getAmount();
		}
		assert totalAmount == currentWeight : "totalAmount != currentWeight";
	}
	
}
