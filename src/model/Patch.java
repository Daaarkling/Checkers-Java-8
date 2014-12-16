package model;

import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;

/**
 *
 * @author Jan Vaňura
 */
public class Patch extends FlowPane {
	
	public static final String BLACK = "black";
	public static final String WHITE = "white";
	private String color;
	private int x, y;

	
	
	public Patch(int x, int y, String color) {
		
		super();
		this.color = color;
		this.x = x;
		this.y = y;
		setStyle("-fx-background-color: " + color + "; -fx-alignment: center;");
	}

	
	public void add(Stone stone){
		
		getChildren().add(stone);
	}
	
	public void remove(){

		if(hasStone()){
			Stone stone = getStone();
			stone.setEaten(true);
			getChildren().remove(stone);
		}
	}
	
	
	
	public Stone getStone(){
		
		if(hasStone()){
			return (Stone) getChildren().get(0);
		}
		return null;
	}
	
	public boolean hasStone(){
		
		if(!getChildren().isEmpty()){
			Stone stone = (Stone) getChildren().get(0);
			return !stone.isEaten();
		}
		return false;
	}
	
	public boolean hasStone(Color who){
		
		if(hasStone()){
			Stone stone = getStone();
			return stone.getPlayer().equals(who);
		} else {
			return false;
		}
	}
	
	public boolean hasStone(Stone stone){
		
		if(hasStone()){
			Stone s = getStone();
			return stone.equals(s);
		} else {
			return false;
		}
	}
	
	
	public void setHighlight(boolean value) {

		if(value)
			setStyle("-fx-background-color: green; -fx-alignment: center;");
		else
			setStyle("-fx-background-color: " + color + "; -fx-alignment: center;");
	}
	
	public void setHighlightHover(boolean value) {

		if(value)
			setStyle("-fx-background-color: yellow; -fx-alignment: center;");
		else
			setStyle("-fx-background-color: " + color + "; -fx-alignment: center;");
	}
	
	
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	

	
}
