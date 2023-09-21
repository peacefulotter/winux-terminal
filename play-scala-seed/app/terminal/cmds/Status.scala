package terminal.cmds

object Status extends Enumeration {
	type Status = Value
	val Success, Error, Nothing = Value
}
