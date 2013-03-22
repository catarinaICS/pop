package algorithm;

import java.util.List;

import objects.Action;

public class Domain {

	private List<Action> domainActions;
	
	
	
	public Domain(List<Action> domainActions) {
		super();
		this.domainActions = domainActions;
	}



	public List<Action> getDomainActions() {
		return domainActions;
	}
}
