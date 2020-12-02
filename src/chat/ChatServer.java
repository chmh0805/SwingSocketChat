package chat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Vector;

import com.google.gson.Gson;

import lombok.Data;
import protocol.Protocol;
import protocol.RequestDto;

@Data
public class ChatServer {
	
	private ChatServer chatServer = this;
	private static final String TAG = "ChatServer : ";
	private ServerSocket serverSocket;
	private Socket socket;
	private Vector<ClientThread> vc;
	private RequestDto dto;
	
	private FileWriter fout;
	
	private String hour;
	private String minute;
	private String second;
	
	public ChatServer() {
		vc = new Vector<>();
		try {
			fout = new FileWriter("chatlog.txt");
		} catch (IOException e1) {
			System.out.println("로그파일 생성 실패");
		}
		try {
			serverSocket = new ServerSocket(Protocol.PORT);
			System.out.println(TAG + "클라이언트 연결 대기중....");
			while (true) {
				socket = serverSocket.accept();
				System.out.println(TAG + "연결 성공");
				ClientThread ct = new ClientThread(socket);
				ct.start();
				vc.add(ct);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	class ClientThread extends Thread {
		private Socket socket;
		private BufferedReader reader;
		private PrintWriter writer;
		private String id;
		
		public ClientThread(Socket socket) {
			this.socket = socket;
			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer = new PrintWriter(socket.getOutputStream(), true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			String text = null;
			Gson gson = new Gson();
			dto = new RequestDto();
			try {
				while ((text = reader.readLine()) != null) {
					dto = gson.fromJson(text, RequestDto.class);
					hour = Integer.toString(LocalDateTime.now().getHour());
					minute = Integer.toString(LocalDateTime.now().getMinute());
					second = Integer.toString(LocalDateTime.now().getSecond());
					String textTemp = "[" + hour + ":" + minute + ":" + second + "]" + "[" + dto.getId() + "] " + dto.getText();
					if (dto.getTo() == null) {
						for (int i = 0; i < vc.size(); i++) {
							if (vc.get(i) != this) {
								vc.get(i).writer.println(textTemp);
							}
						}
					} else if (dto.getTo() != null) {
						for (int i = 0; i < vc.size(); i++) {
							if (vc.get(i).id.equalsIgnoreCase(this.id)) {
								vc.get(i).writer.println(textTemp);
							}
						}
					}
					fout.write(textTemp + "\n");
				}
			} catch (IOException e) {
				System.out.println(TAG + "연결 종료");
				try {
					fout.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		new ChatServer();
	}
	
}
