package com.test.ui2.handlers;

import android.support.test.uiautomator.Direction;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.Until;

import com.google.gson.JsonObject;
import com.test.ui2.utils.AndroidCommand;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

public class clickAndWaitHandler extends CommandHandler {
    public clickAndWaitHandler(UiDevice device){super(device);}

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
        JsonObject conparamJson=actionJson.get("param").getAsJsonObject();
        //String value=conparamJson.get("value").toString().replace("\"","").trim();
        String method=actionJson.get("method").toString().replace("\"","").trim();
        long timeout=Long.parseLong(actionJson.get("timeout").toString().replace("\"","").trim());
        if(method.equals("newWindow")){
            mObject.clickAndWait(Until.newWindow(),timeout);
        }else if(method.equals("scrollFinished")){
            int direction=Integer.parseInt(conparamJson.get("value").toString().replace("\"","").trim());
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
            mObject.clickAndWait(Until.scrollFinished(direct),timeout);
        }
        return resultJSON;
    }
}
