package objects;

import java.util.ArrayList;
import java.util.List;

public class Literal {
	
	private String name;
	private List<String> formalArguments;
	private List<String> actualArguments;
	private boolean value;
//	private boolean isSatisfied;

	public Literal(String name, List<String> formalArguments,
			List<String> actualArguments, boolean value) {
		super();
		this.name = name;
		this.formalArguments = formalArguments;
		this.actualArguments = actualArguments;
		this.value = value;
	}
	
	public Literal createCopy() {
		List<String> cloneActualArguments = new ArrayList<String>(actualArguments);
		return new Literal(name, formalArguments, cloneActualArguments, value);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getFormalArguments() {
		return formalArguments;
	}

	public void setFormalArguments(List<String> formalArguments) {
		this.formalArguments = formalArguments;
	}

	public List<String> getActualArguments() {
		return actualArguments;
	}

	public void setActualArguments(List<String> actualArguments) {
		this.actualArguments = actualArguments;
	}

	public boolean getValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}
	
	public boolean argumentsMatch(Literal otherLiteral, List<String> variablesUsed){
		boolean matchingArgs = true;
		for(String arg : otherLiteral.getActualArguments()){
			int index = otherLiteral.getActualArguments().indexOf(arg);
			String myArg = actualArguments.get(index); 
			if(!myArg.equals(arg) && !variablesUsed.contains(myArg) && !variablesUsed.contains(arg)){
				matchingArgs = false;
			}
		}
		return matchingArgs;
	}
	
	

//	public boolean isSatisfied() {
//		return isSatisfied;
//	}
//
//	public void setSatisfied(boolean isSatisfied) {
//		this.isSatisfied = isSatisfied;
//	}
	
	@Override
	public String toString() {
		String litValue = "";
		if(value == false ){
			litValue = "not ";
		}
		if(actualArguments.isEmpty()){
			return litValue + name + formalArguments;
		}else{
			return litValue + name + actualArguments;
		}
		
	}

	public boolean containsVariables(List<String> variablesUsed) {
		for(String arg : actualArguments){
			if(variablesUsed.contains(arg)){
				return true;
			}
		}
		return false;
		
	}

	public void replaceVariables(VariableBinding newVar) {
		List<String> newArgs = cloneStringList(actualArguments);
		for(String arg : actualArguments){
			int index = actualArguments.indexOf(arg);
			if(arg.equals(newVar.getVariableName())){
				newArgs.set(index, newVar.getVariableValue());
			}
		}
		setActualArguments(newArgs);
	}

		private List<String> cloneStringList(List<String> listToClone){
		List<String> clone = new ArrayList<String>();
		for(String s : listToClone){
			String copy = new String(s);
			clone.add(copy);
		}
		return clone;
	}
	
	
	
}
