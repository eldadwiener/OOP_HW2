package homework2;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
	
	enum NodeType { BLACK, WHITE };

	private final NodeType type;
	private final T id;
	private final Object object;
	private Map<T,T> ingoingEdges = new HashMap<>();
	private Map<T,T> outgoingEdges = new HashMap<>();

	public Node(T id, NodeType type, Object obj) {
		this.type = type;
		this.id = id;
		object = obj;
	}
	
	public boolean insertChildEdge(T edgeId, T childId) {
		if ( ingoingEdges.containsKey(edgeId) || ingoingEdges.containsValue(childId) ) {
			return false;
		}
		ingoingEdges.put(edgeId, childId);
		return true;
	}
	
	public boolean insertParentEdge(T edgeId, T parentId) {
		if ( outgoingEdges.containsKey(edgeId) || outgoingEdges.containsValue(parentId)) {
			return false;
		}
		outgoingEdges.put(edgeId, parentId);
		return true;
	}

	public void removeChild(T childId) {
		for (Map.Entry<T, T> child : outgoingEdges.entrySet()) {
			if (child.getValue().equals(childId)) {
				outgoingEdges.remove(child.getKey());
				return;
			}
		}
	}

	public void removeParent(T parentId) {
		for (Map.Entry<T, T> parent : ingoingEdges.entrySet()) {
			if (parent.getValue().equals(parentId)) {
				ingoingEdges.remove(parent.getKey());
				return;
			}
		}
	}

	public void removeChildEdge(T edgeId) {
		ingoingEdges.remove(edgeId);
	}

	public void removeParentEdge(T edgeId) {
		outgoingEdges.remove(edgeId);
	}

	public Object getObject() {
		return object;
	}
	
	public T getId() {
		return id;
	}

	public NodeType getType() {
		return type;
	}

	public Collection<T> getAllChildren() {
		return outgoingEdges.values();
	}

	public Collection<T> getAllParents() {
		return ingoingEdges.values();
	}
	
	// TODO: what if edgeId is NULL?
	public T getChildByEdge(T edgeId) {
		return outgoingEdges.get(edgeId);
	}
	
	public T getParentByEdge(T edgeId) {
		return ingoingEdges.get(edgeId);
	}
	
	public boolean isChild(T nodeId) {
		return outgoingEdges.containsValue(nodeId);
	}
	
	public boolean isParent(T nodeId) {
		return ingoingEdges.containsValue(nodeId);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Node<?>) {
			if (((Node<?>)obj).id == id) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
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
