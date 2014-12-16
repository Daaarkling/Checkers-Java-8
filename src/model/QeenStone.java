package model;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;

/**
 *
 * @author Jan Vaňura
 */
public class QeenStone extends Stone {

	private List<Patch> unconfirmed = new ArrayList<>();
	
	
	public QeenStone(Patch[][] patches, int x, int y, Color player) {
		super(patches, x, y, player);
		getStyleClass().add("qeen-stone");
	}

	@Override
	protected void getPossiblePositions() {
		
		int posX1 = x - 1;
		int posX2 = x + 1;
		int posY1 = y - 1;
		int posY2 = y + 1;
		getPossiblePositions1(posX2, posY1);
		getPossiblePositions2(posX1, posY1);
		getPossiblePositions3(posX1, posY2);
		getPossiblePositions4(posX2, posY2);
	}
	
	
	private void getPossiblePositions1(int posX, int posY){
	
		if (posX <= patches.length-1 && posY >= 0 && !patches[posX][posY].hasStone()){
			possibleMoves.add(patches[posX][posY]);
			getPossiblePositions1(++posX, --posY);
		}
	}
	private void getPossiblePositions2(int posX, int posY){
	
		if (posX >= 0 && posY >= 0 && !patches[posX][posY].hasStone()){
			possibleMoves.add(patches[posX][posY]);
			getPossiblePositions2(--posX, --posY);
		}
	}
	private void getPossiblePositions3(int posX, int posY){
		
		if (posX >= 0 && posY <= patches.length-1 && !patches[posX][posY].hasStone()){
			possibleMoves.add(patches[posX][posY]);
			getPossiblePositions3(--posX, ++posY);
		}
	}
	private void getPossiblePositions4(int posX, int posY){
	
		if (posX <= patches.length-1 && posY <= patches.length-1 && !patches[posX][posY].hasStone()){
			possibleMoves.add(patches[posX][posY]);
			getPossiblePositions4(++posX, ++posY);
		}
	}
	
	
	
	@Override
	public void jump() {
				
		jump1(x, y, false);
		unconfirmed.clear();
		jump2(x, y, false);
		unconfirmed.clear();
		jump3(x, y, false);
		unconfirmed.clear();
		jump4(x, y, false);
		unconfirmed.clear();

		removeRedundantJumps();
		extendJumps();
	}
	
	
	private void jump1(int posX, int posY, boolean spread) {
		
		unconfirmed.add(patches[posX][posY]);
		if(posX+2 <= patches.length-1 && posY-2 >= 0 && isStoneOn(posX, posY)){ 
			if(patches[posX+1][posY-1].hasStone(opposite) && isStoneOn(posX+2, posY-2) && !wasEaten(patches[posX+1][posY-1].getStone())){
				Jump j = new Jump();
				j.getPatches().addAll(unconfirmed);
				j.getPatches().add(patches[posX+1][posY-1]);
				j.getPatches().add(patches[posX+2][posY-2]);
				jumps.add(j);
				unconfirmed.add(patches[posX+1][posY-1]);
				jump1(posX+2, posY-2, true);
				unconfirmed.remove(unconfirmed.size()-1);
				jump2(posX+2, posY-2, false);
				unconfirmed.remove(unconfirmed.size()-1);
				jump4(posX+2, posY-2, false);
				unconfirmed.remove(unconfirmed.size()-1);
			} else {
				jump1(posX+1, posY-1, spread);
				unconfirmed.remove(unconfirmed.size()-1);
				if(spread){
					jump2(posX+1, posY-1, false);
					unconfirmed.remove(unconfirmed.size()-1);
					jump4(posX+1, posY-1, false);
					unconfirmed.remove(unconfirmed.size()-1);
				}
			}
		}
	}
	private void jump2(int posX, int posY, boolean spread) {
		
		unconfirmed.add(patches[posX][posY]);
		if(posX-2 >= 0 && posY-2 >= 0 && isStoneOn(posX, posY)){
			if(patches[posX-1][posY-1].hasStone(opposite) && isStoneOn(posX-2, posY-2) && !wasEaten(patches[posX-1][posY-1].getStone())){
				Jump j = new Jump();
				j.getPatches().addAll(unconfirmed);
				j.getPatches().add(patches[posX-1][posY-1]);
				j.getPatches().add(patches[posX-2][posY-2]);
				jumps.add(j);
				unconfirmed.add(patches[posX-1][posY-1]);
				jump2(posX-2, posY-2, true);
				unconfirmed.remove(unconfirmed.size()-1);
				jump3(posX-2, posY-2, false);
				unconfirmed.remove(unconfirmed.size()-1);
				jump1(posX-2, posY-2, false);
				unconfirmed.remove(unconfirmed.size()-1);
			} else {
				jump2(posX-1, posY-1, spread);
				unconfirmed.remove(unconfirmed.size()-1);
				if(spread){
					jump1(posX-1, posY-1, false);
					unconfirmed.remove(unconfirmed.size()-1);
					jump3(posX-1, posY-1, false);
					unconfirmed.remove(unconfirmed.size()-1);
				}
			}
		}
	}
	private void jump3(int posX, int posY, boolean spread) {
		
		unconfirmed.add(patches[posX][posY]);
		if(posX-2 >= 0 && posY+2 <= patches.length-1 && isStoneOn(posX, posY)){
			if(patches[posX-1][posY+1].hasStone(opposite) && isStoneOn(posX-2, posY+2) && !wasEaten(patches[posX-1][posY+1].getStone())){
				Jump j = new Jump();
				j.getPatches().addAll(unconfirmed);
				j.getPatches().add(patches[posX-1][posY+1]);
				j.getPatches().add(patches[posX-2][posY+2]);
				jumps.add(j);
				unconfirmed.add(patches[posX-1][posY+1]);
				jump3(posX-2, posY+2, true);
				unconfirmed.remove(unconfirmed.size()-1);
				jump4(posX-2, posY+2, false);
				unconfirmed.remove(unconfirmed.size()-1);
				jump2(posX-2, posY+2, false);
				unconfirmed.remove(unconfirmed.size()-1);
			} else {
				jump3(posX-1, posY+1, spread);
				unconfirmed.remove(unconfirmed.size()-1);
				if(spread){
					jump2(posX-1, posY+1, false);
					unconfirmed.remove(unconfirmed.size()-1);
					jump4(posX-1, posY+1, false);
					unconfirmed.remove(unconfirmed.size()-1);
				}
			}
		}
	}
	private void jump4(int posX, int posY, boolean spread) {
		
		unconfirmed.add(patches[posX][posY]);
		if(posX+2 <= patches.length-1 && posY+2 <= patches.length-1 && isStoneOn(posX, posY)){
			if(patches[posX+1][posY+1].hasStone(opposite) && isStoneOn(posX+2, posY+2) && !wasEaten(patches[posX+1][posY+1].getStone())){
				Jump j = new Jump();
				j.getPatches().addAll(unconfirmed);
				j.getPatches().add(patches[posX+1][posY+1]);
				j.getPatches().add(patches[posX+2][posY+2]);
				jumps.add(j);
				unconfirmed.add(patches[posX+1][posY+1]);
				jump4(posX+2, posY+2, true);
				unconfirmed.remove(unconfirmed.size()-1);
				jump1(posX+2, posY+2, false);
				unconfirmed.remove(unconfirmed.size()-1);
				jump3(posX+2, posY+2, false);
				unconfirmed.remove(unconfirmed.size()-1);
			} else {
				jump4(posX+1, posY+1, spread);
				unconfirmed.remove(unconfirmed.size()-1);
				if(spread){
					jump1(posX+1, posY+1, false);
					unconfirmed.remove(unconfirmed.size()-1);
					jump3(posX+1, posY+1, false);
					unconfirmed.remove(unconfirmed.size()-1);
				}
			}
		}
	}

	private void extendJumps() {

		jumps.stream().map((jump) -> jump.getPatches()).forEach((currentPatches) -> {		
			Patch last = currentPatches.get(currentPatches.size()-1);
			Patch prevLast = currentPatches.get(currentPatches.size()-2);
			int deltaX = last.getX()-prevLast.getX();
			int deltaY = last.getY()-prevLast.getY();
			int extendX = last.getX()+deltaX;
			int extendY = last.getY()+deltaY;
			while(extendX >= 0 && extendX <= patches.length-1 && extendY >= 0 && extendY <= patches.length-1 && isStoneOn(extendX, extendY)){
				currentPatches.add(patches[extendX][extendY]);
				extendX += deltaX;
				extendY += deltaY;
			}
		});
	}
	
	private boolean isStoneOn(int posX, int posY){
	
		if(!patches[posX][posY].hasStone()){
			return true;
		} else {
			return patches[posX][posY].hasStone(this);
		}
	}
	
	
	private boolean wasEaten(Stone stone){
	
		removeRedundantJumps();
		if(!jumps.isEmpty()){
			Jump lastJump = jumps.get(jumps.size()-1);
			return lastJump.getEatenStones().contains(stone);
		}		
		return false;
	}

	
	@Override
	public boolean isOnQeenPosition() {
		return false;
	}

}
