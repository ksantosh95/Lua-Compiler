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
import cop5556fa19.AST.Exp;
import cop5556fa19.AST.ExpBinary;
import cop5556fa19.AST.ExpFalse;
import cop5556fa19.AST.ExpFunction;
import cop5556fa19.AST.ExpInt;
import cop5556fa19.AST.ExpName;
import cop5556fa19.AST.ExpNil;
import cop5556fa19.AST.ExpString;
import cop5556fa19.AST.ExpTable;
import cop5556fa19.AST.ExpTrue;
import cop5556fa19.AST.ExpUnary;
import cop5556fa19.AST.ExpVarArgs;
import cop5556fa19.AST.Field;
import cop5556fa19.AST.FieldList;
import cop5556fa19.AST.FieldExpKey;
import cop5556fa19.AST.FieldImplicitKey;
import cop5556fa19.AST.FieldNameKey;
import cop5556fa19.AST.FuncBody;
import cop5556fa19.AST.Name;
import cop5556fa19.AST.ParList;
import cop5556fa19.Token.Kind;
import static cop5556fa19.Token.Kind.*;

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
		
		Token tmp = t;
		Exp e0 = andExp();
		//l.add(e0.toString());

		
		while (isKind(KW_or)) {
			Token op = consume();
		
			Exp e1 = andExp();
			e0 = new ExpBinary(first, e0, op, e1);
		}
		
		//System.out.println(isKind(OP_PLUS));
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
			return erel;
		
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
			return ebit;
			
		case BIT_SHIFTR:
			consume();
			e1 = dotdotExp();
			ebit = new ExpBinary(first, ebit, BIT_SHIFTR, e1);
			return ebit;
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
			e2 = powExp();
			e1 = new ExpBinary(first,e1,op,e2 );
			return e1;
			
			
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
		System.out.println("reached here");
		Token op = consume();
		Exp tmp_eunary = powExp();
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
			e1 = powExp();	
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
		
		
		case NAME: ExpName ename = new ExpName(t);
		consume();
		return ename;
		
		case LPAREN:consume();
		e2 = exp();
		match(RPAREN);
		return e2;
		
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

	
	private List fieldlist() throws Exception{
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
		match(RSQUARE);
		match(ASSIGN);
		value=exp();
		f = new FieldExpKey(first,key,value);
		return f;
		
	case NAME:
		Name nm = new Name(first,t.text);
		consume();
		match(ASSIGN);
		key=exp();
		f=new FieldNameKey(first,nm,key);
		return f;
		
	
	case RCURLY:
		return null;
	default:
		
		key = exp();
		
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
		throw new UnsupportedOperationException("andExp");
	}
	
	
	private ParList parlist() throws Exception{
		
			ParList e1 ;
			List<Name> l;
			l = namelist();
			System.out.println(l);
			if(l.isEmpty())
			{
				System.out.println("reached here");
				if(isKind(DOTDOTDOT))
				{
					boolean hasvarargs = true;
					consume();
					System.out.println(t);
					e1= new ParList(first,l,hasvarargs);
				}
				
			}
			else if(!l.isEmpty())
			{
				
				if(isKind(COMMA))
				{
					System.out.println("reached here");
					consume();
					if(isKind(DOTDOTDOT))
					{
						boolean hasvarargs = true;
						consume();
						System.out.println(t);
						e1= new ParList(first,l,hasvarargs);
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
			}
			return null;
		}
	
	
		private List namelist() throws Exception{
			
			List<Name> l = new ArrayList<Name>();
			Name nm ;
			if(isKind(NAME))
			{
				System.out.println(first);
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
					throw new UnsupportedOperationException("andExp");
				}
			}
			
			return l;
		}
	
	
	
	
	
	
	
	
	private Block block() {
		return new Block(null);  //this is OK for Assignment 2
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