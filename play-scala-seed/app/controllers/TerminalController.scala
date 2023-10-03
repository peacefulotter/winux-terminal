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
	
	private def getBody(f: (String, Path, JsValue) => Response)(implicit request: Request[AnyContent]): Result =
		request.body.asJson match {
			case Some(body) =>
				val (cmd, path) = (body("cmd").as[String], body("path").as[String])
				println(s"CMD: $cmd, PATH: $path")
				val json = f(cmd, Path(path), body).toJson
				println(s"final json $json")
				Ok(json)
			case None =>
				Ok("No json body passed")
		}
	
	def cmd: Action[AnyContent] = Action { implicit request =>
		getBody { (cmd, path, _) => terminal.handleCommand(cmd, path) }
	}
	
	def autocomplete: Action[AnyContent] = Action { implicit request =>
		getBody { (cmd, path, _) =>
			terminal.autocomplete.handle(cmd, path)
		}
	}
	
	def history: Action[AnyContent] = Action { implicit request =>
		getBody { (_, _, json) => json("dir") match {
			case JsString("up") => terminal.history.arrowUp()
			case JsString("down") => terminal.history.arrowDown()
			case _ => Response.Failure("dir param must either be 'up' or 'down'")
		} }
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
