import java.util.Scanner;

public final class GameLoop {
	static final String CLS = "\u001B[2J\u001B[0;0f";
	Game runningGame;

	public GameLoop() {
		this.runningGame = null;
	}

	public void greet() {
	}

	public void promptQuit() {
		final String quitDialog = "Are you sure you want to quit? All progress will be lost (Press 'q' again to quit the program or 'Enter' continue)\n> ";
		
		final Scanner stdin = new Scanner(System.in);
		stdin.useDelimiter("");

		System.out.print(quitDialog);

		if (stdin.hasNext()) {
			final char nextChar = stdin.next().charAt(0);

			if (nextChar == 'q') {
				System.out.println("Quitting program");
				
				this.exit();
			}
		}
	}

	public int promptSize() {
		final String promptDialog = "Enter an integer board size >10 (Note: board sizes too large may not render properly)\n> ";
		final String errorInvalidType = "Error: %s is an invalid integer\n";
		final String errorSmallBoard = "Error: %d is too small of a board size\n";
		final String warningLargeBoard = "Warning: a board size of %d might not render properly\n";
		Scanner stdin = new Scanner(System.in);
		
		// System.out.print(promptDialog);

		boolean isSuccessfulSizeGiven = false;
		int successfulSize = -1;
		
		while (!isSuccessfulSizeGiven) {
			System.out.print(promptDialog);
			
			final boolean hasInteger = stdin.hasNextInt();
			
			if (hasInteger) {
				final int integerInput = stdin.nextInt();

				if (integerInput > 10) {
					isSuccessfulSizeGiven = true;
					successfulSize = integerInput;

					break;
				}

				else {
					System.out.printf(errorSmallBoard, integerInput);

					continue;
				}
			}

			if (!hasInteger) {
				final String next = stdin.next();

				if (next.length() == 1 && next.charAt(0) == 'q') {
					stdin = null;
					this.promptQuit();
					stdin = new Scanner(System.in);
					
					continue;
				}

				System.out.printf(errorInvalidType, next);
			}
		}

		if (successfulSize > 350) {
			System.out.printf(warningLargeBoard, successfulSize);
		}

		return successfulSize;
	}

	public char moveDirection() {
		final String moveDialog = "Enter the direction that you want to go towards\n\t(please enter 'w' to move north, 'a' to move west, 'd' to move east, and 's' to move south)\n> ";
		
		System.out.print(moveDialog);
		
		Scanner x = new Scanner(System.in);
		
		while (x.hasNext()) {
			String nextInput = x.next();

			if (nextInput.length() != 1) {
				System.out.printf("%s is not a valid move. Try again\n> ", nextInput);
				
				continue;
			}

			char charAt = nextInput.charAt(0);

			if (charAt == 'q') {
				return 'q';
			}
			if (charAt == 'w') {
				return 'w';
			}
			else if (charAt == 'a') {
				return 'a';
			}
			else if (charAt == 's') {
				return 's';
			}
			else if (charAt == 'd') {
				return 'd';
			}

			else {
				System.out.printf("%c is not a valid move. Try again\n> ", charAt);
			}
		}
		
		return ' ';
	}

	public void displayMenu() {
		GameLoop.clearScreen();
	}

	public void displayHelpMenu() {
		GameLoop.clearScreen();
	}
	
	public void startGame() {
		GameLoop.clearScreen();
		
		final int gameSize = (this.promptSize() / 2) * 2;
		
		this.runningGame = new Game(gameSize, gameSize);
		this.runningGame.initialize();
		
		clearScreen();

		while (runningGame.getIsRunning()) {
			System.out.println(this.runningGame);

			final char userMoveChar = this.moveDirection();

			if (userMoveChar == 'q') {
				promptQuit();
				clearScreen();
				continue;
			}
			
			final boolean wasValidMove = this.runningGame.makeMove(userMoveChar);

			if (!wasValidMove || userMoveChar == ' ') {
				System.out.printf("%c was not a valid move. Try again\n> ", userMoveChar);
				
				(new Scanner(System.in)).nextLine();
			}

			clearScreen();
		}

		final Game.PlayerIdentifier winner = this.runningGame.getWinner();

		System.out.printf("Congratulations, %s%s%s is the winner!\n", 
						  ((winner == Game.PlayerIdentifier.P1) ? Color.CRED : Color.CBLUE).getValue(),
						  winner,
						  Color.CEND.getValue());

		this.runningGame = null;
	}

	public static void clearScreen() {
		System.out.print(GameLoop.CLS);
	}

	public void exit() {
		System.exit(0);
	}
}