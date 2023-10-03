package terminal.cmds

import models.Response
import os.Path
import terminal.helpers.PathHelper

import scala.util.{Failure, Success}
import com.sun.management.OperatingSystemMXBean
import oshi.SystemInfo

import java.lang.management.ManagementFactory
import java.time.format.DateTimeFormatter
import java.time.{Instant, LocalDateTime, ZoneId};

class Top extends Command {
	
	private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
	private val bean = ManagementFactory.getPlatformMXBean(classOf[OperatingSystemMXBean])
	
	private def formatDouble(d: Double) = math.round(d * 1000) / 1000.doubleValue * 100
	private def formatMem(mem: Long) = math.round(mem / 10_000_000.doubleValue) / 100.doubleValue
	
	def handle(params: List[String]): Response = {
		Response.Success(DataList(List(
		s"CPU Load:  process ${formatDouble(bean.getProcessCpuLoad)}%, system ${formatDouble(bean.getSystemCpuLoad)}%",
		s"Phys MEM:  free ${formatMem(bean.getFreePhysicalMemorySize)}G, total: ${formatMem(bean.getTotalPhysicalMemorySize)}G",
		s"Phys SWAP: free ${formatMem(bean.getFreeSwapSpaceSize)}G, total: ${formatMem(bean.getTotalSwapSpaceSize)}G",
		s"arch ${bean.getArch}, processors: ${bean.getAvailableProcessors}",
	)))
	}
}
