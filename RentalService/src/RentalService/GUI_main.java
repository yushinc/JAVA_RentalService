package RentalService;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class GUI_main extends JFrame {
	
	// contentPane
	private JPanel contentPane;
	
	// GUI_main
	private JPanel mainPanel;
	private JLabel titleLabel;
	private JButton rentBtn_main;
	private JButton returnBtn_main;
	private JButton manageBtn;
	private JButton exitBtn;
	
	// 객체 초기화
	Manager act = null;
	Calendar getToday = Calendar.getInstance();
	ObjectInputStream ois = null;

	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI_main frame1 = new GUI_main();
					frame1.setVisible(true);
					frame1.setResizable(false);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUI_main() {

		// contentPane
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 550, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		//--------------------------------------------------------
		// GUI_main panel 생성                  
		mainPanel = new JPanel();
		mainPanel.setBounds(0, 0, 546, 374);
		mainPanel.setLayout(null);

		// Rental Service 메인 텍스트
		titleLabel = new JLabel("Rental Service");
		titleLabel.setBounds(85, 89, 363, 48);
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 50));
		mainPanel.add(titleLabel);

		// 메인화면 대여 버튼
		rentBtn_main = new JButton("대여");
		rentBtn_main.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		rentBtn_main.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 사용자 대여 프레임 생성 후 창 띄우기
				user_Rent frame = new user_Rent(act); 
				frame.setVisible(true);
			}
		});
		rentBtn_main.setBounds(120, 200, 120, 80);
		mainPanel.add(rentBtn_main);

		// 반납 버튼
		returnBtn_main = new JButton("반납");
		returnBtn_main.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		returnBtn_main.setBounds(277, 200, 120, 80);
		returnBtn_main.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				user_Return frame = new user_Return(act);
				frame.setVisible(true);
			}
		});
		mainPanel.add(returnBtn_main);

		// 관리자 모드 이동 버튼
		manageBtn = new JButton("관리자");
		manageBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		manageBtn.setBounds(451, 330, 73, 23);
		// 메인에서 관리자 버튼을 누르면 메인화면 패널은 보이지않게되고 관리자패널이 보인다.
		manageBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GUI_Manager frame = new GUI_Manager(act);
				frame.setVisible(true);
			}
		});
		mainPanel.add(manageBtn);

		// 종료버튼 클릭시 종료
		exitBtn = new JButton("종료");
		exitBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		exitBtn.setBounds(12, 332, 73, 23);
		exitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0); // 종료
			}	
		});
		mainPanel.add(exitBtn);


		// 프로그램 실행시 첫번째 실행 여부 물어보고 상황에 맞는 manager객체를 생성한다.
		try {
			int answer = JOptionPane.showConfirmDialog(null, "프로그램을 사용해본적이 있습니까?", "Rental Service", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			
			if (answer == JOptionPane.YES_OPTION) { // 첫번째 실행이 아닌경우
				try { // 파일이 존재하는 경우
					ois = new ObjectInputStream(new FileInputStream("SaveData.txt")); 
					act = new Manager(ois); // file read
					ois.close();

				} catch (FileNotFoundException fnfe) { // 파일이 존재하지 않는 경우
					JOptionPane.showMessageDialog(null, "파일이 현 디렉토리에 존재하지 않아 불러올 수 없습니다.", "Error", JOptionPane.WARNING_MESSAGE); 
					act = new Manager();
					// 출력 후 초기상태의 프로그램 실행
				}
			}
			
			else { // 첫번째 실행인경우
				try { // 파일이 존재하는 경우
					ois = new ObjectInputStream(new FileInputStream("SaveData.txt")); 
					act = new Manager(ois); // 파일의 기존데이터가 존재하므로 적용하여 실행 
					ois.close();

				} catch (FileNotFoundException fnfe) { // 파일이 존재하지 않는 경우 (첫번째 실행이므로 존재하지 않음)
					act = new Manager();
					// 초기상태의 프로그램 실행
				}
			}
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(null, ioe, "Error", JOptionPane.WARNING_MESSAGE); 

		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, "잘못된 방법입니다.", "Error", JOptionPane.WARNING_MESSAGE); 
		}
		
		contentPane.add(mainPanel);
		
	}

}
