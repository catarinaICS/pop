package objects;

import java.util.Map;

public class Literal {
	
	private String name;
	private Map<String, String> args;
	private boolean value;
	private boolean isSatisfied;
	
	public Literal(String name, Map<String, String> args, boolean value) {
		super();
		this.name = name;
		this.args = args;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, String> getArgs() {
		return args;
	}

	public void setArgs(Map<String, String> args) {
		this.args = args;
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
		return litValue + name + args.keySet();
	}
	
	
	
}
