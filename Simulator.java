package homework2;

import homework2.BipartiteGraph.NodeType;

public class Simulator<T, Sim extends Simulatable<T>> {
	
	private BipartiteGraph<T> graph;
	private int round;

	public Simulator() {
		// TODO Auto-generated constructor stub
	}
	
	public void simulate() {
		
	}
	
	public boolean addPipe(T pipeId, Sim pipeObj) {
		return false;
	}
	
	public boolean addFilter(T filterId, Sim filterObj) {
		return false;
	}
	
	public boolean connect(T srcId, T dstId) {
		return false;
	}
	
	/**
	 * @modified this
	 * @effects  remove the pipe with pipeId label (if exist)
	 */
	public void removePipe(T pipeId) {
		if (graph.getNodesByType(NodeType.BLACK).contains(pipeId)) {
			graph.removeNode(pipeId);
		}
	}

	/**
	 * @modified this
	 * @effects  remove the filter with filterId label (if exist)
	 */
	public void removeFIlter(T filterId) {
		if (graph.getNodesByType(NodeType.WHITE).contains(filterId)) {
			graph.removeNode(filterId);
		}
	}
	
	/**
	 * @modified this
	 * @effects if srcId and dstId are connected by edge, remove the edge
	 */
	public void disconnect(T srcId, T dstId) {
		graph.removeEdge(srcId, dstId);;
	}

	/**
 	* @effects get the object of specific pipe
 	* @return if (pipeId not in pipes set or object not extends Simulatable) -null, otherwise the pipe's object
 	*/
	public Simulatable<?> getPipeObject(T pipeId) {
		if (graph.getNodesByType(NodeType.BLACK).contains(pipeId)) {
			Object nodeObj = graph.getNodeObj(pipeId);
			if (nodeObj instanceof Simulatable<?>) {
            	return ( Simulatable<?>)nodeObj;
			}
		}
		return null;
	}

	/**
	* @effects get the object of specific filter
	* @return if (filterId not in filters set or object not extends Simulatable) -null, otherwise the filter's object
	*/
	public Simulatable<?> getFilterObject(T filterId) {
		if (graph.getNodesByType(NodeType.WHITE).contains(filterId)) {
			Object nodeObj = graph.getNodeObj(filterId);
			if (nodeObj instanceof Simulatable<?>) {
            	return ( Simulatable<?>)nodeObj;
			}
		}
		return null;
	}
}
