package game;

public interface CellBoard {

	int getBoardWidth();
	int getBoardHeight();
	
	Cell getCellAt(int x, int y);
	
	void addListener(CellListener listener);
	void removeListener(CellListener listener);
	
}
