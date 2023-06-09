public class Player extends Entity {
	private int points = 0;
	private Game.PlayerIdentifier player;
	private Cordinate position;

	public Player(final Game.PlayerIdentifier assignedPlayer, final Cordinate position) {
		super();

		this.player = assignedPlayer;
		this.points = 1;
		this.position = position;
	}

	public Game.PlayerIdentifier getPlayerIdentifier() {
		return this.player;
	}

	public void setPoints(final int newPoints) {
		this.points = newPoints;
	}

	public int getPoints() {
		return this.points;
	}

	public Cordinate getPosition() {
		return this.position;
	}

	public void setPosition(final Cordinate newPosition) {
		this.position = newPosition;
	}

	public void applyBonus(final Bonus appliedBonus) {
		switch (appliedBonus.getBonusType().getIndex()) {
			case 1: {
				this.points += appliedBonus.getBonusValue();
				break;
			}
			case 2: {
				this.points -= appliedBonus.getBonusValue();
				break;
			}
			case 3: {
				this.points *= appliedBonus.getBonusValue();
				break;
			}
			case 4: {
				this.points /= appliedBonus.getBonusValue();
				break;
			}
		}
	}

	public Color[][] renderColors() {
		if (this.player == Game.PlayerIdentifier.P1) {
			return new Color[][] {
					{ Color.CRED, Color.CRED },
					{ Color.CRED, Color.CRED }
			};
		}

		if (this.player == Game.PlayerIdentifier.P2) {
			return new Color[][] {
					{ Color.CBLUE, Color.CBLUE },
					{ Color.CBLUE, Color.CBLUE }
			};
		}

		return null;
	}

	public char[][] renderChars() {
		final char blankSpaceChar = Game.BLANK_SPACE_CHAR.charAt(0);
		final char legs = '^';

		if (this.player == Game.PlayerIdentifier.P1) {
			return new char[][] {
					{ 'P', '1' },
					{ legs, legs }
			};
		}

		if (this.player == Game.PlayerIdentifier.P2) {
			return new char[][] {
					{ 'P', '2' },
					{ legs, legs }
			};
		}

		return null;
	}
}