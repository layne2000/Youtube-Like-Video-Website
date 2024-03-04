package org.example;

import org.example.util.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController {

    private final FileService fileService;
    @Autowired
    FileController(FileService fileService){
        this.fileService = fileService;
    }

    @GetMapping("/SHA256files")
    public JsonResponse<String> getFileSHA256(MultipartFile file) throws Exception {
        String fileSHA256 = fileService.getFileSHA256(file);
        return new JsonResponse<>(fileSHA256);
    }

    // the following slices requests should pass in the same fileSHA256 as the first piece
    @PutMapping("/file-slices")
    public JsonResponse<String> uploadFileBySlices(MultipartFile slice,
                                                   String fileSHA256,
                                                   Integer sliceNum,
                                                   Integer totalSliceNum) throws Exception {
        //TODO: add JWT verification!
        String filePath = fileService.uploadFileBySlices(slice, fileSHA256, sliceNum, totalSliceNum);
        return new JsonResponse<>(filePath);
    }
}
