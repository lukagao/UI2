package com.test.ui2.handlers;

import android.app.UiAutomation;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
//import android.support.test.uiautomator.InteractionController;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.test.ui2.handlers.CommandHandler;
import com.test.ui2.utils.AndroidCommand;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.view.MotionEvent;
import android.os.SystemClock;
import android.view.InputDevice;
import android.view.MotionEvent.PointerCoords;
import android.view.MotionEvent.PointerProperties;
import android.support.test.InstrumentationRegistry;
public class MutilActionHandler extends CommandHandler {
    private UiAutomation uiautomator=null;
    public MutilActionHandler(UiDevice device){
        super(device);
        uiautomator=InstrumentationRegistry.getInstrumentation().getUiAutomation();
        int steps=0;
        int cSize=0;
        int aSize=0;
        int upNum=1;
        long downTime=0;
        ArrayList<PointerProperties> properties = new ArrayList();
        ArrayList<PointerCoords> coords = new ArrayList();
        ArrayList<PointerProperties> pUP = new ArrayList();
        ArrayList<PointerCoords> cUP = new ArrayList();
        JsonObject action =  getAction();
        aSize=action.size();
        downTime=SystemClock.uptimeMillis();
        for(Integer i=0;i<aSize;i++){
            cSize=action.get(i.toString()).getAsJsonObject().size();
            if(steps<cSize)
                steps=cSize;
        }
        for(Integer step=0;step<steps;step++){

            for(Integer i=0;i<aSize;i++){
                System.out.println("循环step："+step+"次"+" i："+i+"次");
                JsonObject coordJson = action.get(i.toString()).getAsJsonObject();
                JsonElement opJson=coordJson.get(step.toString());
                if(opJson!=null){
                    properties.add(i,getPointerProperties(i));
                    coords.add(i,getPointerCoords(Integer.parseInt(opJson.getAsJsonObject().get("x").toString()),
                            Integer.parseInt(opJson.getAsJsonObject().get("y").toString())));
                }else{
                    System.out.println("操作数据空");
                }

                if(step==0){
                    if(i==0){
                        System.out.println("点击ACTION_DOWN");
                        inJectEvent(downTime,MotionEvent.ACTION_DOWN,properties,coords);
                        properties.clear();
                        coords.clear();
                    }else if(i<aSize){
                        System.out.println("点击ACTION_POINTER_DOWN");
                        inJectEvent(downTime,MotionEvent.ACTION_POINTER_DOWN,properties,coords);
                        properties.clear();
                        coords.clear();
                    }


                }
                if(step>0 && step<coordJson.size()-1){
                    if(i<aSize){
                        System.out.println("点击ACTION_MOVE");
                        inJectEvent(downTime,MotionEvent.ACTION_MOVE,properties,coords);
                        properties.clear();
                        coords.clear();
                    }
                }
                if(step==coordJson.size()-1){
                    pUP.add(properties.get(i));
                    cUP.add(coords.get(i));
                    if(upNum<aSize){
                        System.out.println("点击ACTION_POINTER_UP");
                        inJectEvent(downTime,MotionEvent.ACTION_POINTER_UP,pUP,cUP);
                    }else{
                        System.out.println("点击ACTION_UP");
                        inJectEvent(downTime,MotionEvent.ACTION_UP,pUP,cUP);
                    }
                    upNum++;
                    properties.remove(i);
                    coords.remove(i);

                }
            }
            SystemClock.sleep(5);

        }



    }
    private JsonObject getAction(){
        JsonObject action=new JsonObject();
        JsonObject coords=new JsonObject();
        JsonObject operation=new JsonObject();
        JsonObject operation1=new JsonObject();
        JsonObject operation2=new JsonObject();
        operation.addProperty("x",200);
        operation.addProperty("y",600);
        coords.add("0",operation);
        operation1.addProperty("x",800);
        operation1.addProperty("y",600);
        coords.add("1",operation1);
        operation2.addProperty("x",800);
        operation2.addProperty("y",600);
        coords.add("2",operation2);
        action.add("0",coords);
        System.out.println("ActionString："+action.toString());
        return action;
    }

    private PointerProperties getPointerProperties(int id){
        PointerProperties prop = new PointerProperties();
        prop.id = id;
        prop.toolType = MotionEvent.TOOL_TYPE_FINGER;
        return prop;
    }

    private PointerCoords getPointerCoords(int x,int y){
        PointerCoords p = new PointerCoords();
        p.size = 1;
        p.pressure = 1;
        p.x = x;
        p.y = y;
        return p;
    }

    private boolean inJectEvent(long downTime,int action, List<PointerProperties> properties, List<PointerCoords> coords)
    {
        MotionEvent event=MotionEvent.obtain(downTime,SystemClock.uptimeMillis(),action,coords.size(),
                properties.toArray(new PointerProperties[0]),coords.toArray(new PointerCoords[0]),
                0,0,1,1,0,0,
                InputDevice.SOURCE_TOUCHSCREEN,0);
        uiautomator.injectInputEvent(event,true);
        return true;
    }

    @Override
    public JsonObject execute(AndroidCommand command, JsonObject resultJSON, HashMap<String, List<UiObject2>> objectMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, UiObjectNotFoundException, InstantiationException {
        UiAutomation uiautomator=InstrumentationRegistry.getInstrumentation().getUiAutomation();
        ArrayList<PointerProperties> properties = new ArrayList();
        ArrayList<PointerCoords> coords = new ArrayList();
        long downTime=0;
        PointerProperties prop1 = new PointerProperties();
        prop1.id = 0;
        prop1.toolType = MotionEvent.TOOL_TYPE_FINGER;
        PointerProperties prop2 = new PointerProperties();
        prop2.id = 1;
        prop2.toolType = MotionEvent.TOOL_TYPE_FINGER;
        properties.add(prop1);
        //properties.add(prop2);
        PointerCoords p1 = new PointerCoords();
        p1.size = 1;
        p1.pressure = 1;
        p1.x = 200;
        p1.y =300;
        PointerCoords p2 = new PointerCoords();
        p2.size = 1;
        p2.pressure = 1;
        p2.x = 200;
        p2.y =500;
        coords.add(p1);
        //coords.add(p2);
        downTime=SystemClock.uptimeMillis();
        MotionEvent eventDown=MotionEvent.obtain(downTime,SystemClock.uptimeMillis(),MotionEvent.ACTION_DOWN,coords.size(),
                properties.toArray(new PointerProperties[0]),coords.toArray(new PointerCoords[0]),
                0,0,1,1,0,0,
                InputDevice.SOURCE_TOUCHSCREEN,0);
        coords.clear();
        coords.add(p2);
        MotionEvent eventUp=MotionEvent.obtain(downTime,SystemClock.uptimeMillis(),MotionEvent.ACTION_UP,coords.size(),
                properties.toArray(new PointerProperties[0]),coords.toArray(new PointerCoords[0]),
                0,0,1,1,0,0,
                InputDevice.SOURCE_TOUCHSCREEN,0);

        uiautomator.injectInputEvent(eventDown,true);
        SystemClock.sleep(5);
        uiautomator.injectInputEvent(eventUp,true);

        return resultJSON;
    }

    public static Object getField(final Class clazz, final String fieldName,
                                  final Object object) {
        try {
            final Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);

            return field.get(object);
        } catch (final Exception e) {
            final String msg = String.format(
                    "error while getting field %s from object %s", fieldName, object);
            throw new RuntimeException(msg, e);
        }
    }
}
