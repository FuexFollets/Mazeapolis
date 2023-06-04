public class Bonus extends Entity {
	enum BonusType {
		Add(1, "+"),
		Subtract(2, "-"),
		Multiply(3, "x"), 
		Divide(4, "/");

		private final int index;
		private final String symbol;

		private BonusType(final int index, final String symbol) {
			this.index = index;
			this.symbol = symbol;
		}

		public int getIndex() {
			return this.index;
		}

		public String getSymbol() {
			return this.symbol;
		}
	};

	final BonusType bonusType;
	final boolean isMysteryBonus;
	final int bonusValue;

	public Bonus() {
		super();
		
		final int bonusTypeDecider = MazeGenerator.discreteRandInclusive(0, 10);
		
		if (MazeGenerator.discreteRandInclusive(0, 3) == 0 || bonusTypeDecider >= 8) {
			this.isMysteryBonus = true;
		}

		else {
			this.isMysteryBonus = false;
		}

		if (bonusTypeDecider < 4) {
			this.bonusType = BonusType.Add;
			this.bonusValue = MazeGenerator.discreteRandInclusive(1, 9);
		}
		
		else if (bonusTypeDecider < 6) {
			this.bonusType = BonusType.Multiply;
			this.bonusValue = MazeGenerator.discreteRandInclusive(2, 3);
		}
		
		else if (bonusTypeDecider < 8) {
			this.bonusType = BonusType.Add;
			this.bonusValue = MazeGenerator.discreteRandInclusive(2, 3);
		}

		else {
			this.bonusType = BonusType.Subtract;
			this.bonusValue = MazeGenerator.discreteRandInclusive(1, 9);
		}
	}

	public Color[][] renderColors() {
		if (this.isMysteryBonus) {
			return new Color[][]{
				{Color.CRED, Color.CYELLOW},
				{Color.CGREEN, Color.CBLUE}
			};
		}

		return null;
	}

	public char[][] renderChars() {
		// Render the bonus
		final char blankSpaceCharValue = Game.BLANK_SPACE_CHAR.charAt(0);
		
		if (this.isMysteryBonus || this.bonusType == BonusType.Divide) {
			return new char[][]{
				{'?', '?'},
				{'?', '?'}
			};
		}

		return new char[][]{
			{this.bonusType.getSymbol().charAt(0), (char) (this.bonusValue + '0')},
			{blankSpaceCharValue, blankSpaceCharValue}
		};
	}

	public BonusType getBonusType() {
		return this.bonusType;
	}

	public int getBonusValue() {
		return this.bonusValue;
	}

	public String toString() {
		return String.format("%s%d", this.bonusType.getSymbol(), this.bonusValue);
	}
}