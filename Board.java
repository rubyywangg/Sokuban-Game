package sokoban;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents a Sokoban level board.
 * 
 * <p>
 * This class can read a Sokoban level from a plain text file where the
 * following symbols are used:
 * 
 * <ul>
 * <li>space is an empty square
 * <li># is a wall
 * <li>@ is the player
 * <li>$ is a box
 * <li>. is a storage location
 * <li>+ is the player on a storage location
 * <li>* is a box on a storage location
 * </ul>
 * 
 * <p>
 * The class manages a single {@code Player} object and a number of other
 * Sokoban game objects (boxes, storage locations, and walls).
 * 
 * <p>
 * The class determines whether the player can move to a specified square
 * depending on the configuration of boxes and walls.
 * 
 * <p>
 * The level is won when every box is moved to a storage location.
 * 
 * <p>
 * The class provides several methods that return information about a specified
 * position on the board .
 *
 */
public class Board {

	/**
	 * The pieces, excluding the player piece, making up the board.
	 */
	private List<Piece> pieces;

	/**
	 * The player piece.
	 */
	private Piece player;

	/**
	 * The width and height of the board.
	 */
	private int width;
	private int height;

	/*
	 * REQUIRED METHODS START HERE.
	 */

	/**
	 * Returns a list of Sokoban pieces having the specified type. The order of the
	 * pieces in the returned list is unspecified.
	 * 
	 * <p>
	 * This method never returns the player piece. To obtain the player piece, use
	 * the method {@code getPlayer}.
	 * 
	 * @param type the type of the pieces to return
	 * @return a list of Sokoban pieces having the specified type
	 */
	public List<Piece> getPieces(PieceType type) {
		List<Piece> typeList = new ArrayList<>();
		for (Piece piece : pieces) { // create a new Piece type called piece and go loop through the List<Piece> Type
										// called pieces
			if (piece.type() == type && piece.type() != PieceType.PLAYER) {
				typeList.add(piece);
			}
		}
		return typeList;
	}

	/**
	 * Returns {@code true} if there is a wall, player, or box at the specified
	 * position, {@code false} otherwise. Note that storage locations are considered
	 * unoccupied (unless there is also a player or box on the same position).
	 * 
	 * @param pos a location
	 * @return {@code true} if there is a wall, player, or box at the specified
	 *         location, {@code false} otherwise
	 */
	public boolean isOccupied(Position pos) {
		for (Piece piece : pieces) {
			if (piece.position().equals(pos) && (piece.type() == PieceType.WALL || piece.type() == PieceType.PLAYER
					|| piece.type() == PieceType.BOX)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns {@code true} if the specified location has a wall piece on it,
	 * {@code false} otherwise.
	 * 
	 * @param pos a position
	 * @return {@code true} if the specified location has a wall piece on it,
	 *         {@code false} otherwise
	 */
	public boolean hasWall(Position pos) {
		for (Piece piece : pieces) {
			if (piece.position().equals(pos) && piece.type() == PieceType.WALL) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns {@code true} if the specified location has a box on it, {@code false}
	 * otherwise.
	 * 
	 * @param pos a location
	 * @return {@code true} if the specified location has a box on it, {@code false}
	 *         otherwise
	 */
	public boolean hasBox(Position pos) {
		for (Piece piece : pieces) {
			if (piece.position().equals(pos) && piece.type() == PieceType.BOX) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns {@code true} if the specified location has a storage location on it,
	 * {@code false} otherwise.
	 * 
	 * @param pos a location
	 * @return {@code true} if the specified location has a storage location on it,
	 *         {@code false} otherwise
	 */
	public boolean hasStorage(Position pos) {
		for (Piece piece : pieces) {
			if (piece.position().equals(pos) && piece.type() == PieceType.STORAGE) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns {@code true} if every storage location has a box on it, {@code false}
	 * otherwise.
	 * 
	 * @return {@code true} if every storage location has a box on it, {@code false}
	 *         otherwise
	 */
	public boolean isSolved() {
		// get the locations of all the storage locations in one list
		List<Position> storageLocations = new ArrayList<>();
		for (Piece piece : pieces) {
			if (piece.type() == PieceType.STORAGE) {
				storageLocations.add(piece.position());
			}
		}
		// compare storage location with box locations
		for (Position storageLocation : storageLocations) {
			boolean boxOnStorage = false;

			for (Piece piece : pieces) {
				if (piece.position().equals(storageLocation) && piece.type() == PieceType.BOX) {
					boxOnStorage = true;
					break;
				}
			}
			if (boxOnStorage) {
				return true;
			}
		}
		return false;
	}

	/*
	 * REQUIRED METHODS END HERE.
	 */

	/**
	 * Initialize a board of width 11 and height 11 with a {@code Player} located at
	 * (4, 5), a {@code Box} located at (5, 5), and a storage location located at
	 * (6, 5).
	 */
	public Board() {
		this.player = new Piece(PieceType.PLAYER, new Position(4, 5));
		this.pieces = new ArrayList<>();
		this.pieces.add(new Piece(PieceType.BOX, new Position(5, 5)));
		this.pieces.add(new Piece(PieceType.STORAGE, new Position(6, 5)));
		this.width = 11;
		this.height = 11;
	}

	/**
	 * Initialize a board by reading a level from the file with the specified
	 * filename.
	 * 
	 * <p>
	 * The height of the board is determined by the number of lines in the file. The
	 * width of the board is determined by the longest line in the file where
	 * trailing spaces in a line are ignored.
	 * 
	 * @param filename the filename of the level
	 * @throws IOException if the level file cannot be read
	 */
	public Board(String filename) throws IOException {
		this.pieces = new ArrayList<>();
		this.readLevel(filename);
	}

	private final void readLevel(String filename) throws IOException {
		Path path = FileSystems.getDefault().getPath("src", "sokoban", filename);
		List<String> level = Files.readAllLines(path);
		this.height = level.size();
		this.width = 0;
		for (int y = 0; y < this.height; y++) {
			String row = level.get(this.height - 1 - y);
			if (row.length() > this.width) {
				this.width = row.length();
			}
			for (int x = 0; x < row.length(); x++) {
				Position pos = new Position(x, y);
				char c = row.charAt(x);
				if (c == ' ') {
					continue;
				} else if (c == '#') {
					this.pieces.add(new Piece(PieceType.WALL, pos));
				} else if (c == '@') {
					Piece p = new Piece(PieceType.PLAYER, pos);
					this.player = p;
				} else if (c == '$') {
					this.pieces.add(new Piece(PieceType.BOX, pos));
				} else if (c == '.') {
					this.pieces.add(new Piece(PieceType.STORAGE, pos));
				} else if (c == '+') {
					Piece p = new Piece(PieceType.PLAYER, pos);
					this.player = p;
					this.pieces.add(new Piece(PieceType.STORAGE, pos));
				} else if (c == '*') {
					this.pieces.add(new Piece(PieceType.BOX, pos));
					this.pieces.add(new Piece(PieceType.STORAGE, pos));
				}
			}
		}
	}

	/**
	 * Returns the width of this board.
	 * 
	 * @return the width of this board
	 */
	public int width() {
		return this.width;
	}

	/**
	 * Returns the height of this board.
	 * 
	 * @return the height of this board
	 */
	public int height() {
		return this.height;
	}

	/**
	 * Returns the player.
	 * 
	 * @return the player
	 */
	public Piece getPlayer() {
		return this.player;
	}

	/**
	 * Returns {@code true} if the specified location has a player on it,
	 * {@code false} otherwise.
	 * 
	 * @param pos a location
	 * @return {@code true} if the specified location has a player on it,
	 *         {@code false} otherwise
	 */
	public boolean hasPlayer(Position pos) {
		return this.player.position().equals(pos);
	}

	/**
	 * Returns {@code true} if the specified location is unoccupied or has only a
	 * storage location, {@code false} otherwise.
	 * 
	 * @param loc a location
	 * @return {@code true} if the specified location is unoccupied or has only a
	 *         storage location, {@code false} otherwise
	 */
	public boolean isFree(Position loc) {
		return !this.isOccupied(loc);
	}

	/**
	 * Moves a box from a specified location to a specified location. Assumes that
	 * there is a box at the specified starting location and that there is no box at
	 * the specified destination location
	 * 
	 * @param from the starting location
	 * @param to   the destination location
	 */
	private void moveBoxTo(Position from, Position to) {
		for (Piece obj : this.pieces) {
			if (obj.type() == PieceType.BOX && obj.position().equals(from)) {
				obj.moveTo(to);
				break;
			}
		}
	}

	/**
	 * Moves the player to the specified adjacent position if possible. If there is
	 * a box in the specified adjacent location then the box is pushed to the next
	 * adjacent location.
	 * 
	 * <p>
	 * Returns {@code false} if the player cannot move to the adjacent location
	 * (leaving the player location unchanged).
	 * 
	 * <p>
	 * This method assumes that {@code adj} is actually adjacent to the player. Also
	 * assumes that {@code adjNext} is adjacent to {@code adj} and is in the same
	 * direction relative to the player as {@code adj}.
	 * 
	 * @param adj     a location adjacent to the player
	 * @param adjNext the location two positions away from the player in the same
	 *                direction as {@code adj}
	 * @return true if the player is moved, false otherwise
	 */
	private boolean movePlayerTo(Position adj, Position adjNext) {
		if (this.isFree(adj)) {
			this.player.moveTo(adj);
			return true;
		} else if (this.hasBox(adj) && this.isFree(adjNext)) {
			this.player.moveTo(adj);
			this.moveBoxTo(adj, adjNext);
			return true;
		}
		return false;
	}

	/**
	 * Moves the player to the left adjacent location if possible. If there is a box
	 * in the left adjacent location then the box is pushed to the adjacent location
	 * left of the box.
	 * 
	 * <p>
	 * Returns {@code false} if the player cannot move to the left adjacent location
	 * (leaving the player location unchanged).
	 * 
	 * @return true if the player is moved to the left adjacent location, false
	 *         otherwise
	 */
	public boolean movePlayerLeft() {
		Position left = this.player.position().left();
		return this.movePlayerTo(left, left.left());
	}

	/**
	 * Moves the player to the right adjacent location if possible. If there is a
	 * box in the right adjacent location then the box is pushed to the adjacent
	 * location right of the box.
	 * 
	 * <p>
	 * Returns {@code false} if the player cannot move to the right adjacent
	 * location (leaving the player location unchanged).
	 * 
	 * @return true if the player is moved to the right adjacent location, false
	 *         otherwise
	 */
	public boolean movePlayerRight() {
		Position right = this.player.position().right();
		return this.movePlayerTo(right, right.right());
	}

	/**
	 * Moves the player to the above adjacent location if possible. If there is a
	 * box in the above adjacent location then the box is pushed to the adjacent
	 * location above the box.
	 * 
	 * <p>
	 * Returns {@code false} if the player cannot move to the above adjacent
	 * location (leaving the player location unchanged).
	 * 
	 * @return true if the player is moved to the above adjacent location, false
	 *         otherwise
	 */
	public boolean movePlayerUp() {
		Position up = this.player.position().up();
		return this.movePlayerTo(up, up.up());
	}

	/**
	 * Moves the player to the below adjacent location if possible. If there is a
	 * box in the below adjacent location then the box is pushed to the adjacent
	 * location below of the box.
	 * 
	 * <p>
	 * Returns {@code false} if the player cannot move to the below adjacent
	 * location (leaving the player location unchanged).
	 * 
	 * @return true if the player is moved to the below adjacent location, false
	 *         otherwise
	 */
	public boolean movePlayerDown() {
		Position down = this.player.position().down();
		return this.movePlayerTo(down, down.down());
	}

	/**
	 * Returns a string representation of this board. The string representation is
	 * identical to file format describing Sokoban levels.
	 * 
	 * @return a string representation of this board
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		for (int y = this.height - 1; y >= 0; y--) {
			for (int x = 0; x < this.width; x++) {
				Position pos = new Position(x, y);
				if (this.isFree(pos)) {
					if (this.hasStorage(pos)) {
						b.append(".");
					} else {
						b.append(" ");
					}
				} else if (this.hasWall(pos)) {
					b.append("#");
				} else if (this.hasBox(pos)) {
					if (this.hasStorage(pos)) {
						b.append("*");
					} else {
						b.append("$");
					}
				} else if (this.hasPlayer(pos)) {
					if (this.hasStorage(pos)) {
						b.append("+");
					} else {
						b.append("@");
					}
				}
			}
			b.append('\n');
		}
		return b.toString();
	}

	/*
	 * public static void main(String[] args) throws IOException { Board b = new
	 * Board("level01.txt"); System.out.println(b); }
	 */
}
