package healthblog.services;

import healthblog.models.Image;

import java.util.List;

public interface ImageService {
    List<Image> getAllImages();

    boolean imageExists(Integer id);

    Image findImage(String path);

    void deleteImage(Image image);

    void saveImage(Image image);
}
