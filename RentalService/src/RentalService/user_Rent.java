package RentalService;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class user_Rent extends JFrame {
	
	// user_Rent 
	private JPanel userRentPanel;
	private JLabel productListLabel_user;
	private JTextField searchCodeField_user;
	private JButton searchCodeBtn_user;
	private JButton chooseBtn;
	private JButton mainBtn_user;
	private JTable productList_table_user;
	
	private JLabel nameLabel_rent;
	private JTextField nameField_rent;
	private JLabel phoneLabel_rent;
	private JTextField phoneField_rent;
	private JLabel rentDayLabel_rent;
	private JTextField rentDayField_rent;
	private JLabel returnDayLabel_rent;
	private JTextField returnDayField_rent;
	private JLabel rentProductLabel_rent;
	private JTextPane rentProductField_rent;
	private JButton rentBtn_user;
	
	Boolean searchFlag = false; // 검색 여부 확인 변수
	ArrayList arr = new ArrayList(); // 대여원하는 상품 배열 초기화

	
	public user_Rent(Manager act) {
		

		// user_Rent panel 생성
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 550, 400);
		userRentPanel = new JPanel();
		userRentPanel.setBounds(0, 0, 546, 374);
		userRentPanel.setLayout(null);
		setContentPane(userRentPanel);
				
		productListLabel_user = new JLabel("상퓸 목록");
		productListLabel_user.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		productListLabel_user.setBounds(12, 13, 66, 21);
		add(productListLabel_user);
		
		// 메인 버튼
		mainBtn_user = new JButton("메인");
		mainBtn_user.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		mainBtn_user.setBounds(458, 12, 66, 23);
		mainBtn_user.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		
				dispose(); // 현재 사용자 대여 프레임 닫기
			}
		});
		add(mainBtn_user);
				
		
		// 상품 목록 리스트 (재고상품만 출력 - 상품개수가 0개이면 출력 X)
		String[] columns_user = {
				"상품명", "상품 코드", "상품 개수", "상품 가격"
		};
		String [][] data_user = {
				
		};
		
		DefaultTableModel model = new DefaultTableModel(data_user, columns_user) {
			public boolean isCellEditable(int row, int column) {
				return false; // 셀 내용 수정 불가
			}
		};
		productList_table_user = new JTable(model);
		productList_table_user.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		productList_table_user.getTableHeader().setReorderingAllowed(false); // 열 이동 불가
		productList_table_user.getTableHeader().setResizingAllowed(false); // 열 크기 조절불가
		productList_table_user.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	
		JScrollPane productList_user = new JScrollPane(productList_table_user);
		productList_user.setBounds(12, 44, 512, 200);
		add(productList_user);
		
		try {
			// 배열에 상품이 존재하지 않는다면
			if (act.productAt(0) != null) {
				// productCount만큼 테이블에 행 추가
				for(int i = 0; i < act.getProductCount(); i++)
				{
					// 유저화면에는 재고상품만 출력해야하므로 상품 개수가 0이면 행을 추가하지 않는다.
					if (act.productAt(i).getNumber() != 0) { 
						// productList_table에 넣기 위해 arr에 textField의 값들을 넣어줌
						String arr[] = new String[4];
						arr[0] = act.productAt(i).getName();
						arr[1] = act.productAt(i).getCode();
						arr[2] = Integer.toString(act.productAt(i).getNumber());
						arr[3] = Integer.toString(act.productAt(i).getPrice());
						
						// table에 addRow를 통해 행추가
						model.addRow(arr);
					}
					
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getStackTrace() , "AddProduct error", JOptionPane.WARNING_MESSAGE); 
		}
		
		// 상품 코드로 검색하는 기능 필드
		searchCodeField_user = new JTextField();
		searchCodeField_user.setHorizontalAlignment(SwingConstants.LEFT);
		searchCodeField_user.setBounds(90, 13, 105, 21);
		searchCodeField_user.setColumns(10);
		add(searchCodeField_user);
		 
		// 상품 코드통해 검색하는 버튼
		searchCodeBtn_user = new JButton("검색");
		searchCodeBtn_user.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		searchCodeBtn_user.setBounds(197, 12, 66, 23);
		
		TableRowSorter<TableModel> trs = new TableRowSorter<>(model); // TableRowSorter trs 선언
		// 검색필드에 입력하는 내용이 들어있는 행만 보여줌
		searchCodeBtn_user.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				String name = searchCodeField_user.getText();
				productList_table_user.setRowSorter(trs);
				trs.setRowFilter(RowFilter.regexFilter(name));
				searchFlag = true;
				
				searchCodeField_user.setText(null);
			}
		});
		add(searchCodeBtn_user);
			
		// 대여상품 선택시 누르는 버튼
		chooseBtn = new JButton("선택");
		chooseBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		chooseBtn.setBounds(388, 12, 66, 23);
		chooseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				trs.setRowFilter(RowFilter.regexFilter("")); // 재고 삭제 버튼 누르면 아무것도 검색 안했을 때의 전체 테이블 보여주기

				try {
					int row = Integer.valueOf(productList_table_user.getSelectedRow());
					if (row == -1) 
						return;
					
					String selected = ((String) model.getValueAt(row, 1)); // 선택한 행의 코드 저장
					
					if (searchFlag == true) { // 검색했다면 
						for (int i = 0; i < model.getRowCount(); i++) { // 전체 상품 리스트의 모든 행을 돌며
							if (selected.equals(String.valueOf(model.getValueAt(i, 1)))) { // 검색한 상품과 같은 코드를 가진 상품 찾기
								
								int stockCount = Integer.valueOf((String) model.getValueAt(i, 2)); // 찾은 상품의 개수 저장
								// 매니저에서 불러오면 체크인 전이기 때문에 선택하여도 재고가 줄어들지 않으므로 테이블에서만 수정해준다.
								if (stockCount >= 1 && arr.size() < 3) { // 선택한 상품 행의 재고가 1개 이상이고 선택한 상품 개수가 3개 이하인 경우
									
									model.setValueAt(Integer.toString(stockCount - 1), row, 2); // 상품의 개수 -1 (테이블에서만)
									
									
									arr.add((String) model.getValueAt(row, 1)); // 선택한 행의 상품 코드를 배열에 넣기
									
									String code = ""; // 선택한 상품을 저장할 스트링 변수
									// 대여를 원하는 상품의행을 선택후  선택 버튼을 누르면 대여 상품 textPane에 추가되어 자신이 선택한 상품의 이름이 출력된다.
									for (int j = 0; j < arr.size(); j++) {
										code += (String)arr.get(j); // 선택한 상품 arrayList의 내용을 스트링에 넣는다.
										if (j < (arr.size() - 1))	
											code += ", ";
									}
									rentProductField_rent.setText(code); // 1~3개의 선택한 물품 정보로 대여상품 텍스트를 수정한다.
									
								} else if (stockCount == 0){ // 테이블에서 선택한 행의 재고가 0인 경우
									JOptionPane.showMessageDialog(null, "선택하신 상품의 재고가 남아있지 않아 더이상 대여가 불가능합니다.", "Rent error", JOptionPane.WARNING_MESSAGE); 
								} else if (arr.size() == 3) { // 상품을 3개 모두 선택한 경우
									JOptionPane.showMessageDialog(null, "상품은 3개까지만 대여 가능합니다.", "Rent error", JOptionPane.WARNING_MESSAGE); 
								}
							}
						}
					} else {
						int stockCount = Integer.valueOf((String) model.getValueAt(row, 2)); // 찾은 상품의 개수 저장
						// 매니저에서 불러오면 체크인 전이기 때문에 선택하여도 재고가 줄어들지 않으므로 테이블에서만 수정해준다.
						if (stockCount >= 1 && arr.size() < 3) { // 선택한 상품 행의 재고가 1개 이상이고 선택한 상품 개수가 3개 이하인 경우
							
							model.setValueAt(Integer.toString(stockCount - 1), row, 2); // 상품의 개수 -1 (테이블에서만)
		
							arr.add((String) model.getValueAt(row, 1)); // 선택한 행의 상품 코드를 배열에 넣기
							
							String code = ""; // 선택한 상품을 저장할 스트링 변수
							// 대여를 원하는 상품의행을 선택후  선택 버튼을 누르면 대여 상품 textPane에 추가되어 자신이 선택한 상품의 이름이 출력된다.
							for (int j = 0; j < arr.size(); j++) {
								code += (String)arr.get(j); // 선택한 상품 arrayList의 내용을 스트링에 넣는다.
								if (j < (arr.size() - 1))	
									code += ", ";
							}
							rentProductField_rent.setText(code); // 1~3개의 선택한 물품 정보로 대여상품 텍스트를 수정한다.

							
						} else if (stockCount == 0){ // 테이블에서 선택한 행의 재고가 0인 경우
							JOptionPane.showMessageDialog(null, "선택하신 상품의 재고가 남아있지 않아 더이상 대여가 불가능합니다.", "Rent error", JOptionPane.WARNING_MESSAGE); 
						} else if (arr.size() == 3) { // 상품을 3개 모두 선택한 경우
							JOptionPane.showMessageDialog(null, "상품은 3개까지만 대여 가능합니다.", "Rent error", JOptionPane.WARNING_MESSAGE); 
						}
					}
					
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, e2.getStackTrace() , "AddProduct error", JOptionPane.WARNING_MESSAGE); 
				}
				
			
			}
		});
		add(chooseBtn);
				
		// 사용자 이름 라벨
		nameLabel_rent = new JLabel("이름");
		nameLabel_rent.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		nameLabel_rent.setBounds(22, 254, 66, 21);
		add(nameLabel_rent);
		
		// 사용자 이름 필드
		nameField_rent = new JTextField();
		nameField_rent.setHorizontalAlignment(SwingConstants.LEFT);
		nameField_rent.setColumns(10);
		nameField_rent.setBounds(100, 257, 152, 21);
		add(nameField_rent);
		
		// 전화번호 라벨
		phoneLabel_rent = new JLabel("전화번호");
		phoneLabel_rent.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		phoneLabel_rent.setBounds(273, 254, 66, 21);
		add(phoneLabel_rent);
		
		// 전화번호 필드
		phoneField_rent = new JTextField();
		phoneField_rent.setHorizontalAlignment(SwingConstants.LEFT);
		phoneField_rent.setColumns(10);
		phoneField_rent.setBounds(361, 257, 152, 21);
		phoneField_rent.setText("하이픈 포함 입력");
		add(phoneField_rent);
		
		// 대여 일자 라벨
		rentDayLabel_rent = new JLabel("대여 일자");
		rentDayLabel_rent.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		rentDayLabel_rent.setBounds(22, 288, 66, 21);
		add(rentDayLabel_rent);
		
		// 대여 일자 필드 
		rentDayField_rent = new JTextField();
		rentDayField_rent.setHorizontalAlignment(SwingConstants.LEFT);
		rentDayField_rent.setColumns(10);
		rentDayField_rent.setBounds(100, 291, 152, 21);
		rentDayField_rent.setText("YYYY-MM-DD");
		add(rentDayField_rent);
		
		// 반납 예정일자 라벨
		returnDayLabel_rent = new JLabel("반납 예정일");
		returnDayLabel_rent.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		returnDayLabel_rent.setBounds(273, 288, 100, 21);
		add(returnDayLabel_rent);
		
		// 반납 예정일자 필드
		returnDayField_rent = new JTextField();
		returnDayField_rent.setHorizontalAlignment(SwingConstants.LEFT);
		returnDayField_rent.setColumns(10);
		returnDayField_rent.setBounds(361, 288, 152, 21);
		returnDayField_rent.setText("YYYY-MM-DD");
		add(returnDayField_rent);
		
		// 대여 물품 라벨
		rentProductLabel_rent = new JLabel("대여 상품");
		rentProductLabel_rent.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		rentProductLabel_rent.setBounds(22, 322, 66, 21);
		add(rentProductLabel_rent);
		 
		// 대여 물품 textPane - 사용자 입력 불가능
		rentProductField_rent = new JTextPane();
		rentProductField_rent.setEditable(false);
		rentProductField_rent.setBounds(100, 325, 152, 21);
		add(rentProductField_rent);
		
		// 유저화면 대여 버튼
		rentBtn_user = new JButton("대여하기");
		rentBtn_user.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		rentBtn_user.setBounds(273, 324, 240, 23);
		rentBtn_user.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				trs.setRowFilter(RowFilter.regexFilter("")); // 재고 삭제 버튼 누르면 아무것도 검색 안했을 때의 전체 테이블 보여주기

				try {
					// textField에서 입력받은 값을 변수에 저장
					String addName = nameField_rent.getText();
					String addPhone = phoneField_rent.getText();
					String addRentDay = rentDayField_rent.getText();
					String addReturnDay = returnDayField_rent.getText();

					// 유저 객체 생성
					User u = new User(addName, addPhone, addRentDay, addReturnDay);
					
					for (int i = 0; i < arr.size(); i++)
					{
						if (arr.get(i) != null) {
							String rentalCode = (String)arr.get(i); // 선택한 상품 코드 저장
							int index = act.searchCode(rentalCode); // 코드를 통해 상품의 인덱스 찾기
							int price = act.productAt(index).getPrice(); // 해당 코드 상품의 가격 저장
							u.addProductToUser(rentalCode, price); // 유저객체의 대여상품/가격 배열에 정보 추가
						}
					}
					
					act.checkIn(u); // 체크인하기
					arr = new ArrayList(); // 대여 후 arrayList 초기화

					
					// 상품 대여 후 textField 초기화하여 비워준다.
					nameField_rent.setText(null);
					phoneField_rent.setText(null);
					rentDayField_rent.setText(null);
					returnDayField_rent.setText(null);
					rentProductField_rent.setText(null);

				} 
				catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(null, "정보를 다시 입력해주세요.", "AddProduct error", JOptionPane.WARNING_MESSAGE); 
				}
				catch (Exception e1) { // 예외 발생하면 원인 메시지 알림창 띄우기
					
					JOptionPane.showMessageDialog(null, e1.getStackTrace() , "AddProduct error", JOptionPane.WARNING_MESSAGE); 

				}
			}
		});
		add(rentBtn_user);
	}

}
