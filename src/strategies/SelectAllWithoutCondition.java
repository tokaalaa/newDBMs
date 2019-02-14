package strategies;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SelectAllWithoutCondition extends SelectStrategy{

	public SelectAllWithoutCondition(String tableName) {
		super(tableName);
		// TODO Auto-generated constructor stub
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
				NodeList childlist = element.getChildNodes();
				ArrayList<String> row = new ArrayList<String>();
				for(int j = 0; j < childlist.getLength(); j++) {
					Node n = childlist.item(j);
					if(node.getNodeType()  == Node.ELEMENT_NODE) {
						Element e = (Element) n;
						row.add(e.getTextContent());
						}
				}
				table.add(row);
				}
			}
		return convertToArray2d(table);
	}

}
