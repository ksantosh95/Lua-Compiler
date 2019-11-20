package interpreter;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Math;
import java.lang.reflect.Field;

import cop5556fa19.Token;
import cop5556fa19.Token.Kind;
import cop5556fa19.AST.ASTVisitor;
import cop5556fa19.AST.Block;
import cop5556fa19.AST.Chunk;
import cop5556fa19.AST.Exp;
import cop5556fa19.AST.ExpBinary;
import cop5556fa19.AST.ExpFalse;
import cop5556fa19.AST.ExpFunction;
import cop5556fa19.AST.ExpFunctionCall;
import cop5556fa19.AST.ExpInt;
import cop5556fa19.AST.ExpList;
import cop5556fa19.AST.ExpName;
import cop5556fa19.AST.ExpNil;
import cop5556fa19.AST.ExpString;
import cop5556fa19.AST.ExpTable;
import cop5556fa19.AST.ExpTableLookup;
import cop5556fa19.AST.ExpTrue;
import cop5556fa19.AST.ExpUnary;
import cop5556fa19.AST.ExpVarArgs;
import cop5556fa19.AST.FieldExpKey;
import cop5556fa19.AST.FieldImplicitKey;
import cop5556fa19.AST.FieldList;
import cop5556fa19.AST.FieldNameKey;
import cop5556fa19.AST.FuncBody;
import cop5556fa19.AST.FuncName;
import cop5556fa19.AST.Name;
import cop5556fa19.AST.ParList;
import cop5556fa19.AST.RetStat;
import cop5556fa19.AST.Stat;
import cop5556fa19.AST.StatAssign;
import cop5556fa19.AST.StatBreak;
import cop5556fa19.AST.StatDo;
import cop5556fa19.AST.StatFor;
import cop5556fa19.AST.StatForEach;
import cop5556fa19.AST.StatFunction;
import cop5556fa19.AST.StatGoto;
import cop5556fa19.AST.StatIf;
import cop5556fa19.AST.StatLabel;
import cop5556fa19.AST.StatLocalAssign;
import cop5556fa19.AST.StatLocalFunc;
import cop5556fa19.AST.StatRepeat;
import cop5556fa19.AST.StatWhile;
import interpreter.built_ins.toNumber;

public abstract class ASTVisitorAdapter implements ASTVisitor {
	
	@SuppressWarnings("serial")
	public static class StaticSemanticException extends Exception{
		
			public StaticSemanticException(Token first, String msg) {
				super(first.line + ":" + first.pos + " " + msg);
			}
		}
	int return_value =0;
	int do_return=0;
	
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
		return expNil.toString();
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
				
				
					if((a instanceof LuaInt || a instanceof LuaString) && (b instanceof LuaInt || b instanceof LuaString))
					{
						
						int_list = (List<Integer>) to_number(a,b);
						val1=int_list.get(0);
						val2=int_list.get(1);
						boolean ret_value;
						ret_value = (val1==val2);
						LuaBoolean ret_value1 = new LuaBoolean(ret_value);
						return ret_value1;
					}
					else if (a instanceof LuaValue || b instanceof LuaValue)
					{
						throw new TypeException("Value not assigned to variable");	
					}
				break;
			}
			
			case REL_NOTEQ:
			{
				
				
					if((a instanceof LuaInt || a instanceof LuaString) && (b instanceof LuaInt || b instanceof LuaString))
					{
						int_list = (List<Integer>) to_number(a,b);
						val1=int_list.get(0);
						val2=int_list.get(1);
						boolean ret_value;
						ret_value = (val1!=val2);
						LuaBoolean ret_value1 = new LuaBoolean(ret_value);
						return ret_value1;
					}
					else if (a instanceof LuaValue || b instanceof LuaValue)
					{
						throw new TypeException("Value not assigned to variable");	
					}
				break;
			}
			
			case REL_LE:
			{
				
				
					if((a instanceof LuaInt || a instanceof LuaString) && (b instanceof LuaInt || b instanceof LuaString))
					{
						int_list = (List<Integer>) to_number(a,b);
						val1=int_list.get(0);
						val2=int_list.get(1);
						boolean ret_value;
						ret_value = (val1<=val2);
						LuaBoolean ret_value1 = new LuaBoolean(ret_value);
						return ret_value1;
					}
					else if (a instanceof LuaValue || b instanceof LuaValue)
					{
						throw new TypeException("Value not assigned to variable");	
					}
				break;
			}
		
			case REL_GE:
			{
				
				
					if((a instanceof LuaInt || a instanceof LuaString) && (b instanceof LuaInt || b instanceof LuaString))
					{
						int_list = (List<Integer>) to_number(a,b);
						val1=int_list.get(0);
						val2=int_list.get(1);
						boolean ret_value;
						ret_value = (val1>=val2);
						LuaBoolean ret_value1 = new LuaBoolean(ret_value);
						return ret_value1;
					}
					else if (a instanceof LuaValue || b instanceof LuaValue)
					{
						throw new TypeException("Value not assigned to variable");	
					}
				break;
			}
			
			case REL_LT:
			{
				
				
					if((a instanceof LuaInt || a instanceof LuaString) && (b instanceof LuaInt || b instanceof LuaString))
					{
						int_list = (List<Integer>) to_number(a,b);
						val1=int_list.get(0);
						val2=int_list.get(1);
						boolean ret_value;
						ret_value = (val1<val2);
						LuaBoolean ret_value1 = new LuaBoolean(ret_value);
						return ret_value1;
					}
					else if (a instanceof LuaValue || b instanceof LuaValue)
					{
						throw new TypeException("Value not assigned to variable");	
					}
				break;
			}
			
			case REL_GT:
			{
				
				
					if((a instanceof LuaInt || a instanceof LuaString) && (b instanceof LuaInt || b instanceof LuaString))
					{
						int_list = (List<Integer>) to_number(a,b);
						val1=int_list.get(0);
						val2=int_list.get(1);
						boolean ret_value;
						ret_value = (val1>val2);
						LuaBoolean ret_value1 = new LuaBoolean(ret_value);
						return ret_value1;
					}
					else if (a instanceof LuaValue || b instanceof LuaValue)
					{
						throw new TypeException("Value not assigned to variable");	
					}
				break;
			}
			
			case KW_and:
			{
			
				if(a instanceof LuaBoolean || a instanceof LuaNil)
				{
					return a;
				}
				else if ((a instanceof LuaInt || a instanceof LuaString) && (b instanceof LuaBoolean || b instanceof LuaNil))
				{
					return b;
				}
				else if((a instanceof LuaInt || a instanceof LuaString) && (b instanceof LuaInt || b instanceof LuaString))
					{
						int_list = (List<Integer>) to_number(a,b);
						val1=int_list.get(0);
						val2=int_list.get(1);
						
						return val2;
					}
					else if (a instanceof LuaValue || b instanceof LuaValue)
					{
						throw new TypeException("Value not assigned to variable");	
					}
				break;
			}
			
			case KW_or:
			{
				
				
				if(!(a instanceof LuaBoolean) && !(a instanceof LuaNil))
				{
					return a;
				}
				else if ((a instanceof LuaBoolean || a instanceof LuaNil) && (b instanceof LuaBoolean || b instanceof LuaNil))
				{
					return b;
				}
				else if((a instanceof LuaBoolean || a instanceof LuaNil) && (b instanceof LuaInt || b instanceof LuaString))
					{
						int_list = (List<Integer>) to_number(a,b);
						val1=int_list.get(0);
						val2=int_list.get(1);
						
						return val2;
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
		cop5556fa19.AST.Field fval ;
		LuaValue lv ;
		LuaTable lt = new LuaTable();
		List<cop5556fa19.AST.Field> fl = expTableConstr.fields;
		for (int i=0;i<fl.size();i++)
		{
			fval = fl.get(i);
			lt = (LuaTable)fval.visit(this, lt);
			
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
		System.out.println("parlist reached");
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitFunDef(ExpFunction funcDec, Object arg) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitName(Name name, Object arg) {
		return new LuaString(name.name);
		//	throw new UnsupportedOperationException();
	}

	@Override
	public Object visitBlock(Block block, Object arg) throws Exception {
		List<Stat> statement_list = block.stats;
		List<LuaValue> vals=new ArrayList<LuaValue>();
	
		for (Stat s  : statement_list)
		{
			
			 vals=(List<LuaValue>) s.visit(this, arg);
			
			 if(vals!=null)
			 {
				 return vals;
			 }
			 if(do_return==1)
				 break;
			//System.out.println(vals);
		}
		return vals;
		//throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatBreak(StatBreak statBreak, Object arg, Object arg2) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatBreak(StatBreak statBreak, Object arg) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatGoto(StatGoto statGoto, Object arg) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatDo(StatDo statDo, Object arg) throws Exception {
		//System.out.println(statDo.b);
		List<LuaValue> lua_ret_list=new ArrayList<LuaValue>();
		Block b = statDo.b;
		lua_ret_list = (List<LuaValue>)b.visit(this, arg);
		if(return_value==1)
		{
			do_return=1;
		}
		return lua_ret_list;
		//throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatWhile(StatWhile statWhile, Object arg) throws Exception {
		Exp e = statWhile.e;
		Block b = statWhile.b;
		List<LuaValue> bval = new ArrayList<LuaValue>();
		LuaValue eval = (LuaValue)e.visit(this, arg);
		if(eval instanceof LuaBoolean)
		{
			while(((LuaBoolean)eval).value)
			{
				bval = (List<LuaValue>)b.visit(this, arg);
				 eval = (LuaValue)e.visit(this, arg);
			}
		}
		else if(eval instanceof LuaInt)
		{
			while(((LuaInt)eval).intValue()==0)
			{
				bval = (List<LuaValue>)b.visit(this, arg);
				 eval = (LuaValue)e.visit(this, arg);
			}
		}
		else
		{
			throw new TypeException("Incorrect values specified in while loop");
		}
		
		return null;
		//throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatRepeat(StatRepeat statRepeat, Object arg) throws Exception {
		Exp e = statRepeat.e;
		Block b = statRepeat.b;
		LuaValue bv,ev;
		Boolean loop_control=true;
		
		do
		{
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
		
		return null;
	}

	@Override
	public Object visitStatIf(StatIf statIf, Object arg) throws Exception {
		List<Exp>exp_list = statIf.es;
		List<Block> block_list = statIf.bs;
		Exp e;
		Block b;
		int i;
		LuaValue check;
		List<LuaValue> exec = new ArrayList<LuaValue>();
		boolean scope = true;
		
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
			if(val instanceof LuaTable)
			{
				LuaValue k = (LuaValue)(((ExpTableLookup)e).key).visit(this, arg);
				if(k==LuaNil.nil)
				{
					LuaString s = new LuaString(((ExpName)((ExpTableLookup)e).key).name);
					k=s; 
				}
				val = ((LuaTable)val).get(k);
			}
				lua_ret_list.add(val);
			
			
				
			
		}
		return_value=1;
		return lua_ret_list;
		//throw new UnsupportedOperationException();
	}

	@Override
	public Object visitChunk(Chunk chunk, Object arg) throws Exception {
		
		List<LuaValue> vals=new ArrayList<LuaValue>();
	
	
		vals = (List<LuaValue>)visitBlock(chunk.block,arg);
		return vals;
		//throw new UnsupportedOperationException();
	}

	@Override
	public Object visitFieldExpKey(FieldExpKey fieldExpKey, Object object) throws Exception {
		Exp key = fieldExpKey.key;
		Exp val = fieldExpKey.value;
		
		LuaValue k = (LuaValue)key.visit(this, object);
		LuaValue v = (LuaValue)val.visit(this, object);
		((LuaTable)object).put(k, v);
		
		
		return object;
		
		//		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitFieldNameKey(FieldNameKey fieldNameKey, Object arg) throws Exception {
		Name key = fieldNameKey.name;
		Exp val = fieldNameKey.exp;
		
		LuaValue k = (LuaValue)key.visit(this, arg);
		
		LuaValue v = (LuaValue)val.visit(this, arg);
		((LuaTable)arg).put(k, v);
		
		
		return arg;
		//throw new UnsupportedOperationException();
	}
	
	@Override
	public Object visitFieldImplicitKey(FieldImplicitKey fieldImplicitKey, Object arg) throws Exception {
		Exp val =fieldImplicitKey.exp;
		LuaValue v = (LuaValue)val.visit(this, arg);
		((LuaTable)arg).putImplicit(v);
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
		System.out.println("reached expvarargs");
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
		if(varlist.size()==explist.size())
		{
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
				
				lt = (LuaTable)((LuaValue)et.visit(this, arg));
				//System.out.println(var);
				Exp e = explist.get(i);
				
				exp.add((LuaValue)e.visit(this, arg));
				((LuaTable)lt).put(k, exp.get(i));
				
			}
			
			
			
		
			
			
			//((LuaTable)arg).put(var.get(i), exp.get(i));
			//System.out.println(((LuaTable)arg).get(var.get(i)));
		//	System.out.println(arg);
		
		}
		}
		else
		{
			throw new UnsupportedOperationException();
		}
		
		return null;
		//throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpTableLookup(ExpTableLookup expTableLookup, Object arg) throws Exception {
		Exp table = expTableLookup.table;
		Exp key = expTableLookup.key;
		LuaValue k = (LuaValue)key.visit(this,arg);
		
		LuaValue nm = (LuaValue)table.visit(this, arg);
		
		if(nm instanceof LuaTable)
		{
			((LuaTable)nm).get(k);
				
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
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitLabel(StatLabel statLabel, Object ar) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitFieldList(FieldList fieldList, Object arg) {
		System.out.println("fieldlist");
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpName(ExpName expName, Object arg) {
		LuaString s = new LuaString(expName.name) ;
		
		LuaValue val1 = ((LuaTable)arg).get(s);
		
		
		if(val1!=LuaNil.nil)
		{
			return val1;
		}
		
		//LuaValue val = (LuaValue)s;
		
		return LuaNil.nil;
		
		//throw new UnsupportedOperationException();
	}



}
