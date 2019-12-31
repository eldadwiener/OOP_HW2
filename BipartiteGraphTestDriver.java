package homework2;

import java.util.*;

import homework2.BipartiteGraph.NodeType;

/**
 * This class implements a testing driver for BipartiteGraph. The driver
 * manages BipartiteGraphs whose nodes and edges are Strings.
 */
public class BipartiteGraphTestDriver {

    private Map<String, BipartiteGraph<String>> graphs;

    /**
     * @modifies this
     * @effects Constructs a new test driver.
     */
    public BipartiteGraphTestDriver () {
    	graphs = new HashMap<>();
    }

    
    /**
     * @requires graphName != null
     * @modifies this
     * @effects Creates a new graph named graphName. The graph is initially
     * 			empty.
     */
    public void createGraph(String graphName) {
        if (graphs.containsKey(graphName)) {
        	return;
        }
        
        graphs.put(graphName, new BipartiteGraph<String>());
    }

    
    /**
     * @requires createGraph(graphName)
     *           && nodeName != null
     *           && neither addBlackNode(graphName,nodeName) 
     *                  nor addWhiteNode(graphName,nodeName)
     *                      has already been called on this
     * @modifies graph named graphName
     * @effects Adds a black node represented by the String nodeName to the
     * 			graph named graphName.
     */
    public void addBlackNode(String graphName, String nodeName) {
    	BipartiteGraph<String> graph = graphs.get(graphName);
    	graph.addNode(nodeName, NodeType.BLACK, null);
    }

    
    /**
     * @requires createGraph(graphName)
     *           && nodeName != null
     *           && neither addBlackNode(graphName,nodeName) 
     *                  nor addWhiteNode(graphName,nodeName)
     *                      has already been called on this
     * @modifies graph named graphName
     * @effects Adds a white node represented by the String nodeName to the
     * 			graph named graphName.
     */
    public void addWhiteNode(String graphName, String nodeName) {
    	BipartiteGraph<String> graph = graphs.get(graphName);
    	graph.addNode(nodeName, NodeType.WHITE, null);
    }

    
    /**
     * @requires createGraph(graphName)
     *           && ((addBlackNode(parentName) && addWhiteNode(childName))
     *              || (addWhiteNode(parentName) && addBlackNode(childName)))
     *           && edgeLabel != null
     *           && node named parentName has no other outgoing edge labeled
     * 				edgeLabel
     *           && node named childName has no other incoming edge labeled
     * 				edgeLabel
     * @modifies graph named graphName
     * @effects Adds an edge from the node parentName to the node childName
     * 			in the graph graphName. The new edge's label is the String
     * 			edgeLabel.
     */
    public void addEdge(String graphName,
    					String parentName, String childName, 
                        String edgeLabel) {
    	BipartiteGraph<String> graph = graphs.get(graphName);
    	graph.addEdge(edgeLabel, parentName, childName);
    }

    
    /**
     * @requires createGraph(graphName)
     * @return a space-separated list of the names of all the black nodes
     * 		   in the graph graphName, in alphabetical order.
     */
    public String listBlackNodes(String graphName) {
    	BipartiteGraph<String> graph = graphs.get(graphName);
    	List<String> blackNodes = graph.getNodesByType(NodeType.BLACK);
    	Collections.sort(blackNodes);
    	return String.join(" ", blackNodes);
    }

    
    /**
     * @requires createGraph(graphName)
     * @return a space-separated list of the names of all the white nodes
     * 		   in the graph graphName, in alphabetical order.
     */
    public String listWhiteNodes(String graphName) {
    	BipartiteGraph<String> graph = graphs.get(graphName);
    	List<String> whiteNodes = graph.getNodesByType(NodeType.WHITE);
    	Collections.sort(whiteNodes);
    	return String.join(" ", whiteNodes);
    }

    
    /**
     * @requires createGraph(graphName) && createNode(parentName)
     * @return a space-separated list of the names of the children of
     * 		   parentName in the graph graphName, in alphabetical order.
     */
    public String listChildren(String graphName, String parentName) {
    	BipartiteGraph<String> graph = graphs.get(graphName);
    	Collection<String> childrenList = graph.getListChildren(parentName);
    	if (childrenList == null) {
    		return "";
    	}
    	List<String> sortedChildrenList = new ArrayList<>(childrenList);
    	Collections.sort(sortedChildrenList);
    	return String.join(" ", sortedChildrenList);
    }

    
    /**
     * @requires createGraph(graphName) && createNode(childName)
     * @return a space-separated list of the names of the parents of
     * 		   childName in the graph graphName, in alphabetical order.
     */
    public String listParents(String graphName, String childName) {
       	BipartiteGraph<String> graph = graphs.get(graphName);
    	Collection<String> parentsList = graph.getListParents(childName);
    	if (parentsList == null) {
    		return "";
    	}
    	List<String> sortedParentsList = new ArrayList<>(parentsList);
    	Collections.sort(sortedParentsList);
    	return String.join(" ", sortedParentsList);
    }

    
    /**
     * @requires addEdge(graphName, parentName, str, edgeLabel) for some
     * 			 string str
     * @return the name of the child of parentName that is connected by the
     * 		   edge labeled edgeLabel, in the graph graphName.
     */
    public String getChildByEdgeLabel(String graphName, String parentName,
    								   String edgeLabel) {
    	BipartiteGraph<String> graph = graphs.get(graphName);
    	return graph.getChildByEdgeLabel(parentName, edgeLabel);
    }

    
    /**
     * @requires addEdge(graphName, str, childName, edgeLabel) for some
     * 			 string str
     * @return the name of the parent of childName that is connected by the 
     * 		   edge labeled edgeLabel, in the graph graphName.
     */
    public String getParentByEdgeLabel(String graphName, String childName,
    									String edgeLabel) {
    	BipartiteGraph<String> graph = graphs.get(graphName);
    	return graph.getParentByEdgeLabel(childName, edgeLabel);
    }
}
