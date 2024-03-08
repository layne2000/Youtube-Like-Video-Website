package org.example.ESRepo;

import org.example.entity.Video;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface VideoRepository extends ElasticsearchRepository<Video, String> {

    Video findByTitleLike(String keyword);
}
