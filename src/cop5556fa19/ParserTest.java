package cop5556fa19;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import cop5556fa19.Parser;
import cop5556fa19.Parser.SyntaxException;
import cop5556fa19.AST.ASTNode;
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
import cop5556fa19.AST.ExpVarArgs;
import cop5556fa19.AST.Expressions;
import cop5556fa19.AST.Field;
import cop5556fa19.AST.FieldExpKey;
import cop5556fa19.AST.FieldImplicitKey;
import cop5556fa19.AST.ParList;
import cop5556fa19.AST.Stat;
import cop5556fa19.AST.StatAssign;
import cop5556fa19.AST.StatBreak;
import cop5556fa19.AST.StatDo;
import cop5556fa19.AST.StatGoto;
import cop5556fa19.AST.StatLabel;
import cop5556fa19.Scanner;
import cop5556fa19.Token;

import static cop5556fa19.Token.Kind.*;

class ParserTest {

	// To make it easy to print objects and turn this output on and off
	static final boolean doPrint = true;
//	static final boolean doPrint = false;

	private void show(Object input) {
		if (doPrint) {
			System.out.println(input.toString());
		}
	}
	
	// creates a scanner, parser, and parses the input by calling exp().  
	Exp parseExpAndShow(String input) throws Exception {
		show("parser input:\n" + input); // Display the input
		Reader r = new StringReader(input);
		Scanner scanner = new Scanner(r); // Create a Scanner and initialize it
		Parser parser = new Parser(scanner);
		Exp e = parser.exp();
		show("e=" + e);
		return e;
	}	
	
	
	// creates a scanner, parser, and parses the input by calling block()  
	Block parseBlockAndShow(String input) throws Exception {
		show("parser input:\n" + input); // Display the input
		Reader r = new StringReader(input);
		Scanner scanner = new Scanner(r); // Create a Scanner and initialize it
		Parser parser = new Parser(scanner);
		Method method = Parser.class.getDeclaredMethod("block");
		method.setAccessible(true);
		Block b = (Block) method.invoke(parser);
		show("b=" + b);
		return b;
	}	
	
	
	//creates a scanner, parser, and parses the input by calling parse()
	//this corresponds to the actual use case of the parser
	Chunk parseAndShow(String input) throws Exception {
		show("parser input:\n" + input); // Display the input
		Reader r = new StringReader(input);
		Scanner scanner = new Scanner(r); // Create a Scanner and initialize it
		Parser parser = new Parser(scanner);
		Chunk c = parser.parse();
		show("c="+c);
		return c;
	}
	
	@Test
	void testEmpty1() throws Exception {
		String input = "";
		Block b = parseBlockAndShow(input);
		Block expected = Expressions.makeBlock();
		assertEquals(expected, b);
	}
	
	@Test
	void testEmpty2() throws Exception {
		String input = "";
		ASTNode n = parseAndShow(input);
		Block b = Expressions.makeBlock();
		Chunk expected = new Chunk(b.firstToken,b);
		assertEquals(expected,n);
	}
	
	@Test
	void testAssign1() throws Exception {
		String input = "a=b";
		Block b = parseBlockAndShow(input);		
		List<Exp> lhs = Expressions.makeExpList(Expressions.makeExpNameGlobal("a"));
		List<Exp> rhs = Expressions.makeExpList(Expressions.makeExpNameGlobal("b"));
		StatAssign s = Expressions.makeStatAssign(lhs,rhs);
		Block expected = Expressions.makeBlock(s);
		assertEquals(expected,b);
	}
	
	@Test
	void testAssignChunk1() throws Exception {
		String input = "a=b";
		ASTNode c = parseAndShow(input);		
		List<Exp> lhs = Expressions.makeExpList(Expressions.makeExpNameGlobal("a"));
		List<Exp> rhs = Expressions.makeExpList(Expressions.makeExpNameGlobal("b"));
		StatAssign s = Expressions.makeStatAssign(lhs,rhs);
		Block b = Expressions.makeBlock(s);
		Chunk expected = new Chunk(b.firstToken,b);
		assertEquals(expected,c);
	}
	

	@Test
	void testMultiAssign1() throws Exception {
		String input = "a,c=8,9";
		Block b = parseBlockAndShow(input);		
		List<Exp> lhs = Expressions.makeExpList(
					Expressions.makeExpNameGlobal("a")
					,Expressions.makeExpNameGlobal("c"));
		Exp e1 = Expressions.makeExpInt(8);
		Exp e2 = Expressions.makeExpInt(9);
		List<Exp> rhs = Expressions.makeExpList(e1,e2);
		StatAssign s = Expressions.makeStatAssign(lhs,rhs);
		Block expected = Expressions.makeBlock(s);
		assertEquals(expected,b);		
	}
	

	

	@Test
	void testMultiAssign3() throws Exception {
		String input = "a,c=8,f(x)";
		Block b = parseBlockAndShow(input);		
		List<Exp> lhs = Expressions.makeExpList(
					Expressions.makeExpNameGlobal("a")
					,Expressions.makeExpNameGlobal("c"));
		Exp e1 = Expressions.makeExpInt(8);
		List<Exp> args = new ArrayList<>();
		args.add(Expressions.makeExpNameGlobal("x"));
		Exp e2 = Expressions.makeExpFunCall(Expressions.makeExpNameGlobal("f"),args, null);
		List<Exp> rhs = Expressions.makeExpList(e1,e2);
		StatAssign s = Expressions.makeStatAssign(lhs,rhs);
		Block expected = Expressions.makeBlock(s);
		assertEquals(expected,b);			
	}
	

	
	@Test
	void testAssignToTable() throws Exception {
		String input = "g.a.b = 3";
		Block bl = parseBlockAndShow(input);
		ExpName g = Expressions.makeExpNameGlobal("g");
		ExpString a = Expressions.makeExpString("a");
		Exp gtable = Expressions.makeExpTableLookup(g,a);
		ExpString b = Expressions.makeExpString("b");
		Exp v = Expressions.makeExpTableLookup(gtable, b);
		Exp three = Expressions.makeExpInt(3);		
		Stat s = Expressions.makeStatAssign(Expressions.makeExpList(v), Expressions.makeExpList(three));;
		Block expected = Expressions.makeBlock(s);
		assertEquals(expected,bl);
	}
	
	@Test
	void testAssignTableToVar() throws Exception {
		String input = "x = g.a.b";
		Block bl = parseBlockAndShow(input);
		ExpName g = Expressions.makeExpNameGlobal("g");
		ExpString a = Expressions.makeExpString("a");
		Exp gtable = Expressions.makeExpTableLookup(g,a);
		ExpString b = Expressions.makeExpString("b");
		Exp e = Expressions.makeExpTableLookup(gtable, b);
		Exp v = Expressions.makeExpNameGlobal("x");		
		Stat s = Expressions.makeStatAssign(Expressions.makeExpList(v), Expressions.makeExpList(e));;
		Block expected = Expressions.makeBlock(s);
		assertEquals(expected,bl);
	}
	

	
	@Test
	void testmultistatements6() throws Exception {
		String input = "x = g.a.b ; ::mylabel:: do  y = 2 goto mylabel f=a(0,200) end break"; //same as testmultistatements0 except ;
		ASTNode c = parseAndShow(input);
		ExpName g = Expressions.makeExpNameGlobal("g");
		ExpString a = Expressions.makeExpString("a");
		Exp gtable = Expressions.makeExpTableLookup(g,a);
		ExpString b = Expressions.makeExpString("b");
		Exp e = Expressions.makeExpTableLookup(gtable, b);
		Exp v = Expressions.makeExpNameGlobal("x");		
		Stat s0 = Expressions.makeStatAssign(v,e);
		StatLabel s1 = Expressions.makeStatLabel("mylabel");
		Exp y = Expressions.makeExpNameGlobal("y");
		Exp two = Expressions.makeExpInt(2);
		Stat s2 = Expressions.makeStatAssign(y,two);
		Stat s3 = Expressions.makeStatGoto("mylabel");
		Exp f = Expressions.makeExpNameGlobal("f");
		Exp ae = Expressions.makeExpNameGlobal("a");
		Exp zero = Expressions.makeExpInt(0);
		Exp twohundred = Expressions.makeExpInt(200);
		List<Exp> args = Expressions.makeExpList(zero, twohundred);
		ExpFunctionCall fc = Expressions.makeExpFunCall(ae, args, null);		
		StatAssign s4 = Expressions.makeStatAssign(f,fc);
		StatDo statdo = Expressions.makeStatDo(s2,s3,s4);
		StatBreak statBreak = Expressions.makeStatBreak();
		Block expectedBlock = Expressions.makeBlock(s0,s1,statdo,statBreak);
		Chunk expectedChunk = new Chunk(expectedBlock.firstToken, expectedBlock);
		show("expected="+expectedChunk);
		assertEquals(expectedChunk,c);
	}
	
	@Test
	void testMultiStatements7() throws Exception {
		String input = "x = g.a.b ; ::mylabel:: do  y = 2 goto mylabel f=a(0,200) end break"; //same as testmultistatements0 except ;
		ASTNode c = parseAndShow(input);
		
	}
	
	
	
	/* Test case for prefix exp. Starting with the non-terminal Name. Should pass */
	@Test	
	void testPrefixexp1() throws Exception {
		String input = "f(a)[b]\"g\"";
		Exp e = parseExpAndShow(input);		
		Exp f = Expressions.makeExpNameGlobal("f");
		Exp a = Expressions.makeExpNameGlobal("a");
		List<Exp> args = Expressions.makeExpList(a);
		Exp b = Expressions.makeExpNameGlobal("b");
		Exp g = Expressions.makeExpString("g");
		List<Exp> args_1 = Expressions.makeExpList(g);
		ExpFunctionCall fc = Expressions.makeExpFunCall(f, args, null);
		Exp tl = Expressions.makeExpTableLookup(fc, b);
		ExpFunctionCall fc1 = Expressions.makeExpFunCall(tl,args_1,null);
		show("expected=" + fc1);
		assertEquals(e,fc1);
	}
	
	/* Test case for prefix exp. Starting with the non-terminal Name. Should pass */
	@Test	
	void testPrefixexp2() throws Exception {
		String input = "abc";
		Exp e = parseExpAndShow(input);		
		
	}
	
	/* Test case for prefix exp. Starting with the non-terminal Name. Should pass */
	@Test	
	void testPrefixexp3() throws Exception {
		String input = "ab[xyz]";
		Exp e = parseExpAndShow(input);		
		Exp ab = Expressions.makeExpNameGlobal("ab");
		Exp xyz = Expressions.makeExpNameGlobal("xyz");
		Exp tl = Expressions.makeExpTableLookup(ab, xyz);
		show("expected=" + tl);
		assertEquals(e,tl);
	}
	
	/* Test case for prefix exp. Starting with the non-terminal Name. Should pass */
	@Test	
	void testPrefixexp4() throws Exception {
		String input = "ab.xyz";
		Exp e = parseExpAndShow(input);
		Exp ab = Expressions.makeExpNameGlobal("ab");
		Exp xyz = Expressions.makeExpString("xyz");
		Exp tl = Expressions.makeExpTableLookup(ab, xyz);
		show("expected=" + tl);
		assertEquals(e,tl);
	}
	
	/* Test case for prefix exp. Starting with the non-terminal Name. Should pass */
	@Test	
	void testPrefixexp5() throws Exception {
		String input = "ab(x+y,m-n)";
		Exp e = parseExpAndShow(input);		
		Exp ab = Expressions.makeExpNameGlobal("ab");
		Exp xy = Expressions.makeBinary(Expressions.makeExpNameGlobal("x"),OP_PLUS,Expressions.makeExpNameGlobal("y"));
		Exp mn = Expressions.makeBinary(Expressions.makeExpNameGlobal("m"),OP_MINUS,Expressions.makeExpNameGlobal("n"));
		List<Exp> args = Expressions.makeExpList(xy,mn);
		ExpFunctionCall fc = Expressions.makeExpFunCall(ab, args, null);
		show("expected=" + fc);
		assertEquals(e,fc);
	}
	
	/* Test case for prefix exp. Starting with the non-terminal Name. Should pass */
	@Test	
	void testPrefixexp6() throws Exception {
		String input = "ab{3,a}";
		Exp e = parseExpAndShow(input);		
	
	}
	
	/* Test case for prefix exp. Starting with the non-terminal Name. Should pass */
	@Test	
	void testPrefixexp7() throws Exception {
		String input = "ab\"xy\"";
		Exp e = parseExpAndShow(input);		
	
	}
	
	
	/* Test case for prefix exp. Starting with the non-terminal Name. Should pass */
	@Test	
	void testPrefixexp8() throws Exception {
		String input = "ab:xy(l-m,n-k)";
		Exp e = parseExpAndShow(input);		
		Exp ab = Expressions.makeExpNameGlobal("ab");
		Exp xy = Expressions.makeExpNameGlobal("xy");
		Exp lm = Expressions.makeBinary(Expressions.makeExpNameGlobal("l"),OP_MINUS,Expressions.makeExpNameGlobal("m"));
		Exp nk = Expressions.makeBinary(Expressions.makeExpNameGlobal("n"),OP_MINUS,Expressions.makeExpNameGlobal("k"));
		List<Exp> args = Expressions.makeExpList(ab,lm,nk);
		Exp tl = Expressions.makeExpTableLookup(ab, xy);
		ExpFunctionCall fc = Expressions.makeExpFunCall(tl, args, null);
		show("expected=" + fc);
		assertEquals(e,fc);
	}
	
	/* Test case for prefix exp. Starting with the non-terminal Name. Should pass */
	@Test	
	void testPrefixexp9() throws Exception {
		String input = "ab:xy{3,a}";
		Exp e = parseExpAndShow(input);		
	
	}
	
	/* Test case for prefix exp. Starting with the non-terminal Name. Should pass */
	@Test	
	void testPrefixexp10() throws Exception {
		String input = "ab:xy\"lmn\"";
		Exp e = parseExpAndShow(input);		
	
	}
	
	/* Test case for prefix exp. Starting with the non-terminal Name. Should pass */
	@Test	
	void testPrefixexp11() throws Exception {
		String input = "(a+b)[xyz]";
		Exp e = parseExpAndShow(input);		
	
	}
	
	/* Test case for prefix exp. Starting with the non-terminal Name. Should pass */
	@Test	
	void testPrefixexp12() throws Exception {
		String input = "(a+b).xy";
		Exp e = parseExpAndShow(input);		
	
	}
	
	/* Test case for prefix exp. Starting with the non-terminal Name. Should pass */
	@Test	
	void testPrefixexp13() throws Exception {
		String input = "(a+b)(l+m,x-y)";
		Exp e = parseExpAndShow(input);		
	
	}
	
	/* Test case for prefix exp. Starting with the non-terminal Name. Should pass */
	@Test	
	void testPrefixexp14() throws Exception {
		String input = "(a+b) {field}";
		Exp e = parseExpAndShow(input);		
	
	}
	
	/* Test case for prefix exp. Starting with the non-terminal Name. Should pass */
	@Test	
	void testPrefixexp15() throws Exception {
		String input = "(a+b)\"xy\"";
		Exp e = parseExpAndShow(input);		
	
	}
	
	/* Test case for prefix exp. Starting with the non-terminal Name. Should pass */
	@Test	
	void testPrefixexp16() throws Exception {
		String input = "(a+b):xy(l-m,n-k)";
		Exp e = parseExpAndShow(input);		
	
	}
	
	/* Test case for prefix exp. Starting with the non-terminal Name. Should pass */
	@Test	
	void testPrefixexp17() throws Exception {
		String input = "(a+b)[f].pq : lmn \"xy\" {fields}";
		Exp e = parseExpAndShow(input);		
	
	}
	
	/* Test case for prefix exp. Starting with the non-terminal Name. Should fail */
	@Test	
	void testPrefixexp18() throws Exception {
		String input = "ab.23";
		Exp e = parseExpAndShow(input);		
	
	}
	
	/* Test case for prefix exp. Starting with the non-terminal Name. Should pass */
	@Test	
	void testPrefixexp19() throws Exception {
		String input = "ab[(x+y)(l-n)]";
		Exp e = parseExpAndShow(input);		
	
	}
	
	/* Test case for prefix exp. Starting with the non-terminal Name. Should fail */
	@Test	
	void testPrefixexp20() throws Exception {
		String input = "ab:(l)";
		Exp e = parseExpAndShow(input);		
	
	}
	
	/* Test case for prefix exp. Starting with the non-terminal Name. Should pass */
	@Test	
	void testPrefixexp21() throws Exception {
		String input = "";
		Exp e = parseExpAndShow(input);		
	
	}
	
	
	
	/* Test case for prefix exp. Starting with the non-terminal Name. Should pass */
	@Test	
	void testPrefixexp25() throws Exception {
		String input = "v:name (l,m,n)";
		Exp e = parseExpAndShow(input);		
	
	}
	
	/* Test case for block statements. Case - valist = explist. Should pass */
	@Test	
	void testblockstat1() throws Exception {
		String input = ";;;";
		Block b = parseBlockAndShow(input);		
	
	}
	
	/* Test case for block statements. Case - valist = explist. Should pass */
	@Test	
	void testblockstat2() throws Exception {
		String input = "a=(l+m)";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for block statements. Case - valist = explist. Should pass */
	@Test	
	void testblockstat3() throws Exception {
		String input = "(a+b).xy=(p+q)";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for block statements. Case - valist = explist. Should pass */
	@Test	
	void testblockstat4() throws Exception {
		String input = "ab:xy{fields}.lmn=(p+q)(a+b)";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for block statements. Case - valist = explist. Should pass */
	@Test	
	void testblockstat5() throws Exception {
		String input = "(a+b).xy:lmn\"pq\"=a+b,n.name";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for block statements. Case - valist = explist. Should pass */
	@Test	
	void testblockstat6() throws Exception {
		String input = "(a+b).xy,(l)[m+n] = \"string1\",\"string2\"";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for block statements. Case - valist = explist. Should FAIL */
	@Test	
	void testblockstat7() throws Exception {
		String input = "(a+b)=a";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for block statements. Case - valist = explist. Should pass */
	@Test	
	void testblockstat8() throws Exception {
		String input = "a[b]=g(k,l)[x.y]";
		Block b = parseBlockAndShow(input);		
	
	}
	
	/* Test case for block statements. Case - valist = explist. Should pass */
	@Test	
	void testblockstat9() throws Exception {
		String input = "g(k,l)[x.y]=a[b]";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for block statements. Case - valist = explist. Should FAIL */
	@Test	
	void testblockstat10() throws Exception {
		String input = "a(l,k)=y";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for prefix exp. Starting with the non-terminal Name. Should pass */
	@Test	
	void testblockstat11() throws Exception {
		String input = "(a+b)(k,l)[x.y]=1";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for prefix exp. Starting with the non-terminal Name. Should FAIL */
	@Test	
	void testblockstat12() throws Exception {
		String input = "v:name (l,m,n)";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for prefix exp. Starting with the non-terminal Name. Should pass */
	@Test	
	void testblockstat13() throws Exception {
		String input = "(a+b)[n],f{fields}.a,z=23";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for prefix exp. Starting with the non-terminal Name. Should FAIL */
	@Test	
	void testblockstat14() throws Exception {
		String input = "(a+b)[n],f{fields}.(v),z =23";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for prefix exp. Starting with the non-terminal Name. Should fail */
	@Test	
	void testblockstat15() throws Exception {
		String input = "(a+b)[n],f{fields}.a,(z)=23";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for prefix exp. Starting with the non-terminal Name. Should pass */
	@Test	
	void testblockstat16() throws Exception {
		String input = "(a+b)[n],f{fields}.a,z=23; v=a";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for prefix exp. Starting with the non-terminal Name. Should pass */
	@Test	
	void testblockstat17() throws Exception {
		String input = "(a+b)[n],f{fields}.a,z=23; (z)=v";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for block stat . Starting with label. Should pass */
	@Test	
	void testblockstatlabel1() throws Exception {
		String input = "::a::";
		Block b = parseBlockAndShow(input);		
	
	}
	
	/* Test case for block stat . Starting with label. Should FAIL */
	@Test	
	void testblockstatlabel2() throws Exception {
		String input = "::::";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for block stat . Starting with label. Should FAIL */
	@Test	
	void testblockstatlabel3() throws Exception {
		String input = "::123::";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for block stat . Starting with label. Should pass */
	@Test	
	void testblockstatlabel4() throws Exception {
		String input = "::a:: ::b::";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for block stat . Starting with label. Should pass */
	@Test	
	void testblockstatlabel5() throws Exception {
		String input = "::a:: (a+b)[n],f{fields}.a,z=23; v=a ::v:: ; ::b::";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for block stat . Starting with goto. Should pass */
	@Test	
	void testblockstatgoto1() throws Exception {
		String input = "goto abc1";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for block stat . Starting with goto. Should FAIL */
	@Test	
	void testblockstatgoto2() throws Exception {
		String input = "goto 124";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for block stat . Starting with goto. Should pass */
	@Test	
	void testblockstatgoto3() throws Exception {
		String input = "::a:: (a+b)[n],f{fields}.a,z=23; goto abc";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for block stat . Starting with goto. Should pass */
	@Test	
	void testblockstatgoto4() throws Exception {
		String input = "goto abc goto b";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for block stat . Starting with goto. Should fail */
	@Test	
	void testblockstatgoto5() throws Exception {
		String input = "goto";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for block stat . Starting with goto. Should FAIL */
	@Test	
	void testblockstatgoto6() throws Exception {
		String input = ":: goto c::";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for block stat . Starting with goto. Should FAIL */
	@Test	
	void testblockstatgoto7() throws Exception {
		String input = "goto ; abc";
		Block b = parseBlockAndShow(input);		
	
	}
	
	/* Test case for block stat . Starting with break. Should pass */
	@Test	
	void testblockstatbreak1() throws Exception {
		String input = "break";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for block stat . Starting with break. Should pass */
	@Test	
	void testblockstatbreak2() throws Exception {
		String input = "break; break; goto c";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for block stat . Starting with break. Should FAIL */
	@Test	
	void testblockstatbreak3() throws Exception {
		String input = "break abc;";
		Block b = parseBlockAndShow(input);		
	
	}
	
	/* Test case for block stat . Starting with do. Should pass */
	@Test	
	void testblockstatdo1() throws Exception {
		String input = "do end";
		Block b = parseBlockAndShow(input);		
	
	}
	
	/* Test case for block stat . Starting with do. Should pass */
	@Test	
	void testblockstatdo2() throws Exception {
		String input = "do g(k,l)[x.y]=a[b] end break";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for block stat . Starting with do. Should pass */
	@Test	
	void testblockstatdo3() throws Exception {
		String input = "do do g(k,l)[x.y]=a[b] end end break";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for block stat . Starting with do. Should FAIL */
	@Test	
	void testblockstatdo4() throws Exception {
		String input = "do g(k,l)[x.y]=a[b]";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for block stat . Starting with do. Should FAIL */
	@Test	
	void testblockstatdo5() throws Exception {
		String input = "do x=1 do x=2 end end return x";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for block stat . Starting with do. Should pass */
	@Test	
	void testblockstatdo6() throws Exception {
		String input = "do g(k,l)[x.y]=a[b] end 23";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for block stat . Starting with do. Should pass */
	@Test	
	void testblockstatdo7() throws Exception {
		String input = "do g(k,l)[x.y]=a[b] end ; do end; do do do end end end";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for block stat . Starting with do. Should pass */
	@Test	
	void testblockstatdo8() throws Exception {
		String input = "do goto b ::v:: end ";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for block stat . Starting with do. Should pass */
	@Test	
	void testblockstatdo9() throws Exception {
		String input = "while abc do g(k,l)[x.y]=a[b] end ";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for block stat . Starting with do. Should FAIL */
	@Test	
	void testblockstatdo10() throws Exception {
		String input = "while abc ; do g(k,l)[x.y]=a[b] end";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for block stat . Starting with do. Should pass */
	@Test	
	void testblockstatdo11() throws Exception {
		String input = "while abc do g(k,l)[x.y]=a[b] end end a=c;";
		Block b = parseBlockAndShow(input);		
	
	}

	/* Test case for block stat . Starting with repeat. Should FAIL */
	@Test	
	void testblockstatrepeat1() throws Exception {
		String input = "repeat while abc do g(k,l)[x.y]=a[b] end end until a=c";
		Block b = parseBlockAndShow(input);		
	
	}
	
	/* Test case for block stat . Starting with repeat. Should pass */
	@Test	
	void testblockstatrepeat2() throws Exception {
		String input = "repeat while abc do b=c end until x>y end";
		Block b = parseBlockAndShow(input);		
	
	}
	
	/* Test case for block stat . Starting with repeat. Should FAIL */
	@Test	
	void testblockstatrepeat3() throws Exception {
		String input = "repeat repeat while abc do g(k,l)[x.y]=a[b] end  until a=c while abc do b=c end until x>y ";
		Block b = parseBlockAndShow(input);		
	
	}
	
	/* Test case for block stat . Starting with repeat. Should pass */
	@Test	
	void testblockstatrepeat4() throws Exception {
		String input = "repeat repeat while abc do g(k,l)[x.y]=a[b] end  until a>c until s";
		Block b = parseBlockAndShow(input);		
	
	}
	
	/* Test case for block stat . Starting with if. Should pass */
	@Test	
	void testblockstatif1() throws Exception {
		String input = "if a>c then repeat while abc do g(k,l)[x.y]=a[b] end  until a>c elseif a<c then c=n end";
		Block b = parseBlockAndShow(input);		
	
	}
	
	/* Test case for block stat . Starting with if. Should pass */
	@Test	
	void testblockstatif2() throws Exception {
		String input = "if a>c then c=n else end";
		Block b = parseBlockAndShow(input);		
	
	}
	

	/* Test case for block stat . Starting with for. Should pass */
	@Test	
	void testblockstatfor1() throws Exception {
		String input = "for c=a,n<b,-n do c=b end";
		Block b = parseBlockAndShow(input);		
	
	}
	
	/* Test case for block stat . Starting with for. Should FAIL */
	@Test	
	void testblockstatfor2() throws Exception {
		String input = "for c=a,n<b end";
		Block b = parseBlockAndShow(input);		
	
	}
	
	/* Test case for block stat . Starting with for. Should pass */
	@Test	
	void testblockstatfor3() throws Exception {
		String input = "for c=a,n<b do end";
		Block b = parseBlockAndShow(input);		
	
	}
	
	/* Test case for block stat . Starting with for. Should FAIL */
	@Test	
	void testblockstatfor4() throws Exception {
		String input = "for c=a, do end";
		Block b = parseBlockAndShow(input);		
	
	}
	
	/* Test case for block stat . Starting with for. Should FAIL */
	@Test	
	void testblockstatfor5() throws Exception {
		String input = "for 123=123 ,v do end";
		Block b = parseBlockAndShow(input);		
	
	}
	
	/* Test case for block stat . Starting with for. Should FAIL */
	@Test	
	void testblockstatfor6() throws Exception {
		String input = "for abc,nc in a+b,q-p end";
		Block b = parseBlockAndShow(input);		
	
	}
	
	/* Test case for block stat . Starting with for. Should pass */
	@Test	
	void testblockstatfor7() throws Exception {
		String input = "for abc,nc in a+b do end";
		Block b = parseBlockAndShow(input);		
	
	}
	
	/* Test case for block stat . Starting with for. Should FAIL */
	@Test	
	void testblockstatfor8() throws Exception {
		String input = "for";
		Block b = parseBlockAndShow(input);		
	
	}
	
	/* Test case for block stat . Starting with for. Should FAIL */
	@Test	
	void testblockstatfor13() throws Exception {
		String input = "for in a+b do end";
		Block b = parseBlockAndShow(input);		
	
	}
	
	/* Test case for block stat . Starting with for. Should FAIL */
	@Test	
	void testblockstatfor9() throws Exception {
		String input = "for 123 in a+b do end";
		Block b = parseBlockAndShow(input);		
	
	}
	
	/* Test case for block stat . Starting with for. Should FAIL */
	@Test	
	void testblockstatfor10() throws Exception {
		String input = "for abc,mn in do end";
		Block b = parseBlockAndShow(input);		
	
	}
	
	/* Test case for block stat . Starting with for. Should FAIL */
	@Test	
	void testblockstatfor11() throws Exception {
		String input = "for abc,mn in pop do";
		Block b = parseBlockAndShow(input);		
	
	}
	
	/* Test case for block stat . Starting with for. Should pass */
	@Test	
	void testblockstatfor12() throws Exception {
		String input = "for abc,mn in p-1 do for xyz in m do end end";
		Block b = parseBlockAndShow(input);		
	
	}
	
	/* Test case for block stat . Starting with for. Should pass */
	@Test	
	void testblockstatfor14() throws Exception {
		String input = "for abc,mn in for do for xyz in m do end end";
		Block b = parseBlockAndShow(input);		
	
	}

	/* Test case for block stat . Starting with function. Should pass */
	@Test	
	void testblockstatfunc1() throws Exception {
		String input = "function a () do a=b end end";
		Block b = parseBlockAndShow(input);		
	
	}
	
	/* Test case for block stat . Starting with function. Should pass */
	@Test	
	void testblockstatfunc2() throws Exception {
		String input = "function b.c.a (p,n) do a=b end end";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for block stat . Starting with function. Should pass */
	@Test	
	void testblockstatfunc3() throws Exception {
		String input = "function b.c:a () do a=b end end";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for block stat . Starting with function. Should pass */
	@Test	
	void testblockstatfunc4() throws Exception {
		String input = "function b.123 () do a=b end end";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for block stat . Starting with function. Should pass */
	@Test	
	void testblockstatfunc5() throws Exception {
		String input = "function b:123 () do a=b end end";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for block stat . Starting with function. Should pass */
	@Test	
	void testblockstatfunc6() throws Exception {
		String input = "function b.d (123) do a=b end end";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for block stat . Starting with function. Should pass */
	@Test	
	void testblockstatfunc7() throws Exception {
		String input = "function b:c do a=b end end";
		Block b = parseBlockAndShow(input);		
	
	}
	
	/* Test case for block stat . Starting with function. Should pass */
	@Test	
	void testblockstatfunc8() throws Exception {
		String input = "function b:c do a=b end end";
		Block b = parseBlockAndShow(input);		
	
	}
	
	/* Test case for block stat . Starting with function. Should pass */
	@Test	
	void testblockstatfunc19() throws Exception {
		String input = "local function name (p,q) ::label:: end";
		Block b = parseBlockAndShow(input);		
	
	}
	
	/* Test case for block stat . Starting with function. Should FAIL */
	@Test	
	void testblockstatfunc9() throws Exception {
		String input = "local function 123 (p,q) ::label:: end";
		Block b = parseBlockAndShow(input);		
	
	}
	
	/* Test case for block stat . Starting with function. Should FAIL */
	@Test	
	void testblockstatfunc10() throws Exception {
		String input = "local function abc,abc (p,q) ::label:: end ";
		Block b = parseBlockAndShow(input);		
	
	}
	
	/* Test case for block stat . Starting with function. Should pass */
	@Test	
	void testblockstatfunc11() throws Exception {
		String input = "local function  nb () end";
		Block b = parseBlockAndShow(input);		
	
	}
	
	/* Test case for block stat . Starting with function. Should pass */
	@Test	
	void testblockstatfunc20() throws Exception {
		String input = "local abc,abc";
		Block b = parseBlockAndShow(input);		
	
	}
	
	/* Test case for block stat . Starting with function. Should pass */
	@Test	
	void testblockstatfunc13() throws Exception {
		String input = "local abc,abc = bc";
		Block b = parseBlockAndShow(input);		
	
	}
	
	/* Test case for block stat . Starting with function. Should FAIL */
	@Test	
	void testblockstatfunc14() throws Exception {
		String input = "local 123,abc = bs";
		Block b = parseBlockAndShow(input);		
	
	}
	
	/* Test case for block stat . Starting with function. Should FAIL */
	@Test	
	void testblockstatretstat() throws Exception {
		String input = "local abc,abc = \"trp\" return ";
		Block b = parseBlockAndShow(input);		
	
	}
	
	
	/* Test case for block stat . Starting with function. Should FAIL */
	@Test	
	void testblockstatfunc15() throws Exception {
		String input = "local abc,abc = v ret";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for block stat . Starting with function. Should pass */
	@Test	
	void testblockstatfunc16() throws Exception {
		String input = "local abc,abc = v return exp ";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for block stat . Starting with function. Should pass */
	@Test	
	void testblockstatfunc17() throws Exception {
		String input = "local abc,abc = v return ;";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for block stat . Starting with function. Should pass */
	@Test	
	void testblockstatfunc18() throws Exception {
		String input = "local abc,abc = v return ; ::label::";
		Block b = parseBlockAndShow(input);		
	
	}
	/* Test case for chunk. Should pass */
	@Test	
	void testchunk1() throws Exception {
		String input = "local abc,abc = v return ; ::label::";
		Chunk c = parseAndShow(input);		
	
	}
	
	/* Test case for chunk. Should pass */
	@Test	
	void testchunk2() throws Exception {
		String input = "do g(k,l)[x.y]=a[b] end end";
		Chunk c = parseAndShow(input);		
	
	}
	
	/* Test case for chunk. Should pass */
	@Test	
	void testchunk3() throws Exception {
		String input = "do g(k,l)[x.y]=a[b] end";
		Chunk c = parseAndShow(input);		
	
	}
	
	
	@Test
	void testIdent0() throws Exception {
		String input = "x";
		Exp e = parseExpAndShow(input);
		assertEquals(ExpName.class, e.getClass());
		assertEquals("x", ((ExpName) e).name);
	}

	@Test
	void testIdent1() throws Exception {
		String input = "(x)";
		Exp e = parseExpAndShow(input);
		assertEquals(ExpName.class, e.getClass());
		assertEquals("x", ((ExpName) e).name);
	}

	@Test
	void testString() throws Exception {
		String input = "\"string\"";
		Exp e = parseExpAndShow(input);
		assertEquals(ExpString.class, e.getClass());
		assertEquals("string", ((ExpString) e).v);
	}

	@Test
	void testBoolean0() throws Exception {
		String input = "true";
		Exp e = parseExpAndShow(input);
		assertEquals(ExpTrue.class, e.getClass());
	}

	@Test
	void testBoolean1() throws Exception {
		String input = "false";
		Exp e = parseExpAndShow(input);
		assertEquals(ExpFalse.class, e.getClass());
	}


	@Test
	void testBinary0() throws Exception {
		String input = "1 + 2";
		Exp e = parseExpAndShow(input);
		Exp expected = Expressions.makeBinary(1,OP_PLUS,2);
		show("expected="+expected);
		assertEquals(expected,e);
	}
//Failing
	@Test
	void testUnary0() throws Exception {
		String input = "-2";
		Exp e = parseExpAndShow(input);
		Exp expected = Expressions.makeExpUnary(OP_MINUS, 2);
		show("expected="+expected);
		assertEquals(expected,e);
	}

	//failing
	@Test
	void testUnary1() throws Exception {
		String input = "-*2\n";
		assertThrows(SyntaxException.class, () -> {
		Exp e = parseExpAndShow(input);
		});	
	}
	

	
	@Test
	void testRightAssoc() throws Exception {
		String input = "\"concat\" .. \"is\"..\"right associative\"";
		Exp e = parseExpAndShow(input);
		Exp expected = Expressions.makeBinary(
				Expressions.makeExpString("concat")
				, DOTDOT
				, Expressions.makeBinary("is",DOTDOT,"right associative"));
		show("expected=" + expected);
		assertEquals(expected,e);
	}
	
	@Test
	void testLeftAssoc() throws Exception {
		String input = "\"minus\" - \"is\" - \"left associative\"";
		Exp e = parseExpAndShow(input);
		Exp expected = Expressions.makeBinary(
				Expressions.makeBinary(
						Expressions.makeExpString("minus")
				, OP_MINUS
				, Expressions.makeExpString("is")), OP_MINUS, 
				Expressions.makeExpString("left associative"));
		show("expected=" + expected);
		assertEquals(expected,e);
		
	}
	
	
	@Test
	void testNameList() throws Exception {
		String input = "function (a) end";
		Exp e = parseExpAndShow(input);
		//Exp expected = Expressions.makeBinary(Expressions.makeBinary(Expressions.makeExpNil("nil"),
		//		KW_or,
		//		Expressions.makeExpNil("nil")),KW_or,
		//		Expressions.makeExpNil("nil"));
		//show("expected="+expected);
		//assertEquals(expected,e);
	}

	//Test whether the code works for nil expression. 
	@Test
	void testNil0() throws Exception {
		String input = "nil";
		Exp e = parseExpAndShow(input);
		assertEquals(ExpNil.class, e.getClass());
	}
	
	//Adding random characters after nil 
		@Test
		void testNil1() throws Exception {
			String input = "nil a";
			Exp e = parseExpAndShow(input);
			assertEquals(ExpNil.class, e.getClass());
		}
	
	//true 
	@Test
	void testTrue0() throws Exception {
			String input = "true";
			Exp e = parseExpAndShow(input);
			assertEquals(ExpTrue.class, e.getClass());
		}
		
	//characters after true
	@Test
	void testTrue1() throws Exception {
			String input = "true nil";
			Exp e = parseExpAndShow(input);
			assertEquals(ExpTrue.class, e.getClass());
		}
	
	//false 
		@Test
		void testFalse0() throws Exception {
				String input = "false";
				Exp e = parseExpAndShow(input);
				assertEquals(ExpFalse.class, e.getClass());
			}
			
		//characters after false
		@Test
		void testFalse1() throws Exception {
				String input = "false true";
				Exp e = parseExpAndShow(input);
				assertEquals(ExpFalse.class, e.getClass());
			}
		
		//IntLiteral
		@Test
		void testInt0() throws Exception {
			String input = "123";
			Exp e = parseExpAndShow(input);
			assertEquals(ExpInt.class, e.getClass());
			assertEquals(123, ((ExpInt) e).v);
		}
		
		//IntLiteral
		@Test
		void testInt1() throws Exception {
				String input = "0123";
				Exp e = parseExpAndShow(input);
				assertEquals(ExpInt.class, e.getClass());
				assertEquals(0, ((ExpInt) e).v);
				}
		
		//VarArghs
		@Test
		void testvar0() throws Exception {
				String input = "... a";
				Exp e = parseExpAndShow(input);
				assertEquals(ExpVarArgs.class, e.getClass());
			//	assertEquals("...", ((ExpVarArgs) e).v);
		}
		
		//Prefixexp
		@Test
		void testPrefixExp1() throws Exception {
			String input = "Abc";
			Exp e = parseExpAndShow(input);
			assertEquals(ExpName.class, e.getClass());
			assertEquals("Abc", ((ExpName) e).name);
		}		
	
		@Test
		void testPrefixExp2() throws Exception {
			String input = "(Abc)";
			Exp e = parseExpAndShow(input);
			assertEquals(ExpName.class, e.getClass());
			assertEquals("Abc", ((ExpName) e).name);
		}		

		@Test
		void testPrefixExp3() throws Exception {
			String input = "(Abc) a";
			Exp e = parseExpAndShow(input);
			assertEquals(ExpName.class, e.getClass());
			assertEquals("Abc", ((ExpName) e).name);
		}
		
		//Unary
		@Test
		void testUnary2() throws Exception {
			String input = "-2";
			Exp e = parseExpAndShow(input);
			Exp expected = Expressions.makeExpUnary(OP_MINUS, 2);
			show("expected="+expected);
			assertEquals(expected,e);
		}
		
		//Unary
		@Test
		void testUnary3() throws Exception {
			String input = "not 2";
			Exp e = parseExpAndShow(input);
			Exp expected = Expressions.makeExpUnary(KW_not, 2);
			show("expected="+expected);
			assertEquals(expected,e);
		}
		
		//Unary
		@Test
		void testUnary4() throws Exception {
			String input = "-2 a";
			Exp e = parseExpAndShow(input);
			Exp expected = Expressions.makeExpUnary(OP_MINUS, 2);
			show("expected="+expected);
			assertEquals(expected,e);
		}
		
		//Unary
		@Test
		void testUnary5() throws Exception {
			String input = "#2";
			Exp e = parseExpAndShow(input);
			Exp expected = Expressions.makeExpUnary(OP_HASH, 2);
			show("expected="+expected);
			assertEquals(expected,e);
		}
		
		//Unary
		@Test
		void testUnary6() throws Exception {
			String input = "~2";
			Exp e = parseExpAndShow(input);
			Exp expected = Expressions.makeExpUnary(BIT_XOR, 2);
			show("expected="+expected);
			assertEquals(expected,e);
		}
		
		//Binary Operators
		@Test
		void testBOP1() throws Exception {
			String input = "2+3+4";
			Exp e = parseExpAndShow(input);
			Exp expected = Expressions.makeBinary(
					Expressions.makeBinary(
							Expressions.makeInt(2)
					, OP_PLUS
					, Expressions.makeInt(3)), OP_PLUS, 
					Expressions.makeInt(4));
			show("expected=" + expected);
			assertEquals(expected,e);
			
		}
		
		
		@Test
		void testBOP2() throws Exception {
			String input = "2-3+4";
			Exp e = parseExpAndShow(input);
			Exp expected = Expressions.makeBinary(
					Expressions.makeBinary(
							Expressions.makeInt(2)
					, OP_MINUS
					, Expressions.makeInt(3)), OP_PLUS, 
					Expressions.makeInt(4));
			show("expected=" + expected);
			assertEquals(expected,e);
			
		}
		
		
		@Test
		void testBOP3() throws Exception {
			String input = "2*3+4";
			Exp e = parseExpAndShow(input);
			Exp expected = Expressions.makeBinary(
					Expressions.makeBinary(
							Expressions.makeInt(2)
					, OP_TIMES
					, Expressions.makeInt(3)), OP_PLUS, 
					Expressions.makeInt(4));
			show("expected=" + expected);
			assertEquals(expected,e);
			
		}
		
		
		@Test
		void testBOP4() throws Exception {
			String input = "2-3*4";
			Exp e = parseExpAndShow(input);
			Exp expected = Expressions.makeBinary(Expressions.makeInt(2), OP_MINUS,
					Expressions.makeBinary(
							Expressions.makeInt(3)
					, OP_TIMES
					, Expressions.makeInt(4))
					);
			show("expected=" + expected);
			assertEquals(expected,e);
			
		}
		
		@Test
		void testBOP5() throws Exception {
			String input = "2*3/4";
			Exp e = parseExpAndShow(input);
			Exp expected = Expressions.makeBinary(
					Expressions.makeBinary(
							Expressions.makeInt(2)
					, OP_TIMES
					, Expressions.makeInt(3)), OP_DIV, 
					Expressions.makeInt(4));
			show("expected=" + expected);
			assertEquals(expected,e);
			
		}
		
		@Test
		void testBOP6() throws Exception {
			String input = "-2+3*4";
			Exp e = parseExpAndShow(input);
			Exp expected = Expressions.makeBinary(Expressions.makeExpUnary(OP_MINUS,2),OP_PLUS,
					Expressions.makeBinary(
							Expressions.makeInt(3)
					, OP_TIMES
					, Expressions.makeInt(4))
					);
			show("expected=" + expected);
			assertEquals(expected,e);
			
		}
		
		@Test
		void testBOP7() throws Exception {
			String input = "4/-2";
			Exp e = parseExpAndShow(input);
			Exp expected = 
					Expressions.makeBinary(
							Expressions.makeInt(4)
					, OP_DIV
					, Expressions.makeExpUnary(OP_MINUS,2));
					
			show("expected=" + expected);
			assertEquals(expected,e);
			
		}
		
		@Test
		void testBOP8() throws Exception {
			String input = "3++";
			Exp e = parseExpAndShow(input);
			
			
		}
		
		//comments
		@Test
		void testBOP9() throws Exception {
			String input = "3--";
			Exp e = parseExpAndShow(input);
			
			
		}
		
		//Should fail
		@Test
		void testBOP10() throws Exception {
			String input = "3**";
			Exp e = parseExpAndShow(input);
			
		}
		
		@Test
		void testBOP11() throws Exception {
			String input = "3//";
			Exp e = parseExpAndShow(input);
			
			
		}
		
		
		@Test
		void testBOP12() throws Exception {
			String input = "3%";
			Exp e = parseExpAndShow(input);
			
		}
		
		
		@Test
		void testBOP13() throws Exception {
			String input = "-2^3";
			Exp e = parseExpAndShow(input);
			Exp expected = 
					Expressions.makeExpUnary(OP_MINUS,
							Expressions.makeBinary(Expressions.makeInt(2)
					, OP_POW
					, Expressions.makeInt(3)));
			show("expected=" + expected);
			assertEquals(expected,e);
			
		}
		
		
		@Test
		void testBOP14() throws Exception {
			String input = "-2^3^2";
			Exp e = parseExpAndShow(input);
			Exp expected = Expressions.makeExpUnary(OP_MINUS, Expressions.makeBinary(
					Expressions.makeInt(2), OP_POW, 
					Expressions.makeBinary(
							Expressions.makeInt(3)
					, OP_POW
					, Expressions.makeInt(2))
					));
			show("expected=" + expected);
			assertEquals(expected,e);
			
		}
		
		
		@Test
		void testBOP15() throws Exception {
			String input = "3^-2^-4";
			Exp e = parseExpAndShow(input);
			
			
		}
		
		//Fail case
		@Test
		void testBOP16() throws Exception {
			String input = "3^^3";
			Exp e = parseExpAndShow(input);
			
			
		}
		
		//Fail case
		@Test
		void testBOP17() throws Exception {
			String input = "3++2";
			Exp e = parseExpAndShow(input);
			
			
		}
		
		//Fail case
		@Test
		void testBOP18() throws Exception {
			String input = "+3";
			Exp e = parseExpAndShow(input);
		
			
		}
		
		
		@Test
		void testBOP19() throws Exception {
			String input = "3..4";
			Exp e = parseExpAndShow(input);
			Exp expected = 
					Expressions.makeBinary(
							Expressions.makeInt(3)
					, DOTDOT
					, Expressions.makeInt(4));
			show("expected=" + expected);
			assertEquals(expected,e);
			
		}
		
		
		@Test
		void testBOP20() throws Exception {
			String input = "3..4..5";
			Exp e = parseExpAndShow(input);
			Exp expected = Expressions.makeBinary(Expressions.makeInt(3), DOTDOT,
					Expressions.makeBinary(
							Expressions.makeInt(4)
					, DOTDOT
					, Expressions.makeInt(5))
					);
			show("expected=" + expected);
			assertEquals(expected,e);
			
		}
		
		//Fail Case
		@Test
		void testBOP21() throws Exception {
			String input = "3..3..)";
			Exp e = parseExpAndShow(input);
			
		}
		
		
		

		@Test
		void testBOP22() throws Exception {
			String input = "3<<4";
			Exp e = parseExpAndShow(input);
			Exp expected = 
					Expressions.makeBinary(
							Expressions.makeInt(3)
					, BIT_SHIFTL
					, Expressions.makeInt(4));
			show("expected=" + expected);
			assertEquals(expected,e);
			
		}
		
		
		@Test
		void testBOP23() throws Exception {
			String input = "3<<4>>5";
			Exp e = parseExpAndShow(input);
			Exp expected = Expressions.makeBinary(Expressions.makeBinary(Expressions.makeInt(3), BIT_SHIFTL,
					
							Expressions.makeInt(4))
					, BIT_SHIFTR
					, Expressions.makeInt(5))
					;
			show("expected=" + expected);
			assertEquals(expected,e);
			
		}
		
		//Fail Case
		@Test
		void testBOP24() throws Exception {
			String input = "3<<3>>";
			Exp e = parseExpAndShow(input);
			
		}
		
		
		
		
		@Test
		void testBOP25() throws Exception {
			String input = "3&4";
			Exp e = parseExpAndShow(input);
			Exp expected = 
					Expressions.makeBinary(
							Expressions.makeInt(3)
					, BIT_AMP
					, Expressions.makeInt(4));
			show("expected=" + expected);
			assertEquals(expected,e);
			
		}
		
		
		@Test
		void testBOP26() throws Exception {
			String input = "3&4&5";
			Exp e = parseExpAndShow(input);
			Exp expected = Expressions.makeBinary(Expressions.makeBinary(Expressions.makeInt(3), BIT_AMP,
					
							Expressions.makeInt(4))
					, BIT_AMP
					, Expressions.makeInt(5))
					;
			show("expected=" + expected);
			assertEquals(expected,e);
			
		}
		
		//Fail Case
		@Test
		void testBOP27() throws Exception {
			String input = "9&8>>7..6+5*(not 4)^2";
			Exp e = parseExpAndShow(input);
			
		}
		
		
		@Test
		void testBOP28() throws Exception {
			String input = "5^7^(-4 or -6)";
			Exp e = parseExpAndShow(input);
			
			
		}
		
		//Pass case
		@Test
		void testBOP29() throws Exception {
			String input = "3 not 4 (";
			Exp e = parseExpAndShow(input);
	
			
		}
		
		@Test
		void testBOP30() throws Exception {
			String input = "Name or (Name)";
			Exp e = parseExpAndShow(input);
			
			
		}
		
		@Test
		void testBOP31() throws Exception {
			String input = "- \"string\"";
			Exp e = parseExpAndShow(input);
			
			
		}
		
		//fail case
		@Test
		void testFUNC1() throws Exception {
			String input = "function ";
			Exp e = parseExpAndShow(input);
			
			
		}
		
		//pass case
		@Test
		void testFUNC2() throws Exception {
			String input = "function () end";
			Exp e = parseExpAndShow(input);
			
			
		}
		
		//fail case
		@Test
		void testFUNC3() throws Exception {
			String input = "function end";
			Exp e = parseExpAndShow(input);
			
			
		}
		
		//fail case
		@Test
		void testFUNC4() throws Exception {
			String input = "function () ";
			Exp e = parseExpAndShow(input);
			
			
		}
		
		//Pass case
		@Test
		void testFUNC5() throws Exception {
			String input = "function () end + 3";
			Exp e = parseExpAndShow(input);
			
			
		}
		
		//fail case
		@Test
		void testFUNC6() throws Exception {
			String input = "function () 123 end";
			Exp e = parseExpAndShow(input);
			
			
		}
		
		//Fail case
		@Test
		void testFUNC7() throws Exception {
			String input = "function (()) end";
			Exp e = parseExpAndShow(input);
			
			
		}
		
		//Pass case
		@Test
		void testFUNC8() throws Exception {
			String input = "function (...) end";
			Exp e = parseExpAndShow(input);
			
			
		}
		
		//Fail Case
		@Test
		void testFUNC9() throws Exception {
			String input = "function (... ... ) end";
			Exp e = parseExpAndShow(input);
			
			
		}
		
		//Pass case
		@Test
		void testFUNC10() throws Exception {
			String input = "function (Abc) end";
			Exp e = parseExpAndShow(input);
			
			
		}
		
		//Pass case
		@Test
		void testFUNC11() throws Exception {
			String input = "function (Abc ...) end";
			Exp e = parseExpAndShow(input);
			
			
		}
		
		//Pass case
		@Test
		void testFUNC12() throws Exception {
			String input = "function (Abc,...,abc) end";
			Exp e = parseExpAndShow(input);
			
			
		}
		
		//Fail case
		@Test
		void testFUNC13() throws Exception {
			String input = "function (abc,abc ,...,...) end";
			Exp e = parseExpAndShow(input);
		}
		
		//Pass case
		@Test
		void testFUNC14() throws Exception {
			String input = "function (abc,xyz) end";
			Exp e = parseExpAndShow(input);
		}
		
		//fail case
		@Test
		void testFUNC15() throws Exception {
			String input = "function (abc,pool,) end";
			Exp e = parseExpAndShow(input);
		}
		
		//Pass case
		@Test
		void testFUNC16() throws Exception {
			String input = "function (abc,ppol,...) end";
			Exp e = parseExpAndShow(input);
		}
		
		//fail case
		@Test
		void testFUNC17() throws Exception {
			String input = "function (abc,pool,ab+) end";
			Exp e = parseExpAndShow(input);
		}
		
		@Test
		void testFUNC18() throws Exception {
			String input = "function () end (";
			Exp e = parseExpAndShow(input);
		}
		
		//pass case
		@Test
		void testFUNC19() throws Exception {
			String input = "function (...,a) end";
			Exp e = parseExpAndShow(input);
		}
		
		//fail case
		@Test
		void testFUNC20() throws Exception {
			String input = "function ) end";
			Exp e = parseExpAndShow(input);
		}
		
		@Test
		void testFUNC21() throws Exception {
			String input = "function (abc,pool,ab+) end";
			Exp e = parseExpAndShow(input);
		}
		
		//Pass case
		@Test
		void testFIELD1() throws Exception {
			String input = "{}";
			Exp e = parseExpAndShow(input);
		}
		
		@Test
		void testFIELD2() throws Exception {
			String input = "{3+4}";
			Exp e = parseExpAndShow(input);
		}
		
		//Fail case
		@Test
		void testFIELD3() throws Exception {
			String input = "{3+4 3+4}";
			Exp e = parseExpAndShow(input);
		}
		
		//fail case
		@Test
		void testFIELD4() throws Exception {
			String input = "{[3+4}";
			Exp e = parseExpAndShow(input);
		}
		
		//pass case
		@Test
		void testFIELD5() throws Exception {
			String input = "{[3^4]=abc}";
			Exp e = parseExpAndShow(input);
		}
		
		//fail case
		@Test
		void testFIELD6() throws Exception {
			String input = "{[]=abc}";
			Exp e = parseExpAndShow(input);
		}
		
		//Fail case
		@Test
		void testFIELD7() throws Exception {
			String input = "{[3+] = abc}";
			Exp e = parseExpAndShow(input);
		}
		
		@Test
		void testFIELD8() throws Exception {
			String input = "{[abc] abc}";
			Exp e = parseExpAndShow(input);
		}
		
		//fail case
		@Test
		void testFIELD9() throws Exception {
			String input = "{[abc + bvc] = \"string\" )}";
			Exp e = parseExpAndShow(input);
		}
		
		@Test
		void testFIELD10() throws Exception {
			String input = "{[ds] = (abc)} (";
			Exp e = parseExpAndShow(input);
		}
		
		@Test
		void testFIELD11() throws Exception {
			String input = "{abc  = abc}";
			Exp e = parseExpAndShow(input);
		}
		
		//fail case
		@Test
		void testFIELD12() throws Exception {
			String input = "{3+4 = abc}";
			Exp e = parseExpAndShow(input);
		}
		
		//fail case
		@Test
		void testFIELD13() throws Exception {
			String input = "{abc = 3+4+}";
			Exp e = parseExpAndShow(input);
		}
		
		@Test
		void testFIELD14() throws Exception {
			String input = "{abc = -4^5}";
			Exp e = parseExpAndShow(input);
		}
		
		@Test
		void testFIELD15() throws Exception {
			String input = "{abc = -4^5},{abc = -4^5}";
			Exp e = parseExpAndShow(input);
		}
		
		@Test
		void testFIELD16() throws Exception {
			String input = "{abc = -4^5};{abc = -4^5};{abc = -4^5}";
			Exp e = parseExpAndShow(input);
		}
		
		@Test
		void testFIELD17() throws Exception {
			String input = "{abc = -4^5};lmn";
			Exp e = parseExpAndShow(input);
		}
		
		@Test
		void testFIELD18() throws Exception {
			String input = "{abc = -4^5;lmn}";
			Exp e = parseExpAndShow(input);
		}
		
		@Test
		void testFIELD19() throws Exception {
			String input = "{abc = -4^5;2;}";
			Exp e = parseExpAndShow(input);
		}
		
		@Test
		void testFIELD20() throws Exception {
			String input = "{abc = -4^5}";
			Exp e = parseExpAndShow(input);
		}
		
		@Test
		void testFIELD21() throws Exception {
			String input = "{abc = -4^5}";
			Exp e = parseExpAndShow(input);
		}
		
		@Test
		void testFIELD22() throws Exception {
			String input = "function() end + {abc = -4^5}";
			Exp e = parseExpAndShow(input);
		}
		
		@Test
		void testEMPTY() throws Exception {
			String input = "{3}";
			Exp e = parseExpAndShow(input);
		}
		
		@Test
		void testFailCase1() throws Exception {
			String input = "function(...) end";
			Exp e = parseExpAndShow(input);
		}
		
		@Test
		void testFailCase2() throws Exception {
			String input = "- -23 --a";
			Exp e = parseExpAndShow(input);
		}
		
		@Test
		void testFailCase3() throws Exception {
			String input = "{}";
			Exp e = parseExpAndShow(input);
		}
		
		@Test
		void testFailCase4() throws Exception {
			String input = "{3,a}";
			Exp e = parseExpAndShow(input);
		}

		@Test
		void testFailCase5() throws Exception {
			String input = "not #~1";
			Exp e = parseExpAndShow(input);
		}
		
		@Test
		void testbuild1() throws Exception {
			String input = "a:b(c)";
			Exp e = parseExpAndShow(input);
		}

		/* Test case for block  . failed case */
		@Test	
		void testblockfailcase1() throws Exception {
			String input = "a,c=8,f()";
			Block b = parseBlockAndShow(input);		
		
		}
		
		/* Test case for block  . failed case */
		@Test	
		void testblockfailcase2() throws Exception {
			String input = "x = f();";
			Block b = parseBlockAndShow(input);		
		
		}
		
		/* INterpreter test*/
		@Test	
		void interpretertest1() throws Exception {
			String input = "x = { \nprint(\n\"This is returning nothing. Should this throw?\"\n) \n} \nreturn x";
			Block b = parseBlockAndShow(input);		
		
		}
		
		/* INterpreter test*/
		@Test	
		void interpretertest2() throws Exception {
			String input = "do\n    goto canItSeeThisLabel    ::gotoCanSeeThisLabel::\r\n    return 'obviously it wont hit this'\r\nend\r\n::canItSeeThisLabel::\r\nreturn 'in outer scopes does the label need to occur before the block containing the goto statement?'";
			Block b = parseBlockAndShow(input);		
		
		}
		
}

