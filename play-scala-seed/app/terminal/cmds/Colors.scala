package terminal.cmds

import models.{DataFlex, Response}
import terminal.colors.Ansi

import scala.Console.RESET

class Colors(implicit params: Command.Params) extends Command {
	// Get static variables of an object
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

object Colors {
	object Text extends Enumeration {
		case class C(name: String) extends super.Val
		val Foreground: C = C("foreground")
		val Success: C = C("success")
		val Error: C = C("error")
		val Info: C = C("info")
		val File: C = C("file")
		val Directory: C = C("directory")
	}
}