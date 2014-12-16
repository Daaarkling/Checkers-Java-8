package ai;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import javafx.scene.paint.Color;
import model.Jump;
import model.Patch;
import model.QeenStone;
import model.Stone;

/**
 *
 * @author Jan Vaňura
 */
public class SmartBot extends AIBot {

	private int threatValue, safeValue, distanceValue, attackValue, eatenStonesValue;
	
	private Random random;
	private Color opposite;
	private List<Stone> virtualStones = new ArrayList<>();
	private Patch[][] virtualPatches = new Patch[8][8];

	
	
	public SmartBot(List<Stone> stones, Patch[][] patches, Color player) {
		super(stones, patches, player);
		random = new Random();
		if(player.equals(Stone.ALLY))
			opposite = Stone.ENEMY;
		else
			opposite = Stone.ALLY;
		
		threatValue = 70;
		safeValue = 97;
		distanceValue = 30;
		attackValue = 30;
		eatenStonesValue = 78;
	}
	
    @Override
	public void generateValues(){
	
		threatValue = random.nextInt(50)+50;
		safeValue = random.nextInt(50)+50;
		distanceValue = random.nextInt(50)+50;
		attackValue = random.nextInt(50)+50;
		eatenStonesValue = random.nextInt(50)+50;
	}
    
    @Override
    public void mutate() {
        
    }
	

	@Override
	public Stone move() {
		
		List<Stone> possibleStones = new ArrayList<>();
		stones.stream().filter((stone) -> (stone.getPlayer().equals(player) && stone.isMovePosible())).forEach((stone) -> {
			possibleStones.add(stone);
		});
		
		List<EvalStone> evalStones = new ArrayList<>();
        possibleStones.stream().forEach((stone) -> {
            evalStones.add(evaulateStoneMove(stone));
        });
 		
		EvalStone tempEvalStone = evalStones
            .stream()
                .max(Comparator.comparing(item -> item.getMaxEvalPosValue()))
                .get();
		
        List<EvalStone> eses = new ArrayList<>();
        evalStones.stream().filter((eS) -> (eS.getMaxEvalPosValue() == tempEvalStone.getMaxEvalPosValue())).forEach((eS) -> {
            eses.add(eS);
        });
        
        int randomEvalStone = random.nextInt(eses.size());
        EvalStone finalEvalStone = eses.get(randomEvalStone);
  
		
		List<EvalPosition> eps = finalEvalStone.getMaxEvalPos();
        int randomEvalPos = random.nextInt(eps.size());
        
		EvalPosition finalEvalPosition = eps.get(randomEvalPos);		
		
		Stone stone = finalEvalStone.getStone();
		stone.moveOnPosition(finalEvalPosition.getPatch().getX(), finalEvalPosition.getPatch().getY());
		
		
		return stone;
	}
	
	

	@Override
	public Stone jump(List<Stone> possibleJumpers) {
				
		List<EvalStone> evalStones = new ArrayList<>();
        possibleJumpers.stream().forEach((stone) -> {
            evalStones.add(evaulateStoneJump(stone));
        });
		
		EvalStone tempEvalStone = evalStones.stream()
			.max(Comparator.comparing(item -> item.getMaxEvalJumpValue()))
			.get();
        
        List<EvalStone> eses = new ArrayList<>();
        evalStones.stream().filter((eS) -> (eS.getMaxEvalJumpValue() == tempEvalStone.getMaxEvalJumpValue())).forEach((eS) -> {
            eses.add(eS);
        }); 
        
        int randomEvalStone = random.nextInt(eses.size());
        EvalStone finalEvalStone = eses.get(randomEvalStone);
        
        List<EvalJump> ejs = finalEvalStone.getMaxEvalJump();
        int randomEvalJump = random.nextInt(ejs.size());       
		EvalJump finalEvalJump = ejs.get(randomEvalJump);
      
        List<EvalPosition> eps = finalEvalJump.getMaxEvalPos();
        int randomEvalPos = random.nextInt(eps.size());      
		EvalPosition finalEvalPosition = eps.get(randomEvalPos);	
		
		Stone stone = finalEvalStone.getStone();
		Jump jump = finalEvalJump.getJump();
		lastNumOfEatenStones = jump.getEatenStones().size();
		jump.jump();
		stone.jumpOnPosition(finalEvalPosition.getPatch().getX(), finalEvalPosition.getPatch().getY());

		
		return stone;
	}

	private EvalStone evaulateStoneMove(Stone stone) {
		
		EvalStone es = new EvalStone();
		es.setStone(stone);
		EvalPosition ep;
		
		int sum = 0;		
		for (Patch patch : stone.getPossibleMoves()){		
			sum += evalIsNotThreat(stone, patch.getX(), patch.getY());
			sum += evalAttackOn(stone, patch.getX(), patch.getY());
			sum += evalSafePos(stone, patch.getX(), patch.getY());
            sum += evalDistanceToQeen(stone, patch.getY());
			
			ep = new EvalPosition();
			ep.setPatch(patch);
			ep.addEvaluation(sum);
			
			es.getEvalPositions().add(ep);
            sum = 0;
		}	
		return es;
	}
	
	
	private EvalStone evaulateStoneJump(Stone stone) {
		
		EvalStone es = new EvalStone();
		EvalJump ej;
		EvalPosition ep;	
		int sum = 0;

		
		virtualStones.clear();

		for (Jump jump : stone.getJumps()){		
			
			ej = new EvalJump();
			for (Patch patch : jump.getFinalPatches()){		
				sum += evalIsNotThreat(stone, patch.getX(), patch.getY());
				sum += evalAttackOn(stone, patch.getX(), patch.getY());
				sum += evalSafePos(stone, patch.getX(), patch.getY());
                sum += evalDistanceToQeen(stone, patch.getY());
				sum += evalEatenStones(jump);

				ep = new EvalPosition();
				ep.setPatch(patch);
				ep.addEvaluation(sum);		
								
				ej.getEvalPositions().add(ep);
				ej.setJump(jump);
                sum = 0;
			}
			es.getEvalJumps().add(ej);
			es.setStone(stone);
		}
		
		return es;
	}
	

	
	
	private int evalIsNotThreat(Stone s, int x, int y) {
		
		Stone virtualStone = getVirtualCheckers(s);
		
		virtualStone.moveOnPosition(x, y);		

		List <Stone> possibleJumpers = new ArrayList<>();
		

		virtualStones.stream().filter((stone) -> (!stone.isEaten() && stone.getPlayer().equals(opposite))).map((stone) -> {
			stone.jump();
			return stone;
		}).filter((stone) -> (stone.isJump())).forEach((stone) -> {
			possibleJumpers.add(stone);
		});
        
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

		for(Stone stone : possibleJumpers){
			for(Jump jump : stone.getJumps()){
				if(jump.getEatenStones().contains(virtualStone)){
					return -threatValue;
				}
			}
		}
		return threatValue;
	}


	
	
	private int evalDistanceToQeen(Stone stone, int y) {
		
		if(stone instanceof QeenStone){
			return 0;
		}
		
		int distance;
		if(player.equals(Stone.ALLY)){
			distance = y;
		} else {
			distance = patches.length-1 - y;
		}

        if(distance == 7){
            return -distanceValue;
        }
		
		return (int)((1.f/(distance*distance))*distanceValue);
	}

	
	 
	private int evalAttackOn(Stone s, int x, int y) {
		
		Stone virtualStone = getVirtualCheckers(s);
		virtualStone.moveOnPosition(x, y);
          
        int i;
        if(player.equals(Stone.ALLY)){
            i = -1;
        } else {
            i = 1;
        }
		
        int checkX = s.getX()-virtualStone.getX();
        int checkY = virtualStone.getY()-i;
        
        if(checkY <= 7 || checkY >= 0) return 0;
        
        System.out.println("x: " + virtualStone.getX() + "  y: " + virtualStone.getY());
        System.out.println(checkX);
        System.out.println(checkY);
        
		virtualStone.jump();
		if(virtualStone.isJump() && (checkY <= patches.length-1 || checkY >= 0) && (checkX < 0 || checkX > patches.length-1) && virtualPatches[checkX][checkY].hasStone(player)){
			return attackValue;
		}
		return 0;
	}

	
	private int evalSafePos(Stone s, int x, int y){
	
		Stone virtualStone = getVirtualCheckers(s);
		virtualStone.moveOnPosition(x, y);
		
		int k;
		for(int i = 0; i < patches.length; i += patches.length-1){
			if(i == 0) k = 1;
			else k = 0;
			for(int j = k; j < patches.length; j += 2){
				if(virtualStone.getX() == i && virtualStone.getY() == j){
					return safeValue;
				}
			}
		}
		return 0;
	}
	
	private int evalEatenStones(Jump jump){
	
		return jump.getEatenStones().size() * eatenStonesValue;
	}
	
	
	
	
	private Stone getVirtualCheckers(Stone s) {
		
		virtualStones.clear();
		
		List<Stone> sourceStones;
		Patch[][] sourcePatches;
		Stone virtualStone = null;
		
		sourceStones = stones;
		sourcePatches = patches;
		
		for (int i = 0; i < sourcePatches.length; i++) {
			for (int j = 0; j < sourcePatches.length; j++) {							
				virtualPatches[i][j] = new Patch(sourcePatches[i][j].getX(), sourcePatches[i][j].getY(), sourcePatches[i][j].getColor());				
			}
		}

		for(Stone stone : sourceStones){
			if(!stone.isEaten()){
				if(stone.equals(s)){
					virtualStone = new Stone(virtualPatches, stone.getX(), stone.getY(), stone.getPlayer());
					virtualStones.add(virtualStone);
				} else {
					virtualStones.add(new Stone(virtualPatches, stone.getX(), stone.getY(), stone.getPlayer()));
				}
			}
		}
		
		return virtualStone;
	}

	@Override
	public String toString() {
		return "SmartBot{" + "threatValue=" + threatValue + ", safeValue=" + safeValue + ", distanceValue=" + distanceValue + ", attackValue=" + attackValue + ", eatenStonesValue=" + eatenStonesValue + '}';
	}

    


}
