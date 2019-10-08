package com.test.ui2.core;

import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiObjectNotFoundException;

import com.google.gson.JsonObject;
import com.test.ui2.utils.AndroidCommand;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

public class Server {

    private ServerSocketChannel server_sc=null;
    private SocketChannel client_sc=null;
    private Selector ssc_selector=null;
    private Selector csc_selector=null;
    private int reset=0;
    private JsonObject errorJson=null;
    SelectionKey key=null;
    Iterator<SelectionKey> keyIter=null;
    private int bufferSize=1024;
    private String param=null;
    private String msg;
    private Handler handler=null;
    private int ret=1;
    private int port=5173;
    private ByteBuffer heart = null;
    ActionExecutor executor=null;
    public Server(){
        executor=new ActionExecutor();
    }
    public  void start(){
        System.out.println("server_start");
        try {
            server_sc = ServerSocketChannel.open();
            server_sc.socket().setReuseAddress(true);
            server_sc.socket().bind(new InetSocketAddress(port));
            server_sc.configureBlocking(false);
            ssc_selector = Selector.open();
            csc_selector=Selector.open();
            server_sc.register(ssc_selector, SelectionKey.OP_ACCEPT);
            handler = new Handler();
            InstrumentationRegistry.getInstrumentation().sendStatus(port,null);
            while (true) {
                if (ssc_selector.select(2000) == 0) {
                    if (reset < 30) {
                        System.out.println("请求connect超时" + reset + "次");
                        reset++;
                        continue;
                    } else {
                        ret = -1;
                        reset=0;
                        break;
                    }
                } else {
                    reset = 0;
                    keyIter = ssc_selector.selectedKeys().iterator();
                    while (keyIter.hasNext()) {
                        System.out.println("存在accept连接");
                        key = keyIter.next();
                        keyIter.remove();
                        if (key.isAcceptable()) {
                            ret = handler.handlerAccept(key);
                            break;
                        }
                    }
                }
                break;
            }

            if(ret==-1) {//timeout or accpet error
                throw new Exception("Internal error");
            }

            while (true) {
                if (csc_selector.select(3000) == 0) {
                    reset++;
                    if(reset>=60){
                        //心跳包超时：断网或者死机
                        ret = -2;
                        break;
                    }
                    //发送心跳包
                    heart=ByteBuffer.wrap("heartbeat".getBytes("UTF-8"));
                    if(client_sc!=null) {
                        ret=handler.write(client_sc,heart);
                    }//end if(key
                    //发送完成

                } else {
                    reset = 0;
                    keyIter = csc_selector.selectedKeys().iterator();
                    while (keyIter.hasNext()) {
                        System.out.println("存在read连接");
                        key = keyIter.next();
                        keyIter.remove();
                        if (key.isReadable()) {
                            ret = handler.handlerRead(key);
                        }
                        if (ret < 1) {
                            break;
                        }
                    }
                }
                if(ret==0){//client send shutdown
                    System.out.println("Shutdown");
                    break;
                }else if(ret==-1) {//timeout or client cloesd
                    throw new Exception("Internal error");
                }else if(ret==-2){
                    System.out.println("心跳包超时");
                    break;
                }
            }

        }catch (Exception e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw, true);
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
            System.out.println("异常原因: " + sw.toString());
            if(key!=null) {
                if (key.isValid()) {
                    try {
                        errorJson = new JsonObject();
                        errorJson.addProperty("result", "Error");
                        errorJson.addProperty("reason", "Internal error");
                        ByteBuffer buffer = ByteBuffer.wrap((errorJson.toString()+'\n').getBytes("UTF-8"));
                        System.out.println("开始发送数据");
                        int times = 0;
                        while (buffer.hasRemaining()) {
                            if (client_sc.write(buffer) > 0) {
                                times = 0;
                                continue;
                            } else if (client_sc.write(buffer) == 0) {
                                if (times < 20) {
                                    times++;
                                    Thread.sleep(100);
                                    continue;
                                } else {//times>=20
                                    break;
                                }
                            }//end if(client_sc.write(buffer))
                        }//end while
                    } catch (Exception se) {

                    }
                }
            }
        }finally{
            try {
                if(server_sc!=null){
                    server_sc.close();
                    server_sc=null;
                }
                if(client_sc!=null)
                {
                    client_sc.close();
                    client_sc=null;
                }
                if(ssc_selector!=null)
                {
                    ssc_selector.close();
                    ssc_selector=null;
                }
                if(csc_selector!=null)
                {
                    csc_selector.close();
                    csc_selector=null;
                }
                System.gc();
                System.out.println("连接关闭" );
            }catch (Exception e){
                System.out.println("关闭异常: " );
                e.printStackTrace();
            }
        }
    }
    //0：shutdown，1：success，-1：timeout or close，-2：心跳包超时
    private class Handler{

        public int handlerAccept(SelectionKey key) throws IOException, InterruptedException {
            System.out.println("开始建立连接");
            reset = 0;
            client_sc = ((ServerSocketChannel) key.channel()).accept();
            int times=0;
            while (true) {
                if(times<100){
                    if (client_sc != null) {
                        break;
                    }
                    times++;
                    Thread.sleep(20);
                }else{
                    return -1;
                }
            }
            System.out.println("连接成功");
            client_sc.configureBlocking(false);
            client_sc.register(csc_selector, SelectionKey.OP_READ, ByteBuffer.allocate(bufferSize));
            return 1;
        }

        public int handlerRead(SelectionKey key) throws IOException, NoSuchMethodException, IllegalAccessException, UiObjectNotFoundException, InstantiationException, InvocationTargetException, InterruptedException {
            System.out.println("开始接收数据" );
            int wRet;
            reset=0;
            client_sc=(SocketChannel)key.channel();
            ByteBuffer buffer=(ByteBuffer)key.attachment();
            String data = read(client_sc,buffer);
            if(data==null){
                return -1;
            }
            System.out.println("收到报文："+data);
            if(param==null){
                param=data;
            }else{
                param+=data;
            }
            param = param.replace("heartbeat\n","");
            if(param.equals("")){
                return 1;//忽略心跳包
            }
            if(param.charAt(param.length()-1)== '\n'){
                param=param.trim();
                System.out.println("接收数据成功" +param);
                if(param.equals("shutdown") || param.equals("start")){
                    msg="ok\n";
                    buffer=ByteBuffer.wrap(msg.getBytes("UTF-8"));
                    System.out.println("开始发送数据" );
                    if(key.isValid()) {
                        wRet=write(client_sc,buffer);
                        if(wRet<1){
                            return wRet;
                        }
                    }else{//key not valid
                        return -1;
                    }//end if(key.isValid())
                    if(param.equals("shutdown")){
                        return 0;
                    }else{
                        param=null;
                        return 1;
                    }
                }
                msg=executor.ExecuteCase(new AndroidCommand(param))+'\n';
                param=null;
                buffer=ByteBuffer.wrap(msg.getBytes("UTF-8"));
                System.out.println("开始发送数据" );
                if(key.isValid()) {
                    wRet=write(client_sc,buffer);
                    if(wRet<1){
                        return wRet;
                    }
                }else{//key not valid
                    return -1;
                }//end if(key.isValid())
                System.out.println("发送数据成功" );
            }
            return 1;
        }//end handlerRead

        private String read(SocketChannel client_sc,ByteBuffer buffer) throws IOException, InterruptedException {
            int nRead;
            buffer.clear();
            nRead=client_sc.read(buffer);
            if(nRead>0){
                buffer.flip();
                return Charset.forName("UTF-8").newDecoder().decode(buffer).toString();
            }else if(nRead==0){
                return "";
            }else{//r=-1
                System.out.println("无接收数据");
                client_sc.close();
                key.cancel();
                return null;
            }

        }

        private int write(SocketChannel client_sc,ByteBuffer buffer) throws IOException, InterruptedException {
            int nWrite;
            int times=0;
            while (buffer.hasRemaining()) {
                nWrite=client_sc.write(buffer);
                if (nWrite > 0) {
                    times = 0;
                    continue;
                } else if (nWrite == 0) {
                    if(times<40) {
                        times++;
                        Thread.sleep(50);
                        continue;
                    }else{//times>=40
                        return -1;
                    }
                }//end if(client_sc.write(buffer))
            }//end while
            return 1;
        }
    }
}