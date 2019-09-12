package top.ilikecode.thelastone.infos.handler;

import top.ilikecode.thelastone.infos.DownloadInfo;
import top.ilikecode.thelastone.infos.InfoCache;
import top.ilikecode.thelastone.infos.UploadInfo;
import top.ilikecode.thelastone.infos.socket.Server;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Random;

//上传数据和下载数据处理线程
public class InfoHandler extends Thread{
    private DownloadInfo downloadInfo = new DownloadInfo();
    private Server server = new Server();
    private Socket socket;

    public InfoHandler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        while (!isClose()){                 //断开连接跳出循环
            UploadInfo up = null;
            try {
                up = server.getMessage(socket);
            } catch (IOException e) {
                if (e instanceof SocketTimeoutException){       //捕捉超时
                    run();
                    return;
                }
            } catch (ClassNotFoundException e) {
                //不做处理
            }
            if (up!=null){
//                System.out.println(up.getUserName());
//                System.out.println(up.getIpAddress());
//                System.out.println(up.getMacAddress());
//                System.out.println(up.getRequest());
                saveInputData(up);
                handle(socket,up.getRequest());
            }
        }

        try {               //断开连接remove动态数组中的数据
            socket.close();
            ArrayList<Socket> sockets = server.getSockets();
            int index = sockets.indexOf(socket);
            sockets.remove(socket);
            server.setSockets(sockets);
            InfoCache.playerNames.remove(index);
            InfoCache.playerIps.remove(index);
            InfoCache.playerMacs.remove(index);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO 判断客户机是否断开
    //向客户机发送0xFF，但是客户机并未开启setOOBInline所以并不会处理，
    //但可由此判断客户机是否断开连接，从而保持长连接
    private boolean isClose(){
        try {
            socket.sendUrgentData(0xFF);
        } catch (IOException e) {       //客户机断开连接
            System.out.println(socket.getLocalAddress()+"断开连接");
            return true;
        }
        return false;
    }

    //TODO 将上传上来的用户数据添加到缓存区arrayList
    private void saveInputData(UploadInfo up){
        if (!InfoCache.playerNames.contains(up.getUserName())){
            InfoCache.playerNames.add(up.getUserName());
            InfoCache.playerMacs.add(up.getMacAddress());
            InfoCache.playerIps.add(up.getIpAddress());
        }
    }

    //TODO 处理请求码，构造对应的下载数据，并发送
    private void handle(Socket s, int request){
        if (request==0 ){                //无状态
            downloadInfo.setDice(new int[]{0,0});
            downloadInfo.setRoomId("");
            downloadInfo.setPlayerNames(InfoCache.playerNames);
            downloadInfo.setStart(false);
            server.sendMsg2All(downloadInfo);           //向所有客户机发送
        }else if (request==1){          //生成房间号
            System.out.println("构造房间号");
            if (InfoCache.roomId.equals("")){
                downloadInfo.setRoomId(makeRoomId());
//                System.out.println("生成"+InfoCache.roomId);
            }else{
                downloadInfo.setRoomId(InfoCache.roomId);
//                System.out.println("已生成"+InfoCache.roomId);
            }
            downloadInfo.setDice(new int[]{0,0});
            InfoCache.roomId = downloadInfo.getRoomId();
            downloadInfo.setPlayerNames(InfoCache.playerNames);
            downloadInfo.setStart(false);

        }else if (request==2){          //摇骰子之后将消息发送给所有玩家
            downloadInfo.setDice(makeDice());
            InfoCache.dice = downloadInfo.getDice();
//            System.out.println(InfoCache.dice[0]+InfoCache.dice[1]);
            downloadInfo.setPlayerNames(InfoCache.playerNames);
            downloadInfo.setStart(false);
            downloadInfo.setRoomId("");
            server.sendMsg2All(downloadInfo);
            return;
        }else if (request==3){          //游戏开始
            downloadInfo.setDice(new int[]{0,0});
            downloadInfo.setRoomId("");
            downloadInfo.setStart(true);        //由非房主玩家判断来开始游戏
            downloadInfo.setPlayerNames(InfoCache.playerNames);
            server.sendMsg2All(downloadInfo);
            return;
        }
        server.sendMsg(s,downloadInfo);
    }

    //TODO 把随机数添加到DownLoadInfo对象中
    private int[] makeDice(){
        return new int[]{makeNum(6,1),makeNum(6,1)};
    }

    //TODO 生成随机数
    private int makeNum(int max,int min) {
        return (int)min + new Random().nextInt(max);
    }

    //TODO 生成六位房间号的字符串
    private String makeRoomId(){
        String roomId = "";
        int ONE = makeNum(9,0);
        int TWO = makeNum(9,0);
        int THREE = makeNum(9,0);
        int FOUR = makeNum(9,0);
        int FIVE = makeNum(9,0);
        int SIX = makeNum(9,0);
        roomId = Integer.toString(ONE)+Integer.toString(TWO)+Integer.toString(THREE)
                +Integer.toString(FOUR)+Integer.toString(FIVE)+Integer.toString(SIX);
        return roomId;
    }
}
