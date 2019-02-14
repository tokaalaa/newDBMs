package interfaces.mainprogram;

import java.util.Scanner;

import interfaces.Database;
import interfaces.IParser;
import parsers.Parser;

public class MyMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Database database = new MyDataBase();
    	IParser parser = new Parser(database);
		System.out.println();
		System.out.println("**welcome to you in Simple DBMS**");
		System.out.println();
		System.out.println("* Enter new query or 0 to exist:");
		Scanner scanner = new Scanner( System.in );
		String input = scanner.nextLine();
		while(!input.equals("0")) {
			boolean isValid = parser.execute(input);
			if(isValid) {
				System.out.println();
				System.out.println("* Your query is executed successfully");
				System.out.println("* Enter new query or 0 to exist:");	
			} else {
				System.out.println();
				System.out.println("* Invalid input");
				System.out.println("* Enter new query or 0 to exist:");
			}
			input = scanner.nextLine();
		}
		CacheManager cacheManager = CacheManager.getInstance();
		cacheManager.close();
		scanner.close();
		System.out.println();
		System.out.println("**Thanks for using Simple DBMS**");
	}

}
