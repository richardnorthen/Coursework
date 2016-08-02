import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

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

/**
 * @author Richard Northen
 * CIS 212 - Spring 2014
 * Notes for instructos:
 * The only main issue is having the frame "refresh" when content is changed or added.
 * I have some workarounds that are probably not ideal but it is what I was able to come up with.
 * However, all the components /will/ be repainted if the ComboBox or Remove buttons are used.
 * The progress bar ends prematurely if both searches are conducted at once (but both searches will still complete).
 */
public class Main extends JFrame implements ActionListener {
	// GUI components
	private JPanel generatePanel, sortPanel;
	private JButton generateButton, selectionSortButton, mergeSortButton, removeButton;
	private JTextField sizeTextField, timeTextField;
	private JComboBox<DataWrapper> dataSelectorComboBox;
	private JList<String> dataDisplayList;
	private JScrollPane dataDisplayScroll;
	private JLabel timeLabel, sortedLabel;
	private JCheckBox sortedCheckBox;
	private static JProgressBar progressBar;
	
	public static void main(String[] args) {
		// initialize and display the window
		Main frame = new Main();
		frame.setVisible(true);
	}

	public Main() {
		// frame properties
		super("Data Generator and Sorter");
		setMinimumSize(new Dimension(800, 600));
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// GBL layout
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		setLayout(layout);

		// generate panel
		generatePanel = new JPanel();
		generatePanel.setBorder(BorderFactory.createTitledBorder("Generate data"));
		generatePanel.setLayout(layout);

		generateButton = new JButton("Add");
		generateButton.addActionListener(this);
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(4, 4, 4, 4);
		generatePanel.add(generateButton, c);

		sizeTextField = new JTextField();
		sizeTextField.setHorizontalAlignment(JTextField.CENTER);
		sizeTextField.setPreferredSize(new Dimension(60, 25));
		// only allow 5-digit integers
		sizeTextField.setDocument(new PlainDocument() {
			@Override
			public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
				if (str == null)
					return;
				if (getLength() + str.length() <= 5 && str.matches("[0-9]+"))
					super.insertString(offs, str, a);
			}
		});
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

		selectionSortButton = new JButton("Selection sort");
		selectionSortButton.setHorizontalAlignment(SwingConstants.LEADING);
		selectionSortButton.addActionListener(this);
		c.anchor = GridBagConstraints.CENTER;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 3;
		sortPanel.add(selectionSortButton, c);

		mergeSortButton = new JButton("Merge sort");
		mergeSortButton.setHorizontalAlignment(SwingConstants.LEADING);
		mergeSortButton.addActionListener(this);
		c.gridy = 4;
		sortPanel.add(mergeSortButton, c);

		c.anchor = GridBagConstraints.FIRST_LINE_START;
		add(sortPanel, c);
		// sort buttons panel

		// data selector
		dataSelectorComboBox = new JComboBox<DataWrapper>();
		dataSelectorComboBox.addActionListener(this);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(12, 4, 4, 4);
		c.weightx = 1;
		add(dataSelectorComboBox, c);
		
		// data displayer
		dataDisplayList = new JList<String>();
		dataDisplayList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		dataDisplayList.setLayoutOrientation(JList.VERTICAL);
		dataDisplayScroll = new JScrollPane(dataDisplayList);
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		c.gridheight = 4;
		c.gridwidth = 6;
		c.gridy = 1;
		c.insets = new Insets(4, 4, 4, 4);
		c.weightx = 1;
		c.weighty = 1;
		add(dataDisplayScroll, c);

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

		// stats
		timeLabel = new JLabel("Time:");
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 3;
		add(timeLabel, c);

		timeTextField = new JTextField("0");
		timeTextField.setEditable(false);
		timeTextField.setHorizontalAlignment(JTextField.CENTER);
		timeTextField.setPreferredSize(new Dimension(60, 25));
		c.gridx = 4;
		add(timeTextField, c);

		sortedLabel = new JLabel("Is sorted:");
		c.gridx = 5;
		add(sortedLabel, c);

		sortedCheckBox = new JCheckBox();
		sortedCheckBox.setEnabled(false);
		c.gridx = 6;
		add(sortedCheckBox, c);

		progressBar = new JProgressBar();
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 7;
		c.gridx = 0;
		c.gridy = 5;
		c.insets = new Insets(4, 4, 4, 4);
		add(progressBar, c);
		// stats
		
		pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source.equals(generateButton)) {
			// input validation
			int size = Integer.parseInt(sizeTextField.getText());
			if (size <= 0) {
				System.out.println("The size must be larger than 0!");
			} else {
				// create the data, store it, and configure the buttons
				DataWrapper data = new DataWrapper(DataWrapper.SortType.UNSORTED, size);
				data.start();
				try {
					data.join();
					System.out.println("start");
					addData(data);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		} else if (source.equals(selectionSortButton)) {
			DataWrapper selected = dataSelectorComboBox.getItemAt(0);
			DataWrapper data = new DataWrapper(DataWrapper.SortType.SELECTION, selected.getData());
			//_progress.setIndeterminate(true);
			data.start();
			addData(data);
		} else if (source.equals(mergeSortButton)) {
			DataWrapper selected = dataSelectorComboBox.getItemAt(0);
			DataWrapper data = new DataWrapper(DataWrapper.SortType.MERGE, selected.getData());
			//_progress.setIndeterminate(true);
			data.start();
			addData(data);
		} else if (source.equals(dataSelectorComboBox)) {
			DataWrapper selected = (DataWrapper) dataSelectorComboBox.getSelectedItem();
			setSelectedData(selected);
		} else if (source.equals(removeButton)) {
			DataWrapper selected = (DataWrapper) dataSelectorComboBox.getSelectedItem();
			removeData(selected);
		}	
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
			generateButton.setEnabled(true);
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
		System.out.println(DataHandler.createRandomList(2).toString());
		dataDisplayList.setListData(data.getDataAsArray());
		timeTextField.setText(data.getTime() + " ms");
	}
}