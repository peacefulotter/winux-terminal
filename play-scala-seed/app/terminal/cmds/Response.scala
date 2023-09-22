package terminal.cmds

abstract class Response[T](val status: Status.Value)(data: ResData[T]);

object Response {
	case class Success[T](data: ResData[T]) extends Response[T](Status.Success)(data)
	case class Failure[T](msg: String) extends Response[T](Status.Error)(DataLine(msg))
	case class Nothing[T]() extends Response[T](Status.Nothing)(DataEmpty())
}
