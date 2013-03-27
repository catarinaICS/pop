package algorithm;

import java.util.ArrayList;
import java.util.List;

import objects.Action;
import objects.AgendaElement;
import objects.CausalLink;
import objects.Literal;
import objects.Ordering;
import objects.Plan;

public class PoP {

	private Domain domain;

	public PoP(Domain domain) {
		super();
		this.domain = domain;
	}

	public Plan pop(Plan p, List<AgendaElement> agenda) {
		if (agenda.isEmpty()) {
			return p;
		} else {
			AgendaElement element = agenda.remove(0);
			Literal preCondition = element.getPreCondition();
			Action aj = element.getAction();
			List<Action> relevant = providers(preCondition, p);
			if (relevant.isEmpty()) {
				System.err.println("No Actions available");
				System.exit(0);
			} else {
				// Add causal link
				Action ai = relevant.get(0);
				CausalLink link = new CausalLink(ai, preCondition, aj);
				p.getCausalLinks().add(link);

				// update plan if necessary
				if (!p.getActions().contains(ai)) {
					p.getActions().add(ai); // add to plan actions
					p.getOrderingConstraints().add(new Ordering(ai, aj)); // update
																			// ordering
																			// constraints
					for (Literal l : ai.getPreConditions()) { // update agenda
						agenda.add(new AgendaElement(l, ai));
					}
				}

				// threats
				// for(Action planAction : p.getActions()){
				// //ATM s� procura ac�oes que neguem a pr� condi�ao e
				// "promove-as" com uma ordering constraint
				// for(Literal postCondition : planAction.getPostConditions()){
				// if(postCondition.equals(preCondition) &&
				// postCondition.getValue() != preCondition.getValue()){
				// //metodo equals
				// p.getOrderingConstraints().add(new Ordering(planAction, ai));
				// }
				// }
				// }

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

	private void relevantActions(Literal preCondition, List<Action> allActions,
			List<Action> providers) {
		for (Action a : allActions) {
			for (Literal l : a.getPostConditions()) {
				if (l.getName().equals(preCondition.getName())&& l.getValue() == preCondition.getValue()) {
					
					// se os argumentos forem todos null -> o literal serve, mas
					// nao est� instanciado. � necess�rio instanciar a ac��o.
					if (l.getActualArguments().isEmpty()) {
						System.out.println("I fit but I'm null!");
						
						//instanciar o literal com os argumentos de preCOndition
						l.setActualArguments(preCondition.getActualArguments());
						
						//fazer corresponder os argumentos da ac�ao
						for(String formalArg : a.getFormalArguments()){
							int n = l.getFormalArguments().indexOf(formalArg);
							if(n>=0){
								a.getActualArguments().add(l.getActualArguments().get(n));
							}else{
								String variable = "randomVariable";
								a.getActualArguments().add(variable);
							}
						}
						providers.add(a);
					} else {//temos um literal instanciado pelo que � necess�rio verificar se os argumentos s�o iguais.
						boolean equals = true;
						for (int i = 0; i < preCondition.getActualArguments().size(); i++) {
							//� PRECISO VERIFICAR AS VARIABLE BINDINGS. 
							if (!preCondition.getActualArguments().get(i).equals(l.getActualArguments().get(i))) {
								equals = false;
							}
						}
						if (equals == true) {
							providers.add(a);
						}
					}

				}
			}
		}
	}



}
