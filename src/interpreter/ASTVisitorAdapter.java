package interpreter;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Math;

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
		
		switch(op)
		{
			case OP_PLUS:
			{
				a = (LuaValue)e0.visit(this, arg);
				b = (LuaValue)e1.visit(this, arg);
				
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
				a = (LuaValue)e0.visit(this, arg);
				b = (LuaValue)e1.visit(this, arg);
				
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
				a = (LuaValue)e0.visit(this, arg);
				b = (LuaValue)e1.visit(this, arg);
				
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
				a = (LuaValue)e0.visit(this, arg);
				b = (LuaValue)e1.visit(this, arg);
				
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
				a = (LuaValue)e0.visit(this, arg);
				b = (LuaValue)e1.visit(this, arg);
				
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
				a = (LuaValue)e0.visit(this, arg);
				b = (LuaValue)e1.visit(this, arg);
				
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
				a = (LuaValue)e0.visit(this, arg);
				b = (LuaValue)e1.visit(this, arg);
				
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
				a = (LuaValue)e0.visit(this, arg);
				b = (LuaValue)e1.visit(this, arg);
				
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
				a = (LuaValue)e0.visit(this, arg);
				b = (LuaValue)e1.visit(this, arg);
				
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
				a = (LuaValue)e0.visit(this, arg);
				b = (LuaValue)e1.visit(this, arg);
				
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
				a = (LuaValue)e0.visit(this, arg);
				b = (LuaValue)e1.visit(this, arg);
				
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
		throw new UnsupportedOperationException();
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
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitBlock(Block block, Object arg) throws Exception {
		List<Stat> statement_list = block.stats;
		List<LuaValue> vals=new ArrayList<LuaValue>();
		
		for (Stat s  : statement_list)
		{
			
			 vals = (List<LuaValue>) s.visit(this, arg);
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
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatRepeat(StatRepeat statRepeat, Object arg) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatIf(StatIf statIf, Object arg) throws Exception {
		throw new UnsupportedOperationException();
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
		LuaValue val = null;
		for (Exp e : exp_list)
		{
			val = (LuaValue)e.visit(this,arg);
			
			
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
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitFieldNameKey(FieldNameKey fieldNameKey, Object arg) throws Exception {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Object visitFieldImplicitKey(FieldImplicitKey fieldImplicitKey, Object arg) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpTrue(ExpTrue expTrue, Object arg) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpFalse(ExpFalse expFalse, Object arg) {
		throw new UnsupportedOperationException();
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
		
		
		varlist = statAssign.varList;
		explist = statAssign.expList;
		if(varlist.size()==explist.size())
		{
		for(int i=0;i<varlist.size();i++)
		{
			Exp v = varlist.get(i);
			Exp e = explist.get(i);
			var.add((LuaValue)v.visit(this, arg));
			exp.add((LuaValue)e.visit(this, arg));
			
			
			
			((LuaTable)arg).put(var.get(i), exp.get(i));
			
		
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
		throw new UnsupportedOperationException();
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
		
		LuaValue val = (LuaValue)s;
		
		return val;
		
		//throw new UnsupportedOperationException();
	}



}
