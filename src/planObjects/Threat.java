package planObjects;

public class Threat {
	
	private Ordering threatAfterAi;
	private Ordering threatBeforeAj;
	private CausalLink threatened;
	
	
	public Threat(Ordering threatAfterAi, Ordering threatBeforeAj,
			CausalLink threatened) {
		super();
		this.threatAfterAi = threatAfterAi;
		this.threatBeforeAj = threatBeforeAj;
		this.threatened = threatened;
	}


	public Ordering getThreatAfterAi() {
		return threatAfterAi;
	}


	public Ordering getThreatBeforeAj() {
		return threatBeforeAj;
	}


	public CausalLink getThreatened() {
		return threatened;
	}
	
	@Override
	public String toString() {
		return "Action " + threatAfterAi.getSecond() + " -> (" + threatAfterAi.getSecond().getEffects() + ") threatens link " + threatened;
	}
	
	

}
