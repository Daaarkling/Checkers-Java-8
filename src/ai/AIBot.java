/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai;

import java.util.List;
import javafx.scene.paint.Color;
import model.Patch;
import model.Stone;

/**
 *
 * @author Jan Vaňura
 */
public abstract class AIBot {
	
	protected List<Stone> stones;
	protected Patch[][] patches;
	protected Color player;
	protected int lastNumOfEatenStones;
	
	
	public AIBot(List<Stone> stones, Patch[][] patches, Color player){
		
		this.stones = stones;
		this.patches = patches;
		this.player = player;
	}
	
	
	public abstract Stone move();
	public abstract Stone jump(List<Stone> possibleJumpers);
	public abstract void generateValues();
        public abstract void mutate();

	
	
	public int getLastNumOfEatenStones() {
		return lastNumOfEatenStones;
	}

	public void setGetLastNumOfEatenStones(int lastNumOfEatenStones) {
		this.lastNumOfEatenStones = lastNumOfEatenStones;
	}

	public Color getPlayer() {
		return player;
	}

	public void setPlayer(Color player) {
		this.player = player;
	}

	public List<Stone> getStones() {
		return stones;
	}

	public void setStones(List<Stone> stones) {
		this.stones = stones;
	}

	public Patch[][] getPatches() {
		return patches;
	}

	public void setPatches(Patch[][] patches) {
		this.patches = patches;
	}

	
	
	
	
	
}
