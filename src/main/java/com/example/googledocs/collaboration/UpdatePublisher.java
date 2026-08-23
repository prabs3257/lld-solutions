package com.example.googledocs.collaboration;

/**
 * Decouples collaboration logic from the transport mechanism.
 *
 * Possible implementations:
 * - WebSocket publisher
 * - Kafka publisher
 * - SSE publisher
 * - In-memory publisher used in this LLD
 */
public interface UpdatePublisher {

    void publish(String documentId, CellUpdateResult update);
}
