package interfaces.mainprogram;
import org.w3c.dom.NodeList;
public class Cache {
	private static Cache cache;
	private final static int MAX_SIZE = 5;
	private NodeList[] cacheTables;
	private String [] cacheTablesNames;
	private int[] numberOfrequestTable;
	private Cache() {
		cacheTables = new NodeList[MAX_SIZE];
		cacheTablesNames = new String[MAX_SIZE];
		numberOfrequestTable = new int[MAX_SIZE];
	}
	public static Cache getInstance() {
		if(cache == null) {
			cache = new Cache();
		} 
		return cache;
	}
	public NodeList fetchTable(String tableName) {
		// TODO Auto-generated method stub
		for (int i = 0; i < MAX_SIZE; i++) {
			if (cacheTablesNames[i] == null) {
				return null;
			}else if (cacheTablesNames[i].equalsIgnoreCase(tableName)) {
				numberOfrequestTable[i]++;
				return cacheTables[i];
			}
		}
		return null;
	}
	public void addTable(NodeList list, String tableName) {
		// TODO Auto-generated method stub
		int minIndex = findMinimumRequestIndex();
		replaceMinimumRequestTableWithNewTable(list, tableName, minIndex);
	}
	private int findMinimumRequestIndex() {
		// TODO Auto-generated method stub
		int min = numberOfrequestTable[0];
		int index = 0;
		for(int i = 1; i < MAX_SIZE; i++) {
			if(numberOfrequestTable[i] < min) {
			min = numberOfrequestTable[i];
			index = i;
			}
		}
		return index;
	}
	private void replaceMinimumRequestTableWithNewTable(NodeList list, String tableName, int minIndex) {
		// TODO Auto-generated method stub
		numberOfrequestTable[minIndex] = 0;
		cacheTablesNames[minIndex] = tableName;
		cacheTables[minIndex] = list;
	}
	public void update(NodeList oldList, NodeList newList) {
		// TODO Auto-generated method stub
		for(int i = 0; i < MAX_SIZE; i++) {
			if (cacheTables[i].equals(oldList)) {
				cacheTables[i] = newList;
				break;
			}
		}
	}
	
}
