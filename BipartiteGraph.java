package homework2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Implements a bipartite graph representation, consisting of labeled nodes and edges.
 * Every node in the graph must have a unique label.
 * Two edges with the same label cannot have the same source edge, or the same destination edge.
 * Every node has a color: BLACK/WHITES, an edge cannot directly connect nodes of the same color.
 */
public class BipartiteGraph<T extends Comparable<T>> {
	
	private Map<T,Node<T>> nodes = new HashMap<>();
	
	/**
	 * @modifies this
	 * @effects adds a new node with id = nodeId, type = type, containing obj
	 * 			if a node with id = nodeId already exists, the new node won't be added.
	 * @return true if the node was added.
	 * 		   false otherwise.
	 */
	public boolean addNode (T nodeId, Node.NodeType type, Object obj) {
		if ( nodes.containsKey(nodeId)) {
			return false;
		}
		nodes.put(nodeId, new Node<T>(nodeId, type,obj));
		return true;
	}
	
	/**
	 * @modifies this, srcNode and dstNode objects
	 * @effects 
	 * @return
	 */
	public boolean addEdge (T edgeId, T srcNodeId, T dstNodeId) {
		Node<T> srcNode = nodes.get(srcNodeId);
		Node<T> dstNode = nodes.get(dstNodeId);
		if (srcNode == null || dstNode == null) {
			return false;
		}
		// both nodes exist, make sure they are of different types
		if ( srcNode.getType() == dstNode.getType() ) {
			return false;
		}
		// types are different, try adding the edge to both sides
		if ( !srcNode.insertChildEdge(edgeId, dstNodeId) ) {
			return false;
		}
		
		if ( !dstNode.insertParentEdge(edgeId, srcNodeId) ) {
			// failed to add to destination, remove new edge from source
			srcNode.removeChildEdge(edgeId);
			return false;
		}
		// managed to add the edge on both sides, so it is a legal edge
		return true;
	}
	
	/**
	 * @requires
	 * @modifies 
	 * @effects
	 * @return
	 */
	public boolean containsEdge (T srcNodeId, T dstNodeId) {
		Node<T> srcNode = nodes.get(srcNodeId);
		if (srcNode != null) {
			return srcNode.isChild(dstNodeId);
		}
		return false;
	}
	
	/**
	 * @requires
	 * @modifies 
	 * @effects
	 * @return
	 */
	public boolean containsNode (T nodeId) {
		return nodes.containsKey(nodeId);
	}
	
	/**
	 * @requires
	 * @modifies 
	 * @effects
	 * @return
	 */
	public Object getNodeObj (T nodeId) {
		return nodes.get(nodeId).getObject();
	}
	
	/**
	 * @requires
	 * @modifies 
	 * @effects
	 * @return
	 */
	public void removeNode (T nodeId) {
		Node<T> removedNode = nodes.get(nodeId);
		if (removedNode == null) {
			return;
		}
		// node exists, start cleaning up all it's edges
		// remove all child node edges
		Collection<T> children = removedNode.getAllChildren();
		for (T child : children) {
			nodes.get(child).removeParent(nodeId);
		}
		
		// remove all parent node edges
		Collection<T> parents = removedNode.getAllParents();
		for (T parent : parents) {
			nodes.get(parent).removeChild(nodeId);
		}
		
		// done cleaning up, remove the node
		nodes.remove(nodeId);
	}
	
	/**
	 * @requires
	 * @modifies 
	 * @effects
	 * @return
	 */
	public void removeEdge (T srcNodeId, T dstNodeId) {
		Node<T> srcNode = nodes.get(srcNodeId);
		Node<T> dstNode = nodes.get(dstNodeId);
		if (srcNode == null || dstNode == null) {
			return;
		}
		srcNode.removeChild(dstNodeId);
		dstNode.removeParent(srcNodeId);
	}
	
	/**
	 * @requires
	 * @modifies 
	 * @effects
	 * @return
	 */
	public List<T> getNodesByType(Node.NodeType type) {
		List<T> nodesList = new ArrayList<>();
		for (Map.Entry<T, Node<T>> entry : nodes.entrySet()) {
			if (entry.getValue().getType() == type) {
				nodesList.add(entry.getKey());
			}
		}
		return nodesList;
	}
}
