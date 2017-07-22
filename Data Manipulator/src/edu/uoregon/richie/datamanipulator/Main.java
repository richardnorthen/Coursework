package edu.uoregon.richie.datamanipulator;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/*
 * The only main issue is having the frame "refresh" when content is changed or added.
 * I have some workarounds that are probably not ideal but it is what I was able to come up with.
 * However, all the components /will/ be repainted if the ComboBox or Remove buttons are used.
 * The progress bar ends prematurely if both searches are conducted at once (but both searches will still complete).
 */

/**
 * The starting point of the application. <code>Main</code> extends
 * {@link JFrame} and is used to setup the properties and layout of the window,
 * as well as initializing and adding the GUI components.
 * @author Richard Northen
 * @version 1.0
 */
@SuppressWarnings("serial")
public class Main extends JFrame implements ActionListener, DataListener {
	// GUI components
	private JPanel generatePanel,
			sortPanel;
	private JButton addButton,
			selectionSortButton,
			mergeSortButton,
			removeButton;
	private JTextField sizeTextField,
			timeTextField;
	private JComboBox<DataWrapper> dataSelectorComboBox;
	private JList<String> dataDisplayList;
	private JScrollPane dataDisplayScrollPane;
	private JLabel timeLabel,
			sortedLabel;
	private JCheckBox sortedCheckBox;
	private static JProgressBar progressBar;
	
	/**
	 * Initializes the Main class and displays it.
	 * @param args  Unused
	 */
	public static void main(String[] args) {
		Main frame = new Main();
		frame.setVisible(true);
	}

	/**
	 * Creates a new <code>Main</code> that the application will use with the
	 * required features.
	 */
	public Main() {
		// frame properties
		super("DataManipulator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(800, 600));
		setResizable(true);

		// GBL layout
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		setLayout(layout);

		// generate panel
		generatePanel = new JPanel();
		generatePanel.setBorder(
				BorderFactory.createTitledBorder("Generate data"));
		generatePanel.setLayout(layout);

		// generate button
		addButton = new JButton("Add");
		addButton.addActionListener(this);
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(4, 4, 4, 4);
		generatePanel.add(addButton, c);

		// size text field
		sizeTextField = new JTextField();
		// only allow 5-digit integers
		sizeTextField.setDocument(new PlainDocument() {
			@Override
			public void insertString(int offs, String str, AttributeSet a)
					throws BadLocationException {
				if (str == null)
					return;
				if (getLength() + str.length() <= 5 && str.matches("[0-9]+"))
					super.insertString(offs, str, a);
			}
		});
		sizeTextField.setHorizontalAlignment(JTextField.CENTER);
		sizeTextField.setPreferredSize(new Dimension(60, 25));
		sizeTextField.setText("50000");
		c.gridx = 1;
		generatePanel.add(sizeTextField, c);

		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridheight = 2;
		c.gridx = 0;
		add(generatePanel, c);
		// generate panel

		// sort panel
		sortPanel = new JPanel();
		sortPanel.setBorder(BorderFactory.createTitledBorder("Sort data"));
		sortPanel.setLayout(layout);

		// selection sort button
		selectionSortButton = new JButton("Selection sort");
		selectionSortButton.addActionListener(this);
		selectionSortButton.setHorizontalAlignment(SwingConstants.LEADING);
		c.anchor = GridBagConstraints.CENTER;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 3;
		sortPanel.add(selectionSortButton, c);

		// merge sort button
		mergeSortButton = new JButton("Merge sort");
		mergeSortButton.addActionListener(this);
		mergeSortButton.setHorizontalAlignment(SwingConstants.LEADING);
		c.gridy = 4;
		sortPanel.add(mergeSortButton, c);

		c.anchor = GridBagConstraints.FIRST_LINE_START;
		add(sortPanel, c);
		// sort buttons panel

		// data selector combo box
		dataSelectorComboBox = new JComboBox<DataWrapper>();
		dataSelectorComboBox.addActionListener(this);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(12, 4, 4, 4);
		c.weightx = 1;
		add(dataSelectorComboBox, c);
		
		// data display list
		dataDisplayList = new JList<String>();
		dataDisplayList.setLayoutOrientation(JList.VERTICAL);
		dataDisplayList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// data display scroll pane
		dataDisplayScrollPane = new JScrollPane(dataDisplayList);
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		c.gridheight = 4;
		c.gridwidth = 6;
		c.gridy = 1;
		c.insets = new Insets(4, 4, 4, 4);
		c.weightx = 1;
		c.weighty = 1;
		add(dataDisplayScrollPane, c);

		// remove button
		removeButton = new JButton("Remove");
		removeButton.addActionListener(this);
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.NONE;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.gridx = 2;
		c.gridy = 0;
		c.insets = new Insets(12, 4, 4, 4);
		c.weightx = 0;
		c.weighty = 0;
		add(removeButton, c);

		// time label
		timeLabel = new JLabel("Time:");
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 3;
		add(timeLabel, c);

		// time text field
		timeTextField = new JTextField("0");
		timeTextField.setEditable(false);
		timeTextField.setHorizontalAlignment(JTextField.CENTER);
		timeTextField.setPreferredSize(new Dimension(60, 25));
		c.gridx = 4;
		add(timeTextField, c);

		// sorted label
		sortedLabel = new JLabel("Is sorted:");
		c.gridx = 5;
		add(sortedLabel, c);

		// sorted check box
		sortedCheckBox = new JCheckBox();
		sortedCheckBox.setEnabled(false);
		c.gridx = 6;
		add(sortedCheckBox, c);

		// progress bar
		progressBar = new JProgressBar();
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 7;
		c.gridx = 0;
		c.gridy = 5;
		c.insets = new Insets(4, 4, 4, 4);
		add(progressBar, c);
		
		pack();
	}

	/**
	 * Determines the source of the {@link ActionEvent} and performs the
	 * appropriate action.
	 * @param event  The {@link ActionEvent} that occurred
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		// perform appropriate action
		if (source.equals(addButton)) {
			try {
				int size = Integer.parseInt(sizeTextField.getText());
				if (size <= 0) {
					System.out.println("The size must be larger than 0!");
				} else {
					// create the data, store it, and configure the buttons
					System.out.println(size);
					DataWrapper data = new DataWrapper(DataWrapper.SortType.UNSORTED, size);
					data.setListener(this);
					data.start();
				}
			} catch (NumberFormatException e) {
				// just in case Document fails
				
			}
		} else if (source.equals(selectionSortButton)) {
			DataWrapper selected = dataSelectorComboBox.getItemAt(0);
			DataWrapper data = new DataWrapper(DataWrapper.SortType.SELECTION, selected.getData());
			//_progress.setIndeterminate(true);
			data.setListener(this);
			data.start();
		} else if (source.equals(mergeSortButton)) {
			DataWrapper selected = dataSelectorComboBox.getItemAt(0);
			DataWrapper data = new DataWrapper(DataWrapper.SortType.MERGE, selected.getData());
			//_progress.setIndeterminate(true);
			data.setListener(this);
			data.start();
		} else if (source.equals(dataSelectorComboBox)) {
			DataWrapper selected = (DataWrapper) dataSelectorComboBox.getSelectedItem();
			setSelectedData(selected);
		} else if (source.equals(removeButton)) {
			DataWrapper selected = (DataWrapper) dataSelectorComboBox.getSelectedItem();
			removeData(selected);
		}	
	}

	@Override
	public void dataFinished(DataWrapper data) {
		addData(data);
		System.out.println(data.toString());
	}
	
	public void addData(DataWrapper data) {
		dataSelectorComboBox.addItem(data);
		setSelectedData(data);
	}
	
	public void removeData(DataWrapper data) {
		dataSelectorComboBox.removeItem(data);
		setSelectedData(null);
	}

	public void setSelectedData(DataWrapper data) {
		dataSelectorComboBox.setSelectedItem(data);
		if (data == null) {
			addButton.setEnabled(true);
			selectionSortButton.setEnabled(false);
			mergeSortButton.setEnabled(false);
			timeTextField.setText("n/a");
			sortedCheckBox.setSelected(false);
			dataDisplayList.setListData(new String[0]);
			return;
		}
		switch (data.getSortType()) {
		case UNSORTED:
			selectionSortButton.setEnabled(true);
			mergeSortButton.setEnabled(true);
			sortedCheckBox.setSelected(false);
			break;
		case SELECTION:
			selectionSortButton.setEnabled(false);
			mergeSortButton.setEnabled(true);
			sortedCheckBox.setSelected(true);
			break;
		case MERGE:
			selectionSortButton.setEnabled(true);
			mergeSortButton.setEnabled(false);
			sortedCheckBox.setSelected(true);
			break;
		}
		dataDisplayList.setListData(data.getDataAsArray());
		timeTextField.setText(data.getTime() + " ms");
	}
}