package com.test.ui2.handlers;

import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;

import com.google.gson.JsonObject;
import com.test.ui2.utils.AndroidCommand;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

public class equalsHandler extends CommandHandler {
    public equalsHandler(UiDevice device){super(device);}

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
        objectID=actionJson.get("id").toString().replace("\"","").trim();
        index=Integer.parseInt(actionJson.get("index").toString().replace("\"","").trim());
        list=objectMap.get(objectID);
        if(list.size()==0){
            throw new UiObjectNotFoundException("UiObject not found");
        }
        UiObject2 newObject= list.get(index);

        returnValue=String.valueOf(mObject.equals(newObject));

        resultJSON.addProperty("value",returnValue);
        return resultJSON;
    }
}
