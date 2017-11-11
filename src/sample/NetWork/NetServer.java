package sample.NetWork;


import sample.util.Constants;
import sample.util.DataPacket;
import sample.util.UserManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by HUSTy on 2017/10/20.
 */
public class NetServer {
    private ServerSocket serverSocket;
    private volatile static NetServer instance;
    public static NetServer getInstance(){
        if(instance==null)
            synchronized (NetServer.class){
                if (instance==null)
                    instance=new NetServer();
            }
        return instance;
    }

    private NetServer(){
        try {
            serverSocket = new ServerSocket(Constants.PORT_SERVER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class RunServer implements Runnable{

        @Override
        public void run() {
            while (true){
                try {
                    Socket socket=serverSocket.accept();
                    InputStream inputStream=socket.getInputStream();
                    ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                    DataPacket dataPacket = (DataPacket) objectInputStream.readObject();
                    Thread thread = new Thread(new UserManager(dataPacket));
                    thread.start();
                    objectInputStream.close();
                    inputStream.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void startLearning(){
        Thread thread=new Thread(new RunServer());
        thread.start();
    }

}
