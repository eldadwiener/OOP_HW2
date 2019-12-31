package homework2;

import java.util.Collection;
import java.util.List;

import homework2.BipartiteGraph.NodeType;

/*
 * This class implements a simulator for a system of pipes-filters.
 * Filters pass work-objects between one another via pipes.
 * Each simulation step runs the simulate method of all pipes in the system, followed
 * by all filters in the system.
 */
public class Simulator<T, WorkObj> {
	
	// Abs. Function:
	// represents a Simulator with:
	//		Bipartite graph containing pipes as black nodes, and filters as white nodes.	
	//		round number is represented by the integer round.
	// Rep. Invariant:
	// 		round >= 0

	private BipartiteGraph<T> graph;
	private int round;

	/**
	 * @modifies this
	 * @effects creates a new simulator with empty graph and 0 rounds done.
	 */
	public Simulator() {
		graph = new BipartiteGraph<>();
		round = 0;
		checkRep();
	}
	
	/**
	 * @modifies this
	 * @effects runs a single round of the simulation.
	 * 			invokes the simulate method on the objects of all pipes.
	 * 			and afterwards does the same for all filters.
	 */
	@SuppressWarnings("unchecked")
	public void simulate() {
		checkRep();
		Collection<T> pipes = graph.getNodesByType(NodeType.BLACK);
		Collection<T> filters = graph.getNodesByType(NodeType.WHITE);
		
		for (T pipe : pipes) {
			Object pipeObj = graph.getNodeObj(pipe);
			if (pipeObj instanceof Simulatable<?>) {
				((Simulatable<T>)pipeObj).simulate(graph);
			}
		}

		for (T filter : filters) {
			Object filterObj = graph.getNodeObj(filter);
			if (filterObj instanceof Simulatable<?>) {
				((Simulatable<T>)filterObj).simulate(graph);
			}
		}
		++round;
		checkRep();
	}
	
	/**
	 * @modifies this
	 * @effects adds a pipe with id pipeId and object pipeObj to the simulator.
	 * @return success/failure
	 */
	public boolean addPipe(T pipeId, Simulatable<T> pipeObj) {
		checkRep();
		boolean result = graph.addNode(pipeId, NodeType.BLACK, pipeObj);
		checkRep();
		return result;
	}
	
	/**
	 * @modifies this
	 * @effects adds a filter with id filterId and object filterObj to the simulator. 
	 * @return success/failure
	 */
	public boolean addFilter(T filterId, Simulatable<T> filterObj) {
		checkRep();
		boolean result = graph.addNode(filterId, NodeType.WHITE, filterObj);
		checkRep();
		return result;
	}
	
	/**
	 * @modifies this
	 * @effects adds an edge with id edgeId, connecting between the source and destination as long
	 * 			as they represent a pipe and a filter. 
	 * @return success/failure
	 */
	public boolean connect(T edgeId, T srcId, T dstId) {
		checkRep();
		boolean result = graph.addEdge(edgeId, srcId, dstId);
		checkRep();
		return result;
	}
	
	/**
	 * @modifies this
	 * @effects  remove the pipe with pipeId label (if exist)
	 */
	public void removePipe(T pipeId) {
		checkRep();
		if (graph.getNodesByType(NodeType.BLACK).contains(pipeId)) {
			graph.removeNode(pipeId);
		}
		checkRep();
	}

	/**
	 * @modifies this
	 * @effects  remove the filter with filterId label (if exist)
	 */
	public void removeFilter(T filterId) {
		checkRep();
		if (graph.getNodesByType(NodeType.WHITE).contains(filterId)) {
			graph.removeNode(filterId);
		}
		checkRep();
	}
	
	/**
	 * @modifies this
	 * @effects if srcId and dstId are connected by edge, remove the edge
	 */
	public void disconnect(T srcId, T dstId) {
		checkRep();
		graph.removeEdge(srcId, dstId);;
		checkRep();
	}

	/**
 	* @effects get the object of specific pipe
 	* @return if (pipeId not in pipes set or object not extends Simulatable) -null, otherwise the pipe's object
 	*/
	public Simulatable<?> getPipeObject(T pipeId) {
		checkRep();
		if (graph.getNodesByType(NodeType.BLACK).contains(pipeId)) {
			Object nodeObj = graph.getNodeObj(pipeId);
			if (nodeObj instanceof Simulatable<?>) {
				checkRep();
            	return ( Simulatable<?>)nodeObj;
			}
		}
		checkRep();
		return null;
	}

	/**
	* @effects get the object of specific filter
	* @return if (filterId not in filters set or object not extends Simulatable) -null, otherwise the filter's object
	*/
	public Simulatable<?> getFilterObject(T filterId) {
		checkRep();
		if (graph.getNodesByType(NodeType.WHITE).contains(filterId)) {
			Object nodeObj = graph.getNodeObj(filterId);
			if (nodeObj instanceof Simulatable<?>) {
				checkRep();
            	return ( Simulatable<?>)nodeObj;
			}
		}
		checkRep();
		return null;
	}
	
	/**
	 * @return a list of all pipes in the simulator
	 */
	public List<T> getPipesList() {
		checkRep();
		List<T> nodes = graph.getNodesByType(NodeType.BLACK);
		checkRep();
		return nodes;
	}
	
	/**
	 * @return a list of all filters in the simulator
	 */
	public List<T> getFiltersList() {
		checkRep();
		List<T> nodes = graph.getNodesByType(NodeType.WHITE);
		checkRep();
		return nodes;
	}

	/**
	 * @return list of all children of srcId if it exists.
	 * 			will return null if srcId is null or not in the simulation.
	 */
	public Collection<T> getListChildren(T srcId) {
		return graph.getListChildren(srcId);
	}
	
	/**
	 * @return list of all parents of srcId if it exists.
	 * 			will return null if srcId is null or not in the simulation.
	 */
	public Collection<T> getListParents(T srcId) {
		return graph.getListParents(srcId);
	}
	
	
	private void checkRep() {
		assert round>=0 : "Negative round number";
	}
}
