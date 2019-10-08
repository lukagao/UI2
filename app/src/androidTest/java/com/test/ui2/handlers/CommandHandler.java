package com.test.ui2.handlers;
import android.graphics.Point;
import android.icu.text.SimpleDateFormat;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiObject2;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.io.File;
import java.util.regex.Pattern;
import com.test.ui2.utils.AndroidCommand;

import android.os.Environment;
public class CommandHandler {
    public UiDevice mDevice;
    public JsonObject resultJSON=null;
    public JsonObject objectJson=null;
    public JsonObject actionJson=null;
    public String objectType=null;
    public String actionType=null;
    public BySelector selector;
    public String result=null;
    public String reason=null;
    public String returnValue=null;
    public JsonElement uiobjectElem=null;
    public String objectID=null;
    public Integer number=0;
    public HashMap<String,List<UiObject2>> objectMap=null;
    public List<UiObject2> objectList=null;
    public File dumpFolder =null;
    public String dumpFileName =null;
    public File dumpFile=null;

    public CommandHandler(UiDevice device){
        mDevice=device;
    }

    //for  method which has none param and return value is void,int,string and point
    public JsonObject execute(AndroidCommand command,JsonObject resultJSON,HashMap<String,List<UiObject2>> objectMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, UiObjectNotFoundException, InstantiationException, IOException {
        actionType=command.getActionType();
        if(command.isDevice()) {
            System.out.println("UiDevice默认路径");
            returnValue=String.valueOf(mDevice.getClass().getMethod(actionType).invoke(mDevice));


        }else if(command.isUiObject())
        {
            objectID=command.getObjectID();
            Integer index =command.getObjectIndex();
            List<UiObject2> list=objectMap.get(objectID);
            if(list.size()==0){
                throw new UiObjectNotFoundException("UiObject not found");
            }
            UiObject2 mObject= list.get(index);
            System.out.println("UiObject默认路径");
            returnValue=String.valueOf(mObject.getClass().getMethod(actionType).invoke(mObject));
        }
        resultJSON.addProperty("value",returnValue);
        return resultJSON;
    }
//    public BySelector getBySelector(String attr, String value) throws InstantiationException,IllegalAccessException,InvocationTargetException,NoSuchMethodException{
//        BySelector by=null;
//        switch (attr) {
//            case "ddd":
//
//                break;
//
//            case "hhh":
//
//                break;
//            //for one para and type is string
//            default:
//                System.out.println("Selector默认路径");
//                Class selector = By.class;
//                Constructor con=selector.getDeclaredConstructor();
//                con.setAccessible(true);
//                by = (BySelector) selector.getMethod(attr, String.class).invoke(con.newInstance(), value);
//                break;
//        }
//        return by;
//    }

    public BySelector getBySelector(JsonObject actionJson) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        BySelector by=null;
        int flag=0;
        String method = actionJson.get("method").toString().replace("\"", "").trim();
        JsonObject byParamJson = actionJson.get("param").getAsJsonObject();
        flag=Integer.parseInt(byParamJson.get("flag").toString().replace("\"", "").trim());
        JsonElement nextJson = actionJson.get("next");
        switch (flag) {
            //for bool
            case 0:
                System.out.println("Selector bool路径");
                boolean boolvalue = new Boolean(byParamJson.get("value").toString().replace("\"", "").trim()) ;
                if(nextJson==null){
                    Class selector = By.class;
                    Constructor con=selector.getDeclaredConstructor();
                    con.setAccessible(true);
                    by = (BySelector) selector.getMethod(method, boolean.class).invoke(con.newInstance(), boolvalue);
                }else{
                    BySelector selector=getBySelector(nextJson.getAsJsonObject());
                    by=(BySelector)selector.getClass().getMethod(method,boolean.class).invoke(selector,boolvalue);
                }

                break;
            //for int
            case 1:
                System.out.println("Selector int路径");
                int intvalue = Integer.parseInt(byParamJson.get("value").toString().replace("\"", "").trim()) ;
                if(nextJson==null){
                    Class selector = By.class;
                    Constructor con=selector.getDeclaredConstructor();
                    con.setAccessible(true);
                    by = (BySelector) selector.getMethod(method, int.class).invoke(con.newInstance(), intvalue);
                }else{
                    BySelector selector=getBySelector(nextJson.getAsJsonObject());
                    by=(BySelector)selector.getClass().getMethod(method,int.class).invoke(selector,intvalue);
                }


                break;
            //for pattern
            case 2:
                System.out.println("Selector pattern路径");
                Pattern patternvalue = Pattern.compile(byParamJson.get("value").toString().replace("\"", "").trim());
                if(nextJson==null){
                    Class selector = By.class;
                    Constructor con=selector.getDeclaredConstructor();
                    con.setAccessible(true);
                    by = (BySelector) selector.getMethod(method, Pattern.class).invoke(con.newInstance(), patternvalue);
                }else{
                    BySelector selector=getBySelector(nextJson.getAsJsonObject());
                    by=(BySelector)selector.getClass().getMethod(method,Pattern.class).invoke(selector,patternvalue);
                }

                break;

            //for selector
            case 3:
                System.out.println("Selector selector路径");
                BySelector selectorvalue = getBySelector(byParamJson.get("value").getAsJsonObject());
                if(method.equals("hasDescendant")){
                    int sub = Integer.parseInt(byParamJson.get("sub").toString().replace("\"", "").trim());
                    if(nextJson==null){
                        if(sub==0){
                            by=By.hasDescendant(selectorvalue);
                        }else{
                            by=By.hasDescendant(selectorvalue,sub);
                        }
                    }else{
                        BySelector selector=getBySelector(nextJson.getAsJsonObject());
                        if(sub==0){
                            by=selector.hasDescendant(selectorvalue);
                        }else{
                            by=selector.hasDescendant(selectorvalue,sub);
                        }
                    }

                }else{
                    if(nextJson==null){
                        Class selector = By.class;
                        Constructor con=selector.getDeclaredConstructor();
                        con.setAccessible(true);
                        by = (BySelector) selector.getMethod(method, BySelector.class).invoke(con.newInstance(), selectorvalue);
                    }else{
                        BySelector selector=getBySelector(nextJson.getAsJsonObject());
                        by=(BySelector)selector.getClass().getMethod(method,BySelector.class).invoke(selector,selectorvalue);
                    }
                }

                break;
            //4 for one para and type is string
            default:
                System.out.println("Selector默认路径");
                String value = byParamJson.get("value").toString().replace("\"", "").trim();
                JsonElement subElem=byParamJson.get("sub");
                if((method.equals("res") && subElem !=null) || method.equals("clazz")){
                    String sub = subElem.toString().replace("\"", "").trim();
                    if(nextJson==null){
                        Class selector = By.class;
                        Constructor con=selector.getDeclaredConstructor();
                        con.setAccessible(true);
                        //System.out.println("Selector默认路径"+method+value+sub);
                        by = (BySelector)selector.getMethod(method, String.class,String.class).invoke(con.newInstance(), value,sub);
                    }else{
                        BySelector selector=getBySelector(nextJson.getAsJsonObject());
                        by = (BySelector)selector.getClass().getMethod(method,String.class,String.class).invoke(selector,value,sub);
                    }

                }else{
                    if(nextJson==null){
                        Class selector = By.class;
                        Constructor con=selector.getDeclaredConstructor();
                        con.setAccessible(true);
                        by = (BySelector) selector.getMethod(method, String.class).invoke(con.newInstance(), value);
                    }else{
                        BySelector selector=getBySelector(nextJson.getAsJsonObject());
                        by=(BySelector)selector.getClass().getMethod(method,String.class).invoke(selector,value);
                    }
                }
                break;
        }
        return by;
    }
}
