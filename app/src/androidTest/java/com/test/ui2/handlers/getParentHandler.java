package com.test.ui2.handlers;

import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;

import com.google.gson.JsonObject;
import com.test.ui2.handlers.CommandHandler;
import com.test.ui2.utils.AndroidCommand;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class getParentHandler extends CommandHandler {
    public getParentHandler(UiDevice device){super(device);}

    @Override
    public JsonObject execute(AndroidCommand command, JsonObject resultJSON, HashMap<String, List<UiObject2>> objectMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, UiObjectNotFoundException, InstantiationException {
        number=0;
        objectID=null;
        String parentID=null;
        objectID=command.getObjectID();
        Integer index =command.getObjectIndex();
        List<UiObject2> list=objectMap.get(objectID);
        if(list.size()==0){
            throw new UiObjectNotFoundException("UiObject not found");
        }
        UiObject2 mObject= list.get(index);

        UiObject2 parent=mObject.getParent();
        if(parent!=null){
            number=1;
            objectList = new ArrayList<>();
            objectList.add(0,parent);
            parentID=objectID+"parent";
            objectMap.put(parentID,objectList);
        }
        resultJSON.addProperty("number",number);
        resultJSON.addProperty("id",parentID);
        return resultJSON;
    }
}
