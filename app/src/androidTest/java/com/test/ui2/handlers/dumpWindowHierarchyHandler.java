package com.test.ui2.handlers;

import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiObject2;
import com.google.gson.JsonObject;
import com.test.ui2.utils.AndroidCommand;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.io.File;
import android.os.Environment;
public class dumpWindowHierarchyHandler extends CommandHandler {
    public dumpWindowHierarchyHandler(UiDevice device){
        super(device);
    }
    @Override
    public JsonObject execute(AndroidCommand command, JsonObject resultJSON, HashMap<String, List<UiObject2>> objectMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, UiObjectNotFoundException, InstantiationException {
        actionJson = command.getActionParam();
        dumpFileName=actionJson.get("dumpFileName").toString().replace("\"", "");
        dumpFolder = new File(Environment.getDataDirectory(), "local/tmp");
        dumpFile = new File(dumpFolder, dumpFileName);
        dumpFolder.mkdirs();
        if (dumpFile.exists()) {
            dumpFile.delete();
        }
        try {
            // dumpWindowHierarchy often has a NullPointerException
            mDevice.dumpWindowHierarchy(dumpFile);
        } catch (Exception e) {
            if (dumpFile.exists()) {
                dumpFile.delete();
            }
        }
        if(dumpFile.exists()){
            number=1;
        }
        resultJSON.addProperty("number",number);
        return resultJSON;
    }
}
