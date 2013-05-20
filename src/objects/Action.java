package objects;

import java.util.ArrayList;
import java.util.List;




/**
 * Class that represents an Action. An action has a set of preConditions 
 * that must be satisfied and a set of effects that result from the action
 * 
 * @author Catarina Isabel Carvalho Santana
 */
public class Action {

	private List<Literal> preConditions;
	private List<Literal> effects;
	private String name;
	private List<String> formalArguments;
	private List<String> actualArguments;

	
	/**
	 * Constructs a new Action instance.
	 * @param preConditions - set of preConditions of the Action
	 * @param effects - set of Effects of the Action
	 * @param name - name of the Action
	 * @param formalArguments - the formal arguments of the Action
	 * @param actualArguments - the actual arguments of the Action
	 */
	public Action(List<Literal> preConditions, List<Literal> effects,
			String name, List<String> formalArguments,
			List<String> actualArguments) {
		super();
		this.preConditions = preConditions;
		this.effects = effects;
		this.name = name;
		this.formalArguments = formalArguments;
		this.actualArguments = actualArguments;
	}
	
	/**
	 * 
	 * @return The list of preconditions of the Action
	 */
	public List<Literal> getPreConditions() {
		return preConditions;
	}
	
	/**
	 * Sets the action preconditions
	 * @param preConditions - the new set of preconditions
	 */
	public void setPreConditions(List<Literal> preConditions) {
		this.preConditions = preConditions;
	}
	
	/**
	 * 
	 * @return The action effects
	 */
	public List<Literal> getEffects() {
		return effects;
	}

	/**
	 * Sets new effects for the action
	 * @param effects - the new set of effects
	 */
	public void setEffects(List<Literal> effects) {
		this.effects = effects;
	}

	/**
	 * 
	 * @return The name of the action
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets a new name for the Action
	 * @param name - the new name for the action
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 * @return The list of formal arguments of the Action
	 */
	public List<String> getFormalArguments() {
		return formalArguments;
	}
	
	/**
	 * Sets the formal arguments of the action
	 * @param formalArguments - the new set of formal arguments
	 */
	public void setFormalArguments(List<String> formalArguments) {
		this.formalArguments = formalArguments;
	}
	
	/**
	 * 
	 * @return The actual arguments of the Action
	 */
	public List<String> getActualArguments() {
		return actualArguments;
	}
	
	/**
	 * Sets the actual arguments of the action
	 * @param actualArguments - the new set of actual arguments
	 */
	public void setActualArguments(List<String> actualArguments) {
		this.actualArguments = actualArguments;
	}
	
	/**
	 * Creates a copy of the current Action, so it can be used without disturbing the original action
	 * @return A copy of this Action
	 */
	public Action createCopy(){
		List<Literal> clonePreConditions = cloneLiteralList(preConditions);
		List<Literal> cloneEffects = cloneLiteralList(effects);
		List<String> cloneFormalArguments = cloneStringList(formalArguments);
		List<String> cloneActualArguments = cloneStringList(actualArguments);
		return new Action(clonePreConditions, cloneEffects, name, cloneFormalArguments, cloneActualArguments);
		
	}
	
	/**
	 * Creates a clone of a list of strings. Used by the createCopy() and cloneLiteralList() method, 
	 * so it can copy the arguments of the original action/literal.
	 * 
	 * @param listToClone - the list of strings to be cloned
	 * @return A clone of listToClone
	 */
	private List<String> cloneStringList(List<String> listToClone){
		List<String> clone = new ArrayList<String>();
		for(String s : listToClone){
			String copy = new String(s);
			clone.add(copy);
		}
		return clone;
	}
	
	/**
	 * 
	 * @param listToClone
	 * @return
	 */
	private List<Literal> cloneLiteralList(List<Literal> listToClone){
		List<Literal> clone = new ArrayList<Literal>();
		for(Literal l : listToClone){
			List<String> actualArgumentsCopy = cloneStringList(l.getActualArguments());
			List<String> formalArgumentsCopy = cloneStringList(l.getFormalArguments());
			Literal clonedLiteral = new Literal(l.getName(), formalArgumentsCopy, actualArgumentsCopy, l.getValue());
			clone.add(clonedLiteral);
		}
		return clone;
	}
	
	public boolean effectsContainLiteralNegation(Literal l, List<String> variablesUsed){
		boolean matchingArgs = true;
		for(Literal effect : effects){
			if(effect.getName().equals(l.getName()) && effect.getValue() != l.getValue()){
				matchingArgs = effect.argumentsMatch(l, variablesUsed);
			}
		}
		return matchingArgs;
	}

	
	


	@Override
	public String toString() {
		if(actualArguments.isEmpty()){
			return name + formalArguments;
		}else{
			return name + actualArguments;
		}
		 
	}
	/*********/
	public Literal matchingEffect(Literal preCondition, List<String> variablesUsed) {
		for(Literal effect : effects){
			if(effect.getName().equals(preCondition.getName()) && effect.getValue() == preCondition.getValue()){
				return effect;
			}
		}
		return null;
	}
	/************/
}
