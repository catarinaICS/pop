package algorithm;

import java.util.ArrayList;
import java.util.List;

import objects.Action;
import objects.AgendaElement;
import objects.CausalLink;
import objects.Literal;
import objects.Ordering;
import objects.Plan;

public class PoP {
//-------------experiencia com variaveis-------------//
	private List<String> variablesUsed = new ArrayList<String>();
	private int variableCounter = 1;
	
	private Domain domain;

	public PoP(Domain domain) {
		super();
		this.domain = domain;
	}
	/*
	 * O QUE FALTA;
	 * 	- VERIFICAR CONSISTENCIA DO PLANO
	 * 	- RESOLVER THREATS
	 * 	- ...
	 */
	public Plan pop(Plan p, List<AgendaElement> agenda) {
		while(!agenda.isEmpty()){
			AgendaElement element = agenda.remove(0);
			Literal preCondition = element.getPreCondition();
			Action aj = element.getAction();
			List<Action> relevant = providers(preCondition, p);
			if (relevant.isEmpty()) {
				System.err.println("No Actions available");
				System.exit(0);
			} else {
				// Add causal link
				Action ai = relevant.get(0);
				CausalLink link = new CausalLink(ai, preCondition, aj);
				p.getCausalLinks().add(link);

				// update plan if necessary
				if (!p.getActions().contains(ai)) {
					p.getActions().add(ai); // add to plan actions
					p.getOrderingConstraints().add(new Ordering(ai, aj)); // update
																			// ordering
																			// constraints
					for (Literal l : ai.getPreConditions()) { // update agenda
						agenda.add(new AgendaElement(l, ai));
					}
				}

				// threats - para posterior tratamento...
				
				// for(Action planAction : p.getActions()){
				// for(Literal postCondition : planAction.getPostConditions()){
				// if(postCondition.equals(preCondition) &&
				// postCondition.getValue() != preCondition.getValue()){
				// //metodo equals
				// p.getOrderingConstraints().add(new Ordering(planAction, ai));
				// }
				// }
				// }
				//return pop(p, agenda);
			}
		}
		
		return p;

	}

	private List<Action> providers(Literal preCondition, Plan p) {
		List<Action> providers = new ArrayList<Action>();
		relevantActions(preCondition, p.getActions(), providers);
		relevantActions(preCondition, domain.getDomainActions(), providers);
		return providers;

	}

	private void relevantActions(Literal preCondition, List<Action> allActions, List<Action> providers) {
		for (Action possibleAction : allActions) {
			for (Literal literalToCompare : possibleAction.getPostConditions()) {
				if (literalToCompare.getName().equals(preCondition.getName()) && literalToCompare.getValue() == preCondition.getValue()) {
					
					// se os argumentos forem todos null -> o literal serve, mas
					// nao está instanciado. É necessário instanciar a acção, pré e pos condições
					
					if (literalToCompare.getActualArguments().isEmpty()) {
						System.out.println("I fit but I'm null!"); //apagar isto eventualmente.
						
						//criar uma cópia da acção que encaixa e do literal para instanciar.
						Action actionCopy = possibleAction.createCopy();
						Literal literalCopy = literalToCompare.createCopy();
						
						//instanciar o literal que correspondeu à pré condição que estamos a procurar com os argumentos de preCOndition
						literalCopy.setActualArguments(preCondition.getActualArguments());
						//fazer corresponder os argumentos da acçao
						//ou seja stack(a,b) -> on(a,b), por exemplo. se tenho literalCopy =  on(a,b) 
						//e uma acção nao instanciada (sem argumentos), temos que instanciar.
						
						instantateAction(actionCopy, literalCopy);
						//a acção está agora instanciada. Argumentos sem correspondência = variable bindings.
						
						//instanciar as pré e pos-condições da acção 
						instantiatePreConditions(actionCopy);
						instantiatePostConditions(actionCopy);
						//adicionar
						providers.add(actionCopy);
						
					} else {//temos um literal instanciado pelo que é necessário verificar se é igual à nossa pre condição.
						boolean equals = true;
//						String variableName =null;
//						String variableValue = null;
						//criamos aqui uma String variableName = null, variableValue = null; -> ver se isto é mesmo necessário!
						for (int i = 0; i < preCondition.getActualArguments().size(); i++) {
							String preConditionArg = preCondition.getActualArguments().get(i);
							String literalArg = literalToCompare.getActualArguments().get(i);
							/*
							se a variável de preCondicao for uma variableBinding, passar à frente, mas 
							variableName = preCondicao.variavel, e 
							variableValue = literal.variavel.
							*/
							if(variablesUsed.contains(preCondition.getActualArguments().get(i))){
								//se o arg for uma variavel usada, entao obviamente é diferente do valor do argumento do outro literal.
								//no entanto se os restantes argumentos forem iguais, é só fazer o binding.
								//O que falta aqui:
								//variableName = preConditionArg;
								//variableValue = literalArg;
								System.out.println("I'm a variable binding!");//para apagar eventualmente.
								
							}else{
								if (!preConditionArg.equals(literalArg)) {
									equals = false;
									
								}
							}
						}
						
						if (equals == true) {
							providers.add(possibleAction);
						}
						
					}

				}
			}
		}
	}

	private void instantateAction(Action actionCreated, Literal baseLiteral) {
		for(String formalArg : actionCreated.getFormalArguments()){
			int n = baseLiteral.getFormalArguments().indexOf(formalArg);
			if(n>=0){ //existe argumento igual
				try{
					actionCreated.getActualArguments().add(baseLiteral.getActualArguments().get(n));
				}catch(IndexOutOfBoundsException e){
					System.out.println("Error");
				}
				
			}else{ //nao existe. Variable Binding.
				String variable = getVariableName();
				actionCreated.getActualArguments().add(variable);
			}
		}
	}

	private void instantiatePostConditions(Action actionCreated) {
		for(Literal l : actionCreated.getPostConditions()){
			for(String formalArg : l.getFormalArguments()){
				int i = actionCreated.getFormalArguments().indexOf(formalArg);
				//estamos a usar uma acção completamente instanciada, em princípio não é necessário verificar i. Ficar atenta.
				l.getActualArguments().add(actionCreated.getActualArguments().get(i));
			}
		}
	}

	private void instantiatePreConditions(Action actionCreated) {
		for(Literal l : actionCreated.getPreConditions()){
			for(String formalArg : l.getFormalArguments()){
				int i = actionCreated.getFormalArguments().indexOf(formalArg);
				//estamos a usar uma acção completamente instanciada, em princípio não é necessário verificar i. Ficar atenta.
				l.getActualArguments().add(actionCreated.getActualArguments().get(i));
			}
		}
	}

	private String getVariableName(){
		char a = 'a';
		String variable = "" + a + variableCounter;
		while(variablesUsed.contains(variable)){
			variableCounter++;
			variable = "" + a + variableCounter;
		}
		variablesUsed.add(variable);
		variableCounter++;
		return variable;
	}
	
//	public void checkDomain(){
//		for(Action a : domain.getDomainActions()){
//			for(Literal l : a.getPostConditions()){
//				if(!l.getActualArguments().isEmpty()){
//					System.err.println("DOMÍNIO MODIFICADO!!!!!!");
//				}
//			}
//		}
//	}

}
