package planObjects;

import objects.Action;
import objects.Literal;

public class CausalLink {

	private Action first;
	private Literal achieved;
	private Action second;
	
	public CausalLink(Action first, Literal achieved, Action second) {
		super();
		this.first = first;
		this.achieved = achieved;
		this.second = second;
	}

	public Action getFirst() {
		return first;
	}

	public void setFirst(Action first) {
		this.first = first;
	}

	public Literal getAchieved() {
		return achieved;
	}

	public void setAchieved(Literal achieved) {
		this.achieved = achieved;
	}

	public Action getSecond() {
		return second;
	}

	public void setSecond(Action second) {
		this.second = second;
	}
	
	@Override
	public String toString() {
		return first + "->" + achieved + "->" + second;
	}
	
}
