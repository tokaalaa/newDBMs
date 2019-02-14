package interfaces;

import org.w3c.dom.NodeList;

public interface Observer {
	public void update(NodeList oldList, NodeList newList);
}
