package terminal.cmds

import com.pty4j.{PtyProcess, PtyProcessBuilder}
import models.{DataLine, Response}

import scala.language.postfixOps
import java.io.IOException
import scala.io.Source

class Builtin(implicit params: Command.Params) extends Command {
	
	def handle(params: List[String]): Response = {
		try {
			val env = new java.util.HashMap[String, String](System.getenv())
			env.put("TERM", "xterm")
			val command = (List("cd", path.toString(), "&&", keyword) ::: params).toArray
			println(command.mkString(", "))
			val process: PtyProcess = new PtyProcessBuilder()
				.setCommand(command)
				.setEnvironment(env)
				.start
			val is = process.getInputStream
			Source.fromInputStream(is).foreach(print)
			val stdout = Source.fromInputStream(is).mkString
			process.getOutputStream // to avoid closing unused process's stdin in destroy method
			process.waitFor()
			Response.Success(DataLine(stdout))
		}
		catch {
			case e: IOException => new Response.Failure(e.getMessage)
			case e: UnsupportedOperationException => new Response.Failure(e.getMessage)
			case e: os.SubprocessException => new Response.Failure(e.getMessage)
		}
	}
}
