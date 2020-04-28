package game;

import javax.swing.SwingUtilities;

public class BackgroundTimer extends Thread {
	
	private ConwayController controller;
	private int updateDelay;
	private boolean done;
	
	public BackgroundTimer(ConwayController controller, int updateDelay) {
		this.controller = controller;
		this.updateDelay = updateDelay;
		done = false;
	}

	public void halt() {
		done = true;
	}
	
	public void run() {
		while (!done) {
			try {
				Thread.sleep(updateDelay);
			} catch (InterruptedException e) {
			}
			
			SwingUtilities.invokeLater(new ConwayUpdater(controller));
		}
	}
}

class ConwayUpdater implements Runnable {

	private ConwayController controller;
	
	public ConwayUpdater(ConwayController controller) {
		this.controller = controller;
	}
	
	@Override
	public void run() {
		controller.advanceOnce();
	}
}
