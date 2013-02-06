package com.hill.planner.basic;

import org.drools.planner.core.heuristic.selector.common.decorator.SelectionFilter;
import org.drools.planner.core.heuristic.selector.move.generic.ChangeMove;
import org.drools.planner.core.score.director.ScoreDirector;
import org.joda.time.Instant;

/**
 * Only allows TSATs in the future
 * 
 * @author andrew
 *
 */
public class FutureTSATSwapMoveFilter implements SelectionFilter<Object> {

	@Override
	public boolean accept(ScoreDirector scoreDirector, Object selection) {
		System.out.println("filter called "+selection.getClass().getCanonicalName());
		return true;
//		Plane p = (Plane) selection.getEntity();
//		Instant toPlanningValue = (Instant) selection.getToPlanningValue();
//		
//		if(toPlanningValue.isBefore(p.getTobt()))
//		{
//			return false;
//		} else {
//			return true;
//		}
	}

}
