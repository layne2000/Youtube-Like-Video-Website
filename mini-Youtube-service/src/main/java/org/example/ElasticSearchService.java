package org.example;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Highlight;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import org.elasticsearch.client.RequestOptions;
import org.example.ESRepo.UserInfoRepository;
import org.example.ESRepo.VideoRepository;
import org.example.entity.UserInfo;
import org.example.entity.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class ElasticSearchService {

    private final VideoRepository videoRepository;
    private final UserInfoRepository userInfoRepository;
    private final ElasticsearchClient elasticsearchClient;

    @Autowired
    ElasticSearchService(VideoRepository videoRepository, UserInfoRepository userInfoRepository,
                         ElasticsearchClient elasticsearchClient){
        this.videoRepository = videoRepository;
        this.userInfoRepository = userInfoRepository;
        this.elasticsearchClient = elasticsearchClient;
    }

    public void addVideo(Video video){
        videoRepository.save(video);
    }

    public Video getVideos(String keyword){
        return videoRepository.findByTitleLike(keyword);
    }

    public void deleteAllVideos(){
        videoRepository.deleteAll();
    }

    public void addUserInfo(UserInfo userInfo){
        userInfoRepository.save(userInfo);
    }

//    public List<Map<String, Object>> getContents(String keyword, Integer pageNum, Integer pageSize) throws IOException {
//
//        // Constructing the multi-match query
//        // any document that contains the keyword in one or more of these fields will be considered a match
//        Query query = Query.of(q -> q
//                .multiMatch(mmq -> mmq
//                        .query(keyword)
//                        .fields("title", "description", "nickname")
//                )
//        );
//
//        // Configuring highlighting
//        Highlight highlight = Highlight.of(h -> h
//                .fields("title", f -> f)
//                .fields("description", f -> f)
//                .fields("nickname", f -> f)
//                .preTags("<span style=\"color:red\">")
//                .postTags("</span>")
//        );
//
//        // Building the search request
//        SearchRequest searchRequest = SearchRequest.of(s -> s
//                .index("videos", "user-infos")
//                .query(query)
//                .from(pageNum - 1)
//                .size(pageSize)
//                .highlight(highlight)
//        );
//
//        // Executing the search
//        SearchResponse<Object> searchResponse = elasticsearchClient.search(searchRequest, Object.class);
//
//        // Process the search response
//        List<Map<String, Object>> arrayList = new ArrayList<>();
//        for (Hit<JsonData> hit : searchResponse.hits().hits()) {
//            Map<String, Object> sourceMap = hit.source().toJson(); // Convert JsonData to a map
//            Map<String, List<String>> highlightFields = hit.highlight();
//
//            // Loop through each field you want to highlight
//            for (String key : array) {
//                if (highlightFields != null && highlightFields.containsKey(key)) {
//                    // Concatenate all highlighted fragments for the field
//                    String highlightedText = String.join("", highlightFields.get(key));
//                    sourceMap.put(key, highlightedText);
//                }
//            }
//
//            arrayList.add(sourceMap);
//        }
//
//        return arrayList;
//    }
}
