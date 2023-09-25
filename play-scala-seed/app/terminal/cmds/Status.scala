package terminal.cmds

object Status extends Enumeration {
	type Status = Value
	val Success, Failure, Nothing = Value
}
