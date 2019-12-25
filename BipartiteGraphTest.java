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
    
    
    //  TODO: Add black-box tests
    
	@Test
	public void blackBoxDriverTest() {
        BipartiteGraphTestDriver driver = new BipartiteGraphTestDriver();
		driver.createGraph("graph1");
		
        driver.addBlackNode("graph1", "b1");
        driver.addBlackNode("graph1", "b2");
        driver.addWhiteNode("graph1", "w1");
        driver.addWhiteNode("graph1", "w2");

        driver.addEdge("graph1", "b1", "w1", "b2w");
        assertEquals("addEdge failed", "w1", driver.listChildren("graph1", "b1") );
        assertEquals("addEdge failed", "b1", driver.listParents("graph1", "w1") );
        
        // connect in and out edges with same name
        driver.addEdge("graph1", "w1", "b1", "b2w");
        assertEquals("in and out same name failed","b1" , driver.getChildByEdgeLabel("graph1", "w1", "b2w"));
        
        // same name edge different src and dst
        driver.addEdge("graph1", "b2", "w2", "b2w");
        assertEquals("same name edge different src-dst", "w2", driver.getChildByEdgeLabel("graph1", "b2", "b2w"));
	}

	@Test
	public void blackBoxNoDriverTest() {
		BipartiteGraph<String> graph = new BipartiteGraph<>();
		graph.addNode("b1", NodeType.BLACK, "blackNode1");
		graph.addNode("b2", NodeType.BLACK, "blackNode2");
		graph.addNode("w1", NodeType.WHITE, "whiteNode1");
		graph.addNode("w2", NodeType.WHITE, "whiteNode2");
		
		graph.addEdge("b2w", "b1", "w1");
		
		assertEquals("failed in contains edge", true, graph.containsEdge("b1", "w1"));
		assertEquals("failed in contains edge", false, graph.containsEdge("w1", "b1"));
		
        // make sure if edge1 is child of edge2, then edge2 is parent of edge1
        assertEquals("Child-Parent relationship broken", "w1", graph.getChildByEdgeLabel("b1", "b2w") );
        assertEquals("Child-Parent relationship broken", "b1", graph.getParentByEdgeLabel("w1", "b2w") );
		
		// try adding nodes with same id
		graph.addNode("b1", NodeType.WHITE, "failedwhiteNode1");
        // make sure only the black node was added
        assertEquals("two nodes with same id", false, graph.getNodesByType(NodeType.WHITE).contains("b1"));
        
        // try connecting two nodes of the same color
        graph.addEdge("b2b", "b1", "b2");
        graph.addEdge("w2w", "w1", "w2");
        assertEquals("edge between same color", false, graph.containsEdge("b1", "b2") );
        assertEquals("edge between same color", false, graph.containsEdge("w1", "w2") );
        
        
        // connect edges to non-existent nodes 
        graph.addEdge("nah", "b1", "nope");
        graph.addEdge("nun", "stillNope", "b1");
        assertEquals("connected to non-existent edge",null ,graph.getChildByEdgeLabel("b1", "nah"));
        assertEquals("connected to non-existent edge",null ,graph.getParentByEdgeLabel("b1", "nun"));
        
        // connect 2 edges with same id
        graph.addEdge("b2w", "b1","w2");
        assertEquals("2 edges same id", null, graph.getParentByEdgeLabel("w2", "b2w") );
        
        // connect in and out edges with same name
        graph.addEdge("b2w", "w1", "b1");
        assertEquals("in and out same name failed", "b1", graph.getChildByEdgeLabel("w1", "b2w"));
        
        // add 2 edges with same sides, and different names
        graph.addEdge("w2b",  "w1", "b1");
        assertEquals("same parent-child different name edge", null, graph.getChildByEdgeLabel("w1", "w2b"));
        assertEquals("same parent-child different name edge", "b1", graph.getChildByEdgeLabel("w1", "b2w"));
        
        // same name edge different src and dst
        graph.addEdge("b2w", "b2", "w2");
        assertEquals("same name edge different src-dst", "w2", graph.getChildByEdgeLabel("b2", "b2w"));
	}
}
