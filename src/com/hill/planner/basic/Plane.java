package com.hill.planner.basic;

import java.util.ArrayList;
import java.util.List;

import org.drools.planner.api.domain.entity.PlanningEntity;
import org.drools.planner.api.domain.variable.PlanningVariable;
import org.drools.planner.api.domain.variable.ValueRange;
import org.drools.planner.api.domain.variable.ValueRangeType;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.Interval;

@PlanningEntity
public class Plane {

	private static final Duration TIME_INCREMENT = new Duration(60000*1);
	private static final int MAX_INCREMENTS = 60; // one hour maximum

	// identity
	private Instant tobt = null;

	// planning
	private Instant tsat = null;
	private Duration pushback;
	private String id;

	public Plane(String id, Instant tobt, Duration pushback) {
		this.setTobt(tobt);
		this.setTsat(tobt);	
		this.setId(id);
		this.pushback = pushback;
	}

	@PlanningVariable
	@ValueRange(type = ValueRangeType.FROM_PLANNING_ENTITY_PROPERTY, planningEntityProperty = "tsatOptions")
	public Instant getTsat() {
		return tsat;
	}

	public void setTsat(Instant tsat) {
		this.tsat = tsat;
	}

	/**
	 * List all considerable instants
	 */
	public List<Instant> getTsatOptions() {
		List<Instant> availableTimes = new ArrayList<Instant>();

		for (int i = 0; i < MAX_INCREMENTS; i++) {
			Instant candidateTime = getTobt().plus(
					TIME_INCREMENT.getMillis() * i);

			availableTimes.add(candidateTime);
			//System.out.println("available: "+ candidateTime + "from x :"+TIME_INCREMENT.getMillis()+ " times y:"+MAX_INCREMENTS);
		}
		return availableTimes;
	}

	public Plane clonePlane() {

		Plane clone = new Plane(id, tobt, pushback);

		clone.setTsat(tsat);
		return clone;
	}
	
	

	public Instant getTobt() {
		return tobt;
	}

	public void setTobt(Instant tobt) {
		this.tobt = tobt;
	}
	
	public Interval getTaxiInterval()
	{
		Interval i = new Interval(tsat, tsat.plus(pushback));
		
//		System.out.println("got taxi interval " + i);
		return i;
	}
	
	@Override
	public int hashCode() {
		Integer s = super.hashCode();
		
		s += tobt.hashCode();
		s += pushback.hashCode();
		
		return s;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Plane))
		{
			return false;
		}
		
		Plane other = (Plane) obj;
		
//		if ( tsat.equals(other.tsat) &&
//				tobt.equals(other.tobt) && 
//				getTaxiInterval().equals(other.getTaxiInterval()))
		
		if ( tobt.equals(other.tobt) )
		{
			return true;
		} else {
			return false;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
