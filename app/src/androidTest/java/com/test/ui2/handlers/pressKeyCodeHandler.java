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

public class pressKeyCodeHandler extends CommandHandler {
    public pressKeyCodeHandler(UiDevice device){super(device);}

    @Override
    public JsonObject execute(AndroidCommand command, JsonObject resultJSON, HashMap<String, List<UiObject2>> objectMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, UiObjectNotFoundException, InstantiationException {

        actionJson = command.getActionParam();
        int keyCode=Integer.parseInt(actionJson.get("keyCode").toString().replace("\"","").trim());
        int metaState=Integer.parseInt(actionJson.get("metaState").toString().replace("\"","").trim());
        if(metaState==0){
            returnValue=String.valueOf(mDevice.pressKeyCode(keyCode));
        }else{
            returnValue=String.valueOf(mDevice.pressKeyCode(keyCode,metaState));
        }
        resultJSON.addProperty("value",returnValue);
        return resultJSON;
    }
}
