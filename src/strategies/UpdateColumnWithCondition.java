package strategies;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class UpdateColumnWithCondition extends UpdateStrategy{
	private String columnCondition;
	private String valueCondition;
	private HashMap<String, String> updates;
	
	public UpdateColumnWithCondition(String tableName, String columnCondition,
			String valueCondition, HashMap<String, String> updates) {
		super(tableName);
	this.columnCondition = columnCondition;
	this.valueCondition = valueCondition;
	this.updates = updates;
	}

	@Override
	public int update(NodeList list) {
		// TODO Auto-generated method stub
		if (!tableIsExist(tableName)) {
			return 0;
		}
		int numOfUpdates = 0;
		for(int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if(node.getNodeType()  == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				NodeList childs = element.getChildNodes();
				for(int j = 0; j < childs.getLength(); j++) {
					Node n = childs.item(j);
					if(n.getNodeType()  == Node.ELEMENT_NODE) {
						Element e = (Element) n;
						if (e.getTagName().equalsIgnoreCase(columnCondition)
								&& e.getTextContent().equalsIgnoreCase(valueCondition)) {
							numOfUpdates++;
							for(int k = 0; k < childs.getLength(); k++) {
								Node n1 = childs.item(k);
								if(n1.getNodeType()  == Node.ELEMENT_NODE) {
									Element e1 = (Element) n1;
								for(Map.Entry m : updates.entrySet()) {
									if(((String)m.getKey()).equalsIgnoreCase(e1.getTagName())) {
										e1.setTextContent((String)m.getValue());
									}
								}
								}
							}
							break;
						}
					}
				}
		}
	}
		return numOfUpdates;
	}

}
