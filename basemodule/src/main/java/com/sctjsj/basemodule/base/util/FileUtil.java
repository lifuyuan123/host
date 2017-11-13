package com.sctjsj.basemodule.base.util;

/**
 * Created by mayikang on 17/4/8.
 */

import com.alibaba.android.arouter.utils.TextUtils;

import java.util.regex.Pattern;

/**
 * 文件操作相关工具类
 */
public class FileUtil {

    /**
     * 根据 url 获取文件的扩展名
     * @param url
     * @return
     */
    public static String getFileExtensionFromUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            int fragment = url.lastIndexOf('#');
            if (fragment > 0) {
                url = url.substring(0, fragment);
            }

            int query = url.lastIndexOf('?');
            if (query > 0) {
                url = url.substring(0, query);
            }

            int filenamePos = url.lastIndexOf('/');
            String filename =
                    0 <= filenamePos ? url.substring(filenamePos + 1) : url;

            if (!filename.isEmpty() &&
                    Pattern.matches("[a-zA-Z_0-9\\.\\-\\(\\)\\%]+", filename)) {
                int dotPos = filename.lastIndexOf('.');
                if (0 <= dotPos) {
                    return filename.substring(dotPos + 1);
                }
            }
        }

        return "";
    }
}
