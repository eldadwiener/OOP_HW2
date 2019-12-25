package homework2;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Node<T extends Comparable<T>> {
	enum NodeType { BLACK, WHITE };

	private final NodeType type;
	private final T id;
	private final Object object;
	private Map<T,Node<T>> ingoingEdges = new HashMap<>();
	private Map<T,Node<T>> outgoingEdges = new HashMap<>();

	public Node(T id, NodeType type, Object obj) {
		this.type = type;
		this.id = id;
		object = obj;
	}
	
	protected boolean insertChildEdge(T edgeId, Node<T> child) {
		if ( ingoingEdges.containsKey(edgeId) ) {
			return false;
		}
		ingoingEdges.put(edgeId, child);
		return true;
	}
	
	protected boolean insertParentEdge(T edgeId, Node<T> parent) {
		if ( outgoingEdges.containsKey(edgeId) ) {
			return false;
		}
		outgoingEdges.put(edgeId, parent);
		return true;
	}

	protected void removeChild(T childId) {
		for (Map.Entry<T, Node<T>> child : outgoingEdges.entrySet()) {
			if (child.getValue().getId().equals(childId)) {
				outgoingEdges.remove(child.getKey());
				return;
			}
		}
	}

	protected void removeParent(T parentId) {
		for (Map.Entry<T, Node<T>> parent : ingoingEdges.entrySet()) {
			if (parent.getValue().getId().equals(parentId)) {
				ingoingEdges.remove(parent.getKey());
				return;
			}
		}
	}

	protected void removeChildEdge(T edgeId) {
		ingoingEdges.remove(edgeId);
	}

	protected void removeParentEdge(T edgeId) {
		outgoingEdges.remove(edgeId);
	}

	protected void removeAllEdges() {
		// remove all ingoing edges
		for (Map.Entry<T, Node<T>> edge : ingoingEdges.entrySet()) {
			Node<T> parent = edge.getValue();
			parent.removeChildEdge(edge.getKey());
		}
		// remove all outgoing edges
		for (Map.Entry<T, Node<T>> edge : outgoingEdges.entrySet()) {
			Node<T> child = edge.getValue();
			child.removeParentEdge(edge.getKey());
		}
		
		ingoingEdges.clear();
		outgoingEdges.clear();
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

	public Collection<Node<T>> getAllChildren() {
		return outgoingEdges.values();
	}

	public Collection<Node<T>> getAllParents() {
		return ingoingEdges.values();
	}
	
	// TODO: what if edgeId is NULL?
	public Node<T> getChildByEdge(T edgeId) {
		return outgoingEdges.get(edgeId);
	}
	
	public Node<T> getParentByEdge(T edgeId) {
		return ingoingEdges.get(edgeId);
	}
	
	public boolean isChild(T nodeId) {
		Collection<Node<T>> children = getAllChildren();
		for (Node<T> child : children) {
			if (child.id.equals(nodeId)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isParent(T nodeId) {
		Collection<Node<T>> parents = getAllParents();
		for (Node<T> parent: parents) {
			if (parent.id.equals(nodeId)) {
				return true;
			}
		}
		return false;
	}

}
