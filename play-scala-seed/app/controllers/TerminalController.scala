package controllers

import akka.actor.{ActorRef, ActorSystem}
import akka.stream.{CompletionStrategy, OverflowStrategy}
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
import akka.Done
import com.fasterxml.jackson.databind.JsonNode
import models.Response
import os.Path

import javax.inject._
import scala.concurrent.ExecutionContext
import scala.util.Try

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
	private[this] val terminal = new Terminal(manager)(system, ec)
	
	private def getBody(f: (String, Path, Int, JsValue) => Response): Action[AnyContent] = Action { request =>
		try {
			request.body.asJson match {
				case Some(body) =>
					val (cmd, path, session) = (body("cmd").as[String], body("path").as[String], body("session").as[Int])
					println(s"CMD: $cmd, PATH: $path, SESSION: $session")
					val json = f(cmd, Path(path), session, body).json
					println(s"final json $json")
					Ok(json)
			}
		}
		catch {
			case e: Exception => Ok(new Response.Failure(e.getMessage).json)
		}
	}
	
	def cmd: Action[AnyContent] = getBody { (cmd, path, session, _) =>
		terminal.handleCommand(cmd, path, session)
	}
	
	def autocomplete: Action[AnyContent] = getBody { (cmd, path, _, _) =>
		terminal.autocomplete.handle(cmd, path)
	}
	
	def history: Action[AnyContent] = getBody { (_, _, _, json) => json("dir") match {
		case JsString("up") => terminal.history.arrowUp()
		case JsString("down") => terminal.history.arrowDown()
		case _ => new Response.Failure("dir param must either be 'up' or 'down'")
	} }
	
	def sse: Action[AnyContent] = Action {
		val bufferSize = 128
		val source: Source[String, ActorRef]#ReprMat[String, ActorRef] = Source.actorRef(
			{
				case Done => CompletionStrategy.immediately
			},
			PartialFunction.empty, // never fail the stream because of a message
			bufferSize,
			OverflowStrategy.dropHead
		).watchTermination() { case (actorRef, terminate) =>
			manager ! Register(actorRef)
			terminate.onComplete(_ => manager ! UnRegister(actorRef))
			actorRef
		}
		Ok.chunked(source via EventSource.flow).as(ContentTypes.EVENT_STREAM)
	}
}
