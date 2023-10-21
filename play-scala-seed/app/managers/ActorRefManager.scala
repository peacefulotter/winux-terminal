package managers;

import ActorRefManager.{Register, SendResponse, UnRegister}
import akka.actor.{Actor, ActorRef, Props}
import models.Response
import play.api.libs.json.JsNumber

class ActorRefManager extends Actor {
    private[this] val actor: Option[ActorRef] = None
  
    def receive: PartialFunction[Any, Unit] = onMessage(actor)
    
    private case class ActorNotSetException(private val message: String = "", private val cause: Throwable = None.orNull)
        extends Exception(message, cause)
  
    private def onMessage(actor: Option[ActorRef]): Receive = {
        case Register(actorRef) =>
            context.become(onMessage(Some(actorRef)))
        case UnRegister(_) =>
            context.become(onMessage(None))
        case SendResponse(session, res) => actor match {
            case Some(ref) =>
                val json = res.json + ("session" -> JsNumber(session))
                println(f"Manager sending res: ${json.toString()}")
                ref ! json.toString()
            case None =>
                throw ActorNotSetException("ActorRef is not set, refresh the page!")
        }
    }
}

object ActorRefManager {
  def props: Props = Props[ActorRefManager]()

  case class SendResponse(session: Int, res: Response)
  case class Register(actorRef: ActorRef)
  case class UnRegister(actorRef: ActorRef)
}