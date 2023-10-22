package controllers

import org.scalatestplus.play.PlaySpec
import oshi.SystemInfo
import oshi.software.os.OperatingSystem
import oshi.util.FormatUtil
import terminal.stream.StreamParamsParser

import scala.jdk.CollectionConverters._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 *
 * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 */
class StreamParamsParserSpec extends PlaySpec {
	"StreamParamsParser" should {
		"parse" in {
			val parser = new StreamParamsParser(null)
			val cmd = "cd ../ | grep cat > file.txt".split(" ").toList
			val handlers = parser.parse(cmd)
			println(handlers)
		}
	}
}
