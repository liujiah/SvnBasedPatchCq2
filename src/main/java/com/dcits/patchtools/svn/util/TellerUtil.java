package com.dcits.patchtools.svn.util;

import com.dcits.patchtools.svn.model.FileBlame;
import com.dcits.patchtools.svn.model.FileModel;
import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.*;

public class TellerUtil {
    private static final Logger logger = LoggerFactory.getLogger(TellerUtil.class);
    private static final String tl0="/SmartEnsemble15_CQBANK/Ensemble/Project/trunk/modelBank-teller/SmartTeller9/function/3D/3252/config";
    private static final String tl1="SmartTeller9/function";
    private static final String tl2="SmartTeller9/config";

    public static Set<String> patchFileReader(List<FileBlame> list) {
        Set<String> set = new HashSet<>();
        String strName="";
        for (FileBlame model : list) {
            if(model.getSrcPath().indexOf(tl1)>0){
                String[] arrName=model.getSrcPath().split("/");
                strName = "SmartTeller9\\trans\\"+arrName[8]+"\\"+arrName[9]+".jar";
            }
            set.add(strName);
        }
        logger.info("解析后清单数目: " + set.size());
        return set;
    }

    public static void genFile(List<FileBlame> list, String filepath) {
        String fileFullName = filepath+"app_"+DateUtil.getRunDate()+".txt";
        logger.info("fileFullName:"+fileFullName);
        Set<String> content= patchFileReader(list);
       if(content.size()>0){
           FileUtil.writeFile(fileFullName,content);
       }
    }

}
