package jsyphon

class JSyphonImage(private val name: Int, private val width: Int, private val height: Int) {
    fun textureName(): Int {
        return name
    }

    fun textureWidth(): Int {
        return width
    }

    fun textureHeight(): Int {
        return height
    } /*
	public JSyphonImage()
	{
	}

	//Load the library
	static
	{
		System.out.println("Loading JSyphon");
		System.out.println("Java Library Path: " + System.getProperty("java.library.path"));
		System.loadLibrary("JSyphon");		
		System.out.println("JSyphon Loaded");
	}

	//Native method declarations
	public native int textureName();
	public native int textureWidth();
	public native int textureHeight();
	*/

}