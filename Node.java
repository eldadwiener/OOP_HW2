package homework2;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import homework2.BipartiteGraph.NodeType;

/*
 * Implements a generic node for graph , consisting id, type (BLACK or WHITE) and lists for parents and children.
 * Every connection (parent/children) have an edge with unique id in the relevant list(parents/children)
 */
public class Node<T> {
	
	// Abs. Function:
	// represents a generic node with:
	//		type (BLACK or WHITE) at this.type
	//		generic type ID ad this.id
	//		an inside object (optional) for more operations at this.object
	//		map of in-going edges and parents nodes at this.ingoingEdges detected by generic type id
	//		map of outgoing edges and children nodes at this.outgoingEdges detected by generic type id
	// Rep. Invariant:
	//		type != null, id != null
	// 		for every <key,value> couple in ingoingEdges/outgoingEdges:
	// 			key != null, value != null
	//		for (entry1, entry2 : in ingoingEdges)
	//			entry1.key = entry2.key (2 edges with the same id)
	//			or
	//			entry1.value = entry2.value (2 edges with the same src and dst)
	//			iff
	//			entry1 == entry2
	// 		and the same rule for outgoingEdges
	

	private final NodeType type;
	private final T id;
	private final Object object;
	private Map<T,T> ingoingEdges = new HashMap<>();
	private Map<T,T> outgoingEdges = new HashMap<>();

	/**
	 * @modifies this
	 * @effects creates a new node with label = id, of type = type containing the object obj
	 */
	public Node(T id, NodeType type, Object obj) {
		this.type = type;
		this.id = id;
		object = obj;
		checkRep();
	}
	
	/**
	 * @modifies this
	 * @effects inserts an edge from this node to the node with id childId.
	 * 			if an edge to childId, or a child edge with edgeId already exists, no edge will be added.
	 * @return true if the edge was added, otherwise false.
	 */
	public boolean insertChildEdge(T edgeId, T childId) {
		checkRep();
		if ( outgoingEdges.containsKey(edgeId) || outgoingEdges.containsValue(childId) ) {
			checkRep();
			return false;
		}
		outgoingEdges.put(edgeId, childId);
		checkRep();
		return true;
	}
	
	/**
	 * @modifies this
	 * @effects inserts an edge from the node with id parentId, to this node.
	 * 			if an edge to parentId, or a parent edge with edgeId already exists, no edge will be added.
	 * @return true if the edge was added, otherwise false.
	 */
	public boolean insertParentEdge(T edgeId, T parentId) {
		checkRep();
		if ( ingoingEdges.containsKey(edgeId) || ingoingEdges.containsValue(parentId)) {
			checkRep();
			return false;
		}
		ingoingEdges.put(edgeId, parentId);
		checkRep();
		return true;
	}

	/**
	 * @modifies this
	 * @effects removes outgoing edge from this to childId if such edge exists.
	 */
	public void removeChild(T childId) {
		checkRep();
		for (Map.Entry<T, T> child : outgoingEdges.entrySet()) {
			if (child.getValue().equals(childId)) {
				outgoingEdges.remove(child.getKey());
				checkRep();
				return;
			}
		}
		checkRep();
	}

	/**
	 * @modifies this
	 * @effects removes in going edge from parentId to this if such edge exists.
	 */
	public void removeParent(T parentId) {
		checkRep();
		for (Map.Entry<T, T> parent : ingoingEdges.entrySet()) {
			if (parent.getValue().equals(parentId)) {
				ingoingEdges.remove(parent.getKey());
				checkRep();
				return;
			}
		}
		checkRep();
	}

	/** 
	 * @modifies this
	 * @effects removes edgeId from child edges if such edge exists.
	 */
	public void removeChildEdge(T edgeId) {
		checkRep();
		outgoingEdges.remove(edgeId);
		checkRep();
	}

	/** 
	 * @modifies this
	 * @effects removes edgeId from parent edges if such edge exists.
	 */
	public void removeParentEdge(T edgeId) {
		checkRep();
		ingoingEdges.remove(edgeId);
		checkRep();
	}

	/**
	 * @return The object that this node contains.
	 */
	public Object getObject() {
		checkRep();
		return object;
	}
	
	/**
	 * @return id of this node.
	 */
	public T getId() {
		checkRep();
		return id;
	}

	/**
	 * @return type of this node.
	 */
	public NodeType getType() {
		checkRep();
		return type;
	}

	/**
	 * @return list of all child nodes of this.
	 */
	public Collection<T> getAllChildren() {
		checkRep();
		return outgoingEdges.values();
	}

	/**
	 * @return list of all parent nodes of this.
	 */
	public Collection<T> getAllParents() {
		checkRep();
		return ingoingEdges.values();
	}
	
	/**
	 * @return nodeId of child node connected by edgeId to this
	 * 		   or null if no such edge exists.
	 */
	public T getChildByEdge(T edgeId) {
		checkRep();
		return outgoingEdges.get(edgeId);
	}
	
	/**
	 * @return nodeId of parent node connected by edgeId to this
	 * 		   or null if no such edge exists.
	 */
	public T getParentByEdge(T edgeId) {
		checkRep();
		return ingoingEdges.get(edgeId);
	}
	
	/**
	 * @return true if nodeId is a child node of this, otherwise false.
	 */
	public boolean isChild(T nodeId) {
		checkRep();
		return outgoingEdges.containsValue(nodeId);
	}
	
	/**
	 * @return true if nodeId is a parent node of this, otherwise false.
	 */
	public boolean isParent(T nodeId) {
		checkRep();
		return ingoingEdges.containsValue(nodeId);
	}

	/**
	 * @effects check if obj is equal to this (same nodeId)
	 * @return true if they are equal, otherwise false.
	 */
	@Override
	public boolean equals(Object obj) {
		checkRep();
		if (obj instanceof Node<?>) {
			if (((Node<?>)obj).id == id) {
				checkRep();
				return true;
			}
		}
		checkRep();
		return false;
	}
	
	/**
	 * @effects calculate hashCode of this object, by its id.
	 * @return hash value of this.
	 */
	@Override
	public int hashCode() {
		checkRep();
		return id.hashCode();
	}
	
	private void checkRep() {
		assert type != null : "type == null";
		assert id != null : "id == null";
		
		Set <T> childSet = new HashSet<>();
		for (Map.Entry<T, T> entry : outgoingEdges.entrySet()) {
			//check for: "key != null, value != null"
			assert entry.getKey() != null : "entry.getKey() == null";
			T child = entry.getValue();
			assert child != null : "entry.getVal() == null";
			//check for 2 edges to the same child 
			assert childSet.add(child) : "2 edges with the same src and dst";
		}
		
		Set <T> parentsSet = new HashSet<>();
		for (Map.Entry<T, T> entry : ingoingEdges.entrySet()) {
			//check for: "key != null, value != null"
			assert entry.getKey() != null : "entry.getKey() == null";
			T parent = entry.getValue();
			assert parent != null : "entry.getVal() == null";
			//check for 2 edges from the same parent
			assert parentsSet.add(parent) : "2 edges with the same src and dst";
		}
	}
}
