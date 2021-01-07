package interpreter;


public class LuaNil extends LuaValue {
	
	public static final LuaValue nil = new LuaNil();  
	
	public String toString() {
		return "nil";
	}

	@Override
	public LuaValue copy() {
		return nil;
	}
	
	private LuaNil() {}
}
