package terminal.stream

import akka.actor.ActorRef
import models.Response
import terminal.stream.Streamer.Pipeline

import scala.annotation.tailrec

class Streamer(arguments: List[String], manager: ActorRef, session: Int) {
	
	val pipeline: Pipeline = StreamParamsParser.parse(arguments)
	
	def process(data: Response, filter: Boolean = true): Option[Response] = {
		@tailrec
		def rec(cur: Response, pipeline: Pipeline): Option[Response] = pipeline match {
			case handler :: xs => handler.process(cur, filter) match {
				case Some(data) => data match {
					case Response.Nothing() => None
					case _ => rec(data, xs)
				}
				case _ => None;
			}
			case _ => Some(cur)
		}
		rec(data, pipeline)
	}
	
	def send(res: Response, filter: Boolean = true): Unit = process(res, filter) match {
		case Some(res) => new SSEStreamHandler(manager, session).process(res, filter)
		case None => ;
	}
	
	def respond(res: Response, filter: Boolean = true): Response = process(res, filter) match {
		case Some(res) => res
		case None => Response.Nothing()
	}
}

object Streamer {
	type Pipeline = List[StreamHandler]
}
