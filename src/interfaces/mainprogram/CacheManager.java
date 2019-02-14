package interfaces.mainprogram;
import org.w3c.dom.NodeList;

import interfaces.Observer;
public class CacheManager implements Observer{
private static CacheManager cacheManager;
private Cache cache;
private ReadAndWriteToXml xml;
private String currentTableName;
private String currentDatabaseName;
private NodeList currentList;
private CacheManager() {
	cache = Cache.getInstance();
	xml = new ReadAndWriteToXml();
}
	public static CacheManager getInstance() {
	if(cacheManager == null) {
		cacheManager = new CacheManager();
	} 
	return cacheManager;
	}
	
	public void checkCurrentTable(String tableName) {
		if (currentDatabaseName == null) {
			currentDatabaseName = MyDataBase.currentDatabase;
		}
		if (currentTableName == null) {
			currentTableName = tableName;
		} else if (!tableName.equalsIgnoreCase(currentTableName)
				|| !currentDatabaseName.equalsIgnoreCase(MyDataBase.currentDatabase)) {
			xml.writeToXml(currentTableName, currentDatabaseName,currentList);
			currentTableName = tableName;
			currentDatabaseName = MyDataBase.currentDatabase;
		}
	}

	public NodeList fetchTable(String tableName) {
		currentList = cache.fetchTable(tableName);
		if (currentList != null) {
			return currentList;
		} else {
			currentList = xml.readXml(tableName);
		cache.addTable(currentList, tableName);
		return cache.fetchTable(tableName);
		}
	}
	@Override
	public void update(NodeList oldList, NodeList newList) {
		// TODO Auto-generated method stub
		cache.update(oldList, newList);
		currentList = newList;
	}
	public void close() {
		if (currentList != null)
		xml.writeToXml(currentTableName, currentDatabaseName,currentList);
	}
}
