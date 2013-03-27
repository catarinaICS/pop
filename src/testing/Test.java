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
//		List<Literal> start = start();
//		System.out.println(start);
//		for(Literal l : start){
//			System.out.println(l.getArgs().values());
//		}
//		
		
		
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
		finalPlan.getActions();
		System.out.println(finalPlan.getActions());
//		System.out.println(finalPlan.getActions());
		for(CausalLink c : finalPlan.getCausalLinks()){
			System.out.println(c.getFirst().getName() + " -> " + c.getAchieved() + " -> " + c.getSecond().getName() );
		}
		
	}
	
	public static Action finishAction(){
		List<Literal> preConditions = new ArrayList<Literal>();
		
//		Map<String, String> onTableCArgs = new HashMap<>();
//		onTableCArgs.put("?b", "c");
		List<String> onTableCFormalArgs = new ArrayList<String>();
		onTableCFormalArgs.add("?b");
		List<String> onTableCActualArgs = new ArrayList<String>();
		onTableCActualArgs.add("c");
		Literal onTableC = new Literal("ontable", onTableCFormalArgs, onTableCActualArgs, true);
//		Literal onTableC = new Literal("ontable", onTableCArgs, true);
		
		
//		Map<String, String> onBCArgs = new HashMap<>();
//		onBCArgs.put("?a", "b");
//		onBCArgs.put("?b", "c");
		List<String> onBCFormalArgs = new ArrayList<String>();
		onBCFormalArgs.add("?a");
		onBCFormalArgs.add("?b");
		List<String> onBCActualArgs = new ArrayList<String>();
		onBCActualArgs.add("b");
		onBCActualArgs.add("c");
		Literal onBC = new Literal("on", onBCFormalArgs, onBCActualArgs, true);
//		Literal onBC = new Literal("on", onBCArgs, true);
		
//		Map<String, String> onABArgs = new HashMap<>();
//		onABArgs.put("?a", "a");
//		onABArgs.put("?b","b");
		List<String> onABFormalArgs = new ArrayList<String>();
		onABFormalArgs.add("?a");
		onABFormalArgs.add("?b");
		List<String> onABActualArgs = new ArrayList<String>();
		onABActualArgs.add("a");
		onABActualArgs.add("b");
		Literal onAB = new Literal("on", onABFormalArgs, onABActualArgs, true);
//		Literal onAB = new Literal("on", onABArgs, true);
		
		
		preConditions.add(onTableC);
		preConditions.add(onBC);
		preConditions.add(onAB);
		
		Action finish = new Action(preConditions, new ArrayList<Literal>(), "finish", new ArrayList<String>(), new ArrayList<String>());
		
//		Action a = new Action(preConditions, new ArrayList<Literal>(), "finish", new HashMap<String, String>());
		
		return finish;
		
		
	}
	
	public static Action startAction(){
		List<Literal> postConditions = new ArrayList<Literal>();
//		Map<String, String> onTableArgs = new HashMap<>();
//		onTableArgs.put("?b", "a");
//		Literal onTableA = new Literal("ontable", onTableArgs, true);
		
		List<String> onTableAFormalArgs = new ArrayList<String>();
		onTableAFormalArgs.add("?b");
		List<String> onTableAActualArgs = new ArrayList<String>();
		onTableAActualArgs.add("a");
		Literal onTableA = new Literal("ontable", onTableAFormalArgs, onTableAActualArgs, true);
		
//		Map<String, String> onArgs = new HashMap<>();
//		onArgs.put("?a", "b");
//		onArgs.put("?b", "a");
//		Literal onBA = new Literal("on", onArgs, true);
		
		List<String> onBAFormalArgs = new ArrayList<String>();
		onBAFormalArgs.add("?a");
		onBAFormalArgs.add("?b");
		List<String> onBAActualArgs = new ArrayList<String>();
		onBAActualArgs.add("b");
		onBAActualArgs.add("a");
		Literal onBA = new Literal("on", onBAFormalArgs, onBAActualArgs, true);
		
//		Map<String, String> onCBrgs = new HashMap<>();
//		onCBrgs.put("?a", "c");
//		onCBrgs.put("?b", "b");
//		Literal onCB = new Literal("on", onCBrgs, true);
		
		List<String> onCBFormalArgs = new ArrayList<String>();
		onCBFormalArgs.add("?a");
		onCBFormalArgs.add("?b");
		List<String> onCBActualArgs = new ArrayList<String>();
		onCBActualArgs.add("c");
		onCBActualArgs.add("b");
		Literal onCB = new Literal("on", onCBFormalArgs, onCBActualArgs, true);
		
//		Map<String, String> clearArgs = new HashMap<>();
//		clearArgs.put("?b", "c");
//		Literal clearC = new Literal("clear", clearArgs, true);
		
		List<String> clearCFormalArgs = new ArrayList<String>();
		clearCFormalArgs.add("?b");
		List<String> clearCActualArgs = new ArrayList<String>();
		clearCActualArgs.add("c");
		Literal clearC = new Literal("clear", clearCFormalArgs, clearCActualArgs, true);
		
		
		postConditions.add(onTableA);
		postConditions.add(onBA);
		postConditions.add(onCB);
		postConditions.add(clearC);
		
//		Action a = new Action(new ArrayList<Literal>(), postConditions, "start", new HashMap<String, String>());
		Action start = new Action(new ArrayList<Literal>(), postConditions, "start", new ArrayList<String>(), new ArrayList<String>());
		return start;
		
		
	}
	
	
//	public static List<Literal> goal(){
//		List<Literal> goal = new ArrayList<Literal>();
//		
//		Map<String, String> onTableCArgs = new HashMap<>();
//		onTableCArgs.put("?b", "c");
//		Literal onTableC = new Literal("ontable", onTableCArgs, true);
//		
//		Map<String, String> onBCArgs = new HashMap<>();
//		onBCArgs.put("?a", "b");
//		onBCArgs.put("?b", "c");
//		Literal onBC = new Literal("on", onBCArgs, true);
//		
//		Map<String, String> onABArgs = new HashMap<>();
//		onABArgs.put("?a", "a");
//		onABArgs.put("?b","b");
//		Literal onAB = new Literal("on", onABArgs, true);
//		
//		goal.add(onTableC);
//		goal.add(onBC);
//		goal.add(onAB);
//		
//		return goal;
//		
//		
//	}
	
	
//	public static List<Literal> start(){
//		List<Literal> startState = new ArrayList<Literal>();
//		
//		Map<String, String> onTableArgs = new HashMap<>();
//		onTableArgs.put("?b", "a");
//		Literal onTableA = new Literal("ontable", onTableArgs, true);
//		
//		Map<String, String> onArgs = new HashMap<>();
//		onArgs.put("?a", "b");
//		onArgs.put("?b", "a");
//		Literal onBA = new Literal("on", onArgs, true);
//		
//		Map<String, String> onCBrgs = new HashMap<>();
//		onCBrgs.put("?a", "c");
//		onCBrgs.put("?b", "b");
//		Literal onCB = new Literal("on", onCBrgs, true);
//		
//		Map<String, String> clearArgs = new HashMap<>();
//		clearArgs.put("?b", "c");
//		Literal clearC = new Literal("clear", clearArgs, true);
//		
//		startState.add(onTableA);
//		startState.add(onBA);
//		startState.add(onCB);
//		startState.add(clearC);
//		
//		return startState;
//	}
	
	
	public static Action unstack(){
		//----args----//
//		Map<String, String> args = new HashMap<String, String>();
//		args.put("?a", null);
//		args.put("?b", null);
		
		List<String> unstackFormalArgs = new ArrayList<String>();
		unstackFormalArgs.add("?a");
		unstackFormalArgs.add("?b");
		
		//-----------//

		//-------PreConditions------//
		List<Literal> preConditions = new ArrayList<Literal>();
		List<String> clearAFormalArgs = new ArrayList<String>();
		clearAFormalArgs.add("?a");
		Literal clearA = new Literal("clear", clearAFormalArgs, new ArrayList<String>(), true);
//		Literal clearA = new Literal("clear", clearAFormalArgs, true);//clear(?a)
		
		
		
		
		List<String> onABFormalArgs = new ArrayList<String>();
		onABFormalArgs.add("?a");
		onABFormalArgs.add("?b");
		Literal onAB = new Literal("on", onABFormalArgs, new ArrayList<String>(), true); //on(?a?b)
		
		
		preConditions.add(clearA);
		preConditions.add(onAB);
		//--------------------------//	
		
		//--------PostConditions--------//
		List<Literal> postConditions = new ArrayList<Literal>();
		
		List<String> clearBFormalArgs = new ArrayList<String>();
		clearBFormalArgs.add("?b");
		Literal clearB = new Literal("clear", clearBFormalArgs, new ArrayList<String>(), true);//clear(?b)
		
		List<String> onTableAFormalArgs = new ArrayList<String>();
		onTableAFormalArgs.add("?a");
		Literal onTableA = new Literal("ontable", onTableAFormalArgs, new ArrayList<String>(), true); //onTable(?a)
		
		List<String> notOnABFormalArgs = new ArrayList<String>();
		notOnABFormalArgs.add("?a");
		notOnABFormalArgs.add("?b");
		Literal notOnAB = new Literal("on", notOnABFormalArgs, new ArrayList<String>() ,false); //not on(?a?b)
	
		postConditions.add(clearB);
		postConditions.add(onTableA);
		postConditions.add(notOnAB);
		//------------------------------//
		
		Action a = new Action(preConditions, postConditions, "unstack", unstackFormalArgs, new ArrayList<String>());
		return a;

	}
	
	public static Action stack(){
		//----args----//
				List<String> stackFormalArgs = new ArrayList<String>();
				stackFormalArgs.add("?a");
				stackFormalArgs.add("?b");
		//-----------//
		
		//-------PreConditions------//
				List<Literal> preConditions = new ArrayList<Literal>();
				
				List<String> clearBFormalArgs = new ArrayList<String>();
				clearBFormalArgs.add("?b");
				Literal clearB = new Literal("clear", clearBFormalArgs,new ArrayList<String>() , true);//clear(?b)
				
				List<String> onTableAFormalArgs = new ArrayList<String>();
				onTableAFormalArgs.add("?a");
				Literal onTableA = new Literal("ontable", onTableAFormalArgs, new ArrayList<String>(), true); //onTable(?a)
				
				List<String> clearAFormalArgs = new ArrayList<String>();
				clearAFormalArgs.add("?a");
				Literal clearA = new Literal("clear", clearAFormalArgs, new ArrayList<String>(), true);//clear(?a)
				
				preConditions.add(clearB);
				preConditions.add(onTableA);
				preConditions.add(clearA);
		//--------------------------//	
				
		//--------PostConditions--------//
				List<Literal> postConditions = new ArrayList<Literal>();
				
				List<String> onABFormalArgs = new ArrayList<String>();
				onABFormalArgs.add("?a");
				onABFormalArgs.add("?b");
				Literal onAB = new Literal("on", onABFormalArgs, new ArrayList<String>(), true); //on(?a?b)
				
				List<String> notClearBFormalArgs = new ArrayList<String>();
				notClearBFormalArgs.add("?b");
				Literal notClearB = new Literal("clear", notClearBFormalArgs, new ArrayList<String>(), false);//not clear(?b)
				
				List<String> notOnTableAFormalArgs = new ArrayList<String>();
				notOnTableAFormalArgs.add("?a");
				Literal notOnTableA = new Literal("ontable", notOnTableAFormalArgs, new ArrayList<String>(), false); //not onTable(?a)
				
				postConditions.add(onAB);
				postConditions.add(notClearB);
				postConditions.add(notOnTableA);
				
		//------------------------------//
				
				Action a = new Action(preConditions, postConditions, "stack", stackFormalArgs, new ArrayList<String>());
				return a;
		
	}
	
	public static Action move(){
		//args
		List<String> moveFormalArgs = new ArrayList<String>();
		moveFormalArgs.add("?a");
		moveFormalArgs.add("?b");
		moveFormalArgs.add("?c");
		//-------------//
		//Define preConditions Literals//
		List<Literal> preConditions = new ArrayList<Literal>();
		
		List<String> onABFormalArgs = new ArrayList<String>();
		onABFormalArgs.add("?a");
		onABFormalArgs.add("?b");
		Literal onAB = new Literal("on", onABFormalArgs, new ArrayList<String>(), true); //on(?a?b)
		
		List<String> clearCArgs = new ArrayList<String>();
		clearCArgs.add("?c");
		Literal clearC = new Literal("clear", clearCArgs, new ArrayList<String>(), true);//clear(?c)
		
		List<String> clearAArgs = new ArrayList<String>();
		clearAArgs.add("?a");
		Literal clearA = new Literal("clear", clearAArgs, new ArrayList<String>(), true);//clear(?a)
		
		preConditions.add(onAB);
		preConditions.add(clearC);
		preConditions.add(clearA);
		//----------//
		//Define postCOnditions literals
		List<Literal> postConditions = new ArrayList<Literal>();
		
		List<String> clearBArgs = new ArrayList<String>();
		clearBArgs.add("?b");
		Literal clearB = new Literal("clear", clearBArgs, new ArrayList<String>(), true);//clear(?b)
		
		List<String> onACArgs = new ArrayList<String>();
		onACArgs.add("?a");
		onACArgs.add("?c");
		Literal onAC = new Literal("on", onACArgs, new ArrayList<String>(), true); //on(?a?c)
		
		List<String> notOnABArgs = new ArrayList<String>();
		notOnABArgs.add("?a");
		notOnABArgs.add("?b");
		Literal notOnAB = new Literal("on", notOnABArgs, new ArrayList<String>(), false); //not on(?a?b)
		
		List<String> notClearCArgs = new ArrayList<String>();
		notClearCArgs.add("?c");
		Literal notClearC = new Literal("clear", notClearCArgs, new ArrayList<String>(), false);//not clear(?c)
		
		postConditions.add(clearB);
		postConditions.add(onAC);
		postConditions.add(notOnAB);
		postConditions.add(notClearC);
		//------------//
		Action a = new Action(preConditions, postConditions, "move", moveFormalArgs, new ArrayList<String>());
		return a;
	}
}
