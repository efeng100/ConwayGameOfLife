package game;

abstract public class ConwayViewEvent {

	public boolean isCellClickedEvent() {
		return false;
	}
	
	public boolean isButtonEvent() {
		return false;
	}
	
	public boolean isSliderEvent() {
		return false;
	}
	
	public boolean isCheckBoxEvent() {
		return false;
	}
	
}

class CellClickedEvent extends ConwayViewEvent {
	
	private int cellXCoord;
	private int cellYCoord;
	private boolean cellIsFilled;
	
	public CellClickedEvent(int cellXCoord, int cellYCoord, boolean cellIsFilled) {
		this.cellXCoord = cellXCoord;
		this.cellYCoord = cellYCoord;
		this.cellIsFilled = cellIsFilled;
	}
	
	public int getCellXCoord() {
		return cellXCoord;
	}
	
	public int getCellYCoord() {
		return cellYCoord;
	}
	
	public boolean getCellIsFilled() {
		return cellIsFilled;
	}
	
	public boolean isCellClickedEvent() {
		return true;
	}
	
}

class ButtonEvent extends ConwayViewEvent {
	
	private ConwayView.ButtonSource buttonSource;
	
	public ButtonEvent(ConwayView.ButtonSource buttonSource) {
		this.buttonSource = buttonSource;
	}
	
	public ConwayView.ButtonSource getButtonSource() {
		return buttonSource;
	}
	
	public boolean isButtonEvent() {
		return true;
	}
	
}

class SliderEvent extends ConwayViewEvent {
	
	private ConwayView.SliderSource sliderSource;
	private int value;
	
	public SliderEvent(ConwayView.SliderSource sliderSource, int value) {
		this.sliderSource = sliderSource;
		this.value = value;
	}
	
	public ConwayView.SliderSource getSliderSource() {
		return sliderSource;
	}
	
	public int getValue() {
		return value;
	}
	
	public boolean isSliderEvent() {
		return true;
	}
	
}

class CheckBoxEvent extends ConwayViewEvent {
	
	private ConwayView.CheckBoxSource checkBoxSource;
	private boolean isChecked;
	
	public CheckBoxEvent(ConwayView.CheckBoxSource checkBoxSource, boolean isChecked) {
		this.checkBoxSource = checkBoxSource;
		this.isChecked = isChecked;
	}
	
	public ConwayView.CheckBoxSource getCheckBoxSource() {
		return checkBoxSource;
	}
	
	public boolean isChecked() {
		return isChecked;
	}
	
	public boolean isCheckBoxEvent() {
		return true;
	}
	
}