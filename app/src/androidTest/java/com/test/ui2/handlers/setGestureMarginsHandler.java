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

public class setGestureMarginsHandler extends CommandHandler {

    public setGestureMarginsHandler(UiDevice device){super(device);}

    @Override
    public JsonObject execute(AndroidCommand command, JsonObject resultJSON, HashMap<String, List<UiObject2>> objectMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, UiObjectNotFoundException, InstantiationException {
        objectID=command.getObjectID();
        Integer index =command.getObjectIndex();
        List<UiObject2> list=objectMap.get(objectID);
        if(list.size()==0){
            throw new UiObjectNotFoundException("UiObject not found");
        }
        UiObject2 mObject= list.get(index);
        actionJson = command.getActionParam();
        int left=Integer.parseInt(actionJson.get("left").toString().replace("\"", ""));
        int top=Integer.parseInt(actionJson.get("top").toString().replace("\"", ""));
        int right=Integer.parseInt(actionJson.get("right").toString().replace("\"", ""));
        int bottom=Integer.parseInt(actionJson.get("bottom").toString().replace("\"", ""));

        mObject.setGestureMargins(left,top,right,bottom);
        return resultJSON;
    }
}
