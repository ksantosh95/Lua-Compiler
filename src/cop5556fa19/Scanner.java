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


    public static void main(String args[]) {
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

    int currPos = -1;
    int currLine;
    int ch;
    char nextc;
    Token t = null;
    public void getChar() throws IOException {
        //read character
        ch = r.read();
        //System.out.println("Character read="+(char)ch);
        currPos = currPos + 1;
       
    }


    public Token getNext() throws Exception {
        getChar();

        //StringBuilder sb;
        int pos = -1;
        int line = 0;
        State state = State.START;

        while (t == null) {
        	//System.out.println((char)ch);
            switch (state) {
                case START:
                    {
                        pos = currPos;
                        switch (ch) {
                        	case '+':
                        		{
                        			t = new Token(OP_PLUS, "+",pos, line);
                        			System.out.println(t);
                        			getChar();
                        		}break;
                        	case '-':
                        		{
                        			t = new Token(OP_MINUS,"-", pos,line);
                        			System.out.println(t);
                        			getChar();
                        		}break;
                        	case '*':
                    		{
                    			t = new Token(OP_TIMES,"*", pos,line);
                    			System.out.println(t);
                    			getChar();
                    		}break;
                        	case '/':
                    		{
                    			getChar();
                    			
                    			if(ch=='/')
                    			{
                    				t = new Token(OP_DIVDIV, "//",pos, line);
                    				System.out.println(t);
                    				getChar();
                    				t=null;
                    	
                    			}
                    			
                    			else
                    			{
                    				t = new Token(OP_DIV,"/",pos,line);
                    				System.out.println(t);
                    				
                    				t=null;
                    			}
                    			
                    		}break;
                        	case '%':
                    		{
                    			t = new Token(OP_MOD, "%",pos, line);
                    			System.out.println(t);
                    			getChar();
                    		}break;
                        	case '^':
                    		{
                    			t = new Token(OP_POW, "^",pos, line);
                    			System.out.println(t);
                    			getChar();
                    		}break;
                        	case '#':
                    		{
                    			t = new Token(OP_HASH, "#",pos, line);
                    			System.out.println(t);
                    			getChar();
                    		}break;
                        	case '&':
                    		{
                    			t = new Token(BIT_AMP, "&",pos, line);
                    			System.out.println(t);
                    			getChar();
                    		}break;
                    		
                        	case '~':
                    		{
                    			{
                        			getChar();
                        			
                        			if(ch=='=')
                        			{
                        				t = new Token(REL_NOTEQ,"~=",pos,line);
                        				System.out.println(t);
                        				getChar();
                        				t=null;
                        	
                        			}
                        			
                        			else
                        			{
                        				t = new Token(BIT_XOR, "~",pos, line);
                        				System.out.println(t);
                        				
                        				t=null;
                        			}
                        			
                        		}  }break;
                    			
                    		
                    			
                    			
                    
                        	case '|':
                    		{
                    			t = new Token(BIT_OR, "|",pos, line);
                    			System.out.println(t);
                    			getChar();
                    		}break;
                        	case '<':
                    		{
                    			getChar();
                    			
                    			if(ch=='<')
                    			{
                    				t = new Token(BIT_SHIFTL, "<<",pos, line);
                    				System.out.println(t);
                    				getChar();
                    				t=null;
                    	
                    			}
                    			else if (ch=='=')
                    			{
                    			t = new Token(REL_LE, "<=",pos, line);
                    			System.out.println(t);
                    			getChar();
                    			t=null;
                    			}
                    			else
                    			{
                    				t = new Token(REL_LT,"<",pos,line);
                    				System.out.println(t);
                    				
                    				t=null;
                    			}
                    			break;
                    			
                    		}
                        	case '>':
                    		{

                    			getChar();
                    			if(ch=='>')
                    			{
                    				t = new Token(BIT_SHIFTR, ">>",pos, line);
                    				System.out.println(t);
                    				getChar();
                    				t=null;
                    				
                    			}
                    			else if (ch=='=')
                    			{
                    			t = new Token(REL_GE, ">=",pos, line);
                    			System.out.println(t);
                    			getChar();
                    			t=null;
                    			
                    			}
                    			else
                    			{
                    				t = new Token(REL_GT,">",pos,line);
                    				System.out.println(t);
                    				t=null;
                    				
                    			}
                    			break;
                    		}
                        	case '=':
                    		{

                    			getChar();
                    			if(ch=='=')
                    			{
                    				t = new Token(REL_EQEQ, "==",pos, line);
                    				System.out.println(t);
                    				getChar();
                    				t=null;
                    				
                    			}
                    			
                    			else
                    			{
                    				t = new Token(ASSIGN,"=",pos,line);
                    				System.out.println(t);
                    				t=null;
                    				
                    			}
                    			break;
                    		}
                        	case '(':
                            {
                                t = new Token(LPAREN, "(", pos, line);
                                System.out.println(t);
                                getChar();
                            }
                            
                            break;
                        	case ')':
                    		{
                    			t = new Token(RPAREN, ")",pos, line);
                    			System.out.println(t);
                    			getChar();
                    		}break;
                        	case '{':
                    		{
                    			t = new Token(LCURLY, "{",pos, line);
                    			System.out.println(t);
                    			getChar();
                    		}break;
                    		
                        	case '}':
                    		{
                    			t = new Token(RCURLY, "}",pos, line);
                    			System.out.println(t);
                    			getChar();
                    		}break;
                        	case '[':
                    		{
                    			t = new Token(LSQUARE, "[",pos, line);
                    			System.out.println(t);
                    			getChar();
                    		}break;
                        	case ']':
                    		{
                    			t = new Token(RSQUARE, "]",pos, line);
                    			System.out.println(t);
                    			getChar();
                    		}break;
                    		
                        	case ':':
                    		{
                    			getChar();
                    			
                    			if(ch==':')
                    			{
                    				t = new Token(COLONCOLON, "::",pos, line);
                    				System.out.println(t);
                    				getChar();
                    				t=null;
                    	
                    			}
                    			
                    			else
                    			{
                    				t = new Token(COLON,":",pos,line);
                    				System.out.println(t);
                    				
                    				t=null;
                    			}
                    			
                    		}break;
                    		
                        	case ';':
                    		{
                    			t = new Token(SEMI, ";",pos, line);
                    			System.out.println(t);
                    			getChar();
                    		}break;
                    		
                        	case '.':
                    		{
                    			getChar();
                    			
                    			if(ch=='.')
                    			{
                    				getChar();
                    				if(ch=='.')
                    				{
                    					t = new Token(DOTDOTDOT, "...",pos, line);
                    					System.out.println(t);
                        				getChar();
                        				t=null;
                    				}
                    				else
                    				{
                    				t = new Token(DOTDOT, "..",pos, line);
                    				System.out.println(t);
                    				t=null;
                    				}
                    			}
                    			
                    		
                    			
                    			else
                    			{
                    				t = new Token(DOT,".",pos,line);
                    				System.out.println(t);
                    				
                    				t=null;
                    			}
                    			
                    		}break;
                    		
                    		
                    		
                            case ',':
                                {
                                    System.out.println("Scanned literal :" + (char) ch);
                                    t = new Token(COMMA, ",", pos, line);
                                    System.out.println(t);
                                    getChar();
                                }
                                break;
                            case -1:
                                return new Token(EOF, "eof", pos, 0);

                            default:
                                System.out.println("failure");
                                break;
                        }
                        break;
                    }
                default:
                    System.out.println("second failure");
            }

        }
        pos = pos + 1;
        if (ch == -1) {
            return new Token(EOF, "eof", pos, 0);
        }

        //replace this code.  Just for illustration
        return t;


        //throw new LexicalException("Useful error message");

    }
}