package com.tzxlearn.reggie.controller;

import com.tzxlearn.reggie.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * @description: 用作文件上传下载的
 * @author: 田正轩
 * @time: 2023/2/20 17:47
 */
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;


    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {//参数名不能随便起

        //获取上传时的文件名然后再组合一个
        String originalFilename = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        //根据配置文件创建目录
        File dir = new File(basePath);
        if (!dir.exists()) {
            //文件夹不存在，创建一个
            dir.mkdirs();
        }

        //将临时文件转存到指定位置
        try {
            file.transferTo(new File(basePath + uuid + suffix));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //返回文件名存储的菜品数据库表中
        return R.success(uuid + suffix);

    }

    /**
     * 文件下载（上传完成后显示在浏览器）
     */
    @GetMapping("download")
    public void download(String name, HttpServletResponse response) {

        try {
            //通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
            //通过输出流写入到浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
            //关闭资源
            fileInputStream.close();
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
