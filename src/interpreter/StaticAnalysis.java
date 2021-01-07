package interpreter;

import java.util.ArrayList;
import java.util.List;

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
import luaCompiler.AST.FunctionCall;
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
import interpreter.ASTVisitorAdapter.TypeException;

public class StaticAnalysis implements ASTVisitor {
	
	
	int global_block_index=0;
	Boolean goto_traverse = false;
	Boolean label_traverse = false;
	SymbolTable symtable = new SymbolTable();
	int k;

	@Override
	public Object visitExpNil(ExpNil expNil, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitExpBin(ExpBinary expBin, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitUnExp(ExpUnary unExp, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitExpInt(ExpInt expInt, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitExpString(ExpString expString, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitExpTable(ExpTable expTableConstr, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitExpList(ExpList expList, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitParList(ParList parList, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitFunDef(ExpFunction funcDec, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitName(Name name, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitBlock(Block block, Object arg) throws Exception {
		List<Stat> statement_list = block.stats;
		//System.out.println("\n "+block.stats);
		List<LuaValue> vals=new ArrayList<LuaValue>();
		Stat s ;
		int block_index = global_block_index;
		
		for (int i=0;i < statement_list.size();i++)
		{
			
			s=statement_list.get(i);
			
			s.visit(this,block_index);
			
		}
		
		
		
		return null;
	}

	@Override
	public Object visitStatBreak(StatBreak statBreak, Object arg, Object arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitStatBreak(StatBreak statBreak, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitStatGoto(StatGoto statGoto, Object arg) throws Exception {
		if(goto_traverse)
		{
			
			StatLabel slabel = symtable.get(statGoto.name, (Integer)arg);
			
			
			statGoto.label=slabel;
			
			
		}
	
		
		
		return null;
	}

	@Override
	public Object visitStatDo(StatDo statDo, Object arg) throws Exception {
		global_block_index++;
		Block b = statDo.b;
		b.visit(this, arg);
		return null;
	}

	@Override
	public Object visitStatWhile(StatWhile statWhile, Object arg) throws Exception {
		global_block_index++;
		Block b = statWhile.b;
		b.visit(this, arg);
		return null;
	}

	@Override
	public Object visitStatRepeat(StatRepeat statRepeat, Object arg) throws Exception {
		global_block_index++;
		Block b = statRepeat.b;
		b.visit(this, arg);
		return null;
	}

	@Override
	public Object visitStatIf(StatIf statIf, Object arg) throws Exception {
		
		global_block_index++;
	
		List<Block> block_list = statIf.bs;
		
		Block b;
		int j;
		
		
		
		for(j=0;j<block_list.size();j++)
		{
			
			
				b = block_list.get(j);
				
				b.visit(this, arg);
				
				
			
		}
		
			
		
		
		return null;
	}

	@Override
	public Object visitStatFor(StatFor statFor1, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitStatForEach(StatForEach statForEach, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitFuncName(FuncName funcName, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitStatFunction(StatFunction statFunction, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitStatLocalFunc(StatLocalFunc statLocalFunc, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitStatLocalAssign(StatLocalAssign statLocalAssign, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitRetStat(RetStat retStat, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitChunk(Chunk chunk, Object arg) throws Exception {
		
		Block b = chunk.block;
		label_traverse = true;
		goto_traverse= false;
		b.visit(this, arg);
		label_traverse = false;
		goto_traverse= true;
		global_block_index=0;
		
		b.visit(this, arg);
		//symtable.display();
		chunk.setSymboltable(symtable);
		return null;
	}

	@Override
	public Object visitFieldExpKey(FieldExpKey fieldExpKey, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitFieldNameKey(FieldNameKey fieldNameKey, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitFieldImplicitKey(FieldImplicitKey fieldImplicitKey, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitExpTrue(ExpTrue expTrue, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitExpFalse(ExpFalse expFalse, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitFuncBody(FuncBody funcBody, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitExpVarArgs(ExpVarArgs expVarArgs, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitStatAssign(StatAssign statAssign, Object arg) throws Exception {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitExpTableLookup(ExpTableLookup expTableLookup, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitExpFunctionCall(ExpFunctionCall expFunctionCall, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitLabel(StatLabel statLabel, Object ar) throws Exception {
		
		if(label_traverse)
		{
			
			
			statLabel.index = (Integer)ar;
			
			symtable.put(statLabel, (Integer)ar);
			
		}
		return null;
	}

	@Override
	public Object visitFieldList(FieldList fieldList, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitExpName(ExpName expName, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitFunctionCall(FunctionCall functionCall, Object arg) {
		// TODO Auto-generated method stub
		return null;
	}

}
