package org.javahispano.javaleague.tactic.bch;

import java.util.List;

import org.javahispano.javacup.model.PlayerDetail;
import org.javahispano.javacup.model.Tactic;
import org.javahispano.javacup.model.TacticDetail;
import org.javahispano.javacup.model.command.Command;
import org.javahispano.javacup.model.engine.GameSituations;
import org.javahispano.javacup.model.util.Color;
import org.javahispano.javacup.model.util.Position;
import org.javahispano.javacup.render.EstiloUniforme;

public class BCHTactic implements Tactic {

	Agente agente = new Agente();

	@Override
	public List<Command> execute(GameSituations sp) throws Exception {
		// TODO Auto-generated method stub
		return agente.run(sp);
	}

	@Override
	public TacticDetail getDetail() throws Exception {
		return new TacticDetail() {

			@Override
			public String getTacticName() {
				return "BCH";
			}

			@Override
			public String getCountry() {
				return "Chile";
			}

			@Override
			public String getCoach() {
				return "BCH";
			}

			@Override
			public Color getShirtColor() {
				return new Color(255, 0, 0);
			}

			@Override
			public Color getShortsColor() {
				return new Color(255, 0, 0);
			}

			@Override
			public Color getShirtLineColor() {
				return new Color(255, 0, 0);
			}

			@Override
			public Color getSocksColor() {
				return new Color(255, 0, 0);
			}

			@Override
			public Color getGoalKeeper() {
				return new Color(255, 0, 0);
			}

			@Override
			public EstiloUniforme getStyle() {
				return EstiloUniforme.LINEAS_VERTICALES;
			}

			@Override
			public Color getShirtColor2() {
				return new Color(255, 0, 0);
			}

			@Override
			public Color getShortsColor2() {
				return new Color(255, 0, 0);
			}

			@Override
			public Color getShirtLineColor2() {
				return new Color(255, 0, 0);
			}

			@Override
			public Color getSocksColor2() {
				return new Color(255, 0, 0);
			}

			@Override
			public Color getGoalKeeper2() {
				return new Color(255, 0, 0);
			}

			@Override
			public EstiloUniforme getStyle2() {
				return EstiloUniforme.FRANJA_DIAGONAL;
			}

			class JugadorImpl implements PlayerDetail {
				String nombre;
				int numero;
				Color piel, pelo;
				double velocidad, remate, precision;
				boolean portero;
				Position posicion;

				public JugadorImpl(String nombre, int numero, Color piel, Color pelo, double velocidad, double remate,
						double precision, boolean portero) {
					this.nombre = nombre;
					this.numero = numero;
					this.piel = piel;
					this.pelo = pelo;
					this.velocidad = velocidad;
					this.remate = remate;
					this.precision = precision;
					this.portero = portero;
				}

				public String getPlayerName() {
					return nombre;
				}

				public Color getSkinColor() {
					return piel;
				}

				public Color getHairColor() {
					return pelo;
				}

				public int getNumber() {
					return numero;
				}

				public boolean isGoalKeeper() {
					return portero;
				}

				public double getSpeed() {
					return velocidad;
				}

				public double getPower() {
					return remate;
				}

				public double getPrecision() {
					return precision;
				}

			}

			public PlayerDetail[] getPlayers() {
				return new PlayerDetail[] {
						new JugadorImpl("Calamardo", 1, new Color(255, 200, 150), new Color(255, 200, 150), 1.0d, 1.0d,
								0.67d, true),
						new JugadorImpl("Bob", 2, new Color(255, 102, 102), new Color(255, 200, 150), 1.0d, 1.0d, 0.5d,
								false),
						new JugadorImpl("Patricio", 3, new Color(255, 200, 150), new Color(255, 200, 150), 1.0d, 1.0d,
								0.5d, false),
						new JugadorImpl("Billy", 4, new Color(255, 200, 150), new Color(255, 200, 150), 1.0d, 1.0d,
								0.56d, false),
						new JugadorImpl("Scooby", 5, new Color(255, 200, 150), new Color(255, 200, 150), 0.71d, 0.7d,
								0.49d, false),
						new JugadorImpl("Calimero", 6, new Color(255, 200, 150), new Color(255, 200, 150), 0.67d, 1.0d,
								0.69d, false),
						new JugadorImpl("Burt", 7, new Color(255, 200, 150), new Color(255, 200, 150), 0.67d, 1.0d,
								0.69d, false),
						new JugadorImpl("Hommer", 8, new Color(255, 200, 150), new Color(255, 200, 150), 0.75d, 1.0d,
								0.64d, false),
						new JugadorImpl("Lisa", 9, new Color(255, 200, 150), new Color(255, 200, 150), 0.87d, 1.0d,
								0.79d, false),
						new JugadorImpl("Mandy", 10, new Color(255, 200, 150), new Color(255, 200, 150), 0.85d, 1.0d,
								0.75d, false),
						new JugadorImpl("Berni", 11, new Color(255, 200, 150), new Color(255, 200, 150), 1.0d, 1.0d,
								1.0d, false) };
			}

		};
	}

	@Override
	public Position[] getStartPositions(GameSituations sp) throws Exception {
		return agente.getAlineacion();
	}

	@Override
	public Position[] getNoStartPositions(GameSituations sp) throws Exception {
		return agente.getAlineacion();
	}

}
