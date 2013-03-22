package testing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		/*
		Map<String, String> onArgs = new HashMap<>();
		onArgs.put("?a", null);
		onArgs.put("?b", null);
		Literal on = new Literal("on", onArgs, true);
		
		Map<String, String> clearArgs = new HashMap<>();
		clearArgs.put("?b", null);
		Literal clear = new Literal("clear", clearArgs, true);
		
		Map<String, String> onTableArgs = new HashMap<>();
		onTableArgs.put("?b", null);
		Literal onTable = new Literal("ontable", onTableArgs, true);
		*/
		
		Action finish = finishAction();
		List<AgendaElement> agenda = new ArrayList<AgendaElement>();
		for(Literal l : finish.getPreConditions()){
			AgendaElement element = new AgendaElement(l, finish);
			agenda.add(element);
		}
		
		List<Action> domain = new ArrayList<Action>();
		domain.add(move());
		domain.add(stack());
		domain.add(unstack());
		Domain d = new Domain(domain);
		
		List<Action> actions = new ArrayList<Action>();
		actions.add(startAction());
		List<Ordering> ordering = new ArrayList<Ordering>();
		List<CausalLink> links = new ArrayList<CausalLink>();
		Plan plan = new Plan(actions, ordering, links);
		
		PoP p = new PoP(d);
		
		Plan finalPlan = p.pop(plan, agenda);
		
		System.out.println(finalPlan.getActions());
		for(CausalLink c : finalPlan.getCausalLinks()){
			System.out.println(c.getFirst().getName() + " -> " + c.getAchieved() + " -> " + c.getSecond().getName() );
		}
		
	}
	
	public static Action finishAction(){
		List<Literal> preConditions = new ArrayList<Literal>();
		
		Map<String, String> onTableCArgs = new HashMap<>();
		onTableCArgs.put("?b", "c");
		Literal onTableC = new Literal("ontable", onTableCArgs, true);
		
		Map<String, String> onBCArgs = new HashMap<>();
		onBCArgs.put("?a", "b");
		onBCArgs.put("?b", "c");
		Literal onBC = new Literal("on", onBCArgs, true);
		
		Map<String, String> onABArgs = new HashMap<>();
		onABArgs.put("?a", "a");
		onABArgs.put("?b","b");
		Literal onAB = new Literal("on", onABArgs, true);
		
		preConditions.add(onTableC);
		preConditions.add(onBC);
		preConditions.add(onAB);
		
		Action a = new Action(preConditions, new ArrayList<Literal>(), "finish", new HashMap<String, String>());
		return a;
		
		
	}
	
	public static Action startAction(){
		List<Literal> postConditions = new ArrayList<Literal>();
		Map<String, String> onTableArgs = new HashMap<>();
		onTableArgs.put("?b", "a");
		Literal onTableA = new Literal("ontable", onTableArgs, true);
		
		Map<String, String> onArgs = new HashMap<>();
		onArgs.put("?a", "b");
		onArgs.put("?b", "a");
		Literal onBA = new Literal("on", onArgs, true);
		
		Map<String, String> onCBrgs = new HashMap<>();
		onCBrgs.put("?a", "c");
		onCBrgs.put("?b", "b");
		Literal onCB = new Literal("on", onCBrgs, true);
		
		Map<String, String> clearArgs = new HashMap<>();
		clearArgs.put("?b", "c");
		Literal clearC = new Literal("clear", clearArgs, true);
		
		postConditions.add(onTableA);
		postConditions.add(onBA);
		postConditions.add(onCB);
		postConditions.add(clearC);
		
		Action a = new Action(new ArrayList<Literal>(), postConditions, "start", new HashMap<String, String>());
		return a;
		
		
	}
	
	
	public static List<Literal> goal(){
		List<Literal> goal = new ArrayList<Literal>();
		
		Map<String, String> onTableCArgs = new HashMap<>();
		onTableCArgs.put("?b", "c");
		Literal onTableC = new Literal("ontable", onTableCArgs, true);
		
		Map<String, String> onBCArgs = new HashMap<>();
		onBCArgs.put("?a", "b");
		onBCArgs.put("?b", "c");
		Literal onBC = new Literal("on", onBCArgs, true);
		
		Map<String, String> onABArgs = new HashMap<>();
		onABArgs.put("?a", "a");
		onABArgs.put("?b","b");
		Literal onAB = new Literal("on", onABArgs, true);
		
		goal.add(onTableC);
		goal.add(onBC);
		goal.add(onAB);
		
		return goal;
		
		
	}
	
	
	public static List<Literal> start(){
		List<Literal> startState = new ArrayList<Literal>();
		
		Map<String, String> onTableArgs = new HashMap<>();
		onTableArgs.put("?b", "a");
		Literal onTableA = new Literal("ontable", onTableArgs, true);
		
		Map<String, String> onArgs = new HashMap<>();
		onArgs.put("?a", "b");
		onArgs.put("?b", "a");
		Literal onBA = new Literal("on", onArgs, true);
		
		Map<String, String> onCBrgs = new HashMap<>();
		onCBrgs.put("?a", "c");
		onCBrgs.put("?b", "b");
		Literal onCB = new Literal("on", onCBrgs, true);
		
		Map<String, String> clearArgs = new HashMap<>();
		clearArgs.put("?b", "c");
		Literal clearC = new Literal("clear", clearArgs, true);
		
		startState.add(onTableA);
		startState.add(onBA);
		startState.add(onCB);
		startState.add(clearC);
		
		return startState;
	}
	
	
	public static Action unstack(){
		//----args----//
		Map<String, String> args = new HashMap<String, String>();
		args.put("?a", null);
		args.put("?b", null);
		//-----------//

		//-------PreConditions------//
		List<Literal> preConditions = new ArrayList<Literal>();
		
		Map<String, String> clearAArgs = new HashMap<>();
		clearAArgs.put("?a", null);
		Literal clearA = new Literal("clear", clearAArgs, true);//clear(?a)
		
		Map<String, String> onABArgs = new HashMap<>();
		onABArgs.put("?a", null);
		onABArgs.put("?b", null);
		Literal onAB = new Literal("on", onABArgs, true); //on(?a?b)
		
		
		preConditions.add(clearA);
		preConditions.add(onAB);
		//--------------------------//	
		
		//--------PostConditions--------//
		List<Literal> postConditions = new ArrayList<Literal>();
		
		Map<String, String> clearBArgs = new HashMap<>();
		clearBArgs.put("?b", null);
		Literal clearB = new Literal("clear", clearBArgs, true);//clear(?b)
		
		Map<String, String> onTableAArgs = new HashMap<>();
		onTableAArgs.put("?a", null);
		Literal onTableA = new Literal("ontable", onTableAArgs, true); //onTable(?a)
		
		Map<String, String> notOnABArgs = new HashMap<>();
		notOnABArgs.put("?a", null);
		notOnABArgs.put("?b", null);
		Literal notOnAB = new Literal("on", notOnABArgs, false); //not on(?a?b)
	
		postConditions.add(clearB);
		postConditions.add(onTableA);
		postConditions.add(notOnAB);
		//------------------------------//
		
		Action a = new Action(preConditions, postConditions, "unstack", args);
		return a;

	}
	
	public static Action stack(){
		//----args----//
				Map<String, String> args = new HashMap<String, String>();
				args.put("?a", null);
				args.put("?b", null);
		//-----------//
		
		//-------PreConditions------//
				List<Literal> preConditions = new ArrayList<Literal>();
				
				Map<String, String> clearBArgs = new HashMap<>();
				clearBArgs.put("?b", null);
				Literal clearB = new Literal("clear", clearBArgs, true);//clear(?b)
				
				Map<String, String> onTableAArgs = new HashMap<>();
				onTableAArgs.put("?a", null);
				Literal onTableA = new Literal("ontable", onTableAArgs, true); //onTable(?a)
				
				Map<String, String> clearAArgs = new HashMap<>();
				clearAArgs.put("?a", null);
				Literal clearA = new Literal("clear", clearAArgs, true);//clear(?a)
				
				preConditions.add(clearB);
				preConditions.add(onTableA);
				preConditions.add(clearA);
		//--------------------------//	
				
		//--------PostConditions--------//
				List<Literal> postConditions = new ArrayList<Literal>();
				
				Map<String, String> onABArgs = new HashMap<>();
				onABArgs.put("?a", null);
				onABArgs.put("?b", null);
				Literal onAB = new Literal("on", onABArgs, true); //on(?a?b)
				
				Map<String, String> notClearBArgs = new HashMap<>();
				notClearBArgs.put("?b", null);
				Literal notClearB = new Literal("clear", notClearBArgs, false);//not clear(?b)
				
				Map<String, String> notOnTableAArgs = new HashMap<>();
				notOnTableAArgs.put("?a", null);
				Literal notOnTableA = new Literal("ontable", notOnTableAArgs, false); //not onTable(?a)
				
				postConditions.add(onAB);
				postConditions.add(notClearB);
				postConditions.add(notOnTableA);
				
		//------------------------------//
				
				Action a = new Action(preConditions, postConditions, "stack", args);
				return a;
		
	}
	
	public static Action move(){
		//args
		Map<String, String> args = new HashMap<String, String>();
		args.put("?a", null);
		args.put("?b", null);
		args.put("?c", null);
		//-------------//
		//Define preConditions Literals//
		List<Literal> preConditions = new ArrayList<Literal>();
		
		Map<String, String> onABArgs = new HashMap<>();
		onABArgs.put("?a", null);
		onABArgs.put("?b", null);
		Literal onAB = new Literal("on", onABArgs, true); //on(?a?b)
		
		Map<String, String> clearCArgs = new HashMap<>();
		clearCArgs.put("?c", null);
		Literal clearC = new Literal("clear", clearCArgs, true);//clear(?c)
		
		Map<String, String> clearAArgs = new HashMap<>();
		clearAArgs.put("?a", null);
		Literal clearA = new Literal("clear", clearAArgs, true);//clear(?a)
		
		preConditions.add(onAB);
		preConditions.add(clearC);
		preConditions.add(clearA);
		//----------//
		//Define postCOnditions literals
		List<Literal> postConditions = new ArrayList<Literal>();
		
		Map<String, String> clearBArgs = new HashMap<>();
		clearBArgs.put("?b", null);
		Literal clearB = new Literal("clear", clearBArgs, true);//clear(?b)
		
		Map<String, String> onACArgs = new HashMap<>();
		onACArgs.put("?a", null);
		onACArgs.put("?c", null);
		Literal onAC = new Literal("on", onACArgs, true); //on(?a?c)
		
		Map<String, String> notOnABArgs = new HashMap<>();
		notOnABArgs.put("?a", null);
		notOnABArgs.put("?b", null);
		Literal notOnAB = new Literal("on", notOnABArgs, false); //not on(?a?b)
		
		Map<String, String> notClearCArgs = new HashMap<>();
		notClearCArgs.put("?c", null);
		Literal notClearC = new Literal("clear", notClearCArgs, false);//not clear(?c)
		
		postConditions.add(clearB);
		postConditions.add(onAC);
		postConditions.add(notOnAB);
		postConditions.add(notClearC);
		//------------//
		Action a = new Action(preConditions, postConditions, "move", args);
		return a;
	}
}
