package com.hill.planner.basic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.drools.planner.api.domain.solution.PlanningEntityCollectionProperty;
import org.drools.planner.core.score.buildin.hardandsoft.HardAndSoftScore;
import org.drools.planner.core.solution.Solution;
import org.joda.time.Duration;
import org.joda.time.Instant;


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
	
	public List<Instant> getTsatOptions()
	{
		Instant earliest = planPlanes.get(0).getTobt();
		Instant latest = planPlanes.get(0).getTobt();
		List<Instant> result = new ArrayList<Instant>();
		
		for(Plane p : planPlanes)
		{
			if(p.getTobt().isBefore(earliest))
			{
				earliest = p.getTobt();
			}
			
			if(p.getTobt().isAfter(latest))
			{
				latest = p.getTobt();
			}
		}		
		
		Instant candidate = earliest;
		result.add(candidate); // add first time as a candidate
		
		//add each minute from earliest to latest
		while(candidate.isBefore(latest.plus(new Duration(1000*60*60)))) //should be latest + hour
		{
			candidate = candidate.plus(new Duration(1000*60)); //last plus one minute
			result.add(candidate);
		}	
		
		return result;
		
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
		
		returnValue += "ID \t TOBT \t TSAT \t Interval \t Offblock delay \n";
		
		for(Plane p :planPlanes)
		{
			Long duration = new Duration(p.getTobt(), p.getTsat()).getStandardSeconds();
			
			returnValue += p.getId()+"\t"+p.getTobt() +"\t"+p.getTsat()+"\t"+p.getTaxiInterval()+"\t"+duration+"\t\n";			
		}
		
		
		return returnValue;
	}

}
