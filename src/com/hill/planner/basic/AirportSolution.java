package com.hill.planner.basic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.drools.planner.api.domain.solution.PlanningEntityCollectionProperty;
import org.drools.planner.core.score.buildin.hardandsoft.HardAndSoftScore;
import org.drools.planner.core.solution.Solution;
import org.joda.time.Duration;
import org.joda.time.Instant;

/**
 * AirportSolution represents a single possible solution to the planning
 * problem.
 */
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

	/**
	 * This annotation tells drools that this property contains the planning
	 * elements.
	 * 
	 * @return the planes at this airport
	 */
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

	/**
	 * The possible solutions come from here. Currently we are saying that every
	 * minute from the first planes TOBT to the last planes TOBT (plus one
	 * hour).
	 * 
	 * By reducing count of values here you will reduce the possible number of
	 * solutions. If you have a way of identifying bad values (for example you
	 * know that +1 hour is too much).
	 * 
	 * Be careful though - Planner can only work with what you give it here, be
	 * too strict and you may be preventing planner for coming up with the best
	 * solution.
	 * 
	 * @return the full list of possible TSATs that planner can try
	 */
	public List<Instant> getTsatOptions() {
		Instant earliest = planPlanes.get(0).getTobt();
		Instant latest = planPlanes.get(0).getTobt();
		List<Instant> result = new ArrayList<Instant>();

		for (Plane p : planPlanes) {
			if (p.getTobt().isBefore(earliest)) {
				earliest = p.getTobt();
			}

			if (p.getTobt().isAfter(latest)) {
				latest = p.getTobt();
			}
		}

		Instant candidate = earliest;
		result.add(candidate); // add first time as a candidate

		// add each minute from earliest to latest (should be latest hour)
		while (candidate.isBefore(latest.plus(new Duration(1000 * 60 * 60)))) {
			// increment by one minute
			candidate = candidate.plus(new Duration(1000 * 60));
			result.add(candidate);
		}

		return result;

	}

	@Override
	public Collection<? extends Object> getProblemFacts() {
		/*
		 * Currently not implementing facts, but you can pass static facts into
		 * the Drools environment here if you like - things like stand positions
		 * and taxi times for particular types of planes.
		 */
		return new ArrayList();
	}

	/**
	 * Deep clone this solution. Clones and hash codes must be done correctly if
	 * you want to avoid problems being reported from inside Drools (trust me,
	 * sometimes they are really hard to trace).
	 */
	@Override
	public Solution<HardAndSoftScore> cloneSolution() {

		List<Plane> clonePlanes = new ArrayList<Plane>();

		for (Plane p : planPlanes) {
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

		for (Plane p : planPlanes) {
			Long duration = new Duration(p.getTobt(), p.getTsat())
					.getStandardSeconds();

			returnValue += p.getId() + "\t" + p.getTobt() + "\t" + p.getTsat()
					+ "\t" + p.getTaxiInterval() + "\t" + duration + "\t\n";
		}

		return returnValue;
	}

}
