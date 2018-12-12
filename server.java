package com.example.wuziqiserver;


import java.awt.Point;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;




public class Server {

    //private BufferedReader in;
    private ObjectInputStream inObj;
    private PrintWriter out;
    private List<Socket> sockets=new ArrayList<Socket>();
    private Map<Socket,Socket> maps=new HashMap<Socket, Socket>();
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        new Server();
    }
    public Server(){
        try {
            ServerSocket serverSocket=new ServerSocket(1888);
            System.out.println("���������гɹ�");
            Socket client=null;
            BufferedReader in=null;
            while(true){
                client=serverSocket.accept();
                sockets.add(client);
                in=new BufferedReader(new InputStreamReader(client.getInputStream(),"UTF-8"));
                System.out.println("ip:"+client.getRemoteSocketAddress().toString());
                new Thread(new ReceiveThread(client,in)).start();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("�����������쳣");
            e.printStackTrace();
        }
    }
    class ReceiveThread implements Runnable{
        private Socket socket;
        private BufferedReader br;
        public ReceiveThread(Socket socket,BufferedReader br){
            this.socket=socket;
            this.br=br;
            //System.out.println("����������Ϣ�߳�");
        }
        public void run() {
            // TODO Auto-generated method stub

            String msg=null;

            while(true){
                try {
                    while((msg=br.readLine())!=null){
                        System.out.println("�յ��ͻ�����Ϣ:"+msg);
                        handleClientMessage(msg,socket);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }
    private void handleClientMessage(String msg,Socket socket){
        int index=-1;
        Socket value=null;
        if(msg.equals("match")){
            //���ƥ����Ч�û�����ӵ�maps
            //System.out.println("ϵͳ����Ϊ��ƥ�����");
            value=getValidUser(socket);
            if(value==null){
                send(socket, "û��ƥ�䵽���");
            }else{
                //System.out.println("ƥ��ɹ�");
                maps.put(socket, value);
                //send(value, "ƥ��ɹ�");
                //send(socket, "ƥ��ɹ�");
                //�����һ���������
                int r=getRandom(2);
                System.out.println("r="+r);
                if(r==0){
                    send(socket,"������");
                    send(value,"�Է�����");
                }
                else if(r==1){
                    send(socket, "�Է�����");
                    send(value, "������");
                }
            }

        }else if(msg.equals("match_failed")){
            sockets.remove(socket);
        }else if(msg.equals("exit") || msg.equals("leave")){
            //System.out.println("�˳���socket��:"+socket.toString());
            send(getMyMatch(socket), msg);
            deleteMatch(socket);
            sockets.remove(socket);
            Thread.currentThread().interrupt();//��ֹ�߳�
			/*try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
        }else if((index=msg.indexOf(","))>-1){
            send(getMyMatch(socket), msg.substring(0, index)+","+msg.substring(index+1));

        }else if(msg.startsWith("chat")){
            //int n=Integer.parseInt(msg.substring(3));
            send(getMyMatch(socket), msg);
        }else if(msg.equals("timeout_no_play")){
            send(getMyMatch(socket), "oppo_timeout");
        }else if(msg.equals("continue")){
            int r=getRandom(2);
            System.out.println("r="+r);
            if(r==0){
                send(socket,"������");
                send(getMyMatch(socket),"�Է�����");
            }
            else if(r==1){
                send(socket, "�Է�����");
                send(getMyMatch(socket), "������");
            }
        }

    }
    //�ҵ�����
    private Socket getMyMatch(Socket socket){
        for(Map.Entry<Socket, Socket> entry : maps.entrySet()){
            if(entry.getKey()==socket){
                return entry.getValue();
            }else if(entry.getValue()==socket){
                return entry.getKey();
            }
        }
        return null;
    }
    private int getRandom(int max){
        Random r=new Random();
        return r.nextInt(max);
    }
    private Socket getValidUser(Socket socket){
        //int index;
        Socket value=null;
        Socket temp=null;
		/*if(sockets.size()>1){
			do{
				index=getRandom(sockets.size());
				//System.out.println(index);
				value=sockets.get(index);
			}while((value==socket || isMatchOther(value)) && isMatched(socket)==false);
		}*/
        //ѭ��ƥ��
        for(int i=0;i<sockets.size();i++){
            temp=sockets.get(i);
            if(temp!=socket && isMatchOther(temp)==false && isMatchOther(socket)==false){
                value=temp;
                break;
            }
        }
        return value;
    }

    //�Ѿ�ƥ���˱���
    private boolean isMatchOther(Socket socket){
        for(Map.Entry<Socket, Socket> entry : maps.entrySet()){
            if(entry.getKey()==socket || entry.getValue()==socket){
                return true;
            }
			/*if(entry.getValue()==socket && entry.getKey()!=null){
				return true;
			}*/

        }
        return false;
    }
    //�Լ��Ƿ���ƥ��
	/*private boolean isMatched(Socket socket){
		for(Map.Entry<Socket, Socket> entry : maps.entrySet()){
			if(entry.getKey()==socket || entry.getValue()==socket ){
				//send(socket, "���Ѿ���ƥ����");
				return true;
			}
		}
		return false;
	}*/
    //ɾ��ƥ���û�
    //socket���˳���socket
    private void deleteMatch(Socket socket){

        for(Map.Entry<Socket, Socket> entry : maps.entrySet()){
            Socket key=entry.getKey();
            Socket value=entry.getValue();
            //System.out.println(key.toString()+","+value.toString());
            if(key!=null && key==socket){
                //System.out.println("key=socket");
                maps.remove(key,value);
                //if(value!=null)
                //send(value, "�Է�����");

            }
            if(value!=null && value==socket){
                //System.out.println("value=socket");
                maps.remove(key,value);
                //send(key, "�Է�����");
            }

        }

    }

    private void send(Socket socket,String msg){
        try {

            if(socket!=null){
                System.out.println("����->"+msg);
                out=new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8")));

                out.println(msg);
                out.flush();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
