package algorithm;

import java.util.ArrayList;
import java.util.List;

public class VariableManager {
	private int variableCounter = 1;
	private List<String> variablesUsed = new ArrayList<String>();
	
	public List<String> getVariablesUsed() {
		return variablesUsed;
	}

	public String getVariableName() {
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
