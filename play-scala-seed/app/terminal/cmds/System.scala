package terminal.cmds

import models.Response
import oshi.SystemInfo
import oshi.util.FormatUtil
import terminal.colors.Ansi.{BOLD, BRIGHT_BLUE, BRIGHT_YELLOW, RESET}

import scala.jdk.CollectionConverters._

class System(implicit params: Command.Params) extends Command {
	def handle(): Response = {
		val si = new SystemInfo()
		val os = si.getOperatingSystem
		val hal = si.getHardware
		val mem = hal.getMemory
		val cpu = hal.getProcessor
		val sys = hal.getComputerSystem
		val vm = mem.getVirtualMemory
		
		val gpus = hal.getGraphicsCards.asScala.zipWithIndex.map { case (gpu, i) =>
			s"$BRIGHT_YELLOW[$i]$RESET ${gpu.getName}, VRAM: ${FormatUtil.formatBytes(gpu.getVRam)}"
		}
		val disks = hal.getDiskStores.asScala.zipWithIndex.map { case (disk, i) =>
			s"$BRIGHT_YELLOW[$i]$RESET ${disk.getModel}, ${FormatUtil.formatBytes(disk.getSize)}"
		}
		
		Response.List(List(
			s"$BRIGHT_BLUE${BOLD}Manufacturer$RESET: ${sys.getManufacturer}, Model: ${sys.getModel}, Released: ${sys.getFirmware.getReleaseDate}",
			s"$BRIGHT_BLUE${BOLD}Operating System$RESET: ${os.getManufacturer} ${os.getFamily} ${os.getVersionInfo}",
			s"$BRIGHT_BLUE${BOLD}Virtual Mem$RESET: ${FormatUtil.formatBytes(vm.getVirtualInUse)} / ${FormatUtil.formatBytes(vm.getVirtualMax)}",
			s"$BRIGHT_BLUE${BOLD}Virtual Swap$RESET: ${FormatUtil.formatBytes(vm.getSwapUsed)} / ${FormatUtil.formatBytes(vm.getSwapTotal)}",
			s"$BRIGHT_BLUE${BOLD}Physical Mem$RESET: ${FormatUtil.formatBytes(mem.getTotal - mem.getAvailable)} / ${FormatUtil.formatBytes(mem.getTotal)}",
			s"$BRIGHT_BLUE${BOLD}CPU$RESET:",
			s"\t${cpu.getProcessorIdentifier.getName} ${cpu.getProcessorIdentifier.getModel} ${cpu.getProcessorIdentifier.getMicroarchitecture}",
			s"\tProcessors: ${cpu.getLogicalProcessorCount} physical, ${cpu.getPhysicalProcessorCount} logical",
			s"\tMax frequency: ${FormatUtil.formatHertz(cpu.getMaxFreq)}",
			s"$BRIGHT_BLUE${BOLD}GPU(s)$RESET:",
			s"\t${gpus.mkString("\n\t")}",
			s"$BRIGHT_BLUE${BOLD}Disk(s)$RESET:",
			s"\t${disks.mkString("\n\t")}",
		))
	}
}

