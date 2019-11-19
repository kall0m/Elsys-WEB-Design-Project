package healthblog.services;

import healthblog.models.Document;

import java.util.List;

public interface DocumentService {
    List<Document> getAllDocuments();

    boolean documentExists(Integer id);

    Document findDocument(String path);

    void deleteDocument(Document document);

    void saveDocument(Document document);
}
