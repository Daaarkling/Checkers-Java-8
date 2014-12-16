/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import model.Jump;

/**
 *
 * @author Jan Vaňura
 */
class EvalJump {
	
	private Jump jump;
	private List<EvalPosition> evalPositions = new ArrayList<>();

	
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

	
	public Jump getJump() {
		return jump;
	}

	public void setJump(Jump jump) {
		this.jump = jump;
	}	

	public List<EvalPosition> getEvalPositions() {
		return evalPositions;
	}

	public void setEvalPositions(List<EvalPosition> evalPositions) {
		this.evalPositions = evalPositions;
	}
	
	
}
