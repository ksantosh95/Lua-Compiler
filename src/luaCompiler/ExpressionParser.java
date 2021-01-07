package luaCompiler;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import luaCompiler.AST.Block;
import luaCompiler.AST.Chunk;
import luaCompiler.AST.Exp;
import luaCompiler.AST.ExpBinary;
import luaCompiler.AST.ExpFalse;
import luaCompiler.AST.ExpFunction;
import luaCompiler.AST.ExpFunctionCall;
import luaCompiler.AST.ExpInt;
import luaCompiler.AST.ExpName;
import luaCompiler.AST.ExpNil;
import luaCompiler.AST.ExpString;
import luaCompiler.AST.ExpTable;
import luaCompiler.AST.ExpTableLookup;
import luaCompiler.AST.ExpTrue;
import luaCompiler.AST.ExpUnary;
import luaCompiler.AST.ExpVarArgs;
import luaCompiler.AST.Field;
import luaCompiler.AST.FieldList;
import luaCompiler.AST.FieldExpKey;
import luaCompiler.AST.FieldImplicitKey;
import luaCompiler.AST.FieldNameKey;
import luaCompiler.AST.FuncBody;
import luaCompiler.AST.Name;
import luaCompiler.AST.ParList;
import luaCompiler.AST.Stat;
import luaCompiler.Token.Kind;
import static luaCompiler.Token.Kind.*;

public class ExpressionParser {
	
	@SuppressWarnings("serial")
	class SyntaxException extends Exception {
		Token t;
		
		public SyntaxException(Token t, String message) {
			super(t.line + ":" + t.pos + " " + message);
		}
	}
	
	final Scanner scanner;
	Token t;  //invariant:  this is the next token


	ExpressionParser(Scanner s) throws Exception {
		this.scanner = s;
		t = scanner.getNext(); //establish invariant
	}

	Token first;
	boolean var_flg = false;
	
	
	Exp exp() throws Exception {
		first = t;
		
		//Token tmp = t;
		Exp e0 = andExp();
		
		
		//l.add(e0.toString());

		
		while (isKind(KW_or)) {
			Token op = consume();
		
			Exp e1 = andExp();
			if(e1==null)
			{
				throw new SyntaxException(t,"Invalid Syntax");
			}
			e0 = new ExpBinary(first, e0, op, e1);
		}
		
		//System.out.println(isKind(OP_PLUS));
		if(e0==null)
		{
		throw new SyntaxException(first,"No Input");
		
		}
		return e0;
	}

	
private Exp andExp() throws Exception{
		
	Exp eand = relExp();
	while (isKind(KW_and))
	{
	consume();
		Exp e1 = relExp();
		eand = new ExpBinary(first, eand, KW_and, e1);
		
	}
	return eand;
	
	
	
		//throw new UnsupportedOperationException("andExp");  //I find this is a more useful placeholder than returning null.
	}
	
	private Exp relExp() throws Exception{
		Exp erel = bitorExp();
		Exp e1;
		Token op ;
		while (isKind(REL_LT) | isKind(REL_GT) | isKind(REL_LE) | isKind(REL_GE) | isKind(REL_NOTEQ) | isKind(REL_EQEQ))
		{
		
			op= consume();
			e1 = bitorExp();
			erel = new ExpBinary(first, erel, op, e1);
			
		
		}
	
		return erel;
	}
	
	
	private Exp bitorExp() throws Exception{
		Exp eor = bitxorExp();
		while (isKind(BIT_OR))
		{
		consume();
			Exp e1 = bitxorExp();
			eor = new ExpBinary(first, eor, BIT_OR, e1);
			
			
		}
		return eor;
	}

	private Exp bitxorExp() throws Exception{
		Exp exor = bitampExp();
		while (isKind(BIT_XOR))
		{
		consume();
			Exp e1 = bitampExp();
			exor = new ExpBinary(first, exor, BIT_XOR, e1);
			
		}
		return exor;
	}
	
	
	private Exp bitampExp() throws Exception{
		Exp eamp = bitExp();
		while (isKind(BIT_AMP))
		{
		consume();
			Exp e1 = bitExp();
			eamp = new ExpBinary(first, eamp, BIT_AMP, e1);
			
		}
		return eamp;
	}
	
	
	private Exp bitExp() throws Exception{
		Exp ebit = dotdotExp();
		Exp e1;
		while (isKind(BIT_SHIFTL) | isKind(BIT_SHIFTR))
		{
		switch(t.kind)
		{
		case BIT_SHIFTL:
			consume();
			e1 = dotdotExp();
			ebit = new ExpBinary(first, ebit, BIT_SHIFTL, e1);
			
			break;
			
		case BIT_SHIFTR:
			consume();
			e1 = dotdotExp();
			ebit = new ExpBinary(first, ebit, BIT_SHIFTR, e1);
			
			break;
		}
		
		}
	
		return ebit;
	}
	
	
	private Exp dotdotExp() throws Exception{
		Exp edotdot = plusminusExp();
		
		Exp e1= edotdot;
		boolean execute = false;
		while (isKind(DOTDOT))
		{
		consume();
		execute = true;
			//Exp e1 = terminal();
			e1 = dotdotExp();	
			if(e1==null)
			{
				throw new SyntaxException(t,"Invalid Syntax");
			}
		}
		if(execute)
		{
			edotdot = new ExpBinary(first, edotdot, DOTDOT, e1);
		}
		return edotdot;
	}
	
	
	private Exp plusminusExp() throws Exception{
		Token tmp=t;
		Exp e0 = divmulExp();
		while (isKind(OP_PLUS)||isKind(OP_MINUS))
		{
		
			if(isKind(OP_PLUS))
			{
				tmp = match(OP_PLUS);
			}
			else if(isKind(OP_MINUS))
			{
				tmp = match(OP_MINUS);
			}
			Exp e1 = divmulExp();
			if(e1==null)
			{
				throw new SyntaxException(t,"Invalid syntax");
			}
			e0 = new ExpBinary(first, e0, tmp, e1);
		}
		
		return e0;
	}
	private Exp divmulExp() throws Exception{
		Exp e1 = unaryExp();
		Exp e2;
		Token op;
		while(isKind(OP_TIMES) | isKind(OP_DIV) | isKind(OP_DIVDIV) | isKind(OP_MOD))
		{
			
				op= consume();
			e2 = unaryExp();
			
			if(e2==null)
			{
				throw new SyntaxException(t,"Invalid syntax");
			}
			e1 = new ExpBinary(first,e1,op,e2 );
			
		}
		return e1;
	}

	private Exp unaryExp() throws Exception{
	Exp eunary = powExp();
	Exp e1 = eunary;
	if(eunary==null)
	{
	if(isKind(OP_MINUS) | isKind(KW_not) | isKind(OP_HASH) | isKind(BIT_XOR))
	{
		
		Token op = consume();
		Exp tmp_eunary = unaryExp();
			if(tmp_eunary ==null)
			{
				
				throw new SyntaxException(t,"Unary Operator expects Expression");
			}
		e1 = new ExpUnary(first ,op.kind, tmp_eunary );
		
		return e1;
	}
	}
	return eunary;
	
	}
	
	
	private Exp powExp() throws Exception{
		Exp epow = terminal();
		Exp e1= epow;
		
		boolean execute = false;
		while (isKind(OP_POW))
		{
		consume();
		execute = true;
			//Exp e1 = terminal();
			e1 = unaryExp();	
			if(e1==null)
			{
				throw new SyntaxException(t,"Invalid Syntax");
			}
		}
		if(execute)
		{
			epow = new ExpBinary(first, epow, OP_POW, e1);
		}
		return epow;
	}


	private Exp terminal() throws Exception{
		Exp e2;
		switch(t.kind)
		{
		case KW_nil: ExpNil enil = new ExpNil(t);
		consume();
		return enil;
		
		case KW_false: ExpFalse efalse = new ExpFalse(t);
		consume();
		return efalse;
		
		case KW_true: ExpTrue etrue = new ExpTrue(t);
		consume();
		return etrue;
		
		case INTLIT: ExpInt eint = new ExpInt(t);
		consume();
		return eint;
		
		case STRINGLIT: ExpString estring = new ExpString(t);
		consume();
		return estring;
		
		case DOTDOTDOT: ExpVarArgs evarargs = new ExpVarArgs(t);
		consume();
		return evarargs;
		
		
		case NAME: //ExpName ename = new ExpName(t);
		//consume();
		e2= prefixexptail();
		//return ename;
		return e2;
		
		case LPAREN://consume();
		//e2 = exp();
		//match(RPAREN);
		return prefixexptail();
		
		case KW_function:
			consume();
			FuncBody e3 = funcbody();
			e2 = new ExpFunction(first,e3);
			return e2;
			
		case LCURLY:
			consume();
			
			List<Field> fl = fieldlist();
			
			match(RCURLY);
			e2 = new ExpTable(first,fl);
			return e2;
			
		default: return null;
		}
		
	}

	
	private List<Field> fieldlist() throws Exception{
	List<Field> fl = new ArrayList<Field>();
		Field f = field();
		
		fl.add(f);
		while(isKind(COMMA) | isKind(SEMI))
		{
			consume();
			f = field();
			if(f==null)
			{
				break;
			}
			fl.add(f);
		}
		if(isKind(COMMA) | isKind(SEMI))
		{
			throw new UnsupportedOperationException("Extra Separated");
		}
		return fl;
	}
	
	private Field field() throws Exception{
	Field f;
	Exp key;
	Exp value;
	
	switch(t.kind)
	{
	case LSQUARE:
		consume();
		key = exp();
		if(key==null)
		{
			throw new SyntaxException(first,"Invalid syntax");
		}
		match(RSQUARE);
		match(ASSIGN);
		value=exp();
		f = new FieldExpKey(first,key,value);
		return f;
		
	case NAME:
		//System.out.println(t);
		Name nm = new Name(first,t.text);
		Exp r = new ExpName(t);
		consume();
		if(t.kind==ASSIGN)
		{
		match(ASSIGN);
		key=exp();
		f=new FieldNameKey(first,nm,key);
		return f;
		}
		else
		{
			
			return new FieldImplicitKey(first,r);
		}
	
	case RCURLY:
		return null;
	default:
		
		key = exp();
		//System.out.println(t);
		f = new FieldImplicitKey(first,key);
		return f;
	}
	}
	
	private FuncBody funcbody() throws Exception{
	FuncBody e1;
	ParList p;
	Block b;
	if(isKind(LPAREN))
	{
		consume();
		p = parlist();
		match(RPAREN);
		b = block();
		match(KW_end);
		e1 = new FuncBody(first,p,b);
		return e1;
	}
	else
		throw new SyntaxException(first,"Incomplete function");
	}
	
	
	private ParList parlist() throws Exception{
		
			ParList e1 ;
			List<Name> l;
			l = namelist();
		
			if(l.isEmpty())
			{
				
				if(isKind(DOTDOTDOT))
				{
					boolean hasvarargs = true;
					consume();
					System.out.println("Reached");
					return new ParList(first,l,hasvarargs);
				}
				
			}
			else if(!l.isEmpty())
			{
				
				if(isKind(COMMA))
				{
					
					consume();
					if(isKind(DOTDOTDOT))
					{
						boolean hasvarargs = true;
						consume();
						
						return new ParList(first,l,hasvarargs);
					}
					else
					{
						throw new UnsupportedOperationException("Error");
					}
				}
				else if(var_flg)
					{
					var_flg=false;
					consume();
					return new ParList(first,l,true);
					
					}
				else
					return new ParList(first,l,false);
			}
			return null;
		}
	
	
		private List<Name> namelist() throws Exception{
			
			List<Name> l = new ArrayList<Name>();
			Name nm ;
			if(isKind(NAME))
			{
				
				nm = new Name(first,t.text);
				l.add(nm);
			
				consume();
			}
			while(isKind(COMMA))
			{
				consume();
				if(isKind(NAME))
				{
					nm = new Name(first,t.text);
					l.add(nm);
				consume();
				}
				else if (isKind(DOTDOTDOT))
				{
					var_flg=true;
				}
				else
				{
					throw new SyntaxException(first,"Name List incomplete");
				}
			}
			
			return l;
		}
		
		private Exp prefixexp() throws Exception{
		switch(t.kind)
		{
			case NAME: ExpName ename = new ExpName(t);
			consume();
			return ename;
		
			case LPAREN:consume();
			Exp e3 = exp();
			match(RPAREN);
			return e3;
			
			default : return null;
		}
		
		}
		private Exp prefixexptail() throws Exception{
			List<Exp> explist = new ArrayList<Exp>();
			Exp e1;
			Exp e=prefixexp();
			for(;;)
			{
				switch(t.kind)
			
				{
				case LSQUARE:consume();
							e1 = exp();
							match(RSQUARE);
							e =new ExpTableLookup(first,e,e1);
							break;
					
				case DOT:consume();
						if(t.kind==NAME)
						{
							ExpName ename = new ExpName(t);
							e= new ExpTableLookup(first,e,ename);
							consume();
							break;
						}
						else
						{
							throw new SyntaxException(first,"Name expected");
						}
				
				case LPAREN:
							explist = args();
							
							e = new ExpFunctionCall(first,e,explist);
							break;
							
				case LCURLY:
							explist = args();
							e = new ExpFunctionCall(first,e,explist);
							break;
							
				case STRINGLIT:
							
							explist = args();
							
							e = new ExpFunctionCall(first,e,explist);
							break;
							
				case COLON:consume();
							if(t.kind==NAME) {
								ExpName nm = new ExpName(t);
								consume();
								e = new ExpTableLookup(first,e,nm);
								explist = args();
								e = new ExpFunctionCall(first,e,explist);
								break;
							}
							else
							{
								throw new SyntaxException(first,"Name expected for function call");
							}
							
							
							
				default : return e;
			}
		}
			
		}
	
private List<Exp> args() throws Exception{
	List<Exp> explist = new ArrayList<Exp>();
	
	switch(t.kind)
	{
	case LPAREN:consume();
	explist = explist();
	match(RPAREN);
	return explist;
	
	case LCURLY:consume();
	List<Field> fl = fieldlist();
	match(RCURLY);
	Exp e2 = new ExpTable(first,fl);
	explist.add(e2);
	return explist;
	
	case STRINGLIT:
		
	e2 = exp();
	//System.out.println(e2);
	explist.add(e2);
	
	return explist;
	
	default : return null;
	}
}	
		
private List<Exp> explist() throws Exception{
			
			List<Exp> l = new ArrayList<Exp>();
			Exp e ;
			e = exp();
		
			if(e==null )
			{
				throw new SyntaxException(first,"Expression expected in expression list after comma");
			}
			l.add(e);
			
			
			while(isKind(COMMA))
			{
				consume();
				e=exp();
			
				if(e==null )
				{
					throw new SyntaxException(first,"Expression expected in expression list after comma");
				}
				else
				{
					l.add(e);
				}
			}
			
			return l;
		}
		
	
	
	private Chunk chunk()
	{
		Chunk c ;
		Block b = block();
		c=  new Chunk(first,b);
		return c;
	}
	
	private Block block() {
		List<Stat> l = new ArrayList<Stat>();
		Block b;
		Stat s;
		s = stat();
		l.add(s);
		while(s!=null)
		{
			s =  stat();
			if(s!=null)
			{l.add(s);
				}
		}
		b = new Block(first,l);
		return b;  
	}

	private Stat stat() {
		return null;
		
	}
	
	protected boolean isKind(Kind kind) {
		return t.kind == kind;
	}

	protected boolean isKind(Kind... kinds) {
		for (Kind k : kinds) {
			if (k == t.kind)
				return true;
		}
		return false;
	}

	/**
	 * @param kind
	 * @return
	 * @throws Exception
	 */
	Token match(Kind kind) throws Exception {
		Token tmp = t;
		if (isKind(kind)) {
			consume();
			return tmp;
		}
		error(kind);
		return null; // unreachable
	}

	/**
	 * @param kind
	 * @return
	 * @throws Exception
	 */
	Token match(Kind... kinds) throws Exception {
		Token tmp = t;
		if (isKind(kinds)) {
			consume();
			return tmp;
		}
		StringBuilder sb = new StringBuilder();
		for (Kind kind1 : kinds) {
			sb.append(kind1).append(kind1).append(" ");
		}
		error(kinds);
		return null; // unreachable
	}

	Token consume() throws Exception {
		Token tmp = t;
        t = scanner.getNext();
		return tmp;
	}
	
	void error(Kind... expectedKinds) throws SyntaxException {
		String kinds = Arrays.toString(expectedKinds);
		String message;
		if (expectedKinds.length == 1) {
			message = "Expected " + kinds + " at " + t.line + ":" + t.pos;
		} else {
			message = "Expected one of" + kinds + " at " + t.line + ":" + t.pos;
		}
		throw new SyntaxException(t, message);
	}

	void error(Token t, String m) throws SyntaxException {
		String message = m + " at " + t.line + ":" + t.pos;
		throw new SyntaxException(t, message);
	}
	


}
