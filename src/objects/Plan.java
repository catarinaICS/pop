package objects;

import java.util.List;

public class Plan {
	
	private List<Action> actions;
	private List<Ordering> orderingConstraints;
	private List<CausalLink> causalLinks;
	
	public Plan(List<Action> actions, List<Ordering> orderingConstraints,
			List<CausalLink> causalLinks) {
		super();
		this.actions = actions;
		this.orderingConstraints = orderingConstraints;
		this.causalLinks = causalLinks;
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

	public List<Ordering> getOrderingConstraints() {
		return orderingConstraints;
	}

	public void setOrderingConstraints(List<Ordering> orderingConstraints) {
		this.orderingConstraints = orderingConstraints;
	}

	public List<CausalLink> getCausalLinks() {
		return causalLinks;
	}

	public void setCausalLinks(List<CausalLink> causalLinks) {
		this.causalLinks = causalLinks;
	}

	
	
	

}
