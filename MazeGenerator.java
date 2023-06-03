import java.util.ArrayList;

public final class MazeGenerator {
	public enum Cell { Block, Blank };

	Cell[][] maze;

	final int rows;
	final int columns;

	public MazeGenerator(final int columns, final int rows) {
		this.rows = rows;
		this.columns = columns;
		
		this.maze = new Cell[rows][columns];

		for (int rowIndex = 0; rowIndex < this.rows; rowIndex++) {
			for (int columnIndex = 0; columnIndex < this.columns; columnIndex++) {
				maze[rowIndex][columnIndex] = Cell.Blank;
			}
		}
	}

	public Cell[][] getMaze() {
		return this.maze;
	}

	public Cell at(final Cordinate position) {
		return this.maze[position.getY()][position.getX()];
	}

	public boolean inBounds(final Cordinate cordinate) {
		final int x = cordinate.getX();
		final int y = cordinate.getY();

		return (
			0 <= x && x < columns &&
			0 <= y && y < rows
		);
	}

	public void generate() {
		final Cordinate sectionUpperLeft = new Cordinate(0, 0);
		final Cordinate sectionLowerRight = new Cordinate(columns, rows);
		
		divide(sectionUpperLeft, sectionLowerRight);
	}

	// Divdes the seciton in two
	public void divide(final Cordinate sectionUpperLeft, final Cordinate sectionLowerRight) {
		final int sectionWidth = sectionLowerRight.getX() - sectionUpperLeft.getX();
		final int sectionHeight = sectionLowerRight.getY() - sectionUpperLeft.getY();
		
		final Cordinate sectionUpperRight = sectionUpperLeft.inDirection(Cordinate.Direction.East, sectionWidth);
		final Cordinate sectionLowerLeft = sectionLowerRight.inDirection(Cordinate.Direction.West, sectionWidth);
		
		if (sectionWidth < 3 || sectionHeight < 3) { return; }

		if ((sectionWidth == sectionHeight && discreteRandInclusive(0, 1) == 1) || sectionWidth > sectionHeight) { // Wide section, creates a vertical line
			final ArrayList<Cordinate> possibleStarts = possibleWallStarts(sectionUpperLeft, sectionUpperRight);
			final Cordinate wallStart = randChoose(possibleStarts);
			final Cordinate wallEnd = wallStart.inDirection(Cordinate.Direction.South, sectionHeight);
			final Cordinate hole = generateHoleInLine(wallStart, wallEnd);

			this.makeMazeLine(wallStart, wallEnd, hole);

			divide(sectionUpperLeft, wallEnd);
			divide(wallStart, sectionLowerRight);
		}

		else { // Thin section, creates a horizontal line
			final ArrayList<Cordinate> possibleStarts = possibleWallStarts(sectionUpperLeft, sectionLowerLeft);
			final Cordinate wallStart = randChoose(possibleStarts);
			final Cordinate wallEnd = wallStart.inDirection(Cordinate.Direction.East, sectionWidth);
			final Cordinate hole = generateHoleInLine(wallStart, wallEnd);
			
			this.makeMazeLine(wallStart, wallEnd, hole);
			
			divide(sectionUpperLeft, wallEnd);
			divide(wallStart, sectionLowerRight);
		}
	}
	
	public void makeMazeLine(final Cordinate pointStart, final Cordinate pointEnd, final Cordinate hole) { // Draws a line with a hole in it
		if (pointStart.equals(pointEnd)) {
			return;
		}

		for (final Cordinate blockCordinate : pointStart.allInBetween(pointEnd, false)) {
			if (this.inBounds(blockCordinate)) {
				this.maze[blockCordinate.getY()][blockCordinate.getX()] = Cell.Block;
			}
		}
		
		// final Cordinate autoHole = generateHoleInLine(pointStart, pointEnd);

		if (hole != null) {
			this.maze[hole.getY()][hole.getX()] = Cell.Blank;
		}
	}

	public static ArrayList<Cordinate> possibleWallStarts(final Cordinate lineStart, final Cordinate lineEnd) {
		final int lineLength = lineStart.distanceFrom(lineEnd);

		if (lineLength == -1 || lineLength < 2) {
			return null;
		}

		final Cordinate.Direction lineDirection = lineStart.directionTowards(lineEnd);
		ArrayList<Cordinate> possibleStarts = new ArrayList<Cordinate>();

		final Cordinate possibleBegin = lineStart.inDirection(lineDirection, 2);
		final Cordinate possibleEnd = lineEnd.inDirection(lineDirection.opposite(), 2);

		for (final Cordinate lineElement : possibleBegin.allInBetween(possibleEnd, true)) {
			if (lineElement.getX() % 2 == 0 && lineElement.getY() % 2 == 0) { // Must be even
				possibleStarts.add(lineElement);
			}
		}

		return possibleStarts;
	} 

	// Requires both cordinates to be on the same horizontal or vertical axis. Returns null otherwise
	static Cordinate generateHoleInLine(final Cordinate pointStart, final Cordinate pointEnd) {
		final int lineLength = pointStart.distanceFrom(pointEnd);
		
		if (lineLength == -1 || lineLength < 2) {
			return null;
		}

		
		final Cordinate.Direction lineDirection = pointStart.directionTowards(pointEnd);

		/*
		if (lineLength <= 3) {
			return pointStart.inDirection(lineDirection, ));
		}
		*/
		
		final int position = discreteRandInclusive(0, lineLength / 2) * 2 + 1; // Must be odd 
		final Cordinate generatedHole = pointStart.inDirection(lineDirection, position);

		return generatedHole;
	}

	public static int discreteRandInclusive(final int min, final int max) {
		final int range = max - min;
		
		return (int) (Math.random() * range) + min;
	}

	public static <E> E randChoose(final E[] collection) {
		if (collection == null || collection.length == 0) {
			return null;
		}
		
		return collection[discreteRandInclusive(0, collection.length - 1)];
	}
	
	public static <E> E randChoose(final ArrayList<E> collection) {
		if (collection == null || collection.size() == 0) {
			return null;
		}
		
		return collection.get(discreteRandInclusive(0, collection.size() - 1));
	}

	@Override
	public String toString() {
		String gridRepresentation = "";

		for (final MazeGenerator.Cell[] row : this.maze) {
			for (final MazeGenerator.Cell value : row) {
				if (value == Cell.Block) {
					gridRepresentation += "#";
				}

				else {
					gridRepresentation += " ";
				}
			}

			gridRepresentation += "\n";
		}

		return "rows: " + this.rows + "\ncolumns: " + this.columns + "\n" + gridRepresentation;
	}
}