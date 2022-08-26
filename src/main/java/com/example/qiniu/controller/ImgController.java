package com.example.qiniu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * @PROJECT_NAME: qiniu
 * @DESCRIPTION:
 * @USER: 韩冰
 * @DATE: 2022/8/26 0026 1:14
 */
@RestController
public class ImgController {

    //在七牛云绑定的域名
    String imgPath = "http://qiniu.hanbing777.top";

    @GetMapping(value = "/{url}/**")
    // eg: url = "img/202110122154999.png"
    public void GetImg(@PathVariable String url, HttpServletResponse response, HttpServletRequest request) throws IOException {

        // 不知道为什么会请求 http://qiniu.hanbing777.top/favicon.ico, 我选择禁用
        if (url.equals("favicon.ico")){
            return;
        }

        //得到忽略"/"的url
        String path = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();

        //设置图片格式,我的测试是:下面代码jpg和png都可以
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        URLConnection urlConnection = new URL(imgPath+path).openConnection();
        urlConnection.connect();
        InputStream inputStream = urlConnection.getInputStream();
        BufferedImage image = ImageIO.read(inputStream);
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            ImageIO.write(image, "jpg", outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
