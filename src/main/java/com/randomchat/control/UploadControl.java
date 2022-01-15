package com.randomchat.control;

import cn.hutool.core.io.IoUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import com.randomchat.util.FileTypeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

@RestController
@Slf4j
public class UploadControl {
    @Value("${filepath}")
    private String FILE_PATH;

    @Autowired
    private AES aes;


    @GetMapping(value = "/file/{filePath}",produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getFile(@PathVariable String filePath, HttpServletResponse response) throws IOException {
        File file = new File(FILE_PATH+filePath);
        log.info("获取图片路径为:{}",filePath);

        if (file.exists()) {
            FileInputStream fileInputStream = new FileInputStream(file);
            return aes.decrypt(IoUtil.readBytes(fileInputStream));
        }
        return null;
    }

    @PostMapping("/uploadFile")
    public String upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return "上传失败!";
        }
        String ex = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString().concat(ex);
        if(FileTypeUtil.FileTypeMap.get(ex.toLowerCase(Locale.ROOT)).equals(FileTypeUtil.FILE_TYPE_IMG)){
            FileOutputStream fileOutputStream=new FileOutputStream(FILE_PATH.concat(filename));
            aes.encrypt(file.getInputStream(),fileOutputStream,true);
            return filename;
        }else if (FileTypeUtil.FileTypeMap.get(ex.toLowerCase(Locale.ROOT)).equals(FileTypeUtil.FILE_TYPE_VIDEO)){
            FileOutputStream fileOutputStream=new FileOutputStream(FILE_PATH.concat(filename));
            aes.encrypt(file.getInputStream(),fileOutputStream,true);
            return filename;
        }
        System.out.println(originalFilename);
        return "上传失败!";
    }

    public static void main(String[] args) throws Exception {
        String content = "0123456789ABHAEQ";

        AES aes = new AES(Mode.CTS, Padding.PKCS5Padding, "0CoJUm6Qyw8W8jud".getBytes(), "0102030405060708".getBytes());


        FileInputStream fileInputStream = new FileInputStream("pom.xml");
        FileOutputStream fileOutputStream = new FileOutputStream("pomjiami.xml");
        aes.encrypt(fileInputStream, fileOutputStream, true);
        FileInputStream pomjiami = new FileInputStream("pomjiami.xml");
        FileOutputStream pomjiemi = new FileOutputStream("pomjiemi.xml");
        aes.decrypt(pomjiami, pomjiemi, true);
    }
}
