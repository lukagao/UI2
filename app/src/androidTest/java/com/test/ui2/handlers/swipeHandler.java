package com.test.ui2.handlers;

import android.support.test.uiautomator.Direction;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiObject2;
import com.google.gson.JsonObject;
import com.test.ui2.handlers.CommandHandler;
import com.test.ui2.utils.AndroidCommand;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

public class swipeHandler extends CommandHandler {
    public swipeHandler(UiDevice device){
        super(device);
    }
    @Override
    public JsonObject execute(AndroidCommand command, JsonObject resultJSON, HashMap<String, List<UiObject2>> objectMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, UiObjectNotFoundException, InstantiationException {

        if(command.isDevice()) {
            System.out.println("UiDevice drag默认路径");
            actionJson = command.getActionParam();
            int startX=Integer.parseInt(actionJson.get("startX").toString().replace("\"", ""));
            int startY=Integer.parseInt(actionJson.get("startY").toString().replace("\"", ""));
            int endX=Integer.parseInt(actionJson.get("endX").toString().replace("\"", ""));
            int endY=Integer.parseInt(actionJson.get("endY").toString().replace("\"", ""));
            int steps=Integer.parseInt(actionJson.get("steps").toString().replace("\"", ""));
            System.out.println("开始滑动");
            returnValue=String.valueOf(mDevice.swipe(startX,startY,endX,endY,steps));
            System.out.println("结束滑动");


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
            int direction=Integer.parseInt(actionJson.get("direction").toString().replace("\"", ""));
            float percent=Float.parseFloat(actionJson.get("percent").toString().replace("\"", ""));
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
            System.out.println("开始滑动");
            if(speed==0){
                mObject.swipe(direct,percent);
            }else{
                mObject.swipe(direct,percent,speed);
            }
            System.out.println("结束滑动");
        }

        resultJSON.addProperty("value",returnValue);
        return resultJSON;
    }
}
