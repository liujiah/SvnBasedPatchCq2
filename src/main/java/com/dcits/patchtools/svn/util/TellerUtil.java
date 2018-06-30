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
    private static final String tl00="/SmartEnsemble15_CQBANK/Ensemble/Project/trunk/modelBank-teller/SmartTeller9/frame_work/inter/application/lgr/sss.lgdict";
    private static final String tl1="SmartTeller9/function";
    private static final String tl2="SmartTeller9/frame_work";
    private static final String tl3="SmartTeller9/config";

    public static Set<String> patchFileReader(List<FileBlame> list, String filepath) {
        Set<String> set = new HashSet<>();
        String strName="";
        for (FileBlame model : list) {
            if(model.getSrcPath().indexOf(tl1)>0){
                logger.info("patchFileReader_1: " +model.getSrcPath());
                String[] arrName=model.getSrcPath().split("/");
                strName = "SmartTeller9\\trans\\"+arrName[8]+"\\"+arrName[9]+".jar";
            }

            if(model.getSrcPath().indexOf(tl2)>0){
                logger.info("patchFileReader_2: " +model.getSrcPath());
                String strNametmp=model.getSrcPath().substring(model.getSrcPath().indexOf(tl2));
                strNametmp=strNametmp.replaceFirst("/frame_work/","/");
                strName = strNametmp.replace("/","\\");
            }
            logger.info("patchFileReader_3: " +strName);
            if(!deletelist(strName,filepath)){
                //如果不在删除清单则抽取
                logger.info("patchFileReader_4: " +strName);
                set.add(strName);
            }else{
                logger.info("patchFileReader_5 deletelist: " +strName);
            }

        }
        logger.info("解析后清单数目: " + set.size());
        return set;
    }

    public static void genFile(List<FileBlame> list, String filepath) {
        String fileFullName = filepath+"app_"+DateUtil.getRunDate()+".txt";
        logger.info("fileFullName:"+fileFullName);
        Set<String> content= patchFileReader(list,filepath);
       if(content.size()>0){
           FileUtil.writeFileTeller(fileFullName,content);
       }
    }

    public static boolean deletelist(String str,String filepath){
        Set<String> patchSet = new HashSet<>();
        Set<String> matchSet = new HashSet<>();
        Set<String> deleteSet = new HashSet<>();
        String yamlSource = filepath +"teller.yml";
        YamlHelper.yamlPaser(yamlSource, patchSet, matchSet, deleteSet);
        logger.info("patchFileReader_deletelist  0: " +str);
        for (String list : deleteSet) {
            if(str.indexOf(list)>0){
                logger.info("patchFileReader_deletelist1: " +list);
                return true;
            }else{
                logger.info("patchFileReader_deletelist2: " +list);
                return false;
            }

        }
        return false;
    }

}
