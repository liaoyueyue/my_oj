package org.example.myojssm.common.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * Description: 阿里对象存储工具类
 * User: liaoyueyue
 * Date: 2024-04-09
 * Time: 1:39
 */

@Component
@ConfigurationProperties(prefix = "aliyun.oss")
@Setter
public class AliyunOSSUtil implements InitializingBean {
    private String endpoint;
    private String bucketName;
    private String accessKeyId;
    private String accessKeySecret;
    private String urlPrefix;

    @Override
    public void afterPropertiesSet() {
        int insertPosition = endpoint.lastIndexOf('/') + 1;
        this.urlPrefix = endpoint.substring(0, insertPosition) + bucketName + '.' + endpoint.substring(insertPosition) + '/';
    }

    private OSS createOssClient() {
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

    public String upload(String targetFolder, String objectName, InputStream inputStream) {
        OSS ossClient = createOssClient();
        String targetPath = targetFolder + '/' + objectName;
        String returnUrl = "";
        try {
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, targetPath, inputStream);
            // 上传文件。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            returnUrl = urlPrefix + targetPath;
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return returnUrl;
    }

    public void delete(String targetFolder, String objectName) {
        // 创建OSSClient实例。
        OSS ossClient = createOssClient();
        try {
            // 删除文件或目录。如果要删除目录，目录必须为空。
            ossClient.deleteObject(bucketName, targetFolder + '/' + objectName);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
