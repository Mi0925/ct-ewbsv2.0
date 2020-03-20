package cn.comtom.tools.utils;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;

import java.io.File;

public class FileUtils {
    /**
     * 音频文件获取文件时长（秒）
     * @param source
     * @return
     */
    public static Long getDuration(File source) {
        Encoder encoder = new Encoder();
        long ls = 0;
        MultimediaInfo m;
        try {
            m = encoder.getInfo(source);
            ls = m.getDuration()/1000;
        } catch (Exception e) {
            System.out.println("获取音频时长有误：" + e.getMessage());
        }
        return ls;
    }

}
