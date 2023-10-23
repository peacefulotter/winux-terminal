package terminal.stream

import akka.actor.ActorRef

import scala.annotation.tailrec

object StreamParamsParser {

	private type SSEOutput = Boolean
	private type Pipeline = Streamer.Pipeline
	private type HandlerFactory = List[String] => StreamHandler
	private case class HandlerManager(pipeline: Pipeline = Nil,
	                                  factory: Option[HandlerFactory] = None,
	                                  params: List[String] = Nil)
	
	private def addHandler(handler: HandlerManager, newFactory: HandlerFactory): HandlerManager = HandlerManager(
		handler.factory match {
			case Some(factory) => handler.pipeline :+ factory(handler.params)
			case _ => handler.pipeline
		},
		Some(newFactory),
		Nil
	)
	
	private def addHandlerParam(handler: HandlerManager, param: String): HandlerManager = HandlerManager(
		handler.pipeline,
		handler.factory,
		handler.params :+ param
	)
	
	@tailrec
	private def _parse(
		                  arguments: List[String],
		                  handler: HandlerManager = HandlerManager(),
		                  sseOutput: SSEOutput = true
	                  ): (Pipeline, SSEOutput) =
		arguments match {
			case cmd :: args => (cmd, args) match {
				case (separator, x :: xs) if separator == "|" || separator == ">" =>
					separator match {
						case "|" => x match {
							case "grep" => _parse(args, addHandler(handler, new Grep(_)), sseOutput);
							case _ => throw new Error("| needs to be followed by a stream handler command")
						}
						case ">" => _parse(args, addHandler(handler, new FileStreamHandler(_)), sseOutput=false)
					}
				case (param, _) => handler.factory match {
					case Some(_) => _parse(args, addHandlerParam(handler, param), sseOutput)
					case None => _parse(args, handler)
				}
			}
			case _ =>
				(addHandler(handler, null).pipeline, sseOutput)
		}

	def parse(arguments: List[String], manager: ActorRef, session: Int): Pipeline = _parse(arguments) match {
		case (handlers, true) =>
			val SSEHandler = new SSEStreamHandler(manager, session)
			handlers ::: List(SSEHandler)
		case (handlers, _) => handlers
	}
}
