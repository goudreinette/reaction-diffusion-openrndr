/**
 *
 */
package jsyphon.util

/**
 * @author Skye Book
 */
class NSRect(
    /**
     * @param origin the origin to set
     */
    var origin: NSPoint,
    /**
     * @param size the size to set
     */
    var size: NSSize
) {
    /**
     * @return the origin
     */
    /**
     * @return the size
     */

    /**
     *
     */
    constructor(startX: Int, xLength: Int, startY: Int, yLength: Int) : this(
        NSPoint(
            startX,
            startY
        ), NSSize(xLength, yLength)
    ) {
    }

}