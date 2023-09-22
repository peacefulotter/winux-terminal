package controllers

import akka.actor.ActorSystem
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.Source
import managers.ActorRefManager
import managers.ActorRefManager.{Register, SendMessage, UnRegister}
import play.api.http.ContentTypes
import play.api.libs.EventSource
import play.api.libs.json._
import play.api.mvc._
import play.libs.Json
import terminal.Terminal
import terminal.cmds.Response
import WritableImplicits._
import com.fasterxml.jackson.databind.JsonNode
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
	
	private def resToJson(res: Response[_]): JsObject = res match {
		case Response.Success(data) => JsObject(Seq(
			"status" -> JsNumber(res.status.id),
			"name" -> JsString(data.name),
			"data" -> data.json
		))
		case Response.Failure(msg) => JsObject(Seq(
			"status" -> JsNumber(res.status.id),
			"data" -> JsString(msg)
		))
		case res: Response.Nothing[_] => JsObject(Seq(
			"status" -> JsNumber(res.status.id),
		))
	}
	
	private def getBody(f: (String, Path) => Response[_])(implicit request: Request[AnyContent]): Result =
		request.body.asJson match {
			case Some(body) =>
				val (cmd, path) = (body("cmd").as[String], body("path").as[String])
				println(s"$cmd, $path")
				val res = f(cmd, Path(path))
				val json = resToJson(res)
				println(s"final json $json")
				Ok(json)
			case None =>
				Ok("No json body passed")
		}
	
	def cmd: Action[AnyContent] = Action { implicit request =>
		getBody { (cmd, _) => terminal.handleCommand(cmd) }
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
