package game;

import java.util.ArrayList;
import java.util.List;

public class ConwayModel {
	
	private int[][] cells;
	private int width;
	private int height;
	private int surviveLow;
	private int surviveHigh;
	private int birthLow;
	private int birthHigh;
	private boolean torusMode;
	private List<ConwayObserver> observers;
	
	public ConwayModel(int width, int height, int surviveLow, int surviveHigh, 
			int birthLow, int birthHigh, boolean torusMode) {
		this.width = width;
		this.height = height;
		this.surviveLow = surviveLow;
		this.surviveHigh = surviveHigh;
		this.birthLow = birthLow;
		this.birthHigh = birthHigh;
		this.torusMode = torusMode;
		
		cells = new int[this.width][this.height];
		
		observers = new ArrayList<ConwayObserver>();
	}
	
	public ConwayModel() {
		this(ConwayView.DEFAULT_BOARD_WIDTH, ConwayView.DEFAULT_BOARD_HEIGHT, 
			 ConwayView.DEFAULT_SURVIVE_LOW, ConwayView.DEFAULT_SURVIVE_HIGH, 
			 ConwayView.DEFAULT_BIRTH_LOW, ConwayView.DEFAULT_BIRTH_HIGH, false);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getSurviveLow() {
		return surviveLow;
	}
	
	public void setSurviveLow(int surviveLow) {
		this.surviveLow = surviveLow;
	}
	
	public int getSurviveHigh() {
		return surviveHigh;
	}
	
	public void setSurviveHigh(int surviveHigh) {
		this.surviveHigh = surviveHigh;
	}
	
	public int getBirthLow() {
		return birthLow;
	}
	
	public void setBirthLow(int birthLow) {
		this.birthLow = birthLow;
	}
	
	public int getBirthHigh() {
		return birthHigh;
	}
	
	public void setBirthHigh(int birthHigh) {
		this.birthHigh = birthHigh;
	}
	
	public boolean getTorusMode() {
		return torusMode;
	}
	
	public void enableTorusMode() {
		torusMode = true;
	}
	
	public void disableTorusMode() {
		torusMode = false;
	}
	
	public void toggleTorusMode() {
		torusMode = !torusMode;
	}
	
	public void setWidthAndHeight(int width, int height) {
		this.width = width;
		this.height = height;
		
		cells = new int[this.width][this.height];
	}
	
	public boolean isCellFilledAt(int xCoord, int yCoord) {
		return cells[xCoord][yCoord] == 1;
	}
	
	public void fillCellAt(int xCoord, int yCoord) {
		cells[xCoord][yCoord] = 1;
	}
	
	public void emptyCellAt(int xCoord, int yCoord) {
		cells[xCoord][yCoord] = 0;
	}
	
	public void toggleCellFillAt(int xCoord, int yCoord) {
		cells[xCoord][yCoord] = 1-cells[xCoord][yCoord];
	}
	
	public void fillCellsRandomly() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				cells[x][y] = Math.random() < 0.5 ? 0 : 1;
			}
		}
		
		notifyObservers();
	}
	
	public void emptyAllCells() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				cells[x][y] = 0;
			}
		}
	}
	
	private int countLiveNeighbors(int xCoord, int yCoord) {
		int liveNeighbors = 0;
		
		for (int xDiff = -1; xDiff <= 1; xDiff++) {
			for (int yDiff = -1; yDiff <= 1; yDiff++) {
				if (xDiff == 0 && yDiff == 0) {
					continue;
				}
				
				int examinedX = xCoord+xDiff;
				int examinedY = yCoord+yDiff;
				
				if (examinedX < 0 || examinedX >= width) {
					if (torusMode) {
						examinedX = (examinedX+width) % width;
					} else {
						continue;
					}
				}
				
				if (examinedY < 0 || examinedY >= height) {
					if (torusMode) {
						examinedY = (examinedY+height) % height;
					} else {
						continue;
					}
				}
				
				liveNeighbors += cells[examinedX][examinedY];
			}
		}
		
		return liveNeighbors;
	}
	
	public void advanceOnce() {
		int[][] newCells = new int[width][height];
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int liveNeighbors = countLiveNeighbors(x, y);
				
				if (cells[x][y] == 1) {
					if (liveNeighbors >= surviveLow && liveNeighbors <= surviveHigh) {
						newCells[x][y] = 1;
					}
				} else {
					if (liveNeighbors >= birthLow && liveNeighbors <= birthHigh) {
						newCells[x][y] = 1;
					}
				}
			}			
		}
		
		cells = newCells;
		notifyObservers();
	}
	
	public void addObserver(ConwayObserver observer) {
		observers.add(observer);
	}
	
	public void removeObserver(ConwayObserver observer) {
		observers.remove(observer);
	}
	
	public void notifyObservers() {
		for (ConwayObserver observer : observers) {
			observer.updateCells(cells);
		}
	}
	
}
