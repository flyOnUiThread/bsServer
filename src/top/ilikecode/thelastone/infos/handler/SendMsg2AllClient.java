//package top.ilikecode.thelastone.infos.handler;
//
//import top.ilikecode.thelastone.infos.DownloadInfo;
//
//import java.io.IOException;
//import java.io.ObjectOutputStream;
//import java.net.Socket;
//
//public class SendMsg2AllClient extends Thread{
//    private Socket socket;
//    private DownloadInfo downloadInfo;
//
//    public SendMsg2AllClient(Socket socket,DownloadInfo downloadInfo){
//        this.socket = socket;
//        this.downloadInfo = downloadInfo;
//    }
//
//    @Override
//    public void run() {
//        try {
//            ObjectOutputStream o = new ObjectOutputStream(socket.getOutputStream());
//            o.writeObject(downloadInfo);
//            o.flush();
////            o.close();                    //关闭流会造成对应的socket也被关闭
////            socket.shutdownOutput();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
