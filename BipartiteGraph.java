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
	
	// Abs. Function:
	// represents a BipartiteGraph with:
	//		black nodes and white nodes, with: {id, object and edges list} at nodes list
	//		where the entry.key = nodeId, and entry.value = node
	// Rep. Invariant:
	// for every <key,value> couple in nodes:
	// 		key != null, value != null
	//		unique id for every node
	//		value.type != value.parent.type for every parent
	//		value.type != value.children.type for every children
	//		for every nodes a,b: a is in b.parents iff b in a.children
	
	private Map<T,Node<T>> nodes = new HashMap<>();
	
	/**
	 * @modifies this
	 * @effects adds a new node with id = nodeId, type = type, containing obj
	 * 			if a node with id = nodeId already exists, the new node won't be added.
	 * @return true if the node was added.
	 * 		   false otherwise.
	 */
	public boolean addNode (T nodeId, Node.NodeType type, Object obj) {
		checkRep();
		if ( nodes.containsKey(nodeId)) {
			return false;
		}
		nodes.put(nodeId, new Node<T>(nodeId, type,obj));
		checkRep();
		return true;
	}
	
	/**
	 * @modifies this, srcNode and dstNode objects
	 * @effects 
	 * @return
	 */
	public boolean addEdge (T edgeId, T srcNodeId, T dstNodeId) {
		checkRep();
		Node<T> srcNode = nodes.get(srcNodeId);
		Node<T> dstNode = nodes.get(dstNodeId);
		if (srcNode == null || dstNode == null) {
			checkRep();
			return false;
		}
		// both nodes exist, make sure they are of different types
		if ( srcNode.getType() == dstNode.getType() ) {
			checkRep();
			return false;
		}
		// types are different, try adding the edge to both sides
		if ( !srcNode.insertChildEdge(edgeId, dstNodeId) ) {
			checkRep();
			return false;
		}
		
		if ( !dstNode.insertParentEdge(edgeId, srcNodeId) ) {
			// failed to add to destination, remove new edge from source
			srcNode.removeChildEdge(edgeId);
			checkRep();
			return false;
		}
		checkRep();
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
		checkRep();
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
		checkRep();
		return nodes.containsKey(nodeId);
	}
	
	/**
	 * @requires
	 * @modifies 
	 * @effects
	 * @return
	 */
	public Object getNodeObj (T nodeId) {
		checkRep();
		return nodes.get(nodeId).getObject();
	}
	
	/**
	 * @requires
	 * @modifies 
	 * @effects
	 * @return
	 */
	public void removeNode (T nodeId) {
		checkRep();
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
		checkRep();
	}
	
	/**
	 * @requires
	 * @modifies 
	 * @effects
	 * @return
	 */
	public void removeEdge (T srcNodeId, T dstNodeId) {
		checkRep();
		Node<T> srcNode = nodes.get(srcNodeId);
		Node<T> dstNode = nodes.get(dstNodeId);
		if (srcNode == null || dstNode == null) {
			return;
		}
		srcNode.removeChild(dstNodeId);
		dstNode.removeParent(srcNodeId);
		checkRep();
	}
	
	/**
	 * @requires
	 * @modifies 
	 * @effects
	 * @return
	 */
	public List<T> getNodesByType(Node.NodeType type) {
		checkRep();
		List<T> nodesList = new ArrayList<>();
		for (Map.Entry<T, Node<T>> entry : nodes.entrySet()) {
			if (entry.getValue().getType() == type) {
				nodesList.add(entry.getKey());
			}
		}
		checkRep();
		return nodesList;
	}
	
	private void checkRep() {
		for (Map.Entry<T, Node<T>> entry : nodes.entrySet()) {
			//check for: "key != null, value != null"
			assert entry.getKey() != null : "entry.getKey() == null";
			Node<T> checkedNode = entry.getValue();
			assert checkedNode != null : "checkedNode == null";
			
			//check for: for every nodes a,b: a is in b.parents iff b in a.children
			
			//check for: value.type != value.parent.type for every parent
			Collection <T> childs = checkedNode.getAllChildren();
			for (T nodeId : childs) {
				assert nodes.get(nodeId).getType() != checkedNode.getType() : "children type = parent type (not a BipartiteGraph)";
				assert nodes.get(nodeId).isChild(checkedNode.getId()) : "missing edge between parent and child";
			}

			//check for: value.type != value.children.type for every children
			Collection <T> parents = checkedNode.getAllParents();
			for (T nodeId : parents) {
				assert nodes.get(nodeId).getType() != checkedNode.getType() : "parent type = children type (not a BipartiteGraph)";
				assert nodes.get(nodeId).isParent(checkedNode.getId()) : "missing edge between parent and child";
			}
		}
	}
}
