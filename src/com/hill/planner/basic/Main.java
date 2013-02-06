package com.hill.planner.basic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.drools.planner.config.XmlSolverFactory;
import org.drools.planner.core.Solver;
import org.drools.planner.core.event.BestSolutionChangedEvent;
import org.drools.planner.core.event.SolverEventListener;
import org.drools.planner.core.score.Score;
import org.drools.planner.core.score.director.ScoreDirector;
import org.drools.planner.core.score.director.ScoreDirectorFactory;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Super simple sequencer.
 * 
 * Each flight has a TOBT and has a lead time of n minutes (varies).
 * 
 * Hard constraints
 * 
 * No other TSAT can fall within that window.
 * 
 * Soft constraints
 * 
 * Minimise overall delay cost
 * 
 * @author andrew
 * 
 */
public class Main {
	
	//public static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd HH/mm");
	
	public static DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm");

	public static void main(String[] args) throws FileNotFoundException, ParseException {
		
		//one off initialisation (slow as it reads config from disk)
		DroolsRunner dRunner = new DroolsRunner(generateProblem());
		
		//track how long it took to solve the problem
		Instant startTime = new Instant();
		
		//solve it!
		AirportSolution bestSolution = dRunner.solve();
		
		Instant endTime = new Instant();
	    
		//print some infos on the solution
	    System.out.println(bestSolution.toString());
		System.out.println("Start: "+ startTime);
		System.out.println("End: "+ endTime);
		System.out.println("Took: "+ new Interval(startTime, endTime).toDurationMillis() +"ms");
	}
	
	
	/**
	 * Statically generate a starting situation.
	 * 
	 * @return
	 * @throws ParseException
	 */
	public static AirportSolution generateProblem() throws ParseException {
		
		List<Plane> p = new ArrayList<Plane>();
		
		Plane plane = null;
		
		long oneMinute = 60000;
		String id = "a";
//		plane = new Plane("a", fmt.parseDateTime("2013/01/01 01:00").toInstant(), new Duration(oneMinute*10));
//		p.add(plane);
//		plane = new Plane("b", fmt.parseDateTime("2013/01/01 01:01").toInstant(), new Duration(oneMinute*12));
//		p.add(plane);		
//		plane = new Plane("c", fmt.parseDateTime("2013/01/01 01:10").toInstant(), new Duration(oneMinute*2));
//		p.add(plane);
//		plane = new Plane("d", fmt.parseDateTime("2013/01/01 01:20").toInstant(), new Duration(oneMinute*13));
//		p.add(plane);
//		plane = new Plane("e", fmt.parseDateTime("2013/01/01 01:30").toInstant(), new Duration(oneMinute*3));
//		p.add(plane);		
//		plane = new Plane("f", fmt.parseDateTime("2013/01/01 01:30").toInstant(), new Duration(oneMinute*5));
//		p.add(plane);		
//		plane = new Plane("g", fmt.parseDateTime("2013/01/01 01:30").toInstant(), new Duration(oneMinute*6));
//		p.add(plane);		
//		plane = new Plane("h", fmt.parseDateTime("2013/01/01 01:30").toInstant(), new Duration(oneMinute*10));
//		p.add(plane);		
		plane = new Plane("i", fmt.parseDateTime("2013/01/01 01:30").toInstant(), new Duration(oneMinute*7));
		p.add(plane);		
		plane = new Plane("j", fmt.parseDateTime("2013/01/01 01:30").toInstant(), new Duration(oneMinute*4));
		p.add(plane);
		
		AirportSolution a = new AirportSolution(p);
		
		return a;
	}
}
