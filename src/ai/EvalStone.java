package ai;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import model.Stone;

/**
 *
 * @author Jan Vaňura
 */
public class EvalStone {
	
	private Stone stone;
	private List<EvalPosition> evalPositions = new ArrayList<>();
	private List<EvalJump> evalJumps = new ArrayList<>();
	

	public List<EvalPosition> getMaxEvalPos() {
		
		List<EvalPosition> eps = new ArrayList<>();       

        EvalPosition maxEvalPos = evalPositions.stream()
                .max(Comparator.comparing(item -> item.getEvaluation()))
                .get();
        
        
        evalPositions.stream().filter((eP) -> (eP.getEvaluation() == maxEvalPos.getEvaluation())).forEach((eP) -> {
            eps.add(eP);
        });
        
		return eps;
	}

	
	

	public int getMaxEvalPosValue() {
		
		List<EvalPosition> eps = getMaxEvalPos();		
		return eps.get(0).getEvaluation();
	}
	
	
	
	public List<EvalJump> getMaxEvalJump() {
			
        List<EvalJump> ejs = new ArrayList<>();       

        EvalJump maxEvalJump = evalJumps.stream()
                .max(Comparator.comparing(item -> item.getMaxEvalPosValue()))
                .get();
        
        
        evalJumps.stream().filter((eJ) -> (eJ.getMaxEvalPosValue() == maxEvalJump.getMaxEvalPosValue())).forEach((eP) -> {
            ejs.add(eP);
        });
        
		return ejs;
	}

	public int getMaxEvalJumpValue() {
		
		List<EvalJump> maxEvalJump = getMaxEvalJump();
		return maxEvalJump.get(0).getMaxEvalPosValue();
	}
	
	
	

	public Stone getStone() {
		return stone;
	}

	public void setStone(Stone stone) {
		this.stone = stone;
	}

	public List<EvalPosition> getEvalPositions() {
		return evalPositions;
	}

	public void setEvalPositions(List<EvalPosition> evalPositions) {
		this.evalPositions = evalPositions;
	}

	public List<EvalJump> getEvalJumps() {
		return evalJumps;
	}

	public void setEvalJumps(List<EvalJump> evalJumps) {
		this.evalJumps = evalJumps;
	}
	
	
}
