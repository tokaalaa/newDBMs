package strategies;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



public class DeleteAll extends DeleteStrategy {

	public DeleteAll(String tableName) {
		super(tableName);
	}

	@Override
	public int deleteFrom(NodeList list) {
		if (!tableIsExist(tableName)) {
			return 0;
		}
		int deletedrow = list.getLength();
		for(int i = 0; i < deletedrow; i++) {
			Node node = list.item(list.getLength() - 1);
			if(node.getNodeType()  == Node.ELEMENT_NODE) {
				Element element = (Element) node;
		element.getParentNode().removeChild(element);
		}
	}
		return deletedrow;
	}


}
