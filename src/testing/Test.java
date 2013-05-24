package testing;

import java.io.IOException;

import objects.Plan;
import uk.ac.bham.cs.zas.pddl.parser.InvalidPDDLElementException;
import uk.ac.bham.cs.zas.pddl.parser.PDDLSyntaxException;
import algorithm.PoP;

public class Test {
	public static void main(String[] args) throws IOException,
			PDDLSyntaxException, InvalidPDDLElementException {
		PoP p = new PoP();
		Plan finalPlan = p.pop();
		System.out.println(finalPlan.getActions());
		System.out.println(finalPlan.getCausalLinks());
		System.out.println(finalPlan.getOrderingConstraints());
		System.out.println(finalPlan.getVariableBindings());
	}


}
