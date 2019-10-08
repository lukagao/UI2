package com.test.ui2.handlers;

import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiObject2;
import com.google.gson.JsonObject;
import com.test.ui2.utils.AndroidCommand;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

public class clearLastTraversedTextHandler extends CommandHandler {
    public clearLastTraversedTextHandler(UiDevice device){
        super(device);
    }
    @Override
    public JsonObject execute(AndroidCommand command, JsonObject resultJSON, HashMap<String, List<UiObject2>> objectMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, UiObjectNotFoundException, InstantiationException {
        mDevice.clearLastTraversedText();
        return resultJSON;

    }
}
