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
package util;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * @author enrique
 * This implements statedomain for integer joint actions and coordinates
 *
 */
public class StateDomain_JointActionGrid extends StateDomain<State_JointActionGrid> {
	//maps joint coordinates and joint actions to states 
	private Map<Vector<int[]>,Map<Vector<Object>,State_JointActionGrid>> mapping;
	private int numAgents = 0;
	private int numStates = 0;
	private int[] limits = new int[2];
	private int[] agentsActions;
	
	/**
	 * Constructs the state space domain using relevant info from the environment
	 * @param actions
	 */
	public StateDomain_JointActionGrid (Info_Grid state){
		domain = new LinkedHashSet<State_JointActionGrid>();
		mapping = new HashMap<Vector<int[]>, Map<Vector<Object>, State_JointActionGrid>>();
		numAgents = state.currentJointAction().size();
		
		//get the number of actions
		agentsActions = new int[numAgents];
		int numberOfActions = 1;
		  for (int i = 0; i < numAgents; i++){
			  agentsActions[i] = state.currentJointAction().get(i).getDomainSet().size();
			  numberOfActions *= agentsActions[i];
		  }
		  
		//get the number of coordinates
		limits =  state.currentJointCoord().get(0).getLimits();
		int numberOfCoords = limits[0]*limits[1];
		
		int[] coord = new int[limits.length];
		Vector<Object> jointAct = new Vector<Object>();
		for (int k = 0; k < numAgents; k++) {
			for (int l = 0; l < limits[0]; l++) {
				coord[0] = l;
				for (int m = 0; m < limits[1]; m++) {//for each joint coordinate
					coord[1]= m;
					
					for (int i = 0; i < numAgents; i++) {
						for(Object o : state.currentJointAction().get(i).getDomainSet())
							jointAct.add(o);
					}
					
					
					
					
					
					
					for (int i = 0; i < numberOfActions; i++) {
						State_JointActionGrid s = new State_JointActionGrid();
						s.addFeature(l); s.addFeature(m);
						//TODO: fix this part
						for (int j = 0; j < numAgents; j++) {
							for(Object o : state.currentJointAction().get(j).getDomainSet()){
								jointAct.add(o);
							}
							domain.add(s);
						}
					}
				}
			}
		}
	}
	

	
// get state domain		
	public Set<State_JointActionGrid> getStateSet(){
		return domain;	
	}	
// returns size of the domain	
	public int size(){
		return domain.size();
	}
	
	/**
	 * Transforms info coming from the environment to the perceived state info
	 * @param info the info coming from the environment
	 * @return
	 */
	public State_JointActionGrid getState(ObservableEnvInfo e){
		String s = e.getClass().toString();
		if(s.equals("class util.Info_Grid")){
			Info_Grid grid = (Info_Grid) e;
			Vector<int[]> coord = grid.currentArrayJointCoord();
			//Map<Vector<Object>,State_JointActionGrid>  jointA= mapping.get(coord);
			Vector<Action> vectA = grid.currentJointAction();
			Vector<Object> vectO = new Vector<Object>();
			for (int i=0; i< vectA.size(); i++) {
				vectO.add(vectA.get(i));
			}
			Map<Vector<Object>,State_JointActionGrid> m = mapping.get(coord);
			return m.get(vectO);
		}
		else{
			System.err.println("the state info is not of type NFGInfo, JointActionStateDomain cannot handdle this info type");
			return null;
		}
	}
	
	/**
	 * @param n the state (joint action)
	 * @return the vector of actions
	 */
	private int[] toActions(int n){
		int[] strat = new int[numAgents];
	       int i=0;
	       while (n > 0) {
	          strat[i] = n % agentsActions[i];
	          n = n/agentsActions[i];
	          i++;
	       }
	       assert(strat.length == numAgents);
	       return strat;
	}
	
	/**
	 * @param n the state (joint coord)
	 * @return the vector of coordinates
	 */
	private int[] toCoords(int n){
		int[] coord = new int[2];
	       int i=0;
	       while (n > 0) {
	          coord[i] = n % limits[i];
	          n = n/limits[i];
	          i++;
	       }
	       return coord;
	}

}
