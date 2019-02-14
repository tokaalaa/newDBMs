package adapters;

import java.sql.SQLException;

import interfaces.Command;
import interfaces.Database;

public class Select implements Command{

	private Database database;

	public Select(Database database){
		this.database = database;
	}
	   
	@Override
	public boolean execute(String query) {
		Object[][] sele;
		if (database != null) {
			try {
				sele = database.executeQuery(query);
				if (sele == null) {
					return false;
				}
				if (sele.length != 0) {
				for(int j = 0; j < sele[0].length; j++) {
	    			System.out.print( "+--------------------" );
				}
				System.out.print("+\n");
				for(int j = 0; j < sele[0].length; j++) {
	    			System.out.print( "|column_"+ j +"            " );
				}
				System.out.print("|\n");
				for(int j = 0; j < sele[0].length; j++) {
	    			System.out.print( "+--------------------" );
				}
				System.out.print("+\n");
				for(int i = 0; i < sele.length; i++) {
					for(int j = 0; j < sele[0].length; j++) {
		    			System.out.print( "|" + sele[i][j] );
		    			if (sele[i][j] instanceof String) {
		    				if (((String) sele[i][j]).length() < 20) {
			    				int count = 20 - ((String) sele[i][j]).length();
			    				while(count != 0) {
			    					System.out.print(" ");
			    					count--;
			    				}
			    			}
		    			} else {
		    				String numberAsString = Integer.toString(((int) sele[i][j]));
		    				if (numberAsString.length() < 20) {
			    				int count = 20 - numberAsString.length();
			    				while(count != 0) {
			    					System.out.print(" ");
			    					count--;
			    				}
			    			}
		    			}
					}
	    			System.out.print( "|\n");
				}
				for(int j = 0; j < sele[0].length; j++) {
	    			System.out.print( "+--------------------" );
				}
				System.out.print("+\n");
				} else {
					System.out.println("zero matching rows");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("Error has occured");
			}
			return true;
		}
		return false;		
	}

}
