package com.test.ui2.handlers;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiObject2;

import com.google.gson.JsonObject;
import com.test.ui2.core.ActionExecutor;
import com.test.ui2.utils.AndroidCommand;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.support.test.uiautomator.UiWatcher;
import android.support.test.uiautomator.Until;

public class registerWatcherHandler extends CommandHandler {
    private String attr=null;
    private String value=null;
    private String watcherActionType=null;
    private String watcherName=null;
    private JsonObject watcherActionJson=null;
    public registerWatcherHandler(UiDevice device){
        super(device);
    }
    @Override
    public JsonObject execute(final AndroidCommand command, JsonObject resultJSON, HashMap<String, List<UiObject2>> objectMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, UiObjectNotFoundException, InstantiationException {
        System.out.println("Watcher default: "+command.toString());
        actionJson = command.getActionParam();
        JsonObject selectorJson=actionJson.get("selector").getAsJsonObject();
        watcherName=actionJson.get("name").toString().replace("\"","").trim();
        watcherActionType=actionJson.get("actiontype").toString().replace("\"","").trim();
        watcherActionJson=actionJson.get("param").getAsJsonObject();
        attr = selectorJson.get("method").toString().replace("\"", "").trim();
        if(!attr.equals("soulDismiss")){
            value = selectorJson.get("param").getAsJsonObject().get("value").toString().replace("\"", "").trim();
            selector = getBySelector(selectorJson);
        }
        mDevice.registerWatcher(watcherName, new UiWatcher() {
            @Override
            public boolean checkForCondition() {
                try{
                    SoulDismiss();
                    UiObject2 mObject= mDevice.findObject(selector);
                    if(mObject!=null){
                        objectList = new ArrayList<>();
                        objectList.add(0,mObject);
                        objectID = attr+value+String.valueOf(System.currentTimeMillis());
                        System.out.println("元素ID: "+objectID);
                        ActionExecutor executor=new ActionExecutor();
                        executor.objectMap.put(objectID,objectList);
                        JsonObject mObjectJson=new JsonObject();
                        mObjectJson.addProperty("id",objectID);
                        mObjectJson.addProperty("index",0);
                        AndroidCommand  cmd = new AndroidCommand(command.param.toString());
                        cmd.param.addProperty("objecttype","uiobject");
                        cmd.param.add("uiobject",mObjectJson);
                        cmd.param.addProperty("actiontype",watcherActionType);
                        cmd.param.add(watcherActionType,watcherActionJson);
                        System.out.println("执行参数："+cmd.toString());
                        executor.ExecuteCase(cmd);
                        return true;
                    }else{
                        return false;
                    }

                }catch(Exception e){

                    e.printStackTrace();
                }
                return false;
            }
        });

        return resultJSON;
    }

    public boolean SoulDismiss(){
        UiObject2 mObject =null;
        //屏蔽捏脸弹框
        try{
            mObject=mDevice.findObject(By.res("cn.soulapp.android:id/tv_go_try"));
            if(mObject!=null){
                mDevice.findObject(By.res("cn.soulapp.android:id/tv_cancel")).click();
            }
        }catch (Exception e){

        }
        //屏蔽通讯录弹框
        try{
            mObject=mDevice.findObject(By.text("要屏蔽通讯录吗？"));
            if(mObject!=null){
                mDevice.findObject(By.res("cn.soulapp.android:id/btn_sure")).click();
                mDevice.wait(Until.findObject(By.res("com.android.packageinstaller:id/permission_deny_button")),10).click();
            }
        }catch (Exception e){

        }
        //屏蔽新版本
        try{
            mObject=mDevice.findObject(By.text("发现新版本"));
            if(mObject!=null) {
                mDevice.findObject(By.text("忽略")).click();
            }
        }catch (Exception e){

        }

        //屏蔽夜间模式和玩soul的正确姿势
        try{
            mObject=mDevice.findObject(By.res("cn.soulapp.android:id/ivWanSoul"));
            if(mObject!=null) {
                mDevice.findObject(By.res("cn.soulapp.android:id/ivClose")).click();
            }
        }catch (Exception e){

        }

        return true;
    }
}
