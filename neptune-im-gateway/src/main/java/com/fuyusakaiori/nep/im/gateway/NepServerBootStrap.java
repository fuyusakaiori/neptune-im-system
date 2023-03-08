package com.fuyusakaiori.nep.im.gateway;

import cn.hutool.core.util.StrUtil;
import com.example.neptune.im.common.constant.PathConstant;
import com.fuyusakaiori.nep.im.codec.config.NepServerBootStrapConfig;
import com.fuyusakaiori.nep.im.gateway.server.NepTcpServer;
import com.fuyusakaiori.nep.im.gateway.server.NepWebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * <h3>服务端启动类</h3>
 */
@Slf4j
public class NepServerBootStrap {

    private static final String DEFAULT_CONFIG_FILE_NAME = "config.yml";

    public static void main(String[] args) throws IOException {
        NepServerBootStrapConfig.NepServerConfig serverConfig = getServerBootStrapConfig().getServer();
        new NepTcpServer(serverConfig).start();
        new NepWebSocketServer(serverConfig).start();
    }

    private static NepServerBootStrapConfig getServerBootStrapConfig() throws IOException {
        // 1. 获取文件路径
        String configPath = getConfigPath(DEFAULT_CONFIG_FILE_NAME);
        // 2. 准备配置文件解析类
        Yaml yaml = new Yaml();
        // 3. 准备文件输入流
        InputStream inputStream = Files.newInputStream(Paths.get(configPath));
        // 4. 解析得到返回结果
        return yaml.loadAs(inputStream, NepServerBootStrapConfig.class);
    }

    private static String getConfigPath(String fileName){
        // 1. 获取类加载器
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        // 2. 加载配置文件
        URL url = classLoader.getResource(fileName);
        // 3. 获取配置文件路径
        if (Objects.isNull(url)){
            log.error("NepServerBootStrap getConfigPath: 配置文件不存在!");
            return StrUtil.EMPTY;
        }
        // 4. 处理文件路径
        return url.getPath().substring(1).replace(PathConstant.LINUX_PATH_SEPARATOR, PathConstant.WINDOWS_PATH_SEPARATOR);
    }



}
