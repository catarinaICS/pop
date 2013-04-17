package objects;

public class AgendaElement {
	
	private Literal preCondition;
	private Action action;
	
	public AgendaElement(Literal preCondition, Action action) {
		super();
		this.preCondition = preCondition;
		this.action = action;
	}

	public Literal getPreCondition() {
		return preCondition;
	}

	public void setPreCondition(Literal preCondition) {
		this.preCondition = preCondition;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}
	
	@Override
	public String toString() {
		return preCondition + " - " + action;
	}
	
	
}
