package testing;

import java.util.ArrayList;
import java.util.List;

import objects.Action;
import objects.AgendaElement;
import objects.CausalLink;
import objects.Literal;
import objects.Ordering;
import objects.Plan;
import objects.VariableBinding;
import algorithm.Domain;
import algorithm.PoP;

public class Test {
	public static void main(String[] args) {

		Action finish =AlgorithmObjects.finishAction();
		List<AgendaElement> agenda = new ArrayList<AgendaElement>();
		for(Literal l : finish.getPreConditions()){
			AgendaElement element = new AgendaElement(l, finish);
			agenda.add(element);
		}
		
		Domain d = new Domain();
		Plan plan = getInitialPlan();
		PoP p = new PoP(d);
		Plan finalPlan = p.pop(plan, agenda);
		System.out.println(finalPlan.getActions());
		System.out.println(finalPlan.getCausalLinks());
		System.out.println(finalPlan.getOrderingConstraints());
		
	}


	private static Plan getInitialPlan() {
		Action start = AlgorithmObjects.startAction();
		Action finish = AlgorithmObjects.finishAction();
		List<Action> actions = new ArrayList<Action>();
		actions.add(start);
		actions.add(finish);
		List<Ordering> ordering = new ArrayList<Ordering>();
		ordering.add(new Ordering(start, finish));
		List<CausalLink> links = new ArrayList<CausalLink>();
		List<VariableBinding> variables = new ArrayList<VariableBinding>();
		Plan plan = new Plan(actions, ordering, links, variables);
		return plan;
	}


}
