package com.dcits.patchtools.svn.util;

import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class ChlFileDeal {
    public static void main(String[] args) {
        genChlFile(args[0], args[1]);
       //genChlFile("d:\\1.txt~", "1");
       /* String reg="(09[0-9]{4}|[1-9]{1}[0-9]{5}).jar";
        String lineTxt="ffffffff\\ffff\\090123.jar";
        String lineTxt2="ffffffff\\ffff\\070123.jar";
        String lineTxt3="ffffffff\\ffff\\0123.jar";
        String lineTxt4="ffffffff\\ffff\\110123.jar";
        String lineTxt5="ffffffff\\ffff\\310123.jar";
       System.out.println( lineTxt.substring(lineTxt.length()-10).matches(reg));
        System.out.println( lineTxt2.substring(lineTxt2.length()-10).matches(reg));
        System.out.println( lineTxt3.substring(lineTxt3.length()-10).matches(reg));
        System.out.println( lineTxt4.substring(lineTxt4.length()-10).matches(reg));
        System.out.println( lineTxt5.substring(lineTxt5.length()-10).matches(reg));*/
    }

    public static void genChlFile(String filepath, String chl) {
        Set<String> set = new HashSet<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filepath))));
            String lineTxt=null;
            while ((lineTxt=br.readLine())!=null){
                String reg="(09[0-9]{4}|[1-9]{1}[0-9]{5}).jar";
                String msg="SmartTeller9\\trans";
                boolean bn=lineTxt.substring(lineTxt.length()-10).matches(reg);
                int num=lineTxt.indexOf(msg);
                if(bn) {
                    if (chl.equals("1")) {
                        //导出渠道交易
                        set.add(lineTxt);
                    }else {
                        continue;
                    }
                }else {
                    if (chl.equals("1")) {
                       continue;
                    }else {
                        set.add(lineTxt);
                    }
                }
            }
            writeFileTeller(filepath + "～",set);
        } catch (Exception e) {
            e.printStackTrace();
            ;
        }
    }


    public static void writeFileTeller(String writeFile, Set<String> content) {
        File file = new File(writeFile);
        if (file.exists() && file.isFile()) {
             file.delete();
        }
        /* 输出数据 */
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file,true), "UTF-8"));
            bw.newLine();
            for (String line : content) {
                bw.write(line);
                bw.newLine();
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
