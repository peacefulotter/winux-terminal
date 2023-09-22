package terminal.features

import terminal.cmds.{DataInput, Response}

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
	
	private def older(): Option[String] = {
		index = math.min(history.size - 1, index + 1)
		history.lift(index)
	}
	
	private def younger(): Option[String] = {
		index = math.max(DEFAULT_INDEX, index - 1)
		history.lift(index)
	}
	
	private def handleHistoryPull(routine: () => Option[String]): Response[String] = routine() match {
		case Some(cmd) => Response.Success(DataInput(cmd))
		case None => Response.Nothing();
	}
	
	def get: ListBuffer[String] = history
	def arrowUp(): Response[String] = handleHistoryPull(older)
	def arrowDown(): Response[String] = handleHistoryPull(younger)
}

object History {
	def apply(): History = new History()
}