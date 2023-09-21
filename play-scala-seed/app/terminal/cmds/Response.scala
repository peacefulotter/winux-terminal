package terminal.cmds

import terminal.cmds.ResType.ResType

abstract class Response[T](status: Status.Value)(implicit resType: ResType);
object Response {
	case class Success[T](t: T)(implicit resType: ResType) extends Response[T](Status.Success)
	case class Failure[T](msg: String) extends Response[T](Status.Error)(ResType.Line)
	case class Nothing[T]() extends Response[T](Status.Nothing)(ResType.Empty)
}
