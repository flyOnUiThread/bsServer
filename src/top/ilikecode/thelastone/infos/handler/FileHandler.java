package top.ilikecode.thelastone.infos.handler;

import top.ilikecode.thelastone.infos.InfoCache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
*   生成日志文件
 */
public class FileHandler {

    //TODO 生成日志文件，包含玩家名称
    public void makeLog(){
        try{
            File file=new File("/home/admin/applicationlogs/Server.log");
//            File file=new File("F:/applicationlogs/Server.log");
            if(!file.exists()){
                file.createNewFile();
            }
            FileOutputStream out=new FileOutputStream(file,true);
            StringBuffer sb=new StringBuffer();
            sb.append(GetNowDate());
            for (int i = 0;i<InfoCache.playerNames.size();i++){
                sb.append(InfoCache.playerNames.get(i)+"\n");
                sb.append(InfoCache.playerIps.get(i)+"\n");
                sb.append(InfoCache.playerMacs.get(i)+"\n");
                sb.append("当前所在房间："+InfoCache.roomId+"\n");
                sb.append("\n###########################################\n\n");
            }

            out.write(sb.toString().getBytes("utf-8"));
            out.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public String GetNowDate(){
        String temp_str = "";
        Date dt = new Date();
        //最后的aa表示“上午”或“下午”    HH表示24小时制    如果换成hh表示12小时制   
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss aa");
        temp_str=sdf.format(dt);
        return temp_str+"\n";
    }
}
