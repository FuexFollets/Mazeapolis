public class Wall extends Entity {
	public Wall() {
		super();
	}

	public Color[][] renderColors() {
		return null;
	}

	public char[][] renderChars() {
		return new char[][]{
			{'#', '#'},
			{'#', '#'},
		};
	}
}