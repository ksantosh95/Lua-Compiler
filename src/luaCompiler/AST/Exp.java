package luaCompiler.AST;

import luaCompiler.Token;

public abstract class Exp extends ASTNode{

	public Exp(Token firstToken) {
		super(firstToken);
	}

}
