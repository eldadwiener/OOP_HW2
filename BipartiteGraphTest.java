package homework2;

import static org.junit.Assert.*;
import org.junit.Test;


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
	public void test1() {
        BipartiteGraphTestDriver driver = new BipartiteGraphTestDriver();
		driver.createGraph("graph1");
		driver.createGraph("graph2");
		
		// try adding nodes with same id
        driver.addBlackNode("graph1", "b1");
        driver.addWhiteNode("graph1", "b1");
        // make sure only the black node was added
        assertEquals("two nodes with same id", "", driver.listWhiteNodes("graph1"));
        assertEquals("missing black node b1", "b1", driver.listBlackNodes("graph1"));
        
        
        // try connecting two nodes of the same color
        driver.addBlackNode("graph1", "b2");
        driver.addWhiteNode("graph1", "w1");
        driver.addWhiteNode("graph1", "w2");
        driver.addEdge("graph1", "b1", "b2", "b2b");
        driver.addEdge("graph1", "w1", "w2", "w2w");
        assertEquals("edge between same color", "", driver.listChildren("graph1", "b1") );
        assertEquals("edge between same color", "", driver.listChildren("graph1", "w1") );
        
        // make sure if edge1 is child of edge2, then edge2 is parent of edge1
        driver.addEdge("graph1", "b1", "w1", "b2w");
        assertEquals("Child-Parent relationship broken", "w1", driver.listChildren("graph1", "b1") );
        assertEquals("Child-Parent relationship broken", "b1", driver.listParents("graph1", "w1") );
	}
  
}
