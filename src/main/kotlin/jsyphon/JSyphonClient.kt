package jsyphon

import java.util.*

class JSyphonClient  // public API
{
    private var ptr: Long = 0

    companion object {
        init {
            System.loadLibrary("JSyphon")
        }
    }

    fun init() {
        ptr = init(null)
    }

    fun setApplicationName(appName: String?) {
        setApplicationName(ptr, appName)
    }

    fun setServerName(serverName: String?) {
        setServerName(ptr, serverName)
    }

    val isValid: Boolean
        get() = isValid(ptr)

    fun newFrameImageForContext(): JSyphonImage {
        val dict = newFrameDataForContext()
        val name = dict["name"] as Long?
        val width = dict["width"] as Double?
        val height = dict["height"] as Double?
        return JSyphonImage(name!!.toInt(), width!!.toInt(), height!!.toInt())
    }

    // Native method declarations
    external fun init(options: HashMap<String?, Any?>?): Long
    external fun setApplicationName(ptr: Long, appName: String?)
    external fun setServerName(ptr: Long, serverName: String?)
    external fun isValid(ptr: Long): Boolean

    @JvmOverloads
    external fun serverDescription(ptr: Long = this.ptr): HashMap<String?, String?>?

    @JvmOverloads
    external fun hasNewFrame(ptr: Long = this.ptr): Boolean

    @JvmOverloads
    external fun newFrameDataForContext(ptr: Long = this.ptr): HashMap<String, Any>

    @JvmOverloads
    external fun stop(ptr: Long = this.ptr) //	public JSyphonImage newFrameImageForContext() {
    //	  HashMap<String, Object> dict = newFrameDataForContext();
    //	  Long name = (Long)dict.get("name");
    //	  Double width = (Double)dict.get("width");
    //	  Double height = (Double)dict.get("height");
    //	  return new JSyphonImage(name.intValue(), width.intValue(), height.intValue());
    //	}
}