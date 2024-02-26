package sokoban;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class SokobanGUI extends JFrame implements ActionListener {
	
	private static final String LEFT = "LEFT";
	private static final String RIGHT = "RIGHT";
	private static final String UP = "UP";
	private static final String DOWN = "DOWN";
	private static final String RELOAD = "RELOAD";
	private static final String LOAD = "LOAD";
	private static final String EXIT = "EXIT";
	
	private String filename;
	private Map<Position, JLabel> levelMap;
	private Board board;
	private ImageIcon boxIcon;
	private ImageIcon boxAndStorageIcon;
	private ImageIcon playerIcon;
	private ImageIcon storageIcon;
	private ImageIcon wallIcon;
	
	public SokobanGUI() {
		super("Sokoban");
		this.board = new Board();
		this.filename = "";
		this.levelMap = new HashMap<>();
		this.boxIcon = createImageIcon("box.png", "box");
		this.boxAndStorageIcon = createImageIcon("box_on_storage.png", "box on storage");
		this.playerIcon = createImageIcon("player.png", "player");
		this.storageIcon = createImageIcon("storage.png", "storage");
		this.wallIcon = createImageIcon("wall.png", "wall");
		
		this.setJMenuBar(this.makeMenu());
		
		
		this.initLevel();
	}
	
	private final void initLevel() {
		String title = "Sokoban";
		if (!this.filename.isEmpty()) {
			title += " (" + this.filename + ")";
		}
		this.setTitle(title);
		JPanel contentPanel = new JPanel();
		contentPanel.add(makeLevelPanel());
		contentPanel.add(makeButtonPanel());
		this.setContentPane(contentPanel);
		this.pack();
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.drawWalls();
		this.drawStorage();
		this.drawBoxes();
		this.drawPlayer();
	}
	
	
	private final ImageIcon createImageIcon(String path, String description) {
	    java.net.URL imgURL = getClass().getResource(path);
	    if (imgURL != null) {
	        return new ImageIcon(imgURL, description);
	    } else {
	        System.err.println("Couldn't find file: " + path);
	        return null;
	    }
	}
	
	private final JMenuBar makeMenu() {
		JMenuBar bar = new JMenuBar();
		JMenu menu = new JMenu("Sokoban");
		bar.add(menu);

		this.addMenuItem(menu, "Reload level", RELOAD);
		this.addMenuItem(menu, "Load level", LOAD);
		menu.addSeparator();
		this.addMenuItem(menu, "Exit", EXIT);
		return bar;
	}

	private final void addMenuItem(JMenu menu, String label, String action) {
		JMenuItem item = new JMenuItem(label);
		item.setActionCommand(action);
		item.addActionListener(this);
		menu.add(item);
	}
	
	private static JLabel makeLabel(String s) {
		Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 32);
		JLabel b = new JLabel(s);
		b.setPreferredSize(new Dimension(50, 50));
		b.setMaximumSize(b.getSize());
		b.setFont(font);
		b.setBackground(Color.WHITE);
		return b;
	}
	
	private JButton makeButton(String s, String cmd) {
		Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
		JButton b = new JButton(s);
		b.setPreferredSize(new Dimension(50, 50));
		b.setMaximumSize(b.getSize());
		b.setFont(font);
		b.setBackground(Color.WHITE);
		b.setActionCommand(cmd);
		b.addActionListener(this);
		return b;
	}
	
	private JPanel makeLevelPanel() {
		
		int width = this.board.width();
		int height = this.board.height();
		
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(height, width, 0, 0));
		for (int y = height - 1; y >= 0; y--) {
			for (int x = 0; x < width; x++) {
				Position loc = new Position(x, y);
				JLabel b = makeLabel("");
				p.add(b);
				this.levelMap.put(loc, b);
			}
		}
		return p;
	}
	
	private JPanel makeButtonPanel() {
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(3, 3, 0, 0));
		
		// row 1
		p.add(makeLabel(""));
		JButton up = makeButton("U", UP);
		p.add(up);
		p.add(makeLabel(""));
		
		// row 2
		JButton left = makeButton("L", LEFT);
		p.add(left);
		p.add(makeLabel(""));
		JButton right = makeButton("R", RIGHT);
		p.add(right);
		
		// row 3
		p.add(makeLabel(""));
		JButton down = makeButton("D", DOWN);
		p.add(down);
		p.add(makeLabel(""));
		
		return p;
	}
	
	private void drawWalls() {
		List<Piece> walls = this.board.getPieces(PieceType.WALL);
		for (Piece w : walls) {
			Position loc = w.position();
			JLabel b = this.levelMap.get(loc);
			b.setIcon(this.wallIcon);
		}
	}
	
	private void drawStorage() {
		List<Piece> storage = this.board.getPieces(PieceType.STORAGE);
		for (Piece s : storage) {
			Position loc = s.position();
			JLabel b = this.levelMap.get(loc);
			if (this.board.hasBox(loc)) {
				b.setIcon(this.boxAndStorageIcon);
			}
			else {
				b.setIcon(this.storageIcon);
			}
			
		}
	}
	
	private void drawBoxes() {
		List<Piece> boxes = this.board.getPieces(PieceType.BOX);
		for (Piece box : boxes) {
			Position loc = box.position();
			JLabel b = this.levelMap.get(loc);
			if (this.board.hasStorage(loc)) {
				b.setIcon(this.boxAndStorageIcon);
			}
			else {
				b.setIcon(this.boxIcon);
			}
			
		}
	}
	
	private void drawPlayer() {
		Position loc = this.board.getPlayer().position();
		JLabel b = this.levelMap.get(loc);
		b.setIcon(this.playerIcon);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		Position loc = this.board.getPlayer().position();
		JLabel b = this.levelMap.get(loc);
		boolean moved = false;
		if (cmd.equals(LEFT)) {
			moved = this.board.movePlayerLeft();
		}
		else if (cmd.equals(RIGHT)) {
			moved = this.board.movePlayerRight();
		}
		else if (cmd.equals(UP)) {
			moved = this.board.movePlayerUp();
		}
		else if (cmd.equals(DOWN)) {
			moved = this.board.movePlayerDown();
		}
		else if (cmd.equals(RELOAD)) {
			if (this.filename.isEmpty()) {
				this.board = new Board();
				this.initLevel();
			}
			else {
				try {
					this.board = new Board(this.filename);
					this.initLevel();
				}
				catch (IOException x) {
					JOptionPane.showMessageDialog(this, "Could not read the level file.");
				}
			}
		}
		else if (cmd.equals(LOAD)) {
			Path path = FileSystems.getDefault().getPath("src", "sokoban");
			final JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(path.toFile());
			int returnVal = fc.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				try {
					this.board = new Board(file.getName());
					this.filename = file.getName();
					this.initLevel();
				}
				catch (IOException x) {
					JOptionPane.showMessageDialog(this, "Could not read the level file.");
				}
				
			}
			
		}
		else if (cmd.equals(EXIT)) {
			this.dispose();
		}
		
		if (moved) {
			b.setIcon(null);
			this.drawStorage();
			this.drawBoxes();
			this.drawPlayer();
			if (this.board.isSolved()) {
				JOptionPane.showMessageDialog(this, "You won!");
			}
		}
	}
	
	
	public static void main(String[] args) throws IOException {
		SokobanGUI gui = new SokobanGUI();
		gui.setVisible(true);
		
	}

}
