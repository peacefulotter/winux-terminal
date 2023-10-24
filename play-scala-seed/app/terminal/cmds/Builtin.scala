package terminal.cmds

import com.pty4j.{PtyProcess, PtyProcessBuilder}
import models.Response

import scala.language.postfixOps
import java.io.IOException
import scala.io.Source

class Builtin(implicit params: Command.Params) extends Command {
	
	def handle(): Response = {
		try {
			val env = new java.util.HashMap[String, String](System.getenv())
			env.put("TERM", "xterm")
			val command = (List("cd", path.toString(), "&&", keyword) ::: arguments).toArray
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
			Response.Line(stdout)
		}
		catch {
			case e: IOException => Response.Line.error(e.getMessage)
			case e: UnsupportedOperationException => Response.Line.error(e.getMessage)
			case e: os.SubprocessException => Response.Line.error(e.getMessage)
		}
	}
}
