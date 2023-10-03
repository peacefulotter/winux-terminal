package controllers

import org.scalatestplus.play.PlaySpec
import oshi.SystemInfo
import oshi.software.os.OperatingSystem
import oshi.util.FormatUtil

import scala.jdk.CollectionConverters._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 *
 * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 */
class HomeControllerSpec extends PlaySpec {
	private def round2(n: Double): Double = math.round(n * 100) / 100

	def test() = {
		val si = new SystemInfo
		val os = si.getOperatingSystem
		
		val prevCpuTime = os.getProcesses(OperatingSystem.ProcessFiltering.VALID_PROCESS, OperatingSystem.ProcessSorting.CPU_DESC, 20)
			.asScala
			.map(p => (p.getProcessID, (p.getKernelTime + p.getUserTime, System.nanoTime())))
			.toMap
		
		Thread.sleep(512)
		
		val currentProcesses = os.getProcesses(OperatingSystem.ProcessFiltering.VALID_PROCESS, OperatingSystem.ProcessSorting.CPU_DESC, 20)
			.asScala
			.filter(p => "Idle" != p.getName && prevCpuTime.contains(p.getProcessID))
			.map(p => {
				val cpu = prevCpuTime.get(p.getProcessID) match {
					case Some((prevTime, prevNano)) =>
						val curTime = p.getKernelTime + p.getUserTime;
						val elapsed = (System.nanoTime() - prevNano) / 1_000_000.toDouble;
						val timeDif = curTime - prevTime
						100.toDouble * timeDif / elapsed;
					case None =>
						0.toDouble
				}
				(p, cpu)
			})
			.sortBy { -_._2 }
		
		println("TOTAL: " + currentProcesses.map { _._2 }.sum)
		println( currentProcesses.map( e => s"${e._1.getName}: ${round2(e._2)}%  ${e._1.getProcessID}, ${e._1.getStartTime}, ${e._1.getResidentSetSize}, ${e._1.getVirtualSize}") )
	}
	
	"GenresController GET" should {
		
		"test" in {
			var i = 0;
			while (i < 50) {
				test()
				Thread.sleep(256)
				i += 1
			}
		}
		
		"fetch genres in proper format" in {
			val si = new SystemInfo()
			val os = si.getOperatingSystem
			val osInfo = s"${os.getManufacturer} ${os.getFamily} ${os.getVersionInfo}"
			println(osInfo)
			println(SystemInfo.getCurrentPlatform)
			
			println(os.getProcessCount)
			println(os.getServices.asScala.map(s => (s.getName, s.getState, s.getProcessID)).mkString("\n"))
			
			val hal = si.getHardware
			val mem = hal.getMemory
			println(FormatUtil.formatBytes(mem.getVirtualMemory.getVirtualMax))
			println(FormatUtil.formatBytes(mem.getVirtualMemory.getVirtualInUse))
			println(FormatUtil.formatBytes(mem.getVirtualMemory.getSwapTotal))
			println(FormatUtil.formatBytes(mem.getVirtualMemory.getSwapUsed))
			println(FormatUtil.formatBytes(mem.getPhysicalMemory.asScala.map(_.getCapacity).sum))
			println(FormatUtil.formatBytes(mem.getTotal), mem.getTotal)
			println(FormatUtil.formatBytes(mem.getAvailable),  mem.getAvailable)
			println("=====")
			println(hal.getProcessor.getLogicalProcessorCount)
			println(hal.getProcessor.getSystemCpuLoad(200))
			println("=====")
			val sys = hal.getComputerSystem
			val a = s"Manufacturer: ${sys.getManufacturer}, Model: ${sys.getModel}, Released: ${sys.getFirmware.getReleaseDate}"
			val gpus = hal.getGraphicsCards.asScala.map(gpu =>
				s"Name: ${gpu.getName}, VRAM: ${FormatUtil.formatBytes(gpu.getVRam)}"
			)
			println(gpus)
			println(hal.getDiskStores.asScala.map(d => (d.getModel, FormatUtil.formatBytes(d.getSize))))
		}
	}
}
