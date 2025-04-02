package com.shawnmall.service.imp;

import com.google.common.collect.Lists;
import com.shawnmall.service.IFileService;
import com.shawnmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @program: ShawnMall
 * @description: Implementation of file service
 * @author: Shawn Li
 * @create: 2018-09-15 15:07
 **/

@Service("iFileService")
public class FileServiceImp implements IFileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImp.class);

    public String upload(MultipartFile file, String path){

        String fileName = file.getOriginalFilename();
        //get the extension of the file
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        String uploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;
        logger.info("Start file upload-- file name: {}, file path: {}, new file name: {}", fileName, path, uploadFileName);
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);

            fileDir.mkdirs();
        }
        File targetFile = new File(path, uploadFileName);
        try {
            file.transferTo(targetFile);

            FTPUtil.uploadFile(Lists.newArrayList(targetFile));

            targetFile.delete();
        } catch (IOException e) {
            logger.error("exception for file uploading");
            return null;
        }
        return targetFile.getName();
    }
}
