import java.util.ArrayList;

public class Game {
	public static final String BLANK_SPACE_CHAR = ".";
	public enum PlayerIdentifier {
		P1("P1"), P2("P2"), Both("Both"), None("None");
		
		private final String playerName;

		private PlayerIdentifier(final String playerName) {
			this.playerName = playerName;
		}

		public String getPlayerName() {
			return this.playerName;
		}
	};

	private final Player player1;
	private final Player player2;

	private Endpoint player1Endpoint;
	private Endpoint player2Endpoint;

	private final int gridRows;
	private final int gridColumns;

	private boolean player1Finished;
	private boolean player2Finished;
	
	private boolean isRunning;
	private PlayerIdentifier turn;
	private final Entity[][] grid;
	private int moveCount;
	private boolean bonusesWereRemoved;
	private PlayerIdentifier winner;

	public class GameMove {
		private final PlayerIdentifier movePlayer; 
		private final int moveNumber;
		private final Cordinate moveStart;
		private final Cordinate moveEnd;
		private final Bonus moveBonusReceived;
		private final Cordinate.Direction moveDirection;

		public GameMove(final PlayerIdentifier movePlayer,
						final int moveNumber,
						final Cordinate moveStart,
						final Cordinate moveEnd,
						final Cordinate.Direction moveDirection,
						final Bonus moveBonusReceived) {
			this.movePlayer = movePlayer;
			this.moveNumber = moveNumber;
			this.moveStart = moveStart;
			this.moveEnd = moveEnd;
			this.moveBonusReceived = moveBonusReceived;
			this.moveDirection = moveDirection;
		}

		public PlayerIdentifier getPlayerIdentifier() {
			return this.movePlayer;
		}

		public Cordinate getStart() {
			return this.moveStart;
		}

		public Cordinate getEnd() {
			return this.moveEnd;
		}

		public Cordinate.Direction getMoveDirection() {
			return this.moveDirection;
		}

		public Bonus getBonusReceived() {
			return this.moveBonusReceived;
		}

		public int getMoveNumber() {
			return this.moveNumber;
		}

		public String toStringDescriptive(final boolean colored) {
			final String colorValue = (!colored) ? "" :
				((movePlayer == PlayerIdentifier.P1) ? Color.CRED : Color.CBLUE).getValue();

			return String.format(
				"%s%d. %s to %s (%s) %s%s",
				colorValue,
				this.moveNumber,
				this.moveStart,
				this.moveEnd,
				this.moveDirection.getOrdinal(),
				((this.moveBonusReceived == null) ? "" : this.moveBonusReceived.toString()),
				Color.CEND.getValue()
			);
		}

		@Override
		public boolean equals(final Object compared) {
			if (!(compared instanceof GameMove)) {
				return false;
			}

			final GameMove comparedMove = (GameMove) compared;

			return 
				this.getPlayerIdentifier() == comparedMove.getPlayerIdentifier() &&
				this.getMoveNumber() == comparedMove.getMoveNumber() &&
				this.getStart().equals(comparedMove.getStart()) &&
				this.getEnd().equals(comparedMove.getEnd()) &&
				this.getMoveDirection() == comparedMove.getMoveDirection();
		}

		@Override
		public String toString() {
			return 
				this.getMoveDirection().getKey() +
				((this.moveBonusReceived == null) ? "" : (": " + this.moveBonusReceived.toString()));
		}
	}
	
	private ArrayList<GameMove> moveHistory;

	Game(final int gridRows, final int gridColumns) {
		this.isRunning = true;
		this.turn = PlayerIdentifier.P1;
		this.moveCount = 0;
		this.grid = new Entity[gridRows][gridColumns];
		this.gridRows = gridRows;
		this.gridColumns = gridColumns;
		this.player1 = new Player(PlayerIdentifier.P1, new Cordinate(0, 0));
		this.player2 = new Player(PlayerIdentifier.P2, new Cordinate(this.gridColumns - 1, this.gridRows - 1));
		this.moveHistory = new ArrayList<GameMove>();
		this.bonusesWereRemoved = false;
		this.winner = PlayerIdentifier.None;
	}

	public void initialize() {
		this.generateMaze();
		this.addBonuses();
		this.addEndpoints();
		this.addPlayers();
	}
	
	public void generateMaze() {
		final MazeGenerator gameMaze = new MazeGenerator(this.gridRows, this.gridColumns);
		gameMaze.generate();
		
		MazeGenerator.Cell[][] mazeCellGrid = gameMaze.getMaze();

		for (int rowIndex = 0; rowIndex < this.gridRows; rowIndex++) {
			for (int columnIndex = 0; columnIndex < this.gridColumns; columnIndex++) {
				this.grid[rowIndex][columnIndex] = (mazeCellGrid[rowIndex][columnIndex] == MazeGenerator.Cell.Block) ? new Wall() : null;
			}
		}
	}

	public void addBonuses() {
		for (final Entity[] row : this.grid) {
			for (int rowIndex = 0; rowIndex < row.length; rowIndex++) {
				if (MazeGenerator.discreteRandInclusive(0, 4) == 0) {
					row[rowIndex] = new Bonus();
				}
			}
		}

		this.bonusesWereRemoved = false;
	}
	
	public void addEndpoints() {
		this.player1Endpoint = new Endpoint(PlayerIdentifier.P1, new Cordinate(0, 0));
		this.player2Endpoint = new Endpoint(PlayerIdentifier.P2, new Cordinate(this.gridRows - 1, this.gridColumns - 1));

		this.drawEndpoints();
	}
	
	public void drawEndpoints() {
		final Cordinate endpoint1Position = this.player1Endpoint.getPosition();
		final Cordinate endpoint2Position = this.player2Endpoint.getPosition();
			
		if (this.at(endpoint1Position) == null) {
			this.replaceAt(endpoint1Position, this.player1Endpoint);
		}
		
		if (this.at(endpoint2Position) == null) {
			this.replaceAt(endpoint2Position, this.player2Endpoint);
		}
	}

	public void addPlayers() {
		final Cordinate player1Position = this.player1.getPosition();
		final Cordinate player2Position = this.player2.getPosition();

		this.grid[player1Position.getY()][player1Position.getX()] = this.player1;
		this.grid[player2Position.getY()][player2Position.getX()] = this.player2;
	}

	public PlayerIdentifier getTurn() {
		return this.turn;
	}

	public boolean getIsRunning() {
		return this.isRunning;
	}

	public int getMoveCount() {
		return this.moveCount;
	}

	public PlayerIdentifier getWinner() {
		return this.winner;
	}

	public int getPoints(final PlayerIdentifier player) {
		if (player == PlayerIdentifier.P1) {
			return this.player1.getPoints();
		}

		return this.player2.getPoints();
	}

	public Entity at(final Cordinate returnedPosition) {
		return this.grid[returnedPosition.getY()][returnedPosition.getX()];
	}
	
	public void replaceAt(final Cordinate position, final Entity newEntity) {
		this.grid[position.getY()][position.getX()] = newEntity;
	}

	public boolean inBounds(final Cordinate checkedPosition) {
		final int xCord = checkedPosition.getX();
		final int yCord = checkedPosition.getY();
		
		return
			0 <= xCord && xCord < this.gridColumns &&
			0 <= yCord && yCord < this.gridRows;
			
	}

	public void removeBonuses() {
		if (this.bonusesWereRemoved) {
			return;
		}
		
		for (Entity[] row : this.grid) {
			for (int index = 0; index < row.length; index++) {
				if (row[index] instanceof Bonus) {
					row[index] = null;
				}
			}
		}

		this.bonusesWereRemoved = true;
	}

	public ArrayList<GameMove> viableMoves() {
		final Player playerMoving = (this.turn == PlayerIdentifier.P1) ? this.player1 : this.player2;
		final Cordinate playerStartingPosition = playerMoving.getPosition();

		final ArrayList<GameMove> checkedViableMoves = new ArrayList<GameMove>();
		
		for (final Cordinate.Direction direction : Cordinate.Direction.values()) { // Check all directions
			final Cordinate newPosition = playerStartingPosition.inDirection(direction, 1);

			if (!this.inBounds(newPosition)) {
				continue;
			}
			
			final Entity atNewPosition = this.at(newPosition);
			final boolean isBonusAtNewPosition = atNewPosition instanceof Bonus;
			final boolean isEndpointAtNewPosition = atNewPosition instanceof Endpoint;
			
			if (atNewPosition == null | isBonusAtNewPosition | isEndpointAtNewPosition) {
				checkedViableMoves.add(
					new GameMove(
						playerMoving.getPlayerIdentifier(),
						this.moveCount + 1, // Next move
						playerStartingPosition,
						newPosition,
						direction,
						(isBonusAtNewPosition) ? (Bonus) atNewPosition : null
					)
				);
			}
		}

		return checkedViableMoves;
	}

	public boolean makeMove(final char moveCode) {
		final ArrayList<GameMove> possibleMoves = this.viableMoves();

		for (final GameMove maybeMove : possibleMoves) {
			if (maybeMove.getMoveDirection().getKey() == moveCode) {
				return this.makeMove(maybeMove);
			}
		}
		
		return false;
	}
	
	public boolean makeMove(final GameMove moveMade) {
		if (moveMade == null) {
			return false;
		}
		
		final Cordinate startingSquare = moveMade.getStart();
		final Cordinate endSquare = moveMade.getEnd();
		final Entity atEndSquare = this.at(endSquare);
		final boolean isViableMove = this.viableMoves().contains(moveMade);
		
		if (!isViableMove) {
			return false;
		}

		final Player playerMoved = (this.turn == PlayerIdentifier.P1) ? player1 : player2;
		
		if (atEndSquare instanceof Bonus) {
			playerMoved.applyBonus((Bonus) atEndSquare);
		}

		this.replaceAt(endSquare, playerMoved);
		this.replaceAt(startingSquare, null);

		playerMoved.setPosition(endSquare);

		if (player1.getPosition().equals(player2Endpoint.getPosition())) {
			this.removeBonuses();
			player1Finished = true;
		}
		
		if (player2.getPosition().equals(player1Endpoint.getPosition())) {
			this.removeBonuses();
			player2Finished = true;
		}

		if (!this.player1Finished && !this.player2Finished) {
			this.turn = ((this.turn == PlayerIdentifier.P1) ?
						 PlayerIdentifier.P2 : PlayerIdentifier.P1);
		}

		if (this.player1Finished && !this.player2Finished) {
			// this.removeBonuses();
			
			this.turn = PlayerIdentifier.P2;
			this.player2.setPoints(this.player2.getPoints() - 1);

			if (this.player1.getPoints() > this.player2.getPoints()) {
				this.isRunning = false;
				this.winner = PlayerIdentifier.P1;
			}
			
			if (this.player1.getPoints() < this.player2.getPoints()) {
				this.isRunning = false;
				this.winner = PlayerIdentifier.P2;
			}
		}
		
		if (this.player2Finished && !this.player1Finished) {
			// this.removeBonuses();
			
			this.turn = PlayerIdentifier.P1;
			this.player1.setPoints(this.player1.getPoints() - 1);
			
			if (this.player2.getPoints() > this.player1.getPoints()) {
				this.isRunning = false;
				this.winner = PlayerIdentifier.P2;
			}
		}

		if (this.player1Finished && this.player2Finished) {
			this.isRunning = false;
		}

		this.drawEndpoints();
		this.moveHistory.add(moveMade);
		this.moveCount += 1;
		
		return true;
	}
	
	public String[] renderLines() {
		final int entitySize = Entity.DIMENSIONS;
		final String colorReset = Color.CEND.getValue();
		
		final String blankSpacesChar = ".";

		// *3 accounts for colors and oclor resets

		/** For each entity: 
		 * Char | Color | Reset | Char | Color | Reset
		 * Char | Color | Reset | Char | Color | Reset
		 */

		String[] lines = new String[entitySize * this.gridRows];

		for (int lineIndex = 0; lineIndex < lines.length; lineIndex++) {
			lines[lineIndex] = "";
		}

		for (int rowIndex = 0; rowIndex < gridRows; rowIndex++) {
			for (final Entity renderedEntity : this.grid[rowIndex]) {
				if (renderedEntity == null) {
					lines[rowIndex * 2] += BLANK_SPACE_CHAR + BLANK_SPACE_CHAR;
					lines[rowIndex * 2 + 1] += BLANK_SPACE_CHAR + BLANK_SPACE_CHAR;
				}

				else {
					final Color[][] colors = renderedEntity.renderColors();
					final char[][] chars = renderedEntity.renderChars();
	
					for (int rowOfEntity = 0; rowOfEntity < 2; rowOfEntity++) {
						if (colors != null) {
							lines[rowIndex * 2 + rowOfEntity] += colors[rowOfEntity][0].getValue();
							lines[rowIndex * 2 + rowOfEntity] += chars[rowOfEntity][0];
							lines[rowIndex * 2 + rowOfEntity] += colorReset;
							lines[rowIndex * 2 + rowOfEntity] += colors[rowOfEntity][1].getValue();
							lines[rowIndex * 2 + rowOfEntity] += chars[rowOfEntity][1];
							lines[rowIndex * 2 + rowOfEntity] += colorReset;
						}

						if (chars != null && colors == null) {
							lines[rowIndex * 2 + rowOfEntity] += chars[rowOfEntity][0];
							lines[rowIndex * 2 + rowOfEntity] += chars[rowOfEntity][1];
							lines[rowIndex * 2 + rowOfEntity] += colorReset;
						}
					}
				}
			}
		}

		return lines;
	}

	public String renderString(final char borderChar, final int borderWidth) {
		String renderedString = "";

		final int width = (this.gridColumns + borderWidth) * 2;

		for (int iteration = 0; iteration < borderWidth; iteration++) { // Header padding
			for (int index = 0; index < width; index++) {
				renderedString += borderChar;
			}

			renderedString += '\n';
		}

		final String[] renderedLines = this.renderLines();

		for (final String row : renderedLines) { // Content
			for (int index = 0; index < borderWidth; index++) {
				renderedString += borderChar;
			}

			renderedString += row;
			
			for (int index = 0; index < borderWidth; index++) {
				renderedString += borderChar;
			}

			renderedString += '\n';
		}

		for (int iteration = 0; iteration < borderWidth; iteration++) { // Lower padding
			for (int index = 0; index < width; index++) {
				renderedString += borderChar;
			}

			renderedString += '\n';
		}

		return renderedString;
	}

	public String renderScores() {
		return
			"Scores: " +
			Color.CRED.getValue() + "P1 - " + this.player1.getPoints() + " " +
			Color.CBLUE.getValue() + "P2 - " + this.player2.getPoints() + Color.CEND.getValue();
	}

	public String renderTurn() {
		return
			"Turn: " + ((this.turn == PlayerIdentifier.P1) ? Color.CRED : Color.CBLUE).getValue() +
			this.turn + Color.CEND.getValue();
	}
	
	public String renderViableMoves() {
		return null;
	}

	public String renderMoveHistory(final int maxHistoryLength) {
		final int length = Math.min(maxHistoryLength, this.moveHistory.size());
		
		GameMove[] lastFewMoves = new GameMove[length];
		
		for (int lastFewMovesIndexer = length - 1; lastFewMovesIndexer >= 0; lastFewMovesIndexer--) {
			lastFewMoves[lastFewMovesIndexer] = this.moveHistory.get(
				this.moveHistory.size() - (length - lastFewMovesIndexer));
		}

		String renderString = "Move History:\n";
		
		for (final GameMove renderedMove : lastFewMoves) {
			renderString += renderedMove.toStringDescriptive(true);
			renderString += Color.CEND.getValue();
			renderString += '\n';
		}
		
		return renderString;
	}

	@Override
	public String toString() {
		return
			this.renderScores() + "\n" +
			this.renderTurn() + "\n" +
			this.renderString('*', 1) +
			this.renderMoveHistory(4);
	}
}