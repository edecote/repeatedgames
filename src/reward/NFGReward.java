package reward;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import util.Action;
import util.ObservableEnvInfo;
import util.State;



//
/**
 * @author Enrique Munoz de Cote
 * This class creates rewards for different classic games
 */
public class NFGReward extends Reward{
	/**
	 * the first Vector of string is a vector of actions (as strings), the second String is the id of the agent, 
	 * the int is the reward
	 */
	Map<Vector<Integer>, Map<String,Integer>> rewards;
	Vector<Integer> jointA = new Vector<Integer>(2);
	Vector<Integer> jointA1 = new Vector<Integer>(2);
	Vector<Integer> jointA2 = new Vector<Integer>(2);
	Vector<Integer> jointA3 = new Vector<Integer>(2);
	
	Map<String,Integer> r = new HashMap<String, Integer>();
	Map<String,Integer> r1 = new HashMap<String, Integer>();
	Map<String,Integer> r2 = new HashMap<String, Integer>();
	Map<String,Integer> r3 = new HashMap<String, Integer>();
	public enum Game
	{
	    PD, CHICKEN, BOS; 
	}

	public NFGReward () {
		rewards = new HashMap<Vector<Integer>, Map<String,Integer>>();
	}
	// generates the reward function
	public void Init (String game) {
		
		switch (Game.valueOf(game)) {
		
		case PD: //0 = D, 1 = D
			jointA.add(0); jointA.add(0); r.put("row", 1); r.put("col", 1);
			rewards.put(jointA, r);
			
			jointA1.add(0); jointA1.add(1); r1.put("row", 4); r1.put("col", 0);
			rewards.put(jointA1, r1);
			
			jointA2.add(1); jointA2.add(0); r2.put("row", 0); r2.put("col", 4);
			rewards.put(jointA2, r2);

			jointA3.add(1); jointA3.add(1); r3.put("row", 3); r3.put("col", 3);
			rewards.put(jointA3, r3);
			break;

		case CHICKEN: //0 = D, 1 = D

			jointA.add(0); jointA.add(0); r.put("row", 3); r.put("col", 3);
			rewards.put(jointA, r);

			jointA1.add(0); jointA1.add(1); r1.put("row", 2); r1.put("col", 5);
			rewards.put(jointA1, r1);

			jointA2.add(1); jointA2.add(0); r2.put("row", 5); r2.put("col", 2);
			rewards.put(jointA2, r2);

			jointA3.add(1); jointA3.add(1); r3.put("row", 1); r3.put("col", 1);
			rewards.put(jointA3, r3);
			break;
			
		case BOS: //0 = D, 1 = D

			jointA.add(0); jointA.add(0); r.put("row", 5); r.put("col", 3);
			rewards.put(jointA, r);

			jointA1.add(0); jointA1.add(1); r1.put("row", 0); r1.put("col", 0);
			rewards.put(jointA1, r1);

			jointA2.add(1); jointA2.add(0); r2.put("row", 0); r2.put("col",0);
			rewards.put(jointA2, r2);

			jointA3.add(1); jointA3.add(1); r3.put("row", 3); r3.put("col", 5);
			rewards.put(jointA3, r3);
			break;
			
		default:
			break;
		}
	
	}
	
	public int getReward(ObservableEnvInfo s, Vector<Object> jointAction, int agent){
		if(agent == 0)
			return rewards.get(jointAction).get("row"); 
		else
			return rewards.get(jointAction).get("col");
	}
	
	@Override
	public int[] getRewards(ObservableEnvInfo s, Vector<Action> jointAction){
		Vector<Object> feat = toFeatures(jointAction);
		int[] rwds =  new int[2];
		rwds[0] = rewards.get(toFeatures(jointAction)).get("row");
		rwds[1] = rewards.get(toFeatures(jointAction)).get("col");
		return rwds;

	}
	
	private Vector<Object> toFeatures(Vector<Action> jointAction){
		Vector<Object> feats = new Vector<Object>();
		for (int i=0; i< jointAction.size(); i++) {
			feats.add(jointAction.get(i).getCurrentState());
		}
		return feats;
	}


}
