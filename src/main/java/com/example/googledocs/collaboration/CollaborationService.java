package com.example.googledocs.collaboration;

import com.example.googledocs.model.Cell;
import com.example.googledocs.model.CellId;
import com.example.googledocs.model.Document;
import com.example.googledocs.operation.Operation;

/**
 * Facade used by the application layer.
 *
 * It coordinates:
 *   Document -> Cell -> Concurrent operation handling -> Broadcasting
 */
public class CollaborationService {

    private final CellCollaborationService cellCollaborationService;
    private final UpdatePublisher updatePublisher;

    public CollaborationService(
            CellCollaborationService cellCollaborationService,
            UpdatePublisher updatePublisher) {

        this.cellCollaborationService = cellCollaborationService;
        this.updatePublisher = updatePublisher;
    }

    public CellUpdateResult submitOperation(
            Document document,
            CellId cellId,
            Operation operation) {

        Cell cell = document.getOrCreateCell(cellId);

        CellUpdateResult result =
                cellCollaborationService.applyOperation(cell, operation);

        // Publish only after the operation has been successfully committed.
        updatePublisher.publish(document.getDocumentId(), result);

        return result;
    }
}
