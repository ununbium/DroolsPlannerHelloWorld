package com.hill.planner.basic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.drools.planner.config.XmlSolverFactory;
import org.drools.planner.core.Solver;
import org.drools.planner.core.score.Score;
import org.drools.planner.core.score.director.ScoreDirector;
import org.drools.planner.core.score.director.ScoreDirectorFactory;

/**
 * This is a simple class that neatly wraps up the drools initialisation and
 * allows us to periodically output the score - you don't have to worry about
 * this if your looking for a Drools planner introduction. 
 */
public class DroolsRunner {
	private AirportSolution planningProblem = null;
	private Solver solver = null;
	private boolean solving = false;

	public DroolsRunner(AirportSolution planningProblem)
			throws FileNotFoundException {
		this.planningProblem = planningProblem;

		File f = new File("./rules/basic/solverConfiguration.xml");

		XmlSolverFactory solverFactory = new XmlSolverFactory();

		InputStream s = new FileInputStream(f);
		solverFactory.configure(s);

		solver = solverFactory.buildSolver();
	}

	public AirportSolution solve() {

		solver.setPlanningProblem(planningProblem);

		this.solving = true;

		runPrinterThread();

		solver.solve();

		this.solving = false;

		AirportSolution bestSolution = (AirportSolution) solver
				.getBestSolution();

		return bestSolution;

	}

	/**
	 * Simple thread to give some insight into the score changing over time -
	 * not necessary.
	 */
	private void runPrinterThread() {
		Thread scorePrinter = new Thread() {
			ScoreDirectorFactory scoreDirectorFactory = solver
					.getScoreDirectorFactory();

			ScoreDirector guiScoreDirector = scoreDirectorFactory
					.buildScoreDirector();

			@Override
			public void run() {
				while (solving) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}

					guiScoreDirector.setWorkingSolution(solver
							.getBestSolution());

					Score score = guiScoreDirector.calculateScore();
					System.out.println("score :" + score);
				}
			}
		};
		scorePrinter.start();
	}

}
