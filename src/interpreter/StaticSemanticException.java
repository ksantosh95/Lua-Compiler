package interpreter;

import luaCompiler.Token;

@SuppressWarnings("serial")
public class StaticSemanticException extends Exception {

	
		Token t;
		
		public StaticSemanticException(Token t, String message) {
			super(t.line + ":" + t.pos + " " + message);
		}
	
}
