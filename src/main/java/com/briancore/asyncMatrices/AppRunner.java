package com.briancore.asyncMatrices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

@Component
public class AppRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(AppRunner.class);
    private final MatricesService matricesService;

    @Autowired
    public AppRunner(MatricesService matricesService) {
        this.matricesService = matricesService;
    }

    @Override
    public void run(String... args) throws Exception {
        long start = System.currentTimeMillis();

        /* for single one row matrix */
        int[] oneRowArr = {56, 8, 12, 4, 7, 9};
        int[] elementOf = {1, 4};
        CompletableFuture<HashMap<String, Object>> oneRowExec = this.matricesService.matricesInformation(
                oneRowArr, elementOf
        );
        /* for two row matrix */
        int[][] twoRowArr = {{4,56,1,0,7}, {1,8,3,6,3}};
        CompletableFuture<HashMap<String, Object>> twoRowExec = this.matricesService.matricesInformation(
                twoRowArr, elementOf
        );
        CompletableFuture.allOf(oneRowExec, twoRowExec).join();
        logger.info("Elapsed time: {}", System.currentTimeMillis() - start);
        logger.info("--> {}", oneRowExec.get());
        logger.info("--> {}", twoRowExec.get());
    }
}
