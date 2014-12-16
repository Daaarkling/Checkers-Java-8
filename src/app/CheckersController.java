package app;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import model.Board;
import model.Jump;

/**
 *
 * @author Jan Vaňura
 */
public class CheckersController implements Initializable {

	@FXML
	private GridPane grid;
	
	@FXML
	private RadioButton rbSoundOn;
	
	@FXML
	private RadioButton rbSoundOff;
	
	@FXML
	private Label lbOverText;

	@FXML
	private ListView<Jump> listJumps;
	
	private Board board;
	private Checkers app;
	private AIPractise aIPractise;
	
	

	@Override
	public void initialize(URL url, ResourceBundle rb) {
				
		ToggleGroup tG = new ToggleGroup();
		rbSoundOn.setToggleGroup(tG);
		rbSoundOff.setToggleGroup(tG);
		start(true);		
	}

	
	@FXML
	public void handleBtnPvP(ActionEvent event){
		
		start(false);
	}
	
	@FXML
	public void handleBtnPvE(ActionEvent event){
		
		//aIPractise = new AIPractise(new Board(grid));
		//aIPractise.start();
		start(true);
	}
	
	@FXML
	public void handleBtnDeselect(ActionEvent event){
		
		listJumps.getSelectionModel().clearSelection();
		app.getPossibleJumps().stream().forEach((jump) -> {
			jump.setHighlight(true);
		});	
	}
	
	@FXML
	public void handleSounds(ActionEvent event){
		
		RadioButton rb = (RadioButton) event.getSource();
		if(rb.equals(rbSoundOn)){
			app.setSound(true);
		} else {
			app.setSound(false);
		}		
	}

	private void initJumpList() {
		
		listJumps.setItems(app.getPossibleJumps());
		listJumps.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Jump> observable, Jump oldValue, Jump newValue) -> {
						
			if(oldValue != null) oldValue.setHighlightHover(false);
			app.getPossibleJumps().stream().forEach((jump) -> {
				jump.setHighlight(false);
			});
			
			if(newValue != null){
				newValue.setHighlightHover(true);
				app.setSelectedJump(newValue);
			}
		});
	}
	
	
	private void start(boolean pve){
		
		board = new Board(grid);
		app = new Checkers(board, pve);
		initJumpList();
		lbOverText.textProperty().bind(app.getOverText());
	}
}
