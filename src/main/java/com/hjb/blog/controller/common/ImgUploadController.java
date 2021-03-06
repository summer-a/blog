package com.hjb.blog.controller.common;

import com.hjb.blog.entity.vo.ResultVO;
import com.hjb.blog.util.FTPUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 图片上传控制器
 * @author 胡江斌
 * @version 1.0
 * @title: ImgUploadController
 * @projectName blog
 * @description: TODO
 * @date 2019/8/19 15:16
 */
@RestController
@RequestMapping("/img")
public class ImgUploadController {

    @Value("${host.imageUrl}")
    private String imageHost;

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public ResultVO editorUpload(@RequestParam("file") MultipartFile file) {
        FTPUtils client = new FTPUtils();
        if (!file.isEmpty()) {
            // 获取后缀
            String suffix = getFileSuffix(file.getOriginalFilename());
            //
            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("/yyyy/MM/dd/"));
            // 新图片名
            String newFileName = FTPUtils.createFileName(suffix);
            try {
                // 上传返回服务器文件位置
                String serverFilePath = client.uploadFile("/blog", today, newFileName, file.getInputStream());
                // 返回格式要求
                Map<String, String> map = new HashMap<>(2);
                map.put("src", imageHost + serverFilePath);
                map.put("title", newFileName);
                return ResultVO.build(0, "success", map);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ResultVO.build(1, "上传失败", null);
    }

    /**
     * 获取文件后缀
     *
     * @param originalFilename 完整文件名
     * @return
     */
    private String getFileSuffix(String originalFilename) {
        return originalFilename.substring(originalFilename.lastIndexOf("."));
    }
}
