package com.test.ui2.utils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.List;



public class AndroidCommand {
    public JsonObject param =null;
    private String objectType = null;
    private String actionType = null;
    private String objectID = null;
    private int objectIndex = -1;
    private JsonObject objectParam = null;
    private JsonObject actionParam = null;
    public AndroidCommand(String param){
            this.param = new JsonParser().parse(param).getAsJsonObject();
    }


    public String toString(){
        return param.toString();
    }

    public boolean isDevice(){
        if(getObjectType().equals("uidevice")){
            return true;
        }
        return false;
    }

    public boolean isUiObject(){
        if(getObjectType().equals("uiobject")){
            return true;
        }
        return false;
    }

    //获取动作的参数
    public JsonObject getActionParam(){
        if(actionParam!=null){
            return actionParam;
        }else{
            actionParam = param.get(getActionType()).getAsJsonObject();
            return actionParam;
        }
    }
    //获取操作对象的类型
    public String getObjectType(){
        if(objectType != null){
            return objectType;
        }else{
            objectType =  param.get("objecttype").toString().replace("\"","").trim();
            return objectType;
        }

    }
    //获取动作的类型
    public String getActionType(){
        if(actionType!=null){
            return actionType;
        }else{
            actionType = param.get("actiontype").toString().replace("\"","").trim();
            return actionType;
        }
    }
    //获取uiobject2的id
    public String getObjectID(){
        if(objectID != null){
            return objectID;
        }else{
            //JsonObject objectParam = getObjectParam();
            objectID = getObjectParam().get("id").toString().replace("\"", "");
            return objectID;
        }
    }
    //获取uiobject2的目录
    public int getObjectIndex(){
        if(objectIndex!=-1){
            return objectIndex;
        }else{
            //JsonObject objectParam = getObjectParam();
            objectIndex = Integer.parseInt(getObjectParam().get("index").toString().replace("\"", ""));
            return objectIndex;
        }
    }
    //获取uiobject2的参数:包含目录 id
    public JsonObject getObjectParam(){
        if(objectParam!=null){
            return objectParam;
        }
        else{
            objectParam = param.get("uiobject").getAsJsonObject();
            return objectParam;
        }
    }

    public String getSelectorMethod(){
        JsonElement method = getActionParam().get("method");
        if(method!=null){
            return method.toString().replace("\"", "").trim();
        }else{
            return null;
        }
    }

    public String getSelectorParamValue(){
        JsonObject selParam = getActionParam().get("param").getAsJsonObject();
        if(selParam!=null){
            return selParam.get("value").toString().replace("\"", "").trim();
        }else{
            return null;
        }
    }

}
