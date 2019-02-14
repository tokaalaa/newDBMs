package strategies;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class DeleteWithCondition extends DeleteStrategy {
	private String columnCondition;
	private String operation;
	private String valueCondition;
	private boolean checkString;
	public DeleteWithCondition(String tableName, String columnCondition, String valueCondition, 
			String operation, boolean checkString) {
		super(tableName);
		this.columnCondition = columnCondition;
		this.operation = operation;
		this.valueCondition = valueCondition;
		this.checkString = checkString;
	}
	@Override
	public int deleteFrom(NodeList list) {
if (!tableIsExist(tableName)) {
	return 0;
}
		int deletedrows = 0;
		int count = 1;
		boolean found = false;
		// TODO Auto-generated method stub
		int length = list.getLength();
		for(int i = 0; i < length; i++) {
			Node node = list.item(list.getLength() - count);
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
							element.getParentNode().removeChild(element);
							deletedrows++;
							found = true;
							break;
							
						}else if (!checkString && e.getTagName().equalsIgnoreCase(columnCondition)
								&& checkOperation(operation, e.getTextContent(), valueCondition )) {
							element.getParentNode().removeChild(element);
							deletedrows++;
							found = true;
							break;
						}
					}
				}
				if (!found) {
					count++;
				}
		}
	}
		return deletedrows;
	}
	private boolean checkOperation(String operation, String textContent, String valueCondition) {
		// TODO Auto-generated method stub
		int oldValue = Integer.parseInt(textContent);
		int condition = Integer.parseInt(valueCondition);
		switch (operation) {
		case ">": return oldValue > condition; 
		case "<": return oldValue < condition;
		case "=": return oldValue == condition;
		default : return false;
		}
	}
}
