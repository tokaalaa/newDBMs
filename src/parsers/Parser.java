package parsers;

import adapters.CreateDrop;
import adapters.Operation;
import adapters.Select;
import interfaces.Command;
import interfaces.Database;
import interfaces.IParser;


public class Parser implements IParser{
	
	private Database database;
	private Command command;


	public Parser(Database database) {
		this.database = database;
	 }


	public boolean execute(String q) {
       String query = q.trim().replaceAll(" +", " ");
		String arr[] = query.split(" ", 2);
		String firstWord = arr[0];
		
		if (firstWord.equalsIgnoreCase("select")
				|| firstWord.equalsIgnoreCase("select*")) {
			this.command = new Select(database);
			return command.execute(query);	
		} else if (firstWord.equalsIgnoreCase("INSERT") 
				|| firstWord.equalsIgnoreCase("UPDATE")
				|| firstWord.equalsIgnoreCase("DELETE")) {
			this.command = new Operation(database);
			return command.execute(query);	
		} else if (firstWord.equalsIgnoreCase("DROP") 
				|| firstWord.equalsIgnoreCase("CREATE")) {
			this.command = new CreateDrop(database);
			return command.execute(query);	
		} else {
			return false;
		}
	}
}
