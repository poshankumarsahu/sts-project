package com.example.demoAPI;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

public class ConcurrencyTest {

    @Test
    public void testCompletableFutureParallelExecution()
            throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Task1").orTimeout(1, TimeUnit.SECONDS);
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "Task2").orTimeout(1, TimeUnit.SECONDS);
        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> "Task3").orTimeout(1, TimeUnit.SECONDS);

        CompletableFuture<Void> allOf = CompletableFuture.allOf(future1, future2, future3);
        allOf.get(2, TimeUnit.SECONDS);

        assertThat(future1.get()).isEqualTo("Task1");
        assertThat(future2.get()).isEqualTo("Task2");
        assertThat(future3.get()).isEqualTo("Task3");
    }

    @Test
    public void testCompletableFutureTimeout() throws Exception {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
                return "Done";
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).orTimeout(500, TimeUnit.MILLISECONDS);

        // Wait a bit
        Thread.sleep(1000);
        assertThat(future.isCompletedExceptionally()).isTrue();
    }
}