package gte.utils;

/**
 * User: Alan P. Sexton
 * Date: 20/06/13
 * Time: 18:44
 */

/**
 * Signals that an Image read exception of some sort has occurred.
 */
public class UnsupportedImageTypeException extends Exception
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Constructs an <code>project.utils.UnsupportedImageTypeException</code> with <code>null</code>
     * as its error detail message.
     */
    public UnsupportedImageTypeException()
    {
        super();
    }

    /**
     * Constructs an <code>project.utils.UnsupportedImageTypeException</code> with the specified detail
     * message. The error message string <code>s</code> can later be
     * retrieved by the <code>{@link Throwable#getMessage}</code>
     * method of class <code>java.lang.Throwable</code>.
     *
     * @param s the detail message.
     */
    public UnsupportedImageTypeException(String s)
    {
        super(s);
    }
}
