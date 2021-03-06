/*******************************************************************************
 * Copyright (c) 2011 Enrique Munoz de Cote.
 * repeatedgames is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * repeatedgames is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with repeatedgames.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Please send an email to: jemc@inaoep.mx for comments or to become part of this project.
 * Contributors:
 *     Enrique Munoz de Cote - initial API and implementation
 ******************************************************************************/
/**
 * 
 */
package util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * @author enrique
 * this class implements a state, where a state constitute a set of strategies.
 * One for each agent, where number of players is defined in Init(int players, ...)
 *
 */
public  class StrategyState extends State<Strategy,Strategy> {
	private Map<Strategy,Double> reward;
	private Map<Strategy,Integer> experience;
	private int experienceSum = 0;
	private int K = 15; 
	private final double GAMMA = 0.05;
	
	public StrategyState(){
		super();
	}
	
	public void Init(int players, Set<Strategy> strategySet, StrategyState s0, double Rmax, int k){
		//super.init(strategySet);
		this.K = k;
		reward = new HashMap<Strategy, Double>(strategySet.size());
		experience = new HashMap<Strategy, Integer>(strategySet.size());
		for (Iterator<Strategy> iterator = strategySet.iterator(); iterator.hasNext();) {//for all actions
			Strategy strat = (Strategy) iterator.next();
			HashMap<State<Strategy,Strategy>,Integer> nextState = new HashMap<State<Strategy,Strategy>,Integer>(1);
			nextState.put(s0, 1);
			T.put(strat, nextState);
			reward.put(strat, Rmax/(1-GAMMA));
			experience.put(strat, 1);
		}
		//reward = new int[players];
		//java.util.Arrays.fill(reward, 0);
	}
	
	public Map<StrategyState,Double> probs(Strategy strat){
		int div = experience.get(strat);
		assert(div != 0);
		//System.out.print(this.features);
		//System.out.print("'"+strat);
		Map<StrategyState,Double> probs = new HashMap<StrategyState,Double>(T.get(strat).size());
		for (State s : T.get(strat).keySet()) {
			StrategyState state = (StrategyState) s;
			//assert(state!=null);
			probs.put(state, T.get(strat).get(state)/ (double)div);
		}
		return probs;
	}
	/**
	 * this is R-max updates! might completely change for other algorithms
	 * @param strat action
	 * @param r instantaneous rewards
	 */
	public boolean update(Strategy strat, StrategyState state, double r){
		//assert(state!=null);
		if(!T.get(strat).containsKey(state))
			T.get(strat).put(state, 1);
		else
			T.get(strat).put(state, T.get(strat).get(state)+1);
		//this should be an (exponential) average
		//TODO: check this update
		double p = (1-GAMMA) *reward.get(strat) + GAMMA * r;
		reward.put(strat, p);
		experience.put(strat, experience.get(strat)+1);
		experienceSum++;
		if(experienceSum == K){
			known = true;
			//System.out.print(this.features);
			return true;
		}
		else return false;
	}
	
	public double getReward(Strategy strat){
		return reward.get(strat);
	}
	public boolean isKnown(){
		return known;
	}
	
	public int getExperience(){
		return experienceSum;
	}
}
