package com.hill.planner.basic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.drools.planner.api.domain.entity.PlanningEntity;
import org.drools.planner.api.domain.solution.PlanningEntityCollectionProperty;
import org.drools.planner.core.score.buildin.hardandsoft.HardAndSoftScore;
import org.drools.planner.core.solution.Solution;


public class AirportSolution implements Solution<HardAndSoftScore> {

	private HardAndSoftScore score;
	
	private List<Plane> planPlanes = new ArrayList<Plane>();

	/**
	 * List of all planes to plan for
	 * 
	 * @param planPlanes
	 *            - planes that need TSATs calculating
	 * @param factPlanes
	 *            - planes that have exited the planning window, but may need to
	 *            be accounted for
	 */
	public AirportSolution(List<Plane> planPlanes) {
		this.planPlanes.addAll(planPlanes);
	}

	@PlanningEntityCollectionProperty
	public List<Plane> getPlanPlanes() {
		return planPlanes;
	}

	public void setPlanPlanes(List<Plane> planes) {
		this.planPlanes = planes;
	}
	
	
	
	
	@Override
	public HardAndSoftScore getScore() {
		return score;
	}

	@Override
	public void setScore(HardAndSoftScore score) {
		this.score = score;
	}

	@Override
	public Collection<? extends Object> getProblemFacts() {
		// TODO Add facts into Drools
		
		//Do not add problem entities - they are added automatically.
		
		/*
		 * Existing flights should be added here, as well as -
		 *  - Stand information
		 *  - Aircraft types
		 *  - etc
		 */
		
		return new ArrayList();
	}

	@Override
	public Solution<HardAndSoftScore> cloneSolution() {
		
		List<Plane> clonePlanes = new ArrayList<Plane>();
		
		for(Plane p : planPlanes)
		{
			clonePlanes.add(p.clonePlane());			
		}
		
		
		AirportSolution clone = new AirportSolution(clonePlanes);
		
		clone.setScore(this.getScore());
		
		return clone;
	}
	
	@Override
	public String toString() {
		String returnValue = "";
		
		returnValue += "TOBT \t TSAT \t Interval \n";
		
		for(Plane p :planPlanes)
		{
			returnValue += p.getTobt() +"\t"+p.getTsat()+"\t"+p.getTaxiInterval()+"\t\n";			
		}
		
		
		return returnValue;
	}

}
