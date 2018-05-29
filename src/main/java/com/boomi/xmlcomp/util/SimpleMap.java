package com.boomi.xmlcomp.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class SimpleMap
{
	private ArrayList<String> keys;
	private ArrayList<String> values;

	public SimpleMap()
	{
		keys = new ArrayList<String>();
		values = new ArrayList<String>();
	}

	public void put(String s, String v)
	{
		keys.add(s);
		values.add(v);
	}

	public ArrayList<String> get(String k)
	{
		ArrayList<String> result = new ArrayList<String>();
		for (int c = 0; c < keys.size(); c++)
			if (keys.get(c).equals(k)) result.add(values.get(c));

		return result;
	}

	public ArrayList<String> keys()
	{
		return keys;
	}

	public ArrayList<String> uniqueKeys()
	{

		// ... the list is already populated
		Set<String> s = new TreeSet<String>(new Comparator<String>()
		{

			@Override
			public int compare(String o1, String o2)
			{
				if (o1.equals(o2))
					return 0;
				return 1;
			}
		});
		s.addAll(keys);
		
		ArrayList<String> result = new ArrayList<String>();
		
		for (String str : s)
			result.add(str);
				
		return result;
	}
	
	public ArrayList<String> getCompleteItemList()
	{
		ArrayList<String> res = new ArrayList<String>();
		for (String key : uniqueKeys())
			for (String val : get(key))
				res.add(key + " :: " + val);
		
		return res;
	}
}
