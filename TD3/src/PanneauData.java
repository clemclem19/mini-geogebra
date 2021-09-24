import javax.swing.table.AbstractTableModel;

public class PanneauData extends AbstractTableModel {
	private static final long serialVersionUID = 1L;

	private Polynome parent;
	private String[] columnNames = {"X", "Y"};
	private int min, max;
	
	//Fonctions basiques
	public PanneauData(Polynome parent) {
		super();
		this.parent = parent;
		this.min = 0;
		this.max = 0;
	}
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return abs(min-max)+1;
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		if(col == 0)
			return min+row;
		
		else 
			return parent.result(min+row);
	}
	
	//Prends les nouvelles bornes et mets a jour le tableau
	public void setBounds(int min, int max) {
		this.min = min;
		this.max = max;
		this.fireTableDataChanged();
	}
	
	//Renvoie la valeur absolue de l'argument
	private int abs(int i) {
		if(i<0)
			i *= -1;

		return i;
	}
}
