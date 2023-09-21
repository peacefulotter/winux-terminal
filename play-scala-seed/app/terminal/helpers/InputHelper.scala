package terminal.helpers

object InputHelper {
	def parseInput(text: String): List[String] = text
		.split(" ")
		.filter(x => x.trim().nonEmpty)
		.toList
}
