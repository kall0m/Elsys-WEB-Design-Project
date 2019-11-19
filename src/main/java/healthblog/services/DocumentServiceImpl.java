package healthblog.services;

import healthblog.models.Document;
import healthblog.repositories.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("documentService")
public class DocumentServiceImpl implements DocumentService {
    private DocumentRepository documentRepository;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public List<Document> getAllDocuments() {
        return this.documentRepository.findAll();
    }

    public boolean documentExists(Integer id) {
        return this.documentRepository.exists(id);
    }

    public Document findDocument(String path) {
        return this.documentRepository.findByPath(path);
    }

    public void deleteDocument(Document document) {
        this.documentRepository.delete(document);
    }

    public void saveDocument(Document document) {
        this.documentRepository.saveAndFlush(document);
    }
}
