package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class CellImpl extends JPanel implements Cell, MouseListener {
	
	private static final Color DEFAULT_EMPTY_COLOR = Color.WHITE;
	private static final Color DEFAULT_FILL_COLOR = Color.BLACK;
	private static final Color DEFAULT_EDGE_COLOR = Color.GRAY;
	
	private Color emptyColor;
	private Color fillColor;
	private Color edgeColor;
	
	private int xCoord;
	private int yCoord;
	private boolean isFilled;
	private CellBoard board;
	private List<CellListener> cellListeners;
	
	public CellImpl(CellBoard board, int x, int y) {
		this.board = board;
		this.xCoord = x;
		this.yCoord = y;
		
		emptyColor = DEFAULT_EMPTY_COLOR;
		fillColor = DEFAULT_FILL_COLOR;
		edgeColor = DEFAULT_EDGE_COLOR;
		
		isFilled = false;
		cellListeners = new ArrayList<CellListener>();
		
		setBackground(DEFAULT_EMPTY_COLOR);
		addMouseListener(this);		
	}
	
	public int getXCoord() {
		return xCoord;
	}
	
	public int getYCoord() {
		return yCoord;
	}

	@Override
	public CellBoard getBoard() {
		return board;
	}

	@Override
	public boolean isFilled() {
		return isFilled;
	}

	@Override
	public void fill() {
		isFilled = true;
		triggerUpdate();
	}

	@Override
	public void empty() {
		isFilled = false;
		triggerUpdate();
	}

	@Override
	public void addListener(CellListener listener) {
		cellListeners.add(listener);
	}

	@Override
	public void removeListener(CellListener listener) {
		cellListeners.remove(listener);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		for (CellListener listener: cellListeners) {
			listener.cellClicked(this);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
	
	// From JPanel
	@Override
	public void paintComponent(Graphics g) {
		if (isFilled()) {
			setBackground(fillColor);
		} else {
			setBackground(emptyColor);
		}
		
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g.create();
		
		g2d.setColor(edgeColor);
		g2d.setStroke(new BasicStroke(1));
		g2d.drawRect(0, 0, getWidth(), getHeight());
	}
	
	private void triggerUpdate() {
		repaint();
		
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
				}
				repaint();
			}
		}).start();
	}

}
