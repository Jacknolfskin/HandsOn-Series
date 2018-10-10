package com.personal.handson.multithreadingswitch;

import org.apache.commons.lang3.StringUtils;

import java.io.*;

public class FileUtil {

    private static String localTmp = "switch.biz.ring.path.localTmp";

    /**
     * 逐行读取文本
     */
    public static void readFile(InputStream inputStream, String code, StringBuffer sbOnline, StringBuffer sbOffLine) {
        String result = "";
        try {
            PrintWriter writerOnline;
            PrintWriter writerCancel;
            String oneline = "";
            String cancel = "";
            //用10M的缓冲读取文本文件
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 10 * 1024 * 1024);
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                //套餐替换
                if (StringUtils.containsAny(line, SwitchOrderTextDic.MOVE_FIVE_MUSIC, SwitchOrderTextDic.CARD_TEN_MUSIC)) {
                    result = StringUtils.replaceEach(line, SwitchOrderTextDic.twoList, new String[]{"2", "2"});
                } else if (StringUtils.contains(line, SwitchOrderTextDic.CARD_TWENTY_MUSIC)) {
                    result = StringUtils.replace(line, SwitchOrderTextDic.CARD_TWENTY_MUSIC, "3");
                } else if (StringUtils.contains(line, SwitchOrderTextDic.AB_MUSIC)) {
                    result = StringUtils.replace(line, SwitchOrderTextDic.AB_MUSIC, "4");
                } else {
                    result = StringUtils.replaceEach(line, SwitchOrderTextDic.oneList, new String[]{"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1"});
                }

                //运营商替换
                if (StringUtils.contains(result, SwitchOrderTextDic.MOVE)) {
                    result = StringUtils.replace(result, SwitchOrderTextDic.MOVE, "1");
                } else if (StringUtils.contains(result, SwitchOrderTextDic.UNICOM)) {
                    result = StringUtils.replace(result, SwitchOrderTextDic.UNICOM, "2");
                } else if (StringUtils.contains(result, SwitchOrderTextDic.TELECOM)) {
                    result = StringUtils.replace(result, SwitchOrderTextDic.TELECOM, "3");
                }

                //在网或退订替换为空
                if (StringUtils.contains(result, SwitchOrderTextDic.ONLINE)) {
                    result = StringUtils.replace(result, SwitchOrderTextDic.ONLINE, "");
                    //空值替换
                    if (StringUtils.contains(result, SwitchOrderTextDic.NULL)) {
                        result = StringUtils.replace(result, SwitchOrderTextDic.NULL, "");
                    } else {
                        result = StringUtils.substring(result, 0, result.length() - 20);
                    }
                    sbOnline.append(result + System.lineSeparator());
                } else {
                    result = StringUtils.replace(result, SwitchOrderTextDic.OFFONLINE, "");
                    sbOffLine.append(result + System.lineSeparator());
                }
            }
            //判断文件类型
            if (code.equals(SwitchOrderTextDic.ORDER)) {
                oneline = "Orders_online.txt";
                cancel = "Orders_cancel.txt";
            } else if (code.equals(SwitchOrderTextDic.SH_ORDER)) {
                oneline = "SH_Orders_online.txt";
                cancel = "SH_Orders_cancel.txt";
            }

            File fileDir = new File(localTmp);
            //如果不存在 则创建
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            writerOnline = new PrintWriter(localTmp + oneline, "UTF-8");
            writerCancel = new PrintWriter(localTmp + cancel, "UTF-8");
            writerOnline.println(sbOnline.toString());
            writerCancel.println(sbOffLine.toString());
            writerOnline.close();
            writerCancel.close();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除固定路径下所有文件
     *
     * @param path
     * @return
     */
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                //先删除文件夹里面的文件
                delAllFile(path + "/" + tempList[i]);
                //再删除空文件夹
                delFolder(path + "/" + tempList[i]);
                flag = true;
            }
        }
        return flag;
    }

    public static void delFolder(String folderPath) {
        try {
            //删除完里面所有内容
            delAllFile(folderPath);
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            //删除空文件夹
            myFilePath.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
