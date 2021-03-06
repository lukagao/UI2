package com.test.ui2.handlers;

import android.support.test.uiautomator.Direction;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;

import com.google.gson.JsonObject;
import com.test.ui2.utils.AndroidCommand;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

public class flingHandler extends CommandHandler {
    public flingHandler(UiDevice device){super(device);}

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
        int direction=Integer.parseInt(actionJson.get("direction").toString().replace("\"", ""));
        int speed = Integer.parseInt(actionJson.get("speed").toString().replace("\"", ""));
        Direction direct;
        if(direction==0){
            direct=Direction.UP;
        }else if(direction==1){
            direct=Direction.DOWN;
        }else if(direction==2){
            direct=Direction.LEFT;
        }else{
            direct=Direction.RIGHT;
        }
        if(speed==0){
            returnValue=String.valueOf(mObject.fling(direct));
        }else{
            returnValue=String.valueOf(mObject.fling(direct,speed));
        }

        resultJSON.addProperty("value",returnValue);
        return resultJSON;

    }
}
