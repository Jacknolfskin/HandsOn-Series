package com.personal.handson.multithreadingswitch;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class FTPUtil {


    private final static Log logger = LogFactory.getLog(FTPUtil.class);
    private static String ftpHost = "switch.biz.ring.ftp.host";
    private static Integer ftpPort = 0;
    private static String ftpUserName = "switch.biz.ring.ftp.user";
    private static String ftpPassword = "switch.biz.ring.ftp.passWord";

    StringBuffer orderSbOnline = new StringBuffer();
    StringBuffer orderSbOffLine = new StringBuffer();
    StringBuffer shorderSbOnline = new StringBuffer();
    StringBuffer shorderSbOffLine = new StringBuffer();

    /**
     * 获取FTPClient对象
     *
     * @return
     */
    public static FTPClient getFTPClient() {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient = new FTPClient();
            // 连接FTP服务器
            ftpClient.connect(ftpHost, ftpPort);
            // 登陆FTP服务器
            ftpClient.login(ftpUserName, ftpPassword);
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                logger.info("未连接到FTP，用户名或密码错误。");
                ftpClient.disconnect();
            } else {
                logger.info("FTP连接成功。");
            }
        } catch (SocketException e) {
            e.printStackTrace();
            logger.info("FTP的IP地址可能错误，请正确配置。");
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("FTP的端口错误,请正确配置。");
        }
        return ftpClient;
    }

    /**
     * 关闭链接
     */
    public static void close() {
        try {
            FTPClient ftpClient = getFTPClient();
            if (ftpClient != null && ftpClient.isConnected()) {
                ftpClient.disconnect();
            }
            System.out.println("成功关闭连接，服务器");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从FTP服务器下载文件
     *
     * @param ftpPath FTP服务器中文件所在路径 格式： ftptest/aa
     */
    public void SwithFtpFile(String ftpPath) {
        try {
            FTPClient ftpClient = getFTPClient();
            ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

            // 创建多个有返回值的任务
            List<Future> list = new ArrayList<Future>();
            // 中文支持
            ftpClient.setControlEncoding("UTF-8");
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            ftpClient.changeWorkingDirectory(ftpPath);

            String[] fs = ftpClient.listNames();
            System.out.println(Arrays.toString(fs));
            for (String ff : fs) {
                Callable c = new MyCallable(ff, ftpPath);
                // 执行任务并获取Future对象
                Future f = cachedThreadPool.submit(c);
                list.add(f);
            }
            // 关闭线程池,不允许再向线程池中增加线程
            cachedThreadPool.shutdown();
            // 获取所有并发任务的运行结果
            for (Future f : list) {
                // 从Future对象上获取任务的返回值，并输出到控制台
                System.out.println(">>>" + f.get().toString());
            }
            close();
        } catch (FileNotFoundException e) {
            logger.error("没有找到" + ftpPath + "文件");
            e.printStackTrace();
        } catch (SocketException e) {
            logger.error("连接FTP失败.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("文件读取错误或没有相关文件");
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    class MyCallable implements Callable<Object> {

        private String fileName;
        private String ftpPath;

        MyCallable(String fileName, String ftpPath) {
            this.fileName = fileName;
            this.ftpPath = ftpPath;
        }

        @Override
        public Object call() throws Exception {
            FTPClient ftpClient = getFTPClient();
            ftpClient.changeWorkingDirectory(ftpPath);
            System.out.println(">>>" + fileName + "任务启动");
            InputStream is = ftpClient.retrieveFileStream(fileName);
            if (StringUtils.contains(fileName, "SH")) {
                FileUtil.readFile(is, SwitchOrderTextDic.SH_ORDER, shorderSbOnline, shorderSbOffLine);
            } else {
                FileUtil.readFile(is, SwitchOrderTextDic.ORDER, orderSbOnline, orderSbOffLine);
            }
            return fileName + "任务执行完毕";
        }
    }

    /**
     * 删除ftp文件
     *
     * @param srcFname
     * @return
     */
    public static boolean removeFiles(String srcFname) {
        boolean flag = false;
        try {
            FTPClient ftpClient = getFTPClient();
            String[] fs = getFileNameList(srcFname);
            for (String ff : fs) {
                flag = ftpClient.deleteFile(ff);
            }
            ftpClient.logout();
        } catch (IOException e) {
            close();
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 返回FTP目录下的文件列表
     *
     * @param pathName
     * @return
     */
    public static String[] getFileNameList(String pathName) {
        try {
            FTPClient ftpClient = getFTPClient();
            //更换目录到当前目录
            ftpClient.changeWorkingDirectory(pathName);
            return ftpClient.listNames();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取目录下所有的文件名称
     *
     * @param filePath 指定的目录
     * @return 文件列表, 或者null
     */
    public static FTPFile[] getFileList(String filePath) {
        try {
            FTPClient ftpClient = getFTPClient();
            ftpClient.changeWorkingDirectory(filePath);
            FTPFile[] files = ftpClient.listFiles();
            return files;
        } catch (IOException e) {
            close();
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 批量上传文件
     *
     * @param ftpPath
     * @return
     */
    public static boolean uploadFiles(String ftpPath, String path) {
        FTPClient ftpClient = getFTPClient();
        File file = new File(path);
        boolean success = false;
        File[] files = file.listFiles();
        InputStream is = null;
        try {
            ftpClient.makeDirectory(ftpPath);
            ftpClient.changeWorkingDirectory(ftpPath);
            for (File file2 : files) {
                //文件写入流
                is = new FileInputStream(file2.getAbsolutePath());
                ftpClient.storeFile(file2.getName(), is);
                is.close();
                success = true;
                logger.info("文件:" + file2.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
            //连接关闭
            close();
        }
        return success;
    }
}
