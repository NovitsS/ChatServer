package sample.util;

import sample.Main;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class UserManager implements Runnable{
    private DataPacket dataPacket;

    public UserManager(DataPacket dataPacket){
        this.dataPacket = dataPacket;
    }

    @Override
    public void run() {
        String kind = dataPacket.getKind();
        switch (kind) {
            case Constants.KIND_LOGIN:
                login();
                break;
            case Constants.KIND_REGISTER:
                register();
                break;
            case Constants.KIND_REVERSE:
                reverse();
                break;
            case Constants.KIND_ADD:
                add();
                break;
            case Constants.KIND_OFFLINE_MSG:
                offlineMsg(dataPacket.getUserPassword(),dataPacket.getContent());
                break;
            case Constants.KIND_OFFLINE:
                offline();
                break;
        }
    }

    private void login(){
        User user = Main.map.get(dataPacket.getUserAccount());
        if(user!=null){
            if(user.getPwd().equals(dataPacket.getUserPassword())){
                user.setOnline();
                Msg msg = new Msg(Constants.KIND_SERVER);
                msg.setContent(Constants.SUCESS_LOGIN);
                Thread thread = new Thread(new ToClient(msg));
                thread.start();
                if(user.getOfflineMsg().equals("true")){
                    List<String> list = Main.msgMap.get(user.getId());
                    for(int i=0;i<list.size();i++){
                        Msg msg1=new Msg(Constants.KIND_SERVER);
                        msg1.setContent("offlineMsg");
                        msg1.setIsOnLine(list.get(i));
                        Thread thread1 = new Thread(new ToClient(msg));
                        thread1.start();
                    }
                    user.setOfflineMsg("false");
                    Main.msgMap.remove(dataPacket.getUserAccount());
                }
            }else{
                Msg msg = new Msg(Constants.KIND_SERVER);
                msg.setContent(Constants.ERROR_ACCOUNT_PASSWORD);
                Thread thread = new Thread(new ToClient(msg));
                thread.start();
            }
        }else{
            Msg msg = new Msg(Constants.KIND_SERVER);
            msg.setContent(Constants.ERROR_NO_MATCH);
            Thread thread = new Thread(new ToClient(msg));
            thread.start();
        }
    }

    private void register(){
        User user = Main.map.get(dataPacket.getUserAccount());
        if(user!=null){
            Msg msg = new Msg(Constants.KIND_SERVER);
            msg.setContent(Constants.ERROR_HAS);
            Thread thread = new Thread(new ToClient(msg));
            thread.start();
        }else{
            User newUser = new User(dataPacket.getUserAccount(), dataPacket.getUserPassword(), dataPacket.getUserIP());
            Main.map.put(newUser.getId(), newUser);
            Msg msg = new Msg(Constants.KIND_SERVER);
            msg.setContent(Constants.SUCESS_REGISTER);
            Thread thread = new Thread(new ToClient(msg));
            thread.start();
            //TODO:写入文件/数据库
        }
    }

    private void reverse(){
        User user = Main.map.get(dataPacket.getUserAccount());
        if(user!=null){
            if(user.getPwd().equals(dataPacket.getUserPassword())){
                user.setPwd(dataPacket.getUserNewPassword());
                Msg msg = new Msg(Constants.KIND_SERVER);
                msg.setContent(Constants.SUCESS_REVERSE);
                Thread thread = new Thread(new ToClient(msg));
                thread.start();
                //TODO:写入文件/数据库
            }else{
                Msg msg = new Msg(Constants.KIND_SERVER);
                msg.setContent(Constants.ERROR_ACCOUNT_PASSWORD);
                Thread thread = new Thread(new ToClient(msg));
                thread.start();
            }
        }else{
            Msg msg = new Msg(Constants.KIND_SERVER);
            msg.setContent(Constants.ERROR_NO_MATCH);
            Thread thread = new Thread(new ToClient(msg));
            thread.start();
        }
    }

    private void add(){
        //TODO:处理添加好友信息
        User user = Main.map.get(dataPacket.getTargetId());
        if(user!=null){
            //TODO：发送信息更新双方客户端的listview的信息
            Msg msg=new Msg(Constants.KIND_SERVER);
            msg.setContent(Constants.SUCESS_ADD);
            msg.setIp(user.getIp());
            msg.setId(dataPacket.getTargetId());
            msg.setIsOnLine(user.getIsOnline());
            Thread thread = new Thread(new ToClient(msg));
            thread.start();
        }else{
            //TODO:返回账号不存在
        }
    }

    private void offlineMsg(String client,String content){
        //TODO:处理离线消息信息
        User user = Main.map.get(dataPacket.getTargetId());
        if(user!=null){
            if(user.getOfflineMsg().equals("false")){
                user.setOfflineMsg("true");
                ArrayList<String> list = new ArrayList<>();
                Main.msgMap.put(user.getId(),list);
                Main.msgMap.get(user.getId()).add(client+":"+content);
            }else {
                Main.msgMap.get(user.getId()).add(client+":"+content);
            }
        }
    }

    private void offline(){
        User user = Main.map.get(dataPacket.getUserAccount());
        if(user!=null){
            user.setNotOnLine();
        }else{
        }
    }

    class ToClient implements Runnable{
        Msg msg;
        public ToClient(Msg msg){
            this.msg = msg;
        }

        @Override
        public void run() {
            try {
                Socket socket = new Socket(dataPacket.getUserIP(), Constants.PORT_CLIENT);
                socket.setReuseAddress(true);
                socket.setSoTimeout(5000);

                OutputStream outputStream = socket.getOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(msg);

                objectOutputStream.flush();
                objectOutputStream.close();
                outputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
