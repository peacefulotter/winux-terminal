package controllers

import akka.actor.ActorSystem
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.Source
import managers.ActorRefManager
import managers.ActorRefManager.{Register, UnRegister}
import play.api.http.ContentTypes
import play.api.libs.EventSource
import play.api.libs.json._
import play.api.mvc._
import play.libs.Json
import terminal.Terminal
import WritableImplicits._
import com.fasterxml.jackson.databind.JsonNode
import models.Response
import os.Path

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
	private[this] val terminal = new Terminal(manager)
	
	// manager ! SendResponse(response)
	
	def get: Action[AnyContent] = Action { implicit request =>
		Ok("GET Got request [" + request + "]")
	}
	
	private def getBody(f: (String, Path) => Response)(implicit request: Request[AnyContent]): Result =
		request.body.asJson match {
			case Some(body) =>
				val (cmd, path) = (body("cmd").as[String], body("path").as[String])
				println(s"$cmd, $path")
				val json = f(cmd, Path(path)).toJson
				println(s"final json $json")
				Ok(json)
			case None =>
				Ok("No json body passed")
		}
	
	def cmd: Action[AnyContent] = Action { implicit request =>
		getBody { (cmd, path) => terminal.handleCommand(cmd, path) }
	}
	
	def autocomplete: Action[AnyContent] = Action { implicit request =>
		getBody { (cmd, path) =>
			terminal.autocomplete.handle(cmd, path)
		}
	}
	
	def up: Action[AnyContent] = Action { implicit request =>
		getBody { (_, _) => terminal.history.arrowUp() }
	}
	
	def down: Action[AnyContent] = Action { implicit request =>
		getBody { (_, _) => terminal.history.arrowDown() }
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
