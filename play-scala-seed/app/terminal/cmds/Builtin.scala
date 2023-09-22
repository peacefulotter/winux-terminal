package terminal.cmds

import os.{Path, SubProcess}

import java.io.ByteArrayOutputStream
import scala.language.postfixOps

class Builtin(cmd: String, path: Path) extends Command[String] {
	
//	private def process(f: String => Unit) = os.ProcessOutput.Readlines(f)
//	private def processOut = process(line =>
//		println(s"'$line '")
//		winux.addMessage(line))
//	private def processErr = process(winux.addErrorMessage)
	
	private def process(f: (Array[Byte], Int) => Unit) = os.ProcessOutput.ReadBytes(f)
	
	private def processOut = process( (line, len) => {
		val buf = line.slice(0, len)
		val other = buf.map(b =>
			(b.toInt, b.toChar)
		)
		println(s"OUT '${buf.mkString("Array(", ", ", ")")} '")
		println(s"other '${other.mkString("Array(", ", ", ")")} '")
	} )
	
	private def processErr = process( (line, len) => {
		val buf = line.slice(0, len)
		println(s"ERR '${buf.map(_.toChar).mkString("Array(", ", ", ")")} '")
	} )
	
	def handle(params: List[String]): Response[String] = {
		try {
			println(path)
			println(cmd :: params)
			
//			os.proc(cmd :: params).call(
//				cwd = wd,
//				env = Map("TERM" -> "xterm-color"),
//				stdout = processOut,
//				stderr = processErr
//			)
			val proc = os.proc(cmd :: params).spawn(
				cwd = path,
				env = Map("TERM" -> "xterm"),
				stderr = processErr
			)
			println("here")
			for (i <- 0 until 10000) {
				val s = proc.stdout.data.readByte()
				println((s.toInt, s.toChar))
			}
		}
		catch {
			case e: os.SubprocessException => println(e.getMessage)
		}
		Response.Nothing[String]()
	}
}
