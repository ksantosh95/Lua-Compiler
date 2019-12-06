package interpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cop5556fa19.AST.Name;
import cop5556fa19.AST.Stat;
import cop5556fa19.AST.StatLabel;
import interpreter.LuaTable.IllegalTableKeyException;

public class SymbolTable {

	@SuppressWarnings("serial")
	public class IllegalTableKeyException extends RuntimeException {

	}



	static final int DEFAULT_ARRAY_SIZE = 32;
	

	Map<Integer,ArrayList<StatLabel>> map ;

	

	
	public SymbolTable(){
		map = new HashMap<>();
	}
	
	

	public void put(StatLabel key, Integer val) {
		ArrayList<StatLabel> s= new ArrayList<>();
		if (key instanceof StatLabel) {
			
			 ArrayList<StatLabel> m = map.get(val);
			 if(m==null)
			 {s.add(key);}
			 else
				 {
				 s=m;
				 s.add(key);
				 }
			 map.put(val,s);
			
		}
	}
	
	
	public void display()
	{
		
		Set<Integer> statset = map.keySet();
		for (Integer s:statset)
		{
			System.out.println("\nKEY= "+s+" VALUE="+map.get(s));
		}
		
	}
	
	public StatLabel get(Name nm,int ind )
	{
		ArrayList<StatLabel> slist= new ArrayList<>();
		while(ind>=0)
		{
			ArrayList<StatLabel> m = map.get(ind);
			if(m ==null)
			{
				return null;
			}
			else
			{
				slist=m;
			
			for(StatLabel s:slist )
			{
				if((nm.name).equals((s.label).name))
				{
					return s;
				}
			}
			}
			ind--;
		}
		
		return null;
	}
	
	public Boolean StatExists(Name nm, int ind)
	{
		ArrayList<StatLabel> slist= new ArrayList<>();
		while(ind>=0)
		{
			ArrayList<StatLabel> m = map.get(ind);
			if(m ==null)
			{
				return null;
			}
			else
			{
			slist = m;
			for(StatLabel s:slist )
			{
				if((nm.name).equals((s.label).name))
				{
					return true;
				}
			}
			ind=ind-1;
		}
		}
		
		
		return false;
	}
	
}
