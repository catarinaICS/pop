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
	//variáveis//
	private List<String> variablesUsed = new ArrayList<String>();
	private int variableCounter = 1;
	//---------//
	private Domain domain;

	public PoP(Domain domain) {
		super();
		this.domain = domain;
	}
	
	
	/*
	 * O QUE FALTA; - VERIFICAR CONSISTENCIA DO PLANO - RESOLVER THREATS - ... -
	 */
	public Plan pop(Plan plan, List<AgendaElement> agenda) {
		while (!agenda.isEmpty()) {
			AgendaElement element = agenda.remove(0);
			Literal preCondition = element.getPreCondition();
			Action aj = element.getAction();
			List<Action> relevant = providers(preCondition, plan);
			if (relevant.isEmpty()) {
				System.err.println("No Actions available");
				System.exit(0);
			} else {
				Action ai = relevant.get(0);
				addCausalLink(plan, preCondition, aj, ai);
				updateBindingConstraints();
				if (!plan.getActions().contains(ai)) {
					updatePlan(plan, agenda, aj, ai);
				}
				resolveThreats();

			}
		}

		return plan;

	}


	private void resolveThreats() {
		// TODO Auto-generated method stub
		
	}

	private void updateBindingConstraints() {
		// TODO Auto-generated method stub
	}

	private void addCausalLink(Plan plan, Literal preCondition, Action aj,
			Action ai) {
		CausalLink link = new CausalLink(ai, preCondition, aj);
		plan.getCausalLinks().add(link);
	}

	private void updatePlan(Plan plan, List<AgendaElement> agenda, Action aj,
			Action ai) {
		plan.getActions().add(ai); // add to plan actions
		// update ordering constraints
		plan.getOrderingConstraints().add(new Ordering(ai, aj));
		for (Literal l : ai.getPreConditions()) { // update agenda
			agenda.add(new AgendaElement(l, ai));
		}
	}

	private List<Action> providers(Literal preCondition, Plan p) {
		List<Action> providers = new ArrayList<Action>();
		relevantActions(preCondition, p.getActions(), providers);
		relevantActions(preCondition, domain.getDomainActions(), providers);
		return providers;

	}

	private void relevantActions(Literal preCondition, List<Action> allActions,
			List<Action> providers) {
		for (Action possibleAction : allActions) {
			for (Literal literalToCompare : possibleAction.getPostConditions()) {
				if (literalToCompare.getName().equals(preCondition.getName())
						&& literalToCompare.getValue() == preCondition
								.getValue()) {

					// se os argumentos forem todos null -> o literal serve, mas
					// nao está instanciado. É necessário instanciar a acção,
					// pré e pos condições

					if (literalToCompare.getActualArguments().isEmpty()) {
						// criar uma cópia da acção que encaixa e do literal
						// para instanciar.
						Action actionCopy = possibleAction.createCopy();
						Literal literalCopy = literalToCompare.createCopy();

						// instanciar o literal que correspondeu à pré condição
						// que estamos a procurar com os argumentos de
						// preCOndition
						literalCopy.setActualArguments(preCondition
								.getActualArguments());
						// fazer corresponder os argumentos da acçao
						// ou seja stack(a,b) -> on(a,b), por exemplo. se tenho
						// literalCopy = on(a,b)
						// e uma acção nao instanciada (sem argumentos), temos
						// que instanciar.

						instantateAction(actionCopy, literalCopy);
						// a acção está agora instanciada. Argumentos sem
						// correspondência = variable bindings.
						instantiatePreConditions(actionCopy);
						instantiatePostConditions(actionCopy);
						providers.add(actionCopy);

					} else {// temos um literal instanciado pelo que é
							// necessário verificar se é igual à nossa pre
							// condição.
						boolean equals = true;
						
						for (int i = 0; i < preCondition.getActualArguments()
								.size(); i++) {
							String preConditionArg = preCondition
									.getActualArguments().get(i);
							String literalArg = literalToCompare
									.getActualArguments().get(i);
							
							if (variablesUsed.contains(preCondition
									.getActualArguments().get(i))) {
								System.out.println("I'm a variable binding!");// para
																				// apagar
																				// eventualmente.
							} else {
								if (!preConditionArg.equals(literalArg)) {
									equals = false;
								}
							}
						}

						if (equals == true) {
							providers.add(possibleAction);
						}

					}

				}
			}
		}
	}

	private void instantateAction(Action actionCreated, Literal baseLiteral) {
		for (String formalArg : actionCreated.getFormalArguments()) {
			int n = baseLiteral.getFormalArguments().indexOf(formalArg);
			if (n >= 0) {
				actionCreated.getActualArguments().add(
						baseLiteral.getActualArguments().get(n));
			} else {
				String variable = getVariableName();
				actionCreated.getActualArguments().add(variable);
			}
		}
	}

	private void instantiatePostConditions(Action actionCreated) {
		for (Literal l : actionCreated.getPostConditions()) {
			for (String formalArg : l.getFormalArguments()) {
				int i = actionCreated.getFormalArguments().indexOf(formalArg);
				l.getActualArguments().add(
						actionCreated.getActualArguments().get(i));
			}
		}
	}

	private void instantiatePreConditions(Action actionCreated) {
		for (Literal l : actionCreated.getPreConditions()) {
			for (String formalArg : l.getFormalArguments()) {
				int i = actionCreated.getFormalArguments().indexOf(formalArg);
				l.getActualArguments().add(
						actionCreated.getActualArguments().get(i));
			}
		}
	}

	private String getVariableName() {
		char a = 'a';
		String variable = "" + a + variableCounter;
		while (variablesUsed.contains(variable)) {
			variableCounter++;
			variable = "" + a + variableCounter;
		}
		variablesUsed.add(variable);
		variableCounter++;
		return variable;
	}

}
