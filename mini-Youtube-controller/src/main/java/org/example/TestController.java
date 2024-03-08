package org.example;

import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.example.entity.Video;
import org.example.util.FastDFSUtil;
import org.example.util.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class TestController {
    private final FastDFSUtil fastDFSUtil;

    private final ElasticSearchService elasticSearchService;

    @Autowired
    TestController(FastDFSUtil fastDFSUtil, ElasticSearchService elasticSearchService){
        this.fastDFSUtil = fastDFSUtil;
        this.elasticSearchService = elasticSearchService;
    }

    @GetMapping("/test-slices")
    public JsonResponse<String> sliceFile(MultipartFile multipartFile) throws Exception {
        fastDFSUtil.convertFileToSlices(multipartFile);
        return JsonResponse.success();
    }

    @GetMapping("/es-videos")
    public JsonResponse<Video> getEsVideos(@RequestParam String keyword){
        Video video = elasticSearchService.getVideos(keyword);
        return new JsonResponse<>(video);
    }

}
