package homework2;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Node<T> {
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
}
