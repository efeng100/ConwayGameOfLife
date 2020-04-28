package game;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ConwayView extends JPanel implements ActionListener, CellListener, ChangeListener, ItemListener {
	
	public enum ButtonSource {UPDATE_DIMENSIONS, CLEAR_CELLS, FILL_RANDOMLY, ADVANCE_TICK, START, STOP}
	public enum SliderSource {WIDTH, HEIGHT, SURVIVE_LOW, SURVIVE_HIGH, BIRTH_LOW, BIRTH_HIGH, UPDATE_DELAY}
	public enum CheckBoxSource {TORUS_MODE}
	
	public static final int DEFAULT_BOARD_WIDTH = 30;
	public static final int DEFAULT_BOARD_HEIGHT = 20;
	public static final int DEFAULT_SURVIVE_LOW = 2;
	public static final int DEFAULT_SURVIVE_HIGH = 3;
	public static final int DEFAULT_BIRTH_LOW = 3;
	public static final int DEFAULT_BIRTH_HIGH = 3;
	public static final int DEFAULT_UPDATE_DELAY = 500;
	
	private CellBoardImpl board;
	private JSlider widthSlider;
	private JSlider heightSlider;
	private JSlider surviveLowSlider;
	private JSlider surviveHighSlider;
	private JSlider birthLowSlider;
	private JSlider birthHighSlider;
	private JSlider updateDelaySlider;
	private JButton updateDimensionsButton;
	private JButton clearCellsButton;
	private JButton fillRandomlyButton;
	private JButton advanceTickButton;
	private JButton startStopButton;
	private JCheckBox torusModeCheckBox;
	private JLabel widthSliderLabel;
	private JLabel heightSliderLabel;
	private JLabel surviveLowSliderLabel;
	private JLabel surviveHighSliderLabel;
	private JLabel birthLowSliderLabel;
	private JLabel birthHighSliderLabel;
	private JLabel updateDelaySliderLabel;
	private JLabel messageLabel;
	private List<ConwayViewListener> listeners;
	
	public ConwayView() {
		setLayout(new BorderLayout());
		
		board = new CellBoardImpl(DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT);
		add(board, BorderLayout.CENTER);
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());
		
		messageLabel = new JLabel("Welcome to Conway's Game of Life");
		bottomPanel.add(messageLabel, BorderLayout.SOUTH);
		
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
		
		JPanel dimensionsControlPanel = new JPanel();
		JPanel thresholdsControlPanel = new JPanel();
		JPanel miscControlPanel = new JPanel();
		JPanel updateControlPanel = new JPanel();
		
		// Dimensions Control Panel
		dimensionsControlPanel.setLayout(new BoxLayout(dimensionsControlPanel, BoxLayout.Y_AXIS));
		
		JPanel widthSliderPanel = new JPanel();
		widthSliderPanel.setLayout(new BorderLayout());
		widthSlider = new JSlider(10, 500, DEFAULT_BOARD_WIDTH);
		widthSliderLabel = new JLabel("   Width: " + widthSlider.getValue());
		widthSliderPanel.add(widthSliderLabel, BorderLayout.NORTH);
		widthSliderPanel.add(widthSlider, BorderLayout.CENTER);
		dimensionsControlPanel.add(widthSliderPanel);
		
		JPanel heightSliderPanel = new JPanel();
		heightSliderPanel.setLayout(new BorderLayout());
		heightSlider = new JSlider(10, 500, DEFAULT_BOARD_HEIGHT);
		heightSliderLabel = new JLabel("   Height: " + heightSlider.getValue());
		heightSliderPanel.add(heightSliderLabel, BorderLayout.NORTH);
		heightSliderPanel.add(heightSlider, BorderLayout.CENTER);
		dimensionsControlPanel.add(heightSliderPanel);
		
		updateDimensionsButton = new JButton("Update Dimensions");
		updateDimensionsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		dimensionsControlPanel.add(updateDimensionsButton);
		
		dimensionsControlPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		
		inputPanel.add(dimensionsControlPanel);
		
		// Thresholds Control Panel
		thresholdsControlPanel.setLayout(new BoxLayout(thresholdsControlPanel, BoxLayout.Y_AXIS));
		
		JPanel surviveLowSliderPanel = new JPanel();
		surviveLowSliderPanel.setLayout(new BorderLayout());
		surviveLowSlider = new JSlider(0, 8, DEFAULT_SURVIVE_LOW);
		surviveLowSliderLabel = new JLabel("   Survival Low Threshold: " + surviveLowSlider.getValue());
		surviveLowSliderPanel.add(surviveLowSliderLabel, BorderLayout.NORTH);
		surviveLowSliderPanel.add(surviveLowSlider, BorderLayout.CENTER);
		thresholdsControlPanel.add(surviveLowSliderPanel);
		
		JPanel surviveHighSliderPanel = new JPanel();
		surviveHighSliderPanel.setLayout(new BorderLayout());
		surviveHighSlider = new JSlider(0, 8, DEFAULT_SURVIVE_HIGH);
		surviveHighSliderLabel = new JLabel("   Survival High Threshold: " + surviveHighSlider.getValue());
		surviveHighSliderPanel.add(surviveHighSliderLabel, BorderLayout.NORTH);
		surviveHighSliderPanel.add(surviveHighSlider, BorderLayout.CENTER);
		thresholdsControlPanel.add(surviveHighSliderPanel);
		
		JPanel birthLowSliderPanel = new JPanel();
		birthLowSliderPanel.setLayout(new BorderLayout());
		birthLowSlider = new JSlider(0, 8, DEFAULT_BIRTH_LOW);
		birthLowSliderLabel = new JLabel("   Birth Low Threshold: " + birthLowSlider.getValue());
		birthLowSliderPanel.add(birthLowSliderLabel, BorderLayout.NORTH);
		birthLowSliderPanel.add(birthLowSlider, BorderLayout.CENTER);
		thresholdsControlPanel.add(birthLowSliderPanel);
		
		JPanel birthHighSliderPanel = new JPanel();
		birthHighSliderPanel.setLayout(new BorderLayout());
		birthHighSlider = new JSlider(0, 8, DEFAULT_BIRTH_HIGH);
		birthHighSliderLabel = new JLabel("   Birth High Threshold: " + birthHighSlider.getValue());
		birthHighSliderPanel.add(birthHighSliderLabel, BorderLayout.NORTH);
		birthHighSliderPanel.add(birthHighSlider, BorderLayout.CENTER);
		thresholdsControlPanel.add(birthHighSliderPanel);
		
		inputPanel.add(thresholdsControlPanel);
				
		// Misc Control Panel
		miscControlPanel.setLayout(new BoxLayout(miscControlPanel, BoxLayout.Y_AXIS));
		
		clearCellsButton = new JButton("Clear All Cells");
		clearCellsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		miscControlPanel.add(clearCellsButton);
		
		miscControlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		
		fillRandomlyButton = new JButton("Fill Cells Randomly");
		fillRandomlyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		miscControlPanel.add(fillRandomlyButton);
		
		miscControlPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		
		torusModeCheckBox = new JCheckBox("Torus Mode");
		torusModeCheckBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		miscControlPanel.add(torusModeCheckBox);
		
		inputPanel.add(miscControlPanel);
		
		// Update Control Panel
		updateControlPanel.setLayout(new BoxLayout(updateControlPanel, BoxLayout.Y_AXIS));
		
		updateControlPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		
		advanceTickButton = new JButton("Advance 1 Tick");
		advanceTickButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		updateControlPanel.add(advanceTickButton);
		
		updateControlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		
		JPanel updateDelaySliderPanel = new JPanel();
		updateDelaySliderPanel.setLayout(new BorderLayout());
		updateDelaySlider = new JSlider(10, 1000, DEFAULT_UPDATE_DELAY);
		updateDelaySliderLabel = new JLabel(String.format("   Update Delay: %d ms", updateDelaySlider.getValue()));
		updateDelaySliderPanel.add(updateDelaySliderLabel, BorderLayout.NORTH);
		updateDelaySliderPanel.add(updateDelaySlider, BorderLayout.CENTER);
		updateControlPanel.add(updateDelaySliderPanel);
		
		startStopButton = new JButton("Start");
		startStopButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		updateControlPanel.add(startStopButton);
		
		updateControlPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		
		inputPanel.add(updateControlPanel);
		
		bottomPanel.add(inputPanel, BorderLayout.CENTER);
		
		add(bottomPanel, BorderLayout.SOUTH);
		
		board.addListener(this);
		
		updateDimensionsButton.addActionListener(this);
		clearCellsButton.addActionListener(this);
		fillRandomlyButton.addActionListener(this);
		advanceTickButton.addActionListener(this);
		startStopButton.addActionListener(this);
		
		widthSlider.addChangeListener(this);
		heightSlider.addChangeListener(this);
		surviveLowSlider.addChangeListener(this);
		surviveHighSlider.addChangeListener(this);
		birthLowSlider.addChangeListener(this);
		birthHighSlider.addChangeListener(this);
		updateDelaySlider.addChangeListener(this);
		
		torusModeCheckBox.addItemListener(this);
		
		listeners = new ArrayList<ConwayViewListener>();
	}
	
	public boolean isCellFilledAt(int xCoord, int yCoord) {
		return board.getCellAt(xCoord, yCoord).isFilled();
	}
	
	public void fillCellAt(int xCoord, int yCoord) {
		board.getCellAt(xCoord, yCoord).fill();
	}
	
	public void emptyCellAt(int xCoord, int yCoord) {
		board.getCellAt(xCoord, yCoord).empty();
	}
	
	public void toggleCellFillAt(int xCoord, int yCoord) {
		board.getCellAt(xCoord, yCoord).toggleFill();
	}
	
	public void emptyAllCells() {
		for (int x = 0; x < board.getBoardWidth(); x++) {
			for (int y = 0; y < board.getBoardHeight(); y++) {
				board.getCellAt(x, y).empty();
			}
		}
	}
	
	public void replaceBoard(int width, int height) {
		remove(board);
		board = new CellBoardImpl(width, height);
		board.addListener(this);
		add(board, BorderLayout.CENTER);
	}
	
	public void setSurviveLowSliderValue(int value) {
		surviveLowSlider.setValue(value);
	}
	
	public void setSurviveHighSliderValue(int value) {
		surviveHighSlider.setValue(value);
	}
	
	public void setBirthLowSliderValue(int value) {
		birthLowSlider.setValue(value);
	}
	
	public void setBirthHighSliderValue(int value) {
		birthHighSlider.setValue(value);
	}
	
	public void setUpdateDelaySliderValue(int value) {
		updateDelaySlider.setValue(value);
	}
	
	public void setTorusModeCheckBoxSelected(boolean selected) {
		torusModeCheckBox.setSelected(selected);
	}
	
	public void updateSliderLabels() {
		widthSliderLabel.setText("   Width: " + widthSlider.getValue());
		heightSliderLabel.setText("   Height: " + heightSlider.getValue());
		surviveLowSliderLabel.setText("   Survival Low Threshold: " + surviveLowSlider.getValue());
		surviveHighSliderLabel.setText("   Survival High Threshold: " + surviveHighSlider.getValue());
		birthLowSliderLabel.setText("   Birth Low Threshold: " + birthLowSlider.getValue());
		birthHighSliderLabel.setText("   Birth High Threshold: " + birthHighSlider.getValue());
		updateDelaySliderLabel.setText(String.format("   Update Delay: %d ms", updateDelaySlider.getValue()));
	}
	
	public void toggleStartStopButton() {
		if (startStopButton.getText().equals("Start")) {
			startStopButton.setText("Stop");
		} else {
			startStopButton.setText("Start");
		}
	}
	
	public void setMessage(String message) {
		messageLabel.setText(message);
	}
	
	public void clearMessage() {
		messageLabel.setText("");
	}
	
	public void addListener(ConwayViewListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(ConwayViewListener listener) {
		listeners.remove(listener);
	}
	
	public void cellClicked(Cell cell) {
		fireEvent(new CellClickedEvent(cell.getXCoord(), cell.getYCoord(), cell.isFilled()));
	}
	
	public void actionPerformed(ActionEvent event) {
		JButton button = (JButton) event.getSource();
		ButtonSource buttonSource = null;
		
		switch (button.getText()) {
		case "Update Dimensions":
			buttonSource = ButtonSource.UPDATE_DIMENSIONS;
			break;
		case "Clear All Cells":
			buttonSource = ButtonSource.CLEAR_CELLS;
			break;
		case "Fill Cells Randomly":
			buttonSource = ButtonSource.FILL_RANDOMLY;
			break;
		case "Advance 1 Tick":
			buttonSource = ButtonSource.ADVANCE_TICK;
			break;
		case "Start":
			buttonSource = ButtonSource.START;
			break;
		case "Stop":
			buttonSource = ButtonSource.STOP;
			break;
		}
		
		fireEvent(new ButtonEvent(buttonSource));		
	}
	
	public void stateChanged(ChangeEvent event) {
		JSlider slider = (JSlider) event.getSource();
		SliderSource sliderSource = null;
		
		if (slider.equals(widthSlider)) {
			sliderSource = SliderSource.WIDTH;
		} else if (slider.equals(heightSlider)) {
			sliderSource = SliderSource.HEIGHT;
		} else if (slider.equals(surviveLowSlider)) {
			sliderSource = SliderSource.SURVIVE_LOW;
		} else if (slider.equals(surviveHighSlider)) {
			sliderSource = SliderSource.SURVIVE_HIGH;
		} else if (slider.equals(birthLowSlider)) {
			sliderSource = SliderSource.BIRTH_LOW;
		} else if (slider.equals(birthHighSlider)) {
			sliderSource = SliderSource.BIRTH_HIGH;
		} else if (slider.equals(updateDelaySlider)) {
			sliderSource = SliderSource.UPDATE_DELAY;
		}
		
		fireEvent(new SliderEvent(sliderSource, slider.getValue()));
	}
	
	public void itemStateChanged(ItemEvent event) {
		JCheckBox checkBox = (JCheckBox) event.getSource();
		CheckBoxSource checkBoxSource = null;
		
		switch (checkBox.getText()) {
		case "Torus Mode":
			checkBoxSource = CheckBoxSource.TORUS_MODE;
		}
		
		fireEvent(new CheckBoxEvent(checkBoxSource,checkBox.isSelected()));
	}
	
	private void fireEvent(ConwayViewEvent event) {
		for (ConwayViewListener listener : listeners) {
			listener.handleConwayViewEvent(event);
		}
	}
	
}
