package interfaces.mainprogram;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



public class ReadAndWriteToXml {

	private File xmlFile;
	private DocumentBuilderFactory documentBuilderfactory;
	private DocumentBuilder documentBuilder;
	private Document document;
	
	private NodeList list;
	public ReadAndWriteToXml() {
		// for the first using of document
		 		documentBuilderfactory = DocumentBuilderFactory.newInstance();
				try {
					documentBuilder = documentBuilderfactory.newDocumentBuilder();
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		 		document = documentBuilder.newDocument();
		 		list = document.getElementsByTagName("row");
		 		document.appendChild(document.createElement("table"));
	}
	public NodeList readXml(String currentTable) {
		try {
			xmlFile = new File("main folder" + System.getProperty("file.separator") + MyDataBase.currentDatabase +
					System.getProperty("file.separator") + currentTable + ".xml");
			documentBuilderfactory = DocumentBuilderFactory.newInstance();
			documentBuilder = documentBuilderfactory.newDocumentBuilder();
			document = documentBuilder.parse(xmlFile);
				list = document.getElementsByTagName("row");
				return list;
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		return null;	
	}
	
	public void writeToXml(String currentTable, String currentDatabaseName, NodeList list) {
		convertNodeListToDocument(list);
		list = document.getElementsByTagName("row");
		xmlFile = new File("main folder" + System.getProperty("file.separator") + currentDatabaseName +
				System.getProperty("file.separator") + currentTable	+ ".xml");
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		try {
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			// main folder + database name
			StreamResult streamResult = new StreamResult(xmlFile);
			
				transformer.transform(source, streamResult);
		
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void convertNodeListToDocument(NodeList newlist) {
		NodeList oldlist = document.getElementsByTagName("row");
		Element root = document.getDocumentElement();
		for(int i = oldlist.getLength(); i < newlist.getLength(); i++) {
			Node node = newlist.item(i);
			if(node.getNodeType()  == Node.ELEMENT_NODE) {
				Node importedNode = document.importNode(node, true);
				root.appendChild(importedNode);
			}
		}
	}
}
