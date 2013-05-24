package objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import planObjects.VariableBinding;

public class Literal {

	private String name;
	private List<String> formalArguments;
	private List<String> actualArguments;
	private boolean value;

	// private boolean isSatisfied;

	public Literal(String name, List<String> formalArguments,
			List<String> actualArguments, boolean value) {
		super();
		this.name = name;
		this.formalArguments = formalArguments;
		this.actualArguments = actualArguments;
		this.value = value;
	}

	public Literal createCopy() {
		List<String> cloneActualArguments = new ArrayList<String>(
				actualArguments);
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

	public boolean argumentsMatch(Literal otherLiteral,
			List<String> variablesUsed, Map<String, String> map) {
		boolean matchingArgs = true;

		List<String> thisArgs = new ArrayList<String>();
		List<String> otherLiteralArgs = new ArrayList<String>();
		// verificar em this
		for (String thisArg : actualArguments) {
			if (variablesUsed.contains(thisArg) && map.containsKey(thisArg)) {
				thisArgs.add(map.get(thisArg));
			} else {
				thisArgs.add(thisArg);
			}
		}
		// verificar em otherLiteral
		for (String otherArg : otherLiteral.getActualArguments()) {
			if (variablesUsed.contains(otherArg) && map.containsKey(otherArg)) {
				otherLiteralArgs.add(map.get(otherArg));
			} else {
				otherLiteralArgs.add(otherArg);
			}
		}

		boolean thisHasFullArgs = true;
		for (String thisArg : thisArgs) {
			if (variablesUsed.contains(thisArg)) {
				thisHasFullArgs = false;
			}
		}

		boolean otherHasFullArgs = true;
		for (String otherArg : otherLiteralArgs) {
			if (variablesUsed.contains(otherArg)) {
				otherHasFullArgs = false;
			}
		}

		if (thisHasFullArgs && otherHasFullArgs) { //tudo instanciado
			for(int i = 0 ; i< thisArgs.size() ; i++){
				if(!thisArgs.get(i).equals(otherLiteralArgs.get(i))){
					matchingArgs = false;
				}
			}

		} else if (!thisHasFullArgs && !otherHasFullArgs) { //os dois literais têm variaveis nao instanciadas
			for(int i = 0 ; i< thisArgs.size() ; i++){
				String thisArg = thisArgs.get(i);
				String otherArg = otherLiteralArgs.get(i);
				if(!thisArg.equals(otherArg) && !variablesUsed.contains(thisArg) && !variablesUsed.contains(otherArg)){
					matchingArgs = false;
				}
						
			}

		} else { //um literal está completamente instanciado e outro não
			for(int i = 0 ; i< thisArgs.size() ; i++){
				String thisArg = thisArgs.get(i);
				String otherArg = otherLiteralArgs.get(i);
				if(!thisArg.equals(otherArg) && !variablesUsed.contains(thisArg) && !variablesUsed.contains(otherArg)){
					matchingArgs = false;
				}
						
			}

		}

		// for(String arg : otherLiteral.getActualArguments()){
		// int index = otherLiteral.getActualArguments().indexOf(arg);
		//
		// String myArg = actualArguments.get(index);
		// for(VariableBinding b : variableBindings){
		// if(b.getVariableName().equals(myArg)){
		// myArg = b.getVariableValue();
		// }
		// }
		// String literalArg = arg;
		// // for(VariableBinding b : variableBindings){
		// // if(b.getVariableName().equals(arg)){
		// // literalArg = b.getVariableValue();
		// // }
		// // }
		// if(!myArg.equals(literalArg) && !variablesUsed.contains(myArg) &&
		// !variablesUsed.contains(literalArg)){
		// matchingArgs = false;
		// }
		// }
		return matchingArgs;
	}
	
	public void instantiate(Action actionCreated) {
			for (String formalArg : formalArguments) {
				int i = actionCreated.getFormalArguments().indexOf(formalArg);
				String arg = actionCreated.getActualArguments().get(i);
				actualArguments.add(arg);
			}
		
		
	}
	
	@Override
	public String toString() {
		String litValue = "";
		if (value == false) {
			litValue = "not ";
		}
		if (actualArguments.isEmpty()) {
			return litValue + name + formalArguments;
		} else {
			return litValue + name + actualArguments;
		}

	}

	public boolean containsVariables(List<String> variablesUsed) {
		for (String arg : actualArguments) {
			if (variablesUsed.contains(arg)) {
				return true;
			}
		}
		return false;

	}

	public void replaceVariables(VariableBinding newVar) {
		List<String> newArgs = cloneStringList(actualArguments);
		for (String arg : actualArguments) {
			int index = actualArguments.indexOf(arg);
			if (arg.equals(newVar.getVariableName())) {
				newArgs.set(index, newVar.getVariableValue());
			}
		}
		setActualArguments(newArgs);
	}

	private List<String> cloneStringList(List<String> listToClone) {
		List<String> clone = new ArrayList<String>();
		for (String s : listToClone) {
			String copy = new String(s);
			clone.add(copy);
		}
		return clone;
	}

}
