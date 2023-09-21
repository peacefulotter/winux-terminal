package terminal.cmds

abstract class Command[T] {
	def handle(params: List[String]): Response[T];
}
