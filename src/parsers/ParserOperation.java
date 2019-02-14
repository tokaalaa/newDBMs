package parsers;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.NodeList;

import interfaces.mainprogram.CacheManager;
import interfaces.mainprogram.CreateDTD;
import strategies.DeleteAll;
import strategies.DeleteStrategy;
import strategies.DeleteWithCondition;
import strategies.InsertStrategy;
import strategies.UpdateColumnWithCondition;
import strategies.UpdateColumnWithoutCondition;
import strategies.UpdateStrategy;


public class ParserOperation {
	private String query;
	private CacheManager cacheManager;
	private InsertStrategy insertStrategy;
	public ParserOperation(String query) {
		this.query = query;
		cacheManager = CacheManager.getInstance();
		insertStrategy = new InsertStrategy();
	}
	
	@SuppressWarnings("unused")
	public int setInput(String query) {
        query = query.trim().replaceAll(" +", " ");
		this.query = query;
		int countWith = 0, countWithout = 0;
		int count = 0;
		String[] arra = new String[4];
		String table_name = null;
		String condition = null;
		int i = 0;
		String type = null;
		boolean valid = true;
 
		if (query.toLowerCase().contains("insert into")) {
			type = "insert";
		} else if (query.toLowerCase().contains("delete from")) {
			type = "delete";
		} else if (query.toLowerCase().contains("update")) {
			type = "update";
		} else {
			type = "error";
			valid = false;
		}
		
		if (valid == true) {
			if (type == "insert") {
		//for insert into table
		//String test = "INSERT INTO table_name (c1,c2,c3) VALUES (abc,def,ghi), (jkl,mno,pqr)";
		String regexInsert = "((?<=(INSERT\\sINTO\\s))[\\w\\d_]+)|((?<=\\()([\\w\\d,\\s\\'_]+)+(?=\\)))";
		Pattern patternInsert = Pattern.compile(regexInsert, Pattern.CASE_INSENSITIVE);		
		char[] stringToCharArray = this.query.toCharArray();
		 if (!BalancedParan.areParenthesisBalanced(stringToCharArray)) {
				valid = false;
		 } else {		 
				Matcher m = patternInsert.matcher(this.query);
				while (m.find()) {
					arra[count] = m.group(0);
					count++;
				}
				boolean check = this.query.toLowerCase().contains("values");
				if (count < 3 && check == false) {
					valid = false;
				} else if(count < 3 && check == true) {
					table_name = arra[0];
					int norow = count - 1;
					boolean read = CreateDTD.readNamesAndTypes(table_name);
					String[][] table = CreateDTD.getTable();
					String[][] values = new String[norow][table.length];
			        HashMap<String, String> hash = new HashMap<>();
					if (!table_name.equalsIgnoreCase(ParserCreateTable.currentTable) && !read) {

						valid = false;
						return -1;
					}
			        
					while(i < norow) {
						String[] row = arra[i + 1].split(",");
						if (row.length != table.length) {
							valid = false;
							break;
						}
						for (int j = 0; j < table.length; j++) {
								valid = true;
								if (row[j].contains("'") && table[j][1].equals("int")) {
									valid = false;
								} else if ((!row[j].contains("'")) && table[j][1].equals("varchar")) {
									valid = false;
								} else if (row[j].contains("'") && table[j][1].equals("varchar")) {
									row[j] = row[j].replaceAll(" ", "");
									row[j] = row[j].replaceAll("'", "");
									hash.put(table[j][0], row[j]);
								} else {
									row[j] = row[j].replaceAll(" ", "");
									hash.put(table[j][0], row[j]);
								}
						}
						i++;
					}
					if(valid == true) {

	//------------------------------insert---------------------------------------
						cacheManager.checkCurrentTable(table_name);
						NodeList tableList = cacheManager.fetchTable(table_name);
						
						return insertStrategy.insertInList(hash, tableList);
		
					}
				} else {
					table_name = arra[0];
					String colunm[] = arra[1].split(",");
					int norow = count - 2;
					String[][] values = new String[norow][colunm.length];
					boolean read = CreateDTD.readNamesAndTypes(table_name);
					String[][] table = CreateDTD.getTable();
			        HashMap<String, String> hash = new HashMap<>();
			        HashMap<String, String> arr = new HashMap<>();
					if (!table_name.equalsIgnoreCase(ParserCreateTable.currentTable) && !read) {

						valid = false;
						return -1;
					}

					while(i < norow) {
						String[] row = arra[i + 2].split(",");
						if (colunm[i] == null) {
							valid = false;
							break;
						}
						if (row.length != table.length) {
							valid = false;
							break;
						}
						for( int k = 0; k < row.length; k++) {
						for (int j = 0; j < table.length; j++) {
							colunm[k] = colunm[k].replaceAll(" ", "");
							if (colunm[k].equalsIgnoreCase(table[j][0])) {			
								valid = true;
								if (row[k].contains("'") && table[j][1].equals("int")) {
									valid = false;
								} else if ((!row[k].contains("'")) && table[j][1].equals("varchar")) {
									valid = false;
								} else if (row[k].contains("'") && table[j][1].equals("varchar")) {
									row[k] = row[k].replace("'", "");
									row[k] = row[k].replaceAll(" ", "");
									row[k] = row[k].replaceAll("'", "");
									arr.put(table[j][0], row[k]);
								} else {
									row[k] = row[k].replaceAll(" ", "");
									arr.put(table[j][0], row[k]);
								}
								break;
							} else {
								valid = false;
 							}
						}
						if (valid == false ) {
							break;
						}
						}
						i++;
					}
					if(valid == true) {
						hash = arrMethod(arr, table);
	//------------------------------insert---------------------------------------
						cacheManager.checkCurrentTable(table_name);
						NodeList tableList = cacheManager.fetchTable(table_name);
						
						return insertStrategy.insertInList(hash, tableList);
						
					}
				}
		 		}
			} else if (type == "delete") {
		//for delete column without condition
        //String testdelete1 = "DELETE FROM Customers";
        String regexDelete1 = "((?<=(delete\\sFROM\\s))[\\w\\d_]+)";
        Pattern patternDelete1 = Pattern.compile(regexDelete1, Pattern.CASE_INSENSITIVE);
        Matcher mDelete1 = patternDelete1.matcher(this.query);
        while (mDelete1.find()) {
        	countWithout++;
		}
        //for delete column with condition
       // String testdelete2 = "DELETE FROM Customers WHERE CustomerName='Alfreds Futterkiste'";
        String regexDelete2 = "((?<=(delete\\sFROM\\s))[\\w\\d_]+)|((?<=(where\\s))[\\w\\d=<>\\'\\s_]+)";
        Pattern patternDelete2 = Pattern.compile(regexDelete2, Pattern.CASE_INSENSITIVE);
        Matcher mDelete2 = patternDelete2.matcher(this.query);
        while (mDelete2.find()) {
			arra[countWithout] = mDelete2.group(0);
			countWith++;
		}
        
        if (countWith == 2) {
        	mDelete2 = patternDelete2.matcher(query);
			while (mDelete2.find()) {
				arra[count] = mDelete2.group(0);
				count++;
			}
			table_name = arra[0];
			boolean read = CreateDTD.readNamesAndTypes(table_name);
			condition = arra[1];
			String split[] = null;
			boolean checkString ;
			String operation = null;
			if (!table_name.equalsIgnoreCase(ParserCreateTable.currentTable) && !read) {
				valid = false;
				return -1;
			}
			if (condition.contains("==")) {
				valid = false;
			} else if (condition.contains("=")) {
				split = arra[1].split("=");
				operation = "=";
			} else if (condition.contains(">")) {
				split = arra[1].split(">");
				operation = ">";
			} else if (condition.contains("<")) {
				split = arra[1].split("<");
				operation = "<";
			}
			if (split.length != 2) {
				valid = false;
			}
			split[0] = split[0].replaceAll(" ", "");
			if (split[1].contains("'")) {
				checkString = true;
				split[1] = split[1].replaceAll(" ", "");
				split[1] = split[1].replaceAll("'", "");
			} else {
				checkString = false;
			}
			if ((!operation.equals("=")) && checkString == true) {
				valid = false;
			}
			if(valid == true) {

			//--------------------------delete------------------------------
			cacheManager.checkCurrentTable(table_name);
			NodeList tableList = cacheManager.fetchTable(table_name);
			DeleteStrategy deleteStrategy = new DeleteWithCondition(table_name, split[0], split[1], operation, checkString);
			System.out.println("tableList size:" + tableList.getLength());
			return deleteStrategy.deleteFrom(tableList);

			}
        } else if (query.toLowerCase().contains("where") && countWith != 2) {
			valid = false;
        } else if (countWithout == 1) {
        	mDelete1 = patternDelete1.matcher(query);
			while (mDelete1.find()) {
				arra[count] = mDelete1.group(0);
				count++;
			}
			table_name = arra[0];
			boolean read = CreateDTD.readNamesAndTypes(table_name);
			if (!table_name.equalsIgnoreCase(ParserCreateTable.currentTable) && !read) {
				valid = false;
				return -1;
			}
			if(valid == true) {
				//--------------------------delete------------------------------
				cacheManager.checkCurrentTable(table_name);
				NodeList tableList = cacheManager.fetchTable(table_name);
				DeleteStrategy deleteStrategy = new DeleteAll(table_name);
				return deleteStrategy.deleteFrom(tableList);

			} 
        } 

			} else if (type == "update") {
        //for update column without condition
        //String testupdate1 = "UPDATE Customers SET ContactName='Juan'";
        String regexUpdate1 = "((?<=(UPDATE\\s))\\w+)|((?<=SET\\s)([\\w\\s_,=\\'\\d]+)+)";
        Pattern patternUpdate1 = Pattern.compile(regexUpdate1, Pattern.CASE_INSENSITIVE);
        Matcher mUpdate1 = patternUpdate1.matcher(this.query);
        while (mUpdate1.find()) {
        	countWithout++;
        }
        
        //for update column with condition
        //String testupdate2 = "UPDATE Customers SET ContactName = 'Alfred Schmidt', City= 'Frankfurt' WHERe CustomerID > 1";
        String arr[] = this.query.split("\\s*(?i)where\\s*", 2);
        String regexUpdate2 = "((?<=(UPDATE\\s))\\w+)"
        		+ "|((?<=SET\\s)([\\w\\s,_=\\'\\d]+))";
        String regexUpdateCon = "((?<=(where\\s))[\\w\\d=_<>\\'\\s]+)";
        Pattern patternUpdate2 = Pattern.compile(regexUpdate2, Pattern.CASE_INSENSITIVE);
        Matcher mUpdate2 = patternUpdate2.matcher(arr[0]);
        Pattern patternUpdateCon = Pattern.compile(regexUpdateCon, Pattern.CASE_INSENSITIVE);
        Matcher mUpdateCon = patternUpdateCon.matcher(this.query);
        while (mUpdate2.find()) {
        	countWith++;
        }
        while (mUpdateCon.find()) {
        	countWith++;
        }

        if (countWith == 3) {
        	count = 0;
			mUpdate2 = patternUpdate2.matcher(arr[0]);
			while (mUpdate2.find()) {
				arra[count] = mUpdate2.group(0);
				count++;
			}
			mUpdateCon = patternUpdateCon.matcher(this.query);
			while (mUpdateCon.find()) {
				arra[count] = mUpdateCon.group(0);
				count++;
			}

			table_name = arra[0];
			boolean read = CreateDTD.readNamesAndTypes(table_name);
			condition = arra[2];
			String split[] = null;
			boolean checkString ;
			String operation = null;
			
			if (!table_name.equalsIgnoreCase(ParserCreateTable.currentTable) && !read) {

				valid = false;
				return -1;
			}
			if (condition.contains("==")) {
				valid = false;
			} else if (condition.contains("=")) {
				split = arra[2].split("=");
				operation = "=";
			} else if (condition.contains(">")) {
				split = arra[2].split(">");
				operation = ">";
			} else if (condition.contains("<")) {
				split = arra[2].split("<");
				operation = "<";
			}
			if (split.length != 2) {
				valid = false;
			} else {
				String row[] = arra[1].split(",");
				String colunm[] = new String[row.length];
				String[][] table = CreateDTD.getTable();
				HashMap<String, String> hash = new HashMap<>();
				
				for (int j = 0; j < row.length; j++) {
					String value[] = new String[2];
					if (row[j].contains("==")) {
						valid = false;
						break;
					} else if (row[j].contains("=")) {
						value = row[j].split("=");
					}
					if(value.length != 2) {
						valid = false;
						break;
					}
				 	for (int j1 = 0; j1 < table.length; j1++) {
						value[0] = value[0].replace(" ", "");
						if (value[0].equalsIgnoreCase(table[j1][0])) {
							if (value[1].contains("'") && table[j1][1].equals("int")) {
								valid = false;
								break;
							} else if ((!value[1].contains("'")) && table[j1][1].equals("varchar")) {
								valid = false;
								break;
							} else if (value[1].contains("'") && table[j][1].equals("varchar")) {
								int m = 0;
								for (int n = 0; n < value[1].length(); n ++) {
									if (value[1].charAt(n) == '\'' ) {
										m++;
									}
								}
								if (m != 2) {
									valid = false;
									break;
								}
								value[1] = value[1].replace(" ", "");
								value[1] = value[1].replace("'", "");
								hash.put(value[0], value[1]);
							} else {
								value[1] = value[1].replace(" ", "");
								hash.put(value[0], value[1]);
							}
						}
					}
				}
				if (split[1].contains("'")) {
					checkString = true;
					split[1] = split[1].replace("'", "");
					split[1] = split[1].replace(" ", "");
				} else {
					checkString = false;
				}
				if ((!operation.equals("=")) && checkString == true) {
					valid = false;
				}
				split[0] = split[0].replace(" ", "");
				if(valid == true) {

//-------------------------update-----------------------------------------
					cacheManager.checkCurrentTable(table_name);
					NodeList tableList = cacheManager.fetchTable(table_name);
					UpdateStrategy updateStrategy = new UpdateColumnWithCondition(table_name, split[0], split[1], hash);
					return updateStrategy.update(tableList);
					}

			}
        } else if (query.toLowerCase().contains("where") && countWith != 3) {
			valid = false;
        } else if (countWithout == 2) {
        	mUpdate1 = patternUpdate1.matcher(query);
			while (mUpdate1.find()) {
				arra[count] = mUpdate1.group(0);
				count++;
			}
			table_name = arra[0];
			boolean read = CreateDTD.readNamesAndTypes(table_name);
			if (!table_name.equalsIgnoreCase(ParserCreateTable.currentTable) && !read) {

				valid = false;
				return -1;
			}
			String row[] = arra[1].split(",");
			String colunm[] = new String[row.length];
			String[][] table = CreateDTD.getTable();
			HashMap<String, String> hash = new HashMap<>();
	        
			for (int j = 0; j < row.length; j++) {
				String value[] = new String[2];
				if (row[j].contains("==")) {
					valid = false;
					break;
				} else if (row[j].contains("=")) {
					value = row[j].split("=");
				}
				if(value.length != 2) {
					valid = false;
					break;
				}
				for (int j1 = 0; j1 < table.length; j1++) {
					value[0] = value[0].replace(" ", "");
					if (value[0].equalsIgnoreCase(table[j1][0])) {
						if (value[1].contains("'") && table[j1][1].equals("int")) {
							valid = false;
						} else if ((!value[1].contains("'")) && table[j1][1].equals("varchar")) {
							valid = false;
						} else if (value[1].contains("'") && table[j][1].equals("varchar")) {
							int m = 0;
							for (int n = 0; n < value[1].length(); n ++) {
								if (value[1].charAt(n) == '\'' ) {
									m++;
								}
							}
							if (m != 2) {
								valid = false;
								break;
							}
							value[1] = value[1].replace("'", "");
							value[1] = value[1].replace(" ", "");
							hash.put(value[0], value[1]);
						} else {
							value[1] = value[1].replace(" ", "");
							hash.put(value[0], value[1]);
						}
					}
				}
			}
//--------------------------update-------------------------------
			if(valid == true) {

				cacheManager.checkCurrentTable(table_name);
				NodeList tableList = cacheManager.fetchTable(table_name);
				UpdateStrategy updateStrategy = new UpdateColumnWithoutCondition(table_name, hash);
				return updateStrategy.update(tableList);
				} 
        }
			}
	  }
		return 0;
	}

	private HashMap<String, String> arrMethod(HashMap<String, String> arr, String[][] table) {
        HashMap<String, String> hash = new HashMap<>();
		for (int i = 0; i < table.length; i++) {
			hash.put(table[i][0], arr.get(table[i][0])); 
		}
		return hash;
	}
}

