package org.home.components;

import org.home.services.UnblockingService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SchedulerUnblockingTask {
    static final int rate = 1000 * 60 * 60 * 24;

    private final UnblockingService unblockingService;

    public SchedulerUnblockingTask(UnblockingService unblockingService) {
        this.unblockingService = unblockingService;
    }

    @Scheduled(fixedRate = 1000)
    public void unblockUsers() {
        //...
    }
}
