package pddl;

import java.io.File;
import java.io.IOException;

import uk.ac.bham.cs.zas.pddl.domain.Domain;
import uk.ac.bham.cs.zas.pddl.domain.Problem;
import uk.ac.bham.cs.zas.pddl.parser.ANTLRDomainBuilder;
import uk.ac.bham.cs.zas.pddl.parser.ANTLRProblemBuilder;
import uk.ac.bham.cs.zas.pddl.parser.InvalidPDDLElementException;
import uk.ac.bham.cs.zas.pddl.parser.PDDLSyntaxException;

public class PDDLParser {

	public PDDLParser() {
	}

	public Domain parsePDDLDomainFile(File pddlFile) throws IOException, PDDLSyntaxException, InvalidPDDLElementException {
		ANTLRDomainBuilder domain_builder = new ANTLRDomainBuilder(pddlFile);
		return domain_builder.buildDomain();
	}

	public Problem parsePDDLProblemFile(Domain domain, File problemFile) throws IOException, PDDLSyntaxException, InvalidPDDLElementException {
		ANTLRProblemBuilder problem_builder = new ANTLRProblemBuilder(domain, problemFile);
		return problem_builder.buildProblem();
	}
}
