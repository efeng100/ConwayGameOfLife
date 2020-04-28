package game;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;

public class CellBoardImpl extends JPanel implements CellBoard {

	private static final int DEFAULT_BOARD_AREA_WIDTH = 1000;
	private static final int DEFAULT_BOARD_AREA_HEIGHT = 600;
	
	private Cell[][] cells;
	
	public CellBoardImpl(int width, int height) {
		setLayout(new GridLayout(height, width));
		cells = new Cell[width][height];
		
		int cellLength = Math.min(DEFAULT_BOARD_AREA_WIDTH/width, DEFAULT_BOARD_AREA_HEIGHT/height);
		Dimension preferredSize = new Dimension(cellLength, cellLength);
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				cells[x][y] = new CellImpl(this, x, y);
				((CellImpl)cells[x][y]).setPreferredSize(preferredSize);
				add(((CellImpl) cells[x][y]));
			}
		}
	}
	
	@Override
	public int getBoardWidth() {
		return cells.length;
	}

	@Override
	public int getBoardHeight() {
		return cells[0].length;
	}

	@Override
	public Cell getCellAt(int x, int y) {
		return cells[x][y];
	}

	@Override
	public void addListener(CellListener listener) {
		for (int x = 0; x < getBoardWidth(); x++) {
			for (int y = 0; y < getBoardHeight(); y++) {
				cells[x][y].addListener(listener);
			}
		}
	}

	@Override
	public void removeListener(CellListener listener) {
		for (int x = 0; x < getBoardWidth(); x++) {
			for (int y = 0; y < getBoardHeight(); y++) {
				cells[x][y].removeListener(listener);
			}
		}
	}

}
