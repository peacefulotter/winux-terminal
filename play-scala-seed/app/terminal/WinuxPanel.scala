package terminal

import terminal.cmds.{FailureResponse, Response, SuccessResponse}

class WinuxPanel(winux: Winux) {

	def addMessage(msg: String, color: Color = Settings.Color.foreground): Unit = ???
	def addSuccessMessage(msg: String): Unit = addMessage(msg, Settings.Color.success)
	def addErrorMessage(msg: String): Unit = addMessage(msg, Settings.Color.error)
	
	def addResponseMessage(response: Response[String]): Unit = response match {
		case SuccessResponse(msg) => addSuccessMessage(msg)
		case FailureResponse(msg) => addErrorMessage(msg)
		case _ => ;
	}
	
	def addInputLine(terminal: Terminal, text: String = ""): Unit = ???
	
	def setInputText(text: String): Unit = ???
}
