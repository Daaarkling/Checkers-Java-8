package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author Jan Vaňura
 */
public class Stone extends Circle {
	
	public static final Color ENEMY = Color.BLUE;
	public static final Color ALLY = Color.RED;
	protected int x, y;
	protected boolean active = false;
	protected boolean eaten = false;
	protected Color player, opposite;
	protected Patch[][] patches;
	protected List<Patch> possibleMoves = new ArrayList<>();
	protected List<Jump> jumps = new ArrayList<>();
	
	

	public Stone(Patch[][] patches, int x, int y, Color player) {
		super();
		this.patches = patches;
		this.x = x;
		this.y = y;
		this.player = player;	
		
		if(player.equals(Stone.ENEMY)){		
			opposite = Stone.ALLY;
		} else {
			opposite = Stone.ENEMY;
		}
		
		setRadius(20);
		setFill(player);
		getStyleClass().add("stone");
		redrawStone(x, y);
	}
	
	public void jumpOnPosition(int newX, int newY){
	
		redrawStone(newX, newY);
		x = newX;
		y = newY;
		setEaten(false);
		setActive(false);
		jumps.clear();
	}
	
	public boolean moveOnPosition(int newX, int newY){
		
		if(isMovePossible(newX, newY)){
			redrawStone(newX, newY);
			x = newX;
			y = newY;
			setEaten(false);
			setActive(false);
			possibleMoves.clear();
			return true;
		}
		return false;
	}

	public boolean isMovePossible(int newX, int newY){
		
		getPossiblePositions();		
		return possibleMoves.stream().anyMatch((posPos) -> (posPos.getX() == newX && posPos.getY() == newY));
	}
	
	
	public boolean isMovePosible(){
		
		if(!isEaten()){
			getPossiblePositions();
			return !possibleMoves.isEmpty();
		}
		return false;
	}
	
	
	public void jump() {
		
		jumps.clear();
		jump(x, y, null);
		removeRedundantJumps();				
	}
	
	
	protected void removeRedundantJumps() {
		
		List<Jump> redundantJumps = new ArrayList<>();		
		if(isJump()){
			for (Jump jump1 : jumps){
				for (Jump jump2 : jumps){
					if(jump2.equals(jump1)) continue;
					if(jump2.getPatches().containsAll(jump1.getPatches())){
						redundantJumps.add(jump1);
					}
				}
			}
			jumps.removeAll(redundantJumps);
		}
	}

	
	private void jump(int posX, int posY, Jump prevJump){
	
		int i = 0;
		if(player.equals(Stone.ENEMY)){		
			i = 1;
		} else {
			i = -1;
		}
		
		if(posX-1*2 >= 0 && posY+i*2 >= 0 && posY+i*2 <= patches.length-1 && patches[posX-1][posY+i].hasStone(opposite) && !patches[posX-1*2][posY+i*2].hasStone()){		
			
			Jump j = new Jump();		
			if(prevJump!=null) j.getPatches().addAll(prevJump.getPatches());
			j.getPatches().add(patches[posX][posY]);
			j.getPatches().add(patches[posX-1][posY+i]);
			j.getPatches().add(patches[posX-1*2][posY+i*2]);
			
			jumps.add(j);
			jump(posX-1*2, posY+i*2, j);
		}
		if(posX+1*2 <= patches.length-1 && posY+i*2 >= 0 && posY+i*2 <= patches.length-1 && patches[posX+1][posY+i].hasStone(opposite) && !patches[posX+1*2][posY+i*2].hasStone()){			
			Jump j = new Jump();
			if(prevJump!=null) j.getPatches().addAll(prevJump.getPatches());
			j.getPatches().add(patches[posX][posY]);
			j.getPatches().add(patches[posX+1][posY+i]);
			j.getPatches().add(patches[posX+1*2][posY+i*2]);
			jumps.add(j);
			jump(posX+1*2,  posY+i*2, j);
		}
	}
	
	
	public boolean isOnQeenPosition(){
		
		if(player.equals(Stone.ALLY) && y == 0)		
			return true;
		else if(player.equals(Stone.ENEMY) && y == patches.length-1)
			return true;
		else
			return false;
	}
	
	
	protected void getPossiblePositions(){
		
		possibleMoves.clear();
		int posY;
		int posX1;
		int posX2;
		
		posX1 = x - 1;
		posX2 = x + 1;
		
		if(player.equals(Stone.ALLY))		
			posY = y - 1;
		else
			posY = y + 1;
		
		if (posY >= 0 && posY <= patches.length-1 && posX1 >= 0 && !patches[posX1][posY].hasStone()){
			possibleMoves.add(patches[posX1][posY]);
		}
		if (posY >= 0 && posY <= patches.length-1 && posX2 <= patches.length-1 && !patches[posX2][posY].hasStone()){
			possibleMoves.add(patches[posX2][posY]);
		}
	}
	
	
	
	
	
	protected void redrawStone(int x, int y) {
		
		patches[x][y].add(this);
	}
	
	
	public void setActive(boolean state){
		
		active = state;
		if(active){
			setRadius(25);
		} else {
			setRadius(20);
		}
	}

	@Override
	public int hashCode() {
		int hash = 5;
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Stone other = (Stone) obj;
		if (this.x != other.x) {
			return false;
		}
		if (this.y != other.y) {
			return false;
		}
		if (this.active != other.active) {
			return false;
		}
		if (this.eaten != other.eaten) {
			return false;
		}
		if (!Objects.equals(this.player, other.player)) {
			return false;
		}
		return true;
	}

	
	
	
	public Color getPlayer() {
		return player;
	}

	public void setPlayer(Color player) {
		this.player = player;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean isJump() {
		return !jumps.isEmpty();
	}

	public boolean isEaten() {
		return eaten;
	}

	public void setEaten(boolean eaten) {
		this.eaten = eaten;
	}

	public List<Jump> getJumps() {
		return jumps;
	}

	public List<Patch> getPossibleMoves() {
		getPossiblePositions();
		return possibleMoves;
	}
	
	
}
