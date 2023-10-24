import akka.actor.testkit.typed.scaladsl.ActorTestKit
import akka.actor.typed.ActorSystem
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.play.PlaySpec

class TestService
	extends PlaySpec
		with BeforeAndAfterAll
		with Matchers {
	val testKit: ActorTestKit = ActorTestKit()
	implicit val system: ActorSystem[_] = testKit.system
	
	override def afterAll(): Unit = testKit.shutdownTestKit()
}
