/**
 * Developed  for the class project in COP5556 Programming Language Principles 
 * at the University of Florida, Fall 2019.
 * 
 * This software is solely for the educational benefit of students 
 * enrolled in the course during the Fall 2019 semester.  
 * 
 * This software, and any software derived from it,  may not be shared with others or posted to public web sites,
 * either during the course or afterwards.
 * 
 *  @Beverly A. Sanders, 2019
 */

package cop5556fa19;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cop5556fa19.AST.Block;
import cop5556fa19.AST.Chunk;
import cop5556fa19.AST.Exp;
import cop5556fa19.AST.ExpBinary;
import cop5556fa19.AST.ExpFalse;
import cop5556fa19.AST.ExpFunction;
import cop5556fa19.AST.ExpFunctionCall;
import cop5556fa19.AST.ExpInt;
import cop5556fa19.AST.ExpName;
import cop5556fa19.AST.ExpNil;
import cop5556fa19.AST.ExpString;
import cop5556fa19.AST.ExpTable;
import cop5556fa19.AST.ExpTableLookup;
import cop5556fa19.AST.ExpTrue;
import cop5556fa19.AST.ExpUnary;
import cop5556fa19.AST.ExpVarArgs;
import cop5556fa19.AST.Field;
import cop5556fa19.AST.FieldList;
import cop5556fa19.AST.FieldExpKey;
import cop5556fa19.AST.FieldImplicitKey;
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
import cop5556fa19.Token.Kind;
import static cop5556fa19.Token.Kind.*;

public class Parser {
	
	@SuppressWarnings("serial")
	class SyntaxException extends Exception {
		Token t;
		
		public SyntaxException(Token t, String message) {
			super(t.line + ":" + t.pos + " " + message);
		}
	}
	
	final Scanner scanner;
	Token t;  //invariant:  this is the next token


	Parser(Scanner s) throws Exception {
		this.scanner = s;
		t = scanner.getNext(); //establish invariant
	}

	Token first;
	boolean var_flg = false;
	boolean var_complete = false;
	
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
		throw new SyntaxException(first,"No Input where expression is expected");
		
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
		e2= prefixexptail(false);
		//return ename;
		return e2;
		
		case LPAREN://consume();
		//e2 = exp();
		//match(RPAREN);
		return prefixexptail(false);
		
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
		if(f!=null)
		{
			fl.add(f);	
		}
		
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
			var_complete=true;
			return ename;
		
			case LPAREN:consume();
			Exp e3 = exp();
			match(RPAREN);
			var_complete=false;
			return e3;
			
			default : return null;
		}
		
		}
		private Exp prefixexptail(boolean var_call) throws Exception{
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
							if(var_call)
							{
								var_complete=true;
							}
							break;
					
				case DOT:consume();
						if(t.kind==NAME)
						{
							ExpString ename = new ExpString(t);
							e= new ExpTableLookup(first,e,ename);
							consume();
							if(var_call)
							{
								var_complete=true;
							}
							break;
						}
						else
						{
							throw new SyntaxException(first,"Name expected in place of "+t.kind+" at position"+t.pos+" and line no. "+t.line);
						}
				
				case LPAREN:
							explist = args(null);
							
							e = new ExpFunctionCall(first,e,explist);
							if(var_call)
							{
								var_complete=false;
							}
							break;
							
				case LCURLY:
							explist = args(null);
							e = new ExpFunctionCall(first,e,explist);
							if(var_call)
							{
								var_complete=false;
							}
							break;
							
				case STRINGLIT:
							
							explist = args(null);
							
							e = new ExpFunctionCall(first,e,explist);
							if(var_call)
							{
								var_complete=false;
							}
							break;
							
				case COLON:consume();
							if(t.kind==NAME) {
								ExpName nm = new ExpName(t);
								consume();
								Exp n = e;
								e = new ExpTableLookup(first,e,nm);
								explist = args(n);
								e = new ExpFunctionCall(first,e,explist);
								if(var_call)
								{
									var_complete=false;
								}
								break;
							}
							else
							{
								throw new SyntaxException(first,"Name expected for function call  in place of "+t.kind+" at position"+t.pos+" and line no. "+t.line);
							}
							
							
							
				default : return e;
			}
		}
			
		}
	
private List<Exp> args(Exp nm) throws Exception{
	List<Exp> explist = new ArrayList<Exp>();
	List<Exp> explist2 = new ArrayList<Exp>();
	if(nm!=null)
	{
		explist.add(nm);
	}
	switch(t.kind)
	{
	case LPAREN:consume();
	explist2 = explist();
	explist.addAll(explist2);
	match(RPAREN);
	return explist;
	
	case LCURLY:consume();
	List<Field> fl = fieldlist();
	match(RCURLY);
	Exp e2 = new ExpTable(first,fl);
	explist2.add(e2);
	explist.addAll(explist2);
	return explist;
	
	case STRINGLIT:
		
	e2 = exp();
	//System.out.println(e2);
	explist2.add(e2);
	explist.addAll(explist2);
	
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
		
	
	
	public Chunk parse() throws Exception
	{
		Chunk c ;
		Block b = block();
		c=  new Chunk(first,b);
		if (!isKind(EOF)) throw new SyntaxException(t, "Parse ended before end of input");
		return c;
	}
	
	private Block block() throws Exception {
		List<Stat> l = new ArrayList<Stat>();
		List<Exp> elist = new ArrayList<Exp>();
		Block b;
		Stat s;
		s = stat();
		if(s!=null)
		{
			l.add(s);
		}
		if(t.kind==SEMI)
		{
			consume();
		}
		while(s!=null)
		{
			
			s =  stat();
			
			if(s!=null)
			{
				l.add(s);
				//System.out.println(s);
				}
			
			if(t.kind==SEMI)
			{
				consume();
			}
		}
		if(t.kind==KW_return)
		{
			elist = explist();
			if(t.kind==SEMI)
			{
				consume();
			}
			RetStat rs = new RetStat(first,elist);
			l.add(rs);
		}
		b = new Block(first,l);
		return b;  
	}

	private Stat stat() throws Exception{
		Stat s;
		Exp e2;
		Exp e3;
		Exp e4=null;
		Block b;
		Name nm;
		ExpName expnm=null;
		FuncName fname;
		FuncBody fbody;
		List<Exp> explist = new ArrayList<Exp>();
		List<Exp> varlist = new ArrayList<Exp>();
		List<Block> blocklist = new ArrayList<Block>();
		List<ExpName> namelist = new ArrayList<ExpName>();
		List<ExpName> namelist_2 = new ArrayList<ExpName>();
		switch(t.kind)
		{
		
					
		case NAME: e2= prefixexptail(true);
		
		if(!var_complete)
		{
			throw new SyntaxException(first,"Var not ended properly at position "+t.pos+" at line "+t.line);
		}
					varlist.add(e2);
					
					
					while(t.kind==COMMA)
					{
						consume();
						e2 = prefixexptail(true);
						if(!var_complete)
						{
							throw new SyntaxException(first,"Var not ended properly at position "+t.pos+" at line "+t.line);
						}
						if(e2!=null)
						{
							varlist.add(e2);
						}
					
					}
					match(ASSIGN);
					
					e3= exp();
					if(e3==null)
					{
						throw new SyntaxException(first,"Expression expected in statement");
					}
					explist.add(e3);
					
					while(t.kind==COMMA)
					{
						consume();
						e3 = exp();
						if(e3!=null)
						{
							explist.add(e3);
						}
					}
					s= new StatAssign(first,varlist,explist);
					return s;
					
					
		case LPAREN:e2=prefixexptail(true);
					if(!var_complete)
					{
						throw new SyntaxException(first,"Var not ended properly at position "+t.pos+" at line "+t.line);
					}
					System.out.println(e2);
					varlist.add(e2);
					
					while(t.kind==COMMA)
					{
						consume();
					
						e2 = prefixexptail(true);
						if(!var_complete)
						{
							throw new SyntaxException(first,"Var not ended properly at position "+t.pos+" at line "+t.line);
						}
						if(e2!=null)
						{
							varlist.add(e2);
						}
					}
					match(ASSIGN);
					e3= exp();
					if(e3==null)
					{
						throw new SyntaxException(first,"Expression expected in statement");
					}
					explist.add(e3);
					
					while(t.kind==COMMA)
					{
						consume();
					
						e3 = exp();
						if(e3!=null)
						{
							explist.add(e3);
						}
					}
					s= new StatAssign(first,varlist,explist);
					return s;
		
					
		case COLONCOLON:match(COLONCOLON);
						if(t.kind==NAME)
							{
							nm = new Name(first,t.text);
							consume();
							match(COLONCOLON);
							s = new StatLabel(first,nm);
							return s;
							}
						else
						{
							
							throw new SyntaxException(t,"Name expected in label after COLONCOLON at position"+t.pos+" on line "+t.line);
						}
		case KW_break: s= new StatBreak(first);
						consume();
						return s;
		
		case KW_goto: consume();
						if(t.kind==NAME)
							{
							nm = new Name(first,t.text);
							consume();
							s = new StatGoto(first,nm);
							
							return s;
							}
						else
						{
							throw new SyntaxException(t,"Name expected in goto statement after COLONCOLON at position"+t.pos+" on line "+t.line);	
						}
		
		case KW_do: consume();
					
					b = block();
					match(KW_end);
					
					s = new StatDo(first,b);
					return s;
		
		case KW_while:consume();
					e2 = exp();
					match(KW_do);
					b = block();
					match(KW_end);
					s = new StatWhile(first,e2,b);
					return s;
					
		case KW_repeat:consume();
					b=block();
					
					match(KW_until);
					e2=exp();
					s = new StatRepeat(first,b,e2);
					return s;
					
		case KW_if:consume();
					e2 = exp();
					if(e2==null)
					{
						throw new SyntaxException(first,"Expression missing after If");
					}
					explist.add(e2);
					match(KW_then);
					b=block();
					blocklist.add(b);
					while(t.kind == KW_elseif)
					{
						consume();
						e2=exp();
						if(e2==null)
						{
							throw new SyntaxException(first,"Expression missing after If");
						}
						explist.add(e2);
						match(KW_then);
						b=block();
						blocklist.add(b);
					}
					if(t.kind==KW_else)
					{
						consume();
						b=block();
						blocklist.add(b);
					}
					match(KW_end);
					s = new StatIf(first,explist,blocklist);
					return s;
					
					
		case KW_for: consume();
					if(t.kind==NAME)
					{
						ExpName n = new ExpName(t);
						namelist.add(n);
						consume();
						switch(t.kind)
						{
						case ASSIGN:consume();
									e2=exp();
									match(COMMA);
									e3=exp();
									if(t.kind==COMMA)
									{
										consume();
										e4 = exp();
									}
									match(KW_do);
									b=block();
									match(KW_end);
									s = new StatFor(first,n,e2,e3,e4,b);
									return s;
									
						case COMMA: while(t.kind==COMMA)
									{
										consume();
										if(t.kind==NAME)
										{
											n = new ExpName(t);
											namelist.add(n);	
										}
										else
										{
											throw new SyntaxException(first,"Name expected after COMMA in statement");
										}
									}
									match(KW_in);
									explist = explist();
									match(KW_do);
									b=block();
									match(KW_end);
									s = new StatForEach(first,namelist,explist,b);
									return s;
									
									
						case KW_in:consume();
									explist = explist();
									match(KW_do);
									b=block();
									match(KW_end);
									s = new StatForEach(first,namelist,explist,b);
									return s;
						}
						
					}
					else
					{
						throw new SyntaxException(t,"Name expected after for");
					}
						
					
		case KW_function : consume();
							if(t.kind==NAME)
							{
								ExpName n = new ExpName(t);
								namelist.add(n);
								consume();
								while(t.kind==DOT)
								{
									consume();
									if(t.kind==NAME)
									{
										n = new ExpName(t);
										namelist.add(n);	
									}
									else
									{
										throw new SyntaxException(first,"Name expected after DOT in function name");
									}
								}
								if(t.kind==COLON)
								{
									consume();
									if(t.kind==NAME)
									{
										expnm = new ExpName(t);
										consume();
									}
									else
									{
										throw new SyntaxException(first,"Name expected after COLON in function name");
									}
									
								}
								
							}
							else
							{
								throw new SyntaxException(first,"function name absent");
							}
							fname = new FuncName(first,namelist,expnm);
							fbody = funcbody();
							s = new StatFunction(first,fname,fbody);
							return s;
							
		case KW_local : consume();
						if(t.kind==KW_function)
						{
							consume();
							if(t.kind==NAME)
							{
								expnm = new ExpName(t);
								List<ExpName> namelist_3 = new ArrayList<ExpName>();
								consume();
								namelist_3.add(expnm);
								fname = new FuncName(first,namelist_3,null);
								fbody = funcbody();
							}
							else
							{
								throw new SyntaxException(first,"Name expected local function name declaration");
								
							}
							s = new StatLocalFunc(first,fname,fbody);
							return s;
						}
						else
						{
							if(t.kind==NAME)
							{
								ExpName n = new ExpName(t);
								namelist.add(n);
								consume();
								while(t.kind==COMMA)
								{
									consume();
									if(t.kind==NAME)
									{
										n = new ExpName(t);
										namelist.add(n);	
									}
									else
									{
										throw new SyntaxException(first,"Name expected after COMMA in local function name");
									}
								}
								if(t.kind==ASSIGN)
								{
									consume();
									explist =explist();
								}
								s = new StatLocalAssign(first,namelist,explist);
								return s;
							}
							
							else
							{
								throw new SyntaxException(first,"Name expected local function name declaration");
							}
						}
						
		default: return null;
		}
		
		
		
		
		
		
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
