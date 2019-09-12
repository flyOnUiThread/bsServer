import top.ilikecode.thelastone.infos.cmd.Cmd;
import top.ilikecode.thelastone.infos.handler.InfoHandler;
import top.ilikecode.thelastone.infos.DownloadInfo;
import top.ilikecode.thelastone.infos.socket.Server;


public class Main {
    private static Server server = new Server();
    private static Thread cmdThread,acceptClientThread;
    public static void main(String[] args){

        server.start();         //启动socket服务器

        //服务器端命令行线程
        cmdThread = new Thread(new Runnable() {
            private  Cmd cmd = new Cmd();
            @Override
            public void run() {
                    cmd.listenCmd();
                    System.exit(0);
            }
        });

        //接收客户机连接,并开启相应客户机的线程
        acceptClientThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    InfoHandler infoHandler = new InfoHandler(server.acceptClient());
                    infoHandler.start();
                }
            }
        });

        openThread();
    }
    //TODO 开启线程方法
    private static void openThread(){
        cmdThread.start();
        acceptClientThread.start();
    }
}