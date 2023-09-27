package terminal.cmds

import akka.actor.ActorRef
import com.pty4j.{PtyProcess, PtyProcessBuilder}
import managers.ActorRefManager.SendResponse
import models.Response
import os.{Path, SubProcess}

import java.io.{BufferedReader, ByteArrayOutputStream, IOException, InputStreamReader}
import java.nio.charset.StandardCharsets
import scala.collection.mutable
import scala.io.Source
import scala.language.postfixOps

class Builtin(manager: ActorRef, cmd: String, path: Path) extends Command {
	
	private def process(f: String => Unit) = os.ProcessOutput.Readlines(f)
	
	private def processOut = process( line => {
		manager ! SendResponse(Response.Success(DataLine(line)))
	})
	
	private def processErr = process( line => {
		manager ! SendResponse(Response.Failure(line))
	})
	
	def handle(params: List[String]): Response = {
		try {
			println(cmd :: params)
			println(path)
//			os.proc(cmd :: params).spawn(
//				cwd = path,
//				env = Map("TERM" -> "xterm-color"),
//				stdout = processOut,
//				stderr = processErr
//			).join()
			//   + (
			val env = new java.util.HashMap[String, String](
				System.getenv()
			)
			println(env)
			env.put("TERM", "xterm")
			println(env)
			val command = (cmd :: params).toArray
			println(command.mkString(", "))
			val process: PtyProcess = new PtyProcessBuilder()
				.setCommand(command)
				.setEnvironment(env)
				.start
			val is = process.getInputStream
			val stdout = Source.fromInputStream(is).mkString
			process.getOutputStream // to avoid closing unused process's stdin in destroy method
			process.waitFor()
			Response.Success(DataLine(stdout))
		}
		catch {
			case e: IOException => Response.Failure(e.getMessage)
			case e: UnsupportedOperationException => Response.Failure(e.getMessage)
			case e: os.SubprocessException => Response.Failure(e.getMessage)
		}
	}
}
