package healthblog.services;

import healthblog.models.Tag;
import healthblog.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagServiceStubImpl implements TagService {
    private TagRepository tagRepository;

    @Autowired
    public TagServiceStubImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Tag findTag(String name) {
        return this.tagRepository.findByName(name);
    }

    public void saveTag(Tag tag) {
        this.tagRepository.saveAndFlush(tag);
    }
}
