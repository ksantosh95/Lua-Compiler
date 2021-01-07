package interpreter;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Math;
import java.lang.reflect.Field;

import luaCompiler.Token;
import luaCompiler.Token.Kind;
import luaCompiler.AST.ASTVisitor;
import luaCompiler.AST.Block;
import luaCompiler.AST.Chunk;
import luaCompiler.AST.Exp;
import luaCompiler.AST.ExpBinary;
import luaCompiler.AST.ExpFalse;
import luaCompiler.AST.ExpFunction;
import luaCompiler.AST.ExpFunctionCall;
import luaCompiler.AST.ExpInt;
import luaCompiler.AST.ExpList;
import luaCompiler.AST.ExpName;
import luaCompiler.AST.ExpNil;
import luaCompiler.AST.ExpString;
import luaCompiler.AST.ExpTable;
import luaCompiler.AST.ExpTableLookup;
import luaCompiler.AST.ExpTrue;
import luaCompiler.AST.ExpUnary;
import luaCompiler.AST.ExpVarArgs;
import luaCompiler.AST.FieldExpKey;
import luaCompiler.AST.FieldImplicitKey;
import luaCompiler.AST.FieldList;
import luaCompiler.AST.FieldNameKey;
import luaCompiler.AST.FuncBody;
import luaCompiler.AST.FuncName;
import luaCompiler.AST.Name;
import luaCompiler.AST.ParList;
import luaCompiler.AST.RetStat;
import luaCompiler.AST.Stat;
import luaCompiler.AST.StatAssign;
import luaCompiler.AST.StatBreak;
import luaCompiler.AST.StatDo;
import luaCompiler.AST.StatFor;
import luaCompiler.AST.StatForEach;
import luaCompiler.AST.StatFunction;
import luaCompiler.AST.StatGoto;
import luaCompiler.AST.StatIf;
import luaCompiler.AST.StatLabel;
import luaCompiler.AST.StatLocalAssign;
import luaCompiler.AST.StatLocalFunc;
import luaCompiler.AST.StatRepeat;
import luaCompiler.AST.StatWhile;
import interpreter.built_ins.print;
import interpreter.built_ins.println;
import interpreter.built_ins.toNumber;

public abstract class ASTVisitorAdapter implements ASTVisitor {
	
	/*@SuppressWarnings("serial")
	public static class StaticSemanticException extends Exception{
		
			public StaticSemanticException(Token first, String msg) {
				super(first.line + ":" + first.pos + " " + msg);
			}
		}*/
	
	@SuppressWarnings("serial")
	public static class GotoException extends Exception{
		
			public GotoException() {
				
			}
		}
	
	@SuppressWarnings("serial")
	public static class BreakException extends Exception{
		
			public BreakException() {
				
			}
		}
	
	
	int return_value =0;
	int do_return=0;
	Boolean jump = false;
	Boolean go = true;
	StatLabel s_label = new StatLabel(null,null,null,0);
	SymbolTable symtable = new SymbolTable();
	Name goto_label_name;
	int global_block_index=0;
	Boolean loop_body=false;
	Block global_block;
	int run_value=1;
	
	@SuppressWarnings("serial")
	public
	static class TypeException extends Exception{

		public TypeException(String msg) {
			super(msg);
		}
		
		public TypeException(Token first, String msg) {
			super(first.line + ":" + first.pos + " " + msg);
		}
		
	}
	
	public abstract List<LuaValue> load(Reader r) throws Exception;

	@Override
	public Object visitExpNil(ExpNil expNil, Object arg) {
		return  LuaNil.nil;
		//throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpBin(ExpBinary expBin, Object arg) throws Exception {
		Exp e0,e1;
		Kind op;
		e0= expBin.e0;
		e1= expBin.e1;
		op = expBin.op;
		LuaValue a,b,ret=null;
		int s ;
		toNumber to_num = new toNumber();
		List<Integer> int_list = new ArrayList<Integer>();
		int val1,val2;
		
		
		a = (LuaValue)e0.visit(this, arg);
		b = (LuaValue)e1.visit(this, arg);
		
		
		
		switch(op)
		{
			case OP_PLUS:
			{
				
				
					if((a instanceof LuaInt || a instanceof LuaString) && (b instanceof LuaInt || b instanceof LuaString))
					{
						int_list = (List<Integer>) to_number(a,b);
						val1=int_list.get(0);
						val2=int_list.get(1);
						
						 s = val1 + val2;
						ret = new LuaInt(s);
						return ret;
					}
					else if (a instanceof LuaValue || b instanceof LuaValue)
					{
						throw new TypeException("Value not assigned to variable");	
					}
				break;
			}
			case OP_MINUS:
			{
				
				
					if((a instanceof LuaInt || a instanceof LuaString) && (b instanceof LuaInt || b instanceof LuaString))
					{
						int_list = (List<Integer>) to_number(a,b);
						val1=int_list.get(0);
						val2=int_list.get(1);
						
						 s = val1 - val2;
						ret = new LuaInt(s);
						return ret;
					}
					else if (a instanceof LuaValue || b instanceof LuaValue)
					{
						throw new TypeException("Value not assigned to variable");	
					}
				break;
			}
			
			case OP_TIMES:
			{
				
				
					if((a instanceof LuaInt || a instanceof LuaString) && (b instanceof LuaInt || b instanceof LuaString))
					{
						int_list = (List<Integer>) to_number(a,b);
						val1=int_list.get(0);
						val2=int_list.get(1);
						
						 s = val1 * val2;
						ret = new LuaInt(s);
						return ret;
					}
					else if (a instanceof LuaValue || b instanceof LuaValue)
					{
						throw new TypeException("Value not assigned to variable");	
					}
				break;
			}
			
			case OP_DIV:
			{
				
				
					if((a instanceof LuaInt || a instanceof LuaString) && (b instanceof LuaInt || b instanceof LuaString))
					{
						int_list = (List<Integer>) to_number(a,b);
						val1=int_list.get(0);
						val2=int_list.get(1);
						
						 s = val1 / val2;
						ret = new LuaInt(s);
						return ret;
					}
					else if (a instanceof LuaValue || b instanceof LuaValue)
					{
						throw new TypeException("Value not assigned to variable");	
					}
				break;
			}
			case OP_DIVDIV:
			{
				
					if((a instanceof LuaInt || a instanceof LuaString) && (b instanceof LuaInt || b instanceof LuaString))
					{
						int_list = (List<Integer>) to_number(a,b);
						val1=int_list.get(0);
						val2=int_list.get(1);
						
						
						s = Math.floorDiv(val1, val2);
						// s = ((LuaInt) list1.get(0)).intValue() + ((LuaInt) list2.get(0)).intValue();
						ret = new LuaInt(s);
						return ret;
					}
					else if (a instanceof LuaValue || b instanceof LuaValue)
					{
						throw new TypeException("Value not assigned to variable");	
					}
				break;
			}
			
			case OP_MOD:
			{
				
					if((a instanceof LuaInt || a instanceof LuaString) && (b instanceof LuaInt || b instanceof LuaString))
					{
						int_list = (List<Integer>) to_number(a,b);
						val1=int_list.get(0);
						val2=int_list.get(1);
						
						 s = val1 % val2;
						ret = new LuaInt(s);
						return ret;
					}
					else if (a instanceof LuaValue || b instanceof LuaValue)
					{
						throw new TypeException("Value not assigned to variable");	
					}
				break;
			}
			
			case OP_POW:
			{
				
					if((a instanceof LuaInt || a instanceof LuaString) && (b instanceof LuaInt || b instanceof LuaString))
					{
						int_list = (List<Integer>) to_number(a,b);
						val1=int_list.get(0);
						val2=int_list.get(1);
						
						 s = (int) Math.pow(val1, val2);
						 
						ret = new LuaInt(s);
						
						return ret;	
					}
					else if (a instanceof LuaValue || b instanceof LuaValue)
					{
						throw new TypeException("Value not assigned to variable");	
					}
				break;
			}
			
			case BIT_AMP:
			{
			
				
					if((a instanceof LuaInt || a instanceof LuaString) && (b instanceof LuaInt || b instanceof LuaString))
					{
						int_list = (List<Integer>) to_number(a,b);
						val1=int_list.get(0);
						val2=int_list.get(1);
						
						 s = val1 & val2;
						ret = new LuaInt(s);
						return ret;
					}
					else if (a instanceof LuaValue || b instanceof LuaValue)
					{
						throw new TypeException("Value not assigned to variable");	
					}
				break;
			}
			
			case BIT_OR:
			{
			
				
					if((a instanceof LuaInt || a instanceof LuaString) && (b instanceof LuaInt || b instanceof LuaString))
					{
						int_list = (List<Integer>) to_number(a,b);
						val1=int_list.get(0);
						val2=int_list.get(1);
						
						 s = val1 | val2;
						ret = new LuaInt(s);
						return ret;
					}
					else if (a instanceof LuaValue || b instanceof LuaValue)
					{
						throw new TypeException("Value not assigned to variable");	
					}
				break;
			}
			
			case BIT_XOR:
			{
				
					if((a instanceof LuaInt || a instanceof LuaString) && (b instanceof LuaInt || b instanceof LuaString))
					{
						int_list = (List<Integer>) to_number(a,b);
						val1=int_list.get(0);
						val2=int_list.get(1);
						
						 s = val1 ^ val2;
						ret = new LuaInt(s);
						return ret;
					}
					else if (a instanceof LuaValue || b instanceof LuaValue)
					{
						throw new TypeException("Value not assigned to variable");	
					}
				break;
			}
			
			case BIT_SHIFTL:
			{
			
				
					if((a instanceof LuaInt || a instanceof LuaString) && (b instanceof LuaInt || b instanceof LuaString))
					{
						int_list = (List<Integer>) to_number(a,b);
						val1=int_list.get(0);
						val2=int_list.get(1);
						
						 s = val1 << val2;
						ret = new LuaInt(s);
						return ret;
					}
					else if (a instanceof LuaValue || b instanceof LuaValue)
					{
						throw new TypeException("Value not assigned to variable");	
					}
				break;
			}
			
			case BIT_SHIFTR:
			{
			
				
					if((a instanceof LuaInt || a instanceof LuaString) && (b instanceof LuaInt || b instanceof LuaString))
					{
						int_list = (List<Integer>) to_number(a,b);
						val1=int_list.get(0);
						val2=int_list.get(1);
						 s = val1 >> val2;
						ret = new LuaInt(s);
						return ret;
					}
					else if (a instanceof LuaValue || b instanceof LuaValue)
					{
						throw new TypeException("Value not assigned to variable");	
					}
				break;
			}
			
			case REL_EQEQ:
			{
				
				boolean ret_value;
					if(a instanceof LuaInt   && b instanceof LuaInt )
					{
						
						int_list = (List<Integer>) to_number(a,b);
						val1=int_list.get(0);
						val2=int_list.get(1);
						
						ret_value = (val1==val2);
						LuaBoolean ret_value1 = new LuaBoolean(ret_value);
						return ret_value1;
					}
					else if(a instanceof LuaString && b instanceof LuaString)
					{
						int res = (a.toString()).compareTo(b.toString());
						if(res==0)
						{
							ret_value = true;
						}
						else
						{
							ret_value = false;
						}
						return new LuaBoolean(ret_value);
					}
					else if (a instanceof LuaValue || b instanceof LuaValue)
					{
						throw new TypeException("Value not assigned to variable");	
					}
				break;
			}
			
			case REL_NOTEQ:
			{
						
				boolean ret_value;
				if(a instanceof LuaInt   && b instanceof LuaInt )
				{
					
					int_list = (List<Integer>) to_number(a,b);
					val1=int_list.get(0);
					val2=int_list.get(1);
					
					ret_value = (val1!=val2);
					LuaBoolean ret_value1 = new LuaBoolean(ret_value);
					return ret_value1;
				}
				else if(a instanceof LuaString && b instanceof LuaString)
				{
					int res = (a.toString()).compareTo(b.toString());
					if(res!=0)
					{
						ret_value = true;
					}
					else
					{
						ret_value = false;
					}
					return new LuaBoolean(ret_value);
				}
				else if (a instanceof LuaValue || b instanceof LuaValue)
				{
					throw new TypeException("Value not assigned to variable");	
				}
			break;
			}
			
			case REL_LE:
			{
				
				
				boolean ret_value;
				if(a instanceof LuaInt   && b instanceof LuaInt )
				{
					
					int_list = (List<Integer>) to_number(a,b);
					val1=int_list.get(0);
					val2=int_list.get(1);
					
					ret_value = (val1<=val2);
					LuaBoolean ret_value1 = new LuaBoolean(ret_value);
					return ret_value1;
				}
				else if(a instanceof LuaString && b instanceof LuaString)
				{
					int res = (a.toString()).compareTo(b.toString());
					if(res<=0)
					{
						ret_value = true;
					}
					else
					{
						ret_value = false;
					}
					return new LuaBoolean(ret_value);
				}
				else if (a instanceof LuaValue || b instanceof LuaValue)
				{
					throw new TypeException("Value not assigned to variable");	
				}
			break;
			}
		
			case REL_GE:
			{
				
				
				boolean ret_value;
				if(a instanceof LuaInt   && b instanceof LuaInt )
				{
					
					int_list = (List<Integer>) to_number(a,b);
					val1=int_list.get(0);
					val2=int_list.get(1);
					
					ret_value = (val1>=val2);
					LuaBoolean ret_value1 = new LuaBoolean(ret_value);
					return ret_value1;
				}
				else if(a instanceof LuaString && b instanceof LuaString)
				{
					int res = (a.toString()).compareTo(b.toString());
					if(res>=0)
					{
						ret_value = true;
					}
					else
					{
						ret_value = false;
					}
					return new LuaBoolean(ret_value);
				}
				else if (a instanceof LuaValue || b instanceof LuaValue)
				{
					throw new TypeException("Value not assigned to variable");	
				}
			break;
			}
			
			case REL_LT:
			{
				boolean ret_value;
				if(a instanceof LuaInt   && b instanceof LuaInt )
				{
					
					int_list = (List<Integer>) to_number(a,b);
					val1=int_list.get(0);
					val2=int_list.get(1);
					
					ret_value = (val1<val2);
					LuaBoolean ret_value1 = new LuaBoolean(ret_value);
					return ret_value1;
				}
				else if(a instanceof LuaString && b instanceof LuaString)
				{
					int res = (a.toString()).compareTo(b.toString());
					if(res<0)
					{
						ret_value = true;
					}
					else
					{
						ret_value = false;
					}
					return new LuaBoolean(ret_value);
				}
				else if (a instanceof LuaValue || b instanceof LuaValue)
				{
					throw new TypeException("Value not assigned to variable");	
				}
			break;
			}
			
			case REL_GT:
			{
				boolean ret_value;
				if(a instanceof LuaInt   && b instanceof LuaInt )
				{
					
					int_list = (List<Integer>) to_number(a,b);
					val1=int_list.get(0);
					val2=int_list.get(1);
					
					ret_value = (val1>val2);
					LuaBoolean ret_value1 = new LuaBoolean(ret_value);
					return ret_value1;
				}
				else if(a instanceof LuaString && b instanceof LuaString)
				{
					int res = (a.toString()).compareTo(b.toString());
					if(res>0)
					{
						ret_value = true;
					}
					else
					{
						ret_value = false;
					}
					return new LuaBoolean(ret_value);
				}
				else if (a instanceof LuaValue || b instanceof LuaValue)
				{
					throw new TypeException("Value not assigned to variable");	
				}
			break;
			}
			
			case KW_and:
			{
			
				if( a instanceof LuaNil)
				{
					return a;
				}
				else if(a instanceof LuaBoolean)
				{
					if(((LuaBoolean) a).value==false)
					{
					return a;	
					}
					else
					{
						return b;
					}
				}
				else if ((a instanceof LuaInt || a instanceof LuaString) )
				{
					return b;
				}
				
					else if (a instanceof LuaValue || b instanceof LuaValue)
					{
						throw new TypeException("Value not assigned to variable");	
					}
				break;
			}
			
			case KW_or:
			{
				
				
				if( a instanceof LuaNil)
				{
					return b;
				}
				else if(a instanceof LuaBoolean)
				{
					if(((LuaBoolean) a).value==false)
					{
					return b;	
					}
					else
					{
						return a;
					}
				}
				else if((a instanceof LuaInt || a instanceof LuaString))
					{
					return a;
					}
					else if (a instanceof LuaValue || b instanceof LuaValue)
					{
						throw new TypeException("Value not assigned to variable");	
					}
				break;
			}
			
			case DOTDOT:
			{
				
				
					if((a instanceof LuaInt || a instanceof LuaString) && (b instanceof LuaInt || b instanceof LuaString))
					{
						if(a instanceof LuaInt)
						{
							
							LuaString ls1 = new LuaString(Integer.toString(((LuaInt)a).intValue()));
							a = ls1;
						}
						if(b instanceof LuaInt)
						{
							LuaString ls1 = new LuaString(Integer.toString(((LuaInt)b).intValue()));
							b = ls1;
						}
						
						
						String rets = (a.toString()).concat(b.toString());
						
						return new LuaString(rets);
					}
					else if (a instanceof LuaValue || b instanceof LuaValue)
					{
						throw new TypeException("Value not assigned to variable");	
					}
				break;
			}
			
		default:
	   return ret;
		}
		return null;
		//throw new UnsupportedOperationException();
	}

	public Object to_number(LuaValue a,LuaValue b) throws Exception
	{
		
		List<Integer> x = new ArrayList<Integer>();
		toNumber to_num = new toNumber();
		List<LuaValue> list1=new ArrayList<LuaValue>();
		List<LuaValue> list2=new ArrayList<LuaValue>();
		list1.add(a);
		list2.add(b);
		
		list1= to_num.call(list1);
		list2= to_num.call(list2);
		x.add(((LuaInt) list1.get(0)).intValue());
		x.add(((LuaInt) list2.get(0)).intValue());
		return x;
	}
	
	
	
	
	@Override
	public Object visitUnExp(ExpUnary unExp, Object arg) throws Exception {
		Exp e = unExp.e;
		Kind op = unExp.op;
		LuaValue a;
		List<Integer> int_list = new ArrayList<Integer>();
		int val1;
		int s;
		LuaInt ret;
		
		switch(op)
		{
		
		case OP_MINUS:
		{
			a = (LuaValue)e.visit(this, arg);
				
			
				if((a instanceof LuaInt || a instanceof LuaString))
				{
					int_list = (List<Integer>) to_number(a,a);
					val1=int_list.get(0);
					
					
					 
					ret = new LuaInt(-(val1));
					
					return ret;
				}
				else if (a instanceof LuaValue )
				{
					throw new TypeException("Value not assigned to variable");	
				}
			break;
		}
		case BIT_XOR:
		{
			a = (LuaValue)e.visit(this, arg);
			
			
				if((a instanceof LuaInt || a instanceof LuaString))
				{
					int_list = (List<Integer>) to_number(a,a);
					val1=int_list.get(0);
					
					
					 
					ret = new LuaInt(~(val1));
					return ret;
				}
				else if (a instanceof LuaValue )
				{
					throw new TypeException("Value not assigned to variable");	
				}
			break;
		}
		
		case KW_not:
		{
			a = (LuaValue)e.visit(this, arg);
			
			if(a instanceof LuaBoolean)
			{
				Boolean x= ((LuaBoolean) a).value;
				return !x;
			}
			else if(a instanceof LuaNil)
			{
				return new LuaBoolean(true);
			}
			
			else if((a instanceof LuaInt || a instanceof LuaString))
				{
					int_list = (List<Integer>) to_number(a,a);
					val1=int_list.get(0);
					
					
					 
					return new LuaBoolean(false);
				}
				else if (a instanceof LuaValue )
				{
					throw new TypeException("Value not assigned to variable");	
				}
			break;
		}
		
		case OP_HASH:
		{
			a = (LuaValue)e.visit(this, arg);
			
			
				if((a instanceof LuaInt || a instanceof LuaString))
				{
					if(a instanceof LuaInt)
					{
						
						//LuaString ls1 = new LuaString(Integer.toString(((LuaInt)a).intValue()));
						//a = ls1;
						throw new TypeException("# operator does not work on integer values");
					}
					
					
					LuaInt reti = new LuaInt((((LuaString)a).toString()).length()); 
					
					return reti;
				}
				else if(a instanceof LuaTable)
				{
					
					LuaInt s1,s2;
					LuaValue v1,v2;
					
					for(int i=1;i<=((LuaTable)a).arraySize;i++)
					{
						 s1 = new LuaInt(i);
						 s2 = new LuaInt(i+1); 
						 v1 = ((LuaTable)a).get(s1);
						 v2 = ((LuaTable)a).get(s2);
						 
						 if(v1!=LuaNil.nil && v2.equals(LuaNil.nil))
						 {
							 
							 return s1;
						 }
					}
				}
				else if (a instanceof LuaValue )
				{
					throw new TypeException("Value not assigned to variable");	
				}
			break;
		}
		
		default:return null;
		}
		
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpInt(ExpInt expInt, Object arg) {
		
		LuaInt v = new LuaInt(expInt.v);
		return v;
		
		//throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpString(ExpString expString, Object arg) {
		LuaString s = new LuaString(expString.v) ;
		return s;
		//throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpTable(ExpTable expTableConstr, Object arg) throws Exception {
		luaCompiler.AST.Field fval ;
		LuaValue lv ;
		LuaTable lt = new LuaTable();
		List<LuaTable> lt_list = new ArrayList<LuaTable>();
		lt_list.add(((LuaTable)arg));
		lt_list.add(lt);
		
		List<luaCompiler.AST.Field> fl = expTableConstr.fields;
		
		for (int i=0;i<fl.size();i++)
		{
			fval = fl.get(i);
		
			 lt_list= (List<LuaTable>)fval.visit(this, lt_list);
			
		
			
			 lt = lt_list.get(1);	 
			 
			 
			 
		}
		
		return lt;
		//throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpList(ExpList expList, Object arg) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitParList(ParList parList, Object arg) throws Exception {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitFunDef(ExpFunction funcDec, Object arg) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitName(Name name, Object arg) {
		
		
LuaString s = new LuaString(name.name) ;

		
		
		
		
		LuaValue val1 = ((LuaTable)arg).get(s);
		LuaValue retval =s;
		
		if(val1!=LuaNil.nil)
		{
			 retval = val1;
		}
		
		//LuaValue val = (LuaValue)s;
		
		return retval;
		//	throw new UnsupportedOperationException();
	}

	@Override
	public Object visitBlock(Block block, Object arg) throws Exception {
		List<Stat> statement_list = block.stats;
		List<LuaValue> vals=new ArrayList<LuaValue>();
		vals=null;
		int block_index= global_block_index;
		
	try {
		
		for (Stat s  : statement_list)
		{	
			
			
			if(s instanceof StatLabel)
			{
				
				s.visit(this, arg);
			}
			
			if(!jump)
			{
					
			vals=(List<LuaValue>) s.visit(this, arg);
			
			
			 if(vals!=null )
			 {
				 return vals;
			 }
			 if(do_return==1)
				 break;
			}
			
			//System.out.println(vals);
		}
		if(jump)
		{
			if(run_value==2)
			{
				
				return null;
			}
			throw new StaticSemanticException(block.firstToken,"");
		}
		
		return vals;
		
	}
	catch(GotoException e)
	{
	
			vals= (List<LuaValue>)visitBlock(block,arg);
			if(vals==null && run_value==2 && (block_index!=0))
			{
				if(block_index==1)
				{
					run_value=1;
				}
				throw new GotoException();
			}
			
				
			return vals;
		
		
	}
	catch(StaticSemanticException f)
	{
		if(block_index==global_block_index)
		{
			throw new StaticSemanticException(block.firstToken,""); 
		}
		else
		{
			for (Stat s  : statement_list)
			{	 
				if(s instanceof StatLabel)
				{
					s.visit(this, arg);
				}
				if(!jump)
				{
					
				vals=(List<LuaValue>) s.visit(this, arg);
				 if(vals!=null)
				 {
					 return vals;
				 }
				 if(do_return==1)
					 break;
				}
				//System.out.println(vals);
			}
			if(jump)
			{
				
				throw new StaticSemanticException(block.firstToken,"");
			}
			return vals;
		}
	}
	
		//throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatBreak(StatBreak statBreak, Object arg, Object arg2) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatBreak(StatBreak statBreak, Object arg) throws Exception {
		throw new BreakException();
		//throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatGoto(StatGoto statGoto, Object arg) throws Exception {
		goto_label_name = statGoto.name;
		run_value=2;
		jump = true;
		throw new GotoException();
		//	throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatDo(StatDo statDo, Object arg) throws Exception {
		//System.out.println(statDo.b);
		global_block_index++;
		List<LuaValue> lua_ret_list=new ArrayList<LuaValue>();
		Block b = statDo.b;
		
		try {
		lua_ret_list = (List<LuaValue>)b.visit(this, arg);
		if(return_value==1)
		{
			do_return=1;
		}
		
		return lua_ret_list;
		}
		catch(BreakException be)
		{
			if(loop_body)
			{
				throw new BreakException();
			}
		}
		return null;
		//throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatWhile(StatWhile statWhile, Object arg) throws Exception {
		global_block_index++;
		
			
		Exp e = statWhile.e;
		Block b = statWhile.b;
		List<LuaValue> bval = new ArrayList<LuaValue>();
		LuaValue eval = (LuaValue)e.visit(this, arg);
		try {
		if(eval instanceof LuaBoolean)
		{
			while(((LuaBoolean)eval).value)
			{
				loop_body=true;
				bval = (List<LuaValue>)b.visit(this, arg);
				 eval = (LuaValue)e.visit(this, arg);
			}
		}
		else if(eval instanceof LuaInt)
		{
			while(((LuaInt)eval).intValue()==0)
			{
				loop_body=true;
				bval = (List<LuaValue>)b.visit(this, arg);
				 eval = (LuaValue)e.visit(this, arg);
			}
		}
		else
		{
			throw new TypeException("Incorrect values specified in while loop");
		}
		loop_body=false;
		return bval;
		}
		catch(BreakException be) {
			loop_body=false;
			return null	;
		}
		//throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatRepeat(StatRepeat statRepeat, Object arg) throws Exception {
		global_block_index++;
		Exp e = statRepeat.e;
		Block b = statRepeat.b;
		LuaValue bv,ev;
		Boolean loop_control=true;
		try {
		do
		{
			loop_body=true;
			bv= (LuaValue)b.visit(this, arg);
			ev= (LuaValue)e.visit(this, arg);
			
			 if(ev instanceof LuaInt)
			{
				if(((LuaInt)ev).intValue()==0)
				{
					loop_control=true;
				}
				else
				{
					loop_control=false;
				}
				
			}
			else if(!(ev instanceof LuaBoolean))
			{
				throw new TypeException("Boolean Expression expected inside while loop");
			}
			else
			{
				loop_control= ((LuaBoolean)ev).value;
			}
		}
		while(loop_control);
		loop_body=false;
		return bv;}
		catch(BreakException be) {
			loop_body=false;
			return null	;
		}
	}

	@Override
	public Object visitStatIf(StatIf statIf, Object arg) throws Exception {
		global_block_index++;
		List<Exp>exp_list = statIf.es;
		List<Block> block_list = statIf.bs;
		Exp e;
		Block b;
		int i;
		LuaValue check;
		List<LuaValue> exec = new ArrayList<LuaValue>();
		boolean scope = true;
		try {
			
		
		for(i=0;i<exp_list.size() && scope;i++)
		{
			
			e = exp_list.get(i);
			check= (LuaValue)e.visit(this, arg);
			if(check instanceof LuaBoolean)
			{
			if(((LuaBoolean)check).value)
			{
				scope=false;
				b = block_list.get(i);
				
				exec = (List<LuaValue>)b.visit(this, arg);
				
				if(exec!=null)
				{
					
					return exec;
					
				}
			}
		}
			else 
			{
				
				if(check instanceof LuaInt)
				{
				if(((LuaInt)check).intValue() == 0)
				{
					scope=false;
					b = block_list.get(i);
					
					exec = (List<LuaValue>)b.visit(this, arg);
					
					if(exec!=null)
					{
						return exec;
					}
				}
			}
			}
			
		}
		if(block_list.size()==i+1 && scope)
		{
			b = block_list.get(i);
			exec = (List<LuaValue>)b.visit(this, arg);
			if(exec!=null)
			{
				return exec;
			}
			
		}
		
		
		}
		catch(BreakException be)
		{
		if(loop_body)
		{
			throw new BreakException();
		}
		}
		return null;
		//throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatFor(StatFor statFor1, Object arg) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatForEach(StatForEach statForEach, Object arg) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitFuncName(FuncName funcName, Object arg) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatFunction(StatFunction statFunction, Object arg) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatLocalFunc(StatLocalFunc statLocalFunc, Object arg) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatLocalAssign(StatLocalAssign statLocalAssign, Object arg) throws Exception {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitRetStat(RetStat retStat, Object arg) throws Exception {
		
		List<Exp> exp_list = retStat.el;
		List<LuaValue> lua_ret_list=new ArrayList<LuaValue>();
		LuaValue val,val1 = null;
		for (Exp e : exp_list)
		{
				
			
			val = (LuaValue)e.visit(this,arg);
			
	/*		if(val instanceof LuaTable)
			{
				
				LuaValue k = (LuaValue)(((ExpTableLookup)e).key).visit(this, arg);
				;
				if(k==LuaNil.nil)
				{
					LuaString s = new LuaString(((ExpName)((ExpTableLookup)e).key).name);
					k=s; 
				}
				val = ((LuaTable)val).get(k);
			}*/
			if(val!=null)
			{
				lua_ret_list.add(val);
			}
				
			
			
				
			
		}
		return_value=1;
		return lua_ret_list;
		//throw new UnsupportedOperationException();
	}

	@Override
	public Object visitChunk(Chunk chunk, Object arg) throws Exception {
		symtable = chunk.getSymboltable();
		List<LuaValue> vals=new ArrayList<LuaValue>();
		global_block = chunk.block;
	
		vals = (List<LuaValue>)visitBlock(chunk.block,arg);
		return vals;
		//throw new UnsupportedOperationException();
	}

	@Override
	public Object visitFieldExpKey(FieldExpKey fieldExpKey, Object object) throws Exception {
		Exp key = fieldExpKey.key;
		Exp val = fieldExpKey.value;
		ExpName ename= new ExpName("");
		LuaTable lt = ((List<LuaTable>)object).get(1);
		LuaTable _G = ((List<LuaTable>)object).get(0);
		LuaValue k = (LuaValue)key.visit(this, _G);
		if(k==LuaNil.nil)
		{
			if(key.getClass()==ename.getClass())
			{
				LuaString ls = new LuaString(((ExpName)key).name);
				k = ls;
			}
		}
		
		
		LuaValue v = (LuaValue)val.visit(this, _G);
		lt.put(k, v);
		((List<LuaTable>)object).remove(1);
		((List<LuaTable>)object).add(lt);
		
		return object;
		
		//		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitFieldNameKey(FieldNameKey fieldNameKey, Object arg) throws Exception {
		Name key = fieldNameKey.name;
		Exp val = fieldNameKey.exp;
		//System.out.println(key);
		LuaTable lt = ((List<LuaTable>)arg).get(1);
		LuaTable _G = ((List<LuaTable>)arg).get(0);
		LuaValue k = (LuaValue)key.visit(this, _G);
		
		LuaValue v = (LuaValue)val.visit(this, _G);
		
		lt.put(k, v);
		((List<LuaTable>)arg).remove(1);
		((List<LuaTable>)arg).add(lt);
		
		
		return arg;
		//throw new UnsupportedOperationException();
	}
	
	@Override
	public Object visitFieldImplicitKey(FieldImplicitKey fieldImplicitKey, Object arg) throws Exception {
		Exp val =fieldImplicitKey.exp;
		LuaTable lt = ((List<LuaTable>)arg).get(1);
		LuaTable _G = ((List<LuaTable>)arg).get(0);
		
		LuaValue v = (LuaValue)val.visit(this, _G);
		if(v!=null)
		{
			lt.putImplicit(v);	
		}
		
		
		((List<LuaTable>)arg).remove(1);
		((List<LuaTable>)arg).add(lt);
		
		return arg;
		
	//	throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpTrue(ExpTrue expTrue, Object arg) {
		return new LuaBoolean(true);
	}

	@Override
	public Object visitExpFalse(ExpFalse expFalse, Object arg) {
		return new LuaBoolean(false);
	}

	@Override
	public Object visitFuncBody(FuncBody funcBody, Object arg) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpVarArgs(ExpVarArgs expVarArgs, Object arg) {
		
		
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatAssign(StatAssign statAssign, Object arg) throws Exception {
		List<Exp> varlist = new ArrayList<Exp>();
		List<Exp> explist = new ArrayList<Exp>();
		List<LuaValue> var = new ArrayList<LuaValue>();
		List<LuaValue> exp = new ArrayList<LuaValue>();
		ExpName ename = new ExpName("");
		ExpTableLookup et = new ExpTableLookup(null,null,null);
		LuaString ls ;
		LuaTable lt=new LuaTable();
		
		varlist = statAssign.varList;
		explist = statAssign.expList;
		
		if(varlist.size()>=explist.size())
		{
			
			int size = explist.size();
			varlist = varlist.subList(0, size);
		}
		
		for(int i=0;i<varlist.size();i++)
		{
			
			Exp v = varlist.get(i);
			
			if(v.getClass() == ename.getClass())
			{
				ename=(ExpName)v;
				
				ls = new LuaString(ename.name);
				var.add(ls);
				Exp e = explist.get(i);
				
				exp.add((LuaValue)e.visit(this, arg));
				
				((LuaTable)arg).put(var.get(i), exp.get(i));
				
			}
			else if(v.getClass()==et.getClass())
			{
					
				et = (ExpTableLookup)v;
				
				LuaValue k = (LuaValue)(et.key).visit(this, arg);
				
				LuaValue l = (LuaValue)et.visit(this, arg);
				if(l==LuaNil.nil)
				{
					
					Exp t = et.table;
					
					l = (LuaValue)t.visit(this, arg);
					
					if(l==LuaNil.nil)
					{
						 ls= new LuaString(((ExpName)t).name);
						 
						lt = (LuaTable)((LuaTable)arg).get(ls);
					}
					else if(l instanceof LuaTable)
					{
						lt=(LuaTable)l;
						ls= new LuaString(((ExpName)t).name);
					}
					else
					{
						ls= new LuaString(((ExpName)t).name);
						throw new TypeException("Table Name "+ls.value+" is defined as some other type");
					}
				}
				else
				{
					lt = (LuaTable)((LuaValue)et.visit(this, arg));
					
				}
				
				
				Exp e = explist.get(i);
				
				exp.add((LuaValue)e.visit(this, arg));
				
				((LuaTable)lt).put(k, exp.get(i));
				
			}
			
			
			//System.out.println(lt);
		
			
			
			//((LuaTable)arg).put(var.get(i), exp.get(i));
			//System.out.println(((LuaTable)arg).get(var.get(i)));
		//	System.out.println(arg);
		
		}
		
		
		
		return null;
		//throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpTableLookup(ExpTableLookup expTableLookup, Object arg) throws Exception {
		Exp table = expTableLookup.table;
		Exp key = expTableLookup.key;
		ExpName ename = new ExpName("");
		
		LuaValue k = (LuaValue)key.visit(this,arg);
		
		if(k==LuaNil.nil)
		{
			if(key.getClass()==ename.getClass())
			{
				LuaString ls = new LuaString(((ExpName)key).name);
				k = ls;
			}
		}
		
		LuaValue nm = (LuaValue)table.visit(this, arg);
		
		if(nm instanceof LuaTable)
		{
			 nm=((LuaTable)nm).get(k);
			 
				//System.out.println(k);
			//System.out.println(((LuaTable)nm).get(k));
			return nm;
		}
		else
		{
			throw new TypeException("Attempted Lookup cannot be completed since the table is not initialised");
		}
		
		
		//
	//	throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpFunctionCall(ExpFunctionCall expFunctionCall, Object arg) throws Exception {
			Exp f_name = expFunctionCall.f;
			List<Exp> par = expFunctionCall.args;
			List<LuaValue> l = new ArrayList<LuaValue>();
			List<LuaValue> ret = new ArrayList<LuaValue>();
			ExpName nm = new ExpName("");
			print fun_p = new print();
			println fun_pn = new println();
			toNumber num = new toNumber();
			for (Exp p:par)
			{
				
				l.add((LuaValue)p.visit(this, arg));
				
			}
			if(f_name.getClass()==nm.getClass())
			{
				
				String s = ((ExpName)f_name).name;
				
				if(s.equals("print"))
				{
					
					ret = (List<LuaValue>)fun_p.call(l);
					
					
				}
				else if(s.equals("println"))
				{
					
					ret = (List<LuaValue>)fun_pn.call(l);
				}
				else if(s.equals("toNumber"))
				{
					
					ret = (List<LuaValue>)num.call(l);
					
					return ret.get(0);
				}
				else
				{
					throw new TypeException("Illegal function call");
				}
			}
			else
			{
				throw new TypeException("Illegal function call");
			}
			
			
			
		
		return null;
	}

	@Override
	public Object visitLabel(StatLabel statLabel, Object ar) throws Exception {
		Name key = statLabel.label;
		
		Boolean exists_flg = 	symtable.StatExists(key,statLabel.index);
		
		
		
		if(exists_flg)
		{
			if(goto_label_name != null)
			{
			if((goto_label_name.name).equals(key.name))
			{
				
				jump=false;
				goto_label_name=null;
				run_value=1;
			}
			}
		}
		
		return null;
		//throw new UnsupportedOperationException();
	}

	@Override
	public Object visitFieldList(FieldList fieldList, Object arg) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpName(ExpName expName, Object arg) {
		LuaString s = new LuaString(expName.name) ;
		
		LuaValue val1 = ((LuaTable)arg).get(s);
		
		//System.out.println(((LuaTable)arg));
		if(val1!=LuaNil.nil)
		{
			
			return val1;
		}
		
		//LuaValue val = (LuaValue)s;
		
		return LuaNil.nil;
		
		//throw new UnsupportedOperationException();
	}



}
