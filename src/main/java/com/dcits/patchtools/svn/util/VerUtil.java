package com.dcits.patchtools.svn.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;


public class VerUtil {
    private static final Logger logger = LoggerFactory.getLogger(VerUtil.class);

    public static String getVersionFile(String patchFolderName,String filePath) {
       // String filePath="D://baktmp//version.properties";
        logger.info("获取版本号文件filePath:"+filePath);
        String svnVerSionFile = filePath;
        Properties pps = new Properties();
        String VersionNum = "";
        File file =null;
        Writer fw = null;
        try {
            pps.load(new FileInputStream(svnVerSionFile));
            VersionNum = pps.getProperty(patchFolderName);
            String VersionNumNew = VersionNum.replace(".", "");
            int ver = Integer.parseInt(VersionNumNew) + 1;
            String Version = fillString(Integer.toString(ver), "0", 6, true);
            VersionNum = Version.substring(0, 2) + "." + Version.substring(2, 4) + "." + Version.substring(4, 6);
            pps.setProperty(patchFolderName,VersionNum);
            System.out.println(pps.toString());
            file = new File(filePath);
            fw = new FileWriter(file);
            pps.store(fw,pps.toString());
        } catch (Exception e) {
            logger.info("获取版本号文件失败！");
            e.printStackTrace();
        }finally {
            try {
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return VersionNum;
    }


    public static  String fillString(String string, String pattern, int length,
                                    boolean left) throws Exception {
        if (pattern == null || pattern.equals(""))
            return string;
        StringBuffer sb = new StringBuffer("");
        string = string == null ? "" : string;
        if (left == true) {
            if (length > string.length()) {
                for (int i = 0; i < length - string.length(); i++)
                    sb.append(pattern);
            }
            sb.append(string);
        } else {
            sb.append(string);
            if (length > string.length()) {
                for (int i = 0; i < length - string.length(); i++)
                    sb.append(pattern);
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String filePath="D://baktmp//version.properties";
        System.out.println(getVersionFile("accounting",filePath));
        System.out.println(getVersionFile("ensemble",filePath));
    }

}
