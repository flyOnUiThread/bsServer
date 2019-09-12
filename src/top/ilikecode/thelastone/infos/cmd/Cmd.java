package top.ilikecode.thelastone.infos.cmd;

import top.ilikecode.thelastone.infos.handler.FileHandler;
import top.ilikecode.thelastone.infos.socket.Server;

import java.io.IOException;
import java.util.Scanner;

public class Cmd {
    private FileHandler fileHandler = new FileHandler();
    private Scanner scanner = new Scanner(System.in);
    private String cmd;
    private Server server = new Server();
    private String help = "restart:重启服务器;\n" +
            "closeserver:关闭服务器;\n" +
            "serverstate:查看当前服务器运行状态\n" +
            "help:查看帮助;\n" +
            "makelog:生成日志文件\n";

    //TODO 监听服务器输入
    public void listenCmd() {
        try{
            while (true){
                cmd = scanner.nextLine();
                switch (cmd){
                    case "restart":
                        server.closeServer();
                        server.start();
                        break;
                    case "closeserver":
                        server.closeServer();
                        return;
                    case "help":
                        System.out.println(help);
                        break;
                    case "serverstate":
                        System.out.println(" ");
                        break;
                    case "makelog":
                        fileHandler.makeLog();
                        break;
                    default:
                        System.out.println("输入的命令不存在，输入help查看所有命令");
                        break;
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
