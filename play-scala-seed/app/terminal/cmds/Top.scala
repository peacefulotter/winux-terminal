package terminal.cmds

import akka.actor.{ActorRef, ActorSystem, Cancellable}
import models.Response
import com.sun.management.OperatingSystemMXBean
import managers.ActorRefManager.SendResponse
import org.joda.time.format.FormatUtils
import oshi.SystemInfo
import oshi.software.os.OperatingSystem
import oshi.util.FormatUtil

import java.lang.management.ManagementFactory
import java.text.SimpleDateFormat
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt
import scala.jdk.CollectionConverters._

class Top(manager: ActorRef, session: Int)(implicit system: ActorSystem, implicit val ec: ExecutionContext) extends Command {
	
	private val formatter: SimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
	private val bean = ManagementFactory.getPlatformMXBean(classOf[OperatingSystemMXBean])
	
	private def round2(n: Double): Double = math.round(n * 100) / 100.toDouble
	
	private def getProcesses(implicit os: OperatingSystem) =
		os.getProcesses(OperatingSystem.ProcessFiltering.VALID_PROCESS, OperatingSystem.ProcessSorting.CPU_DESC, 20).asScala
	

	private def retrieveData(): Response = {
		val si = new SystemInfo
		implicit val os: OperatingSystem = si.getOperatingSystem
		
		val prevCpuTime = getProcesses
			.map(p => (p.getProcessID, (p.getKernelTime + p.getUserTime, System.nanoTime())))
			.toMap
		
		Thread.sleep(512)
		
		val currentProcesses = getProcesses
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
		
		println(s"CPU Load:  process ${bean.getProcessCpuLoad}%, system ${bean.getSystemCpuLoad}%")
		println("TOTAL: " + currentProcesses.map { _._2 }.sum)
		val table = currentProcesses.map(e => Map(
			"start_time" -> (e._1.getStartTime, formatter.format(e._1.getStartTime)),
			"process_id" -> (e._1.getProcessID, e._1.getProcessID.toString),
			"name" -> e._1.getName,
			"RSS" -> (e._1.getResidentSetSize, FormatUtil.formatBytes(e._1.getResidentSetSize)),
			"VS" -> (e._1.getVirtualSize, FormatUtil.formatBytes(e._1.getVirtualSize)),
			"CPU" -> (round2(e._2), round2(e._2).toString)
		)).toSeq
		Response.Success(DataTable(Map(
			"table" -> table,
			"keys" -> table.head.keySet.toSeq
		)), replace = true)
	}
	
	def handle(params: List[String]): Response = {
		val instance: Cancellable = system.scheduler.scheduleAtFixedRate(256.millisecond, 256.milliseconds)(() => {
			val res = retrieveData()
			manager ! SendResponse(session, res)
		})
		system.scheduler.scheduleOnce(5.seconds, new Runnable {
			override def run(): Unit = instance.cancel()
		})
		retrieveData()
	}
}
