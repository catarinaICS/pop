package objects;

public class Ordering {
	
	private Action first;
	private Action second;
	
	public Ordering(Action first, Action second) {
		super();
		this.first = first;
		this.second = second;
	}

	public Action getFirst() {
		return first;
	}

	public void setFirst(Action first) {
		this.first = first;
	}

	public Action getSecond() {
		return second;
	}

	public void setSecond(Action second) {
		this.second = second;
	}
	
	@Override
	public String toString() {
		return "(" + first + " < " + second + ")";
	}
	

}
