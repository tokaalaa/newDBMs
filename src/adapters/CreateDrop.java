package adapters;

import java.sql.SQLException;

import interfaces.Command;
import interfaces.Database;

public class CreateDrop implements Command{
	
	private Database database;

	public CreateDrop(Database database){
		this.database = database;
	}
	
	@Override
	public boolean execute(String query) {
		if (database != null) {
			try {
				return database.executeStructureQuery(query);
				//System.out.println(database.executeStructureQuery(query));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("Error has occured");
			}
		}
		return false;
	}

}
