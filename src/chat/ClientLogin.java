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
		btnLogin = new JButton("�α���");
		laNotice = new JLabel("10�� �̳��� ���̵� �����մϴ�.");
	}

	@Override
	public void setting() {
		setTitle("ä���� ���� ID����");
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
		// �α��� ��ư ���� �α���
		btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
		// ����Ű ���� �α���
		tfId.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
	}
	
	private void login() {
		
		id = tfId.getText();
		// ���̵� ���̰� 10�ڸ� ���� ���
		if (id.length() > 10) {
			laNotice.setText("10�� �̳��� �Է��ϼ��� !");
			return;
		}
		// ���̵� ���Է½�
		if (id.equalsIgnoreCase("")) {
			laNotice.setText("���̵� �Է��ϼ��� !");
			return;
		}
		// �α��� ���� �� ä��â ����
		System.out.println("�α��� ����");
		setVisible(false);
		new ChatClient(id);
	}
	
	public static void main(String[] args) {
		new ClientLogin();
	}
}
