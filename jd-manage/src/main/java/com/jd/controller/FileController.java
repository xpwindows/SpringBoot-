package com.jd.controller;

import com.jd.service.FileService;
import com.jd.vo.EasyUI_Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 文件上传，图片上传
 */
@Controller
@PropertySource("classpath:/properties/image.properties")
public class FileController {
    @Value("${image.localDirPath}")
    private String localDirPath;  //本地路径
    @Autowired
    private FileService fileService;
    /**
     * 业务要求：将图片上传到指定文件夹；D:\1-JT\images\
     * 1.确定url请求路径：http://localhost:8091/file
     * 2.获取用户提交的参数，方法参数名称要和input框的name相同
     * 3.响应合适的页面  重定向到上传页面 http://localhost:8091/file.jsp
     * MultipartFile 是spring框架中的类，用来接收前台传来的文件
     */
    @RequestMapping("/file")
    public String file(MultipartFile fileImage) throws IOException {
        /**
         * 文件上传优化，优化思路：
         * 1.不把文件夹路径写死在代码，放在image.properties文件里面；
         * 2.在创建File实例对象之前，判断文件夹是否存在，不存在则创建；
         * 3.获取源文件名称作为文件名称；
         */
        //获取文件名称
        String filename = fileImage.getOriginalFilename();
        //判断文件夹是否存在
        File fileDir = new File(localDirPath);
        if (!fileDir.exists()){
            //表示文件夹不存在，创建文件夹
            fileDir.mkdirs();
        }
        /**
         * 使用路径名+源文件名来创建文件实例   File
         */
        File file = new File(localDirPath+filename);
        //1.实现文件上传
        fileImage.transferTo(file);
        //2.重定向页面
        return "redirect:/file.jsp";
    }
    /**
     * 实现用户图片上传
     * 请求路径：url= /pic/upload
     * 返回的数据：EasyUI_Image 对象的JSON串
     * 接收参数名称：uploadFile
     */
    @RequestMapping("/pic/upload")
    @ResponseBody
    public EasyUI_Image fileUpload(MultipartFile uploadFile){
       return fileService.fileUpload(uploadFile);
    }
}
