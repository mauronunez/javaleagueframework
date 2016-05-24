package org.javahispano.javaleague.tactic.bch;

import java.util.ArrayList;
import java.util.List;

import org.javahispano.javacup.model.command.Command;
import org.javahispano.javacup.model.command.CommandHitBall;
import org.javahispano.javacup.model.command.CommandMoveTo;
import org.javahispano.javacup.model.engine.GameSituations;
import org.javahispano.javacup.model.util.Constants;
import org.javahispano.javacup.model.util.Position;

public class Agente {
	
	

	private boolean initialized = false;
	private int rivalGoalKeeper;

	Position alineacion[] = new Position[] { new Position(0.2595419847328244, -50.41044776119403),
			new Position(-19.46564885496183, -31.6044776119403), new Position(0.2595419847328244, -31.082089552238806),
			new Position(19.984732824427482, -31.6044776119403), new Position(7.526717557251908, -11.753731343283583),
			new Position(-9, -12), new Position(-25, -2.3507462686567164), new Position(25, -3), new Position(-10, 30),
			new Position(-0.8, 8), new Position(10, 30) };
	
	Position zona[] = new Position[] { new Position(0.2595419847328244, -50.41044776119403),
			new Position(-19.46564885496183, -30), new Position(0.2595419847328244, 0),
			new Position(19.984732824427482, -30), new Position(8, 20),
			new Position(-8, 20), new Position(-25, 30), new Position(25, 30), new Position(-10, 50),
			new Position(-0.8, 40), new Position(10, 50) };
	

	Position arco = Constants.centroArcoSup;
	Position arcoleft = new Position(-Constants.LARGO_ARCO / 2, Constants.LARGO_CAMPO_JUEGO / 2);
	Position arcoright = new Position(Constants.LARGO_ARCO / 2, Constants.LARGO_CAMPO_JUEGO / 2);
	Position der = new Position(Constants.LARGO_AREA_CHICA / 2,
			Constants.LARGO_CAMPO_JUEGO / 2 - Constants.LARGO_AREA_GRANDE);
	Position izq = new Position(-Constants.LARGO_AREA_CHICA / 2,
			Constants.LARGO_CAMPO_JUEGO / 2 - Constants.LARGO_AREA_GRANDE);

	Position keep = Constants.centroArcoInf;

	public List<Command> run(GameSituations sp) {

		if (!initialized) {
			initialize(sp);
		}

		List<Command> commands = new ArrayList<Command>();

		analisis(sp);

		for (int i = 0; i < 11; i++) {
			Command c = individual(sp, i);
			if (c != null) {
				System.out.println(sp.iteration() + "[" + i + "]:" + c);
				commands.add(c);
			}
		}

		return commands;
	}

	private void initialize(GameSituations sp) {

		for (int i = 0; i < 11; i++) {
			if (sp.rivalPlayersDetail()[i].isGoalKeeper()) {
				rivalGoalKeeper = i;
			}
		}
		initialized = true;
	}

	private void analisis(GameSituations sp) {

	}

	private void analisis(GameSituations sp, int i) {

		sp.getMyPlayerAceleration(i);
		sp.getMyPlayerEnergy(i);
		sp.getMyPlayerError(i);
		sp.getMyPlayerPower(i);
		sp.getMyPlayerSpeed(i);
		sp.getMyPlayerEnergy(i);

		sp.rivalPlayers();
		sp.rivalCanKick();
		sp.rivalIterationsToKick();
		sp.iterationsToKick();

		sp.ballPosition();

		sp.canKick();
		sp.rivalCanKick();

		sp.getRivalEnergy(i);

		sp.rivalPlayersDetail();
		sp.myPlayersDetail();

		// boolean found = false;
		// while(!found){
		// double[] pos=sp.getTrajectory(0);
		// sp.distanceTotal(playerIndex, iter)
		// sp.distanceIter(playerIndex, iter, isSprint);
		// }

	}

	private double evaluate(GameSituations sp, Position from, Position to) {
		Position[] pr = sp.rivalPlayers();
		double dangerCandidate = 0;
		for (int ll = 0; ll < 11; ll++) {
			dangerCandidate += pr[ll].distance(to);
		}
		dangerCandidate -= to.distance(arco) / 2;
		dangerCandidate += to.distance(keep);
		return dangerCandidate;
	}
	
	private static int KICK_LOWER=20;
	private static int KICK_HIGH=30;
	private static double KICK_POWER=0.7d;
	
	

	private Command individual(GameSituations sp, int i) {

		Command cmd = null;
		boolean sprint = false;
		Position p = null;
		Position[] pr = null;

		p = sp.myPlayers()[i];
		pr = sp.rivalPlayers();

		Position rivalGK = pr[rivalGoalKeeper];

		int[] cankick = sp.canKick();
		for (int j = 0; j < cankick.length; j++) {
			if (cankick[j] == i) {
				Position dest = arco;
				double power = 0.8;
				boolean high = false;

				if (p.distance(arco) > KICK_LOWER && p.distance(arco) < KICK_HIGH) {
					dest = p.getX() < 0 ? izq : der;
					power = KICK_POWER;
				} else if (p.distance(arco) > 50) {
					Position near = null;
					double bestEvaluation = 0;
					for (int l = 0; l < 11; l++) {
						if (l == j){
							continue;
						}
						Position candidate = sp.myPlayers()[l].movePosition(0, 10);
						if (candidate.getY() < p.getY())
							continue;
						double evaluation = evaluate(sp, p, candidate);
						if (near == null || evaluation > bestEvaluation) {
							near = candidate;
							bestEvaluation = evaluation;
						}
					}
					if (near == null) {
						near = arco;
					}
					if (near.distance(p) > 50) {
						dest = p.getX() < 0 ? izq : der;
						power = 1;
						high = true;
					} else {
						dest = near;
						power = p.distance(near) / p.distance(arco);
						power = power < 0.7 ? 0.7 : power;
						high = near.distance(p) > 25;
					}

				} else {
					if (rivalGK.getX() < 0) {
						dest = p.getX() < 0 ? izq : arcoright;
					} else {
						dest = p.getX() < 0 ? arcoleft : der;
					}
					power = 1;
				}

				cmd = new CommandHitBall(i, dest, power, high);
			}
		}

		Position b = sp.ballPosition();
		Position origen = zona[i];

		if (cmd == null) {
			if (i == 0) {
				if (b.distance(p) < 20 && origen.distance(p) < 10) {
					cmd = new CommandMoveTo(i, b, sprint);
				} else if (origen.distance(p) > 10) {
					cmd = new CommandMoveTo(i, origen, sprint);
				}
			} else {
				if (i == 8 || i == 10) {
					if (b.distance(keep) < 50) {
						if (i == 8) {
							cmd = new CommandMoveTo(i, izq, true);

						} else {
							cmd = new CommandMoveTo(i, der, true);

						}

					} else {
						cmd = new CommandMoveTo(i, b, false);
					}

				} else {
					if (sp.isStarts() || (b.distance(p) < 20 && origen.distance(p) < 10)) {
						cmd = new CommandMoveTo(i, b, true);
					} else if (origen.distance(p) > 5) {
						cmd = new CommandMoveTo(i, origen, true);
					}
				}

			}

		}

		return cmd;
	}

	public Position[] getAlineacion() {
		return alineacion;
	}

}
