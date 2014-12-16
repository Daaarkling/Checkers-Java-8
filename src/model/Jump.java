package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jan Vaňura
 */
public class Jump {
	
	private List<Patch> patches = new ArrayList<>();
	


	public List<Patch> getPatches() {
		return patches;
	}

	
	public List<Stone> getEatenStones(){
		
		List<Stone> eaten = new ArrayList<>();
        patches.stream().filter((patch) -> (patch.hasStone() && !patch.hasStone(getStartPatch().getStone()))).forEach((patch) -> {
			eaten.add(patch.getStone());
        });
		return eaten;
	}
	
	
	public boolean isStartPosition(int x, int y) {
		
		Patch patch = patches.get(0);
		return patch.getX() == x && patch.getY() == y;
	}
	
	public Patch getStartPatch() {
		
		return patches.get(0);
	}
	
	
	
	public boolean isFinalPosition(Point pos){
		
		return isFinalPosition(pos.x, pos.y);
	}
	
	public boolean isFinalPosition(int posX, int posY){
		
		return getFinalPatches().stream().anyMatch((patch) -> (patch.getX() == posX && patch.getY() == posY));
	}
	

	public List<Patch> getFinalPatches() {
		
        List<Patch> finalPatches = new ArrayList<>();
        
        for(int i = patches.size()-1; i >= 0; i--){
            Patch patch = patches.get(i);
            if(patch.hasStone()){
                break;
            }
            finalPatches.add(patch);
        }
        
		return finalPatches;
	}
    
    
    public void jump(){
        
        getPatches().stream().forEach((patch) -> {
			patch.setHighlight(false);
            patch.remove();
        });
    }
    

    public void setHighlight(boolean hightlight) {
        
        patches.stream().forEach((patch) -> {
            patch.setHighlight(hightlight);
        });
    }
	
	public void setHighlightHover(boolean hightlight){
	
		patches.stream().forEach((patch) -> {
            patch.setHighlightHover(hightlight);
        });
	}
	

	@Override
	public String toString() {
		return "Jump";
	}

	
	
}
