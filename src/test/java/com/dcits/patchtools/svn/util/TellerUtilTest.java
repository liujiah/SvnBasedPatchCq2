package com.dcits.patchtools.svn.util;

import com.dcits.patchtools.svn.TestBase;
import com.dcits.patchtools.svn.model.FileBlame;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.beans.factory.config.YamlMapFactoryBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TellerUtilTest extends TestBase{

        @Test
        public void yamlTest() {
            String srcPath1="/SmartEnsemble15_CQBANK/Ensemble/Project/trunk/modelBank-teller/SmartTeller9/function/3D/3252/config";
           // String srcPath2="/SmartEnsemble15_CQBANK/Ensemble/Project/trunk/modelBank-teller/SmartTeller9/function/5D/5254/config";
            String srcPath2="/SmartEnsemble15_CQBANK/Ensemble/Project/trunk/modelBank-teller/SmartTeller9/frame_work/inter/application/lgr/sss.lgdict";
            List<FileBlame> fileBlameList = new ArrayList<>();
            FileBlame fileBlame = new FileBlame();
            fileBlame.setSrcPath(srcPath1);
            fileBlameList.add(fileBlame);
            fileBlame = new FileBlame();
            fileBlame.setSrcPath(srcPath2);
            fileBlameList.add(fileBlame);
            //Set<String> set= TellerUtil.patchFileReader(fileBlameList);
            //logger.info(set.toString());
            TellerUtil.genFile(fileBlameList,"d://");
        }
    }
