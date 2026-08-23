package com.example.googledocs.collaboration;

/**
 * Abstraction for a client connected to a document.
 *
 * In production this could correspond to a WebSocket session.
 */
public interface UpdateListener {

    void onUpdate(String documentId, CellUpdateResult update);
}
