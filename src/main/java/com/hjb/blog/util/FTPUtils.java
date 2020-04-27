package com.hjb.blog.util;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.Properties;
import java.util.Random;

/**
 * @author 胡江斌
 * @version 1.0
 * @title: FTPUtils
 * @projectName blog
 * @description: TODO
 * @date 2019/8/19 15:46
 */
public class FTPUtils {

    /**
     * FTP登录账号
     */
    private String username;
    /**
     * FTP登录密码
     */
    private String password;
    /**
     * FTP服务器hostname
     */
    private String host;
    /**
     * FTP服务器端口
     */
    private int port;

    /**
     * 客户端
     */
    private FTPClient ftp;

    /**
     * 配置
     */
    private Properties properties = CommonUtils.getProperties();

    /**
     * 基础路径
     */
    private static final String FTP_BASE_PATH = "/";


    public FTPUtils() {
        String username = properties.getProperty("ftp.username");
        String password = properties.getProperty("ftp.password");
        String host = properties.getProperty("ftp.host");
        int port = Integer.parseInt(properties.getProperty("ftp.port"));

        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
        ftp = this.connect();
    }

    public FTPUtils(String username, String password, String host, int port) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
        ftp = this.connect();
    }

    public FTPClient getFtpClient() {
        return this.ftp;
    }

    /**
     * Description: 向FTP服务器上传文件<br>
     *     使用默认/根目录
     * @param filePath FTP服务器文件存放路径。例如分日期存放：/2015/01/01。文件的路径为basePath+filePath
     * @param filename 上传到FTP服务器上的文件名
     * @param input 输入流
     * @return 成功返回路径(格式 ： / goods / master / xxx.jpg)，否则返回null
     */
    public String uploadFile(String filePath, String filename, InputStream input) {
        return uploadFile(FTP_BASE_PATH, filePath, filename, input);
    }

    /**
     * 根据完整路径删除文件
     * @param fullPath 完整路径，例：/goods/master/xx/xx.jpg
     * @return 是否成功
     */
    public boolean deleteFile(String fullPath) {
        return deleteFile(null, fullPath);
    }

    /**
     * Description: 向FTP服务器上传文件
     *
     * @param basePath FTP服务器基础目录
     * @param filePath FTP服务器文件存放路径。例如分日期存放：/2015/01/01。文件的路径为basePath+filePath
     * @param filename 上传到FTP服务器上的文件名
     * @param input    输入流
     * @return 成功返回路径，否则返回null
     */
    public String uploadFile(String basePath, String filePath, String filename, InputStream input) {
        String result = null;
        FTPClient ftp = connect();
        if (null == ftp) return null;
        ftp.setControlEncoding(StandardCharsets.UTF_8.toString());
        ftp.enterLocalPassiveMode();
        try {
            if (filePath.indexOf("/") != 0) filePath = "/" + filePath;
            // 完整路径
            String full_path = basePath + filePath;
            full_path = full_path.replaceAll("//", "/");
            // 判断完整路径是否存在,存在则直接上传
            if (!ftp.changeWorkingDirectory(full_path)) {
                //如果目录不存在创建目录
                String[] dirs = full_path.split("/");
                String tempPath = "";
                for (String dir : dirs) {
                    if (null == dir || "".equals(dir)) continue;
                    tempPath += "/" + dir;
                    if (!ftp.changeWorkingDirectory(tempPath)) {
                        if (!ftp.makeDirectory(new String(tempPath.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1))) {
                            return null;
                        } else {
                            ftp.changeWorkingDirectory(tempPath);
                        }
                    }
                }
            }
            //设置上传文件的类型为二进制类型
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            //上传文件
            if (!ftp.storeFile(new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1), input)) {
                return null;
            }
            input.close();
            result = (full_path + (filename.indexOf("/") != 0 ? ("/" + filename) : filename)).replaceAll("//", "/");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            logout();
        }
        return result;
    }

    /**
     * 从FTP服务器下载文件
     *
     * @param remotePath FTP服务器上的相对路径
     * @param fileName   要下载的文件名
     * @return InputStream
     */
    public InputStream downloadFile(String remotePath, String fileName) {
        FTPClient ftp = connect();
        if (null == ftp) return null;
        InputStream is = null;
        try {
            //ftp.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
            // 切换为主动模式
            ftp.enterLocalActiveMode();

            is = ftp.retrieveFileStream(remotePath + "/" + fileName);
            ftp.getReply();
            return is;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            logout();
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 删除文件
     *
     * @param path     文件路径
     * @param fileName 文件名
     * @return 是否成功
     */
    public boolean deleteFile(String path, String fileName) {
        FTPClient ftp = connect();
        try {
            if (null != path) {
                ftp.changeWorkingDirectory(path);
            }
            // 如果文件名为空，则删除整个文件夹...暂不做
            return ftp.deleteFile(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            logout();
        }
        return false;
    }

    /**
     * 登陆FTP
     *
     * @return FTPClient
     */
    private FTPClient connect() {
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(host, port);
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
            // 登录
            ftp.login(username, password);
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return ftp;
    }

    /**
     * ftp登出
     *
     */
    private void logout() {
        if (ftp != null) {
            try {
                ftp.logout();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (ftp.isConnected()) {
                    try {
                        ftp.disconnect();
                    } catch (IOException ioe) {
                    }
                }
            }
        }
    }

    /**
     * 创建文件名
     * @param suffix 源文件后缀
     * @return
     */
    public static String createFileName(String suffix) {
        LocalTime now = LocalTime.now();
        int i = new Random().nextInt(10000);
        return "" + now.toNanoOfDay() + i + suffix;
    }

}
