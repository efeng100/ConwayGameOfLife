package game;

public interface Cell {

	int getXCoord();
	int getYCoord();
	CellBoard getBoard();
	
	boolean isFilled();
	void fill();
	void empty();
	default void toggleFill() {
		if (isFilled()) {
			empty();
		} else {
			fill();
		}
	}
	
	void addListener(CellListener listener);
	void removeListener(CellListener listener);
	
}
