package strategies;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.w3c.dom.NodeList;

import interfaces.mainprogram.MyDataBase;

public abstract class DeleteStrategy {
	protected String tableName;
	public DeleteStrategy(String tableName) {
		this.tableName = tableName;
	}
	 public abstract int deleteFrom (NodeList list);
	 
	 protected boolean tableIsExist(String tableName) {
		 
		 Path p = Paths.get("main folder" + System.getProperty("file.separator") + MyDataBase.currentDatabase +
					System.getProperty("file.separator") + tableName + ".xml");
		 return Files.exists(p);
	
	 }
}
