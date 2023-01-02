package RentalService;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class user_Return extends JFrame {

	private JPanel contentPane;
	// panel1
	private JLabel searchTitle;
	private JLabel searchPhoneLabel;
	private JTextField searchPhoneField;
	private JButton searchBtn;
	// panel2
	private JLabel userTitle;
	private JLabel nameLabel;
	private JTextPane nameField;
	private JLabel phoneLabel;
	private JTextPane phoneField;
	private JLabel rentDayLabel;
	private JTextPane rentDayField;
	private JLabel returnDayLabel;
	private JTextPane returnDayField;
	private JLabel rentListLabel;
	private JTable rentList_table;
	private JLabel payLabel;
	private JTextPane payField;
	
	private JButton returnBtn;
	
	private JButton mainBtn;
	
	int returnIndex; // 전화 번호를 통해 검색한 사용자 인덱스 
	int sum = 0; // 총 지불해야할 최종 금액
	
	public user_Return(Manager act) {
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 550, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// 대여한 상품 테이블 모델 선언
		String[] columns_return = {
				"상품명", "상품 코드", "상품 가격"		
		};
		String [][] data_return = {
				
		};
		
		DefaultTableModel model = new DefaultTableModel(data_return, columns_return) {
			public boolean isCellEditable(int row, int column) {
				return false; // 셀 내용 수정 불가
			}
		};
		
		//--------------------------------------------------------------------
		try {		
			// 반납 하는 패널
			JPanel panel2 = new JPanel();
			panel2.setBounds(0, 0, 550, 400);
			panel2.setLayout(null);
			panel2.setVisible(false);
			
			// 사용자 정보 타이틀 라벨
			userTitle = new JLabel("사용자 정보");
			userTitle.setBounds(160, 5, 214, 54);
			userTitle.setHorizontalAlignment(SwingConstants.CENTER);
			userTitle.setFont(new Font("맑은 고딕", Font.BOLD, 30));
			panel2.add(userTitle);
			
			// 이름 라벨
			nameLabel = new JLabel("이름");
			nameLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
			nameLabel.setBounds(30, 73, 75, 21);
			panel2.add(nameLabel);
			
			// 이름 필드
			nameField = new JTextPane();
			nameField.setEditable(false);
			nameField.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
			nameField.setBounds(108, 73, 152, 21);
			panel2.add(nameField);
			
			// 대여 일자 라벨
			rentDayLabel = new JLabel("대여 일자");
			rentDayLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
			rentDayLabel.setBounds(30, 104, 75, 21);
			panel2.add(rentDayLabel);
			
			// 대여 일자 필드
			rentDayField = new JTextPane();
			rentDayField.setEditable(false);
			rentDayField.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
			rentDayField.setBounds(108, 104, 152, 21);
			panel2.add(rentDayField);
			
			// 전화 번호 라벨
			phoneLabel = new JLabel("전화번호");
			phoneLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
			phoneLabel.setBounds(272, 73, 75, 21);
			panel2.add(phoneLabel);
			
			// 전화 번호 필드
			phoneField = new JTextPane();
			phoneField.setEditable(false);
			phoneField.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
			phoneField.setBounds(360, 73, 152, 21);
			panel2.add(phoneField);
			
			// 반납 예정일 라벨
			returnDayLabel = new JLabel("반납 예정일");
			returnDayLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
			returnDayLabel.setBounds(272, 104, 82, 21);
			panel2.add(returnDayLabel);
			
			// 반납 예정일 필드
			returnDayField = new JTextPane();
			returnDayField.setEditable(false);
			returnDayField.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
			returnDayField.setBounds(360, 104, 152, 21);
			panel2.add(returnDayField);	
			
			// 사용 금액 라벨 (연체료 제외)
			payLabel = new JLabel("사용금액");
			payLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
			payLabel.setBounds(272, 135, 75, 21);
			panel2.add(payLabel);
			
			// 사용 금액 필드
			payField = new JTextPane();
			payField.setEditable(false);
			payField.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
			payField.setBounds(360, 135, 152, 21);
			panel2.add(payField);
			
			// 대여 상품 테이블 라벨
			rentListLabel = new JLabel("대여 물품 목록");
			rentListLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
			rentListLabel.setBounds(30, 179, 115, 21);
			panel2.add(rentListLabel);
			
			// 대여 상품 리스트 테이블
			rentList_table = new JTable(model);
			rentList_table.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
			rentList_table.getTableHeader().setReorderingAllowed(false); // 열 이동 불가
			rentList_table.getTableHeader().setResizingAllowed(false); // 열 크기 조절불가
			rentList_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
			JScrollPane rentList = new JScrollPane(rentList_table);
			rentList.setBounds(30, 205, 482, 118);
			panel2.add(rentList);
	
			// 반납 버튼
			returnBtn = new JButton("반납하기");
			returnBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
			returnBtn.setBounds(150, 333, 240, 23);
			returnBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					// 프로그램 실행시 첫번째 실행 여부 물어보고 상황에 맞는 manager객체를 생성한다.
					try {
						String returnMessage = ""; // 반납시 띄우는 메시지
						int pay = act.userAt(returnIndex).pay();
						if (act.userAt(returnIndex).returnDay() < 0) { // 연체되었다면
							returnMessage = Math.abs(act.userAt(returnIndex).returnDay())
									+ "일 연체되어\n" + "지불하실 금액은 " + pay + "원 입니다.\n"
									+ "반납하시겠습니까?";
						}
						else {// 연체되지 않았다면
							returnMessage = "지불하실 금액은 " + pay + "원 입니다.\n" + "반납하시겠습니까?";
						}
						
						// 반납 확인 창 띄우기
						int answer = JOptionPane.showConfirmDialog(null, 
								returnMessage, "Return Service", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
						
						if (answer == JOptionPane.YES_OPTION) { // 반납하길 원하는경우
							try { 
								act.checkOut(returnIndex); // 매니저 객체를 통해 체크아웃
								dispose(); //반납 후 메인프레임으로 돌아가기

							} catch (Exception e1) { 
								JOptionPane.showMessageDialog(null, e1.getStackTrace(), "Save error", JOptionPane.WARNING_MESSAGE); 
							}
						}
						else { // 반납하길 원하지 않는 경우
							try {
								dispose(); // 반납 프레임 닫고 메인프레임으로 돌아가기

							} catch (Exception e1) { 
								JOptionPane.showMessageDialog(null, e1.getStackTrace(), "Save error", JOptionPane.WARNING_MESSAGE); 
							}
						}
					}
						
					catch (Exception e1) {
						JOptionPane.showMessageDialog(null, "잘못된 방법입니다.", "Error", JOptionPane.WARNING_MESSAGE); 
						}
				
					
				}
			});
			panel2.add(returnBtn);
		
			
			mainBtn = new JButton("메인");
			mainBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
			mainBtn.setBounds(461, 5, 66, 23);
			mainBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {				
					dispose();
				}
	
			});
			panel2.add(mainBtn);
			
			// contentPane에 패널2 추가
			contentPane.add(panel2);
			
			
			
			// 전화번호로 사용자 조회
			//---------------------------------------------
			
			JPanel panel1 = new JPanel();
					
			panel1.setBounds(0, 0, 546, 374);
			panel1.setLayout(null);
			panel1.setVisible(true);
			
			mainBtn = new JButton("메인");
			mainBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
			mainBtn.setBounds(461, 5, 66, 23);
			mainBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {				
					dispose();
				}
	
			});
			panel1.add(mainBtn);
			
			// 사용자 조회 타이틀 라벨
			searchTitle = new JLabel("사용자 조회");
			searchTitle.setBounds(149, 112, 230, 54);
			searchTitle.setHorizontalAlignment(SwingConstants.CENTER);
			searchTitle.setFont(new Font("맑은 고딕", Font.BOLD, 40));
			panel1.add(searchTitle);
				
			// 전화번호 입력 라벨
			searchPhoneLabel = new JLabel("전화번호");
			searchPhoneLabel.setBounds(107, 199, 84, 21);
			searchPhoneLabel.setHorizontalAlignment(SwingConstants.CENTER);
			searchPhoneLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
			panel1.add(searchPhoneLabel);
				
			// 전화번호 입력 필드
			searchPhoneField = new JTextField();
			searchPhoneField.setBounds(193, 197, 152, 25);
			searchPhoneField.setHorizontalAlignment(SwingConstants.LEFT);
			searchPhoneField.setColumns(10);
			panel1.add(searchPhoneField);
			
			// 조회 버튼
			searchBtn = new JButton("조회");
			searchBtn.setBounds(350, 196, 69, 27);
			searchBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					// 전화 번호 검색 버튼 클릭 후 사용자 정보 출력 패널로 넘어가며 버튼이 존재하는 패널은 보이지 않게됨
					panel1.setVisible(false);
					panel2.setVisible(true);

					// 사용자가 대여한 상품을 테이블에 추가
					try {
						// 전화 번호 입력 후 조회버튼 클릭시 해당 전화번호를 가진 사용자를 찾아 인덱스 리턴
						returnIndex = act.searchUser(searchPhoneField.getText());
						// 사용자가 대여한 상품의 개수만큼
						for(int i = 0; i < act.userAt(returnIndex).getRentalCount(); i++)
						{
							String rentCode = act.userAt(returnIndex).codeAt(i); // 대여한 상품의 코드를 저장
							int rentCodeIndex = act.searchCode(rentCode); // 해당 상품의 인덱스 반환
							
							String arr[] = new String[3];
							// 상품의 정보 테이블에 추가
							arr[0] = act.productAt(rentCodeIndex).getName();
							arr[1] = act.productAt(rentCodeIndex).getCode();
							arr[2] = Integer.toString(act.productAt(rentCodeIndex).getPrice());
							
							// table에 addRow를 통해 행추가
							model.addRow(arr);
						}
						
						// 사용 금액 계산 (연체료 제외)
						for(int k = 0; k < act.userAt(returnIndex).getRentalCount(); k++)
						{
							sum += act.userAt(returnIndex).payAt(k);
						}
						
						// 전화번호 입력받고 리턴한 해당 사용자의 인덱스로 사용자 정보 출력
						nameField.setText(act.userAt(returnIndex).getName());
						rentDayField.setText(act.userAt(returnIndex).getRentalDay());
						phoneField.setText(act.userAt(returnIndex).getPhone());
						returnDayField.setText(act.userAt(returnIndex).getReturnDay());
						payField.setText(String.valueOf(sum));
						
					} catch (Exception e1) {
						panel1.setVisible(true);
						panel2.setVisible(false);
						JOptionPane.showMessageDialog(null, "일치하는 회원정보가 존재하지 않습니다.\n전화번호를 다시 입력해주세요.", "Return error", JOptionPane.WARNING_MESSAGE); 
						searchPhoneField.setText(null);
					}
				}
			});
			searchBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
			panel1.add(searchBtn);
			
			contentPane.add(panel1);
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getStackTrace(), "Error", JOptionPane.WARNING_MESSAGE); 
		}
		
	}
}
