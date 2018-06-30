package com.dcits.patchtools.svn.service.impl;

import com.dcits.patchtools.svn.TestBase;
import com.dcits.patchtools.svn.model.FileBlame;
import com.dcits.patchtools.svn.service.PathRuleService;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Kevin
 * @date 2018-05-04 19:13.
 * @desc
 * @email chenkunh@dcits.com
 */
public class PathRuleServiceImplTest extends TestBase {

    @Test
    public void pathExcludeFilter() {
        PathRuleService pathRuleService = context.getBean(PathRuleServiceImpl.class);
        boolean res = pathRuleService.pathExcludeFilter("abc.xls");
        logger.info(String.valueOf(res));
    }

    @Test
    public void pathListOnlyFilter() {
    }

    @Test
    public void pathConvert() {
        String srcPath1="/SmartEnsemble15_CQBANK/Ensemble/Project/trunk/modelBank-teller/SmartTeller9/function/3D/3252/config";
        String srcPath2="/SmartEnsemble15_CQBANK/Ensemble/Project/trunk/modelBank-teller/SmartTeller9/function/5D/5254/config";
        List<FileBlame> fileBlameList = new ArrayList<>();
        FileBlame fileBlame = new FileBlame();
        fileBlame.setSrcPath(srcPath1);
        fileBlameList.add(fileBlame);
        fileBlame = new FileBlame();
        fileBlame.setSrcPath(srcPath2);
        fileBlameList.add(fileBlame);
        PathRuleService pathRuleService = context.getBean(PathRuleServiceImpl.class);
         pathRuleService.pathConvert(fileBlame);

    }
}