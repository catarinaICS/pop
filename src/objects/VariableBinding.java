package objects;

public class VariableBinding {

	private String variableName;
	private String variableValue;

	public VariableBinding(String variableName, String variableValue) {
		super();
		this.variableName = variableName;
		this.variableValue = variableValue;
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public String getVariableValue() {
		return variableValue;
	}

	public void setVariableValue(String variableValue) {
		this.variableValue = variableValue;
	}

	@Override
	public String toString() {
		return "(" + variableName + " = " + variableValue + ")";
	}

}
