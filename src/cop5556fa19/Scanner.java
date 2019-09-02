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
        IN_QUOTES,
        IN_APOSTROPE,
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

        StringBuilder sb;
        sb=new StringBuilder();
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
                            {
                            	return new Token(EOF, "eof", pos, 0);
                            }
                            default:
                                {
                                	if(Character.isDigit(ch))
                                	{
                                		if((char)ch=='0')
                                		{
                                			t= new Token(INTLIT,"0",pos,line);
                                			System.out.println(t);
                                			getChar();
                                			t=null;
                                		}
                                		else
                                		{
                                		state = State.IN_NUMLIT;	
                                		sb=new StringBuilder();
                                		sb.append((char)ch);
                                		getChar();
                                		}
                                	}
                                	else if(Character.isAlphabetic(ch))
                                	{
                                		state = State.IN_IDENT;
                                		sb=new StringBuilder();
                                		sb.append((char)ch);
                                		getChar();
                                		
                                	}
                                	else if((char)ch=='\"')
                                	{
                                		state= State.IN_QUOTES;
                                		sb= new StringBuilder();
                                		sb.append((char)ch);
                                		getChar();
                                	}
                                	else if((char)ch=='\'')
                                	{
                                		state= State.IN_APOSTROPE;
                                		sb= new StringBuilder();
                                		sb.append((char)ch);
                                		getChar();
                                	}
                                	else
                                	{
                                		throw new LexicalException("Unacceptable Character "+(char)ch+" found at position "+pos+" on line no:"+line);
                                	}
                                break;
                                }
                        }
                        break;
                    }
                case IN_NUMLIT:
                {
                	
                	if(Character.isDigit(ch))
                	{
                		sb.append((char)ch);
                		getChar();
                	}
                	else
                	{
                		t = new Token(INTLIT,sb.toString(),pos,line);
                		System.out.println(t);
                		state=State.START;
                		t=null;
                	}
                }break;
                
                case IN_IDENT:
                {
                	if(Character.isJavaIdentifierPart(ch))
                	{
                		sb.append((char)ch);
                		getChar();
                	}
                	else
                	{
                		switch(sb.toString())
                		{
                		case "and":
                		{
                			t=new Token(KW_and,"and",pos,line);	
                			System.out.println(t);
                    		state=State.START;
                    		t=null;
                		}break;
                		case "break":
                		{
                			t=new Token(KW_break,"break",pos,line);	
                			System.out.println(t);
                    		state=State.START;
                    		t=null;
                		}break;
                		case "do":
                		{
                			t=new Token(KW_do,"do",pos,line);	
                			System.out.println(t);
                    		state=State.START;
                    		t=null;
                		}break;
                		case "else":
                		{
                			t=new Token(KW_else,"else",pos,line);	
                			System.out.println(t);
                    		state=State.START;
                    		t=null;
                		}break;
                	
                		case "elseif":
                		{
                			t=new Token(KW_elseif,"elseif",pos,line);	
                			System.out.println(t);
                    		state=State.START;
                    		t=null;
                		}break;
                		case "end":
                		{
                			t=new Token(KW_end,"end",pos,line);	
                			System.out.println(t);
                    		state=State.START;
                    		t=null;
                		}break;
                		case "false":
                		{
                			t=new Token(KW_false,"false",pos,line);	
                			System.out.println(t);
                    		state=State.START;
                    		t=null;
                		}break;
                		case "for":
                		{
                			t=new Token(KW_for,"for",pos,line);	
                			System.out.println(t);
                    		state=State.START;
                    		t=null;
                		}break;
                		case "function":
                		{
                			t=new Token(KW_function,"function",pos,line);	
                			System.out.println(t);
                    		state=State.START;
                    		t=null;
                		}break;
                		case "goto":
                		{
                			t=new Token(KW_goto,"goto",pos,line);	
                			System.out.println(t);
                    		state=State.START;
                    		t=null;
                		}break;
                		case "if":
                		{
                			t=new Token(KW_if,"if",pos,line);	
                			System.out.println(t);
                    		state=State.START;
                    		t=null;
                		}break;
                		case "in":
                		{
                			t=new Token(KW_in,"in",pos,line);	
                			System.out.println(t);
                    		state=State.START;
                    		t=null;
                		}break;
                		case "local":
                		{
                			t=new Token(KW_local,"local",pos,line);	
                			System.out.println(t);
                    		state=State.START;
                    		t=null;
                		}break;
                		case "nil":
                		{
                			t=new Token(KW_nil,"nil",pos,line);	
                			System.out.println(t);
                    		state=State.START;
                    		t=null;
                		}break;
                		case "not":
                		{
                			t=new Token(KW_not,"not",pos,line);	
                			System.out.println(t);
                    		state=State.START;
                    		t=null;
                		}break;
                		case "or":
                		{
                			t=new Token(KW_or,"or",pos,line);	
                			System.out.println(t);
                    		state=State.START;
                    		t=null;
                		}break;
                		case "repeat":
                		{
                			t=new Token(KW_repeat,"repeat",pos,line);	
                			System.out.println(t);
                    		state=State.START;
                    		t=null;
                		}break;
                		case "return":
                		{
                			t=new Token(KW_return,"return",pos,line);	
                			System.out.println(t);
                    		state=State.START;
                    		t=null;
                		}break;
                		case "then":
                		{
                			t=new Token(KW_then,"then",pos,line);	
                			System.out.println(t);
                    		state=State.START;
                    		t=null;
                		}break;
                		case "true":
                		{
                			t=new Token(KW_true,"true",pos,line);	
                			System.out.println(t);
                    		state=State.START;
                    		t=null;
                		}break;
                		case "until":
                		{
                			t=new Token(KW_until,"until",pos,line);	
                			System.out.println(t);
                    		state=State.START;
                    		t=null;
                		}break;
                		case "while":
                		{
                			t=new Token(KW_while,"while",pos,line);	
                			System.out.println(t);
                    		state=State.START;
                    		t=null;
                		}break;
                		
                		default:
                		{
                			t=new Token(NAME,sb.toString(),pos,line);
                    		System.out.println(t);
                    		state=State.START;
                    		t=null;
                		}break;
                		}
                		
                	}
                }break;
                
                case IN_QUOTES:
                {
                	if(ch==-1)
                	{
                		pos=pos+1;
                		throw new LexicalException("String Literal should end with \" at pos "+pos+" on line no:"+line);
                	}
                	else if((char)ch=='\"')
                	{
                		sb.append((char)ch);
                		t=new Token(STRINGLIT,sb.toString(),pos,line);
                		System.out.println(t);
                		state=State.START;
                		t=null;
                		getChar();
                	}
                	else
                	{
                		sb.append((char)ch);
                		getChar();
                	}
                }break;
                case IN_APOSTROPE:
                {
                	if(ch==-1)
                	{
                		pos=pos+1;
                		throw new LexicalException("String Literal should end with \' at pos "+pos+" on line no:"+line);
                	}
                	else if((char)ch=='\'')
                	{
                		sb.append((char)ch);
                		t=new Token(STRINGLIT,sb.toString(),pos,line);
                		System.out.println(t);
                		state=State.START;
                		t=null;
                		getChar();
                	}
                	else
                	{
                		sb.append((char)ch);
                		getChar();
                	}
                }break;
                default:
                    System.out.println("second failure");
                    
                    t=new Token(STRINGLIT,"xyz",pos,line);
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