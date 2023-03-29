package com.learndm.nnsman.utils;

import android.content.Context;
import android.view.WindowManager;

/**
 * Created by anlia on 2017/12/11.
 */

public class ScreenUtils {
    /**
     * 获取屏幕宽度
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context){
        int width;
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        return width;
    }

    /**
     * 获取屏幕高度
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context){
        int height;
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        height = wm.getDefaultDisplay().getHeight();
        return height;
    }

    public static int getStatusBarHeight(Context context){
        int height = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = context.getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }
}
