package terminal.stream

import akka.actor.ActorRef

import scala.annotation.tailrec

class StreamParamsParser(manager: ActorRef) {

	private type SSEOutput = Boolean
	private type HandlerFactory = List[String] => LineStreamHandler
	
	private val SSEHandler = new SSEStreamHandler(manager)
	
	private def addHandler(
		                      handlers: List[LineStreamHandler], 
		                      handlerFactory: Option[HandlerFactory], 
		                      handlerParams: List[String]
	                      ): List[LineStreamHandler] = handlerFactory match {
		case Some(factory) => handlers :+ factory(handlerParams)
		case _ => handlers
	}
	
	@tailrec
	private def _parse(
		                  params: List[String],
		                  handlerFactory: Option[HandlerFactory] = None,
		                  handlerParams: List[String] = Nil,
		                  handlers: List[LineStreamHandler] = Nil,
		                  sseOutput: SSEOutput = true
	                  ): (List[LineStreamHandler], SSEOutput) =
		params match {
			case cmd :: rest => (cmd, rest) match {
				case (separator, x :: xs) if separator == "|" || separator == ">" =>
					val newHandlers = addHandler(handlers, handlerFactory, handlerParams)
					separator match {
						case "|" => x match {
							case "grep" => _parse(rest, Some(params => new Grep(params)), Nil, newHandlers, sseOutput);
							case _ => throw new Error("| needs to be followed by a stream handler command")
						}
						case ">" => _parse(rest, Some(params => new FileStreamHandler(params)), Nil, newHandlers, sseOutput=false)
					}
				case (param, _) => handlerFactory match {
					case Some(_) => _parse(rest, handlerFactory, handlerParams :+ param, handlers, sseOutput)
					case None => _parse(rest, handlerFactory, handlerParams, handlers)
				}
			}
			case _ =>
				val newHandlers = addHandler(handlers, handlerFactory, handlerParams)
				(newHandlers, sseOutput)
		}

	def parse(params: List[String]): List[LineStreamHandler] = _parse(params) match {
		case (handlers, true) => handlers ::: List(SSEHandler)
		case (handlers, _) => handlers
	}
}
