import java.util.ArrayList;
import java.util.Scanner;
import java.lang.NumberFormatException;

public final class GameLoop {
	static final String CLS = "\u001B[2J\u001B[0;0f";
	private Game runningGame;

	public GameLoop() {
		this.runningGame = null;
	}

	public void greet() {
		clearScreen();
		
		final String greetDialog = 
			"""
			Welcome to theGame!
			
				> h - help
				> q - quit
				> s - start a new game
			
			""";
		
		System.out.print(greetDialog);
		System.out.print("> ");

		final Scanner stdin = new Scanner(System.in);

		boolean gameStarted = false;

		while (!gameStarted) {
			final String input = stdin.next();
			
			if (input.length() != 1) {
				System.out.printf("Error, %s is not a valid input\n> ", input);
				
				continue;
			}

			if (input.charAt(0) == 'q') {
				promptQuit();
				clearScreen();
				System.out.print(greetDialog);
				System.out.print("> ");
				continue;
			}
			
			if (input.charAt(0) == 'h') {
				clearScreen();
				showHelp();
				clearScreen();
				System.out.print(greetDialog);
				System.out.print("> ");
				continue;
			}
			
			if (input.charAt(0) == 's') {
				clearScreen();
				this.startGame();
				clearScreen();
				System.out.print(greetDialog);
				System.out.print("> ");
				continue;
			}
		}
	}

	public static void showHelp() {
		clearScreen();
		System.out.print(HelpTexts.helpDialog);
		
		final Scanner stdin = new Scanner(System.in);
		stdin.useDelimiter("");

		stdin.next();
	}
	
	public static void promptQuit(final Scanner stdin) {
		final String quitDialog = "Are you sure you want to quit? All progress will be lost (Press 'q' again to quit the program or 'Enter' continue)\n> ";
		
		stdin.useDelimiter("");

		System.out.print(quitDialog);

		if (stdin.hasNext()) {
			final char nextChar = stdin.next().charAt(0);

			if (nextChar == 'q') {
				System.out.println("Quitting program");
				
				exit();
			}
		}
	}

	public static void promptQuit() {
		promptQuit(new Scanner(System.in));
	}

	public Cordinate promptSize() {
		final String promptDialog = "Enter an integer board size >10 for the rows and columns separated by a space (Note: board sizes too large may not render properly)\n> ";
		final String errorInvalidType = "Error: %s is an invalid integer\n";
		final String errorSmallBoard = "Error: %d is too small of a board size\n";
		final String warningLargeBoard = "Warning: a board size of %d might not render properly\n";
		
		Scanner stdin = new Scanner(System.in);

		boolean isSuccessfulSizeGiven = false;
		Cordinate successfulSize = null;
		
		while (!isSuccessfulSizeGiven) {
			System.out.print(promptDialog);

			final String input1 = stdin.next();
			
			if (input1.length() == 1 && input1.charAt(0) == 'q') {
				clearScreen();
				promptQuit();

				clearScreen();
				
				continue;
			}
			
			if (input1.length() == 1 && input1.charAt(0) == 'h') {
				clearScreen();
				showHelp();

				clearScreen();
				
				continue;
			}
			
			final String input2 = stdin.next();

			int integerInput1 = -1;
			int integerInput2 = -1;

			try {
				integerInput1 = Integer.parseInt(input1);
				integerInput2 = Integer.parseInt(input2);
			}
			
			catch (final NumberFormatException error) {
				System.out.printf(errorInvalidType, input1 + " or " + input2);
				
				continue;
			}

			if (integerInput1 <= 10 || integerInput2 <= 10) {
				System.out.printf("A board size of %d by %d is too small. Both dimensions must be >10.\n> ", integerInput1, integerInput2);
				continue;
			}

			successfulSize = new Cordinate(integerInput2 / 2 * 2 + 1, integerInput1 / 2 * 2 + 1);
			isSuccessfulSizeGiven = true;
		}
			
		return successfulSize;
	}

	public char moveDirection(final ArrayList<Character> viableMoveKeys) {
		final String moveDialog = "Enter your move\n\t> w - North\n\t> a - West\n\t> s - South\n\t> d - East\n\t> x - Waste\n> ";
		
		System.out.print(moveDialog);
		
		Scanner x = new Scanner(System.in);
		
		while (x.hasNext()) {
			String nextInput = x.next();

			if (nextInput.length() != 1) {
				System.out.printf("\"%s\" is not a valid move. Try again\n> ", nextInput);
				
				continue;
			}

			char charAt = nextInput.charAt(0);

			if (charAt == 'q' || charAt == 'h') {
				return charAt;
			}

			if (viableMoveKeys.contains(charAt)) {
				return charAt;
			}
			
			System.out.printf("%c is not a valid move. Try again\n> ", charAt);
		}
		
		return ' ';
	}
	
	public void startGame() {
		GameLoop.clearScreen();
		
		final Cordinate gameSize = this.promptSize();
		
		this.runningGame = new Game(gameSize.getY() - 10, gameSize.getX() - 10);
		this.runningGame.initialize();
		
		clearScreen();

		while (runningGame.getIsRunning()) {
			clearScreen();
			
			System.out.println(this.runningGame);

			final char userMoveChar = this.moveDirection(this.runningGame.viableMoveKeys());

			if (userMoveChar == 'q') {
				promptQuit();
				clearScreen();
				continue;
			}
			
			if (userMoveChar == 'h') {
				showHelp();
				clearScreen();
				continue;
			}
			
			final boolean wasValidMove = this.runningGame.makeMove(userMoveChar);

			if (!wasValidMove || userMoveChar == ' ') {
				System.out.printf("%c was not a valid move. Try again\n> ", userMoveChar);
				
				(new Scanner(System.in)).nextLine();
			}
		}

		clearScreen();
		
		System.out.println(this.runningGame);

		final Game.PlayerIdentifier winner = this.runningGame.getWinner();

		System.out.printf("Congratulations, %s%s%s is the winner!\n", 
						  ((winner == Game.PlayerIdentifier.P1) ? Color.CRED : Color.CBLUE).getValue(),
						  winner,
						  Color.CEND.getValue());

		this.runningGame = null;

		System.out.println("Press Enter to continue...");

		final Scanner enterWait = new Scanner(System.in);
		enterWait.nextLine();
	}

	public static void clearScreen() {
		System.out.print(GameLoop.CLS);
	}

	public static void exit() {
		System.exit(0);
	}
}