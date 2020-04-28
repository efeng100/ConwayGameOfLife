package game;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		ConwayModel model = new ConwayModel();
		ConwayView view = new ConwayView();
		
		JFrame mainFrame = new JFrame();
		mainFrame.setTitle("Conway's Game of Life");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mainFrame.setContentPane(view);
		
		ConwayController controller = new ConwayController(model, view, mainFrame);
		
		mainFrame.pack();
		mainFrame.setVisible(true);
	}
	
}
