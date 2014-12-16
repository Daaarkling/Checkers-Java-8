package model;

import javafx.scene.layout.GridPane;

/**
 *
 * @author Jan Vaňura
 */
public class Board {

	private final GridPane grid;
	private final Patch[][] patches = new Patch[8][8];
	

	public Board(GridPane grid) {

		this.grid = grid;
		initBoard();
	}

	private void initBoard() {

		for (int i = 0; i < patches.length; i++) {
			for (int j = 0; j < patches.length; j++) {			
				String color = Patch.BLACK;
				if (j % 2 == 0) {
					if (i % 2 == 0) {
						color = Patch.WHITE;
					}
				} else {
					if (i % 2 != 0) {
						color = Patch.WHITE;
					}
				}
				patches[i][j] = new Patch(i, j, color);				
				grid.add(patches[i][j], i, j);
			}
		}
	}

	public Patch[][] getPatches() {
		return patches;
	}
	
	

}
