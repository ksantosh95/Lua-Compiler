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
		List<Exp> lhs = Expressions.makeExpList(Expressions.makeExpName("a"));
		List<Exp> rhs = Expressions.makeExpList(Expressions.makeExpName("b"));
		StatAssign s = Expressions.makeStatAssign(lhs,rhs);
		Block expected = Expressions.makeBlock(s);
		assertEquals(expected,b);
	}
	
	@Test
	void testAssignChunk1() throws Exception {
		String input = "a=b";
		ASTNode c = parseAndShow(input);		
		List<Exp> lhs = Expressions.makeExpList(Expressions.makeExpName("a"));
		List<Exp> rhs = Expressions.makeExpList(Expressions.makeExpName("b"));
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
					Expressions.makeExpName("a")
					,Expressions.makeExpName("c"));
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
					Expressions.makeExpName("a")
					,Expressions.makeExpName("c"));
		Exp e1 = Expressions.makeExpInt(8);
		List<Exp> args = new ArrayList<>();
		args.add(Expressions.makeExpName("x"));
		Exp e2 = Expressions.makeExpFunCall(Expressions.makeExpName("f"),args, null);
		List<Exp> rhs = Expressions.makeExpList(e1,e2);
		StatAssign s = Expressions.makeStatAssign(lhs,rhs);
		Block expected = Expressions.makeBlock(s);
		assertEquals(expected,b);			
	}
	

	
	@Test
	void testAssignToTable() throws Exception {
		String input = "g.a.b = 3";
		Block bl = parseBlockAndShow(input);
		ExpName g = Expressions.makeExpName("g");
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
		ExpName g = Expressions.makeExpName("g");
		ExpString a = Expressions.makeExpString("a");
		Exp gtable = Expressions.makeExpTableLookup(g,a);
		ExpString b = Expressions.makeExpString("b");
		Exp e = Expressions.makeExpTableLookup(gtable, b);
		Exp v = Expressions.makeExpName("x");		
		Stat s = Expressions.makeStatAssign(Expressions.makeExpList(v), Expressions.makeExpList(e));;
		Block expected = Expressions.makeBlock(s);
		assertEquals(expected,bl);
	}
	

	
	@Test
	void testmultistatements6() throws Exception {
		String input = "x = g.a.b ; ::mylabel:: do  y = 2 goto mylabel f=a(0,200) end break"; //same as testmultistatements0 except ;
		ASTNode c = parseAndShow(input);
		ExpName g = Expressions.makeExpName("g");
		ExpString a = Expressions.makeExpString("a");
		Exp gtable = Expressions.makeExpTableLookup(g,a);
		ExpString b = Expressions.makeExpString("b");
		Exp e = Expressions.makeExpTableLookup(gtable, b);
		Exp v = Expressions.makeExpName("x");		
		Stat s0 = Expressions.makeStatAssign(v,e);
		StatLabel s1 = Expressions.makeStatLabel("mylabel");
		Exp y = Expressions.makeExpName("y");
		Exp two = Expressions.makeExpInt(2);
		Stat s2 = Expressions.makeStatAssign(y,two);
		Stat s3 = Expressions.makeStatGoto("mylabel");
		Exp f = Expressions.makeExpName("f");
		Exp ae = Expressions.makeExpName("a");
		Exp zero = Expressions.makeExpInt(0);
		Exp twohundred = Expressions.makeExpInt(200);
		List<Exp> args = Expressions.makeExpList(zero, twohundred);
		ExpFunctionCall fc = Expressions.makeExpFunCall(ae, args, null);		
		StatAssign s4 = Expressions.makeStatAssign(f,fc);
		StatDo statdo = Expressions.makeStatDo(s2,s3,s4);
		StatBreak statBreak = Expressions.makeStatBreak();
		Block expectedBlock = Expressions.makeBlock(s0,s1,statdo,statBreak);
		Chunk expectedChunk = new Chunk(expectedBlock.firstToken, expectedBlock);
		//show("expected="+expectedChunk);
		assertEquals(expectedChunk,c);
	}
	
	@Test
	void testMultiStatements7() throws Exception {
		String input = "x = g.a.b ; ::mylabel:: do  y = 2 goto mylabel f=a(0,200) end "; //same as testmultistatements0 except ;
		ASTNode c = parseAndShow(input);
		
	}
	
	
	
	/* Test case for prefix exp. Starting with the non-terminal Name. Should pass */
	@Test	
	void testPrefixexp1() throws Exception {
		String input = "f(a)[b]\"g\"";
		Exp e = parseExpAndShow(input);		
		Exp f = Expressions.makeExpName("f");
		Exp a = Expressions.makeExpName("a");
		List<Exp> args = Expressions.makeExpList(a);
		Exp b = Expressions.makeExpName("b");
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
		Exp ab = Expressions.makeExpName("ab");
		Exp xyz = Expressions.makeExpName("xyz");
		Exp tl = Expressions.makeExpTableLookup(ab, xyz);
		show("expected=" + tl);
		assertEquals(e,tl);
	}
	
	/* Test case for prefix exp. Starting with the non-terminal Name. Should pass */
	@Test	
	void testPrefixexp4() throws Exception {
		String input = "ab.xyz";
		Exp e = parseExpAndShow(input);
		Exp ab = Expressions.makeExpName("ab");
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
		Exp ab = Expressions.makeExpName("ab");
		Exp xy = Expressions.makeBinary(Expressions.makeExpName("x"),OP_PLUS,Expressions.makeExpName("y"));
		Exp mn = Expressions.makeBinary(Expressions.makeExpName("m"),OP_MINUS,Expressions.makeExpName("n"));
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
		String input = "ab{3,a}";
		Exp e = parseExpAndShow(input);		
	
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

	
}

