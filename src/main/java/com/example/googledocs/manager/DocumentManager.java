package com.example.googledocs.manager;

import com.example.googledocs.model.Document;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Responsible for document lifecycle.
 */
public class DocumentManager {

    private final Map<String, Document> documents =
            new ConcurrentHashMap<>();

    public Document createDocument(String name) {
        String documentId = UUID.randomUUID().toString();
        Document document = new Document(documentId, name);
        documents.put(documentId, document);
        return document;
    }

    public Document getDocument(String documentId) {
        return documents.get(documentId);
    }
}
