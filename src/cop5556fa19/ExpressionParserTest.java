/* *
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

import static cop5556fa19.Token.Kind.*;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.io.Reader;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

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
import cop5556fa19.AST.Expressions;
import cop5556fa19.AST.Field;
import cop5556fa19.AST.FieldExpKey;
import cop5556fa19.AST.FieldImplicitKey;
import cop5556fa19.AST.ParList;
import cop5556fa19.ExpressionParser.SyntaxException;

class ExpressionParserTest {

	// To make it easy to print objects and turn this output on and off
	static final boolean doPrint = true;

	private void show(Object input) {
		if (doPrint) {
			System.out.println(input.toString());
		}
	}


	
	// creates a scanner, parser, and parses the input.  
	Exp parseAndShow(String input) throws Exception {
		show("parser input:\n" + input); // Display the input
		Reader r = new StringReader(input);
		Scanner scanner = new Scanner(r); // Create a Scanner and initialize it
		ExpressionParser parser = new ExpressionParser(scanner);  // Create a parser
		Exp e = parser.exp(); // Parse and expression
		show("e=" + e);  //Show the resulting AST
		return e;
	}
	


	@Test
	void testIdent0() throws Exception {
		String input = "x";
		Exp e = parseAndShow(input);
		assertEquals(ExpName.class, e.getClass());
		assertEquals("x", ((ExpName) e).name);
	}

	@Test
	void testIdent1() throws Exception {
		String input = "(x)";
		Exp e = parseAndShow(input);
		assertEquals(ExpName.class, e.getClass());
		assertEquals("x", ((ExpName) e).name);
	}

	@Test
	void testString() throws Exception {
		String input = "\"string\"";
		Exp e = parseAndShow(input);
		assertEquals(ExpString.class, e.getClass());
		assertEquals("string", ((ExpString) e).v);
	}

	@Test
	void testBoolean0() throws Exception {
		String input = "true";
		Exp e = parseAndShow(input);
		assertEquals(ExpTrue.class, e.getClass());
	}

	@Test
	void testBoolean1() throws Exception {
		String input = "false";
		Exp e = parseAndShow(input);
		assertEquals(ExpFalse.class, e.getClass());
	}


	@Test
	void testBinary0() throws Exception {
		String input = "1 + 2";
		Exp e = parseAndShow(input);
		Exp expected = Expressions.makeBinary(1,OP_PLUS,2);
		show("expected="+expected);
		assertEquals(expected,e);
	}
//Failing
	@Test
	void testUnary0() throws Exception {
		String input = "-2";
		Exp e = parseAndShow(input);
		Exp expected = Expressions.makeExpUnary(OP_MINUS, 2);
		show("expected="+expected);
		assertEquals(expected,e);
	}

	//failing
	@Test
	void testUnary1() throws Exception {
		String input = "-*2\n";
		assertThrows(SyntaxException.class, () -> {
		Exp e = parseAndShow(input);
		});	
	}
	

	
	@Test
	void testRightAssoc() throws Exception {
		String input = "\"concat\" .. \"is\"..\"right associative\"";
		Exp e = parseAndShow(input);
		Exp expected = Expressions.makeBinary(
				Expressions.makeExpString("\"concat\"")
				, DOTDOT
				, Expressions.makeBinary("\"is\"",DOTDOT,"\"right associative\""));
		show("expected=" + expected);
		assertEquals(expected,e);
	}
	
	@Test
	void testLeftAssoc() throws Exception {
		String input = "\"minus\" - \"is\" - \"left associative\"";
		Exp e = parseAndShow(input);
		Exp expected = Expressions.makeBinary(
				Expressions.makeBinary(
						Expressions.makeExpString("\"minus\"")
				, OP_MINUS
				, Expressions.makeExpString("\"is\"")), OP_MINUS, 
				Expressions.makeExpString("\"left associative\""));
		show("expected=" + expected);
		assertEquals(expected,e);
		
	}
	
	
	@Test
	void testNameList() throws Exception {
		String input = "function (a) end";
		Exp e = parseAndShow(input);
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
		Exp e = parseAndShow(input);
		assertEquals(ExpNil.class, e.getClass());
	}
	
	//Adding random characters after nil 
		@Test
		void testNil1() throws Exception {
			String input = "nil a";
			Exp e = parseAndShow(input);
			assertEquals(ExpNil.class, e.getClass());
		}
	
	//true 
	@Test
	void testTrue0() throws Exception {
			String input = "true";
			Exp e = parseAndShow(input);
			assertEquals(ExpTrue.class, e.getClass());
		}
		
	//characters after true
	@Test
	void testTrue1() throws Exception {
			String input = "true nil";
			Exp e = parseAndShow(input);
			assertEquals(ExpTrue.class, e.getClass());
		}
	
	//false 
		@Test
		void testFalse0() throws Exception {
				String input = "false";
				Exp e = parseAndShow(input);
				assertEquals(ExpFalse.class, e.getClass());
			}
			
		//characters after false
		@Test
		void testFalse1() throws Exception {
				String input = "false true";
				Exp e = parseAndShow(input);
				assertEquals(ExpFalse.class, e.getClass());
			}
		
		//IntLiteral
		@Test
		void testInt0() throws Exception {
			String input = "123";
			Exp e = parseAndShow(input);
			assertEquals(ExpInt.class, e.getClass());
			assertEquals(123, ((ExpInt) e).v);
		}
		
		//IntLiteral
		@Test
		void testInt1() throws Exception {
				String input = "0123";
				Exp e = parseAndShow(input);
				assertEquals(ExpInt.class, e.getClass());
				assertEquals(0, ((ExpInt) e).v);
				}
		
		//VarArghs
		@Test
		void testvar0() throws Exception {
				String input = "... a";
				Exp e = parseAndShow(input);
				assertEquals(ExpVarArgs.class, e.getClass());
			//	assertEquals("...", ((ExpVarArgs) e).v);
		}
		
		//Prefixexp
		@Test
		void testPrefixExp1() throws Exception {
			String input = "Abc";
			Exp e = parseAndShow(input);
			assertEquals(ExpName.class, e.getClass());
			assertEquals("Abc", ((ExpName) e).name);
		}		
	
		@Test
		void testPrefixExp2() throws Exception {
			String input = "(Abc)";
			Exp e = parseAndShow(input);
			assertEquals(ExpName.class, e.getClass());
			assertEquals("Abc", ((ExpName) e).name);
		}		

		@Test
		void testPrefixExp3() throws Exception {
			String input = "(Abc) a";
			Exp e = parseAndShow(input);
			assertEquals(ExpName.class, e.getClass());
			assertEquals("Abc", ((ExpName) e).name);
		}
		
		//Unary
		@Test
		void testUnary2() throws Exception {
			String input = "-2";
			Exp e = parseAndShow(input);
			Exp expected = Expressions.makeExpUnary(OP_MINUS, 2);
			show("expected="+expected);
			assertEquals(expected,e);
		}
		
		//Unary
		@Test
		void testUnary3() throws Exception {
			String input = "not 2";
			Exp e = parseAndShow(input);
			Exp expected = Expressions.makeExpUnary(KW_not, 2);
			show("expected="+expected);
			assertEquals(expected,e);
		}
		
		//Unary
		@Test
		void testUnary4() throws Exception {
			String input = "-2 a";
			Exp e = parseAndShow(input);
			Exp expected = Expressions.makeExpUnary(OP_MINUS, 2);
			show("expected="+expected);
			assertEquals(expected,e);
		}
		
		//Unary
		@Test
		void testUnary5() throws Exception {
			String input = "#2";
			Exp e = parseAndShow(input);
			Exp expected = Expressions.makeExpUnary(OP_HASH, 2);
			show("expected="+expected);
			assertEquals(expected,e);
		}
		
		//Unary
		@Test
		void testUnary6() throws Exception {
			String input = "~2";
			Exp e = parseAndShow(input);
			Exp expected = Expressions.makeExpUnary(BIT_XOR, 2);
			show("expected="+expected);
			assertEquals(expected,e);
		}
		
		//Binary Operators
		@Test
		void testBOP1() throws Exception {
			String input = "2+3+4";
			Exp e = parseAndShow(input);
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
			Exp e = parseAndShow(input);
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
			Exp e = parseAndShow(input);
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
			Exp e = parseAndShow(input);
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
			Exp e = parseAndShow(input);
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
			Exp e = parseAndShow(input);
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
			Exp e = parseAndShow(input);
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
			Exp e = parseAndShow(input);
			
			
		}
		
		//comments
		@Test
		void testBOP9() throws Exception {
			String input = "3--";
			Exp e = parseAndShow(input);
			
			
		}
		
		//Should fail
		@Test
		void testBOP10() throws Exception {
			String input = "3**";
			Exp e = parseAndShow(input);
			
		}
		
		@Test
		void testBOP11() throws Exception {
			String input = "3//";
			Exp e = parseAndShow(input);
			
			
		}
		
		
		@Test
		void testBOP12() throws Exception {
			String input = "3%";
			Exp e = parseAndShow(input);
			
		}
		
		
		@Test
		void testBOP13() throws Exception {
			String input = "-2^3";
			Exp e = parseAndShow(input);
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
			Exp e = parseAndShow(input);
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
			Exp e = parseAndShow(input);
			
			
		}
		
		//Fail case
		@Test
		void testBOP16() throws Exception {
			String input = "3^^3";
			Exp e = parseAndShow(input);
			
			
		}
		
		//Fail case
		@Test
		void testBOP17() throws Exception {
			String input = "3++2";
			Exp e = parseAndShow(input);
			
			
		}
		
		//Fail case
		@Test
		void testBOP18() throws Exception {
			String input = "+3";
			Exp e = parseAndShow(input);
		
			
		}
		
		
		@Test
		void testBOP19() throws Exception {
			String input = "3..4";
			Exp e = parseAndShow(input);
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
			Exp e = parseAndShow(input);
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
			Exp e = parseAndShow(input);
			
		}
		
		
		

		@Test
		void testBOP22() throws Exception {
			String input = "3<<4";
			Exp e = parseAndShow(input);
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
			Exp e = parseAndShow(input);
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
			Exp e = parseAndShow(input);
			
		}
		
		
		
		
		@Test
		void testBOP25() throws Exception {
			String input = "3&4";
			Exp e = parseAndShow(input);
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
			Exp e = parseAndShow(input);
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
			Exp e = parseAndShow(input);
			
		}
		
		
		@Test
		void testBOP28() throws Exception {
			String input = "5^7^(-4 or -6)";
			Exp e = parseAndShow(input);
			
			
		}
		
		//Pass case
		@Test
		void testBOP29() throws Exception {
			String input = "3 not 4 (";
			Exp e = parseAndShow(input);
	
			
		}
		
		@Test
		void testBOP30() throws Exception {
			String input = "Name or (Name)";
			Exp e = parseAndShow(input);
			
			
		}
		
		@Test
		void testBOP31() throws Exception {
			String input = "- \"string\"";
			Exp e = parseAndShow(input);
			
			
		}
		
		//fail case
		@Test
		void testFUNC1() throws Exception {
			String input = "function ";
			Exp e = parseAndShow(input);
			
			
		}
		
		//pass case
		@Test
		void testFUNC2() throws Exception {
			String input = "function () end";
			Exp e = parseAndShow(input);
			
			
		}
		
		//fail case
		@Test
		void testFUNC3() throws Exception {
			String input = "function end";
			Exp e = parseAndShow(input);
			
			
		}
		
		//fail case
		@Test
		void testFUNC4() throws Exception {
			String input = "function () ";
			Exp e = parseAndShow(input);
			
			
		}
		
		//Pass case
		@Test
		void testFUNC5() throws Exception {
			String input = "function () end + 3";
			Exp e = parseAndShow(input);
			
			
		}
		
		//fail case
		@Test
		void testFUNC6() throws Exception {
			String input = "function () 123 end";
			Exp e = parseAndShow(input);
			
			
		}
		
		//Fail case
		@Test
		void testFUNC7() throws Exception {
			String input = "function (()) end";
			Exp e = parseAndShow(input);
			
			
		}
		
		//Pass case
		@Test
		void testFUNC8() throws Exception {
			String input = "function (...) end";
			Exp e = parseAndShow(input);
			
			
		}
		
		//Fail Case
		@Test
		void testFUNC9() throws Exception {
			String input = "function (... ... ) end";
			Exp e = parseAndShow(input);
			
			
		}
		
		//Pass case
		@Test
		void testFUNC10() throws Exception {
			String input = "function (Abc) end";
			Exp e = parseAndShow(input);
			
			
		}
		
		//Pass case
		@Test
		void testFUNC11() throws Exception {
			String input = "function (Abc ...) end";
			Exp e = parseAndShow(input);
			
			
		}
		
		//Pass case
		@Test
		void testFUNC12() throws Exception {
			String input = "function (Abc,...,abc) end";
			Exp e = parseAndShow(input);
			
			
		}
		
		//Fail case
		@Test
		void testFUNC13() throws Exception {
			String input = "function (abc,abc ,...,...) end";
			Exp e = parseAndShow(input);
		}
		
		//Pass case
		@Test
		void testFUNC14() throws Exception {
			String input = "function (abc,xyz) end";
			Exp e = parseAndShow(input);
		}
		
		//fail case
		@Test
		void testFUNC15() throws Exception {
			String input = "function (abc,pool,) end";
			Exp e = parseAndShow(input);
		}
		
		//Pass case
		@Test
		void testFUNC16() throws Exception {
			String input = "function (abc,ppol,...) end";
			Exp e = parseAndShow(input);
		}
		
		//fail case
		@Test
		void testFUNC17() throws Exception {
			String input = "function (abc,pool,ab+) end";
			Exp e = parseAndShow(input);
		}
		
		@Test
		void testFUNC18() throws Exception {
			String input = "function () end (";
			Exp e = parseAndShow(input);
		}
		
		//pass case
		@Test
		void testFUNC19() throws Exception {
			String input = "function (...,a) end";
			Exp e = parseAndShow(input);
		}
		
		//fail case
		@Test
		void testFUNC20() throws Exception {
			String input = "function ) end";
			Exp e = parseAndShow(input);
		}
		
		@Test
		void testFUNC21() throws Exception {
			String input = "function (abc,pool,ab+) end";
			Exp e = parseAndShow(input);
		}
		
		//Pass case
		@Test
		void testFIELD1() throws Exception {
			String input = "{}";
			Exp e = parseAndShow(input);
		}
		
		@Test
		void testFIELD2() throws Exception {
			String input = "{3+4}";
			Exp e = parseAndShow(input);
		}
		
		//Fail case
		@Test
		void testFIELD3() throws Exception {
			String input = "{3+4 3+4}";
			Exp e = parseAndShow(input);
		}
		
		//fail case
		@Test
		void testFIELD4() throws Exception {
			String input = "{[3+4}";
			Exp e = parseAndShow(input);
		}
		
		//pass case
		@Test
		void testFIELD5() throws Exception {
			String input = "{[3^4]=abc}";
			Exp e = parseAndShow(input);
		}
		
		//fail case
		@Test
		void testFIELD6() throws Exception {
			String input = "{[]=abc}";
			Exp e = parseAndShow(input);
		}
		
		//Fail case
		@Test
		void testFIELD7() throws Exception {
			String input = "{[3+] = abc}";
			Exp e = parseAndShow(input);
		}
		
		@Test
		void testFIELD8() throws Exception {
			String input = "{[abc] abc}";
			Exp e = parseAndShow(input);
		}
		
		//fail case
		@Test
		void testFIELD9() throws Exception {
			String input = "{[abc + bvc] = \"string\" )}";
			Exp e = parseAndShow(input);
		}
		
		@Test
		void testFIELD10() throws Exception {
			String input = "{[ds] = (abc)} (";
			Exp e = parseAndShow(input);
		}
		
		@Test
		void testFIELD11() throws Exception {
			String input = "{abc  = abc}";
			Exp e = parseAndShow(input);
		}
		
		//fail case
		@Test
		void testFIELD12() throws Exception {
			String input = "{3+4 = abc}";
			Exp e = parseAndShow(input);
		}
		
		//fail case
		@Test
		void testFIELD13() throws Exception {
			String input = "{abc = 3+4+}";
			Exp e = parseAndShow(input);
		}
		
		@Test
		void testFIELD14() throws Exception {
			String input = "{abc = -4^5}";
			Exp e = parseAndShow(input);
		}
		
		@Test
		void testFIELD15() throws Exception {
			String input = "{abc = -4^5},{abc = -4^5}";
			Exp e = parseAndShow(input);
		}
		
		@Test
		void testFIELD16() throws Exception {
			String input = "{abc = -4^5};{abc = -4^5};{abc = -4^5}";
			Exp e = parseAndShow(input);
		}
		
		@Test
		void testFIELD17() throws Exception {
			String input = "{abc = -4^5};lmn";
			Exp e = parseAndShow(input);
		}
		
		@Test
		void testFIELD18() throws Exception {
			String input = "{abc = -4^5;lmn}";
			Exp e = parseAndShow(input);
		}
		
		@Test
		void testFIELD19() throws Exception {
			String input = "{abc = -4^5;2;}";
			Exp e = parseAndShow(input);
		}
		
		@Test
		void testFIELD20() throws Exception {
			String input = "{abc = -4^5}";
			Exp e = parseAndShow(input);
		}
		
		@Test
		void testFIELD21() throws Exception {
			String input = "{abc = -4^5}";
			Exp e = parseAndShow(input);
		}
		
		@Test
		void testFIELD22() throws Exception {
			String input = "function() end + {abc = -4^5}";
			Exp e = parseAndShow(input);
		}
		
		@Test
		void testEMPTY() throws Exception {
			String input = " ";
			Exp e = parseAndShow(input);
		}
		
		
}

