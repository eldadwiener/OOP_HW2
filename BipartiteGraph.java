package homework2;


public interface BipartiteGraph<T> {
	enum NodeType { BLACK, WHITE };
	public boolean addNode (T id, NodeType type, Object obj);
	
	public boolean addEdge (T edgeId, T srcNodeId, T dstNodeId);
	
	public boolean containsEdge (T edgeId);
	
	public boolean containsEdge (T srcNodeId, T dstNodeId);
	
	public boolean containsNode (T nodeId);
	
	public Object getNode (T nodeId);
	
	public boolean removeNode (T nodeId);
	
	public boolean removeEdge (T edgeId);
	
	public boolean removeEdge (T srcNodeId, T dstNodeId);
	
	
}
