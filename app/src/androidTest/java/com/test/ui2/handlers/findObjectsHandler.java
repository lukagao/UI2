package com.test.ui2.handlers;

import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiObject2;
import com.google.gson.JsonObject;
import com.test.ui2.utils.AndroidCommand;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import java.util.List;

public class findObjectsHandler extends CommandHandler {
    public findObjectsHandler(UiDevice device){
        super(device);
    }
    @Override
    public JsonObject execute(AndroidCommand command, JsonObject resultJSON, HashMap<String, List<UiObject2>> objectMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, UiObjectNotFoundException, InstantiationException {
        number=0;
        objectID=null;
        actionJson = command.getActionParam();
        if(command.isDevice()) {
            System.out.println("UiDevice findObjects路径");
            selector = getBySelector(actionJson);
            objectList = mDevice.findObjects(selector);
            number=objectList.size();
            objectID = command.getSelectorMethod()+command.getSelectorParamValue()+String.valueOf(System.currentTimeMillis());
            System.out.println("元素ID: "+objectID);
            objectMap.put(objectID,objectList);

        }else if(command.isUiObject())
        {
            System.out.println("UiObject findObjects路径");
            objectID=command.getObjectID();
            Integer index =command.getObjectIndex();
            List<UiObject2> list=objectMap.get(objectID);
            if(list.size()==0){
                throw new UiObjectNotFoundException("UiObject not found");
            }
            UiObject2 mObject= list.get(index);

            selector = getBySelector(actionJson);
            objectList = mObject.findObjects(selector);
            number=objectList.size();
            objectID = command.getSelectorMethod()+command.getSelectorParamValue()+String.valueOf(System.currentTimeMillis());
            System.out.println("元素ID: "+objectID);
            objectMap.put(objectID,objectList);

        }
        resultJSON.addProperty("number",number);
        resultJSON.addProperty("id",objectID);
        return resultJSON;
    }

}
