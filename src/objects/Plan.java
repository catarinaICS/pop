package objects;

import java.util.List;
import java.util.Map;

import planObjects.CausalLink;
import planObjects.Ordering;

public class Plan {
	
	private List<Action> actions;
	private List<Ordering> orderingConstraints;
	private List<CausalLink> causalLinks;
//	private List<VariableBinding> variableBindings;
	private Map<String, String> variableBindings;
	
	public Plan(List<Action> actions, List<Ordering> orderingConstraints,
			List<CausalLink> causalLinks,Map<String, String> variableBindings ) {
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

	public Map<String, String> getVariableBindings() {
		return variableBindings;
	}

	public void setVariableBindings(Map<String, String> variableBindings) {
		this.variableBindings = variableBindings;
	}
	
	

}
