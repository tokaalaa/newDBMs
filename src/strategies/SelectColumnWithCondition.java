package strategies;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import interfaces.mainprogram.CreateDTD;

public class SelectColumnWithCondition extends SelectStrategy{
	private String columnName;
	private String valueCondition;
	private String columnCondition;
	private String operation;
	private boolean checkString;
	
	public SelectColumnWithCondition(String tableName, String columnName, String valueCondition,
			String columnCondition, String operation, boolean checkString) {
		super(tableName);
		// TODO Auto-generated constructor stub
		this.columnName = columnName;
		this.valueCondition = valueCondition;
		this.columnCondition = columnCondition;
		this.operation = operation;
		this.checkString = checkString;
	}

	@Override
	public Object[][] selectFrom(NodeList list) {
		// TODO Auto-generated method stub
		if (!tableIsExist(tableName)) {
			return null;
		}
		ArrayList<String> column = new ArrayList<String>();
		for(int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if(node.getNodeType()  == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				NodeList childs = element.getChildNodes();
				for(int j = 0; j < childs.getLength(); j++) {
					Node n = childs.item(j);
					if(n.getNodeType()  == Node.ELEMENT_NODE) {
						Element e = (Element) n;
						// if string check column condition direct
						if (checkString && e.getTagName().equalsIgnoreCase(columnCondition)
								&& e.getTextContent().equalsIgnoreCase(valueCondition)) {
							for(int k = 0; k < childs.getLength(); k++) {
								Node n1 = childs.item(k);
								if(n1.getNodeType()  == Node.ELEMENT_NODE) {
									Element e1 = (Element) n1;
									if((e1.getTagName()).equalsIgnoreCase(columnName)) {
										column.add(e1.getTextContent());
								}
								}
							}
							break;
						}else if (!checkString && e.getTagName().equalsIgnoreCase(columnCondition)
								&& checkOperation(operation, e.getTextContent(), valueCondition )) {
							for(int k = 0; k < childs.getLength(); k++) {
								Node n1 = childs.item(k);
								if(n1.getNodeType()  == Node.ELEMENT_NODE) {
									Element e1 = (Element) n1;
									if((e1.getTagName()).equalsIgnoreCase(columnName)) {
										column.add(e1.getTextContent());
								}
								}
							}
							break;
						}
					}
				}
		}
	}
		return convertColumn(column,columnName);
	}
	

	private Object[][] convertColumn(ArrayList<String> a, String columnName) {
		// TODO Auto-generated method stub
		
		String[][] table = CreateDTD.getTable();
		String type = null;
		for(int j = 0; j < table.length; j++) {
			if (table[j][0].equalsIgnoreCase(columnName)) {
				type = table[j][1];
			}
		}
		Object[][] array = new Object[a.size()][1];
		for (int i = 0; i < a.size(); i++) {
			if(type.equalsIgnoreCase("int")) {
				array[i][0] = Integer.parseInt(a.get(i));
			} else {
				array[i][0] = a.get(i);
			}
		}
		return array;
	}


}
