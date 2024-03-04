package org.example;

import io.netty.util.internal.StringUtil;
import org.example.entity.File;
import org.example.mapper.FileMapper;
import org.example.util.FastDFSUtil;
import org.example.util.SHA256Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;


@Service
public class FileService {
    private final FileMapper fileMapper;

    private final FastDFSUtil fastDFSUtil;

    @Autowired
    FileService(FileMapper fileMapper, FastDFSUtil fastDFSUtil){
        this.fileMapper = fileMapper;
        this.fastDFSUtil = fastDFSUtil;
    }

    public String uploadFileBySlices(MultipartFile slice,
                                     String fileSHA256,
                                     Integer sliceNum,
                                     Integer totalSliceNum) throws Exception {
        File dbFileSHA256 = fileMapper.getFileBySHA256(fileSHA256);
        if(dbFileSHA256 != null){
            return dbFileSHA256.getUrl();
        }
        String url = fastDFSUtil.uploadFileBySlices(slice, fileSHA256, sliceNum, totalSliceNum);
        if(!StringUtil.isNullOrEmpty(url)){
            dbFileSHA256 = new File();
            dbFileSHA256.setCreatedTime(LocalDateTime.now());
            dbFileSHA256.setSHA256(fileSHA256);
            dbFileSHA256.setUrl(url);
            dbFileSHA256.setType(fastDFSUtil.getFileType(slice));
            fileMapper.addFile(dbFileSHA256);
        }
        return url;
    }

    public String getFileSHA256(MultipartFile file) throws Exception {
        return SHA256Util.getFileSHA256(file);
    }

    public File getFileBySHA256(String fileSHA256) {
        return fileMapper.getFileBySHA256(fileSHA256);
    }
}
