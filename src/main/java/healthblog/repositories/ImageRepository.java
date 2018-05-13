package healthblog.repositories;

import healthblog.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
    Image findByBytes(byte[] bytes);
    Image findByBase64(String base64);
}
