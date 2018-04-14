package healthblog.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    private List<Tag> tags;

    public Article() {   }

    public Article(String category, String title, String content, User author, Date date) {
        this.category = category;
        this.title = title;
        this.content = content;
        this.author = author;
        this.date = date;
        this.tags = new ArrayList<>();
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "articles_tags")
    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Article article = (Article) o;

        if (!id.equals(article.id)) return false;
        if (!category.equals(article.category)) return false;
        if (!title.equals(article.title)) return false;
        if (!content.equals(article.content)) return false;
        if (!author.equals(article.author)) return false;
        if (!image.equals(article.image)) return false;
        if (!date.equals(article.date)) return false;
        return tags.equals(article.tags);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + category.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + content.hashCode();
        result = 31 * result + author.hashCode();
        result = 31 * result + image.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + tags.hashCode();
        return result;
    }
}

