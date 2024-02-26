package sokoban;

/**
 * A class that represents a game piece in Sokoban.
 * 
 * <p>
 * The game Sokoban uses four different types of pieces: The player, boxes that
 * the player pushes around the board, storage positions that mark the positions
 * where the boxes are to be placed by the player, and walls that restrict the
 * movement of the player and boxes.
 * 
 * <p>
 * Every {@code Piece} instance knows its current position on the board and its
 * type (player, box, storage position, wall).
 */
public class Piece {
	/*
	 * MAKE SURE THAT YOUR FIELDS ARE PRIVATE.
	 * 
	 * MAKE SURE THAT YOUR METHODS HAVE THE SAME RETURN TYPE, NAME, AND PARAMETER
	 * LISTS AS SHOWN IN THE DOCUMENTATION.
	 */
	private PieceType type;
	private Position pos;

	public Piece(PieceType type, Position pos) {
		this.type = type;
		this.pos = pos;
	}

	public boolean isMovable() {
		if (type == PieceType.PLAYER || type == PieceType.BOX) {
			return true;
		}
		return false;
	}

	public boolean moveTo(Position to) {
		if (to != null) {
			pos = to;
			return true;
		}
		return false;
	}

	public boolean moveDown() {
		return moveTo(pos.down());
	}

	public boolean moveUp() {
		return moveTo(pos.up());
	}

	public boolean moveLeft() {
		return moveTo(pos.left());
	}

	public boolean moveRight() {
		return moveTo(pos.right());
	}

	public Position position() {
		return pos;
	}

	public PieceType type() {
		return type;
	}

	public static void main(String[] args) {
		// starting positions
		Piece player = new Piece(PieceType.PLAYER, new Position(2, 2));
		Piece box = new Piece(PieceType.BOX, new Position(3, 3));

		// print starting positions
		System.out.println("Initial player position: " + player.position());
		System.out.println("Initial box position: " + box.position());

		// shift the player and box by calling move methods
		player.moveLeft();
		box.moveUp();

		// print out new positions
		System.out.println("New player position: " + player.position());
		System.out.println("New box position: " + box.position());

		// print out the types of player and box using type method
		System.out.println("Player type: " + player.type());
		System.out.println("Box type: " + box.type());

	}
}
