package terminal.colors

object Ansi {
	final val BRIGHT_BLACK = "\u001b[90m"
	final val BRIGHT_RED = "\u001b[91m"
	final val BRIGHT_GREEN = "\u001b[92m"
	final val BRIGHT_YELLOW = "\u001b[93m"
	final val BRIGHT_BLUE = "\u001b[94m"
	final val BRIGHT_MAGENTA = "\u001b[95m"
	final val BRIGHT_CYAN = "\u001b[96m"
	
	// from scala.io.AnsiColor
	// did not extend the trait
	// so that the colors command can get these fields
	// and it avoids importing two things when working
	// with colors as well...
	final val BLACK = "\u001b[30m"
	/** Foreground color for ANSI red
	 *
	 * @group color-red
	 */
	final val RED = "\u001b[31m"
	/** Foreground color for ANSI green
	 *
	 * @group color-green
	 */
	final val GREEN = "\u001b[32m"
	/** Foreground color for ANSI yellow
	 *
	 * @group color-yellow
	 */
	final val YELLOW = "\u001b[33m"
	/** Foreground color for ANSI blue
	 *
	 * @group color-blue
	 */
	final val BLUE = "\u001b[34m"
	/** Foreground color for ANSI magenta
	 *
	 * @group color-magenta
	 */
	final val MAGENTA = "\u001b[35m"
	/** Foreground color for ANSI cyan
	 *
	 * @group color-cyan
	 */
	final val CYAN = "\u001b[36m"
	/** Foreground color for ANSI white
	 *
	 * @group color-white
	 */
	final val WHITE = "\u001b[37m"
	
	/** Background color for ANSI black
	 *
	 * @group color-black
	 */
	final val BLACK_B = "\u001b[40m"
	/** Background color for ANSI red
	 *
	 * @group color-red
	 */
	final val RED_B = "\u001b[41m"
	/** Background color for ANSI green
	 *
	 * @group color-green
	 */
	final val GREEN_B = "\u001b[42m"
	/** Background color for ANSI yellow
	 *
	 * @group color-yellow
	 */
	final val YELLOW_B = "\u001b[43m"
	/** Background color for ANSI blue
	 *
	 * @group color-blue
	 */
	final val BLUE_B = "\u001b[44m"
	/** Background color for ANSI magenta
	 *
	 * @group color-magenta
	 */
	final val MAGENTA_B = "\u001b[45m"
	/** Background color for ANSI cyan
	 *
	 * @group color-cyan
	 */
	final val CYAN_B = "\u001b[46m"
	/** Background color for ANSI white
	 *
	 * @group color-white
	 */
	final val WHITE_B = "\u001b[47m"
	
	/** Reset ANSI styles
	 *
	 * @group style-control
	 */
	final val RESET = "\u001b[0m"
	/** ANSI bold
	 *
	 * @group style-control
	 */
	final val BOLD = "\u001b[1m"
	/** ANSI underlines
	 *
	 * @group style-control
	 */
	final val UNDERLINED = "\u001b[4m"
	/** ANSI blink
	 *
	 * @group style-control
	 */
	final val BLINK = "\u001b[5m"
	/** ANSI reversed
	 *
	 * @group style-control
	 */
	final val REVERSED = "\u001b[7m"
	/** ANSI invisible
	 *
	 * @group style-control
	 */
	final val INVISIBLE = "\u001b[8m"
}
