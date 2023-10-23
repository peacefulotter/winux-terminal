package terminal.stream

import akka.actor.ActorRef
import managers.ActorRefManager.SendResponse
import models.{Response, ResponseData}

class SSEStreamHandler(manager: ActorRef, session: Int) extends StreamHandler {
	override def process(data: ResponseData[_], filter: Boolean = true): Option[ResponseData[_]] = {
		val res = Response.Success(data)
		manager ! SendResponse(session, res)
		None
	}
}
