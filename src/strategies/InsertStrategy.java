package strategies;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import interfaces.Observable;
import interfaces.Observer;
import interfaces.mainprogram.CacheManager;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class InsertStrategy implements Observable{
	private ArrayList<Observer> observers;
	private Document document;
	public InsertStrategy() {
		observers = new ArrayList<Observer>();
		attatch(CacheManager.getInstance());
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			document = builder.newDocument();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public int insertInList(HashMap<String, String> newRow, NodeList list) {		
			document.appendChild(document.createElement("table"));
			addOldListToDocument(list);
			addNewElementsToDocument(newRow);			
		NodeList newlist = document.getElementsByTagName("row");
			notify(list, newlist);

		return 1;
	}
	private void addOldListToDocument(NodeList list) {
		Element root = document.getDocumentElement();
		for(int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if(node.getNodeType()  == Node.ELEMENT_NODE) {
				Node importedNode = document.importNode(node, true);
				root.appendChild(importedNode);
			}
		}
	}
	private void addNewElementsToDocument(HashMap<String, String> newRow) {
		Element row = document.createElement("row");
		for(Map.Entry m : newRow.entrySet()) {
			Element column = document.createElement((String) m.getKey());
			column.appendChild(document.createTextNode((String) m.getValue()));
			row.appendChild(column);
		}
		Element root = document.getDocumentElement();
		root.appendChild(row);
	}
	@Override
	public void attatch(Observer o) {
		// TODO Auto-generated method stub
		observers.add(o);
	}
	@Override
	public void detach(Observer o) {
		// TODO Auto-generated method stub
		observers.remove(o);
	}
	@Override
	public void notify(NodeList oldList, NodeList newList) {
		// TODO Auto-generated method stub
		for(Observer o : observers) {
			o.update(oldList, newList);
		}
	}
}
