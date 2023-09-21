package terminal.cmds

object ResType extends Enumeration {
	type ResType = Value
	val Empty, // Response does nothing (e.g. when doing "cd .")
		Line,  // Response adds a paragraph line
		List,  // Response adds a list of paragraphs
		Flex,  // Response adds many paragraphs that need to be arranged in a flexbox
		Path,  // Response modifies the current path
		Input  // Response modifies the current input
		= Value
}
