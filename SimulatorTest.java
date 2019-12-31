package homework2;

import static org.junit.Assert.*;
import org.junit.Test;


/**
 * BipartiteGraphTest contains JUnit block-box unit tests for BipartiteGraph.
 */
public class SimulatorTest {

	@Test
    public void SimulatorTestDriverTest() {
		SimulatorTestDriver sim = new SimulatorTestDriver();
        
        //create the simulator
		sim.createSimulator("sim1");
		
		//add channels
		sim.addChannel("sim1", "chann0", 10000);
		sim.addChannel("sim1", "chann1", 1500);
		sim.addChannel("sim1", "chann2", 1500);
		
		//add participants
		sim.addParticipant("sim1", "part1", "BANANAS", 45);
		sim.addParticipant("sim1", "part2", "SOCKS", 70);
		sim.addParticipant("sim1", "part3", "IPHONES", 1000);
		
		//connect channels and participants
		sim.addEdge("sim1", "chann0", "part1", "e01");
		sim.addEdge("sim1", "part1", "chann1", "e10");
		sim.addEdge("sim1", "chann1", "part2", "e11");
		sim.addEdge("sim1", "part2", "chann2", "e20");
		sim.addEdge("sim1", "chann2", "part3", "e21");
		
		System.out.println("the edges Names:");
		sim.printAllEdges("sim1");
		
		// pushes transactions into the initial channel
		Transaction tx0 = new Transaction ("BANANAS", 10);
		Transaction tx1 = new Transaction ("SOCKS", 700);
		Transaction tx2 = new Transaction ("IPHONES", 1500);
		Transaction tx3 = new Transaction ("COWS", 1050);
		
		sim.sendTransaction("sim1","chann0", tx0);
		sim.sendTransaction("sim1","chann0", tx1);
		sim.sendTransaction("sim1","chann0", tx2);
		sim.sendTransaction("sim1","chann0", tx3);

		// simulate
		
		for (int i = 0; i < 15; ++i) {
			sim.simulate("sim1");
		}
		
		// check sim
		assertEquals("msgTODO", 10, (int)sim.getParticipantStorageAmount("sim1","part1"));
		assertEquals("msgTODO", 0, (int)sim.getParticipantToRecycleAmount("sim1","part1"));
		assertEquals("msgTODO", 70, (int)sim.getParticipantStorageAmount("sim1","part2"));
		assertEquals("msgTODO", 0, (int)sim.getParticipantToRecycleAmount("sim1","part2"));
		assertEquals("msgTODO", 1000, (int)sim.getParticipantStorageAmount("sim1","part3"));
		assertEquals("msgTODO", 2180, (int)sim.getParticipantToRecycleAmount("sim1","part3"));

    }
}