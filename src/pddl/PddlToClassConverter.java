package pddl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import objects.Action;
import uk.ac.bham.cs.zas.pddl.domain.ActionDef;
import uk.ac.bham.cs.zas.pddl.domain.Domain;
import uk.ac.bham.cs.zas.pddl.domain.Effect;
import uk.ac.bham.cs.zas.pddl.domain.FormalArgument;
import uk.ac.bham.cs.zas.pddl.domain.GoalDesc;
import uk.ac.bham.cs.zas.pddl.domain.Literal;
import uk.ac.bham.cs.zas.pddl.domain.PDDLObject;
import uk.ac.bham.cs.zas.pddl.domain.PredicateHeader;
import uk.ac.bham.cs.zas.pddl.domain.PredicateInstance;
import uk.ac.bham.cs.zas.pddl.domain.PredicateLiteral;
import uk.ac.bham.cs.zas.pddl.domain.Problem;
import uk.ac.bham.cs.zas.pddl.goalseffects.ConjunctionEffect;
import uk.ac.bham.cs.zas.pddl.goalseffects.ConjunctionGoalDesc;
import uk.ac.bham.cs.zas.pddl.goalseffects.PredicateEffect;
import uk.ac.bham.cs.zas.pddl.parser.InvalidPDDLElementException;
import uk.ac.bham.cs.zas.pddl.parser.PDDLSyntaxException;

public class PddlToClassConverter {
	
	private Domain pddlDomain;
	private Problem problem;
	
	private Action init;
	private Action finish;
	private List<Action> domainActions = new ArrayList<Action>();
	
	public PddlToClassConverter() throws IOException, PDDLSyntaxException, InvalidPDDLElementException {
		PDDLParser pddl_parser = new PDDLParser();
		pddlDomain = pddl_parser.parsePDDLDomainFile(new File("data/blocks.skills"));
		if (pddlDomain != null) {
			problem = pddl_parser.parsePDDLProblemFile(pddlDomain, new File("data/blocks1.problems"));
			instantiateDomain();
			instantiateProblem();
		}
			
	}
	
	public Action getInitAction(){
		return init;
	}
	
	public Action getFinishAction(){
		return finish;
	}
	
	public List<Action> getDomainActions() {
		return domainActions;
	}
	
	
	private void instantiateDomain(){
		for(ActionDef domainAction : pddlDomain.getActions()){
			String actionName = domainAction.getName();
			//formal args da ac�ao
			List<String> actionArgs = instantiateFormalArgs(domainAction.getArguments());
			
			List<objects.Literal> actionPreConditions = getActionPreConditions(domainAction);
			List<objects.Literal> actionEffects = getActionEffects(domainAction);
			
			Action newAction = new Action(actionPreConditions, actionEffects, actionName, actionArgs, new ArrayList<String>());
			domainActions.add(newAction);
			
		}
	}

	private List<objects.Literal> getActionEffects(ActionDef domainAction) {
		List<objects.Literal> actionEffects = new ArrayList<objects.Literal>();
		ConjunctionEffect e = (ConjunctionEffect) domainAction.getEffect();
		for(Effect effect : e.getSubEffects()){
			PredicateEffect effectInstance = (PredicateEffect) effect;
			PredicateHeader effectHeader = (PredicateHeader) effectInstance.target;
			boolean value = effectInstance.value;
			String effectName = effectHeader.definition.getName();
			List<String> effectArgs = instantiateFormalArgs(effectHeader.arguments);
			objects.Literal newEffect = new objects.Literal(effectName, effectArgs, new ArrayList<String>(), value);
			actionEffects.add(newEffect);
		}
		return actionEffects;
	}

	private List<objects.Literal> getActionPreConditions(ActionDef domainAction) {
		List<objects.Literal> actionPreConditions = new ArrayList<objects.Literal>();
		ConjunctionGoalDesc preC = (ConjunctionGoalDesc) domainAction.getPreCondition();
		for(GoalDesc preCondition : preC.getSubGoals()){
			PredicateHeader precondition = (PredicateHeader) preCondition;
			String preConditionName = precondition.definition.getName();
			List<String> preConditionArgs = instantiateFormalArgs(precondition.arguments);
			objects.Literal newPreCondition = new objects.Literal(preConditionName, preConditionArgs, new ArrayList<String>(), true);
			actionPreConditions.add(newPreCondition);
		}
		return actionPreConditions;
	}
	
	private void instantiateProblem(){
		instantiateStartAction();
		instantiateFinishAction();
	}

	private void instantiateFinishAction() { //ac��o s� com pre condi�oes
		List<objects.Literal> preConditions = new ArrayList<objects.Literal>();
		ConjunctionGoalDesc goal = (ConjunctionGoalDesc) problem.getGoal();
		for(GoalDesc subGoal : goal.getSubGoals()){
			PredicateInstance subGoalInstance = (PredicateInstance) subGoal;
			String litName = subGoalInstance.getDefinition().getName();
			List<String> formalArgs = instantiateFormalArgs(subGoalInstance.getDefinition().getArguments());
			List<String> actualArgs = instantiateActualArgs(subGoalInstance);
			
			objects.Literal newLiteral = new objects.Literal(litName, formalArgs, actualArgs, true);
			preConditions.add(newLiteral);
			
		}
		
		finish = new Action(preConditions, new ArrayList<objects.Literal>(), "finish", new ArrayList<String>(), new ArrayList<String>());
		
		
	}

	private void instantiateStartAction() {
		List<objects.Literal> postConditions = new ArrayList<objects.Literal>();
		for(Literal l : problem.getStartState().getLiterals()){
			PredicateLiteral literal = (PredicateLiteral) l;
			PredicateInstance instance = ((PredicateInstance) l.getInstance());
			String literalName = instance.getDefinition().getName();
			boolean literalValue = literal.getValue();
			List<String> formalArgs = instantiateFormalArgs(instance.getDefinition().getArguments());
			List<String> actualArgs = instantiateActualArgs(instance);
			objects.Literal newLiteral = new objects.Literal(literalName, formalArgs, actualArgs, literalValue);
			postConditions.add(newLiteral);
		}
		init = new Action(null, postConditions, "start", new ArrayList<String>(), new ArrayList<String>());
	}

	private List<String> instantiateActualArgs(PredicateInstance instance) {
		List<String> actualArgs = new ArrayList<String>();
		for(PDDLObject actualArg : instance.getArguments()){
			actualArgs.add(actualArg.getName());
		}
		return actualArgs;
	}

	private List<String> instantiateFormalArgs(List<FormalArgument> args) {
		List<String> formalArgs = new ArrayList<String>();
		for(FormalArgument i : args){
			formalArgs.add(i.getName());
		}
		return formalArgs;
	}
	
	
	

}
