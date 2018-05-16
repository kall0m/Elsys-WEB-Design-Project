package healthblog.models;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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

    private List<Image> images;

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
        this.images = new ArrayList<>();
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

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinTable(name = "articles_images")
    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    //@Column(columnDefinition = "DATETIME", nullable = false)
    @Column(insertable = false, updatable = false) //postgresql
    @Temporal(TemporalType.TIMESTAMP) //postgresql
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

        if (id != null ? !id.equals(article.id) : article.id != null) return false;
        if (category != null ? !category.equals(article.category) : article.category != null) return false;
        if (title != null ? !title.equals(article.title) : article.title != null) return false;
        if (content != null ? !content.equals(article.content) : article.content != null) return false;
        if (author != null ? !author.equals(article.author) : article.author != null) return false;
        if (image != null ? !image.equals(article.image) : article.image != null) return false;
        if (images != null ? !images.equals(article.images) : article.images != null) return false;
        if (date != null ? !date.equals(article.date) : article.date != null) return false;
        return tags != null ? tags.equals(article.tags) : article.tags == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (images != null ? images.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        return result;
    }
}

