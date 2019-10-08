package com.test.ui2.handlers;

import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiObject2;
import com.google.gson.JsonObject;
import com.test.ui2.utils.AndroidCommand;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class findObjectHandler extends CommandHandler {
    public findObjectHandler(UiDevice device){
        super(device);
    }
    @Override
    public JsonObject execute(AndroidCommand command, JsonObject resultJSON, HashMap<String, List<UiObject2>> objectMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, UiObjectNotFoundException, InstantiationException {
        number=0;
        objectID=null;
        actionJson = command.getActionParam();
        if(command.isDevice()) {
            System.out.println("UiDevice findObject路径");
            selector = getBySelector(actionJson);
            UiObject2 mObject = mDevice.findObject(selector);
            if(mObject!=null)
            {
                number=1;
                objectList = new ArrayList<>();
                objectList.add(0,mObject);
                objectID = command.getSelectorMethod()+command.getSelectorParamValue()+String.valueOf(System.currentTimeMillis());
                objectMap.put(objectID,objectList);
            }

        }else if(command.isUiObject())
        {
            System.out.println("UiObject findObject路径");
            objectID=command.getObjectID();
            Integer index =command.getObjectIndex();
            List<UiObject2> list=objectMap.get(objectID);
            if(list.size()==0){
                throw new UiObjectNotFoundException("UiObject not found");
            }
            UiObject2 mObject= list.get(index);
            selector = getBySelector(actionJson);
            UiObject2 newObject = mObject.findObject(selector);
            if(newObject!=null)
            {
                number=1;
                objectList = new ArrayList<>();
                objectList.add(0,newObject);
                objectID = command.getSelectorMethod()+command.getSelectorParamValue()+String.valueOf(System.currentTimeMillis());
                System.out.println("元素ID: "+objectID);
                objectMap.put(objectID,objectList);
            }
        }
        //Uiautomator2.0 找不到元素不抛出异常，直接返回null
        resultJSON.addProperty("number",number);
        resultJSON.addProperty("id",objectID);
        return resultJSON;
    }
}
