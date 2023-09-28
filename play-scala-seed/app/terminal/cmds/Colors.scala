package terminal.cmds

import models.Response
import terminal.colors.Ansi

import scala.Console.RESET

class Colors() extends Command {
	
	private def deepMembers[A: scala.reflect.runtime.universe.TypeTag](a: A): List[String] = {
		import scala.reflect.runtime.universe._
		def members(s: Symbol): List[String] =
			s.typeSignature.decls.collect {
				case m: ModuleSymbol => members(m)
				case m: MethodSymbol if m.isAccessor => m.returnType match {
					case ConstantType(Constant(s: String)) =>
						List(s"${s}${m.name.decodedName.toString}${RESET}")
					case _ => List.empty[String]
				}
			}.foldLeft(List.empty[String])(_ ::: _)
		
		members(typeOf[A].termSymbol)
	}
	
	private val ANSI_MEMBERS: List[String] = deepMembers(Ansi)
	
	def handle(params: List[String]): Response = {
		Response.Success(DataFlex(ANSI_MEMBERS))
	}
}
