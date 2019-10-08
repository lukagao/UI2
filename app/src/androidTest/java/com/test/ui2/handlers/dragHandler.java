package com.test.ui2.handlers;

import android.graphics.Point;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiObject2;
import com.google.gson.JsonObject;
import com.test.ui2.utils.AndroidCommand;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

public class dragHandler extends CommandHandler{
    public dragHandler(UiDevice device){
        super(device);
    }
    @Override
    public JsonObject execute(AndroidCommand command, JsonObject resultJSON, HashMap<String, List<UiObject2>> objectMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, UiObjectNotFoundException, InstantiationException {

        actionType=command.getActionType();
        if(command.isDevice()) {
            System.out.println("UiDevice drag默认路径");
            actionJson = command.getActionParam();
            int startX=Integer.parseInt(actionJson.get("startX").toString().replace("\"", ""));
            int startY=Integer.parseInt(actionJson.get("startY").toString().replace("\"", ""));
            int endX=Integer.parseInt(actionJson.get("endX").toString().replace("\"", ""));
            int endY=Integer.parseInt(actionJson.get("endY").toString().replace("\"", ""));
            int steps=Integer.parseInt(actionJson.get("steps").toString().replace("\"", ""));
            returnValue=String.valueOf(mDevice.drag(startX,startY,endX,endY,steps));


        }else if(command.isUiObject())
        {
            System.out.println("UiObject drag默认路径");
            objectID=command.getObjectID();
            Integer index =command.getObjectIndex();
            List<UiObject2> list=objectMap.get(objectID);
            if(list.size()==0){
                throw new UiObjectNotFoundException("UiObject not found");
            }
            UiObject2 mObject= list.get(index);
            actionJson = command.getActionParam();
            int x=Integer.parseInt(actionJson.get("x").toString().replace("\"", ""));
            int y=Integer.parseInt(actionJson.get("y").toString().replace("\"", ""));
            int speed = Integer.parseInt(actionJson.get("speed").toString().replace("\"", ""));
            Point dest = new Point(x,y);
            if(speed==0){
                mObject.drag(dest);
            }else{
                mObject.drag(dest,speed);
            }

        }

        resultJSON.addProperty("value",returnValue);
        return resultJSON;
    }
}
