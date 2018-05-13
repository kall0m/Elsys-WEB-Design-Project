package healthblog.services;

import healthblog.models.Image;

import java.util.List;

public interface ImageService {
    List<Image> getAllImages();

    boolean imageExists(Integer id);

    Image findImage(byte[] bytes);

    void deleteImage(Image image);

    void saveImage(Image image);
}
