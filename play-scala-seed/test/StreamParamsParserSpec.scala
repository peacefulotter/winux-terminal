import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.testkit.TestProbe
import managers.ActorRefManager
import models.Response
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.JsNumber
import terminal.stream.{StreamParamsParser, Streamer}

import javax.inject.Inject

private class StreamParamsParserSpec @Inject()(implicit system: ActorSystem) extends PlaySpec {
	
	class TestManager extends Actor {
		import TestManager._
		private[this] val actor: Option[ActorRef] = None
		
		def receive: PartialFunction[Any, Unit] = onMessage(actor)
		
		private def onMessage(actor: Option[ActorRef]): Receive = {
			case SendResponse(session, res) => actor match {
				case Some(ref) =>
					val json = res.json + ("session" -> JsNumber(session))
					println(f"Manager sending res: ${json.toString()}")
			}
		}
	}
	
	object TestManager {
		def props: Props = Props[ActorRefManager]()
		case class SendResponse(session: Int, res: Response)
		case class Register(actorRef: ActorRef)
		case class UnRegister(actorRef: ActorRef)
	}
	"StreamParamsParser" should {
		"parse" in {
			val parent = TestProbe()
			val manager = parent.childActorOf(Props[ActorRefManager])
			
			val cmd = "cd ../ | grep cat" //  > file.txt
			val arguments = cmd.split(" ").toList
			val res = Response.Line("Is this working? Am I a cat? Omg omg cats everywhere")
			val pipeline = StreamParamsParser.parse(arguments)
			println(pipeline)
			val streamer = new Streamer(arguments, manager, session=0)
			streamer.send(res)
			val processed = streamer.respond(res)
			println(processed)
		}
	}
}