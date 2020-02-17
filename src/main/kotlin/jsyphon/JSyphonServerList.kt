package jsyphon

import java.util.ArrayList
import java.util.HashMap

object JSyphonServerList {
    val list: ArrayList<HashMap<String?, String?>?>?
        external get

    init {
        System.loadLibrary("JSyphon")
    }
}