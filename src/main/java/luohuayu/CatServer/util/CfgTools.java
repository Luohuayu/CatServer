package luohuayu.CatServer.util;

import java.io.File;

public class CfgTools{

    public static File mergeFile(File pDir,File pFile){
        return pFile.isAbsolute()?pFile
                :new File(pDir,pFile.getPath());
    }

}
