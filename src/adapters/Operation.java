package adapters;

import interfaces.Command;
import interfaces.Database;
import parsers.ParserOperation;

public class Operation implements Command{
	
	private Database database;

	public Operation(Database database){
		this.database = database;
	}
 
	@Override
	public boolean execute(String query) {
		int check = 0;
		if (database != null) {
			ParserOperation h = new ParserOperation(query);
			check = h.setInput(query);
			if (check == -1) {
				return false;
			} else {
			return true;
			}
		}
		return false;		
	}

}
