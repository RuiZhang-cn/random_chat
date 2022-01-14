package com.randomchat.control;

import com.randomchat.util.FileTypeUtil;
import com.randomchat.websocket.WebSocketServer;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

@RestController
public class UploadControl {

    @GetMapping("/file/{filePath}")
    public String getFile(@PathVariable String filePath, HttpServletResponse response) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            FileInputStream fileInputStream = new FileInputStream(file);
            // 创建流的最大字节数组
            byte[] inOutBytes = new byte[fileInputStream.available()];
            fileInputStream.read(inOutBytes);
            response.getOutputStream().write(inOutBytes);
        }
        return "文件不存在!";
    }

    @PostMapping("/uploadFile")
    public String upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return "上传失败!";
        }
        String ex = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
        if(FileTypeUtil.FileTypeMap.get(ex.toLowerCase(Locale.ROOT)).equals(FileTypeUtil.FILE_TYPE_IMG)){
            File file1 = new File(originalFilename);
            if (file1.exists()){
                file1=new File(UUID.randomUUID().toString().concat(ex));
            }
            file.transferTo(file1);
            return file1.getPath();
        }
        System.out.println(originalFilename);
        return "上传失败!";
    }
}
