package com.test.ui2;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObjectNotFoundException;

import java.io.IOException;

import com.test.ui2.core.MySocket;
import com.test.ui2.core.Server;

@RunWith(AndroidJUnit4.class)
public class UiTest {
    private UiDevice mDevice;
    private MySocket sock;
    private Server server;
    @Test
    public void run() throws UiObjectNotFoundException,IOException {

        //sock=new MySocket();
        //sock.start();
        //Choreographer.getInstance().postFrameCallback(new FPSFrameCallback());
        server=new Server();
        server.start();
    }
}
