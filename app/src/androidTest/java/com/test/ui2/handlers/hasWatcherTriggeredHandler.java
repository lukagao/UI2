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

public class hasWatcherTriggeredHandler extends CommandHandler {
    public hasWatcherTriggeredHandler(UiDevice device){super(device);}

    @Override
    public JsonObject execute(AndroidCommand command, JsonObject resultJSON, HashMap<String, List<UiObject2>> objectMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, UiObjectNotFoundException, InstantiationException {

        actionJson = command.getActionParam();
        String watcherName=actionJson.get("watcherName").toString().replace("\"","").trim();
        returnValue=String.valueOf(mDevice.hasWatcherTriggered(watcherName));
        resultJSON.addProperty("value",returnValue);
        return resultJSON;
    }
}
