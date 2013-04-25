package objects;

import java.util.List;

public class Plan {
	
	private List<Action> actions;
	private List<Ordering> orderingConstraints;
	private List<CausalLink> causalLinks;
	private List<VariableBinding> variableBindings;
	
	public Plan(List<Action> actions, List<Ordering> orderingConstraints,
			List<CausalLink> causalLinks,List<VariableBinding> variableBindings ) {
		super();
		this.actions = actions;
		this.orderingConstraints = orderingConstraints;
		this.causalLinks = causalLinks;
		this.variableBindings = variableBindings;
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

	public List<VariableBinding> getVariableBindings() {
		return variableBindings;
	}

	public void setVariableBindings(List<VariableBinding> variableBindings) {
		this.variableBindings = variableBindings;
	}
	
	

}
