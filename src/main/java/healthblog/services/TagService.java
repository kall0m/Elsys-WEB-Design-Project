package healthblog.services;

import healthblog.models.Tag;

public interface TagService {
    Tag findTag(String tag);

    void saveTag(Tag tag);
}
