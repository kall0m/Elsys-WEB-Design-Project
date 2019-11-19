package healthblog.models;

import javax.persistence.*;

@Entity
@Table(name = "images")
public class Image extends File {
    public static String IMAGES_DIR = "articles-images/";

    public Image() {    }

    public Image(String path, Article article) {
        super(path, article);
    }
}
