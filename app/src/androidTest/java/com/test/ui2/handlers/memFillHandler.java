package com.test.ui2.handlers;

import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;

import com.google.gson.JsonObject;
import com.test.ui2.handlers.CommandHandler;
import com.test.ui2.utils.AndroidCommand;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

public class memFillHandler extends CommandHandler {
    public int alloc=0;
    public memFillHandler(UiDevice device){
        super(device);
        System.loadLibrary("mem_fill");

    }

    @Override
    public JsonObject execute(AndroidCommand command, JsonObject resultJSON, HashMap<String, List<UiObject2>> objectMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, UiObjectNotFoundException, InstantiationException {

        actionJson = command.getActionParam();
        int size = actionJson.get("size").getAsInt();
        if(size==-1){
            if(alloc>0){
                returnValue=String.valueOf(freeMem());
            }else{
                returnValue="-1";
            }
        }else{
            alloc = fillMem(size);
            returnValue=String.valueOf(alloc);
        }
        resultJSON.addProperty("value",returnValue);
        return resultJSON;
    }

    // 填充xxxMB内存
    public native int fillMem(int blockNum);

    // 释放刚才填充的内存
    public native int freeMem();
}
