package com.dcits.patchtools.svn;

import com.dcits.patchtools.svn.service.PatchService;
import com.dcits.patchtools.svn.util.DateUtil;
import com.dcits.patchtools.svn.util.FileUtil;
import com.dcits.patchtools.svn.util.VerUtil;
import com.dcits.patchtools.svn.util.YamlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.util.*;

/**
 * @author Kevin
 * @date 2018-05-08 09:18.
 * @desc 增量抽取
 * @email chenkunh@dcits.com
 */
public class PatchHandler {
    private static final Logger logger = LoggerFactory.getLogger(PatchHandler.class);

    private List<PatchService> patchServices;
    private Map<String, Object> yamlConf;
    private String yamlPath;
    private String yamlSurfix;
    private String targetPath;
    private String targetSrcPath;
    private String patchTmpFolderName;
    private String patchFolderName;
    private String patchZipName;
    private String delFileName;
    private String  patchVerName;
    /**
     * 依次执行各套配置生成增量清单和送测清单
     *
     * @param baseDir
     * @param versionFrom
     * @param versionTo
     * @return
     */
    public boolean patchListAndReport(String baseDir, long versionFrom, long versionTo) {
        boolean res = true;
        for (PatchService patchService : patchServices) {
            res = res & patchService.genPatchListAndReport(baseDir, versionFrom, versionTo);
        }
        return res;
    }

    /**
     * 统一执行增量抽取工作
     *
     * @param baseDir
     * @param versionFrom
     * @param versionTo
     * @return
     */
    public boolean patchExecute(String baseDir, long versionFrom, long versionTo, String yamlPrefix) {
        logger.info("开始读取增量描述文件...");
        boolean res = true;
        Set<String> patchSet = new HashSet<>();
        for (PatchService patchService : patchServices) {
            Set<String> set = patchService.executePatch(baseDir, versionFrom, versionTo);
            patchSet.addAll(set);
        }
        logger.info("开始读取三方包增量描述文件...");
        String[] yamls = yamlPrefix.split("#");
        Set<String> matchSet = new HashSet<>();
        Set<String> deleteSet = new HashSet<>();
        for (String prefix : yamls) {
            String yamlSource = baseDir.concat(yamlPath) + File.separator
                    + prefix.concat(yamlSurfix).concat(".yml");
            YamlHelper.yamlPaser(yamlSource, patchSet, matchSet, deleteSet);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("最终待抽取的增量清单数目：" + patchSet.size());
            for (String tmp : patchSet) logger.debug(tmp);

            logger.debug("模糊匹配清单：");
            for (String tmp : matchSet) logger.debug(tmp);

            logger.debug("删除文件清单：");
            for (String tmp : deleteSet) logger.debug(tmp);
        }

        // 创建增量文件临时存放目录
        String tmpDir = targetPath + "/" + patchTmpFolderName;
        File insTmpDir = new File(baseDir + tmpDir);
        File insAppDir = new File(insTmpDir.getAbsolutePath() + "/" + patchFolderName);
        if (insTmpDir.exists()) {
            logger.info("删除目录 ：" + insTmpDir.getAbsolutePath());
//            insTmpDir.delete();
            FileUtil.deleteDir(insTmpDir);
        }
        insTmpDir.mkdir();
        insAppDir.mkdir();

        // 复制部署包增量文件目录结构
        logger.info("复制目录结构到 :" + insTmpDir.getAbsolutePath());
        FileUtil.copyFolder(new File(baseDir + targetSrcPath), insAppDir);

/*
        // 移动具体文件的增量文件到临时存放目录
        FileUtil.mvFiles(baseDir + targetSrcPath, insAppDir.getPath(), patchSet);
*/

        matchSet.addAll(patchSet);
        // 移动模糊匹配文件到临时存放目录
        FileUtil.mvMatchFiles(baseDir,
                targetSrcPath,
                targetPath.concat("/").concat(patchTmpFolderName).concat("/").concat(patchFolderName),
                matchSet, deleteSet);

        // 指定文件夹下的文件全部抽取（不递归子目录）
        Object patchFullObj = yamlConf.get("patchFull");
        if (patchFullObj instanceof Map) {
            Map<String, String> patchFullMap = (Map) patchFullObj;
            Iterator<Map.Entry<String, String>> iterator = patchFullMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                String key = entry.getKey();
                String value = entry.getValue();
                FileUtil.mvFiles(baseDir,
                        targetSrcPath.concat("/").concat(key),
                        targetPath
                                .concat("/").concat(patchTmpFolderName)
                                .concat("/").concat(patchFolderName)
                                .concat("/").concat(value));
            }
        }

        // 进行deleteList文件生成的处理
        String delFileDir = baseDir + targetPath.concat("/")
                + patchTmpFolderName.concat("/")
                + patchFolderName.concat("/")
                + delFileName;
        FileUtil.writeFile(delFileDir, deleteSet);

        // 生成增量部署zip包
       // String zipName = targetPath + "/" + patchZipName;
        String zipName = targetPath + "/" +patchFolderName+"_INC_"+ VerUtil.getVersionFile(patchFolderName,patchVerName)+"_"+DateUtil.getRunDate()+".zip";
        if (logger.isDebugEnabled()) {
            logger.debug("最终patchFolderName：" +patchFolderName);
            logger.debug("最终patchVerName：" +patchVerName);
            logger.debug("最终待zipName：" +zipName);
        }
        ZipUtil.pack(new File(baseDir + tmpDir), new File(baseDir + zipName));

        return res;
    }

    public String getYamlPath() {
        return yamlPath;
    }

    public void setYamlPath(String yamlPath) {
        this.yamlPath = (Objects.equals("@", yamlPath) ? "" : yamlPath)
                .replaceAll("yyyyMMdd", DateUtil.getRunDate());
    }


    public List<PatchService> getPatchServices() {
        return patchServices;
    }

    public void setPatchServices(List<PatchService> patchServices) {
        this.patchServices = patchServices;
    }

    public Map<String, Object> getYamlConf() {
        return yamlConf;
    }

    public void setYamlConf(Map<String, Object> yamlConf) {
        this.yamlConf = yamlConf;
    }

    public String getYamlSurfix() {
        return yamlSurfix;
    }

    public void setYamlSurfix(String yamlSurfix) {
        this.yamlSurfix = yamlSurfix;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public String getTargetSrcPath() {
        return targetSrcPath;
    }

    public void setTargetSrcPath(String targetSrcPath) {
        this.targetSrcPath = targetSrcPath;
    }

    public String getPatchTmpFolderName() {
        return patchTmpFolderName;
    }

    public void setPatchTmpFolderName(String patchTmpFolderName) {
        this.patchTmpFolderName = patchTmpFolderName;
    }

    public String getPatchFolderName() {
        return patchFolderName;
    }

    public void setPatchFolderName(String patchFolderName) {
        this.patchFolderName = patchFolderName;
    }

    public String getPatchZipName() {
        return patchZipName;
    }

    public void setPatchZipName(String patchZipName) {
        this.patchZipName = patchZipName;
    }

    public String getDelFileName() {
        return delFileName;
    }

    public void setDelFileName(String delFileName) {
        this.delFileName = delFileName;
    }

    public String getPatchVerName() {
        return patchVerName;
    }

    public void setPatchVerName(String patchVerName) {
        this.patchVerName = patchVerName;
    }
}
