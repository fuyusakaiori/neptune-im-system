package com.fuyusakaiori.nep.im.service.util.file;

import cn.hutool.core.util.StrUtil;

public class NepPathUtil {

    // 获取文件系统的路径分隔符
    public static String separator = System.getProperty("file.separator");

    public static String getImageBasePath(){
        // 1. 获取运行进程的操作系统
        String os = System.getProperty("os.name");
        // 2. 根据不同的操作系统设置不同的图片存储路径
        String basePath = os.startsWith("Windows") ? "d:/project/java/neptune-im-system/neptune-im-service/images/": "/home/fuyusakaiori/images/";
        // 3. 替换分隔符
        return basePath.replace("/", separator);
    }

    public static String getUserImagePath(int userId){
        return userId + StrUtil.DASHED + "avatar.png";
    }
}
