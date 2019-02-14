package interfaces;

import org.w3c.dom.NodeList;

public interface Observable {
 
	public void attatch(Observer o);
	public void detach(Observer o);
	public void notify(NodeList oldList, NodeList newList);
}
