package com.example.googledocs.collaboration;

import com.example.googledocs.operation.Operation;

/**
 * Strategy abstraction for conflict resolution.
 *
 * The LLD does not couple CellCollaborationService to a particular OT/CRDT
 * algorithm. A production implementation can replace this strategy with a
 * more complete operational transformation or CRDT implementation.
 */
public interface OperationTransformer {

    Operation transform(Operation incoming, Operation alreadyApplied);
}
