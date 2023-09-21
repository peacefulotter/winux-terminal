package terminal.features

import scala.collection.mutable.ListBuffer

class History() {
	private val DEFAULT_INDEX = -1;
	private val MAX_SIZE = 64
	private val history = ListBuffer[String]()
	
	private var index = DEFAULT_INDEX;
	
	def push(cmd: String): Unit = {
		index = DEFAULT_INDEX
		if (history.nonEmpty && cmd == history.head)
			return;
		if (history.size >= MAX_SIZE)
			history.dropInPlace(1) // Remove the last element to make space for the new one
		history.prepend(cmd)
	}
	
	def older(): Option[String] = {
		index = math.min(history.size - 1, index + 1)
		history.lift(index)
	}
	
	
	def younger(): Option[String] = {
		index = math.max(DEFAULT_INDEX, index - 1)
		history.lift(index)
	}
	
	def get: ListBuffer[String] = history;
}

object History {
	def apply(): History = new History()
}