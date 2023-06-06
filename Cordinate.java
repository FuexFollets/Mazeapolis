public class Cordinate {
	public enum Direction {
		North('w', "north"),
		East('d', "east"),
		South('s', "south"),
		West('a', "west"),
		None(' ', "none");

		/**
		 * N
		 * W E
		 * S
		 */

		private char key;
		private String ordinal;

		private Direction(final char key, final String ordinal) {
			this.key = key;
			this.ordinal = ordinal;
		}

		public char getKey() {
			return this.key;
		}

		public String getOrdinal() {
			return this.ordinal;
		}

		public Direction rotateLeft() {
			switch (this) {
				case North:
					return West;
				case West:
					return South;
				case South:
					return East;
				case East:
					return North;

				case None:
					return None;
			}

			return None;
		}

		public static Direction fromKey(final char keyFrom) {
			switch (keyFrom) {
				case 'w':
					return North;
				case 'd':
					return East;
				case 's':
					return South;
				case 'a':
					return West;

				default:
					return None;
			}
		}

		public Direction rotateRight() {
			return this.rotateLeft().rotateLeft().rotateLeft();
		}

		public Direction opposite() {
			return this.rotateLeft().rotateLeft();
		}

		public boolean isVertical() {
			return this == North || this == South;
		}
	};

	private int x;
	private int y;

	public Cordinate() {
		x = 0;
		y = 0;
	}

	public Cordinate(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public void setX(final int value) {
		this.x = value;
	}

	public void setY(final int value) {
		this.y = value;
	}

	public boolean inBounds(final int rows, final int columns) {
		return (this.getX() < columns && this.getX() >= 0) &&
				(this.getY() < rows && this.getY() >= 0);
	}

	public int distanceFrom(final Cordinate otherCordinate) { // Returns -1 if they are not on the same row or column
		if (this.getX() == otherCordinate.getX()) {
			return Math.abs(this.getY() - otherCordinate.getY());
		}

		if (this.getY() == otherCordinate.getY()) {
			return Math.abs(this.getX() - otherCordinate.getX());
		}

		return -1;
	}

	// Must be on the same horizontal or vertical axis
	public Direction directionTowards(final Cordinate otherCordinate) {
		if (this.distanceFrom(otherCordinate) == -1) {
			return Direction.None;
		}

		if (this.getX() == otherCordinate.getX() && this.getY() > otherCordinate.getY()) {
			return Direction.North;
		}
		if (this.getX() == otherCordinate.getX() && this.getY() < otherCordinate.getY()) {
			return Direction.South;
		}

		if (this.getY() == otherCordinate.getY() && this.getX() < otherCordinate.getX()) {
			return Direction.East;
		}
		if (this.getY() == otherCordinate.getY() && this.getX() > otherCordinate.getX()) {
			return Direction.West;
		}

		return Direction.None;
	}

	public Cordinate inDirection(final Direction direction, int distance) {
		switch (direction) {
			case North:
				return new Cordinate(this.getX(), this.getY() - distance);
			case South:
				return new Cordinate(this.getX(), this.getY() + distance);
			case East:
				return new Cordinate(this.getX() + distance, this.getY());
			case West:
				return new Cordinate(this.getX() - distance, this.getY());
			case None:
				return this;
		}

		return this;
	}

	public Cordinate[] allInBetween(final Cordinate otherCordinate, final boolean inclusive) {
		final int distance = this.distanceFrom(otherCordinate) + (inclusive ? 1 : 0) + 1;

		if (distance == -1) {
			return null;
		}

		Cordinate[] range = new Cordinate[distance];
		Direction direction = this.directionTowards(otherCordinate);

		for (int index = 0; index < distance; index++) {
			range[index] = this.inDirection(direction, index);
		}

		return range;
	}

	@Override
	public boolean equals(Object otherPossibleCordinate) {
		if (!(otherPossibleCordinate instanceof Cordinate)) {
			return false;
		}

		final Cordinate otherCordinate = (Cordinate) otherPossibleCordinate;

		return otherCordinate.getX() == this.getX() &&
				otherCordinate.getY() == this.getY();
	}

	@Override
	public String toString() {
		return "(" + this.getX() + ", " + this.getY() + ")";
	}
}