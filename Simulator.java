package homework2;

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
	
	public boolean removePipe(T pipeId) {
		return false;
	}

	public boolean removeFIlter(T filterId) {
		return false;
	}
	
	public boolean disconnect(T srcId, T dstId) {
		return false;
	}
	
	public Sim getPipeObject(T pipeId) {
		return null;
	}

	public Sim getFilterObject(T filterId) {
		return null;
	}
}
