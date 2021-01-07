package luaCompiler.AST;

import luaCompiler.Token;

public abstract class Var extends Exp {

	public Var(Token firstToken) {
		super(firstToken);
	}


}
