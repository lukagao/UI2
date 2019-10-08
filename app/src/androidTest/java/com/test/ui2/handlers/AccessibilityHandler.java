package com.test.ui2.handlers;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.view.accessibility.AccessibilityNodeInfo;

import com.google.gson.JsonObject;
import com.test.ui2.utils.AndroidCommand;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

public class AccessibilityHandler{

    public JsonObject execute(AndroidCommand command, JsonObject resultJSON, HashMap<String, List<UiObject2>> objectMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, UiObjectNotFoundException, InstantiationException, IOException {
        AccessibilityNodeInfo accessibilityNodeInfo = null;

        return null;
    }
}
