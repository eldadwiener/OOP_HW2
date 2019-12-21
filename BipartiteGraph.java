package homework2;

import java.util.HashMap;
import java.util.Map;

public class BipartiteGraph<T extends Comparable<T>> {
	
	private Map<T,Node<T>> nodes = new HashMap<>();
	
	public boolean addNode (T nodeId, Node.NodeType type, Object obj) {
		if ( nodes.containsKey(nodeId)) {
			return false;
		}
		nodes.put(nodeId, new Node<T>(nodeId, type,obj));
		return true;
	}
	
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
		if ( !srcNode.insertChildEdge(edgeId, dstNode) ) {
			return false;
		}
		
		if ( !dstNode.insertParentEdge(edgeId, srcNode) ) {
			// failed to add to destination, remove new edge from source
			srcNode.removeChildEdge(edgeId);
			return false;
		}
		// managed to add the edge on both sides, so it is a legal edge
		return true;
	}
	
	public boolean containsEdge (T srcNodeId, T dstNodeId) {
		Node<T> srcNode = nodes.get(srcNodeId);
		if (srcNode != null) {
			return srcNode.isChild(dstNodeId);
		}
		return false;
	}
	
	public boolean containsNode (T nodeId) {
		return nodes.containsKey(nodeId);
	}
	
	public Node<T> getNode (T nodeId) {
		return nodes.get(nodeId);
	}
	
	public void removeNode (T nodeId) {
		Node<T> removedNode = nodes.get(nodeId);
		if (removedNode == null) {
			return;
		}
		removedNode.removeAllEdges();
		nodes.remove(nodeId);
	}
	
	public void removeEdge (T srcNodeId, T dstNodeId) {
		Node<T> srcNode = nodes.get(srcNodeId);
		Node<T> dstNode = nodes.get(dstNodeId);
		if (srcNode == null || dstNode == null) {
			return;
		}
		srcNode.removeChild(dstNodeId);
		dstNode.removeParent(srcNodeId);
	}
	
}
