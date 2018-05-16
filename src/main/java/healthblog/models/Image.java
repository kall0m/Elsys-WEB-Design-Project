package healthblog.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "images")
public class Image {
    private Integer id;

    private byte[] bytes;

    private String base64;

    private List<Article> articles;

    public Image() {
    }

    public Image(byte[] bytes) {
        this.bytes = bytes;
        this.articles = new ArrayList<Article>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Lob
    @Column(name = "image", nullable = false, columnDefinition = "BYTEA")
    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    @Lob
    @Column(name = "base64Value", nullable = false)
    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    @ManyToMany(mappedBy = "images")
    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
