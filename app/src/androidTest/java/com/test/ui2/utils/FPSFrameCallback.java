package com.test.ui2.utils;
import android.view.Choreographer;
public class FPSFrameCallback implements Choreographer.FrameCallback{
    private long lastFrameTime=0;
    private long internalTime;

    public FPSFrameCallback(){
        internalTime=(long)(1000000000/60.0);
    }
    @Override
     public void doFrame(long nowFrameTime){
        if(lastFrameTime==0)
            lastFrameTime=nowFrameTime;
        long duration = nowFrameTime-lastFrameTime;
        //if(duration>=internalTime){
            long skipped=duration/internalTime;
            System.out.println("跳过帧数："+skipped+"帧");
        //}
        lastFrameTime=nowFrameTime;
        Choreographer.getInstance().postFrameCallback(this);

     }
}
