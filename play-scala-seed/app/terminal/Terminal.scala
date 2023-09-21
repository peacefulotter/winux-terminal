package terminal

import os.Path
import terminal.cmds._
import terminal.features.History
import terminal.helpers.InputHelper.parseInput

class Terminal() {
	private var path: Path = os.root
	def getPath: Path = path
	def setPath(path: Path): Unit = this.path = path
	
	val history = new History
	
	private def getCommand(text: String): (Option[Command[?]], List[String]) = {
		val input = parseInput(text)
		if (input.isEmpty) {
			return (Option.empty[Command[?]], Nil)
		}
		
		val (keyword, params) = (input.head, input.tail)
		val cmd: Command[?] = keyword match {
			case "cat" => new Cat()
			case "cd" => new Cd(this)
			case "ls" => new Ls(path)
			case "find" => new Find(path)
			case "history" => new HistoryCmd(history)
			case _ => new Builtin(keyword, path) // new Err(keyword)
		}
		(Some(cmd), params)
	}
	
	def handleCommand(text: String): Response[?] = {
		getCommand(text) match {
			case (Some(cmd), params) =>
				val res = cmd.handle(params)
				history.push(text)
				res
			case _ => Response.Nothing()
		}
	}
	
	private def handleHistoryPull(routine: () => Option[String]): Response[String] = routine() match {
		case Some(cmd) => Response.Success(cmd)(ResType.Input)
		case None => Response.Nothing();
	}
	private def handleArrowUp(): Unit = handleHistoryPull(history.older)
	private def handleArrowDown(): Unit = handleHistoryPull(history.younger)
	
	private def handleKill(): Unit = println("===== killing")
}
