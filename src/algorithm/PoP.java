package algorithm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import objects.Action;
import objects.AgendaElement;
import objects.CausalLink;
import objects.Literal;
import objects.Ordering;
import objects.Plan;
import objects.Threat;
import objects.VariableBinding;
import pddl.PddlToClassConverter;
import uk.ac.bham.cs.zas.pddl.parser.InvalidPDDLElementException;
import uk.ac.bham.cs.zas.pddl.parser.PDDLSyntaxException;

public class PoP {

	private List<String> variablesUsed = new ArrayList<String>();
	private int variableCounter = 1;
	private List<Action> domainActions;
	private Action start;
	private Action finish;
	private Plan plan;
	private List<AgendaElement> agenda = new ArrayList<AgendaElement>();

	private void init() {
		try {
			PddlToClassConverter converter = new PddlToClassConverter();
			domainActions = converter.getDomainActions();
			start = converter.getInitAction();
			finish = converter.getFinishAction();
			List<Action> planActions = new ArrayList<Action>();
			planActions.add(start);
			planActions.add(finish);
			List<Ordering> ordering = new ArrayList<Ordering>();
			ordering.add(new Ordering(start, finish));
			for (Literal l : finish.getPreConditions()) {
				AgendaElement element = new AgendaElement(l, finish);
				agenda.add(element);
			}
			plan = new Plan(planActions, ordering, new ArrayList<CausalLink>(),
					new ArrayList<VariableBinding>());
		} catch (IOException | PDDLSyntaxException
				| InvalidPDDLElementException e) {
			System.err.println("Error with PDDL");
		}

	}

	public Plan pop() {
		init();
		while (!agenda.isEmpty()) {
			AgendaElement element = agenda.remove(0); //select a sub goal
			Literal preCondition = element.getPreCondition();
			Action aj = element.getAction();
			List<Action> relevant = providers(preCondition, plan);
			if (relevant.isEmpty()) {
				System.err.println("No Actions available");
				System.exit(0);
			} else {
				boolean planIsConsistent = false;
				while (!planIsConsistent) {
					boolean orderingThreatExisting = false;
					boolean variableThreatExisting = false;
					List<AgendaElement> agendaCache = new ArrayList<AgendaElement>();
					List<VariableBinding> variablesCache = new ArrayList<VariableBinding>();
					Action ai = relevant.remove(0); //choose an operator
					System.out.println("Escolhi a acção " + ai);
					CausalLink link = new CausalLink(ai, preCondition, aj);
					plan.getCausalLinks().add(link);
					Ordering o = new Ordering(ai, aj);
					plan.getOrderingConstraints().add(o); //add ordering constraint
					boolean containsVarBindings = preConditionContainsVariableBindings(preCondition);
					boolean actionLiteralIsInstantiated = actionLiteralIsInstantiated(
							preCondition, ai);
					if (containsVarBindings && actionLiteralIsInstantiated) {
						for (String var : preCondition.getActualArguments()) {
							if (variablesUsed.contains(var)) {
								int index = preCondition.getActualArguments().indexOf(var);
								for (Literal l : ai.getEffects()) {
									if (l.getName().equals(preCondition.getName())) {
										String possibleValue = l.getActualArguments().get(index);
										VariableBinding newVar = new VariableBinding(var, possibleValue);
										for(VariableBinding v : plan.getVariableBindings()){
											if(v.getVariableName().equals(var) && !v.getVariableValue().equals(possibleValue)){
												variableThreatExisting = true;
												plan.getCausalLinks().remove(link);
												plan.getOrderingConstraints().remove(o);
												System.out.println("Não posso usar a acção " + ai + " porque já existem bindings");
											}
										}
										if(!variableThreatExisting){
											plan.getVariableBindings().add(newVar);
											variablesCache.add(newVar);
										}
									}
								}
							}
						}
					}
					if(!variableThreatExisting){
						if (!plan.getActions().contains(ai)) {
							updatePlan(plan, agenda, aj, ai, agendaCache);
						}
						Ordering resolver = null;
						Threat t = findThreat(plan, ai);
						Threat t2 = null;
						if (t != null) {
							System.out.println("A acção " + ai
									+ "é uma ameaça ao link " + t.getThreatened());
							// promover
							System.out.println("Vou promover");
							if (!t.getThreatAfterAi().getFirst().getName()
									.equals(start.getName())) {
								System.out.println("Posso promover.");
								resolver = promote(ai, t);
								t2 = findThreat(plan, ai);
								if (t2 != null) {
									System.out
											.println("A promoção não resultou, vou demover");
									if (!t.getThreatBeforeAj().getSecond()
											.getName().equals(finish.getName())) {
										resolver = demote(ai, resolver, t);
										t2 = findThreat(plan, ai);
										if (t2 != null) {
											System.out
													.println("Nenhum dos resolvers resultou e temos que seleccionar outra acção");
											removeDataAddedFromPlan(agendaCache,
													variablesCache, ai, link,
													resolver);
										} else {
											System.out
													.println("A demoção resultou");
											orderingThreatExisting = false;
										}
									} else {// promoção nao resultou e não se pode
											// demover
										System.out
												.println("Não posso demover porque Ai ficaria depois da acção final, tenho que escolher outra acção");
										removeDataAddedFromPlan(agendaCache,
												variablesCache, ai, link, resolver);
									}
								} else {
									System.out.println("A promoção resultou");
									orderingThreatExisting = false;
								}
							} else { // não podemos promover. Tentar demover então
								System.out
										.println("Não posso promover porque Ai ficaria antes da acção inicial");
								if (!t.getThreatBeforeAj().getSecond().getName()
										.equals(finish.getName())) {
									System.out.println("Posso apenas demover");
									resolver = demote(ai, t);
									t2 = findThreat(plan, ai);
									if (t2 != null) {
										System.out
												.println("Tenho que escolher outra acção");
										removeDataAddedFromPlan(agendaCache,
												variablesCache, ai, link, resolver);
									} else {
										System.out.println("a demoção resultou");
										orderingThreatExisting = false;
									}

								} else {// não podemos demover.escolher outra acção
										// - não foram feitos resolvers.
									System.out
											.println("Não posso demover porque Ai ficaria depois da acção final. Escolher outra acção");
									removeDataAddedFromPlan(agendaCache,
											variablesCache, ai, link, o);

								}
							}

						} else {
							orderingThreatExisting = false;
						}
						if(!orderingThreatExisting && !variableThreatExisting){
							planIsConsistent = true;
						}
					}
					
					
				}

			}
		}
		return plan;

	}

	private Ordering demote(Action ai, Ordering resolver, Threat t) {
		plan.getOrderingConstraints().remove(resolver);
		resolver = new Ordering(t.getThreatBeforeAj().getSecond(), ai);
		plan.getOrderingConstraints().add(resolver);
		return resolver;
	}

	private Ordering demote(Action ai, Threat t) {
		Ordering resolver;
		plan.getOrderingConstraints().remove(t.getThreatAfterAi());
		plan.getOrderingConstraints().remove(t.getThreatBeforeAj());
		resolver = new Ordering(t.getThreatBeforeAj().getSecond(), ai);
		plan.getOrderingConstraints().add(resolver);
		return resolver;
	}

	private Ordering promote(Action ai, Threat t) {
		Ordering resolver;
		plan.getOrderingConstraints().remove(t.getThreatAfterAi());
		plan.getOrderingConstraints().remove(t.getThreatBeforeAj());
		resolver = new Ordering(ai, t.getThreatAfterAi().getFirst());
		plan.getOrderingConstraints().add(resolver);
		return resolver;
	}

	private void removeDataAddedFromPlan(List<AgendaElement> agendaCache,
			List<VariableBinding> variablesCache, Action ai, CausalLink link,
			Ordering o) {
		removeAgendaElementsAdded(agendaCache);
		removeVariableBindingsAdded(variablesCache);
		plan.getActions().remove(ai);
		plan.getOrderingConstraints().remove(o);
		plan.getCausalLinks().remove(link);
	}

	private void removeVariableBindingsAdded(
			List<VariableBinding> variablesCache) {
		for (VariableBinding vb : variablesCache) {
			plan.getVariableBindings().remove(vb);
		}
	}

	private void removeAgendaElementsAdded(List<AgendaElement> agendaCache) {
		for (AgendaElement el : agendaCache) {
			agenda.remove(el);
		}
	}

	private Threat findThreat(Plan plan, Action a) {
		for (CausalLink planLink : plan.getCausalLinks()) {
			Ordering aAfterAi = findMatchingOrder(planLink.getFirst(), a,
					plan.getOrderingConstraints());
			Ordering aBeforeAj = findMatchingOrder(a, planLink.getSecond(),
					plan.getOrderingConstraints());
			if (aAfterAi != null && aBeforeAj != null) {
				Literal preC = planLink.getAchieved();
				if (a.effectsContainLiteralNegation(preC, variablesUsed)) {
					return new Threat(aAfterAi, aBeforeAj, planLink);
				}
			}
		}
		return null;
	}

	private void updateBindingConstraints(Plan plan, Literal preCondition,
			Action ai, List<VariableBinding> variablesCache) {
		boolean containsVarBindings = preConditionContainsVariableBindings(preCondition);
		boolean actionLiteralIsInstantiated = actionLiteralIsInstantiated(
				preCondition, ai);
		if (containsVarBindings && actionLiteralIsInstantiated) {
			for (String var : preCondition.getActualArguments()) {
				if (variablesUsed.contains(var)) {
					int index = preCondition.getActualArguments().indexOf(var);
					for (Literal l : ai.getEffects()) {
						if (l.getName().equals(preCondition.getName())) {
							String possibleValue = l.getActualArguments().get(index);
							VariableBinding newVar = new VariableBinding(var, possibleValue);
							for(VariableBinding v : plan.getVariableBindings()){
								if(v.getVariableName().equals(var) && !v.getVariableValue().equals(possibleValue)){
									System.out.println("NAO POSSO USAR A ACÇAO " +  " DEVIDO ÀS VARIAVEIS");
								}
							}
							plan.getVariableBindings().add(newVar);
							variablesCache.add(newVar);
						}
					}
				}
			}
		}
	}

//	private void findMatchingVariables(Plan plan, Literal preCondition,
//			Action ai, String var, List<VariableBinding> variablesCache) {
//		int index = preCondition.getActualArguments().indexOf(var);
//		for (Literal l : ai.getEffects()) {
//			if (l.getName().equals(preCondition.getName())) {
//				String possibleValue = l.getActualArguments().get(index);
//				VariableBinding newVar = new VariableBinding(var, possibleValue);
//				plan.getVariableBindings().add(newVar);
//				variablesCache.add(newVar);
//			}
//		}
//	}

	private boolean actionLiteralIsInstantiated(Literal preCondition, Action ai) {
		boolean actionLiteralIsInstantiated = true;
		for (Literal l : ai.getEffects()) {
			if (l.getName().equals(preCondition.getName())) {
				for (String litVar : l.getActualArguments()) {
					if (variablesUsed.contains(litVar)) {
						actionLiteralIsInstantiated = false;
					}
				}
			}
		}
		return actionLiteralIsInstantiated;
	}

	private boolean preConditionContainsVariableBindings(Literal preCondition) {
		boolean containsVarBindings = false;
		for (String var : preCondition.getActualArguments()) {
			if (variablesUsed.contains(var)) {
				containsVarBindings = true;
			}
		}
		return containsVarBindings;
	}

	private Ordering findMatchingOrder(Action first, Action second,
			List<Ordering> orderConstraints) {
		Ordering firstBeforeSecond = null;
		for (Ordering o : orderConstraints) {
			if (o.getFirst().equals(first) && o.getSecond().equals(second)) {
				firstBeforeSecond = o;
			}
		}
		return firstBeforeSecond;
	}

	private void updatePlan(Plan plan, List<AgendaElement> agenda, Action aj,
			Action ai, List<AgendaElement> agendaCache) {
		plan.getActions().add(ai);
		for (Literal l : ai.getPreConditions()) { // update agenda
			AgendaElement el = new AgendaElement(l, ai);
			agenda.add(el);
			agendaCache.add(el);

		}
	}

	private List<Action> providers(Literal preCondition, Plan p) {
		List<Action> providers = new ArrayList<Action>();
		relevantActions(preCondition, p.getActions(), providers);
		relevantActions(preCondition, domainActions, providers);
		return providers;

	}

	
	private void relevantActions(Literal preCondition, List<Action> allActions,
			List<Action> providers) {
		for (Action possibleAction : allActions) {
			for (Literal literalToCompare : possibleAction.getEffects()) {
				boolean namesAreEqual = literalToCompare.getName().equals(
						preCondition.getName());
				boolean valuesAreEqual = literalToCompare.getValue() == preCondition
						.getValue();
				if (namesAreEqual && valuesAreEqual) {
					if (literalToCompare.getActualArguments().isEmpty()) { // a
																			// acçao
																			// serve
																			// mas
																			// nao
																			// está
																			// instanciada.
						// create copies so we won't mess with the original
						// objects.
						Action actionCopy = possibleAction.createCopy();
						Literal literalCopy = literalToCompare.createCopy();

						literalCopy.setActualArguments(preCondition
								.getActualArguments());

						instantateAction(actionCopy, literalCopy);
						instantiatePreConditions(actionCopy);
						instantiatePostConditions(actionCopy);
						providers.add(actionCopy);

					} else { // literal já instanciado
						boolean equals = true;
						int argumentsSize = preCondition.getActualArguments()
								.size();
						for (int i = 0; i < argumentsSize; i++) {
							String preConditionArg = preCondition
									.getActualArguments().get(i);
							String literalArg = literalToCompare
									.getActualArguments().get(i);
							boolean isVariableBinding = variablesUsed
									.contains(preConditionArg);
							boolean equalArgs = preConditionArg
									.equals(literalArg);
							if (!isVariableBinding && !equalArgs) {
								// variable binding.
								equals = false;
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
		for (Literal l : actionCreated.getEffects()) {
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
