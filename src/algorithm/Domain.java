package algorithm;

import java.util.ArrayList;
import java.util.List;

import objects.Action;
import objects.Literal;

public class Domain {
	
	private List<Action> domainActions = new ArrayList<Action>();
	
	public Domain() {
		super();
		domainActions.add(moveAction());
		domainActions.add(stackAction());
		domainActions.add(unstackAction());
	}



	public List<Action> getDomainActions() {
		return domainActions;
	}
	
	private Action moveAction(){
		//-------Formal Arguments--------//
		List<String> moveFormalArgs = new ArrayList<String>();
		moveFormalArgs.add("?a");
		moveFormalArgs.add("?b");
		moveFormalArgs.add("?c");
		//-------------------------------//
		
		//------Pre-Conditions-----------//
		List<Literal> preConditions = new ArrayList<Literal>();
		//on(?a?b)
		List<String> onABFormalArgs = new ArrayList<String>();
		onABFormalArgs.add("?a");
		onABFormalArgs.add("?b");
		Literal onAB = new Literal("on", onABFormalArgs, new ArrayList<String>(), true); 
		//clear(?c)
		List<String> clearCArgs = new ArrayList<String>();
		clearCArgs.add("?c");
		Literal clearC = new Literal("clear", clearCArgs, new ArrayList<String>(), true);
		//clear(?a)
		List<String> clearAArgs = new ArrayList<String>();
		clearAArgs.add("?a");
		Literal clearA = new Literal("clear", clearAArgs, new ArrayList<String>(), true);
		//add to list
		preConditions.add(onAB);
		preConditions.add(clearC);
		preConditions.add(clearA);
		//--------------------------------//
		
		//------------Post-Conditions------------//
		List<Literal> postConditions = new ArrayList<Literal>();
		//clear(?b)
		List<String> clearBArgs = new ArrayList<String>();
		clearBArgs.add("?b");
		Literal clearB = new Literal("clear", clearBArgs, new ArrayList<String>(), true);
		//on(?a?c)
		List<String> onACArgs = new ArrayList<String>();
		onACArgs.add("?a");
		onACArgs.add("?c");
		Literal onAC = new Literal("on", onACArgs, new ArrayList<String>(), true); 
		//not on(?a?b)
		List<String> notOnABArgs = new ArrayList<String>();
		notOnABArgs.add("?a");
		notOnABArgs.add("?b");
		Literal notOnAB = new Literal("on", notOnABArgs, new ArrayList<String>(), false);
		//not clear(?c)
		List<String> notClearCArgs = new ArrayList<String>();
		notClearCArgs.add("?c");
		Literal notClearC = new Literal("clear", notClearCArgs, new ArrayList<String>(), false);
		//add to list
		postConditions.add(clearB);
		postConditions.add(onAC);
		postConditions.add(notOnAB);
		postConditions.add(notClearC);
		//----------------------------------------//
		Action a = new Action(preConditions, postConditions, "move", moveFormalArgs, new ArrayList<String>());
		return a;
	}

	private Action stackAction(){
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

	private Action unstackAction(){
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
	
	
	
}
