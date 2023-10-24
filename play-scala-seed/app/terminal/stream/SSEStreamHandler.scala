package terminal.stream

import akka.actor.ActorRef
import managers.ActorRefManager.SendResponse
import models.Response

class SSEStreamHandler(manager: ActorRef, session: Int) extends StreamHandler {
	override def process(res: Response, filter: Boolean = true): Option[Response] = {
		manager ! SendResponse(session, res)
		None
	}
}
