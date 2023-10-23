package terminal.stream

import akka.actor.ActorRef
import models.ResponseData
import terminal.stream.Streamer.Pipeline

import scala.annotation.tailrec

class Streamer(arguments: List[String], manager: ActorRef, session: Int) {
	
	val pipeline: Pipeline = StreamParamsParser.parse(arguments, manager, session)
	
	def to[T](data: ResponseData[T], filter: Boolean = true): Unit = {
		@tailrec
		def rec[U](cur: ResponseData[U], pipeline: Pipeline): Unit = pipeline match {
			case handler :: xs => handler.process(cur, filter) match {
				case Some(data) => rec(data, xs)
				case _ => _;
			}
			case _ => cur
		}
		rec(data, pipeline)
	}
}

object Streamer {
	type Pipeline = List[StreamHandler]
}
