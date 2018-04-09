package healthblog.models;

import javax.persistence.*;

@Entity
@Table(name = "articles")
public class Article {
    private Integer id;

    private String category;

    private String title;

    private String content;

    private User author;

    private boolean cool;

    private String image;

    public Article() {
    }

    public Article(String category, String title, String content, User author) {
        this.category = category;
        this.title = title;
        this.content = content;
        this.author = author;
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

    @Column(name = "isCool", nullable = true)
    public boolean isCool() {
        return cool;
    }

    public void setCool(boolean cool) {
        this.cool = cool;
    }

    @Column(name = "imageSource", nullable = true)
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

