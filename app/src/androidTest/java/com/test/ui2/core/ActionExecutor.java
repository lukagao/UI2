package com.test.ui2.core;
import android.icu.text.SimpleDateFormat;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.StaleObjectException;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiObject2;

import com.test.ui2.handlers.AppAction;
import com.test.ui2.handlers.CommandHandler;
import com.test.ui2.handlers.clickAndWaitHandler;
import com.test.ui2.handlers.clickHandler;
import com.test.ui2.handlers.dragHandler;
import com.test.ui2.handlers.dumpWindowHierarchyHandler;
import com.test.ui2.handlers.equalsHandler;
import com.test.ui2.handlers.findObjectHandler;
import com.test.ui2.handlers.findObjectsHandler;
import com.test.ui2.handlers.flingHandler;
import com.test.ui2.handlers.getChildrenHandler;
import com.test.ui2.handlers.getParentHandler;
import com.test.ui2.handlers.hasObjectHandler;
import com.test.ui2.handlers.hasWatcherTriggeredHandler;
import com.test.ui2.handlers.memFillHandler;
import com.test.ui2.handlers.pinchCloseHandler;
import com.test.ui2.handlers.pinchOpenHandler;
import com.test.ui2.handlers.pressKeyCodeHandler;
import com.test.ui2.handlers.registerWatcherHandler;
import com.test.ui2.handlers.removeWatcherHandler;
import com.test.ui2.handlers.scrollHandler;
import com.test.ui2.handlers.setCompressedLayoutHeirarchyHandler;
import com.test.ui2.handlers.setGestureMarginHandler;
import com.test.ui2.handlers.setGestureMarginsHandler;
import com.test.ui2.handlers.setTextHandler;
import com.test.ui2.handlers.swipeHandler;
import com.test.ui2.handlers.takeScreenshotHandler;
import com.test.ui2.handlers.waitForIdleHandler;
import com.test.ui2.handlers.waitForWindowUpdateHandler;
import com.test.ui2.handlers.waitHandler;
import com.test.ui2.utils.AndroidCommand;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.io.File;


public class ActionExecutor {
    private UiDevice mDevice;
    private JsonObject paramJson=null;
    private JsonObject resultJSON=null;
    private JsonObject objectJson=null;
    private JsonObject actionJson=null;
    private String objectType=null;
    private String actionType=null;
    private String attr=null;
    private String value=null;
    private BySelector selector;
    private String result=null;
    private String reason=null;
    private String returnValue=null;
    private JsonElement uiobjectElem=null;
    private String time=null;
    private String time1=null;
    private Integer number=0;
    public HashMap<String,List<UiObject2>> objectMap=null;
    public HashMap<String,CommandHandler> handlerMap=null;
    private List<UiObject2> objectList=null;
    private Date date=null;
    private SimpleDateFormat sdf=null;
    private File dumpFolder =null;
    private String dumpFileName =null;
    private File dumpFile=null;
    private CommandHandler handler;
    public ActionExecutor(){

        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        //MutilActionHandler mhandler=new MutilActionHandler(mDevice);
        objectMap=new HashMap<>();
        initHandler();



    }
    public String ExecuteCase(AndroidCommand command) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        resultJSON=new JsonObject();
        result="Success";
        reason=null;
        try {
            //objectType=paramJson.get("objecttype").toString().replace("\"","").trim();
            actionType= command.getActionType();
            handler=handlerMap.get(actionType);
            if(handler!=null){
                handler.execute(command,resultJSON,objectMap);
            }else{
                handler=new CommandHandler(mDevice);
                handler.execute(command,resultJSON,objectMap);
            }
        } catch (UiObjectNotFoundException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw, true);
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
            System.out.println("异常原因: "+sw.toString());
            if(reason==null){
                result="Fail";
                reason="UiObjectNotFoundException";
            }

        }catch (StaleObjectException e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw, true);
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
            System.out.println("异常原因: "+sw.toString());
            if(reason==null){
                result="Fail";
                reason="StaleObjectException";
            }
        }catch (SecurityException e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw, true);
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
            System.out.println("异常原因: "+sw.toString());
            if(reason==null){
                result="Fail";
                reason="SecurityException";
            }
        }
        resultJSON.addProperty("result",result);
        resultJSON.addProperty("reason",reason);
        return resultJSON.toString();
    }

    private void initHandler(){
        handlerMap=new HashMap<>();
        handlerMap.put("findObject",new findObjectHandler(mDevice));
        handlerMap.put("findObjects",new findObjectsHandler(mDevice));
        handlerMap.put("dumpWindowHierarchy",new dumpWindowHierarchyHandler(mDevice));
        //handlerMap.put("clearLastTraversedText",new clearLastTraversedTextHandler(mDevice));
        handlerMap.put("click",new clickHandler(mDevice));
        handlerMap.put("drag",new dragHandler(mDevice));
        handlerMap.put("registerWatcher",new registerWatcherHandler(mDevice));
        handlerMap.put("removeWatcher",new removeWatcherHandler(mDevice));
        handlerMap.put("wait",new waitHandler(mDevice));
        handlerMap.put("takeScreenshot",new takeScreenshotHandler(mDevice));
        handlerMap.put("hasWatcherTriggered",new hasWatcherTriggeredHandler(mDevice));
        handlerMap.put("pressKeyCode",new pressKeyCodeHandler(mDevice));
        handlerMap.put("setCompressedLayoutHeirarchy",new setCompressedLayoutHeirarchyHandler(mDevice));
        handlerMap.put("swipe",new swipeHandler(mDevice));
        handlerMap.put("waitForIdle",new waitForIdleHandler(mDevice));
        handlerMap.put("waitForWindowUpdate",new waitForWindowUpdateHandler(mDevice));
        handlerMap.put("clickAndWait",new clickAndWaitHandler(mDevice));
        handlerMap.put("equals",new equalsHandler(mDevice));
        handlerMap.put("fling",new flingHandler(mDevice));
        handlerMap.put("hasObject",new hasObjectHandler(mDevice));
        handlerMap.put("pinchClose",new pinchCloseHandler(mDevice));
        handlerMap.put("pinchOpen",new pinchOpenHandler(mDevice));
        handlerMap.put("scroll",new scrollHandler(mDevice));
        handlerMap.put("setGestureMargin",new setGestureMarginHandler(mDevice));
        handlerMap.put("setGestureMargins",new setGestureMarginsHandler(mDevice));
        handlerMap.put("setText",new setTextHandler(mDevice));
        handlerMap.put("getParent",new getParentHandler(mDevice));
        handlerMap.put("getChildren",new getChildrenHandler(mDevice));
        handlerMap.put("memFill",new memFillHandler(mDevice));
        handlerMap.put("AppAction", new AppAction(mDevice));
        //int num=new memFillHandler(mDevice).fillMem(2000);
        //System.out.println("申请内存成功！申请了"+num+"MB");
    }


}
