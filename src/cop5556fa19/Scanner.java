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
    int currLine = 0;
    int ch;
    boolean next=true;
    
 
    
    public void getChar() throws IOException {
        //read character
        ch = r.read();
        //System.out.println("Character read="+(char)ch);
      
        currPos = currPos + 1;


    }
    
    public void Esq_Char() throws Exception {
    	getChar();
    	
    }


    public Token getNext() throws Exception {
    	if(next)
    	{
    		getChar();
    		next=true;
    	}
        
        Token t = null;
        StringBuilder sb;
        sb = new StringBuilder();
        int pos = 0;
        int line = 0;
        State state = State.START;

        while (t == null) {
            //System.out.println((char)ch);
            switch (state) {
                case START:
                    {
                    	while((char)ch=='\n' || (char)ch==' ' || (char)ch=='\f' || (char)ch=='\t' ||(char)ch=='\r')
                    	{
                    		
                    		if((char)ch=='\n' || (char)ch=='\r')
                    		{
                    			if((char)ch=='\r')
                    			{
                    				getChar();
                    				if((char)ch=='\n')
                    				{
                    					currLine=currLine+1;
                    					currPos=-1;
                    					getChar();
                    				}
                    				else
                    				{
                    					
                    					currLine=currLine+1;
                					currPos=-1;}
                    			}
                    			else
                    			{
                    				currLine=currLine+1;
                					currPos=-1;
                					getChar();
                    			}
                    		}
                    		else {
                    			getChar();
                    		}


                    		
                    	}
                    	
                    	
                        pos = currPos;
                        line = currLine;
                        switch (ch) {
                            case '+':
                                {
                                    t = new Token(OP_PLUS, "+", pos, line);
                                    next=true;
                                }
                                break;
                            case '-':
                                {
                                	getChar();
                                	if((char)ch=='-')
                                	{
                                		while((char)ch!='\n' && (char)ch!='\r' && ch!=-1)
                                		{
                                			getChar();
                                		}
                                	}
                                	else
                                	{
                                    t = new Token(OP_MINUS, "-", pos, line);
                                    next=false;
                                	}
                                }
                                break;
                            case '*':
                                {
                                    t = new Token(OP_TIMES, "*", pos, line);
                                    next=true;
                                }
                                break;
                            case '/':
                                {
                                    getChar();

                                    if (ch == '/') {
                                        t = new Token(OP_DIVDIV, "//", pos, line);
                                        next=true;
                                        

                                    } else {
                                        t = new Token(OP_DIV, "/", pos, line);
                                       next=false;
                                    }

                                }
                                break;
                            case '%':
                                {
                                    t = new Token(OP_MOD, "%", pos, line);
                                    next=true;
                                }
                                break;
                            case '^':
                                {
                                    t = new Token(OP_POW, "^", pos, line);
                                    next=true;
                                }
                                break;
                            case '#':
                                {
                                    t = new Token(OP_HASH, "#", pos, line);
                                    next=true;
                                }
                                break;
                            case '&':
                                {
                                    t = new Token(BIT_AMP, "&", pos, line);
                                    next=true;
                                }
                                break;

                            case '~':
                                {
                                    {
                                        getChar();

                                        if (ch == '=') {
                                            t = new Token(REL_NOTEQ, "~=", pos, line);
                                            next=true;

                                        } else {
                                            t = new Token(BIT_XOR, "~", pos, line);
                                            next=false;
                                        }

                                    }
                                }
                                break;





                            case '|':
                                {
                                    t = new Token(BIT_OR, "|", pos, line);
                                    next=true;
                                }
                                break;
                            case '<':
                                {
                                    getChar();

                                    if (ch == '<') {
                                        t = new Token(BIT_SHIFTL, "<<", pos, line);
                                        next=true;

                                    } else if (ch == '=') {
                                        t = new Token(REL_LE, "<=", pos, line);
                                        next=true;
                                    } else {
                                        t = new Token(REL_LT, "<", pos, line);
                                        next=false;
                                    }
                                    break;

                                }
                            case '>':
                                {

                                    getChar();
                                    if (ch == '>') {
                                        t = new Token(BIT_SHIFTR, ">>", pos, line);
                                        next=true;

                                    } else if (ch == '=') {
                                        t = new Token(REL_GE, ">=", pos, line);
                                        next=true;

                                    } else {
                                        t = new Token(REL_GT, ">", pos, line);
                                        next=false;

                                    }
                                    break;
                                }
                            case '=':
                                {

                                    getChar();
                                    if (ch == '=') {
                                        t = new Token(REL_EQEQ, "==", pos, line);
                                        next=true;

                                    } else {
                                        t = new Token(ASSIGN, "=", pos, line);
                                        next=false;

                                    }
                                    break;
                                }
                            case '(':
                                {
                                    t = new Token(LPAREN, "(", pos, line);
                                    next=true;
                                }

                                break;
                            case ')':
                                {
                                    t = new Token(RPAREN, ")", pos, line);
                                    next=true;
                                }
                                break;
                            case '{':
                                {
                                    t = new Token(LCURLY, "{", pos, line);
                                    next=true;
                                }
                                break;

                            case '}':
                                {
                                    t = new Token(RCURLY, "}", pos, line);
                                    next=true;
                                }
                                break;
                            case '[':
                                {
                                    t = new Token(LSQUARE, "[", pos, line);
                                    next=true;
                                }
                                break;
                            case ']':
                                {
                                    t = new Token(RSQUARE, "]", pos, line);
                                    next=true;
                                }
                                break;

                            case ':':
                                {
                                    getChar();

                                    if (ch == ':') {
                                        t = new Token(COLONCOLON, "::", pos, line);
                                        next=true;

                                    } else {
                                        t = new Token(COLON, ":", pos, line);
                                       next=false;
                                    }

                                }
                                break;

                            case ';':
                                {
                                    t = new Token(SEMI, ";", pos, line);
                                    next=true;
                                }
                                break;

                            case '.':
                                {
                                    getChar();

                                    if (ch == '.') {
                                        getChar();
                                        if (ch == '.') {
                                            t = new Token(DOTDOTDOT, "...", pos, line);
                                            next=true;
                                           
                                        } else {
                                            t = new Token(DOTDOT, "..", pos, line);
                                            next=false;
                                        }
                                    } else {
                                        t = new Token(DOT, ".", pos, line);
                                        next=false;
                                    }

                                }
                                break;



                            case ',':
                                {
                                    t = new Token(COMMA, ",", pos, line);
                                    next=true;

                                }
                                break;
                            case -1:
                                {
                                    return new Token(EOF, "eof", pos, line);
                                    
                                }
                            default:
                                {
                                    if (Character.isDigit(ch)) {
                                        if ((char) ch == '0') {
                                            t = new Token(INTLIT, "0", pos, line);
                                            next=true;
                                            break;
                                        } else {
                                            state = State.IN_NUMLIT;
                                            sb = new StringBuilder();
                                            sb.append((char) ch);
                                            getChar();
                                        }
                                    } else if (Character.isAlphabetic(ch)) {
                                        state = State.IN_IDENT;
                                        sb = new StringBuilder();
                                        sb.append((char) ch);
                                        getChar();

                                    } else if ((char) ch == '\"') {
                                        state = State.IN_QUOTES;
                                        sb = new StringBuilder();
                                        sb.append((char) ch);
                                        getChar();
                                    } else if ((char) ch == '\'') {
                                        state = State.IN_APOSTROPE;
                                        sb = new StringBuilder();
                                        sb.append((char) ch);
                                        getChar();
                                    } else {
                                        throw new LexicalException("Unacceptable Character " + (char) ch + " found at position " + pos + " on line no:" + line);
                                    }
                                    break;
                                }
                        }
                        break;
                    }
                case IN_NUMLIT:
                    {
                    	
                    	
                        if (Character.isDigit(ch)) {
                        	
                            sb.append((char) ch);
                            
                            getChar();
                        } else {
                        	String s= sb.toString();
                            try {
                            	Integer.parseInt(s);
                            	}
                            catch(Exception e)	
                            {
                            	throw new LexicalException("Numeric literal limit exceeded at position " + pos + " on line no:" + line);
                            	}
                            t = new Token(INTLIT, sb.toString(), pos, line);
                            state = State.START;
                            next=false;
                            
                        }break;
                    }
                  

                case IN_IDENT:
                    {
                        if (Character.isJavaIdentifierPart(ch)) {
                            sb.append((char) ch);
                            getChar();
                        } else {
                            switch (sb.toString()) {
                                case "and":
                                    {
                                        t = new Token(KW_and, "and", pos, line);
                                        state = State.START;
                                        next=false;
                                    }
                                    break;
                                case "break":
                                    {
                                        t = new Token(KW_break, "break", pos, line);
                                        state = State.START;
                                        next=false;
                                    }
                                    break;
                                case "do":
                                    {
                                        t = new Token(KW_do, "do", pos, line);
                                        state = State.START;
                                        next=false;
                                    }
                                    break;
                                case "else":
                                    {
                                        t = new Token(KW_else, "else", pos, line);
                                        state = State.START;
                                        next=false;
                                    }
                                    break;

                                case "elseif":
                                    {
                                        t = new Token(KW_elseif, "elseif", pos, line);
                                        state = State.START;
                                        next=false;
                                    }
                                    break;
                                case "end":
                                    {
                                        t = new Token(KW_end, "end", pos, line);
                                        state = State.START;
                                        next=false;
                                    }
                                    break;
                                case "false":
                                    {
                                        t = new Token(KW_false, "false", pos, line);
                                        state = State.START;
                                        next=false;
                                    }
                                    break;
                                case "for":
                                    {
                                        t = new Token(KW_for, "for", pos, line);
                                        state = State.START;
                                        next=false;;
                                    }
                                    break;
                                case "function":
                                    {
                                        t = new Token(KW_function, "function", pos, line);
                                        state = State.START;
                                        next=false;
                                    }
                                    break;
                                case "goto":
                                    {
                                        t = new Token(KW_goto, "goto", pos, line);
                                        state = State.START;
                                        next=false;
                                    }
                                    break;
                                case "if":
                                    {
                                        t = new Token(KW_if, "if", pos, line);
                                        state = State.START;
                                        next=false;
                                 
                                    }
                                    break;
                                case "in":
                                    {
                                        t = new Token(KW_in, "in", pos, line);
                                        state = State.START;
                                        next=false;
                                    }
                                    break;
                                case "local":
                                    {
                                        t = new Token(KW_local, "local", pos, line);
                                        state = State.START;
                                        next=false;
                                    }
                                    break;
                                case "nil":
                                    {
                                        t = new Token(KW_nil, "nil", pos, line);
                                        state = State.START;
                                        next=false;
                                    }
                                    break;
                                case "not":
                                    {
                                        t = new Token(KW_not, "not", pos, line);
                                        state = State.START;
                                        next=false;
                                    }
                                    break;
                                case "or":
                                    {
                                        t = new Token(KW_or, "or", pos, line);
                                        state = State.START;
                                        next=false;
                                    }
                                    break;
                                case "repeat":
                                    {
                                        t = new Token(KW_repeat, "repeat", pos, line);
                                        state = State.START;
                                        next=false;
                                    }
                                    break;
                                case "return":
                                    {
                                        t = new Token(KW_return, "return", pos, line);
                                        state = State.START;
                                        next=false;
                                    }
                                    break;
                                case "then":
                                    {
                                        t = new Token(KW_then, "then", pos, line);
                                        state = State.START;
                                        next=false;
                                    }
                                    break;
                                case "true":
                                    {
                                        t = new Token(KW_true, "true", pos, line);
                                        state = State.START;
                                        next=false;
                                    }
                                    break;
                                case "until":
                                    {
                                        t = new Token(KW_until, "until", pos, line);
                                        state = State.START;
                                        next=false;
                                    }
                                    break;
                                case "while":
                                    {
                                        t = new Token(KW_while, "while", pos, line);
                                        state = State.START;
                                        next=false;
                                    }
                                    break;

                                default:
                                    {
                                        t = new Token(NAME, sb.toString(), pos, line);
                                        state = State.START;
                                        next=false;
                                    }
                                    break;
                            }

                        }
                    }
                    break;

                case IN_QUOTES:
                    {
                    	
                    
                        if (ch == -1) {
                            pos = pos + 1;
                            throw new LexicalException("String Literal should end with \" at pos " + pos + " on line no:" + line);
                        } else if ((char) ch == '\"') {
                            sb.append((char) ch);
                            t = new Token(STRINGLIT, sb.toString(), pos, line);
                            state = State.START;
                            getChar();
                            next=false;
                        }
                        else if((int)ch==92)
                        {
                        	
                        	getChar();
                        	next=true;
                        	switch(ch)
                        	{
                        	case 'n': ch =10;
                        	sb.append((char)ch);
                        	currLine=currLine+1;
                        	
                        	break;
                        	case 'a': ch=7;
                        	sb.append((char)ch);
                        	break;
                        	case 'b': ch=8;
                        	sb.append((char)ch);
                        	break;
                        	case 'f': ch=12;
                        	sb.append((char)ch);
                        	break;
                        	case 'r': ch=13;
                        	sb.append((char)ch);
                        	currLine=currLine+1;
                        	break;
                        	case 't': ch=9;	
                        	sb.append((char)ch);
                        	break;
                        	case 'v': ch=11;
                        	sb.append((char)ch);
                        	break;
                        	case 92: ch=92;
                        	sb.append((char)ch);
                        	getChar();
                        	break;
                        	case '"': ch=34;
                        	sb.append((char)ch);
                        	getChar();
                        	break;
                        	case 39 : 
                        		sb.append((char)ch);
                            	break;
                            	default :
                            		throw new LexicalException("Symbol \\ not allowed in a string literal at pos " + pos + " on line no:" + line);
                                    
                        	}
                        }
                        else if((char)ch=='\n')
                        {
                        	currLine=currLine+1;
                        	sb.append((char)ch);
                        	getChar();
                        }
                        else if((char)ch =='"' || ch==39)
                        {
                        	pos=currPos;
                        	throw new LexicalException("Symbol "+(char)ch+" not allowed in a string literal at pos " + pos + " on line no:" + line);
                        
                    }else {
                    		
                            
                    			sb.append((char) ch);
                            
                            getChar();
                        }
                    }
                    break;
                case IN_APOSTROPE:
                    {
                        if (ch == -1) {
                            pos = pos + 1;
                            throw new LexicalException("String Literal should end with \' at pos " + pos + " on line no:" + line);
                        } else if ((char) ch == '\'') {
                            sb.append((char) ch);
                            t = new Token(STRINGLIT, sb.toString(), pos, line);
                            state = State.START;
                            getChar();
                            next=false;
                        }
                        else if((int)ch==92)
                        {
                        	
                        	getChar();
                        	next=true;
                        	switch(ch)
                        	{
                        	case 'n': ch =10;
                        	sb.append((char)ch);
                        	currLine=currLine+1;
                        	
                        	break;
                        	case 'a': ch=7;
                        	sb.append((char)ch);
                        	break;
                        	case 'b': ch=8;
                        	sb.append((char)ch);
                        	break;
                        	case 'f': ch=12;
                        	sb.append((char)ch);
                        	break;
                        	case 'r': ch=13;
                        	sb.append((char)ch);
                        	currLine=currLine+1;
                        	break;
                        	case 't': ch=9;	
                        	sb.append((char)ch);
                        	break;
                        	case 'v': ch=11;
                        	sb.append((char)ch);
                        	break;
                        	case 92: ch=92;
                        	sb.append((char)ch);
                        	getChar();
                        	break;
                        	case 34: ch=34;
                        	sb.append((char)ch);
                        	getChar();
                        	break;
                        	case 39 : 
                        		sb.append((char)ch);
                        		getChar();
                            	break;
                            	default :
                            		throw new LexicalException("Symbol \\ not allowed in a string literal at pos " + pos + " on line no:" + line);
                                    
                        	}
                        }
                        else if((char)ch=='\n')
                        {
                        	currLine=currLine+1;
                        	sb.append((char)ch);
                        	getChar();
                        }
                            else if((char)ch =='\"'  || (char)ch=='"')
                            {
                            	pos=currPos;
                            	throw new LexicalException("Symbol "+(char)ch+" not allowed in a string literal at pos " + pos + " on line no:" + line);
                            
                        } else {
                            sb.append((char) ch);
                            getChar();
                        }
                    }
                    break;
                default:
                    

                	throw new LexicalException("Invalid entry at pos " + pos + " on line no:" + line);
            }

        }
        pos = pos + 1;
        
  
        //replace this code.  Just for illustration
        return t;


        //throw new LexicalException("Useful error message");

    }
}