package objects;

import java.util.ArrayList;
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

	public Action createCopy(){
		List<Literal> clonePreConditions = cloneLiteralList(preConditions);
		List<Literal> clonePostConditions = cloneLiteralList(postConditions);
		List<String> cloneFormalArguments = cloneStringList(formalArguments);
		List<String> cloneActualArguments = cloneStringList(actualArguments);
		return new Action(clonePreConditions, clonePostConditions, name, cloneFormalArguments, cloneActualArguments);
		
	}
	
	private List<String> cloneStringList(List<String> listToClone){
		List<String> clone = new ArrayList<String>();
		for(String s : listToClone){
			String copy = new String(s);
			clone.add(copy);
		}
		return clone;
	}
	
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
		if(actualArguments.isEmpty()){
			return name + formalArguments;
		}else{
			return name + actualArguments;
		}
		 
	}

}
