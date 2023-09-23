package managers;

import ActorRefManager.{Register, SendResponse, UnRegister}
import akka.actor.{Actor, ActorRef, Props}
import models.Response

class ActorRefManager extends Actor {
    private[this] val actor: Option[ActorRef] = None
  
    def receive: PartialFunction[Any, Unit] = onMessage(actor)
  
    private def onMessage(actor: Option[ActorRef]): Receive = {
        case Register(actorRef) => context.become(onMessage(Some(actorRef)))
        case UnRegister(_) => context.become(onMessage(None))
        case SendResponse(res) => actor match {
            case Some(ref) =>
                println(res.toJson.toString())
                ref ! res.toJson.toString()
            case None => ;
        }
    }
}

object ActorRefManager {
  def props: Props = Props[ActorRefManager]

  case class SendResponse(res: Response)
  case class Register(actorRef: ActorRef)
  case class UnRegister(actorRef: ActorRef)
}