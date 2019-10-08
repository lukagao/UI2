package com.test.ui2.handlers;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;

import com.google.gson.JsonObject;
import com.test.ui2.handlers.CommandHandler;
import com.test.ui2.utils.AndroidCommand;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

public class AppAction extends CommandHandler{
    private String packageName = null;
    private String activity = null;
    public AppAction(UiDevice device){
        super(device);
    }

    @Override
    public JsonObject execute(AndroidCommand command, JsonObject resultJSON, HashMap<String, List<UiObject2>> objectMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, UiObjectNotFoundException, InstantiationException, IOException {
        returnValue = String.valueOf(false);
        actionJson = command.getActionParam();
        String action = actionJson.get("action").getAsString().trim();
        packageName = actionJson.get("packageName").getAsString().trim();
        if(action.equals("launch")){
            returnValue = String.valueOf(launchApp());
        }else if(action.equals("close")){
            returnValue = String.valueOf(closeApp());
        }
        resultJSON.addProperty("value",returnValue);
        return resultJSON;
    }
    public boolean launchApp() throws IOException{
        activity = actionJson.get("activity").getAsString().trim();
        mDevice.executeShellCommand("am start "+packageName+"/"+activity);
        return true;
    }

    public boolean closeApp() {
        if(packageName!=null){
            try {
                mDevice.executeShellCommand("am force-stop "+packageName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
