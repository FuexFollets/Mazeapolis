public abstract class Entity {
	public static final int DIMENSIONS = 2;

	public abstract Color[][] renderColors(); // Must return DIMENSIONS * DIMENSIONS array
	public abstract char[][] renderChars(); // Must return DIMENSIONS * DIMENSIONS array
}