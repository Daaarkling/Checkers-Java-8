package ai;

import model.Patch;

/**
 *
 * @author Jan Vaňura
 */
public class EvalPosition {

	
	private Patch patch;
	private int evaluation;

	
	
	public EvalPosition() {
	}
	
	public EvalPosition(int evaluation, Patch patch) {
		this.evaluation = evaluation;
		this.patch = patch;
	}

	public Patch getPatch() {
		return patch;
	}

	public void setPatch(Patch patch) {
		this.patch = patch;
	}

	public int getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(int evaluation) {
		this.evaluation = evaluation;
	}

	void addEvaluation(int evaluation) {
		this.evaluation += evaluation;
	}
	
	
}
