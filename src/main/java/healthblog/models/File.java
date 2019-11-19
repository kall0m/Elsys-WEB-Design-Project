package healthblog.models;

import javax.persistence.*;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class File {
    private Integer id;

    private String path;

    private Article article;

    public File() {    }

    public File(String path, Article article) {
        this.path = path;
        this.article = article;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "path", nullable = false)
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @ManyToOne()
    @JoinColumn(name = "articleId")
    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
