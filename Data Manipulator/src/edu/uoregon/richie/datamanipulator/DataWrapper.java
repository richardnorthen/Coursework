package edu.uoregon.richie.datamanipulator;

import java.util.Arrays;
import java.util.List;

public class DataWrapper extends Thread {
	private DataListener listener;
	public enum SortType { UNSORTED, SELECTION, MERGE };
	private SortType sortType;
	private List<String> data;
	private long time;
	
	public DataWrapper(SortType sortType, int size) {
		this.sortType = sortType;
		data = Arrays.asList(new String[size]);
	}

	public DataWrapper(SortType sortType, List<String> data) {
		this.sortType = sortType;
		this.data = data;
	}

	@Override
	public void run() {
		long startTime = System.nanoTime();
		switch (sortType) {
		case UNSORTED:
			System.out.println(data.size());
			data = DataHandler.createRandomList(data.size());
			break;
		case SELECTION:
			data = DataHandler.getSelectionSortedCopy(data);
			break;
		case MERGE:
			data = DataHandler.getMergeSortedCopy(data);
			break;
		default:
			return;
		}
		this.time = (System.nanoTime() - startTime) / 1000000;
		listener.dataFinished(this);
		//MainFrame._progress.setIndeterminate(false);
		//MainFrame.getFrames()[0].repaint();
	}
	
	public String[] getDataAsArray() {
		String[] output = new String[data.size()];
		for (int i = 0; i < output.length; i++) {
			output[i] = data.get(i);
		}
		return output;
	}

	@Override
	public String toString() {
		switch (sortType) {
		case UNSORTED:
			return "Unsorted data (" + data.size() + ")";
		case SELECTION:
			return "Selection sorted data (" + data.size() + ")";
		case MERGE:
			return "Merge sorted data (" + data.size() + ")";
		default:
			return "Error with naming scheme.";
		}
	}

	
	public void setListener(DataListener listener) {
		this.listener = listener;
	}
	
	public DataListener getListener() {
		return listener;
	}
	
	public SortType getSortType() {
		return sortType;
	}

	public void setSortType(SortType sortType) {
		this.sortType = sortType;
	}

	public List<String> getData() {
		return data;
	}

	public void setData(List<String> data) {
		this.data = data;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
}