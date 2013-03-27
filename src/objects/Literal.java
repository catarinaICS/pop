package objects;

import java.util.List;

public class Literal {
	
	private String name;
	private List<String> formalArguments;
	private List<String> actualArguments;
//	private Map<String, String> args;
	private boolean value;
	private boolean isSatisfied;
	
	
	
//	public Literal(String name, Map<String, String> args, boolean value) {
//		super();
//		this.name = name;
//		this.args = args;
//		this.value = value;
//	}

	public Literal(String name, List<String> formalArguments,
			List<String> actualArguments, boolean value) {
		super();
		this.name = name;
		this.formalArguments = formalArguments;
		this.actualArguments = actualArguments;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	
//	public Map<String, String> getArgs() {
//		return args;
//	}
//
//	public void setArgs(Map<String, String> args) {
//		this.args = args;
//	}

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

	public boolean isSatisfied() {
		return isSatisfied;
	}

	public void setSatisfied(boolean isSatisfied) {
		this.isSatisfied = isSatisfied;
	}
	
	
	
	@Override
	public String toString() {
		String litValue = "";
		if(value == false ){
			litValue = "not ";
		}
		return litValue + name + formalArguments;
	}
	
	
	
}
