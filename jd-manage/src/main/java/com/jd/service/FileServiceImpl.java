package com.jd.service;

import com.jd.vo.EasyUI_Image;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 图片上传
 */
@Service
//加载配置文件，将数据交给Spring容器管理
@PropertySource("classpath:/properties/image.properties")
public class FileServiceImpl implements FileService {
    /**
     *由于路径写死在代码里面，扩展不容易，最好的方式就是写在配置文件里面；
     */
    @Value("${image.localDirPath}")  //取配置文件的路径赋值
    private String localDirPath;
    @Value("${image.urlDirPath}")
    private String urlDirPath;  //虚拟网络路径

    /**
     *问题1：校验文件类型是否为图片，如何校验？
     * .jpg .gif .png 利用后缀校验
     * 问题2：防止恶意上传？
     * 将文件交给工具API获取宽高
     * 问题3：众多图片如何保存？
     * 按照年月日来创建文件夹来保存
     * 问题4：文件重名怎么办？
     * 自定义UUID作为文件名称
     *
     */
    @Override
    public EasyUI_Image fileUpload(MultipartFile uploadFile) {
        EasyUI_Image ui_Image = new EasyUI_Image();
        /**
         * 1.获取用户文件名称校验
         * 2.校验是否是图片
         * 3.判断文件的宽高
         * 4.利用日期格式来创建文件夹
         * 5.判断文件件是否存在，不存在，新建文件夹
         * 6.采用UUID为文件命名
         */
        //1.获取文件名称
        String fileName = uploadFile.getOriginalFilename();
        //2.校验文件名称，正则表达式 ； abcWUTOVM899.jpg  png  gif
        fileName = fileName.toLowerCase(); //将名字转换成小写
        if(!fileName.matches("^.+\\.(jpg|png|gif)$")){
            ui_Image.setError(1);//不符合图片正则表达式，表示文件上传有误
            return ui_Image;
        }
        //3.读取文件获取宽高 判断 ImageIO.read 根据输入流读取图片
        try {
            BufferedImage bufferedImage = ImageIO.read(uploadFile.getInputStream());
            int height = bufferedImage.getHeight();  //获取高
            int width = bufferedImage.getWidth();  //获取宽
            //判断 如果有一项为0 ，表示不是图片
            if(height==0||width==0){
                ui_Image.setError(1);
                return ui_Image;
            }
            //如果宽高有数据 封装图片数据
            ui_Image.setWidth(width).setHeight(height);
            //4.用日期格式创建文件夹  D:/1-jt/images/yyyy/MM/dd/abc.jpg
            String datePathDir = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            //真实文件夹路径
            String realDirPath = localDirPath + datePathDir;
            //判断文件夹是否存在 不存在则创建
            File dirFile = new File(realDirPath);
            if(!dirFile.exists()){
                dirFile.mkdirs();
            }
            //5.采用UUID命名，去掉中间的 - 号
            String uuid = UUID.randomUUID().toString().replace("-", "");
            //截取原文件名的后缀  用来拼接新的图片名称；810b399dc2c44adeba622ec2f36a6fee.jpg
            //得到文件类型 .jpg   .png  .gif
            String fileType = fileName.substring(fileName.lastIndexOf("."));
            String realName = uuid + fileType;   //拼接处真实文件名
            //6.实现文件上传
            String realFilePath = realDirPath + "/" + realName; //路径+名称
            File file = new File(realFilePath);
            uploadFile.transferTo(file);
            //7.封装url返回 http://image.jt.com/2020/2/26/abcbbbbbb.jpg
            String realUrlPath = urlDirPath + datePathDir + "/" + realName;
            ui_Image.setUrl(realUrlPath);
        } catch (Exception e) {
            e.printStackTrace();
            //出现异常返回对象
            return ui_Image;
        }
        return ui_Image;
    }
}
