package homework2;

import java.util.HashMap;
import java.util.Map;

public class BipartiteGraph<T extends Comparable<T>> {
	
	private Map<T,Node<T>> whiteNodes = new HashMap<>();
	private Map<T,Node<T>> blackNodes = new HashMap<>();
	
	public boolean addNode (T nodeId, Node.NodeType type, Object obj) {
		if ( whiteNodes.containsKey(nodeId) || blackNodes.containsKey(nodeId)) {
			return false;
		}
		if (type == Node.NodeType.BLACK) {
			blackNodes.put(nodeId, new Node<T>(nodeId, type,obj));
		}
		else {
			whiteNodes.put(nodeId, new Node<T>(nodeId, type,obj));
		}
		return true;
	}
	
	public boolean addEdge (T edgeId, T srcNodeId, T dstNodeId) {
		Node<T> srcNode;
		Node<T> dstNode;
		if (blackNodes.containsKey(srcNodeId)) {
			srcNode = blackNodes.get(srcNodeId);
			dstNode = whiteNodes.get(dstNodeId);
		}
		else {
			srcNode = whiteNodes.get(srcNodeId);
			dstNode = blackNodes.get(dstNodeId);
		}
		if (srcNode == null || dstNode == null) {
			return false;
		}
		// both nodes exist, types are different, try adding the edge to both sides
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
		Node<T> srcNode;
		if (blackNodes.containsKey(srcNodeId)) {
			srcNode = blackNodes.get(srcNodeId);
		}
		else {
			srcNode = whiteNodes.get(srcNodeId);
		}
		if (srcNode != null) {
			return srcNode.isChild(dstNodeId);
		}
		return false;
	}
	
	public boolean containsNode (T nodeId) {
		return (blackNodes.containsKey(nodeId) || whiteNodes.containsKey(nodeId));
	}
	
	public Node<T> getNode (T nodeId) {
		if (blackNodes.containsKey(nodeId)) {
			return blackNodes.get(nodeId);
		}
		return whiteNodes.get(nodeId);
	}
	
	public void removeNode (T nodeId) {
		Node<T> removedNode;
		if (blackNodes.containsKey(nodeId)) {
			removedNode = blackNodes.get(nodeId);
			removedNode.removeAllEdges();
			blackNodes.remove(nodeId);
		}
		else if(whiteNodes.containsKey(nodeId)){
			removedNode = whiteNodes.get(nodeId);
			removedNode.removeAllEdges();
			whiteNodes.remove(nodeId);
		}
	}
	
	public void removeEdge (T srcNodeId, T dstNodeId) {
		Node<T> srcNode;
		Node<T> dstNode;
		if (blackNodes.containsKey(srcNodeId)) {
			srcNode = blackNodes.get(srcNodeId);
			dstNode = whiteNodes.get(dstNodeId);
		}
		else {
			srcNode = whiteNodes.get(srcNodeId);
			dstNode = blackNodes.get(dstNodeId);
		}
		if (srcNode == null || dstNode == null) {
			return;
		}
		srcNode.removeChild(dstNodeId);
		dstNode.removeParent(srcNodeId);
	}
	
}
