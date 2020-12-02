package chat;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ClientLogin extends JFrame implements AppFrame {
	
	private static final String TAG = "ClientLogin : ";
	private String id;
	private JTextField tfId;
	private JButton btnLogin;
	private JLabel laNotice;
	
	public ClientLogin() {
		init();
		
		setting();
		
		batch();
	
		listener();
		
		setVisible(true);
	}

	@Override
	public void init() {
		tfId = new JTextField(10);
		btnLogin = new JButton("로그인");
		laNotice = new JLabel("10자 이내의 아이디만 가능합니다.");
	}

	@Override
	public void setting() {
		setTitle("채팅을 위한 ID설정");
		setSize(300, 300);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	@Override
	public void batch() {
		add(tfId, BorderLayout.NORTH);
		add(laNotice, BorderLayout.CENTER);
		add(btnLogin, BorderLayout.SOUTH);
	}

	@Override
	public void listener() {
		// 로그인 버튼 통한 로그인
		btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
		// 엔터키 통한 로그인
		tfId.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
	}
	
	private void login() {
		
		id = tfId.getText();
		// 아이디 길이가 10자를 넘을 경우
		if (id.length() > 10) {
			laNotice.setText("10자 이내로 입력하세요 !");
			return;
		}
		// 아이디 미입력시
		if (id.equalsIgnoreCase("")) {
			laNotice.setText("아이디를 입력하세요 !");
			return;
		}
		// 로그인 성공 시 채팅창 오픈
		System.out.println("로그인 성공");
		setVisible(false);
		new ChatClient(id);
	}
	
	public static void main(String[] args) {
		new ClientLogin();
	}
}
