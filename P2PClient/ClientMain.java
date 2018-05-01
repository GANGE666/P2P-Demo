package P2PClient;

import java.net.DatagramSocket;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.annotation.processing.Messager;

public class ClientMain extends Thread {
	
	public static final String ServerIP = "63.209.35.169";
	public static final int ServerPort = 30000;
	
	public static final String CHECK_DATA 				= "CHECKDATA";

	public static final int LOGIN_UID 			= 0x0000;
	public static final int LOGOUT_UID 			= 0x0001;
	public static final int ASK_CONNECT_UID		= 0x0002;
	
	public static final int SERVER_RECEIVE 	= 0x1000;
	public static final int CONFIRM_CONNECT	= 0x1001;
	public static final int RECEIVE_CONFIRM_CONNECT		= 0x1002;
	
	public static final int SEND_MSG		= 0x2000;
	public static final int RECEIVE_MSG		= 0x2001;
	
	private static String MYUID = "038327493";
	
	private static Deque<String> MsgQueue = new LinkedList<>();
	public static Map<Integer, Boolean> SendReceive = new HashMap<>();
	public static Map<Integer, String> ClientTable = new HashMap<>();
	public static Deque<String> DoneList = new LinkedList<String>();
	
	public static final int DoneListMaxNum = 100;
	
		
	private static DatagramSocket client;
    private static int MyUDPListenPort = 30000;
    
	public static int MsgID = 1000000000;
	
	public void run(){
		try{
			MsgQueue.clear();
			client = new DatagramSocket(MyUDPListenPort);
			
			/*
			 * C1 Login
			 */
			//UserAddInstr.Login(Integer.valueOf(ClientMain.GetMyUID()));
			
			System.out.println("Client Begin");
						
			ClientReceiveMsg clientReceiveMsg = new ClientReceiveMsg(client);
			clientReceiveMsg.start();
			
			ClientExeMsg clientExeMsg = new ClientExeMsg(client);
			clientExeMsg.start();
			
			/*
			 * C2 Login and connect C1 send Msg to C1
			 */
			UserAddInstr.Login(Integer.valueOf(ClientMain.GetMyUID() + 1));
			UserAddInstr.AskConnect(Integer.valueOf(GetMyUID() + 1), Integer.valueOf(GetMyUID()));
			
			
			sleep(5000);
			System.out.println(UserAddInstr.SendMessage(Integer.valueOf(GetMyUID() + 1), Integer.valueOf(GetMyUID()), "Hello C1"));
			
			
			
			clientExeMsg.join();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static String GetMyUID(){
		return MYUID;
	}
	public static boolean InsertMsg(String Msg){
		try{
			MsgQueue.offer(Msg);
			//System.out.println("Insert : \t" + Msg + "  " + MsgQueueEmpty());
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public static String GetLastMsg(){
		if(!MsgQueue.isEmpty())
			return MsgQueue.getLast();
		else
			return null;
	}
	public static String PollMsg(){
		if(!MsgQueue.isEmpty())
			return MsgQueue.poll();
		else
			return null;
	}
	public static boolean MsgQueueEmpty(){
		if(MsgQueue.isEmpty())
			return true;
		else 
			return false;
	}
	
	public static DatagramSocket GetClient(){
		if(client!=null)
			return client;
		else 
			System.out.println("Client null");
			return null;
	}
}
