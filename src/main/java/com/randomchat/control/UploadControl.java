package com.randomchat.control;

import cn.hutool.core.io.FileUtil;
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
        if(FileTypeUtil.FILE_TYPE_IMG.equals(FileTypeUtil.FileTypeMap.get(ex.toLowerCase(Locale.ROOT)))){
            File tempFile = FileUtil.touch(FILE_PATH.concat(filename));
            FileOutputStream fileOutputStream=new FileOutputStream(tempFile);
            aes.encrypt(file.getInputStream(),fileOutputStream,true);
            return filename;
        }else if (FileTypeUtil.FILE_TYPE_VIDEO.equals(FileTypeUtil.FileTypeMap.get(ex.toLowerCase(Locale.ROOT)))){
            File tempFile = FileUtil.touch(FILE_PATH.concat(filename));
            FileOutputStream fileOutputStream=new FileOutputStream(tempFile);
            aes.encrypt(file.getInputStream(),fileOutputStream,true);
            return filename;
        }
        return "上传失败!格式不支持!";
    }
}
