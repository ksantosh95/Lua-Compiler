package luaCompiler.AST;

import luaCompiler.Token;

public abstract class Stat extends ASTNode {
	
	ASTNode parent;

	public Stat(Token firstToken) {
		super(firstToken);
	}

	public ASTNode getParent() {
		return parent;
	}

	public void setParent(ASTNode parent) {
		this.parent = parent;
	}



}
