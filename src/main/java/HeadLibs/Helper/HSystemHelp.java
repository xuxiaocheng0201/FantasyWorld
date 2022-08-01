package HeadLibs.Helper;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.lang.management.*;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Some tools about system.
 */
public class HSystemHelp {
    /**
     * Get jvm bit.
     * @return true - 64bit. false - 32bit
     */
    public static boolean isJVM64Bit() {
        String temp = System.getProperty("sun.arch.data.model");
        if (temp != null && temp.contains("64"))
            return true;
        temp = System.getProperty("os.arch");
        if (temp != null && temp.contains("64"))
            return true;
        temp = System.getProperty("com.ibm.vm.bitmode");
        return (temp != null && temp.contains("64"));
    }

    /**
     * Get running machine's type.
     * @return machine's type
     */
    public static @NotNull String getRunningType() {
        String temp = System.getProperty("os.name").toLowerCase();
        if (temp.contains("win"))
            return "WINDOWS";
        if (temp.contains("mac"))
            return "OSX";
        if (temp.contains("solaris"))
            return "SOLARIS";
        if (temp.contains("sunos"))
            return "SUNOS";
        if (temp.contains("linux"))
            return "LINUX";
        if (temp.contains("unix"))
            return "UNIX";
        return "UNKNOWN";
    }

    /**
     * Log system details.
     * @param output log stream
     * @throws IOException log error
     */
    @SuppressWarnings("MagicNumber")
    public static void outputSystemDetail(@NotNull Writer output) throws IOException {
        output.write("System Detail:\n");
        output.write(HStringHelper.concat("\tOperating Machine: ", getRunningType(), "\n"));
        output.write(HStringHelper.concat("\tConsole: ", System.console(), "\n"));
        output.write("\tMemory:\n");
        output.write(HStringHelper.concat("\t\t\tMax Memory: ", Runtime.getRuntime().maxMemory(), "Bytes (", Runtime.getRuntime().maxMemory() / 1024 / 1024, "MB)\n"));
        output.write(HStringHelper.concat("\t\t\tTotal Memory: ", Runtime.getRuntime().totalMemory(), "Bytes (", Runtime.getRuntime().totalMemory() / 1024 / 1024, "MB)\n"));
        output.write(HStringHelper.concat("\t\t\tFree Memory: ", Runtime.getRuntime().freeMemory(), "Bytes (", Runtime.getRuntime().freeMemory() / 1024 / 1024, "MB)\n"));
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        output.write("\tHeap:\n");
        MemoryUsage heapMemory = memoryMXBean.getHeapMemoryUsage();
        output.write("\t\tHeap Memory Usage:\n");
        output.write(HStringHelper.concat("\t\t\tInit Heap: ", heapMemory.getInit(), "Bytes (", heapMemory.getInit() / 1024 / 1024, "MB)\n"));
        output.write(HStringHelper.concat("\t\t\tMax Heap: ", heapMemory.getMax(), "Bytes (", heapMemory.getMax() / 1024 / 1024, "MB)\n"));
        output.write(HStringHelper.concat("\t\t\tUsed Heap: ", heapMemory.getUsed(), "Bytes (", heapMemory.getUsed() / 1024 / 1024, "MB)\n"));
        output.write(HStringHelper.concat("\t\t\tCommitted Heap: ", heapMemory.getCommitted(), "Bytes (", heapMemory.getCommitted() / 1024 / 1024, "MB)\n"));
        MemoryUsage nonHeapMemory= memoryMXBean.getNonHeapMemoryUsage();
        output.write("\t\tNon-Heap Memory Usage:\n");
        output.write(HStringHelper.concat("\t\t\tInit Heap: ", nonHeapMemory.getInit(), "Bytes (", nonHeapMemory.getInit() / 1024 / 1024, "MB)\n"));
        output.write(HStringHelper.concat("\t\t\tMax Heap: ", nonHeapMemory.getMax(), "Bytes (", nonHeapMemory.getMax() / 1024 / 1024, "MB)\n"));
        output.write(HStringHelper.concat("\t\t\tUsed Heap: ", nonHeapMemory.getUsed(), "Bytes (", nonHeapMemory.getUsed() / 1024 / 1024, "MB)\n"));
        output.write(HStringHelper.concat("\t\t\tCommitted Heap: ", nonHeapMemory.getCommitted(), "Bytes (", nonHeapMemory.getCommitted() / 1024 / 1024, "MB)\n"));
        output.write(HStringHelper.concat("\t\tVerbose", memoryMXBean.isVerbose(), "\n"));
        output.write(HStringHelper.concat("\t\tObject Pending Finalization Count", memoryMXBean.getObjectPendingFinalizationCount(), "\n"));
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        output.write("\tRuntime:\n");
        output.write(HStringHelper.concat("\t\tPid: ", runtimeMXBean.getPid(), "\n"));
        output.write(HStringHelper.concat("\t\tName: ", runtimeMXBean.getName(), "\n"));
        output.write(HStringHelper.concat("\t\tJVM Name: ", runtimeMXBean.getVmName(), "\n"));
        output.write(HStringHelper.concat("\t\tJVM Vendor: ", runtimeMXBean.getVmVendor(), "\n"));
        output.write(HStringHelper.concat("\t\tJVM Version: ", runtimeMXBean.getVmVersion(), "\n"));
        output.write(HStringHelper.concat("\t\tSpec Name: ", runtimeMXBean.getSpecName(), "\n"));
        output.write(HStringHelper.concat("\t\tSpec Vendor: ", runtimeMXBean.getSpecVendor(), "\n"));
        output.write(HStringHelper.concat("\t\tSpec Version: ", runtimeMXBean.getSpecVersion(), "\n"));
        output.write(HStringHelper.concat("\t\tManagement Spec Version: ", runtimeMXBean.getManagementSpecVersion(), "\n"));
        output.write(HStringHelper.concat("\t\tClass Path: ", runtimeMXBean.getClassPath(), "\n"));
        output.write(HStringHelper.concat("\t\tLibrary Path: ", runtimeMXBean.getLibraryPath(), "\n"));
        output.write(HStringHelper.concat("\t\tBoot Class Path Supported: ", runtimeMXBean.isBootClassPathSupported(), "\n"));
        if (runtimeMXBean.isBootClassPathSupported())
            output.write(HStringHelper.concat("\t\tBoot Class Path: ", runtimeMXBean.getBootClassPath(), "\n"));
        output.write(HStringHelper.concat("\t\tInput Arguments: ", runtimeMXBean.getInputArguments(), "\n"));
        output.write(HStringHelper.concat("\t\tUptime: ", runtimeMXBean.getUptime(), "\n"));
        output.write(HStringHelper.concat("\t\tStart Time: ", runtimeMXBean.getStartTime(), "(", new Date(runtimeMXBean.getStartTime()), ")\n"));
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        output.write("\tOperating System:\n");
        output.write(HStringHelper.concat("\t\tName: ", operatingSystemMXBean.getName(), "\n"));
        output.write(HStringHelper.concat("\t\tArch: ", operatingSystemMXBean.getArch(), "\n"));
        output.write(HStringHelper.concat("\t\tVersion: ", operatingSystemMXBean.getVersion(), "\n"));
        output.write(HStringHelper.concat("\t\tAvailable Processors: ", operatingSystemMXBean.getAvailableProcessors(), "\n"));
        output.write(HStringHelper.concat("\t\tSystem Load Average: ", operatingSystemMXBean.getSystemLoadAverage(), "\n"));
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        output.write("\tThread:\n");
        output.write(HStringHelper.concat("\t\tThread Count: ", threadMXBean.getThreadCount(), "\n"));
        output.write(HStringHelper.concat("\t\tPeak Thread Count: ", threadMXBean.getPeakThreadCount(), "\n"));
        output.write(HStringHelper.concat("\t\tTotal Started Thread Count: ", threadMXBean.getTotalStartedThreadCount(), "\n"));
        output.write(HStringHelper.concat("\t\tDaemon Thread Count: ", threadMXBean.getDaemonThreadCount(), "\n"));
        output.write(HStringHelper.concat("\t\tThread Contention Monitoring Supported: ", threadMXBean.isThreadContentionMonitoringSupported(), "\n"));
        if (threadMXBean.isThreadContentionMonitoringSupported())
            output.write(HStringHelper.concat("\t\tThread Contention Monitoring Enabled: ", threadMXBean.isThreadContentionMonitoringEnabled(), "\n"));
        output.write(HStringHelper.concat("\t\tCurrent Thread Cpu Time Supported: ", threadMXBean.isCurrentThreadCpuTimeSupported(), "\n"));
        if (threadMXBean.isCurrentThreadCpuTimeSupported())
            output.write(HStringHelper.concat("\t\tCurrent Thread Cpu Time: ", threadMXBean.getCurrentThreadCpuTime(), "\n"));
        output.write(HStringHelper.concat("\t\tCurrent Thread User Time: ", threadMXBean.getCurrentThreadUserTime(), "\n"));
        output.write(HStringHelper.concat("\t\tThread Cpu Time Supported: ", threadMXBean.isThreadCpuTimeSupported(), "\n"));
        if (threadMXBean.isThreadCpuTimeSupported())
            output.write(HStringHelper.concat("\t\tThread Cpu Time Enabled: ", threadMXBean.isThreadCpuTimeEnabled(), "\n"));
        output.write(HStringHelper.concat("\t\tObject Monitor Usage Supported: ", threadMXBean.isObjectMonitorUsageSupported(), "\n"));
        output.write(HStringHelper.concat("\t\tSynchronizer Usage Supported: ", threadMXBean.isSynchronizerUsageSupported(), "\n"));
        CompilationMXBean compilationMXBean = ManagementFactory.getCompilationMXBean();
        output.write("\tCompilation:\n");
        output.write(HStringHelper.concat("\t\tName: ", compilationMXBean.getName(), "\n"));
        output.write(HStringHelper.concat("\t\tCompilation Time Monitoring Supported: ", compilationMXBean.isCompilationTimeMonitoringSupported(), "\n"));
        output.write(HStringHelper.concat("\t\tTotal Compilation Time: ", compilationMXBean.getTotalCompilationTime(), "\n"));
        List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
        output.write("\tMemory Pool MX Beans:\n");
        for (MemoryPoolMXBean memoryPoolMXBean: memoryPoolMXBeans) {
            output.write(HStringHelper.concat("\t\tName: ", memoryPoolMXBean.getName(), "\n"));
            output.write(HStringHelper.concat("\t\t\tType: ", memoryPoolMXBean.getType(), "\n"));
            MemoryUsage memoryPoolUsage = memoryPoolMXBean.getUsage();
            output.write("\t\t\tUsage:\n");
            output.write(HStringHelper.concat("\t\t\t\tInit Heap: ", memoryPoolUsage.getInit(), "Bytes (", memoryPoolUsage.getInit() / 1024 / 1024, "MB)\n"));
            output.write(HStringHelper.concat("\t\t\t\tMax Heap: ", memoryPoolUsage.getMax(), "Bytes (", memoryPoolUsage.getMax() / 1024 / 1024, "MB)\n"));
            output.write(HStringHelper.concat("\t\t\t\tUsed Heap: ", memoryPoolUsage.getUsed(), "Bytes (", memoryPoolUsage.getUsed() / 1024 / 1024, "MB)\n"));
            output.write(HStringHelper.concat("\t\t\t\tCommitted Heap: ", memoryPoolUsage.getCommitted(), "Bytes (", memoryPoolUsage.getCommitted() / 1024 / 1024, "MB)\n"));
            MemoryUsage memoryPoolPeakUsage = memoryPoolMXBean.getPeakUsage();
            output.write("\t\t\tPeak Usage:\n");
            output.write(HStringHelper.concat("\t\t\t\tInit Heap: ", memoryPoolPeakUsage.getInit(), "Bytes (", memoryPoolPeakUsage.getInit() / 1024 / 1024, "MB)\n"));
            output.write(HStringHelper.concat("\t\t\t\tMax Heap: ", memoryPoolPeakUsage.getMax(), "Bytes (", memoryPoolPeakUsage.getMax() / 1024 / 1024, "MB)\n"));
            output.write(HStringHelper.concat("\t\t\t\tUsed Heap: ", memoryPoolPeakUsage.getUsed(), "Bytes (", memoryPoolPeakUsage.getUsed() / 1024 / 1024, "MB)\n"));
            output.write(HStringHelper.concat("\t\t\t\tCommitted Heap: ", memoryPoolPeakUsage.getCommitted(), "Bytes (", memoryPoolPeakUsage.getCommitted() / 1024 / 1024, "MB)\n"));
            output.write(HStringHelper.concat("\t\t\tMemory Manager Names: ", memoryPoolMXBean.getMemoryManagerNames(), "\n"));
            output.write(HStringHelper.concat("\t\t\tUsage Threshold Supported: ", memoryPoolMXBean.isUsageThresholdSupported(), "\n"));
            if (memoryPoolMXBean.isUsageThresholdSupported()) {
                output.write(HStringHelper.concat("\t\t\tUsage Threshold: ", memoryPoolMXBean.getUsageThreshold(), "\n"));
                output.write(HStringHelper.concat("\t\t\tUsage Threshold Exceeded: ", memoryPoolMXBean.isUsageThresholdExceeded(), "\n"));
                output.write(HStringHelper.concat("\t\t\tUsage Threshold Count: ", memoryPoolMXBean.getUsageThresholdCount(), "\n"));
            }
            output.write(HStringHelper.concat("\t\t\tCollection Usage Threshold Supported: ", memoryPoolMXBean.isCollectionUsageThresholdSupported(), "\n"));
            if (memoryPoolMXBean.isCollectionUsageThresholdSupported()) {
                output.write(HStringHelper.concat("\t\t\tCollection Usage Threshold: ", memoryPoolMXBean.getCollectionUsageThreshold(), "\n"));
                output.write(HStringHelper.concat("\t\t\tCollection Usage Threshold Exceeded: ", memoryPoolMXBean.isCollectionUsageThresholdExceeded(), "\n"));
                output.write(HStringHelper.concat("\t\t\tCollection Usage Threshold Count: ", memoryPoolMXBean.getCollectionUsageThresholdCount(), "\n"));
                MemoryUsage memoryPoolCollectionUsage = memoryPoolMXBean.getCollectionUsage();
                output.write("\t\t\tCollection Usage:\n");
                output.write(HStringHelper.concat("\t\t\t\tInit Heap: ", memoryPoolCollectionUsage.getInit(), "Bytes (", memoryPoolCollectionUsage.getInit() / 1024 / 1024, "MB)\n"));
                output.write(HStringHelper.concat("\t\t\t\tMax Heap: ", memoryPoolCollectionUsage.getMax(), "Bytes (", memoryPoolCollectionUsage.getMax() / 1024 / 1024, "MB)\n"));
                output.write(HStringHelper.concat("\t\t\t\tUsed Heap: ", memoryPoolCollectionUsage.getUsed(), "Bytes (", memoryPoolCollectionUsage.getUsed() / 1024 / 1024, "MB)\n"));
                output.write(HStringHelper.concat("\t\t\t\tCommitted Heap: ", memoryPoolCollectionUsage.getCommitted(), "Bytes (", memoryPoolCollectionUsage.getCommitted() / 1024 / 1024, "MB)\n"));
            }
        }
        List<GarbageCollectorMXBean> garbageCollectorMXBeans = ManagementFactory.getGarbageCollectorMXBeans();
        output.write("\tGarbage Collector MX Beans:\n");
        for (GarbageCollectorMXBean garbageCollectorMXBean: garbageCollectorMXBeans) {
            output.write(HStringHelper.concat("\t\tName: ", garbageCollectorMXBean.getName(), "\n"));
            output.write(HStringHelper.concat("\t\t\tCollection Count: ", garbageCollectorMXBean.getCollectionCount(), "\n"));
            output.write(HStringHelper.concat("\t\t\tCollection Time: ", garbageCollectorMXBean.getCollectionTime(), "\n"));
        }
        File[] disks = File.listRoots();
        output.write("\tDisks:\n");
        for (File disk: disks) {
            output.write(HStringHelper.concat("\t\tPath: ", disk.getPath(), "\n"));
            output.write(HStringHelper.concat("\t\t\tTotal Space: ", disk.getTotalSpace(), "Bytes (", disk.getTotalSpace() / 1024 / 1024 / 1024, "GB\n"));
            output.write(HStringHelper.concat("\t\t\tFree Space: ", disk.getFreeSpace(), "Bytes (", disk.getFreeSpace() / 1024 / 1024 / 1024, "GB\n"));
            output.write(HStringHelper.concat("\t\t\tUsable Space: ", disk.getUsableSpace(), "Bytes (", disk.getUsableSpace() / 1024 / 1024 / 1024, "GB\n"));
        }
        Properties properties = System.getProperties();
        output.write("\tSystem Properties:\n");
        for (String name: properties.stringPropertyNames()) {
            output.write(HStringHelper.concat("\t\tName: ", name, "\n"));
            output.write(HStringHelper.concat("\t\t\tValue: ", properties.getProperty(name), "\n"));
        }
    }
}
