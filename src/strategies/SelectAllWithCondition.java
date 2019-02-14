package strategies;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class SelectAllWithCondition extends SelectStrategy{
	private String valueCondition;
	private String columnCondition;
	private String operation;
	private boolean checkString;
	public SelectAllWithCondition(String tableName, String valueCondition,
		String columnCondition, String operation, boolean checkString) {
		super(tableName);
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
		ArrayList<ArrayList<String>> table = new ArrayList<ArrayList<String>>();
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
							ArrayList<String> row = new ArrayList<String>();
							for(int k = 0; k < childs.getLength(); k++) {
								Node n1 = childs.item(k);
								if(n1.getNodeType()  == Node.ELEMENT_NODE) {
									Element e1 = (Element) n1;
									row.add(e1.getTextContent());
									}	
								}
							table.add(row);
							break;

						}else if (!checkString && e.getTagName().equalsIgnoreCase(columnCondition)
								&& checkOperation(operation, e.getTextContent(), valueCondition )) {
							ArrayList<String> row = new ArrayList<String>();
							for(int k = 0; k < childs.getLength(); k++) {
								Node n1 = childs.item(k);
								if(n1.getNodeType()  == Node.ELEMENT_NODE) {
									Element e1 = (Element) n1;
									row.add(e1.getTextContent());
									}	
								}
							table.add(row);
							break;
						}
					}
				}
		}
	}
		return convertToArray2d(table);	
	}
}
