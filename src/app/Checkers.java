package app;

import ai.AIBot;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import model.Board;
import model.Patch;
import model.Stone;
import ai.RandomBot;
import ai.SmartBot;
import java.io.File;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import model.Jump;
import model.QeenStone;

/**
 *
 * @author Jan Vaňura
 */
public class Checkers {
	
	public static final int DRAW = 20;
	private Board board;
	private Patch[][] patches; 
	private List<Stone> stones = new ArrayList<>();
	private List<Stone> possibleJumpers = new ArrayList<>();
	private ObservableList<Jump> possibleJumps = FXCollections.observableArrayList();
	private List<Media> media = new ArrayList<>();
	private StringProperty overText = new SimpleStringProperty();
	private Color player;
	private Stone activeStone;
	private AIBot bot;
	private int jumpCounterAlly = 0, jumpCounterEnemy = 0;
	private MediaPlayer mediaPlayer;
	private boolean sound;
	private Jump selectedJump;
	private int inactivity = 0;
	
	
	
	public Checkers(Board board, boolean pve) {
		
		this.board = board;
		patches = board.getPatches();
		player = Stone.ALLY;
		sound = true;
		overText.set("");
		initStones();
		initPatchesListeners();
		initPlayer();
		//if(pve) bot = new RandomBot(stones, patches, Stone.ENEMY);
		if(pve) bot = new SmartBot(stones, patches, Stone.ENEMY);
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
	
			
		
		/*
		
		stones.add(new Stone(patches, 3, 3, Stone.ALLY));
		stones.add(new Stone(patches, 0, 5, Stone.ALLY));
        stones.add(new Stone(patches, 6, 2, Stone.ALLY));
        stones.add(new Stone(patches, 1, 1, Stone.ALLY));
        stones.add(new Stone(patches, 4, 6, Stone.ALLY));
		stones.add(new Stone(patches, 3, 1, Stone.ENEMY));
		stones.add(new QeenStone(patches, 0, 0, Stone.ENEMY));	
                */
	}
	
	
	private void initPatchesListeners() {
		
		for (int i = 0; i < patches.length; i++) {
			for (int j = 0; j < patches.length; j++) {							
				patches[i][j].setOnMouseClicked((MouseEvent event) -> {
					Patch patch = (Patch) event.getSource();
					play(patch);
				});				
			}
		}
	}
	

	private void initPlayer(){
				
		media.add(new Media(new File("firstblood.wav").toURI().toString()));
		media.add(new Media(new File("Double_Kill.wav").toURI().toString()));
		media.add(new Media(new File("triple_kill.wav").toURI().toString()));
		media.add(new Media(new File("UltraKill.wav").toURI().toString()));
		media.add(new Media(new File("Rampage.wav").toURI().toString()));
	}
	
	
	private void play(Patch patch){

		if (activeStone != null && possibleJumpers.contains(activeStone) && !patch.hasStone(player)) {		// jump with jumper			
			if(selectedJump != null){	
				if(activeStone.equals(selectedJump.getStartPatch().getStone()) && selectedJump.isFinalPosition(patch.getX(), patch.getY())){
					jump(selectedJump, patch.getX(), patch.getY());
				}
			} else {				
				for(Jump jump : activeStone.getJumps()){
					if(jump.isFinalPosition(patch.getX(), patch.getY())){		
						jump(jump, patch.getX(), patch.getY());							
						return;
					}
				}	
			}
		} else if(activeStone != null && !patch.hasStone()) {		// make a move with selected stone
			if(activeStone.moveOnPosition(patch.getX(), patch.getY())){
				checkQeenStone(activeStone);
				activeStone = null;
				inactivity++;
				changePlayers();
				checkGameOver();
				hasToJump();
				botMove();
			}

		} else if(patch.hasStone(player)){	// i wanna play with this stone

			Stone clickStone = patch.getStone();

			if(!possibleJumpers.isEmpty()){		// i have to play with jumper
				if(activeStone == null && possibleJumpers.contains(clickStone)){
					activeStone = clickStone;
					activeStone.setActive(true);
				} else if(activeStone != null) {	// i dont want to play with this jumper
					activeStone.setActive(false);
					activeStone = null;
				}					
			} else {
				if(activeStone == null){		// i want to play with this stone
					activeStone = clickStone;
					activeStone.setActive(true);
				} else {							// i dont want to play with this stone
					activeStone.setActive(false);		
					activeStone = null;
				}
			}
		}	
	}
	

	
	private void hasToJump() {

		possibleJumpers.clear();
		possibleJumps.clear();
		
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
		
		possibleJumpers.stream().map((stone) -> {
			possibleJumps.addAll(stone.getJumps());
			return stone;
		}).forEach((stone) -> {
			stone.getJumps().stream().forEach((jump) -> {
				jump.setHighlight(true);
			});
		});
				
		botJump();
	}
	
	
	
	private void jump(Jump jump, int x, int y) {
		
		playSound(jump.getEatenStones().size());
		
		if (player.equals(Stone.ALLY)){
			jumpCounterAlly += jump.getEatenStones().size();
			inactivity = 0;
		} else {
			jumpCounterEnemy += jump.getEatenStones().size();
			inactivity = 0;
		}
		
		jump.jump();
		activeStone.jumpOnPosition(x, y);
		changePlayers();
		checkGameOver();
		checkQeenStone(activeStone);
		resetPatchesHighlight();
		activeStone = null;
		selectedJump = null;		
		hasToJump();
		botMove();
	}
	
	
	private void botMove(){
	
		if(bot != null && player.equals(bot.getPlayer())){
			Stone stone = bot.move();
			inactivity++;
			if(stone != null) checkQeenStone(stone);
			changePlayers();
			hasToJump();
			checkGameOver();					
		}
	}
	
	private void botJump(){
	
		if(bot != null && player.equals(bot.getPlayer()) && !possibleJumpers.isEmpty()){			
			Stone stone = bot.jump(possibleJumpers);
			playSound(bot.getLastNumOfEatenStones());
			jumpCounterEnemy += bot.getLastNumOfEatenStones();
			inactivity = 0;
			
			changePlayers();
			checkGameOver();
			checkQeenStone(stone);
			resetPatchesHighlight();
			hasToJump();
		}
	}
	
	
	private void changePlayers(){
		
		if (player.equals(Stone.ALLY))
			player = Stone.ENEMY;
		else
			player = Stone.ALLY;
	}
	
	private void resetPatchesHighlight(){
	
		for (int i = 0; i < patches.length; i++) {
			for (int j = 0; j < patches.length; j++) {
				patches[i][j].setHighlight(false);
			}
		}
	}

	private void checkQeenStone(Stone stone) {
		
		if(stone.isOnQeenPosition()){
			QeenStone qeen = new QeenStone(patches, stone.getX(), stone.getY(), stone.getPlayer());
			patches[stone.getX()][stone.getY()].remove();
			stones.remove(stone);
			stones.add(qeen);
		}	
	}
	
	
	private void playSound(int count) {
		
		if(!sound){
			return;
		}
		
		if(jumpCounterAlly == 0 && jumpCounterEnemy == 0){
			mediaPlayer = new MediaPlayer(media.get(0));
			mediaPlayer.setOnEndOfMedia(() -> {
				playMultiKill(count);
			});		
			mediaPlayer.play();
		} else {
			playMultiKill(count);
		}		
	}
	
	private void playMultiKill(int count){
	
		if(count == 2){
			mediaPlayer = new MediaPlayer(media.get(1));
			mediaPlayer.play();
		} else if(count == 3){
			mediaPlayer = new MediaPlayer(media.get(2));
			mediaPlayer.play();
		} else if(count == 4){
			mediaPlayer = new MediaPlayer(media.get(3));
			mediaPlayer.play();
		} else if(count == 5){
			mediaPlayer = new MediaPlayer(media.get(4));
			mediaPlayer.play();
		}
	}
	
	
	public void checkGameOver(){

		if(inactivity > Checkers.DRAW){
			overText.set("We have no winner!");
			System.out.println("We have no winner!");
		}
				
		boolean over = true;
		for (Stone stone : stones){
			if(stone.getPlayer().equals(player) && stone.isMovePosible()){
				over = false;
				//break;
			}
		}
		if(over){
			if(player.equals(Stone.ALLY)){
				overText.set("Enemy won!");
				System.out.println("Enemy won!");
			} else {
				overText.set("Ally won!");
				System.out.println("Ally won!");
			}
		}
	}

	public boolean isSound() {
		return sound;
	}

	public void setSound(boolean sound) {
		this.sound = sound;
	}

	public ObservableList<Jump> getPossibleJumps() {		
		return possibleJumps;
	}
	
	public void setSelectedJump(Jump selectedJump){		
		this.selectedJump = selectedJump;
	}

	public StringProperty getOverText() {
		return overText;
	}

}
