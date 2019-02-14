package strategies;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class UpdateColumnWithoutCondition extends UpdateStrategy{
	private HashMap<String, String> updates;
	public UpdateColumnWithoutCondition(String tableName, HashMap<String, String> updates) {
		super(tableName);
		this.updates = updates;
	}

	@Override
	public int update(NodeList list) {
		// TODO Auto-generated method stub
		if (!tableIsExist(tableName)) {
			return 0;
		}
		for(int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if(node.getNodeType()  == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				NodeList childs = element.getChildNodes();
				for(int j = 0; j < childs.getLength(); j++) {
					Node n = childs.item(j);
					if(n.getNodeType()  == Node.ELEMENT_NODE) {
						Element e = (Element) n;
						for(Map.Entry m : updates.entrySet()) {
						if(e.getTagName().equalsIgnoreCase((String)m.getKey())) {
							e.setTextContent((String)m.getValue());
						}
						}
							}}
			}
			}
	
		return list.getLength();	

	}

}
