package healthblog.models;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "documents")
public class Document extends File {
    public static String DOCUMENTS_DIR = "articles-documents/";

    public Document() {     }

    public Document(String path, Article article) {
        super(path, article);
    }
}
