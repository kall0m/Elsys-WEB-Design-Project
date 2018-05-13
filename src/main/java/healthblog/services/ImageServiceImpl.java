package healthblog.services;

import healthblog.models.Image;
import healthblog.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("imageService")
public class ImageServiceImpl implements ImageService {
    private ImageRepository imageRepository;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public List<Image> getAllImages() {
        return this.imageRepository.findAll();
    }

    public boolean imageExists(Integer id) {
        return this.imageRepository.exists(id);
    }

    public Image findImage(byte[] bytes) {
        return this.imageRepository.findByBytes(bytes);
    }

    public void deleteImage(Image image) {
        this.imageRepository.delete(image);
    }

    public void saveImage(Image image) {
        this.imageRepository.saveAndFlush(image);
    }
}
