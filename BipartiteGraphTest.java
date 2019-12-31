package homework2;

import static org.junit.Assert.*;
import org.junit.Test;

import homework2.BipartiteGraph.NodeType;


/**
 * BipartiteGraphTest contains JUnit block-box unit tests for BipartiteGraph.
 */
public class BipartiteGraphTest {

	@Test
    public void testExample() {
        BipartiteGraphTestDriver driver = new BipartiteGraphTestDriver();
        
        //create a graph
        driver.createGraph("graph1");
        
        //add a pair of nodes
        driver.addBlackNode("graph1", "n1");
        driver.addWhiteNode("graph1", "n2");
        
        //add an edge
        driver.addEdge("graph1", "n1", "n2", "edge");
        
        //check neighbors
        assertEquals("wrong black nodes", "n1", driver.listBlackNodes("graph1"));
        assertEquals("wrong white nodes", "n2", driver.listWhiteNodes("graph1"));
        assertEquals("wrong children", "n2", driver.listChildren ("graph1", "n1"));
        assertEquals("wrong children", "", driver.listChildren ("graph1", "n2"));
        assertEquals("wrong parents", "", driver.listParents ("graph1", "n1"));
        assertEquals("wrong parents", "n1", driver.listParents ("graph1", "n2"));
    }
    
    
    
	// test the BipartiteGraph using the test driver
	// try adding nodes and connecting edges, make sure the graph state is correct.
	@Test
	public void BipartiteGraphTestDriverTest1() {
        BipartiteGraphTestDriver driver = new BipartiteGraphTestDriver();
		driver.createGraph("graph1");
		
        driver.addBlackNode("graph1", "b1");
        driver.addBlackNode("graph1", "b2");
        driver.addWhiteNode("graph1", "w1");
        driver.addWhiteNode("graph1", "w2");

        assertEquals("addBlackNode failed", "b1 b2", driver.listBlackNodes("graph1"));
        assertEquals("addWhitWhite failed", "w1 w2", driver.listWhiteNodes("graph1"));

        driver.addEdge("graph1", "b1", "w1", "b2w");
        assertEquals("addEdge failed", "w1", driver.listChildren("graph1", "b1") );
        assertEquals("addEdge failed", "b1", driver.listParents("graph1", "w1") );
        
        // connect in and out edges with same name
        driver.addEdge("graph1", "w1", "b1", "b2w");
        assertEquals("in and out same name failed","b1" , driver.getChildByEdgeLabel("graph1", "w1", "b2w"));
        assertEquals("in and out same name failed","w1" , driver.getParentByEdgeLabel("graph1", "b1", "b2w"));
        
        // same name edge different src and dst
        driver.addEdge("graph1", "b2", "w2", "b2w");
        assertEquals("same name edge different src-dst", "w2", driver.getChildByEdgeLabel("graph1", "b2", "b2w"));
        assertEquals("same name edge different src-dst", "b2", driver.getParentByEdgeLabel("graph1", "w2", "b2w"));
	}

	// tests without test driver
	// try connecting an edge, see if it exists, and if both sides are aware of it.
	@Test
	public void BipartiteGraphTest1() {
		BipartiteGraph<String> graph = new BipartiteGraph<>();
		graph.addNode("b1", NodeType.BLACK, "blackNode1");
		graph.addNode("w1", NodeType.WHITE, "whiteNode1");
		
		graph.addEdge("b2w", "b1", "w1");
		
		assertEquals("failed in contains edge", true, graph.containsEdge("b1", "w1"));
		assertEquals("failed in contains edge", false, graph.containsEdge("w1", "b1"));
		
        // make sure if edge1 is child of edge2, then edge2 is parent of edge1
        assertEquals("Child-Parent relationship broken", "w1", graph.getChildByEdgeLabel("b1", "b2w") );
        assertEquals("Child-Parent relationship broken", "b1", graph.getParentByEdgeLabel("w1", "b2w") );
	}
	
	// try adding nodes with same id
	@Test
	public void BipartiteGraphTest2() {
		BipartiteGraph<String> graph = new BipartiteGraph<>();
		graph.addNode("b1", NodeType.BLACK, "blackNode1");
		graph.addNode("w1", NodeType.WHITE, "whiteNode1");
		graph.addNode("w1", NodeType.BLACK, "blackNode2");
		graph.addNode("b1", NodeType.WHITE, "whiteNode2");
		
        // make sure only the first two nodes were added
        assertFalse("two nodes with same id", graph.getNodesByType(NodeType.WHITE).contains("b1"));
        assertFalse("two nodes with same id", graph.getNodesByType(NodeType.BLACK).contains("w1"));
        
	}
	
	// try connecting two nodes of the same color
	@Test
	public void BipartiteGraphTest3() {
		BipartiteGraph<String> graph = new BipartiteGraph<>();
		graph.addNode("b1", NodeType.BLACK, "blackNode1");
		graph.addNode("b2", NodeType.BLACK, "blackNode2");
		graph.addNode("w1", NodeType.WHITE, "whiteNode1");
		graph.addNode("w2", NodeType.WHITE, "whiteNode2");
		
        graph.addEdge("b2b", "b1", "b2");
        graph.addEdge("w2w", "w1", "w2");
        assertFalse("edge between same color", graph.containsEdge("b1", "b2") );
        assertFalse("edge between same color", graph.containsEdge("w1", "w2") );
	}
	
	// connect edges to non-existent nodes 
	@Test
	public void BipartiteGraphTest4() {
		BipartiteGraph<String> graph = new BipartiteGraph<>();
		graph.addNode("b1", NodeType.BLACK, "blackNode1");
        graph.addEdge("nah", "b1", "nope");
        graph.addEdge("nun", "stillNope", "b1");
        assertNull("connected to non-existent edge", graph.getChildByEdgeLabel("b1", "nah"));
        assertNull("connected to non-existent edge", graph.getParentByEdgeLabel("b1", "nun"));
	}
	
	// connect 2 edges with same id (ingoing and outgoing)
	@Test
	public void BipartiteGraphTest5() {
		BipartiteGraph<String> graph = new BipartiteGraph<>();
		graph.addNode("b1", NodeType.BLACK, "blackNode1");
		graph.addNode("b2", NodeType.BLACK, "blackNode2");
		graph.addNode("w1", NodeType.WHITE, "whiteNode1");
		graph.addNode("w2", NodeType.WHITE, "whiteNode2");
		
		graph.addEdge("b2w", "b1", "w1");
        graph.addEdge("b2w", "b1", "w2");
        graph.addEdge("b2w", "b2", "w1");
        // outgoing conflict
        assertNull("2 edges same id", graph.getParentByEdgeLabel("w2", "b2w") );
        // ingoing conflict
        assertNull("2 edges same id", graph.getChildByEdgeLabel("b2", "b2w") );
	}
	
	// connect in and out edges with same name
	@Test
	public void BipartiteGraphTest6() {
		BipartiteGraph<String> graph = new BipartiteGraph<>();
		graph.addNode("b1", NodeType.BLACK, "blackNode1");
		graph.addNode("w1", NodeType.WHITE, "whiteNode1");
		
		graph.addEdge("b2w", "b1", "w1");
        graph.addEdge("b2w", "w1", "b1");
        assertEquals("in and out same name failed", "b1", graph.getChildByEdgeLabel("w1", "b2w"));
	}
	
	// add 2 edges with same sides, and different names
	@Test
	public void BipartiteGraphTest7() {
		BipartiteGraph<String> graph = new BipartiteGraph<>();
		graph.addNode("b1", NodeType.BLACK, "blackNode1");
		graph.addNode("w1", NodeType.WHITE, "whiteNode1");
		
		graph.addEdge("b2w", "b1", "w1");
        graph.addEdge("w2b", "b1", "w1");
        assertNull("same parent-child different name edge", graph.getChildByEdgeLabel("b1", "w2b"));
        assertEquals("same parent-child different name edge", "w1", graph.getChildByEdgeLabel("b1", "b2w"));
	}
	
	// same name edge different src and dst
	@Test
	public void BipartiteGraphTest8() {
		BipartiteGraph<String> graph = new BipartiteGraph<>();
		graph.addNode("b1", NodeType.BLACK, "blackNode1");
		graph.addNode("b2", NodeType.BLACK, "blackNode2");
		graph.addNode("w1", NodeType.WHITE, "whiteNode1");
		graph.addNode("w2", NodeType.WHITE, "whiteNode2");
		
		graph.addEdge("b2w", "b1", "w1");
        graph.addEdge("b2w", "b2", "w2");
        assertEquals("same name edge different src-dst", "w1", graph.getChildByEdgeLabel("b1", "b2w"));
        assertEquals("same name edge different src-dst", "w2", graph.getChildByEdgeLabel("b2", "b2w"));
	}
}
