package homework2;

import static org.junit.Assert.*;
import org.junit.Test;


/**
 * SimulatorTest contains JUnit block-box unit tests for Simulator.
 */
public class SimulatorTest {

	// check basic channel functionality, 
	// send transactions and see if it reacts properly.
	@Test
    public void SimulatorTestDriverTest1() {
		SimulatorTestDriver sim = new SimulatorTestDriver();
        //create the simulator
		sim.createSimulator("sim1");
		
		//add channel
		sim.addChannel("sim1", "chann0", 1000);
		
		// push transactions into the channel
		// less than max
		Transaction tx0 = new Transaction ("BANANAS", 100);
		Transaction tx1 = new Transaction ("SOCKS", 700);
		// this will exceed max, only 100 should fit
		Transaction tx2 = new Transaction ("IPHONES", 200);
		// will not fit at all
		Transaction tx3 = new Transaction ("COWS", 50);
		
		sim.sendTransaction("sim1","chann0", tx0);
		sim.sendTransaction("sim1","chann0", tx1);
		sim.sendTransaction("sim1","chann0", tx2);
		sim.sendTransaction("sim1","chann0", tx3);

		// simulate
		
		for (int i = 0; i < 15; ++i) {
			sim.simulate("sim1");
		}
		
		//check simulate
		//check channel
		assertEquals("wrong contents list","100 700 200",sim.listContents("sim1", "chann0"));
    }
	
	// Connect a single channel to multiple participants,
	// make sure a transaction will go to exactly one participant.
	@Test
    public void SimulatorTestDriverTest2() {
		SimulatorTestDriver sim = new SimulatorTestDriver();
        //create the simulator
		sim.createSimulator("sim1");
		
		//add channels
		sim.addChannel("sim1", "chann0", 10000);
		
		//add participants
		sim.addParticipant("sim1", "part1", "BANANAS", 45);
		sim.addParticipant("sim1", "part2", "SOCKS", 70);
		sim.addParticipant("sim1", "part3", "IPHONES", 1000);
		
		//connect channels and participants
		sim.addEdge("sim1", "chann0", "part1", "e01");
		sim.addEdge("sim1", "chann0", "part2", "e11");
		sim.addEdge("sim1", "chann0", "part3", "e21");
		
		// pushes transactions into the initial channel
		
		//create one transaction
		Transaction tx0 = new Transaction ("COWS", 10);
		
		sim.sendTransaction("sim1","chann0", tx0);

		// simulate
		sim.simulate("sim1");
		
		//check simulate
		//check participants 
		int sum = 0;
		for (int i = 1; i <=3; ++i) {
			int amount = (int)sim.getParticipantToRecycleAmount("sim1","part"+i);
			sum += amount;
			if (amount > 0) {
				assertEquals("wrong amount at the recycle list", 10, amount);
			}
		}
		assertEquals("wrong amount at the total recycle list", 10, sum);
    }
	
	// make sure a transaction stays in the channel if it has no where to send it to.
	@Test
    public void SimulatorTestDriverTest3() {
		SimulatorTestDriver sim = new SimulatorTestDriver();
        //create the simulator
		sim.createSimulator("sim1");
		
		//add channels
		sim.addChannel("sim1", "chann0", 10000);
		
		//add participants
		sim.addParticipant("sim1", "part1", "COWS", 45);
		
		//create one transaction
		Transaction tx0 = new Transaction ("COWS", 10);
		
		sim.sendTransaction("sim1","chann0", tx0);

		// simulate
		sim.simulate("sim1");
		
		//check simulate
		//check participants
		assertEquals("wrong amount at the storge buffer", 0, (int)sim.getParticipantStorageAmount("sim1","part1"));
		assertEquals("wrong amount at the recycle list", 0, (int)sim.getParticipantToRecycleAmount("sim1","part1"));
		//check channels
		assertEquals("wrong contents list","10",sim.listContents("sim1", "chann0"));
	}
	
	// send a transaction from a large channel, through a participant,
	// to a smaller channel, and see if it was partially passed (and the rest deleted)
	@Test
    public void SimulatorTestDriverTest4() {
		SimulatorTestDriver sim = new SimulatorTestDriver();
        //create the simulator
		sim.createSimulator("sim1");
		
		//add channels
		sim.addChannel("sim1", "chann0", 100);
		sim.addChannel("sim1", "chann1", 50);
		
		//add participants
		sim.addParticipant("sim1", "part1", "BANANAS", 20);
		
		//connect channels and participants
		sim.addEdge("sim1", "chann0", "part1", "e01");
		sim.addEdge("sim1", "part1", "chann1", "e10");
		
		// pushes transactions into the initial channel
		
		Transaction tx0 = new Transaction ("BANANAS", 100);
		
		sim.sendTransaction("sim1","chann0", tx0);

		// simulate 
		sim.simulate("sim1");
		
		//check simulate 
		//check participants 
		assertEquals("wrong amount at the storge buffer", 20, (int)sim.getParticipantStorageAmount("sim1","part1"));
		assertEquals("wrong amount at the recycle list", 0, (int)sim.getParticipantToRecycleAmount("sim1","part1"));
		//check channels
		assertEquals("wrong contents list","",sim.listContents("sim1", "chann0"));
		assertEquals("wrong contents list","50",sim.listContents("sim1", "chann1"));
		
    }
	
	//check the full flow
	@Test
    public void SimulatorTestDriverTest5() {
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
		
		// pushes transactions into the initial channel
		
		//less than required
		Transaction tx0 = new Transaction ("BANANAS", 10);
		//more  than required
		Transaction tx1 = new Transaction ("SOCKS", 700);
		Transaction tx2 = new Transaction ("IPHONES", 1500);
		//not required
		Transaction tx3 = new Transaction ("COWS", 1050);
		
		sim.sendTransaction("sim1","chann0", tx0);
		sim.sendTransaction("sim1","chann0", tx1);
		sim.sendTransaction("sim1","chann0", tx2);
		sim.sendTransaction("sim1","chann0", tx3);

		// simulate
		
		for (int i = 0; i < 15; ++i) {
			sim.simulate("sim1");
		}
		
		//check simulate
		//check participants 
		assertEquals("wrong amount at the storge buffer", 10, (int)sim.getParticipantStorageAmount("sim1","part1"));
		assertEquals("wrong amount at the recycle list", 0, (int)sim.getParticipantToRecycleAmount("sim1","part1"));
		assertEquals("wrong amount at the storge buffer", 70, (int)sim.getParticipantStorageAmount("sim1","part2"));
		assertEquals("wrong amount at the recycle list", 0, (int)sim.getParticipantToRecycleAmount("sim1","part2"));
		assertEquals("wrong amount at the storge buffer", 1000, (int)sim.getParticipantStorageAmount("sim1","part3"));
		assertEquals("wrong amount at the recycle list", 2180, (int)sim.getParticipantToRecycleAmount("sim1","part3"));
		//check channels
		assertEquals("wrong contents list","",sim.listContents("sim1", "chann0"));
		assertEquals("wrong contents list","",sim.listContents("sim1", "chann1"));
		assertEquals("wrong contents list","",sim.listContents("sim1", "chann2"));
    }

	// check add/remove methods of Simulator
	@Test
	public void SimulatorTest1() {
		Simulator<String, Transaction> sim = new Simulator<>();
		sim.addFilter("channel1", new Channel("c1", 100));
		sim.addFilter("channel2", new Channel("c2", 200));
		sim.addPipe("pipe1", new Participant("p1", "CORN", 100));
		sim.addPipe("pipe2", new Participant("p2", "CANDYCORN", 100));
		
		assertEquals("fail in addFilter", "[channel1, channel2]",sim.getFiltersList().toString());
		assertEquals("fail in addPipe", "[pipe1, pipe2]",sim.getPipesList().toString());
		
		// remove c2 and p2 and check again
		sim.removeFilter("channel2");
		sim.removePipe("pipe2");
		assertEquals("fail in removeFilter", "[channel1]",sim.getFiltersList().toString());
		assertEquals("fail in removePipe", "[pipe1]",sim.getPipesList().toString());
		
		// check connect/disconnect
		sim.connect("c1p1", "channel1", "pipe1");
		assertEquals("channel1 to pipe1 connection failed", "[pipe1]", sim.getListChildren("channel1").toString());
		assertEquals("channel1 to pipe1 connection failed", "[channel1]", sim.getListParents("pipe1").toString());
		sim.disconnect("channel1", "pipe1");
		assertNull("channel1 to pipe1 disconnection failed", sim.getListChildren("channel1"));
		assertNull("channel1 to pipe1 disconnection failed", sim.getListParents("pipe1"));
		
	}
	
	// check testDriver assumptions which are not required by Simulator
	@Test
	public void SimulatorTest2() {
		Simulator<String, Transaction> sim = new Simulator<>();
		assertTrue("failed to insert viable filter", sim.addFilter("channel1", new Channel("c1", 100)));
		assertFalse("added 2 filters with same name", sim.addFilter("channel1", new Channel("c2", 200)));
		assertTrue("failed to insert viable pipe", sim.addPipe("pipe1", new Participant("p1", "CORN", 100)));
		assertFalse("added 2 pipes with the same name", sim.addPipe("pipe1", new Participant("p2", "CANDYCORN", 100)));
		assertFalse("added a pipe and channel with the same name", sim.addPipe("channel1", new Participant("p3", "SUGAR", 100)));
		assertNull("getFilterObj with null id did not return null", sim.getFilterObject(null));
		assertNull("getPipeObj with null id did not return null", sim.getPipeObject(null));
	}
}