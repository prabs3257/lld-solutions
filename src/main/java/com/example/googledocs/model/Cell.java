package com.example.googledocs.model;

import com.example.googledocs.collaboration.OperationHistory;

/**
 * Domain object representing one editable cell.
 *
 * The Cell intentionally does not expose setters for version/content.
 * All modifications should go through the collaboration service so that
 * content, version and operation history are updated atomically.
 *
 * Locking is kept outside the domain object in CellLockManager. This keeps
 * concurrency infrastructure separate from the domain model.
 */
public class Cell {

    private final CellId cellId;
    private String content;
    private long version;
    private final OperationHistory operationHistory;

    public Cell(CellId cellId) {
        this.cellId = cellId;
        this.content = "";
        this.version = 0;
        this.operationHistory = new OperationHistory();
    }

    public CellId getCellId() {
        return cellId;
    }

    public String getContent() {
        return content;
    }

    public long getVersion() {
        return version;
    }

    public OperationHistory getOperationHistory() {
        return operationHistory;
    }

    /**
     * Must be called only while the lock for this cell is held.
     */
    public void updateContent(String content) {
        this.content = content;
        this.version++;
    }
}
