package com.nathan.xmlcomp;

import java.io.File;
import java.util.ArrayList;

import com.nathan.xmlcomp.util.SimpleMap;
import com.nathan.xmlcomp.util.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XMLValueComp
{
	private File xfile1;
	private File xfile2;
	
	private SimpleMap f1v;
	private SimpleMap f2v;
	
	public static void main(String[] args)
	{
		if (args.length < 2)
			printHelp();
		
		XMLValueComp xmlComp = new XMLValueComp(args[0], args[1]);
		
		xmlComp.compare();
	}
	
	public static void printHelp()
	{
		System.out.println("Usage: xmlcomp file1 file2");
		System.exit(0);
	}
	
	public XMLValueComp(String file1, String file2)
	{
		f1v = new SimpleMap();
		f2v = new SimpleMap();
		File f1 = new File(file1);
		File f2 = new File(file2);
		
		if (!f1.exists() || !f2.exists())
		{
			System.out.println("One of the files does not exist!");
			System.exit(0);
		}
		xfile1 = f1;
		xfile2 = f2;
	}
	
	public void compare()
	{
		Document doc1 = Utils.stringToDocument(
						Utils.readStringFromFile(xfile1));
			
		Document doc2 = Utils.stringToDocument(
				Utils.readStringFromFile(xfile2));
		
		if (doc1 == null || doc2 == null)
		{
			System.err.println("Could not build docs");
			System.exit(0);
		}
		
		buildMap(f1v, doc1);
		buildMap(f2v, doc2);

		ArrayList<String> uniques = new ArrayList<String>();

		uniques.addAll(f1v.getCompleteItemList());
		uniques.removeAll(f2v.getCompleteItemList());

		System.out.println("Unique to File1");
		for (String v : uniques)
			System.out.println(v);


		uniques = new ArrayList<String>();

		uniques.addAll(f2v.getCompleteItemList());
		uniques.removeAll(f1v.getCompleteItemList());
		
		System.out.println("\n\nUnique to File2");
		for (String v : uniques)
			System.out.println(v);
	}

	private void buildMap(SimpleMap map, Document doc)
	{
		Element e = doc.getDocumentElement();

		mapNodes(map, e, "/" + e.getNodeName());
	}

	private void mapNodes(SimpleMap map, Element elem, String parent)
	{
		for (int x = 0; x < elem.getChildNodes().getLength(); x++)
		{
			Node n = elem.getChildNodes().item(x);
			String path = parent + "/" + n.getNodeName(); 
					
			if (n.getChildNodes().getLength() == 1)
				map.put(path, n.getTextContent().trim());
			
			if (n.getNodeType() == Node.ELEMENT_NODE)
				mapNodes(map, (Element) n, path);
		}
	}
}
