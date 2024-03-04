package org.example;

import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.example.util.FastDFSUtil;
import org.example.util.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class TestController {
    private final FastDFSUtil fastDFSUtil;

    @Autowired
    TestController(FastDFSUtil fastDFSUtil){
        this.fastDFSUtil = fastDFSUtil;
    }

    @GetMapping("/test-slices")
    public JsonResponse<String> sliceFile(MultipartFile multipartFile) throws Exception {
        fastDFSUtil.convertFileToSlices(multipartFile);
        return JsonResponse.success();
    }

}
