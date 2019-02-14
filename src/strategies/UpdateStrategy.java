package strategies;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.w3c.dom.NodeList;

import interfaces.mainprogram.MyDataBase;

public abstract class UpdateStrategy {
	protected String tableName;
	public UpdateStrategy(String tableName) {
		this.tableName = tableName;
	}
	protected boolean tableIsExist(String tableName) {
		 
		 Path p = Paths.get("main folder" + System.getProperty("file.separator") + MyDataBase.currentDatabase +
					System.getProperty("file.separator") + tableName + ".xml");
		 return Files.exists(p);
	
	 }
	public abstract int update(NodeList list);
}
