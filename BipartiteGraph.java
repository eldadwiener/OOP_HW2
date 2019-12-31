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
public class BipartiteGraph<T> {
	
	// Abs. Function:
	// represents a BipartiteGraph with:
	//		black nodes and white nodes, with: {id, object and edges list} at this.nodes list
	//		where the entry.key = nodeId, and entry.value = node
	// Rep. Invariant:
	// for every <key,value> couple in nodes:
	// 		key != null, value != null
	//		unique id for every node
	//		value.type != value.parent.type for every parent
	//		value.type != value.children.type for every children
	//		for every nodes a,b: a is in b.parents iff b in a.children
	
	public enum NodeType{ BLACK, WHITE };
	
	private Map<T,Node<T>> nodes = new HashMap<>();
	
	/**
	 * @modifies this
	 * @effects adds a new node with id = nodeId, type = type, containing obj
	 * 			if a node with id = nodeId already exists, the new node won't be added.
	 * @return true if the node was added.
	 * 		   false otherwise.
	 */
	public boolean addNode (T nodeId, NodeType type, Object obj) {
		checkRep();
		if ( nodes.containsKey(nodeId)) {
			return false;
		}
		nodes.put(nodeId, new Node<T>(nodeId, type,obj));
		checkRep();
		return true;
	}
	
	/**
	 * @modifies this
	 * @effects adds an edge with id edgeId from srcNodeId to dstNodeId
	 * 			if an edge from the source node to the destination node exists already,
	 * 			or if edgeId was already used in an edge outgoing from source, or incoming to destination,
	 * 			then the edge will not be added.
	 * @return true if the edge was added,
	 * 		   false if the edge was not added.
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
	 * @effects check if the graph contains an edge from srcNode to dstNode.
	 * @return true if the edge exists, otherwise false.
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
	 * @effects check if the graph contains a node with id nodeId.
	 * @return true if the node exists, false otherwise.
	 */
	public boolean containsNode (T nodeId) {
		checkRep();
		return nodes.containsKey(nodeId);
	}
	
	/**
	 * @effects if the graph contains a node with id nodeId, returns the object it contains.
	 * @return object of the node with id nodeId, or null if no such node exists.
	 */
	public Object getNodeObj (T nodeId) {
		checkRep();
		Node<T> node = nodes.get(nodeId);
		if (node == null) {
			return null;
		}
		return node.getObject();
	}
	
	/**
	 * @modifies this
	 * @effects removes the node with id nodeId from the graph,
	 * 			and all of it's outgoing and in going edges.
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
	 * @modifies this
	 * @effects remove the edge between the source and destination nodes if one exists.
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
	 * @effects get a list of all node id's in the graph with type "type"
	 * @return list of node id's
	 */
	public List<T> getNodesByType(NodeType type) {
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
	
	/**
	 * @effects get a parent of a node (detected by edge label)
	 * @return if (childName in nodes and childName is a dest for edgeLabel)
	 * 			return the parent label, otherwise return null
	 */
	public T getParentByEdgeLabel(T childName, T edgeLabel) {
		checkRep();
		Node<T> node = nodes.get(childName);
		if (node == null) {
			checkRep();
			return null;
		}
		checkRep();
		return node.getParentByEdge(edgeLabel);
	}

	/**
	 * @effects get a children of a node (detected by edge label)
	 * @return if (parentName in nodes and parentName is a src for edgeLabel)
	 * 			return the parent label, otherwise return null
	 */
	public T getChildByEdgeLabel(T parentName, T edgeLabel) {
		checkRep();
		Node<T> node = nodes.get(parentName);
		if (node == null) {
			checkRep();
			return null;
		}
		checkRep();
		return node.getChildByEdge(edgeLabel);
	}

	/**
	 * @effects get a children labels list of a node (detected by parentName)
	 * @return if (parentName in nodes)
	 * 			return the children labels list , otherwise return null
	 */
	public Collection<T> getListChildren (T parentName){
		checkRep();
		Node<T> node = nodes.get(parentName);
		if (node == null) {
			checkRep();
			return null;
		}
		Collection<T> children = node.getAllChildren();
		if (children.isEmpty()) {
			children = null;
		}
		checkRep();
		return children;
	}
	
	/**
	 * @effects get a parents labels list of a node (detected by childName)
	 * @return if (childName in nodes)
	 * 			return the parents labels list , otherwise return null
	 */	
	public Collection<T> getListParents (T childName){
		checkRep();
		Node<T> node = nodes.get(childName);
		if (node == null) {
			checkRep();
			return null;
		}
		Collection<T> parents = node.getAllParents();
		if (parents.isEmpty()) {
			parents = null;
		}
		checkRep();
		return parents;
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
				assert nodes.get(nodeId).isParent(checkedNode.getId()) : "missing edge between parent and child";
			}

			//check for: value.type != value.children.type for every children
			Collection <T> parents = checkedNode.getAllParents();
			for (T nodeId : parents) {
				assert nodes.get(nodeId).getType() != checkedNode.getType() : "parent type = children type (not a BipartiteGraph)";
				assert nodes.get(nodeId).isChild(checkedNode.getId()) : "missing edge between parent and child";
			}
		}
	}
}
