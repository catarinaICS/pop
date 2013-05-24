package algorithm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import objects.Action;
import objects.Literal;
import objects.Plan;
import pddl.PddlToClassConverter;
import planObjects.AgendaElement;
import planObjects.CausalLink;
import planObjects.Ordering;
import planObjects.Threat;
import planObjects.VariableBinding;
import uk.ac.bham.cs.zas.pddl.parser.InvalidPDDLElementException;
import uk.ac.bham.cs.zas.pddl.parser.PDDLSyntaxException;

public class PoP {
	private VariableManager varManager = new VariableManager();
	private List<Action> domainActions;
	private Action start;
	private Action finish;
	private Plan plan;
	private List<AgendaElement> agenda = new ArrayList<AgendaElement>();

	private void init() {
		try {
			PddlToClassConverter converter = new PddlToClassConverter();
			domainActions = converter.getDomainActions();
			Collections.sort(domainActions, new ActionComparator());
			start = converter.getInitAction();
			finish = converter.getFinishAction();
			List<Action> planActions = initialActions();
			List<Ordering> ordering = new ArrayList<Ordering>();
			ordering.add(new Ordering(start, finish));
			initAgenda();
			plan = new Plan(planActions, ordering, new ArrayList<CausalLink>(),
					new HashMap<String, String>());
		} catch (IOException | PDDLSyntaxException
				| InvalidPDDLElementException e) {
			System.err.println("Error with PDDL");
		}

	}

	private List<Action> initialActions() {
		List<Action> planActions = new ArrayList<Action>();
		planActions.add(start);
		planActions.add(finish);
		return planActions;
	}

	private void initAgenda() {
		for (Literal l : finish.getPreConditions()) {
			AgendaElement element = new AgendaElement(l, finish);
			agenda.add(element);
		}
	}

	public Plan pop() {
		init();
		while (!agenda.isEmpty()) {
			debug("------------------------------------------");
			debug("Agenda is now at: " + agenda);
			AgendaElement element = agenda.remove(0); // select a sub goal
			Literal preCondition = element.getPreCondition();
			debug("Attempting to solve precondition: " + preCondition);
			List<Action> relevant = providers(preCondition, plan);
			debug("Got relevant actions: " + relevant);
			if (relevant.isEmpty()) {
				System.err.println("No Actions available");
				System.exit(0);
			} else {
				boolean planIsConsistent = false;
				while (!planIsConsistent) {
					// boolean orderingThreatExisting = false;
					boolean variableThreatExisting = false;
					List<AgendaElement> agendaCache = new ArrayList<AgendaElement>();
					List<VariableBinding> variablesCache = new ArrayList<VariableBinding>();
					Action operator = relevant.remove(0); // choose an operator
					System.out.println("Escolhi a acção " + operator);
					debug("Effects: " + operator.getEffects());
					if (!operator.equals(element.getAction())) {
						CausalLink link = new CausalLink(operator,
								preCondition, element.getAction());
						plan.getCausalLinks().add(link);
						debug("Added causal link: " + link);
						Ordering o = new Ordering(operator, element.getAction());
						plan.getOrderingConstraints().add(o); // add ordering
																// constraint
						debug("Added ordering constraint " + o);
						variableThreatExisting = updateVariableBindings(
								preCondition, variableThreatExisting,
								variablesCache, operator, link, o);
						if (!variableThreatExisting) {
							if (!plan.getActions().contains(operator)) {
								updatePlan(plan, agenda, element.getAction(),
										operator, agendaCache);
							}
							planIsConsistent = true;
						}

					} else {
						System.out.println("Nao posso escolher a mesma accao!");
					}
				}

			}

			ArrayList<Threat> allthreats = findAllThreatsInPlan();
			resolveThreats(allthreats);

		}

		return plan;

	}

	private void resolveThreats(ArrayList<Threat> allthreats) {
		if (!allthreats.isEmpty()) {
			Threat t = allthreats.remove(0);
			plan.getOrderingConstraints().remove(t.getThreatAfterAi());
			plan.getOrderingConstraints().remove(t.getThreatBeforeAj());
			if (!t.getThreatAfterAi().getFirst().equals(start)) {
				// posso promover
				Ordering promotion = new Ordering(t.getThreatAfterAi()
						.getSecond(), t.getThreatAfterAi().getFirst());
				plan.getOrderingConstraints().add(promotion);
				allthreats = findAllThreatsInPlan();
				if (!allthreats.isEmpty()) {
					// promoçao nao resultou. demover.
					plan.getOrderingConstraints().remove(promotion);
					Ordering demotion = new Ordering(t.getThreatBeforeAj()
							.getSecond(), t.getThreatBeforeAj().getFirst());
					plan.getOrderingConstraints().add(demotion);
					allthreats = findAllThreatsInPlan();
					if (!allthreats.isEmpty()) {
						// demoçao nao resultou. Remover acção.
						Action toRemove = t.getThreatAfterAi().getSecond();
						List<Ordering> newOrdering = new ArrayList<Ordering>();
						for (Ordering o : plan.getOrderingConstraints()) {
							if (!o.getFirst().equals(toRemove)
									&& !o.getSecond().equals(toRemove)) {
								newOrdering.add(o);
							}
						}
						plan.setOrderingConstraints(newOrdering);
						List<CausalLink> newLinks = new ArrayList<CausalLink>();
						for (CausalLink cl : plan.getCausalLinks()) {
							if (!cl.getFirst().equals(toRemove)
									&& !cl.getSecond().equals(toRemove)) {
								newLinks.add(cl);
							} else if (cl.getFirst().equals(toRemove)) {
								agenda.add(
										0,
										new AgendaElement(cl.getAchieved(), cl
												.getSecond()));
							}
						}
						plan.getActions().remove(toRemove);

					}
				}

			} else {
				// nao posso por acçoes antes da inicial
				if (!t.getThreatBeforeAj().getSecond().equals(finish)) {
					// demover
					Ordering demotion = new Ordering(t.getThreatBeforeAj()
							.getSecond(), t.getThreatBeforeAj().getFirst());
					plan.getOrderingConstraints().add(demotion);
					allthreats = findAllThreatsInPlan();
					if (!allthreats.isEmpty()) {
						// nao posso demover e nao posso fazer mais nada.
						// remover acção
						Action toRemove = t.getThreatAfterAi().getSecond();
						List<Ordering> newOrdering = new ArrayList<Ordering>();
						for (Ordering o : plan.getOrderingConstraints()) {
							if (!o.getFirst().equals(toRemove)
									&& !o.getSecond().equals(toRemove)) {
								newOrdering.add(o);
							}
						}
						debug("removing " + newOrdering + " from plan");
						plan.setOrderingConstraints(newOrdering);

						List<CausalLink> newLinks = new ArrayList<CausalLink>();
						for (CausalLink cl : plan.getCausalLinks()) {
							if (!cl.getFirst().equals(toRemove)
									&& !cl.getSecond().equals(toRemove)) {
								newLinks.add(cl);
							} else if (cl.getFirst().equals(toRemove)) {
								agenda.add(
										0,
										new AgendaElement(cl.getAchieved(), cl
												.getSecond()));
							}
						}
						debug("removing " + newLinks + " from plan");
						plan.setCausalLinks(newLinks);
						debug("removing " + toRemove + " from plan");
						plan.getActions().remove(toRemove);
					}

				} else {
					// nao posso por acçoes depois da final. escolher outra
					// acção
					Action toRemove = t.getThreatAfterAi().getSecond();
					List<Ordering> newOrdering = new ArrayList<Ordering>();
					for (Ordering o : plan.getOrderingConstraints()) {
						if (!o.getFirst().equals(toRemove)
								&& !o.getSecond().equals(toRemove)) {
							newOrdering.add(o);
						}
					}
					debug("removing " + newOrdering + " from plan");
					plan.setOrderingConstraints(newOrdering);

					List<CausalLink> newLinks = new ArrayList<CausalLink>();
					for (CausalLink cl : plan.getCausalLinks()) {
						if (!cl.getFirst().equals(toRemove)
								&& !cl.getSecond().equals(toRemove)) {
							newLinks.add(cl);
						} else if (cl.getFirst().equals(toRemove)) {
							agenda.add(0, new AgendaElement(cl.getAchieved(),
									cl.getSecond()));
						}
					}
					debug("removing " + newLinks + " from plan");
					plan.setCausalLinks(newLinks);
					debug("removing " + toRemove + " from plan");
					plan.getActions().remove(toRemove);
				}
			}
		}
		debug("Current threats: " + allthreats);
	}

	private ArrayList<Threat> findAllThreatsInPlan() {
		ArrayList<Threat> allthreats = new ArrayList<Threat>();
		for (Action planAction : plan.getActions()) {
			for (CausalLink cl : plan.getCausalLinks()) {
				Ordering aAfterAi = findMatchingOrder(cl.getFirst(),
						planAction, plan.getOrderingConstraints());
				Ordering aBeforeAj = findMatchingOrder(planAction,
						cl.getSecond(), plan.getOrderingConstraints());
				if (aAfterAi != null && aBeforeAj != null) {
					Literal preC = cl.getAchieved();
					if (planAction.effectsContainLiteralNegation(preC,
							varManager.getVariablesUsed(),
							plan.getVariableBindings())) {
						allthreats.add(new Threat(aAfterAi, aBeforeAj, cl));
					}
				}
			}
		}
		return allthreats;
	}

	private boolean updateVariableBindings(Literal preCondition,
			boolean variableThreatExisting,
			List<VariableBinding> variablesCache, Action ai, CausalLink link,
			Ordering o) {
		if (preCondition.containsVariables(varManager.getVariablesUsed())
				&& ai.isFullyInstantiated(varManager.getVariablesUsed())) {
			for (String var : preCondition.getActualArguments()) {
				/** Update variable bindings if necessary **/
				if (varManager.getVariablesUsed().contains(var)) {
					int index = preCondition.getActualArguments().indexOf(var);
					for (Literal l : ai.getEffects()) {
						if (l.getName().equals(preCondition.getName())
								&& l.argumentsMatch(preCondition,
										varManager.getVariablesUsed(),
										plan.getVariableBindings())) {
							String possibleValue = l.getActualArguments().get(
									index);
							VariableBinding newVar = new VariableBinding(var,
									possibleValue);
							for (String varName : plan.getVariableBindings()
									.keySet()) {
								if (varName.equals(var)
										&& !plan.getVariableBindings()
												.get(varName)
												.equals(possibleValue)) {
									variableThreatExisting = true;
									plan.getCausalLinks().remove(link);
									plan.getOrderingConstraints().remove(o);
									System.out
											.println("Não posso usar a acção "
													+ ai
													+ " porque já existem bindings");
								}
							}
							if (!variableThreatExisting) {
								plan.getVariableBindings().put(var,
										possibleValue);
								variablesCache.add(newVar);
								debug("Adding " + newVar
										+ " to variable bindings");
							}
						}
					}
				}
			}
		}
		return variableThreatExisting;
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
			debug("Adding " + l + " to agenda");
		}
	}

	private List<Action> providers(Literal preCondition, Plan p) {
		List<Action> providers = new ArrayList<Action>();
		getRelevantActionsfromPlan(preCondition, providers);
		getRelevantActionsFromDomain(preCondition, providers);
		return providers;

	}

	private void getRelevantActionsFromDomain(Literal preCondition,
			List<Action> providers) {
		for (Action possible : domainActions) {
			for (Literal effect : possible.getEffects()) {
				boolean equalNames = effect.getName().equals(
						preCondition.getName());
				boolean equalValues = effect.getValue() == preCondition
						.getValue();
				if (equalNames && equalValues) {
					Action actionCopy = possible.createCopy();
					Literal literalCopy = effect.createCopy();
					literalCopy.setActualArguments(preCondition
							.getActualArguments());
					actionCopy.instantiate(literalCopy, varManager);
					providers.add(actionCopy);
				}
			}
		}

	}

	private void getRelevantActionsfromPlan(Literal preCondition,
			List<Action> providers) {
		for (Action possibleAction : plan.getActions()) {
			for (Literal literalToCompare : possibleAction.getEffects()) {
				boolean equalNames = literalToCompare.getName().equals(
						preCondition.getName());
				boolean equalValues = literalToCompare.getValue() == preCondition
						.getValue();
				if (equalNames && equalValues) {
					boolean equals = true;
					int argumentsSize = preCondition.getActualArguments()
							.size();
					for (int i = 0; i < argumentsSize; i++) {
						String preConditionArg = preCondition
								.getActualArguments().get(i);
						String literalArg = literalToCompare
								.getActualArguments().get(i);
						boolean isVariableBinding = varManager
								.getVariablesUsed().contains(preConditionArg);
						boolean equalArgs = preConditionArg.equals(literalArg);
						if (!isVariableBinding && !equalArgs) {
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

	private static final boolean DEBUG = true;

	static void debug(String content) {
		if (DEBUG) {
			System.out.println(content);
		}
	}

}
