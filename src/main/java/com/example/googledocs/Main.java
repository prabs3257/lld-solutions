package com.example.googledocs;

import com.example.googledocs.collaboration.*;
import com.example.googledocs.lock.CellLockManager;
import com.example.googledocs.manager.DocumentManager;
import com.example.googledocs.model.Cell;
import com.example.googledocs.model.CellId;
import com.example.googledocs.model.Document;
import com.example.googledocs.operation.InsertOperation;

import java.util.concurrent.CountDownLatch;

/**
 * Demonstrates two users concurrently editing the SAME cell.
 *
 * Initial value:
 *     Hello
 *
 * Both users read version 1.
 *
 * Alice creates:
 *     Insert "A" at position 0
 *
 * Bob creates:
 *     Insert "B" at position 5
 *
 * Whichever operation commits second may be stale. It is transformed against
 * the operation that committed first, so both edits are preserved.
 *
 * Final value will deterministically contain both A and B.
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {

        // ---------- Wiring ----------
        DocumentManager documentManager = new DocumentManager();

        CellLockManager lockManager = new CellLockManager();

        CellCollaborationService cellCollaborationService =
                new CellCollaborationService(
                        lockManager,
                        new SimpleOperationTransformer(),
                        new OperationApplier()
                );

        InMemoryUpdatePublisher updatePublisher =
                new InMemoryUpdatePublisher();

        CollaborationService collaborationService =
                new CollaborationService(
                        cellCollaborationService,
                        updatePublisher
                );

        // Simulates clients receiving real-time updates.
        updatePublisher.subscribe(
                (documentId, update) ->
                        System.out.println(
                                "[" + Thread.currentThread().getName() + "] "
                                        + "Document=" + documentId
                                        + " -> " + update
                        )
        );

        // ---------- Create document ----------
        Document document =
                documentManager.createDocument("Interview Document");

        CellId cellId = new CellId(0, 0);
        Cell cell = document.getOrCreateCell(cellId);

        // Initial content is inserted through the same collaboration pipeline.
        collaborationService.submitOperation(
                document,
                cellId,
                new InsertOperation(
                        "system",
                        cell.getVersion(),
                        0,
                        "Hello"
                )
        );

        // Both users now see version 1.
        long baseVersionSeenByBothUsers = cell.getVersion();

        CountDownLatch startTogether = new CountDownLatch(1);

        Thread alice = new Thread(() -> {
            await(startTogether);

            collaborationService.submitOperation(
                    document,
                    cellId,
                    new InsertOperation(
                            "alice",
                            baseVersionSeenByBothUsers,
                            0,
                            "A"
                    )
            );
        }, "Alice-Thread");

        Thread bob = new Thread(() -> {
            await(startTogether);

            collaborationService.submitOperation(
                    document,
                    cellId,
                    new InsertOperation(
                            "bob",
                            baseVersionSeenByBothUsers,
                            5,
                            "B"
                    )
            );
        }, "Bob-Thread");

        alice.start();
        bob.start();

        // Release both threads at approximately the same time.
        startTogether.countDown();

        alice.join();
        bob.join();

        System.out.println();
        System.out.println("========== FINAL STATE ==========");
        System.out.println("Cell    : " + cell.getCellId());
        System.out.println("Content : " + cell.getContent());
        System.out.println("Version : " + cell.getVersion());
    }

    private static void await(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted", e);
        }
    }
}
