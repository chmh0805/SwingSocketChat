package chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.google.gson.Gson;

import protocol.Protocol;
import protocol.RequestDto;

public class ChatClient extends JFrame implements AppFrame {
	
	private ChatClient chatClient = this;
	private final static String TAG = "ChatClient : ";

	private JPanel topPanel, bottomPanel;
	private JTextField tfHost, tfText;
	private JButton btnConnect, btnSend;
	private JTextArea taChatList;
	private ScrollPane scrollpane;
	
	private String id;
	private RequestDto dto;
	private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;
	
	public ChatClient(String id) {
		this.id = id;
		
		init();
		
		setting();
		
		batch();
		
		listener();
		
		setVisible(true);
	}

	@Override
	public void init() {
		topPanel = new JPanel();
		bottomPanel = new JPanel();
		tfHost = new JTextField("127.0.0.1", 20);
		tfText = new JTextField(20);
		btnConnect = new JButton("Connect");
		btnSend = new JButton("보내기");
		taChatList = new JTextArea(10, 30);
		scrollpane = new ScrollPane();
		
		dto = new RequestDto();
	}

	@Override
	public void setting() {
		setTitle("MyChat1.0" + "[" + id + "]");
		setSize(350, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		topPanel.setBackground(Color.LIGHT_GRAY);
		bottomPanel.setBackground(Color.LIGHT_GRAY);
		taChatList.setBackground(Color.ORANGE);
		taChatList.setForeground(Color.BLUE);
		taChatList.setEditable(false);
	}

	@Override
	public void batch() {
		topPanel.add(tfHost);
		topPanel.add(btnConnect);
		scrollpane.add(taChatList);
		bottomPanel.add(tfText);
		bottomPanel.add(btnSend);
		
		add(topPanel, BorderLayout.NORTH);
		add(scrollpane, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
	}

	@Override
	public void listener() {
		btnConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connect();
			}
		});
		
		btnSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				send();
			}
		});
		
		tfText.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				send();
			}
		});
	}
	
	class ReaderThread extends Thread {
		
		@Override
		public void run() {
			String text = null;
			try {
				while ((text = reader.readLine()) != null) {
					taChatList.append(text + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private void connect() {
		String host = tfHost.getText();
		try {
			socket = new Socket(host, Protocol.PORT);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream(), true);
			ReaderThread rt = new ReaderThread();
			rt.start();
			taChatList.append("[공지] " + id + "님, 채팅방에 입장하신 것을 환영합니다.\n");
			taChatList.append("[공지] 귓속말은 'TO:대상아이디:메시지'로 가능합니다.\n");
		} catch (Exception e1) {
			taChatList.append("[공지] 서버가 종료되었습니다.\n");
			System.out.println("연결 실패");
		}
	}
	
	private void send() {
		dto.setId(id);
		String text = tfText.getText();
		Gson gson = new Gson();
		String[] gubun = text.split(":");
		if (text.equals("")) {
			taChatList.append("[공지] 보낼 내용을 입력하세요.\n");
			return;
		}
		if (gubun[0].equalsIgnoreCase(Protocol.TO)) {
			dto.setTo(gubun[1]);
			dto.setText(text.substring(text.indexOf(":")));
		} else {
			dto.setText(text);
		}
		
		String dtoJson = gson.toJson(dto, RequestDto.class);
		writer.println(dtoJson);
		tfText.setText("");
	}
}
