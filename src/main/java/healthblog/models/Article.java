package healthblog.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "articles")
public class Article {
    private Integer id;

    private String category;

    private String title;

    private String content;

    private User author;

    private String image;

    private Date date = new Date();

    public Article() {
    }

    public Article(String category, String title, String content, User author, Date date) {
        this.category = category;
        this.title = title;
        this.content = content;
        this.author = author;
        this.date = date;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "category", nullable = false)
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Column(name = "title", nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "content", columnDefinition = "text", nullable = false)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @ManyToOne()
    @JoinColumn(name = "authorId", nullable = false)
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Transient
    public String getSummary(){
        int endIndex = this.getContent().length() / 2;

        return this.getContent().substring(0, endIndex) + "...";
    }

    @Column(name = "imageSource", nullable = true)
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Column(columnDefinition = "DATETIME", nullable = false)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

