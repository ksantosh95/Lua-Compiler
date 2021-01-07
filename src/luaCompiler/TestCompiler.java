package luaCompiler;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import luaCompiler.Parser.SyntaxException;
import luaCompiler.Scanner.LexicalException;
import interpreter.ASTVisitorAdapter;
import interpreter.Interpreter;
import interpreter.LuaBoolean;
import interpreter.LuaInt;
import interpreter.LuaNil;
import interpreter.LuaString;
import interpreter.LuaTable;
import interpreter.LuaValue;
import interpreter.StaticSemanticException;

public class TestCompiler {

	// To make it easy to print objects and turn this output on and off
			static final boolean doPrint = true;
//			static final boolean doPrint = false;

			private void show(Object input) {
				if (doPrint) {
					System.out.println(input);
				}
			}
			
			/**
			 * scans, parses, and interprets a program representing a Lua chunk.
			 * 
			 * @param input  String containing program source code
			 * @return  a (possbily empty) list of  LuaValue objects.
			 * 
			 * @throws Exception
			 * 
			 *Exceptions may be thrown for various static or dynamic errors
			 */
			
			List<LuaValue> interpret(String input) throws Exception{
				ASTVisitorAdapter lua = new Interpreter();
				Reader r = new StringReader(input);
				List<LuaValue> ret = (List<LuaValue>) lua.load(r);	
				return ret;
			}
			
			/**
			 * Utility function for tests. The interpret function may return a List<LuaValue>
			 * whose contents may be compared with expected using assertions.  This function 
			 * helps construct the "expected" object.
			 * 
			 * @param v  variable length list of ints
			 * @return   List<LuaValue> with value corresponding to input params
			 * 
			 */
			List<LuaValue> makeExpectedWithInts(int ... v){
				List<LuaValue> l = new ArrayList<>();
				for (int i: v) {
					l.add(new LuaInt(i));
				}
				return l;
			}
			
			
			@Test
			void test1() throws Exception {
				String file = "src\\luaCompiler\\TestFiles\\test"; 
				Reader r = new BufferedReader(new FileReader(file));
				int intValueOfChar;
			    String targetString = "";
			    while ((intValueOfChar = r.read()) != -1) {
			        targetString += (char) intValueOfChar;
			    }					
				List<LuaValue> ret = interpret(targetString);
				show(ret);
			}
}
