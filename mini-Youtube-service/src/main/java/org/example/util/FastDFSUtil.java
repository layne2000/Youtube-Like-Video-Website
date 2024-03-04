package org.example.util;

import com.github.tobato.fastdfs.domain.fdfs.FileInfo;
import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadCallback;

import com.github.tobato.fastdfs.service.AppendFileStorageClient;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import io.netty.util.internal.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.exception.CustomizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

@Component
public class FastDFSUtil {

    private final FastFileStorageClient fastFileStorageClient;
    private final AppendFileStorageClient appendFileStorageClient;
    private final RedisTemplate<String, String> redisTemplate;
    private final String DEFAULT_GROUP = "group1";
    private final int SLICE_SIZE = 1024 * 1024 * 2; // 2MB

    @Value("${fdfs.http.storage-addr}")
    private String httpFdfsStorageAddr;

    @Autowired
    FastDFSUtil(FastFileStorageClient fastFileStorageClient, AppendFileStorageClient appendFileStorageClient, RedisTemplate<String, String> redisTemplate){
        this.fastFileStorageClient = fastFileStorageClient;
        this.appendFileStorageClient = appendFileStorageClient;
        this.redisTemplate = redisTemplate;
    }

    public String getFileType(MultipartFile file){
        if(file == null){
            throw new CustomizedException("invalid file！");
        }
        String fileName = file.getOriginalFilename();
        if(fileName == null){
            throw new CustomizedException("invalid file！");
        }
        int index = fileName.lastIndexOf(".");
        return fileName.substring(index+1);
    }

    public String uploadCommonFile(MultipartFile file) throws Exception {
        String fileType = this.getFileType(file);
        StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), fileType, new HashSet<>());
        return storePath.getPath();
    }

    public String uploadCommonFile(File file, String fileType) throws Exception {
        StorePath storePath = fastFileStorageClient.uploadFile(new FileInputStream(file),
                file.length(), fileType, new HashSet<>());
        return storePath.getPath();
    }

    public void deleteFile(String filePath){
        fastFileStorageClient.deleteFile(filePath);
    }

    // upload first sliced files
    public String uploadAppenderFile(MultipartFile file) throws Exception{
        String fileType = this.getFileType(file);
        StorePath storePath = appendFileStorageClient.uploadAppenderFile(DEFAULT_GROUP, file.getInputStream(), file.getSize(), fileType);
        return storePath.getPath();
    }

    // upload following sliced files
    public void modifyAppenderFile(MultipartFile file, String filePath, long offset) throws Exception{
        appendFileStorageClient.modifyFile(DEFAULT_GROUP, filePath, file.getInputStream(), file.getSize(), offset);
    }

    // return the result path only after all the slices have been uploaded
    public String uploadFileBySlices(MultipartFile file, String fileSHA256, Integer sliceNum, Integer totalSliceNum) throws Exception {
        if(file == null || sliceNum == null || totalSliceNum == null){
            throw new CustomizedException("invalid parameter(s)！");
        }
        String pathKey = "redis-file-path:" + fileSHA256;
        String uploadedSizeKey = "redis-uploaded-size:" + fileSHA256;
        String uploadedNumKey = "redis-uploaded-num:" + fileSHA256;
        String uploadedSizeValStr = redisTemplate.opsForValue().get(uploadedSizeKey);
        Long uploadedSize = 0L;
        if(!StringUtil.isNullOrEmpty(uploadedSizeValStr)){
            uploadedSize = Long.valueOf(uploadedSizeValStr);
        }
        if(sliceNum == 1){ //first slice
            String path = this.uploadAppenderFile(file);
            if(StringUtil.isNullOrEmpty(path)){
                throw new CustomizedException("upload failed！");
            }
            redisTemplate.opsForValue().set(pathKey, path);
            redisTemplate.opsForValue().set(uploadedNumKey, "1");
        }else{// following slice
            String filePath = redisTemplate.opsForValue().get(pathKey);
            if(StringUtil.isNullOrEmpty(filePath)){
                throw new CustomizedException("upload failed！");
            }
            this.modifyAppenderFile(file, filePath, uploadedSize);
            redisTemplate.opsForValue().increment(uploadedNumKey);
        }
        // modify uploadedSize in the redis
        uploadedSize  += file.getSize();
        redisTemplate.opsForValue().set(uploadedSizeKey, String.valueOf(uploadedSize));
        // if all the slices have been uploaded, clear all relevant keys in the redis
        String uploadedNumValStr = redisTemplate.opsForValue().get(uploadedNumKey);
        Integer uploadedNumVal = Integer.valueOf(uploadedNumValStr);
        String resultPath = "";
        // only after passing all the slices, will DB save the file info
        if(uploadedNumVal.equals(totalSliceNum)){
            resultPath = redisTemplate.opsForValue().get(pathKey);
            List<String> keyList = Arrays.asList(uploadedNumKey, pathKey, uploadedSizeKey);
            redisTemplate.delete(keyList);
        }
        return resultPath;
    }

    // should be finished by frontend(here is just a test)， split the file and save the slices locally
    public void convertFileToSlices(MultipartFile multipartFile) throws Exception{
        String fileType = this.getFileType(multipartFile);
        // convert MultipartFile to temp File
        File file = this.multipartFileToFile(multipartFile);
        long fileLength = file.length();
        int count = 1;
        for(int i = 0; i < fileLength; i += SLICE_SIZE){
            // traditional java file handling
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            randomAccessFile.seek(i);
            byte[] bytes = new byte[SLICE_SIZE];
            int len = randomAccessFile.read(bytes);
            String path = "/Users/longhaozhu/Documents/computer/mini-Youtube/temp" + count + "." + fileType;
            File slice = new File(path);
            FileOutputStream fos = new FileOutputStream(slice);
            fos.write(bytes, 0, len);
            fos.close();
            randomAccessFile.close();
            ++count;
        }
        // delete temp file
        file.delete();
    }

    public File multipartFileToFile(MultipartFile multipartFile) throws Exception{
        String originalFileName = multipartFile.getOriginalFilename();
        String[] fileName = originalFileName.split("\\.");
        File file = File.createTempFile(fileName[0], "." + fileName[1]);
        multipartFile.transferTo(file);
        return file;
    }

    public void viewVideoOnlineBySlices(HttpServletRequest request, HttpServletResponse response, String path) throws Exception{
        FileInfo fileInfo = fastFileStorageClient.queryFileInfo(DEFAULT_GROUP, path);
        long totalFileSize = fileInfo.getFileSize();
        String url = httpFdfsStorageAddr + path;
        // save all the headers and their values into map
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, Object> headers = new HashMap<>();
        while(headerNames.hasMoreElements()){
            String header = headerNames.nextElement();
            headers.put(header, request.getHeader(header));
        }
        // fill in the header of Range if it's non-existent
        String rangeStr = request.getHeader("Range");
        String[] range;
        if(rangeStr == null || rangeStr.equals("")){
            rangeStr = "bytes=0-" + (totalFileSize-1);
        }
        // add value to headers in the response
        range = rangeStr.split("bytes=|-"); // split the string if the char is "bytes=" or "-"
        long begin = 0;
        if(range.length >= 2){
            begin = Long.parseLong(range[1]);
        }
        long end = totalFileSize-1;
        if(range.length >= 3){
            end = Long.parseLong(range[2]);
        }
        String contentRange = "bytes " + begin + "-" + end + "/" + totalFileSize;
        response.setHeader("Content-Range", contentRange);
        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("Content-Type", "video/mp4");
        response.setContentLength((int)(end - begin + 1));
        // set status to 206（Partial Content）
        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
        HttpUtil.get(url, headers, response);
    }
}



