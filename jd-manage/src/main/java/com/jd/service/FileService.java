package com.jd.service;

import com.jd.vo.EasyUI_Image;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
//图片上传
    EasyUI_Image fileUpload(MultipartFile uploadFile);
}
