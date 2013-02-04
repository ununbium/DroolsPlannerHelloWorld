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
import org.drools.planner.core.solution.Solution;
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
		XmlSolverFactory solverFactory = new XmlSolverFactory();

		File f = new File(
				"/home/andrew/Documents/workspace-sts-3.1.0.RELEASE/Planner/rules/basic/solverConfiguration.xml");
		InputStream s = new FileInputStream(f);
		solverFactory.configure(s);

		Solver solver = solverFactory.buildSolver();
		
		AirportSolution planningProblem = generateProblem();
		
		solver.setPlanningProblem(planningProblem);
		
		solver.addEventListener(new SolverEventListener() {
			
			@Override
			public void bestSolutionChanged(BestSolutionChangedEvent event) {
				System.out.println("New Best!");
				
			}
		});

	    solver.solve();

	    AirportSolution bestSolution = (AirportSolution) solver.getBestSolution();
	    
	    System.out.println(bestSolution.toString());

	}
	
	public static AirportSolution generateProblem() throws ParseException {
		
		List<Plane> p = new ArrayList<Plane>();
		
		Plane plane = null;
		
		plane = new Plane(fmt.parseDateTime("2013/01/01 01:00").toInstant(), new Duration(100000*10));
		p.add(plane);
		Plane plane2 = new Plane(fmt.parseDateTime("2013/01/01 01:00").toInstant(), new Duration(100000*5));
		p.add(plane2);		
		plane = new Plane(fmt.parseDateTime("2013/01/01 01:00").toInstant(), new Duration(100000*3));
		p.add(plane);
		plane = new Plane(fmt.parseDateTime("2013/01/01 01:00").toInstant(), new Duration(100000*11));
		p.add(plane);
		plane = new Plane(fmt.parseDateTime("2013/01/01 01:00").toInstant(), new Duration(100000*9));
		p.add(plane);		
		plane = new Plane(fmt.parseDateTime("2013/01/01 01:00").toInstant(), new Duration(100000*9));
		p.add(plane);
		
		AirportSolution a = new AirportSolution(p);
		
		return a;
	}
}
