package healthblog.services;

import healthblog.models.Tag;

import java.util.List;

public interface TagService {
    Tag findTag(String tag);

    void saveTag(Tag tag);

    List<Tag> getAllTags();
}
