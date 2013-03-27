package objects;

import java.util.List;

public class Action {

	

	private List<Literal> preConditions;
	private List<Literal> postConditions;
	private String name;
	private List<String> formalArguments;
	private List<String> actualArguments;
//	private Map<String, String> args;
	
	public Action(List<Literal> preConditions, List<Literal> postConditions,
			String name, List<String> formalArguments,
			List<String> actualArguments) {
		super();
		this.preConditions = preConditions;
		this.postConditions = postConditions;
		this.name = name;
		this.formalArguments = formalArguments;
		this.actualArguments = actualArguments;
	}
//
//	public Action(List<Literal> preConditions, List<Literal> postConditions,
//			String name, Map<String, String> args) {
//		super();
//		this.preConditions = preConditions;
//		this.postConditions = postConditions;
//		this.name = name;
//		this.args = args;
//	}


	public List<Literal> getPreConditions() {
		return preConditions;
	}

	public void setPreConditions(List<Literal> preConditions) {
		this.preConditions = preConditions;
	}

	public List<Literal> getPostConditions() {
		return postConditions;
	}

	public void setPostConditions(List<Literal> postConditions) {
		this.postConditions = postConditions;
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


	@Override
	public String toString() {
		return preConditions + " -> " + name +"("+ formalArguments + ") -> " + postConditions; 
	}

}
