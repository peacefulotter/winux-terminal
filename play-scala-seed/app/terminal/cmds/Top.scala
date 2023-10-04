package terminal.cmds

import akka.actor.{ActorRef, ActorSystem, Cancellable}
import models.Response
import com.sun.management.OperatingSystemMXBean
import managers.ActorRefManager.SendResponse
import oshi.SystemInfo
import oshi.software.os.OperatingSystem

import java.lang.management.ManagementFactory
import oshi.util.FormatUtil

import java.text.SimpleDateFormat
import scala.concurrent.ExecutionContext
import scala.jdk.CollectionConverters._

class Top(manager: ActorRef, session: Int)(implicit system: ActorSystem, implicit val ec: ExecutionContext) extends Command {
	
	private val formatter: SimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
	private val bean = ManagementFactory.getPlatformMXBean(classOf[OperatingSystemMXBean])
	
	private def round2(n: Double): Double = math.round(n * 100) / 100.toDouble
	
	private def getProcesses(implicit os: OperatingSystem) =
		os.getProcesses(OperatingSystem.ProcessFiltering.VALID_PROCESS, OperatingSystem.ProcessSorting.CPU_DESC, 20).asScala
	
	private def retrieveData(): Unit = {
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
		val table: Seq[Map[String, String]] = currentProcesses.map(e => Map(
			"start_time" -> formatter.format(e._1.getStartTime),
			"process_id" -> e._1.getProcessID.toString,
			"name" -> e._1.getName,
			"RSS" -> FormatUtil.formatBytes(e._1.getResidentSetSize),
			"VS" -> FormatUtil.formatBytes(e._1.getVirtualSize),
			"CPU" -> round2(e._2).toString
		)).toSeq
		println(table)
		manager ! SendResponse(session, Response.Success(DataTable(table)))
	}
	
	def handle(params: List[String]): Response = {
		retrieveData()
//		val instance: Cancellable = system.scheduler.scheduleAtFixedRate(0.millisecond, 256.milliseconds)(() => retrieveData())
//		system.scheduler.scheduleOnce(5.seconds, new Runnable {
//			override def run(): Unit = instance.cancel()
//		})
		Response.Nothing()
	}
}
