package testing;

import java.util.ArrayList;
import java.util.List;

import objects.Action;
import objects.AgendaElement;
import objects.CausalLink;
import objects.Literal;
import objects.Ordering;
import objects.Plan;
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
		
		Domain d = getDomain();
		Plan plan = getInitialPlan();
		
		PoP p = new PoP(d);
		Plan finalPlan = p.pop(plan, agenda);
		
		System.out.println(finalPlan.getActions());
		
	}

	private static Plan getInitialPlan() {
		List<Action> actions = new ArrayList<Action>();
		actions.add(AlgorithmObjects.startAction());
		List<Ordering> ordering = new ArrayList<Ordering>();
		List<CausalLink> links = new ArrayList<CausalLink>();
		Plan plan = new Plan(actions, ordering, links);
		return plan;
	}

	private static Domain getDomain() {
		List<Action> domainActions = new ArrayList<Action>();
		domainActions.add(AlgorithmObjects.moveAction());
		domainActions.add(AlgorithmObjects.stackAction());
		domainActions.add(AlgorithmObjects.unstackAction());
		Domain d = new Domain(domainActions);
		return d;
	}

}
