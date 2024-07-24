package com.skyfork.api.langya;

import com.skyfork.api.imflowow.LoadWorldEvent;
import com.skyfork.client.annotations.event.EventTarget;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

/**
 * @author LangYa
 * @since 2024/06/16/下午8:50
 */
public class MemoryManager {

    @EventTarget
    public void onWorldLoad(LoadWorldEvent event) {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage memoryUsage = memoryMXBean.getHeapMemoryUsage();
        if (memoryUsage.getMax() / (1024 * 1024) - memoryUsage.getUsed() / (1024 * 1024) > 1000) {
            System.gc();
        }
    }
}
