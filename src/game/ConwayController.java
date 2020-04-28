package game;

import javax.swing.JFrame;

public class ConwayController implements ConwayObserver, ConwayViewListener {

	private ConwayModel model;
	private ConwayView view;
	private JFrame mainFrame;
	
	private boolean currentlyAdvancing;
	private int newWidth;
	private int newHeight;
	private int updateDelay;
	private BackgroundTimer backgroundTimer;
	
	public ConwayController(ConwayModel model, ConwayView view, JFrame mainFrame) {
		this.model = model;
		this.view = view;
		this.mainFrame = mainFrame;
		
		model.addObserver(this);
		view.addListener(this);
		
		newWidth = ConwayView.DEFAULT_BOARD_WIDTH;
		newHeight = ConwayView.DEFAULT_BOARD_HEIGHT;
		currentlyAdvancing = false;
		updateDelay = ConwayView.DEFAULT_UPDATE_DELAY;
	}
	
	public void advanceOnce() {
		model.advanceOnce();
	}
	
	public void stopAutomaticAdvancement() {
		if (currentlyAdvancing) {
			backgroundTimer.halt();
			try {
				backgroundTimer.join();
			} catch (InterruptedException exception) {
			}
			
			view.toggleStartStopButton();
			view.setMessage("Automatic advancement disabled");
			currentlyAdvancing = false;
		} else {
			view.clearMessage();
		}
	}
	
	@Override
	public void handleConwayViewEvent(ConwayViewEvent event) {
		if (event.isCellClickedEvent()) {
			if (currentlyAdvancing) {
				view.setMessage("Stop automatic advancement (bottom right button) to toggle cells.");
			} else {
				CellClickedEvent cellClickedEvent = (CellClickedEvent) event;
				
				int x = cellClickedEvent.getCellXCoord();
				int y = cellClickedEvent.getCellYCoord();
				model.toggleCellFillAt(x, y);
				view.toggleCellFillAt(x, y);
				
				view.clearMessage();
			}
		} else if (event.isButtonEvent()) {
			ButtonEvent buttonEvent = (ButtonEvent) event;
			
			switch (buttonEvent.getButtonSource()) {
			case UPDATE_DIMENSIONS:
				stopAutomaticAdvancement();
				model.setWidthAndHeight(newWidth, newHeight);
				view.replaceBoard(newWidth, newHeight);
				mainFrame.pack();
				break;
			case CLEAR_CELLS:
				stopAutomaticAdvancement();
				model.emptyAllCells();
				view.emptyAllCells();
				break;
			case FILL_RANDOMLY:
				stopAutomaticAdvancement();
				model.fillCellsRandomly();
				break;
			case ADVANCE_TICK:
				if (currentlyAdvancing) {
					view.setMessage("Stop automatic advancement (bottom right button) to advance by one tick.");
				} else {
					advanceOnce();
					view.clearMessage();
				}
				break;
			case START:
				currentlyAdvancing = true;
				backgroundTimer = new BackgroundTimer(this, updateDelay);
				backgroundTimer.start();
				view.toggleStartStopButton();
				view.setMessage("Automatic advancement enabled");
				break;
			case STOP:
				stopAutomaticAdvancement();
				break;
			}
		} else if (event.isSliderEvent()) {
			SliderEvent sliderEvent = (SliderEvent) event;
			
			switch (sliderEvent.getSliderSource()) {
			case WIDTH:
				newWidth = sliderEvent.getValue();
				view.setMessage("Press Update Dimensions to load new width");
				break;
			case HEIGHT:
				newHeight = sliderEvent.getValue();
				view.setMessage("Press Update Dimensions to load new height");
				break;
			case SURVIVE_LOW:
				if (currentlyAdvancing) {
					view.setMessage("Stop automatic advancement (bottom right button) to adjust Survival Low Threshold.");
					view.setSurviveLowSliderValue(model.getSurviveLow());
				} else {
					model.setSurviveLow(sliderEvent.getValue());
					if (model.getSurviveLow() > model.getSurviveHigh()) {
						view.setMessage("Warning: Survival Low Threshold is now greater than Survival High Threshold. "
								+ "No living cells will survive.");
					} else {
						view.clearMessage();
					}
				}
				break;
			case SURVIVE_HIGH:
				if (currentlyAdvancing) {
					view.setMessage("Stop automatic advancement (bottom right button) to adjust Survival High Threshold.");
					view.setSurviveHighSliderValue(model.getSurviveHigh());
				} else {
					model.setSurviveHigh(sliderEvent.getValue());
					if (model.getSurviveLow() > model.getSurviveHigh()) {
						view.setMessage("Warning: Survival High Threshold is now less than Survival Low Threshold. "
								+ "No living cells will survive.");
					} else {
						view.clearMessage();
					}
				}
				break;
			case BIRTH_LOW:
				if (currentlyAdvancing) {
					view.setMessage("Stop automatic advancement (bottom right button) to adjust Birth Low Threshold.");
					view.setBirthLowSliderValue(model.getBirthLow());
				} else {
					model.setBirthLow(sliderEvent.getValue());
					if (model.getBirthLow() > model.getBirthHigh()) {
						view.setMessage("Warning: Birth Low Threshold is now greater than Birth High Threshold. "
								+ "No new cells will be born.");
					} else {
						view.clearMessage();
					}
				}
				break;
			case BIRTH_HIGH:
				if (currentlyAdvancing) {
					view.setMessage("Stop automatic advancement (bottom right button) to adjust Birth High Threshold.");
					view.setBirthHighSliderValue(model.getBirthHigh());
				} else {
					model.setBirthHigh(sliderEvent.getValue());
					if (model.getBirthLow() > model.getBirthHigh()) {
						view.setMessage("Warning: Birth High Threshold is now less than Birth Low Threshold. "
								+ "No new cells will be born.");
					} else {
						view.clearMessage();
					}
				}
				break;
			case UPDATE_DELAY:
				if (currentlyAdvancing) {
					view.setMessage("Stop automatic advancement (bottom right button) to adjust Update Delay.");
					view.setUpdateDelaySliderValue(updateDelay);
				} else {
					updateDelay = sliderEvent.getValue();
					view.clearMessage();
				}
				break;
			}
			
			view.updateSliderLabels();
		} else if (event.isCheckBoxEvent()) {
			CheckBoxEvent checkBoxEvent = (CheckBoxEvent) event;
			
			switch (checkBoxEvent.getCheckBoxSource()) {
			case TORUS_MODE:
				if (currentlyAdvancing) {
					view.setMessage("Stop automatic advancement (bottom right button) to toggle Torus Mode.");
					view.setTorusModeCheckBoxSelected(model.getTorusMode());
				} else {
					model.toggleTorusMode();
					view.clearMessage();
				}
				break;
			}
		}
		
		mainFrame.pack();
	}

	@Override
	public void updateCells(int[][] updatedCells) {
		for (int x = 0; x < model.getWidth(); x++) {
			for (int y = 0; y < model.getHeight(); y++) {
				if (updatedCells[x][y] == 1) {
					view.fillCellAt(x, y);
				} else {
					view.emptyCellAt(x, y);
				}
			}
		}
	}

}
