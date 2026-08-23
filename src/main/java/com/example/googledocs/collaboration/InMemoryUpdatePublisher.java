package com.example.googledocs.collaboration;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Simple observer-based publisher for the in-memory implementation.
 *
 * CopyOnWriteArrayList is appropriate here because reads/publications are
 * expected to be more frequent than listeners joining/leaving.
 */
public class InMemoryUpdatePublisher implements UpdatePublisher {

    private final List<UpdateListener> listeners =
            new CopyOnWriteArrayList<>();

    public void subscribe(UpdateListener listener) {
        listeners.add(listener);
    }

    public void unsubscribe(UpdateListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void publish(String documentId, CellUpdateResult update) {
        for (UpdateListener listener : listeners) {
            listener.onUpdate(documentId, update);
        }
    }
}
