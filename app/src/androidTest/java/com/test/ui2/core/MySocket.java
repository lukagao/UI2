package com.test.ui2.core;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.google.gson.JsonObject;
import com.test.ui2.utils.AndroidCommand;

public class MySocket {
    private ServerSocket server = null;
    private int PORT=5173;
    private String msg=null;
    private ActionExecutor executor;
    private Socket client = null;
    private String param;
    private PrintWriter writer = null;
    private BufferedReader reader=null;
    JsonObject errorJson=null;
    public MySocket(){
        executor=new ActionExecutor();
    }
    public void start(){
        try {
            server = new ServerSocket(PORT);
            System.out.println("服务器搭建成功");
            System.out.println("开始接受客户端连接");
            client = server.accept();
            System.out.println("接受客户端连接成功");
            reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
            //writer.println("Success");
            while(true) {
                if(!client.isClosed()) {
                    System.out.println("开始接收数据： ");
                    param = reader.readLine();
                    if (param == null) {
                        throw new Exception("连接关闭");
                    }
                    System.out.println("接收数据成功: " + param);
                    if(param.equals("shutdown")){
                        writer.println("end");
                        break;
                    }
                    msg=executor.ExecuteCase(new AndroidCommand(param));
                    writer.println(msg);
                    System.out.println("发送数据成功");
                }else{
                    throw new Exception("连接关闭");
                }
            }
        }catch (Exception e) {
            if(!client.isClosed()) {
                try{
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw, true);
                    e.printStackTrace(pw);
                    pw.flush();
                    sw.flush();
                    System.out.println("异常原因: "+sw.toString());
                    errorJson=new JsonObject();
                    errorJson.addProperty("result", "Error");
                    errorJson.addProperty("reason", "Internal error");
                    writer.println(errorJson.toString());
                }catch (Exception se){
                }

            }
        }finally {
            try {
                reader.close();
                writer.close();
                client.close();
                server.close();
                System.out.println("Socket closed: " );
            } catch (IOException e) {
                System.out.println("关闭异常: " );
                e.printStackTrace();
            }
        }

    }

}

