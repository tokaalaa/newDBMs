package strategies;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.w3c.dom.NodeList;

import interfaces.mainprogram.CreateDTD;
import interfaces.mainprogram.MyDataBase;

public abstract class SelectStrategy {

	protected String tableName;
	public SelectStrategy(String tableName) {
		this.tableName = tableName;
	}
	public abstract Object[][] selectFrom(NodeList list);
	
 protected boolean tableIsExist(String tableName) {
		 
		 Path p = Paths.get("main folder" + System.getProperty("file.separator") + MyDataBase.currentDatabase +
					System.getProperty("file.separator") + tableName + ".xml");
		 return Files.exists(p);
	
	 }
	protected Object[][] convertToArray2d(ArrayList<ArrayList<String>> a){
		Object[][] array = new Object[a.size()][a.get(0).size()];
		String[][] table = CreateDTD.getTable();
		for (int i = 0; i < a.size(); i++) {
			ArrayList<String> row = a.get(i);
			for(int j = 0; j < row.size(); j++) {
				if (table[j][1].equalsIgnoreCase("int")) {
				array[i][j] = Integer.parseInt(row.get(j));
				} else {
					array[i][j] = row.get(j);
				}
			}
		}
		return array;	
	}
	protected boolean checkOperation(String operation, String textContent, String valueCondition) {
		// TODO Auto-generated method stub
		int oldValue = Integer.parseInt(textContent);
		int condition = Integer.parseInt(valueCondition);
		switch (operation) {
		case ">": return oldValue > condition; 
		case "<": return oldValue < condition;
		case "=": return oldValue == condition;
		default : return false;
		}
	}
}
