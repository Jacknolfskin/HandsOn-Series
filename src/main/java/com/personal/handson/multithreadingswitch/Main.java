package com.personal.handson.multithreadingswitch;

import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Auther: ifly
 * @Date: 2018/10/10 09:52
 * @Description:
 */
public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);
    private static String frontSource = "switch.biz.ring.path.front.source";
    private static String localTmp = "switch.biz.ring.path.localTmp";
    private static String backSource = "switch.biz.ring.path.back.source";

    public static void main(String[] args) {
        try {
            new FTPUtil().SwithFtpFile(frontSource);
            logger.info("任务【{}】文件读取转化完成并写入临时区，准备上传到FTP >>>>>>>>>>");
            FTPUtil.uploadFiles(backSource + "/", localTmp);
            FTPFile[] list = FTPUtil.getFileList(localTmp + "/");
            for (FTPFile file : list) {
                if (file.isFile()) {
                    logger.info("CompleteFileName+" + new String(file.getName().getBytes("iso-8859-1"), "GB2312"));
                }
            }
            logger.info("任务【{}】上传完成，开始删除原文件 >>>>>>>>>");
            //删除临时区
            FileUtil.delAllFile(localTmp);
            //转化完成删除原文件
            FTPUtil.removeFiles(frontSource);
            logger.info("任务【{}】处理结束 >>>>>>>>>");
        } catch (Throwable ex) {
            logger.error("Switch构建出现异常：", ex);
        }
    }
}
