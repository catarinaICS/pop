package algorithm;

import objects.Action;

import java.util.ArrayList;
import java.util.List;

import objects.AgendaElement;
import objects.CausalLink;
import objects.Literal;
import objects.Ordering;
import objects.Plan;

public class PoP {
	Domain domain = new Domain();
	
	public Plan pop(Plan p, List<AgendaElement> agenda){
		if(agenda.isEmpty()){
			return p;
		}else{
			AgendaElement element = agenda.remove(0);
			Literal preCondition = element.getPreCondition();
			Action aj = element.getAction();
			List<Action> relevant = providers(preCondition, p);
			if(relevant.isEmpty()){
				System.err.println("No Actions available");
				System.exit(0);
			}else{
				//Add causal link
				Action ai = relevant.get(0);
				CausalLink link = new CausalLink(ai, preCondition, aj);
				p.getCausalLinks().add(link);
				
				//update plan if necessary
				if(!p.getActions().contains(ai)){
					p.getActions().add(ai); //add to plan actions
					p.getOrderingConstraints().add(new Ordering(ai, aj)); //update ordering constraints
					for(Literal l : ai.getPreConditions()){ //update agenda
						agenda.add(new AgendaElement(l, ai));	
					}
				}
				
				//threats
				for(Action planAction : p.getActions()){
					//ATM só procura acçoes que neguem a pré condiçao e "promove-as" com uma ordering constraint
					for(Literal postCondition : planAction.getPostConditions()){
						if(postCondition.equals(preCondition) && postCondition.getValue() != preCondition.getValue()){ //metodo equals
							p.getOrderingConstraints().add(new Ordering(planAction, ai));
						}
					}
				}
				
				return pop(p, agenda);
			}
		}
		return null;
		
	}

	private List<Action> providers(Literal preCondition, Plan p) {
		List<Action> providers = new ArrayList<Action>();
		relevantActions(preCondition, p.getActions(), providers);
		relevantActions(preCondition, domain.getDomainActions(), providers);
		return providers;
		
	}

	private void relevantActions(Literal preCondition, List<Action> allActions, List<Action> providers) {
		for(Action a : allActions){
			for(Literal l : a.getPostConditions()){
				if(l.equals(preCondition) && l.getValue() == preCondition.getValue()){
					providers.add(a);
				}
			}
		}
	}
	
	

}
