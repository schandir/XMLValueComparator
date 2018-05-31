package com.nathan.xmlcomp.util;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public class Utils
{

	public static final char [] CHA_NORMAL_HEX = {'0', '1', '2', '3', '4', '5', 
		'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	
	public static final char [] CHA_INDEX_HEX = {'a', 'b', 'c', 'd', 'e', 'f', 
		'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p'};
	
	public static void saveObjectToDisk(Object ob, String fileName)
			throws IOException
	{
		File ds_of = new File(fileName);
		if (!ds_of.getParentFile().exists())
			ds_of.getParentFile().mkdirs();
		
		// Write to disk with FileOutputStream
		FileOutputStream f_out = new FileOutputStream(fileName);

		// Write object with ObjectOutputStream
		ObjectOutputStream obj_out = new ObjectOutputStream(f_out);

		// Write object out to disk
		obj_out.writeObject(ob);
		
		obj_out.close();
		
		f_out.close();
	}

	public static Object readObjectFromDisk(String fileName)
			throws IOException, ClassNotFoundException
	{
		// Read from disk using FileInputStream
		FileInputStream f_in = new FileInputStream(fileName);

		// Read object using ObjectInputStream
		ObjectInputStream obj_in = new ObjectInputStream(f_in);
		
		// Read an object
		Object ds_result = obj_in.readObject();
		
		obj_in.close();
		f_in.close();
		
		return ds_result;
	}

	public static void saveStringToDisk(String str, String str_file_name)
			throws IOException
	{
		File ds_file = new File(str_file_name);
		Writer ds_writer = new BufferedWriter(new FileWriter(ds_file));
		ds_writer.write(str);
		ds_writer.close();
	}
	
	/**
	 * Fetch the entire contents of a text file, and return it in a String. This
	 * style of implementation does not throw Exceptions to the caller.
	 * 
	 * @param aFile is a file which already exists and can be read.
	 * @throws IOException 
	 */
	static public String readStringFromDisk(String str_file_name) throws IOException
	{
		StringBuilder ds_text = new StringBuilder();
		String str_sep = System.getProperty("line.separator");
		Scanner ds_scan = new Scanner(new FileInputStream(str_file_name), "UTF-8");
		try
		{
			while (ds_scan.hasNextLine())
				ds_text.append(ds_scan.nextLine() + str_sep);
		}
		finally
		{
			ds_scan.close();
		}

		return ds_text.toString();
	}
	
	public static String readStringFromFile(File file)
	{
		StringBuilder ds_text = new StringBuilder();
		String str_sep = System.getProperty("line.separator");
		try
		{
			Scanner ds_scan = new Scanner(new FileInputStream(file), "UTF-8");
			while (ds_scan.hasNextLine())
				ds_text.append(ds_scan.nextLine() + str_sep);
			ds_scan.close();
		}
		catch (Exception e) 
		{
			System.err.println(file.getPath() + " :: Not read");
		}
		
		return ds_text.toString();
	}
	
	public static String readStringFromStream(InputStream ds_stream) throws IOException
	{
		final char[] cha_buffer = new char[0x10000];
		StringBuilder ds_out = new StringBuilder();
		Reader ds_in = new InputStreamReader(ds_stream, "UTF-8");
		int im_read;
		do
		{
			im_read = ds_in.read(cha_buffer, 0, cha_buffer.length);
			if (im_read > 0)
				ds_out.append(cha_buffer, 0, im_read);
		}
		while (im_read >= 0);
		
		ds_in.close();
		ds_stream.close();

		return ds_out.toString();
	}

	public static String getContents(String filename)
	{
		if (filename.length() == 0) return "";
		String contents = "";
		
		try
		{
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			while (br.ready())
				contents += br.readLine() + "\n";
			br.close();
			fr.close();
		}
		catch (Exception e)
		{
			System.out.println("[configs] Failed to read message from: "
					+ filename + " " + e.toString());
		}
		return contents;
	}
	
	public static String strToHex(String str_arg)
	{
		char[] chars = str_arg.toCharArray();

		StringBuffer hex = new StringBuffer();
		for(int im_cnt = 0; im_cnt < chars.length; im_cnt++)
			hex.append(Integer.toHexString((int)chars[im_cnt]));
		
		return hex.toString();
	}

	public static String convertHexToString(String hex)
	{
		StringBuilder ds_res = new StringBuilder();
		StringBuilder ds_tmp = new StringBuilder();

		for (int im_cnt = 0; im_cnt < hex.length() - 1; im_cnt += 2)
		{
			// grab the hex in pairs
			String output = hex.substring(im_cnt, (im_cnt + 2));
			// convert hex to decimal
			int decimal = Integer.parseInt(output, 16);
			// convert the decimal to character
			ds_res.append((char) decimal);

			ds_tmp.append(decimal);
		}

		return ds_res.toString();
	}
	
	public static short charCountShort(String str_term, char ch_char)
	{
		short is_res = 0;
		for (char ch_c : str_term.toCharArray())
			if (ch_c == ch_char)
				is_res ++;
		return is_res;
	}
	
	public static String getTimeFromMs(long ms)
	{
		if (ms < 1000)
			return ms + "ms";
		else if (ms >= 1000 && ms < 60000)
			return (ms/1000) + "s " + getTimeFromMs(ms - ((ms/1000)*1000));
		else if (ms >=60000 && ms < 3600000)
			return (ms/60000) + "m " + getTimeFromMs(ms - ((ms/60000)*60000));
		else return (ms/3600000) + "h " + getTimeFromMs(ms - ((ms/3600000)*3600000));
	}
	
	public static Document stringToDocument(String str_doc)
	{
		Document ds_doc = null;
		try
		{
			ds_doc = DocumentBuilderFactory
					.newInstance()
					.newDocumentBuilder()
					.parse(new ByteArrayInputStream(str_doc.getBytes()));
		}
		catch (Exception e) 
		{
			System.err.println("String could not be converted to Document");
		}
		
		return ds_doc;
	}

	//method to convert Document to String
	public static String documentToString(Document doc)
	{
		try
		{
			DOMSource domSource = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			return writer.toString();
		}
		catch(TransformerException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public static String toIndexableHex(String normalHex)
	{
		String indexHex = normalHex;
		for (int im_nhex = 0; im_nhex < CHA_NORMAL_HEX.length; im_nhex++)
			indexHex = indexHex.replace(CHA_NORMAL_HEX[im_nhex], CHA_INDEX_HEX[im_nhex]);
		
		return indexHex;
	}
}
