package localBlastWrapper;
import java.util.ArrayList;
import java.util.List;;

public class PsiBlastResult {
	private List<List<BlastResult>> iterations = new ArrayList<List<BlastResult>>();
	
	
	////////////////////
	// public Interface
	
	public void addIteration(List<BlastResult> iteration) {
		getIterations().add(iteration);
	}
	public int getNum_iterations() {
		return this.getIterations().size();
	}
	
	public List<BlastResult> getIteration(int iteration) {
		List<BlastResult> result = new ArrayList<BlastResult>();
		
		for (BlastResult blastResult : this.getIterations().get(iteration)) {
			result.add((BlastResult)blastResult.clone());
		}
		
		return result;
	}
	
	///////////////////////
	// getters and setters
	
	private List<List<BlastResult>> getIterations() {
		return this.iterations;
	}
}
