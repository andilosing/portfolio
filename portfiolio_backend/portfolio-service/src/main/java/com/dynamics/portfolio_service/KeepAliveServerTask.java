package com.dynamics.portfolio_service;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.logging.Logger;

@Component
public class KeepAliveServerTask {

    /*

    @Scheduled(fixedRate = 180000)
    public void performComplexCalculation() {
        System.out.println("Starting complex calculation task.");
        long start = System.currentTimeMillis();

        double result = 0;
        for (int i = 0; i < 1000000; i++) {
            result += Math.sqrt(i) * Math.tanh(i);
        }

        long end = System.currentTimeMillis();
        System.out.printf("Completed complex calculation in %d milliseconds. Result: %f%n", (end - start), result);

    }

     */

}
