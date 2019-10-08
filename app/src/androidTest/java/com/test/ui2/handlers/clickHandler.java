package com.test.ui2.handlers;

import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiObject2;
import com.google.gson.JsonObject;
import com.test.ui2.utils.AndroidCommand;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

public class clickHandler extends CommandHandler{
    public clickHandler(UiDevice device){
        super(device);
    }
    @Override
    public JsonObject execute(AndroidCommand command, JsonObject resultJSON, HashMap<String, List<UiObject2>> objectMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, UiObjectNotFoundException, InstantiationException {

        actionType=command.getActionType();
        if(command.isDevice()) {
            System.out.println("UiDevice click路径");
            actionJson = command.getActionParam();
            int x=Integer.parseInt(actionJson.get("x").toString().replace("\"", ""));
            int y=Integer.parseInt(actionJson.get("y").toString().replace("\"", ""));
            returnValue=String.valueOf(mDevice.click(x,y));


        }else if(command.isUiObject())
        {
            System.out.println("UiObject click路径");
            objectID=command.getObjectID();
            Integer index =command.getObjectIndex();
            List<UiObject2> list=objectMap.get(objectID);
            if(list.size()==0){
                throw new UiObjectNotFoundException("UiObject not found");
            }
            UiObject2 mObject= list.get(index);

            actionJson = command.getActionParam();
            long duration= (long) Float.parseFloat(actionJson.get("duration").toString().replace("\"", ""));
            if(duration>0){
                mObject.click(duration);
            }else{
                mObject.click();
            }

        }
        resultJSON.addProperty("value",returnValue);
        return resultJSON;

    }
}
