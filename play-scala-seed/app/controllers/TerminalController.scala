package controllers

import akka.actor.ActorSystem
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.Source
import managers.ActorRefManager
import managers.ActorRefManager.{Register, SendMessage, UnRegister}
import models.Message
import play.api.http.ContentTypes
import play.api.libs.EventSource
import play.api.mvc._
import play.filters.csrf.CSRFAddToken
import play.libs.Json
import terminal.Terminal

import javax.inject._
import scala.concurrent.ExecutionContext

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class TerminalController @Inject()(
	system: ActorSystem,
	cc: ControllerComponents
)(implicit ec: ExecutionContext) extends AbstractController(cc) {
	
	private[this] val manager = system.actorOf(ActorRefManager.props)
	private[this] val terminal = new Terminal()
	
	private def sendMessage: Runnable = new Runnable() {
		def run(): Unit = {
			val msg = "here I am a young man"// Message(...).toString
			manager ! SendMessage(msg)
		}
	}
	
	def get: Action[AnyContent] = Action { implicit request =>
		Ok("GET Got request [" + request + "]")
	}
	
	def cmd: Action[AnyContent] = Action { implicit request =>
		request.body.asJson match {
			case Some(json) => {
				val cmd = json("cmd").as[String]
				val res = terminal.handleCommand(cmd)
				Ok(Json.toJson(res))
			}
			case None => ;
		}
		Ok("POST request")
	}
	
	def sse: Action[AnyContent] = Action {
		val source = Source
		    .actorRef[String](128, OverflowStrategy.dropHead)
		    .watchTermination() { case (actorRef, terminate) =>
		      manager ! Register(actorRef)
		      terminate.onComplete(_ => manager ! UnRegister(actorRef))
		      actorRef
		    }
		Ok.chunked(source via EventSource.flow).as(ContentTypes.EVENT_STREAM)
	}
}
