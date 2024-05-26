package lol.tgformat.irc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author TG_format
 * @since 2024/5/26 12:06
 */
public class ClientMain {
    public interface ObjectAction{
        void doAction(Object obj, ClientMain clientMain);
    }

    public static final class DefaultObjectAction implements ObjectAction{
        public void doAction(Object obj, ClientMain clientMain) {
            System.out.println("Dispatch：\t"+obj.toString());
        }
    }

    private final String serverIp;
    private final int port;
    private static Socket socket;
    private boolean running=false; //连接状态

    private long lastSendTime; //最后一次发送数据的时间

    //用于保存接收消息对象类型及该类型消息处理的对象
    private final ConcurrentHashMap<Class, ObjectAction> actionMapping = new ConcurrentHashMap<>();

    public ClientMain(String serverIp, int port) {
        this.serverIp=serverIp;
        this.port=port;
    }

    public void start() throws IOException {
        if(running)return;
        socket = new Socket(serverIp,port);
        System.out.println("LocalPort："+socket.getLocalPort());
        lastSendTime=System.currentTimeMillis();
        running=true;
        new Thread(new KeepAliveListener()).start();  //保持长连接的线程，每隔2秒向服务器发一个心跳
        new Thread(new ReceiveListener()).start();    //接受消息的线程，处理消息
    }

    public void stop(){
        if(running)running=false;
    }

    /**
     * 添加接收对象的处理对象。
     * @param cls 待处理的对象，其所属的类。
     * @param action 处理过程对象。
     */
    public void addActionMap(Class<Object> cls,ObjectAction action){
        actionMapping.put(cls, action);
    }

    public static void sendObject(Object obj) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(obj);
        System.out.println("Send：\t"+obj);
        oos.flush();
    }

    class KeepAliveListener implements Runnable{
        long checkDelay = 10;
        long keepAliveDelay = 1000;
        public void run() {
            while(running){
                if(System.currentTimeMillis()-lastSendTime>keepAliveDelay){
                    try {
                        JSONObject jo = new JSONObject();
                        jo.put("type","KeepAlive");
                        sendObject(jo);
                    } catch (IOException e) {
                        e.printStackTrace();
                        ClientMain.this.stop();
                    }
                    lastSendTime = System.currentTimeMillis();
                }else{
                    try {
                        Thread.sleep(checkDelay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        ClientMain.this.stop();
                    }
                }
            }
        }
    }

    class ReceiveListener implements Runnable{
        public void run() {
            while(running){
                try {
                    InputStream in = socket.getInputStream();

                    InputStreamReader isr = new InputStreamReader(in, StandardCharsets.UTF_8);//以utf-8读
                    BufferedReader br = new BufferedReader(isr);
                    Object obj = br.readLine();
                    System.out.println("Receive：\t"+obj);

                    JSONObject jo = JSON.parseObject(obj.toString());
                    ReceiveData data = jo.toJavaObject(ReceiveData.class);
                    JSONProcessor processor = new JSONProcessor(data);
                    processor.process();

                    ObjectAction oa = actionMapping.get(obj.getClass());
                    oa = oa==null?new DefaultObjectAction():oa;
                    oa.doAction(obj, ClientMain.this);

                } catch (Exception e) {
                    e.printStackTrace();
                    ClientMain.this.stop();
                }
            }
        }
    }
}
