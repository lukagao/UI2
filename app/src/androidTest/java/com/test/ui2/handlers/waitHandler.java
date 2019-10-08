package com.test.ui2.handlers;

import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.SearchCondition;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObject2Condition;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.Until;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.test.ui2.handlers.CommandHandler;
import com.test.ui2.utils.AndroidCommand;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class waitHandler extends CommandHandler {
    public waitHandler(UiDevice device){super(device);}

    @Override
    public JsonObject execute(AndroidCommand command, JsonObject resultJSON, HashMap<String, List<UiObject2>> objectMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, UiObjectNotFoundException, InstantiationException {
        number=0;
        objectID=null;
        String value=null;

        actionJson = command.getActionParam();
        JsonObject conparamJson=actionJson.get("param").getAsJsonObject();
        JsonElement valueEle=conparamJson.get("value");
        if(valueEle==null){
            value=conparamJson.get("param").getAsJsonObject().get("value").toString().replace("\"", "").trim();
        }else{
            value=valueEle.toString().replace("\"","").trim();
        }
        String method=actionJson.get("method").toString().replace("\"","").trim();
        long timeout=(long)(Integer.parseInt(actionJson.get("timeout").toString().replace("\"","").trim())*1000);
        if(command.isDevice()) {
            System.out.println("UiDevice findObject路径");
            String attr=conparamJson.get("method").toString().replace("\"","").trim();
            selector=getBySelector(conparamJson);
            if(method.equals("findObject")){
                UiObject2 newObject=mDevice.wait((SearchCondition<UiObject2>) Until.class.getMethod(method,BySelector.class).invoke(new Until(),selector),timeout);
                if(newObject!=null)
                {
                    number=1;
                    objectList = new ArrayList<>();
                    objectList.add(0,newObject);
                    objectID = attr+value+String.valueOf(System.currentTimeMillis());
                    System.out.println("元素ID: "+objectID);
                    objectMap.put(objectID,objectList);
                }
                resultJSON.addProperty("number",number);
                resultJSON.addProperty("id",objectID);
            }else if(method.equals("findObjects")){
                objectList=mDevice.wait((SearchCondition<List<UiObject2>>) Until.class.getMethod(method,BySelector.class).invoke(new Until(),selector),timeout);
                number=objectList.size();
                objectID = attr+value+String.valueOf(System.currentTimeMillis());
                System.out.println("元素ID: "+objectID);
                objectMap.put(objectID,objectList);
                resultJSON.addProperty("number",number);
                resultJSON.addProperty("id",objectID);
            }else{
                returnValue=String.valueOf(mDevice.wait((SearchCondition<Boolean>) Until.class.getMethod(method,BySelector.class).invoke(new Until(),selector),timeout));
                resultJSON.addProperty("value",returnValue);
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


            int flag=Integer.parseInt(actionJson.get("flag").toString().replace("\"","").trim());
            switch(flag){
                case 0://means no param
                    //wait method no this param
                    break;
                case 1://means bool param
                    returnValue=String.valueOf(mObject.wait((UiObject2Condition<Boolean>) Until.class.getMethod(method,Boolean.class).invoke(new Until(),new Boolean(value)),timeout));
                    resultJSON.addProperty("value",returnValue);

                    break;
                case 2://means string param
                    returnValue=String.valueOf(mObject.wait((UiObject2Condition<Boolean>) Until.class.getMethod(method,String.class).invoke(new Until(),value),timeout));
                    resultJSON.addProperty("value",returnValue);
                    break;
                case 3://means BySelector param
                    String attr=conparamJson.get("method").toString().replace("\"","").trim();
                    selector=getBySelector(conparamJson);
                    if(method.equals("findObject")){
                        UiObject2 newObject=mObject.wait((SearchCondition<UiObject2>) Until.class.getMethod(method,BySelector.class).invoke(new Until(),selector),timeout);
                        if(newObject!=null)
                        {
                            number=1;
                            objectList = new ArrayList<>();
                            objectList.add(0,newObject);
                            objectID = attr+value+String.valueOf(System.currentTimeMillis());
                            System.out.println("元素ID: "+objectID);
                            objectMap.put(objectID,objectList);
                        }
                        resultJSON.addProperty("number",number);
                        resultJSON.addProperty("id",objectID);
                    }else if(method.equals("findObjects")){
                        objectList=mObject.wait((SearchCondition<List<UiObject2>>) Until.class.getMethod(method,BySelector.class).invoke(new Until(),selector),timeout);
                        number=objectList.size();
                        objectID = attr+value+String.valueOf(System.currentTimeMillis());
                        System.out.println("元素ID: "+objectID);
                        objectMap.put(objectID,objectList);
                        resultJSON.addProperty("number",number);
                        resultJSON.addProperty("id",objectID);
                    }else{
                        returnValue=String.valueOf(mObject.wait((SearchCondition<Boolean>) Until.class.getMethod(method,BySelector.class).invoke(new Until(),selector),timeout));
                        resultJSON.addProperty("value",returnValue);
                    }
                    break;


            }




        }

        return resultJSON;
    }
}
