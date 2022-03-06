package com.zl.minio.config;



import cn.hutool.core.io.file.FileNameUtil;

import com.zl.minio.service.MinioService;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
/**
 * @Description //TODO 上传策略类
 */
@Component
public class UploadFile {

    @Autowired
    private MinioService minioService;

    @Autowired
    private MinioConfig minioConfig;

    @SneakyThrows
    public  void uploadFile(MultipartFile file, String bucketName) {

        // 获取存储桶名称
        bucketName = StringUtils.isNotBlank(bucketName) ? bucketName : minioConfig.getBucketName();

        // 判断是否存在该存储桶
        if (!minioService.bucketExists(bucketName)) {
            // 不存在则创建
            minioService.makeBucket(bucketName);
            // 设置存储桶只读策略
            String bucketPolicy ="{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"" +
                    "Action\":[\"s3:GetBucketLocation\",\"s3:ListBucket\"],\"Resource\":[\"arn:aws:s3:::fast\"]},{\"Effect\":\"Allow\",\"" +
                    "Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetObject\"],\"Resource\":[\"arn:aws:s3:::" +
                    bucketName+"/*\"]}]}";
            // 设置存储桶策略
            minioService.setBucketPolicy(bucketName,bucketPolicy);
        }
        // 原始文件名
        String fileName = file.getOriginalFilename();
        // 获取文件后缀名
        String extName = FileNameUtil.extName(fileName);
        // 定义文件路径
        String format = new SimpleDateFormat("yyyy/MM/dd/").format(new Date());
        // 定义文件修改之后的名字,去除uuid中的' - '
        String fileUUID = UUID.randomUUID().toString().replaceAll("-", "");
        // 定义新的文件名
        String objectName = format + fileUUID + "." + extName;
        //上传文件
        minioService.putObject(bucketName, file, objectName);
        // 在控制台打印图片绝对路径
//        System.out.println("http://127.0.0.1:9000/"+bucketName+"/"+objectName);
        System.out.println(minioConfig.getEndpoint()+"/"+bucketName+"/"+objectName);
    }

}