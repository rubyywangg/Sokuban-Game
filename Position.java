package sokoban;

import java.util.Objects;

/**
 * A class that represents a position using a pair of integer coordinates
 * {@code (x, y)} where {@code x} and {@code y} are non-negative values. A
 * {@code Position} object is immutable.
 * 
 * <p>
 * The positive x-direction points to the right and the positive y-direction
 * points upwards.
 * 
 * <p>
 * A special constant named {@code NONE} is defined by this class to represent
 * the value "no such position". {@code NONE} is equal to itself, and not equal
 * to every other {@code Position} object. The constant {@code NONE} is unusual
 * in that it does not enforce the class invariant that its coordinates be
 * non-negative.
 */
public class Position {

	private int x;
	private int y;

	/**
	 * The special constant representing the value "no such position".
	 */
	public static final Position NONE = new Position();
	static {
		NONE.x = -1;
		NONE.y = -1;
	}

	/*
	 * MAKE SURE THAT YOUR METHODS HAVE THE SAME RETURN TYPE, NAME, AND PARAMETER
	 * LISTS AS SHOWN IN THE DOCUMENTATION.
	 */
	public Position() {
		this.x = 0;
		this.y = 0;
	}

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Position(Position other) {
		this.x = other.x;
		this.y = other.y;
	}

	public Position down() {
		return new Position(this.x, this.y - 1); // DO I NEED THE NEW KEYWORD
	}

	public Position up() {
		return new Position(this.x, this.y + 1); // DO I NEED THE NEW KEYWORD
	}

	public Position left() {
		return new Position(this.x - 1, this.y); // DO I NEED THE NEW KEYWORD
	}

	public Position right() {
		return new Position(this.x + 1, this.y); // DO I NEED THE NEW KEYWORD
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.x, this.y);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Position other = (Position) obj;
		return x == other.x && y == other.y;
	}

	public boolean isAdjacentTo(Position other) {
		boolean isImmedLeft = (this.x == other.x - 1) && (this.y == other.y);
		boolean isImmedRight = (this.x == other.x + 1) && (this.y == other.y);
		boolean isImmedUp = (this.x == other.x) && (this.y == other.y + 1);
		boolean isImmedDown = (this.x == other.x) && (this.y == other.y - 1);

		if (isImmedLeft || isImmedRight || isImmedUp || isImmedDown) {
			return true;
		}
		return false;
	}

	public int x() {
		return this.x;
	}

	public int y() {
		return this.y;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	public static void main(String[] args) {
		Position position = new Position(2, 4);
		System.out.println("Down: " + position.down());
		System.out.println("Up: " + position.up());
		System.out.println("Left: " + position.left());
		System.out.println("Right: " + position.right());
		System.out.println("HashCode: " + position.hashCode());
		System.out.println("Equals: " + position.equals(new Position(2, 4)));
		System.out.println("IsAdjacentTo: " + position.isAdjacentTo(new Position(2, 5)));
		System.out.println("X Coordinate: " + position.x());
		System.out.println("Y Coordinate: " + position.y());
		System.out.println("ToString: " + position);
	}

}
