package com.example.googledocs.model;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Aggregate root for a collaborative document.
 *
 * ConcurrentHashMap allows independent cells to be created/accessed
 * concurrently. Actual mutation of an individual cell is protected by
 * a cell-level lock.
 */
public class Document {

    private final String documentId;
    private final String name;
    private final Map<CellId, Cell> cells = new ConcurrentHashMap<>();

    public Document(String documentId, String name) {
        this.documentId = Objects.requireNonNull(documentId);
        this.name = Objects.requireNonNull(name);
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getName() {
        return name;
    }

    public Cell getOrCreateCell(CellId cellId) {
        return cells.computeIfAbsent(cellId, Cell::new);
    }

    public Cell getCell(CellId cellId) {
        return cells.get(cellId);
    }
}
