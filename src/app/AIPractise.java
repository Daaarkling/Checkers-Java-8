package app;

import ai.AIBot;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;
import model.Board;
import model.Patch;
import model.Stone;
import ai.RandomBot;
import ai.SmartBot;
import model.QeenStone;

/**
 *
 * @author Jan Vaňura
 */
public class AIPractise {
	
	public static final int DRAW = 30;
	public static final int NUM_OF_ROUNDS = 100;
	private Patch[][] patches; 
	private List<Stone> stones = new ArrayList<>();
	private List<Stone> possibleJumpers = new ArrayList<>();
	private Color player;
	private AIBot botAlly, botEnemy;
	private int inactivity = 0, rounds = 0;
	private AIBot loser;
	
	
	public AIPractise(Board board) {
		
		patches = board.getPatches();
		//botAlly = new RandomBot(stones, patches, Stone.ALLY);
		//botEnemy = new RandomBot(stones, patches, Stone.ENEMY);
		botAlly = new SmartBot(stones, patches, Stone.ALLY);
		botEnemy = new SmartBot(stones, patches, Stone.ENEMY);
	}
	
	
	public void start(){
	
		player = Stone.ALLY;		
		stones.clear();
		for (int i = 0; i < patches.length; i++) {
			for (int j = 0; j < patches.length; j++) {							
				patches[i][j].remove();
			}
		}
		initStones();
		
		
		botAlly.setStones(stones);
		botAlly.setPatches(patches);
		botAlly.setPlayer(Stone.ALLY);
		botAlly.generateValues();
		
		botEnemy.setStones(stones);
		botEnemy.setPatches(patches);
		botEnemy.setPlayer(Stone.ENEMY);
		botEnemy.generateValues();

		if (loser != null) loser.generateValues();
		loser = null;
		botMove();
	}
	
	
	
	private void initStones() {
		
		int k;
		for (int j = 0; j < 3; j++) {
			for (int i = 0; i < patches.length; i = i + 2) {
				if (j % 2 != 0) k = 0;
				else k = 1;
				stones.add(new Stone(patches, i + k, j, Stone.ENEMY));
			}
		}

		for (int j = patches.length; j > patches.length - 3; j--) {
			for (int i = 0; i < 8; i = i + 2) {
				if (j % 2 == 0) k = 0;
				else k = 1;
				stones.add(new Stone(patches, i + k, j - 1, Stone.ALLY));
			}
		}				
	}

	
	private void botMove(){
		
		checkGameOver();		
		Stone stone;
		if(player.equals(botAlly.getPlayer())){
			stone = botAlly.move();
		} else {
			stone = botEnemy.move();
		}
		inactivity++;
		if(stone != null) checkQeenStone(stone);
		changePlayers();
		hasToJump();
	}

	
	private void hasToJump() {

		possibleJumpers.clear();
		stones.stream().filter((stone) -> (!stone.isEaten() && player.equals(stone.getPlayer()))).map((stone) -> {
			stone.jump();
			return stone;
		}).filter((stone) -> (stone.isJump())).forEach((stone) -> {
			possibleJumpers.add(stone);
		});
		resolvePossibleJumpers();
	}
	
	private void resolvePossibleJumpers() {
		
		boolean qeenPresent = false;
		List<Stone> justStones = new ArrayList<>();
		
		for(Stone stone : possibleJumpers){
			if(stone instanceof QeenStone){
				qeenPresent = true;
			} else {
				justStones.add(stone);
			}
		}
		
		if(qeenPresent){
			possibleJumpers.removeAll(justStones);
		}		
		botJump();
	}
	
	
	private void botJump(){
	
		checkGameOver();
		Stone stone;
		if(!possibleJumpers.isEmpty()){
			if(player.equals(botAlly.getPlayer())){			
				stone = botAlly.jump(possibleJumpers);		
				inactivity = 0;
			} else {
				stone = botEnemy.jump(possibleJumpers);
				inactivity = 0;
			}	
			changePlayers();
			checkQeenStone(stone);
			hasToJump();
		}
		botMove();
	}
	
	
	private void changePlayers(){
		
		if (player.equals(Stone.ALLY))
			player = Stone.ENEMY;
		else
			player = Stone.ALLY;
	}
	
	

	private void checkQeenStone(Stone stone) {
		
		if(stone.isOnQeenPosition()){
			QeenStone qeen = new QeenStone(patches, stone.getX(), stone.getY(), stone.getPlayer());
			patches[stone.getX()][stone.getY()].remove();
			stones.remove(stone);
			stones.add(qeen);
		}	
	}
	
	
	
	
	
	public void checkGameOver(){
		
		if(rounds >= AIPractise.NUM_OF_ROUNDS){
		
			System.exit(0);
		}
		
		if(inactivity > AIPractise.DRAW){
			System.out.println("We have no winner!");
			rounds++;
			start();
		}
				
		boolean over = true;
		for (Stone stone : stones){
			if(stone.getPlayer().equals(player) && stone.isMovePosible()){
				over = false;
			}
		}
		if(over){
			if(player.equals(Stone.ALLY)){
				System.out.println("Enemy won!");
				System.out.println(botEnemy);
				loser = botAlly;
			} else {
				System.out.println("Ally won!");
				System.out.println(botAlly);
				loser = botEnemy;
			}
			rounds++;
			start();
		}
	}
}
