package ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.scene.paint.Color;
import model.Jump;
import model.Patch;
import model.Stone;

/**
 *
 * @author Jan Vaňura
 */
public class RandomBot extends AIBot {

	private Random random;

	
	
	public RandomBot(List<Stone> stones, Patch[][] patches, Color player) {
		super(stones, patches, player);
		random = new Random();
	}
	

	@Override
	public Stone move() {
		
		List<Stone> possibleMoves = new ArrayList<>();
		stones.stream().filter((stone) -> (stone.getPlayer().equals(player) && stone.isMovePosible())).forEach((stone) -> {
			possibleMoves.add(stone);
		});
		
		if(!possibleMoves.isEmpty()){
			int randomStone = random.nextInt(possibleMoves.size());
			Stone stone = possibleMoves.get(randomStone);

			
			int randomMove = random.nextInt(stone.getPossibleMoves().size());
			Patch patch = stone.getPossibleMoves().get(randomMove);

			stone.moveOnPosition(patch.getX(), patch.getY());
			return stone;
		}
		return null;
	}

	@Override
	public Stone jump(List<Stone> possibleJumpers) {
		
		int randomStone = random.nextInt(possibleJumpers.size());
		Stone stone = possibleJumpers.get(randomStone);

		int randomJump = random.nextInt(stone.getJumps().size());
		Jump jump = stone.getJumps().get(randomJump);
		
		
		
		int randomPos = random.nextInt(jump.getFinalPatches().size());
		Patch patch = jump.getFinalPatches().get(randomPos);

		lastNumOfEatenStones = jump.getEatenStones().size();
		jump.jump();
		stone.jumpOnPosition(patch.getX(), patch.getY());

		return stone;
	}

	@Override
	public void generateValues() {
		
	}

    @Override
    public void mutate() {
    }

}
