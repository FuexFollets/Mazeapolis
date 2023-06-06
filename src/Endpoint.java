public class Endpoint extends Entity {
	private final Game.PlayerIdentifier player;
	private final Cordinate position;

	public Endpoint(final Game.PlayerIdentifier assignedPlayer, final Cordinate position) {
		this.player = assignedPlayer;
		this.position = position;
	}

	public Cordinate getPosition() {
		return this.position;
	}

	public Color[][] renderColors() {
		return null;
	}

	public char[][] renderChars() {
		if (player == Game.PlayerIdentifier.P1) {
			return new char[][] {
					{ '1', '1' },
					{ '1', '1' }
			};
		}

		if (player == Game.PlayerIdentifier.P2) {
			return new char[][] {
					{ '2', '2' },
					{ '2', '2' }
			};
		}

		return null;
	}
}