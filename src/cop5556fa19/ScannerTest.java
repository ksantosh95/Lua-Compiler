/* *
 * Developed  for the class project in COP5556 Programming Language Principles 
 * at the University of Florida, Fall 2019.
 * 
 * This software is solely for the educational benefit of students 
 * enrolled in the course during the Fall 2019 semester.  
 * 
 * This software, and any software derived from it,  may not be shared with others or posted to public web sites or repositories,
 * either during the course or afterwards.
 * 
 *  @Beverly A. Sanders, 2019
 */

package cop5556fa19;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import cop5556fa19.Scanner.LexicalException;

import static cop5556fa19.Token.Kind.*;

class ScannerTest {
	
	//I like this to make it easy to print objects and turn this output on and off
	static boolean doPrint = true;
	private void show(Object input) {
		if (doPrint) {
			System.out.println(input.toString());
		}
	}
	
	

	 /**
	  * Example showing how to get input from a Java string literal.
	  * 
	  * In this case, the string is empty.  The only Token that should be returned is an EOF Token.  
	  * 
	  * This test case passes with the provided skeleton, and should also pass in your final implementation.
	  * Note that calling getNext again after having reached the end of the input should just return another EOF Token.
	  * 
	  */
	@Test 
	void test0() throws Exception {
		Reader r = new StringReader("");
		Scanner s = new Scanner(r);
		Token t;
		show(t= s.getNext()); 
		assertEquals(EOF, t.kind);
		show(t= s.getNext());
		assertEquals(EOF, t.kind);
	}

	/**
	 * Example showing how to create a test case to ensure that an exception is thrown when illegal input is given.
	 * 
	 * This "@" character is illegal in the final scanner (except as part of a String literal or comment). So this
	 * test should remain valid in your complete Scanner.
	 */
	@Test
	void test1() throws Exception {
		Reader r = new StringReader("@");
		Scanner s = new Scanner(r);
        assertThrows(LexicalException.class, ()->{
		   s.getNext();
        });
	}
	
	/**
	 * Example showing how to read the input from a file.  Otherwise it is the same as test1.
	 *
	 */
	@Test
	void test2() throws Exception {
		String file = "C:\\Users\\Santosh Kannan\\Desktop\\UF\\Programming Language Principles\\Assigment 1\\testInputFiles\\test2.input"; 
		Reader r = new BufferedReader(new FileReader(file));
		Scanner s = new Scanner(r);
        assertThrows(LexicalException.class, ()->{
		   s.getNext();
        });
	}
	

	
	/**
	 * Another example.  This test case will fail with the provided code, but should pass in your completed Scanner.
	 * @throws Exception
	 */
	@Test
	void test3() throws Exception {
		Reader r = new StringReader(",,::==");
		Scanner s = new Scanner(r);
		Token t;
		show(t= s.getNext());
		assertEquals(t.kind,COMMA);
		assertEquals(t.text,",");
		show(t = s.getNext());
		assertEquals(t.kind,COMMA);
		assertEquals(t.text,",");
		show(t = s.getNext());
		assertEquals(t.kind,COLONCOLON);
		assertEquals(t.text,"::");
		
		show(t = s.getNext());
		assertEquals(t.kind,REL_EQEQ);
		assertEquals(t.text,"==");
	}

	/*Testing whether each token is scanned correctly*/
	@Test
	void test4() throws Exception {
		Reader r = new StringReader(" +    -     *     /     %     ^     #\r\n" + 
				"      &     ~     |     <<    >>    //\r\n" + 
				"     ==    ~=   <=    >=    <     >     =\r\n" + 
				"     (     )     {     }     [     ]     ::\r\n" + 
				"     ;      :     ,     .     ..    ...\r\n" + 
				"");
		Scanner s = new Scanner(r);
		Token t;
		show(t= s.getNext());
		assertEquals(t.kind,OP_PLUS);
		assertEquals(t.text,"+");
		show(t = s.getNext());
		assertEquals(t.kind,OP_MINUS);
		assertEquals(t.text,"-");
		show(t = s.getNext());
		assertEquals(t.kind,OP_TIMES);
		assertEquals(t.text,"*");
		show(t = s.getNext());
		assertEquals(t.kind,OP_DIV);
		assertEquals(t.text,"/");
		show(t = s.getNext());
		assertEquals(t.kind,OP_MOD);
		assertEquals(t.text,"%");
		show(t = s.getNext());
		assertEquals(t.kind,OP_POW);
		assertEquals(t.text,"^");
		show(t = s.getNext());
		assertEquals(t.kind,OP_HASH);
		assertEquals(t.text,"#");
		show(t = s.getNext());	
		assertEquals(t.kind,BIT_AMP);
		assertEquals(t.text,"&");
		show(t = s.getNext());
		assertEquals(t.kind,BIT_XOR);
		assertEquals(t.text,"~");
		show(t = s.getNext());
		assertEquals(t.kind,BIT_OR);
		assertEquals(t.text,"|");
		show(t = s.getNext());
		assertEquals(t.kind,BIT_SHIFTL);
		assertEquals(t.text,"<<");
		show(t = s.getNext());
		assertEquals(t.kind,BIT_SHIFTR);
		assertEquals(t.text,">>");
		show(t = s.getNext());
		assertEquals(t.kind,OP_DIVDIV);
		assertEquals(t.text,"//");
		show(t = s.getNext());
		assertEquals(t.kind,REL_EQEQ);
		assertEquals(t.text,"==");
		show(t = s.getNext());
		assertEquals(t.kind,REL_NOTEQ);
		assertEquals(t.text,"~=");
		show(t = s.getNext());
		assertEquals(t.kind,REL_LE);
		assertEquals(t.text,"<=");
		show(t = s.getNext());
		assertEquals(t.kind,REL_GE);
		assertEquals(t.text,">=");
		show(t = s.getNext());
		assertEquals(t.kind,REL_LT);
		assertEquals(t.text,"<");
		show(t = s.getNext());
		assertEquals(t.kind,REL_GT);
		assertEquals(t.text,">");
		show(t = s.getNext());
		assertEquals(t.kind,ASSIGN);
		assertEquals(t.text,"=");
		show(t = s.getNext());
		assertEquals(t.kind,LPAREN);
		assertEquals(t.text,"(");
		show(t = s.getNext());
		assertEquals(t.kind,RPAREN);
		assertEquals(t.text,")");
		show(t = s.getNext());
		assertEquals(t.kind,LCURLY);
		assertEquals(t.text,"{");
		show(t = s.getNext());
		assertEquals(t.kind,RCURLY);
		assertEquals(t.text,"}");
		show(t = s.getNext());
		assertEquals(t.kind,LSQUARE);
		assertEquals(t.text,"[");
		show(t = s.getNext());
		assertEquals(t.kind,RSQUARE);
		assertEquals(t.text,"]");
		show(t = s.getNext());
		assertEquals(t.kind,COLONCOLON);
		assertEquals(t.text,"::");
		show(t = s.getNext());
		assertEquals(t.kind,SEMI);
		assertEquals(t.text,";");
		show(t = s.getNext());
		assertEquals(t.kind,COLON);
		assertEquals(t.text,":");
		show(t = s.getNext());
		assertEquals(t.kind,COMMA);
		assertEquals(t.text,",");
		show(t = s.getNext());
		assertEquals(t.kind,DOT);
		assertEquals(t.text,".");
		show(t = s.getNext());
		assertEquals(t.kind,DOTDOT);
		assertEquals(t.text,"..");
		show(t = s.getNext());
		assertEquals(t.kind,DOTDOTDOT);
		assertEquals(t.text,"...");
		show(t = s.getNext());
		assertEquals(t.kind,EOF);

		
	}

	

	/*Testing code with repeated tokens*/
	@Test
	void test5() throws Exception {
		Reader r = new StringReader(">=>=;,,+-+");
		Scanner s = new Scanner(r);
		Token t;
		show(t= s.getNext());
		assertEquals(t.kind,REL_GE);
		assertEquals(t.text,">=");
		show(t = s.getNext());
		assertEquals(t.kind,REL_GE);
		assertEquals(t.text,">=");
		show(t = s.getNext());
		assertEquals(t.kind,SEMI);
		assertEquals(t.text,";");
		show(t = s.getNext());
		assertEquals(t.kind,COMMA);
		assertEquals(t.text,",");
		show(t = s.getNext());
		assertEquals(t.kind,COMMA);
		assertEquals(t.text,",");
		show(t = s.getNext());
		assertEquals(t.kind,OP_PLUS);
		assertEquals(t.text,"+");
		show(t = s.getNext());
		assertEquals(t.kind,OP_MINUS);
		assertEquals(t.text,"-");
		show(t = s.getNext());	
		assertEquals(t.kind,OP_PLUS);
		assertEquals(t.text,"+");
		show(t = s.getNext());
		assertEquals(t.kind,EOF);


	}
	
	/*Testing code for duplicates*/
	@Test
	void test6() throws Exception {
		Reader r = new StringReader(">>>///===<<<>=<=<=>>>>////====<<<<");
		Scanner s = new Scanner(r);
		Token t;
		show(t= s.getNext());
		assertEquals(t.kind,BIT_SHIFTR);
		assertEquals(t.text,">>");
		show(t = s.getNext());
		assertEquals(t.kind,REL_GT);
		assertEquals(t.text,">");
		show(t = s.getNext());
		assertEquals(t.kind,OP_DIVDIV);
		assertEquals(t.text,"//");
		show(t = s.getNext());
		assertEquals(t.kind,OP_DIV);
		assertEquals(t.text,"/");
		show(t = s.getNext());
		assertEquals(t.kind,REL_EQEQ);
		assertEquals(t.text,"==");
		show(t = s.getNext());
		assertEquals(t.kind,ASSIGN);
		assertEquals(t.text,"=");
		show(t = s.getNext());
		assertEquals(t.kind,BIT_SHIFTL);
		assertEquals(t.text,"<<");
		show(t = s.getNext());	
		assertEquals(t.kind,REL_LT);
		assertEquals(t.text,"<");
		show(t = s.getNext());	
		assertEquals(t.kind,REL_GE);
		assertEquals(t.text,">=");
		show(t = s.getNext());	
		assertEquals(t.kind,REL_LE);
		assertEquals(t.text,"<=");
		show(t = s.getNext());	
		assertEquals(t.kind,REL_LE);
		assertEquals(t.text,"<=");
		show(t = s.getNext());
		assertEquals(t.kind,BIT_SHIFTR);
		assertEquals(t.text,">>");
		show(t = s.getNext());
		assertEquals(t.kind,BIT_SHIFTR);
		assertEquals(t.text,">>");
		show(t = s.getNext());
		assertEquals(t.kind,OP_DIVDIV);
		assertEquals(t.text,"//");
		show(t = s.getNext());
		assertEquals(t.kind,OP_DIVDIV);
		assertEquals(t.text,"//");
		show(t = s.getNext());
		assertEquals(t.kind,REL_EQEQ);
		assertEquals(t.text,"==");
		show(t = s.getNext());
		assertEquals(t.kind,REL_EQEQ);
		assertEquals(t.text,"==");
		show(t = s.getNext());
		assertEquals(t.kind,BIT_SHIFTL);
		assertEquals(t.text,"<<");
		show(t = s.getNext());
		assertEquals(t.kind,BIT_SHIFTL);
		assertEquals(t.text,"<<");
		show(t = s.getNext());
		assertEquals(t.kind,EOF);


	}
	
	
	
	/*Testing code for integer literals starting with 0*/
	@Test
	void test7() throws Exception {
		Reader r = new StringReader("-012+032.012");
		Scanner s = new Scanner(r);
		Token t;
		show(t= s.getNext());
		assertEquals(t.kind,OP_MINUS);
		assertEquals(t.text,"-");
		show(t= s.getNext());
		assertEquals(t.kind,INTLIT);
		assertEquals(t.text,"0");
		show(t= s.getNext());
		assertEquals(t.kind,INTLIT);
		assertEquals(t.text,"12");
		show(t= s.getNext());
		assertEquals(t.kind,OP_PLUS);
		assertEquals(t.text,"+");
		show(t= s.getNext());
		assertEquals(t.kind,INTLIT);
		assertEquals(t.text,"0");
		show(t= s.getNext());
		assertEquals(t.kind,INTLIT);
		assertEquals(t.text,"32");
		show(t= s.getNext());
		assertEquals(t.kind,DOT);
		assertEquals(t.text,".");
		show(t= s.getNext());
		assertEquals(t.kind,INTLIT);
		assertEquals(t.text,"0");
		show(t= s.getNext());
		assertEquals(t.kind,INTLIT);
		assertEquals(t.text,"12");
		show(t= s.getNext());
		assertEquals(t.kind,EOF);
	}
	
	
	/*Testing code for integer literals starting with NON ZERO DIGIT*/
	@Test
	void test8() throws Exception {
		Reader r = new StringReader("-2012+032.3012");
		Scanner s = new Scanner(r);
		Token t;
		show(t= s.getNext());
		assertEquals(t.kind,OP_MINUS);
		assertEquals(t.text,"-");
		show(t= s.getNext());
		assertEquals(t.kind,INTLIT);
		assertEquals(t.text,"2012");
		show(t= s.getNext());
		assertEquals(t.kind,OP_PLUS);
		assertEquals(t.text,"+");
		show(t= s.getNext());
		assertEquals(t.kind,INTLIT);
		assertEquals(t.text,"0");
		show(t= s.getNext());
		assertEquals(t.kind,INTLIT);
		assertEquals(t.text,"32");
		show(t= s.getNext());
		assertEquals(t.kind,DOT);
		assertEquals(t.text,".");
		show(t= s.getNext());
		assertEquals(t.kind,INTLIT);
		assertEquals(t.text,"3012");
		show(t= s.getNext());
		assertEquals(t.kind,EOF);
	}
	
	
	/*Testing code for names starting with capital as well as small letters*/
	@Test
	void test9() throws Exception {
		Reader r = new StringReader("Abc-xyz");
		Scanner s = new Scanner(r);
		Token t;
		show(t= s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text,"Abc");
		show(t= s.getNext());
		assertEquals(t.kind,OP_MINUS);
		assertEquals(t.text,"-");
		show(t= s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text,"xyz");
		show(t= s.getNext());
		assertEquals(t.kind,EOF);
	}
	
	/*Testing code for names starting with capital as well as small letters and containing digits*/
	@Test
	void test10() throws Exception {
		Reader r = new StringReader("Abc086-xyz92345");
		Scanner s = new Scanner(r);
		Token t;
		show(t= s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text,"Abc086");
		show(t= s.getNext());
		assertEquals(t.kind,OP_MINUS);
		assertEquals(t.text,"-");
		show(t= s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text,"xyz92345");
		show(t= s.getNext());
		assertEquals(t.kind,EOF);
	}
	
	
	/*Testing code for names starting with capital as well as small letters and containing digits and with _ and 
	 $ symbols*/
	@Test
	void test11() throws Exception {
		Reader r = new StringReader("Abc_08__6-x$yz92345$$");
		Scanner s = new Scanner(r);
		Token t;
		show(t= s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text,"Abc_08__6");
		show(t= s.getNext());
		assertEquals(t.kind,OP_MINUS);
		assertEquals(t.text,"-");
		show(t= s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text,"x$yz92345$$");
		show(t= s.getNext());
		assertEquals(t.kind,EOF);
	}
	
	
	/*Testing code for names starting with  _ and $ symbols. Should fail*/
	@Test
	void test12() throws Exception {
		Reader r = new StringReader("xu-+$Abc_08_");
		Scanner s = new Scanner(r);
		Token t;
		show(t= s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text,"xu");
		show(t= s.getNext());
		assertEquals(t.kind,OP_MINUS);
		assertEquals(t.text,"-");
		show(t= s.getNext());
		assertEquals(t.kind,OP_PLUS);
		assertEquals(t.text,"+");
		show(t= s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text,"_Abc_08_");
		show(t= s.getNext());
		assertEquals(t.kind,EOF);
	}
	
	/*Testing code for whitespaces. Code should ignore the white spaces and return the next character.
	 * Tokens separated by whitespaces should be considered as separate tokens*/
	@Test
	void test13() throws Exception {
		Reader r = new StringReader("  xu - + Ab	c_08_");
		Scanner s = new Scanner(r);
		Token t;
		show(t= s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text,"xu");
		show(t= s.getNext());
		assertEquals(t.kind,OP_MINUS);
		assertEquals(t.text,"-");
		show(t= s.getNext());
		assertEquals(t.kind,OP_PLUS);
		assertEquals(t.text,"+");
		show(t= s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text,"Ab");
		show(t= s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text,"c_08_");
		show(t= s.getNext());
		assertEquals(t.kind,EOF);
	}
	

	/*Testing code for comments */
	@Test
	void test14() throws Exception {
		Reader r = new StringReader("if(a=b) --this is a comment; Not to be included");
		Scanner s = new Scanner(r);
		Token t;
		show(t= s.getNext());
		assertEquals(t.kind,KW_if);
		assertEquals(t.text,"if");
		show(t= s.getNext());
		assertEquals(t.kind,LPAREN);
		assertEquals(t.text,"(");
		show(t= s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text,"a");
		show(t= s.getNext());
		assertEquals(t.kind,ASSIGN);
		assertEquals(t.text,"=");
		show(t= s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text,"b");
		show(t= s.getNext());
		assertEquals(t.kind,RPAREN);
		assertEquals(t.text,")");
		show(t= s.getNext());
		assertEquals(t.kind,EOF);
	}
	
	/*Testing code for single string literal delimited by double quotes */
	@Test
	void test15() throws Exception {
		Reader r = new StringReader("\"This is a string literal\"");
		Scanner s = new Scanner(r);
		Token t;
		show(t= s.getNext());
		assertEquals(t.kind,STRINGLIT);
		assertEquals(t.text,"\"This is a string literal\"");
		show(t.getStringVal());
		show(t= s.getNext());
		assertEquals(t.kind,EOF);

		
	}
	
	
	/*Testing code for single string literal with numeric values delimited by double quotes*/
	@Test
	void test16() throws Exception {
		Reader r = new StringReader("\"234\"");
		Scanner s = new Scanner(r);
		Token t;
		show(t= s.getNext());
		assertEquals(t.kind,STRINGLIT);
		assertEquals(t.text,"\"234\"");
		show(t.getStringVal());
		show(t= s.getNext());
		assertEquals(t.kind,EOF);

		
	}
	
	
	/*Testing code for single string literal with special characters delimited by double quotes*/
	@Test
	void test17() throws Exception {
		Reader r = new StringReader("\"!@#$%^&*()\"");
		Scanner s = new Scanner(r);
		Token t;
		show(t= s.getNext());
		assertEquals(t.kind,STRINGLIT);
		assertEquals(t.text,"\"!@#$%^&*()\"");
		show(t.getStringVal());
		show(t= s.getNext());
		assertEquals(t.kind,EOF);

		
	}
	
	
	/*Testing code for single string literal delimited by double quotes */
	@Test
	void test18() throws Exception {
		Reader r = new StringReader("\'This is a string literal\'");
		Scanner s = new Scanner(r);
		Token t;
		show(t= s.getNext());
		assertEquals(t.kind,STRINGLIT);
		assertEquals(t.text,"\'This is a string literal\'");
		show(t.getStringVal());
		show(t= s.getNext());
		assertEquals(t.kind,EOF);

		
	}
	
	
	/*Testing code for single string literal with numeric values delimited by double quotes*/
	@Test
	void test19() throws Exception {
		Reader r = new StringReader("\'234\'");
		Scanner s = new Scanner(r);
		Token t;
		show(t= s.getNext());
		assertEquals(t.kind,STRINGLIT);
		assertEquals(t.text,"\'234\'");
		show(t.getStringVal());
		show(t= s.getNext());
		assertEquals(t.kind,EOF);

		
	}
	
	
	/*Testing code for single string literal with special characters delimited by double quotes*/
	@Test
	void test20() throws Exception {
		Reader r = new StringReader("\'!@#$%^&*()\'");
		Scanner s = new Scanner(r);
		Token t;
		show(t= s.getNext());
		assertEquals(t.kind,STRINGLIT);
		assertEquals(t.text,"\'!@#$%^&*()\'");
		show(t.getStringVal());
		show(t= s.getNext());
		assertEquals(t.kind,EOF);

		
	}
	
	
	/*Testing code for keywords*/
	@Test
	void test21() throws Exception {
		Reader r = new StringReader("  and       break      do        else       elseif     end \r\n" + 
				"      false      for        function   goto       if         in\r\n" + 
				"      local      nil        not        or         repeat     return\r\n" + 
				"      then      true       until      while");
		Scanner s = new Scanner(r);
		Token t;
		show(t= s.getNext());
		assertEquals(t.kind,KW_and);
		assertEquals(t.text,"and");
		show(t= s.getNext());
		assertEquals(t.kind,KW_break);
		assertEquals(t.text,"break");
		show(t= s.getNext());
		assertEquals(t.kind,KW_do);
		assertEquals(t.text,"do");
		show(t= s.getNext());
		assertEquals(t.kind,KW_else);
		assertEquals(t.text,"else");
		show(t= s.getNext());
		assertEquals(t.kind,KW_elseif);
		assertEquals(t.text,"elseif");
		show(t= s.getNext());
		assertEquals(t.kind,KW_end);
		assertEquals(t.text,"end");
		show(t= s.getNext());
		assertEquals(t.kind,KW_false);
		assertEquals(t.text,"false");
		show(t= s.getNext());
		assertEquals(t.kind,KW_for);
		assertEquals(t.text,"for");
		show(t= s.getNext());
		assertEquals(t.kind,KW_function);
		assertEquals(t.text,"function");
		show(t= s.getNext());
		assertEquals(t.kind,KW_goto);
		assertEquals(t.text,"goto");
		show(t= s.getNext());
		assertEquals(t.kind,KW_if);
		assertEquals(t.text,"if");
		show(t= s.getNext());
		assertEquals(t.kind,KW_in);
		assertEquals(t.text,"in");
		show(t= s.getNext());
		assertEquals(t.kind,KW_local);
		assertEquals(t.text,"local");
		show(t= s.getNext());
		assertEquals(t.kind,KW_nil);
		assertEquals(t.text,"nil");
		show(t= s.getNext());
		assertEquals(t.kind,KW_not);
		assertEquals(t.text,"not");
		show(t= s.getNext());
		assertEquals(t.kind,KW_or);
		assertEquals(t.text,"or");
		show(t= s.getNext());
		assertEquals(t.kind,KW_repeat);
		assertEquals(t.text,"repeat");
		show(t= s.getNext());
		assertEquals(t.kind,KW_return);
		assertEquals(t.text,"return");
		show(t= s.getNext());
		assertEquals(t.kind,KW_then);
		assertEquals(t.text,"then");
		show(t= s.getNext());
		assertEquals(t.kind,KW_true);
		assertEquals(t.text,"true");
		show(t= s.getNext());
		assertEquals(t.kind,KW_until);
		assertEquals(t.text,"until");
		show(t= s.getNext());
		assertEquals(t.kind,KW_while);
		assertEquals(t.text,"while");
		show(t= s.getNext());
		assertEquals(t.kind,EOF);

	}	
	
	/*Testing code for keywords in names, comments and string literals */
	@Test
	void test22() throws Exception {
		Reader r = new StringReader("and -- this is a keyword \n ifand --this is not \n \"and\" -- so is this");
		Scanner s = new Scanner(r);
		Token t;
		show(t= s.getNext());
		assertEquals(t.kind,KW_and);
		assertEquals(t.text,"and");
		show(t= s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text,"ifand");
		show(t= s.getNext());
		assertEquals(t.kind,STRINGLIT);
		assertEquals(t.text,"\"and\"");
		
		show(t= s.getNext());
		assertEquals(t.kind,EOF);

		
	}
	
	/*Inserting apostrophes in string literal - should fail*/
	@Test
	void test23() throws Exception {
		Reader r = new StringReader("\"\'\"");
		Scanner s = new Scanner(r);
		Token t;
		show(t= s.getNext());
		
		
		show(t= s.getNext());
		assertEquals(t.kind,EOF);

		
	}
	
	/*Inserting quotes  in string literal - should fail*/
	@Test
	void test24() throws Exception {
		Reader r = new StringReader("\'\"\'");
		Scanner s = new Scanner(r);
		Token t;
		show(t= s.getNext());
		
		
		show(t= s.getNext());
		assertEquals(t.kind,EOF);

		
	}
	
	/*Inserting backslash  in string literal - should fail*/
	@Test
	void test25() throws Exception {
		Reader r = new StringReader("\"abc\\def\"");
		Scanner s = new Scanner(r);
		Token t;
		show(t= s.getNext());
		show(t= s.getNext());
		
		show(t= s.getNext());
		assertEquals(t.kind,EOF);

		
	}
	
	/* Testing escape sequences from a file */
	@Test
	void test26() throws Exception {
		String file = "C:\\Users\\Santosh Kannan\\eclipse-workspace\\Assignment_1\\src\\testInputFiles\\test2.input"; 
		Reader r = new BufferedReader(new FileReader(file));
		Scanner s = new Scanner(r);
		Token t;
		show(t = s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text,"x");
		show(t = s.getNext());
		assertEquals(t.kind,ASSIGN);
		assertEquals(t.text,"=");
		
		
		show(t = s.getNext());
		show(t = s.getNext());
		show(t = s.getNext());
	//	assertEquals(t.kind,STRINGLIT);
		//assertEquals(t.getStringVal(),"\"");
		
		show(t = s.getNext());
		assertEquals(t.kind,EOF);
	}
	
	/*Testing code for invalid characters in name. Should fail*/
	@Test
	void test27() throws Exception {
		Reader r = new StringReader("21474836847");
		Scanner s = new Scanner(r);
		Token t;
		show(t= s.getNext());
		
		show(t= s.getNext());
		assertEquals(t.kind,EOF);

		
	}
	
	
	@Test
	void test28() throws Exception {
		String file = "testInputFiles\\test3"; 
		Reader r = new BufferedReader(new FileReader(file));
		Scanner s = new Scanner(r);
		Token t;
		do
		{
			show(t= s.getNext());
		}
		while(t.kind!=EOF);
		
	
		/*
		show(t = s.getNext());
		assertEquals(t.kind,COLONCOLON);
		assertEquals(t.text,"::");
		
		show(t = s.getNext());
		assertEquals(t.kind,REL_EQEQ);
		assertEquals(t.text,"==");*/
	}
	
	
	/*Inserting quotes  in string literal - should fail*/
	@Test
	void test29() throws Exception {
		Reader r = new StringReader("\"\\aapple\\bbag \\ffood\\ttable\"");
		Scanner s = new Scanner(r);
		Token t;
		do
		{
			show(t= s.getNext());
		}
		while(t.kind!=EOF);
		
	}
	
	/*Inserting quotes  in string literal - should fail*/
	@Test
	void test30() throws Exception {
		Reader r = new StringReader("--cc");
		Scanner s = new Scanner(r);
		Token t;
		do
		{
			show(t= s.getNext());
		}
		while(t.kind!=EOF);
		
	}
}
