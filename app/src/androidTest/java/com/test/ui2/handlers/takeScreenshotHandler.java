package com.test.ui2.handlers;

import android.os.Environment;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;

import com.google.gson.JsonObject;
import com.test.ui2.handlers.CommandHandler;
import com.test.ui2.utils.AndroidCommand;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

public class takeScreenshotHandler extends CommandHandler {
    public takeScreenshotHandler(UiDevice device){super(device);}

    @Override
    public JsonObject execute(AndroidCommand command, JsonObject resultJSON, HashMap<String, List<UiObject2>> objectMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, UiObjectNotFoundException, InstantiationException {
        System.out.println("takeScreenshot路径");
        actionJson = command.getActionParam();
        float scale=Float.parseFloat(actionJson.get("scale").toString().replace("\"","").trim());
        int quality=Integer.parseInt(actionJson.get("quality").toString().replace("\"","").trim());
        File screenShotFolder =new File(Environment.getExternalStorageDirectory(), "screenShot");
        File screenShotFile=new File(screenShotFolder,"screen.png");
        if(screenShotFolder.mkdirs()){
            System.out.println("创建目录成功");
        }else{
            System.out.println("创建目录失败");
        }
        if (screenShotFile.exists()) {
            screenShotFile.delete();
        }

        returnValue=String.valueOf(mDevice.takeScreenshot(screenShotFile,scale,quality));
        resultJSON.addProperty("value",returnValue);
        return resultJSON;
    }
}
