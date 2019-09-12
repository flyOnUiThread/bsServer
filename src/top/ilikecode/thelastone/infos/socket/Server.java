package top.ilikecode.thelastone.infos.socket;

import top.ilikecode.thelastone.infos.DownloadInfo;
import top.ilikecode.thelastone.infos.InfoCache;
import top.ilikecode.thelastone.infos.UploadInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static final int port = 55211;
    private static ServerSocket serverSocket;
    private static ArrayList<Socket> sockets = new ArrayList<Socket>();

    //TODO 开始监听服务器端口
    public void start() {
        try{
            serverSocket = new ServerSocket(port);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //TODO 监听客户端的连接，并将客户端的socket添加进入相应的ArrayList
    public Socket acceptClient(){
        Socket socket = null;
        try{
            socket = serverSocket.accept();
            socket.setSoTimeout(3000);              //设置3s超时
            sockets.add(socket);
            System.out.println("客户端" + socket.getLocalAddress() + "已连接");
            InfoCache.playerNum++;
        }catch (IOException e){
            e.printStackTrace();
        }
        return socket;
    }

    //TODO 发送数据对象给固定客户机
    public void sendMsg(Socket s,DownloadInfo down){
        try{
            ObjectOutputStream o = new ObjectOutputStream(s.getOutputStream());
            o.writeObject(down);
            o.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //TODO 给所有客户机发送数据对象
    public void sendMsg2All(DownloadInfo down){
        for (Socket s:sockets){
            try {
                ObjectOutputStream o = new ObjectOutputStream(s.getOutputStream());
                o.writeObject(down);
                o.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //TODO 接收来自固定客户机的数据对象
    public UploadInfo getMessage(Socket s) throws IOException, ClassNotFoundException {
        ObjectInputStream i = new ObjectInputStream(s.getInputStream());
        UploadInfo up = (UploadInfo)i.readObject();
        if (up!=null){
            return up;
        }
        return null;
    }

    //TODO 关闭服务器，关闭所有socket，清空相关arraylist
    public void closeServer() throws IOException{
        for (Socket s:sockets){
            s.close();
        }
        sockets.clear();
    }

    //TODO 关闭指定客户机的连接
    public static void closeSocket(int client){
        try{
            sockets.get(client).close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void closeSocket(Socket s){
        try {
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sockets.remove(s);
    }

    //TODO 获取指定客户机的socket对象
    public Socket getSocket(int client){
        return sockets.get(client);
    }

    public ArrayList<Socket> getSockets(){
        return sockets;
    }

    public void setSockets(ArrayList<Socket> sockets){
        this.sockets = sockets;
    }

    //TODO 获取当前服务器中连接的客户机数量
    public static int getClientNum(){
        int num = 0;
        num = sockets.size();
        return num;
    }
}
