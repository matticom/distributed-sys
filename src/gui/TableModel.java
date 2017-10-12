package gui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import models.PN_Entry;

public class TableModel extends AbstractTableModel {

	private List<PN_Entry> phoneBookList;
	private String[] columns;

	public TableModel(List<PN_Entry> phoneBookList){
		    this.phoneBookList = phoneBookList ;
		    columns = new String[]{"Name", "Telefonnummer" };
		  }

	@Override
	public int getColumnCount() {
		return columns.length;
	}

	@Override
	public Object getValueAt(int row, int col) {
		PN_Entry entry = phoneBookList.get(row);
		switch (col) {
		case 0:
			return entry.getName();
		case 1:
			return entry.getPhoneNumber();
		default:
			return null;
		}
	}
	
	@Override
	public int getRowCount() {
		return phoneBookList.size();
	}
	
	@Override
	public String getColumnName(int col) {
	    return columns[col] ;
	}
}
