package terminal.cmds

import akka.actor.ActorRef
import managers.ActorRefManager.SendResponse
import models.Response
import os.{Path, SubProcess}

import java.io.{ByteArrayOutputStream, IOException}
import scala.language.postfixOps

class Builtin(manager: ActorRef, cmd: String, path: Path) extends Command {
	
//	private def process(f: (Array[Byte], Int) => Unit) = os.ProcessOutput.ReadBytes(f)
//
//	private def processOut = process( (line, len) => {
//		val buf = line.slice(0, len)
//		val other = buf.map(b =>
//			(b.toInt, b.toChar)
//		)
//		println(s"OUT '${buf.mkString("Array(", ", ", ")")} '")
//		println(s"other '${other.mkString("Array(", ", ", ")")} '")
//
//	} )
//
//	private def processErr = process( (line, len) => {
//		val buf = line.slice(0, len)
//		println(s"ERR '${buf.map(_.toChar).mkString("Array(", ", ", ")")} '")
//	} )
	
	private def process(f: String => Unit) = os.ProcessOutput.Readlines(f)
	
	private def processOut = process( line => {
		println(f"OUT $line")
		manager ! SendResponse(Response.Success(DataLine(line)))
	})
	
	private def processErr = process( line => {
		println(f"ERR $line")
		manager ! SendResponse(Response.Failure(line))
	})
	
	def handle(params: List[String]): Response = {
		try {
			println(cmd :: params)
			println(path)
			os.proc(cmd :: params).spawn(
				cwd = path,
				env = Map("TERM" -> "xterm-color"),
				stdout = processOut,
				stderr = processErr
			).join()
			Response.Nothing()
		}
		catch {
			case e: IOException => Response.Failure(e.getMessage)
			case e: os.SubprocessException => Response.Failure(e.getMessage)
		}
	}
}
