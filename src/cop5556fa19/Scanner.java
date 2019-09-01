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


import static cop5556fa19.Token.Kind.*;


import java.io.IOException;
import java.io.Reader;

public class Scanner {
	

	public static void main(String args[])
	{
		System.out.println("Scanned");
	}
	
	Reader r;

	public enum State {
		START,
		HAS_EQ,
		IN_NUMLIT,
		IN_IDENT
	}
	
	@SuppressWarnings("serial")
	public static class LexicalException extends Exception {	
		public LexicalException(String arg0) {
			super(arg0);
		}
	}
	
	public Scanner(Reader r) throws IOException {
		this.r = r;
	}

	int currPos=-1;
	int currLine;
	int ch;

	Token t=null;
	public void getChar() throws IOException {
		//read character
		ch = r.read();
		//System.out.println("Character read="+(char)ch);
		currPos = currPos + 1;
		t=null;
	}

	
	public Token getNext() throws Exception {
		getChar();

		//StringBuilder sb;
		int pos=-1;
		int line=0;
		State state=State.START;
		
		while(t==null)
		{
			switch(state)
			{
			case START :
			{
				pos = currPos;
				switch (ch)
				{
				case ',' : {
					System.out.println("Scanned literal :"+(char)ch);
					t= new Token(COMMA,",",pos,line);
					System.out.println(t);
					getChar();
				}break;
				case -1 : System.out.println("EOF reached");
					return new Token(EOF,"eof",pos,0);
							
				default : System.out.println("failure");
				break;
				}
				break;
			}
						default : System.out.println("second failure");
			}
			
		}
	    pos=pos+1;
		if (ch == -1) { return new Token(EOF,"eof",pos,0);
		}
	
		//replace this code.  Just for illustration
	  return t;
		
		   
			//throw new LexicalException("Useful error message");
		
		}
	}

