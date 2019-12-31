package homework2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class implements a testing driver for Simulator. The driver manages
 * Simulators for recycling channels
 */
public class SimulatorTestDriver {

	private Map<String, Simulator<String, Transaction>> simulators;

	/**
	 * @modifies this
	 * @effects Constructs a new test driver.
	 */
	public SimulatorTestDriver() {
		simulators = new HashMap<>();
	}

	/**
	 * @requires simName != null
	 * @modifies this
	 * @effects Creates a new simulator named simName. The simulator's graph is
	 *          initially empty.
	 */
	public void createSimulator(String simName) {
		if (!simulators.containsKey(simName)) {
			simulators.put(simName, new Simulator<String, Transaction>());
		}
	}

	/**
	 * @requires createSimulator(simName) 
     *           && channelName != null && channelName has
	 *           not been used in a previous addChannel()  or
	 *           addParticipant() call on this object
	 *           limit > 0
	 * @modifies simulator named simName
	 * @effects Creates a new Channel named by the String channelName, with a limit, and add it to
	 *          the simulator named simName.
	 */
	public void addChannel(String simName, String channelName, int limit) {
		Simulator<String, Transaction> sim = simulators.get(simName);
		sim.addPipe(channelName, new Channel(channelName, limit));
	}

	/**
	 * @requires createSimulator(simName) && participantName != null 
	 *           && participantName has not been used in a previous addParticipant(), addChannel()
	 *           call on this object
	 *			 amount > 0
	 *			 product must be a single word, without special characters/number and also in lowercase
	 * @modifies simulator named simName
	 * @effects Creates a new Participant named by the String participantName and add
	 *          it to the simulator named simName.
	 */
	public void addParticipant(String simName, String participantName, String product, int amount) {
		Simulator<String, Transaction> sim = simulators.get(simName);
		sim.addFilter(participantName, new Participant(participantName, product, amount));
	}

	/**
	 * @requires createSimulator(simName) && ((addPipe(parentName) &&
	 *           addFilter(childName)) || (addFilter(parentName) &&
	 *           addPipe(childName))) && edgeLabel != null && node named
	 *           parentName has no other outgoing edge labeled edgeLabel 
	 *           && node named childName has no other incoming edge labeled edgeLabel
	 * @modifies simulator named simName
	 * @effects Adds an edge from the node named parentName to the node named
	 *          childName in the simulator named simName. The new edge's label
	 *          is the String edgeLabel.
	 */
	public void addEdge(String simName, String parentName, String childName, String edgeLabel) {
		Simulator<String, Transaction> sim = simulators.get(simName);
		sim.connect(edgeLabel, parentName, childName);
	}

	/**
	 * @requires createSimulator(simName) && addChannel(channelName)
	 *           A transaction Transaction != null
	 * @modifies channel named channelName
	 * @effects pushes the Transaction into the channel named channelName in the
	 *          simulator named simName.
	 */
	public void sendTransaction(String simName, String channelName, Transaction tx) {
		Simulator<String, Transaction> sim = simulators.get(simName);
		Channel channel = (Channel)sim.getPipeObject(channelName);
		channel.receiveTransaction(tx);
    }
	
	
	/**
	 * @requires addChannel(channelName)
	 * @return a space-separated list of the Transaction values currently in the
	 *         channel named channelName in the simulator named simName.
	 */
	public String listContents(String simName, String channelName) {
		Simulator<String, Transaction> sim = simulators.get(simName);
		Channel channel = (Channel)sim.getPipeObject(channelName);
		List<Transaction> transactions = channel.getTXList();
		List<String> txValues = new ArrayList<>();
		for (Transaction tx : transactions) {
			txValues.add( Integer.toString(tx.getAmount()) );
		}
		return String.join(" ", txValues);
	}


	/**
	 * @requires addParticipant(participantName)
	 * @return The sum of all Transaction amount of stored products that one has in his storage buffer.
	 */
	public double getParticipantStorageAmount(String simName, String participantName) {
		Simulator<String, Transaction> sim = simulators.get(simName);
		Participant participant = (Participant)sim.getFilterObject(participantName);
		Map<String, Integer> storageBuffer = participant.getStorageMap();
		int sum = 0;
		for ( int itemAmnt : storageBuffer.values()) {
			sum += itemAmnt;
		}
		return sum;
	}


	/**
	 * @requires addParticipant(participantName)
	 * @return The sum of all Transaction amount of waiting to be recycled products that one has.
	 */
	public double getParticipantToRecycleAmount(String simName, String participantName) {
		Simulator<String, Transaction> sim = simulators.get(simName);
		Participant participant = (Participant)sim.getFilterObject(participantName);
		Map<String, Integer> donationBuffer = participant.getDonationsMap();
		int sum = 0;
		for ( int itemAmnt : donationBuffer.values()) {
			sum += itemAmnt;
		}
		return sum;
	}


	
	/**
	 * @requires createSimulator(simName)
	 * @modifies simulator named simName
	 * @effects runs simulator named simName for a single time slice.
	 */
	public void simulate(String simName) {
		Simulator<String, Transaction> sim = simulators.get(simName);
		sim.simulate();
	}

	/**
	 * Prints the all edges.
	 *
	 * @requires simName the sim name
	 * @effects Prints the all edges.
	 */
	public void printAllEdges(String simName) {
		Simulator<String, Transaction> sim = simulators.get(simName);
		// print all pipes, and then all channels
		List<String> edgeNames = new ArrayList<>();
		edgeNames.addAll(sim.getPipesList());
		edgeNames.addAll(sim.getFiltersList());
		System.out.println(edgeNames);

	}

}
