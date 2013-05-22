package testing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import objects.Action;
import objects.AgendaElement;
import objects.CausalLink;
import objects.Literal;
import objects.Ordering;
import objects.Plan;
import objects.VariableBinding;
import pddl.PddlToClassConverter;
import uk.ac.bham.cs.zas.pddl.parser.InvalidPDDLElementException;
import uk.ac.bham.cs.zas.pddl.parser.PDDLSyntaxException;
import algorithm.PoP;

public class Test {
	public static void main(String[] args) throws IOException,
			PDDLSyntaxException, InvalidPDDLElementException {

		PddlToClassConverter converter = new PddlToClassConverter();

		Action finish = converter.getFinishAction();
		List<AgendaElement> agenda = new ArrayList<AgendaElement>();
		for (Literal l : finish.getPreConditions()) {
			AgendaElement element = new AgendaElement(l, finish);
			agenda.add(element);
		}

		// Plan plan = getInitialPlan();
		PoP p = new PoP();
		Plan finalPlan = p.pop();
		System.out.println(finalPlan.getActions());
		System.out.println(finalPlan.getCausalLinks());
		System.out.println(finalPlan.getOrderingConstraints());
		System.out.println(finalPlan.getVariableBindings());
	}


	// private static Plan getInitialPlan() {
	// Action start = AlgorithmObjects.startAction();
	// Action finish = AlgorithmObjects.finishAction();
	// List<Action> actions = new ArrayList<Action>();
	// actions.add(start);
	// actions.add(finish);
	// List<Ordering> ordering = new ArrayList<Ordering>();
	// ordering.add(new Ordering(start, finish));
	// List<CausalLink> links = new ArrayList<CausalLink>();
	// List<VariableBinding> variables = new ArrayList<VariableBinding>();
	// Plan plan = new Plan(actions, ordering, links, variables);
	// return plan;
	// }

}
