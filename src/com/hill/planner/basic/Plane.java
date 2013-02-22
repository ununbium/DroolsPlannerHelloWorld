package com.hill.planner.basic;

import org.drools.planner.api.domain.entity.PlanningEntity;
import org.drools.planner.api.domain.variable.PlanningVariable;
import org.drools.planner.api.domain.variable.ValueRange;
import org.drools.planner.api.domain.variable.ValueRangeType;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.Interval;

/**
 * This annotation allows Planner to recognise this class as something that it
 * should care about. As you can see there is nearly no fuctionality implemented
 * herein.
 * 
 */
@PlanningEntity
public class Plane {
	// identity - allows us to make sure that we don't do silly things like
	// comparing an instance with itself.
	private String id;

	// planning
	private Instant tsat = null;
	private Duration pushback;
	private Instant tobt = null;

	public Plane(String id, Instant tobt, Duration pushback) {
		this.tobt = tobt;
		this.setTsat(tobt);
		this.setId(id);
		this.pushback = pushback;
	}

	/**
	 * Here we are telling Drools planner;
	 * 
	 * <ol>
	 * <li>where to get the solutions with the @valueRange- the
	 * "FROM_SOLUTION_PROPERTY" tells the planner to use the airports property
	 * tsatOptions. Planner will call "getTsatOptions()" on the Airport class</li>
	 * 
	 * <li>
	 * That this property (tsat) is a Planning variable - I.e. a value that it
	 * can change the value of. You can have as many of these as you like, but
	 * obviously constraining the possible number of solutions is highly
	 * advisable, so don't go crazy - pick planning variables carefully</li>
	 * </ol>
	 * 
	 * Other than that - it is a simple property. You can out the annotations
	 * below on either the getter or the setter.
	 * 
	 * @return
	 */
	@PlanningVariable
	@ValueRange(type = ValueRangeType.FROM_SOLUTION_PROPERTY, solutionProperty = "tsatOptions")
	public Instant getTsat() {
		return tsat;
	}

	public void setTsat(Instant tsat) {
		this.tsat = tsat;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Instant getTobt() {
		return tobt;
	}

	public Interval getTaxiInterval() {
		Interval i = new Interval(tsat, tsat.plus(pushback));
		return i;
	}

	@Override
	public int hashCode() {
		Integer s = super.hashCode();

		s += tobt.hashCode();
		s += pushback.hashCode();
		s += id.hashCode();

		return s;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Plane)) {
			return false;
		}

		Plane other = (Plane) obj;

		// if ( tsat.equals(other.tsat) &&
		// tobt.equals(other.tobt) &&
		// getTaxiInterval().equals(other.getTaxiInterval()))

		if (tobt.equals(other.tobt) && id.equals(other.id)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Deep clone this instance.
	 * 
	 * @return clone of this
	 */
	public Plane clonePlane() {

		Plane clone = new Plane(id, tobt, pushback);

		clone.setTsat(tsat);
		return clone;
	}

}
