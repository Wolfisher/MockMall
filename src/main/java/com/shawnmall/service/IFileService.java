package com.shawnmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @program: ShawnMall
 * @author: Shawn Li
 * @create: 2018-09-15 15:07
 **/
public interface IFileService {

    String upload(MultipartFile file, String path);

}
