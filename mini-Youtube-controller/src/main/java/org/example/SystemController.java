package org.example;

import org.example.util.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class SystemController {
    private final ElasticSearchService elasticSearchService;

    @Autowired
    SystemController(ElasticSearchService elasticSearchService){
        this.elasticSearchService = elasticSearchService;
    }

    @GetMapping("/contents")
    public JsonResponse<List<Map<String, Object>>> getContents(@RequestParam String keyword,
                                                               @RequestParam Integer pageNum,
                                                               @RequestParam Integer pageSize) throws IOException {
        List<Map<String, Object>> list = elasticSearchService.getContents(keyword,pageNum,pageSize);
        return new JsonResponse<>(list);
    }
}
