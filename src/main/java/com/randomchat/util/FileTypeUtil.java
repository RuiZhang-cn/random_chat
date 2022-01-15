package com.randomchat.util;

import java.util.HashMap;

public class FileTypeUtil {
    public static final String FILE_TYPE_IMG = "img";
    public static final String FILE_TYPE_VIDEO = "video";

    public static HashMap<String,String> FileTypeMap=new HashMap<String,String>(){{
        put(".jpg", FILE_TYPE_IMG);
        put(".jpeg", FILE_TYPE_IMG);
        put(".png", FILE_TYPE_IMG);
        put(".gif", FILE_TYPE_IMG);
        put(".bmp", FILE_TYPE_IMG);
        put(".mp4", FILE_TYPE_VIDEO);
        put(".flv", FILE_TYPE_VIDEO);
        put(".avi", FILE_TYPE_VIDEO);
        put(".mov", FILE_TYPE_VIDEO);
        put(".wmv", FILE_TYPE_VIDEO);
    }};
}
