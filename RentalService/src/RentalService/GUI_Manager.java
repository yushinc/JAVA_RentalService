package RentalService;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComponent;
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
import javax.swing.JTabbedPane;

public class GUI_Manager extends JFrame {
	
	private ProductManager productManager = null;
	private UserManager userManager = null;
	Calendar getToday = Calendar.getInstance();

	public GUI_Manager(Manager act) {// manager_GUI // manager panel 생성
	  
		JTabbedPane manager = new JTabbedPane();
		
		productManager = new ProductManager(act);
		userManager = new UserManager(act);
		
		manager.addTab("상품관리", productManager);
		manager.addTab("사용자 관리", userManager);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		setBounds(100, 100, 555, 430);
		setLayout(null);	
		setVisible(true);
		setContentPane(manager);
	}
	
	
	class ProductManager extends JPanel {
	
		private JLabel productListLabel_manager;
		private JTextField searchCodeField_manager;
		private JButton searchCodeBtn;
		private JButton returnBtn_manager;
		private JButton mainBtn_manager;
		private JButton saveBtn_manager;
		private JTable productList_table_manager;
		private JLabel nameLabel_manager;
		private JTextField nameField_manager;
		private JLabel productCountLabel;
		private JTextField productCountField;
		private JLabel codeLabel_manager;
		private JTextField codeField_manager;
		private JLabel productPriceLabel;
		private JTextField productPriceField;
		
		private JLabel editCountLabel;
		private JTextField editCountField;
		private JButton editAddBtn;
		private JButton editDeleteBtn;
		private JButton productAddBtn;
		private JButton productDeleteBtn;
		
		Boolean searchFlag = false;

		public ProductManager(Manager act) { // 상품 관리자 패널 생성자
			
			setBounds(0, 0, 555, 430);
			setLayout(null);
			setVisible(false);
				
		
			
			// 상품 목록 라벨
			productListLabel_manager = new JLabel("상품 목록");
			productListLabel_manager.setFont(new Font("맑은 고딕", Font.BOLD, 15));
			productListLabel_manager.setBounds(12, 13, 66, 21);
			add(productListLabel_manager);
			
				
			// 상품 목록 테이블 (개수가 0개인 상품 포함하여 전체 상품 출력)
			String[] columns_manager = {
					"상품명", "상품 코드", "상품 개수", "상품 가격"
			};
			String [][] data_manager = {
					
			};
			
			DefaultTableModel model = new DefaultTableModel(data_manager, columns_manager) {
				public boolean isCellEditable(int row, int column) {
					return false; // 셀 내용 수정 불가
				}
			};
			productList_table_manager = new JTable(model);
			productList_table_manager.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
			productList_table_manager.getTableHeader().setReorderingAllowed(false); // 열 이동 불가
			productList_table_manager.getTableHeader().setResizingAllowed(false); // 열 크기 조절불가
			productList_table_manager.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
			JScrollPane productList_manager = new JScrollPane(productList_table_manager);
			productList_manager.setBounds(12, 44, 512, 200);
			add(productList_manager);
			
			try {
				// 배열에 상품이 존재한다면
				if (act.productAt(0) != null) {
					// productCount만큼 테이블에 행 추가
					for(int i = 0; i < act.getProductCount(); i++)
					{
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
			} catch (Exception e) {
				
			}

			
			// 상품 검색하는 기능 필드 
			searchCodeField_manager = new JTextField();
			searchCodeField_manager.setHorizontalAlignment(SwingConstants.LEFT);
			searchCodeField_manager.setBounds(90, 13, 105, 21);
			searchCodeField_manager.setColumns(10);
			add(searchCodeField_manager);
			
			// 상품 검색 버튼
			searchCodeBtn = new JButton("검색");
			searchCodeBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
			searchCodeBtn.setBounds(197, 12, 66, 23);
			
			TableRowSorter<TableModel> trs = new TableRowSorter<>(model); // TableRowSorter trs 선언
			// 검색필드에 입력하는 내용이 들어있는 행만 보여줌
			searchCodeBtn.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					String name = searchCodeField_manager.getText();
					productList_table_manager.setRowSorter(trs);
					trs.setRowFilter(RowFilter.regexFilter(name));
					searchFlag = true;
					
					searchCodeField_manager.setText(null);
				}
			});
			add(searchCodeBtn);
			
			// 메인 버튼
			mainBtn_manager = new JButton("메인");
			mainBtn_manager.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
			mainBtn_manager.setBounds(458, 12, 66, 23);
			mainBtn_manager.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
	
			});
			add(mainBtn_manager);
			
			
			
			// 데이터 저장 버튼
			saveBtn_manager = new JButton("저장");
			saveBtn_manager.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
			saveBtn_manager.setBounds(385, 12, 66, 23);
			
			saveBtn_manager.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ObjectOutputStream out = null;
					try {
						out = new ObjectOutputStream(new FileOutputStream("SaveData.txt")); 
	
						// 파일 오픈 (요청할 때만 파일 오픈), 가장 상단에 항상 오픈해두면 다수 이용시 파일 오픈 횟수에 제한이 걸릴 수도 있음
						act.saveFile(out); // manager의 파일 저장 함수 호출 (act는 manager 객체)
					}
						
					catch (IOException ioe) { // 입출력 예외 처리
						JOptionPane.showMessageDialog(null, "파일로 출력할 수 없습니다.", "Save error", JOptionPane.WARNING_MESSAGE); 
						
					}
					catch (Exception e1) {
						JOptionPane.showMessageDialog(null, "파일로 출력할 수 없습니다.", "Save error", JOptionPane.WARNING_MESSAGE); 
					}
					finally {
						try {
							out.close(); // 파일 객체 닫기
						}
						catch (Exception e1) {
						}
					}
				}
			});
			add(saveBtn_manager);
			
			// 상품 개수 증가/감소 시키기위해 개수 입력받는 라벨
			editCountLabel = new JLabel("재고");
			editCountLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
			editCountLabel.setBounds(22, 330, 30, 21);
			add(editCountLabel);
			
			// 상품 개수 증가/감소 시키기위해 개수 입력받는 필드
			editCountField = new JTextField();
			editCountField.setHorizontalAlignment(SwingConstants.LEFT);
			editCountField.setColumns(10);
			editCountField.setBounds(58, 330, 105, 21);
			add(editCountField);
			
			// 상품 개수 증가시키는 버튼
			editAddBtn = new JButton("추가");
			editAddBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 10));
			editAddBtn.setBounds(167, 329, 56, 23);
			
			editAddBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					trs.setRowFilter(RowFilter.regexFilter("")); // 재고 추가 버튼 누르면 아무것도 검색 안했을 때의 전체 테이블 보여주기

					try {
						int row = productList_table_manager.getSelectedRow();
						if (row == -1) 
							return;
						
						int editCount = Integer.parseInt(editCountField.getText()); // 재고 조정 개수 입력
						String search = (String) model.getValueAt(row, 0); // 검색한 상품의 이름 저장
						
						if (searchFlag == true) { // 검색했다면
							for (int i = 0; i < act.getProductCount(); i++) { // 전체 상품 리스트의 모든 행을 돌며
								if (search.equals((String) (model.getValueAt(i, 0)))) { // 검색한 상품과 같은 이름을 가진 상품 찾기
									act.orderStock(i, editCount); // 선택한 행의 상품 재고 추가
									Product p = act.productAt(i); 
									model.setValueAt(p.getNumber(), i, 2); // 선택한 행의 상품 개수셀을 변경된 재고로 수정
								}
							}
							editCountField.setText(null);
							searchFlag = false; // 삭제 후 검색 여부를 판단하는 변수 초기 상태 복원
						}
						else { // 검색한 적이 없다면
							act.orderStock(row, editCount); // 선택한 행의 상품 재고 추가
							Product p = act.productAt(row); 
							model.setValueAt(p.getNumber(), row, 2); // 선택한 행의 상품 개수셀을 변경된 재고로 수정
							editCountField.setText(null);
						}
					} catch (Exception e1) {
						
					}
				
				}
			});
			add(editAddBtn);
			
			
			// 상품 개수 감소시키는 버튼
			editDeleteBtn = new JButton("삭제");
			editDeleteBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 10));
			editDeleteBtn.setBounds(224, 329, 56, 23);
			editDeleteBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					trs.setRowFilter(RowFilter.regexFilter("")); // 재고 삭제 버튼 누르면 아무것도 검색 안했을 때의 전체 테이블 보여주기

					try {
						int row = productList_table_manager.getSelectedRow();
						if (row == -1) 
							return;
						
						int editCount = Integer.parseInt(editCountField.getText()); // 재고 조정 개수 입력
						String search = (String) model.getValueAt(row, 0); // 검색한 상품의 이름 저장
						
						if (searchFlag == true) { // 검색했다면 
							for (int i = 0; i < model.getRowCount(); i++) { // 전체 상품 리스트의 모든 행을 돌며
								if (search.equals(String.valueOf(model.getValueAt(i, 0)))) { // 검색한 상품과 같은 이름을 가진 상품 찾기
									act.deleteStock(i, editCount); // 선택한 행의 상품 재고 삭제
									Product p = act.productAt(i); 
									model.setValueAt(p.getNumber(), i, 2); // 선택한 행의 상품 개수셀을 변경된 재고로 수정
								}
							}
							editCountField.setText(null);
							searchFlag = false; // 삭제 후 검색 여부를 판단하는 변수 초기 상태 복원
						}
						else { // 검색한 적이 없다면
							act.deleteStock(row, editCount); // 선택한 행의 상품 재고 삭제
							Product p = act.productAt(row); 
							model.setValueAt(p.getNumber(), row, 2); // 선택한 행의 상품 개수셀을 변경된 재고로 수정
							editCountField.setText(null);
						}
					} catch (Exception e1) {
						
					}
				}
			});
			add(editDeleteBtn); 
			
			// 상품명 라벨
			nameLabel_manager = new JLabel("상품명");
			nameLabel_manager.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
			nameLabel_manager.setBounds(22, 254, 66, 21);
			add(nameLabel_manager);
			
			// 상품명 필드
			nameField_manager = new JTextField();
			nameField_manager.setHorizontalAlignment(SwingConstants.LEFT);
			nameField_manager.setColumns(10);
			nameField_manager.setBounds(100, 257, 152, 21);
			add(nameField_manager);
			
			// 상품 코드 라벨
			codeLabel_manager = new JLabel("상품 코드");
			codeLabel_manager.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
			codeLabel_manager.setBounds(273, 254, 66, 21);
			add(codeLabel_manager);
					
			// 상품 코드 필드
			codeField_manager = new JTextField();
			codeField_manager.setHorizontalAlignment(SwingConstants.LEFT);
			codeField_manager.setColumns(10);
			codeField_manager.setBounds(361, 257, 152, 21);
			add(codeField_manager);
					
			// 상품 개수 라벨
			productCountLabel = new JLabel("상품 개수");
			productCountLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
			productCountLabel.setBounds(22, 288, 66, 21);
			add(productCountLabel);
			
			// 상품개수 필드
			productCountField = new JTextField();
			productCountField.setHorizontalAlignment(SwingConstants.LEFT);
			productCountField.setColumns(10);
			productCountField.setBounds(100, 291, 152, 21);
			add(productCountField);
			
			// 상품 가격 라벨
			productPriceLabel = new JLabel("상품 가격");
			productPriceLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
			productPriceLabel.setBounds(273, 288, 66, 21);
			add(productPriceLabel);
			
			// 상품 가격 필드
			productPriceField = new JTextField();
			productPriceField.setHorizontalAlignment(SwingConstants.LEFT);
			productPriceField.setColumns(10);
			productPriceField.setBounds(361, 288, 152, 21);
			add(productPriceField);
			
			// 상품 등록 버튼
			productAddBtn = new JButton("등록");
			productAddBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
			productAddBtn.setBounds(292, 319, 110, 38);
			add(productAddBtn);
			
			// 등록 버튼 클릭시 필드에 입력한 정보대로 행 생성
			productAddBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						// textField에서 입력받은 값을 변수에 저장
						String addName = nameField_manager.getText();
						String addCode = codeField_manager.getText();
						int addNumber = Integer.parseInt(productCountField.getText());
						int addPrice = Integer.parseInt(productPriceField.getText());
						// product 객체 생성 후 상품 배열에 넣기
						Product p1;
						p1 = new Product(addName, addCode, addNumber, addPrice);
						act.add(p1);
						
						// productList_table에 넣기 위해 arr에 textField의 값들을 넣어줌
						String arr[] = new String[4];
						arr[0] = addName;
						arr[1] = addCode;
						arr[2] = Integer.toString(addNumber);
						arr[3] = Integer.toString(addPrice);
						
						
						// table에 addRow를 통해 행추가
						DefaultTableModel model = (DefaultTableModel) productList_table_manager.getModel();
						model.addRow(arr);
						
						// 상품 등록 후 textField 초기화하여 비워준다.
						nameField_manager.setText(null);
						codeField_manager.setText(null);
						productCountField.setText(null);
						productPriceField.setText(null);
					} 
					catch (NumberFormatException nfe) {
						JOptionPane.showMessageDialog(null, "상품 개수와 가격에는 숫자를 입력해주세요.", "AddProduct error", JOptionPane.WARNING_MESSAGE); 
					}
					catch (Exception e1) { // 예외 발생하면 원인 메시지 알림창 띄우기
						JOptionPane.showMessageDialog(null, "잘못된 방법입니다.", "AddProduct error", JOptionPane.WARNING_MESSAGE); 
					}
				}
				
			});
			
			// 상품 삭제 버튼
			productDeleteBtn = new JButton("삭제");
			productDeleteBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
			productDeleteBtn.setBounds(414, 319, 110, 38);
			add(productDeleteBtn);
			// 클릭시 선택한 행이 삭제됨
			productDeleteBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					trs.setRowFilter(RowFilter.regexFilter("")); // 삭제 버튼 누르면 아무것도 검색 안했을 때의 전체 테이블 보여주기

					try {
						int row = productList_table_manager.getSelectedRow(); // 선택한 행의 인덱스 불러오기
						if (row == -1) // 선택한 행이 존재하지 않으면
							return;
						String delCode = String.valueOf(model.getValueAt(row, 1)); // 선택한 행의 상품 코드명 불러오기
						
						if (searchFlag == true) { // 검색했다면 
							for (int i = 0; i < act.getProductCount(); i++) { // 전체 상품 리스트의 모든 행을 돌며
								if (delCode.equals(String.valueOf(model.getValueAt(i, 1)))) { // 검색한 상품의 코드와 같은 코드를 가진 상품 찾기
									model.removeRow(i); 
									act.delete(delCode);
									// 상품 삭제
								}
							}
							searchFlag = false; // 삭제 후 검색 여부를 판단하는 변수 초기 상태 복원
						}
						else { // 검색한 적이 없다면
							model.removeRow(row);
							act.delete(delCode);
							// 상품 삭제
						}
					}					
					 catch (Exception e2) {	
						
					}
				}
			});
		}
	}
	
	class UserManager extends JPanel {
		// user_manager
		
		private JLabel userListTableLabel;
		private JTable userTable_manager;
		private JButton mainBtn_userManager;
		private JLabel revenueLabel;
		private JTextPane revenuePane;
		
		public UserManager(Manager act) {
			
			setBounds(0, 0, 555, 430);
			setLayout(null);
			setVisible(false);
			
			userListTableLabel = new JLabel("사용자 목록");
			userListTableLabel.setFont(new Font("맑은 고딕", Font.BOLD, 15));
			userListTableLabel.setBounds(12, 10, 90, 21);
			add(userListTableLabel);
			
			// 상품 목록 리스트 (재고상품만 출력 - 상품개수가 0개이면 출력 X)
			String[] columns_userManager = {
					"이름", "전화번호", "대여 물품", "대여 일자", "반납 예정일"
			};
			String [][] data_userManager = {
			};
			
			DefaultTableModel model2 = new DefaultTableModel(data_userManager, columns_userManager) {
				public boolean isCellEditable(int row, int column) {
					return false; // 셀 내용 수정 불가
				}
			};
			userTable_manager = new JTable(model2);
			userTable_manager.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			userTable_manager.getTableHeader().setReorderingAllowed(false); // 열 이동 불가
			userTable_manager.getTableHeader().setResizingAllowed(false); // 열 크기 조절불가
			userTable_manager.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
			try {
				String arr[] = new String[5];
				
				for (int i = 0; i < act.getUserCount(); i++) {
					String code = ""; // 대여한 상품 저장 스트링

					arr[0] = act.userAt(i).getName();
					arr[1] = act.userAt(i).getPhone();
					
					int j;
					for (j = 0; j < act.userAt(i).getRentalCount(); j++) {
						code += act.userAt(i).codeAt(j); // 대여한 상품의 개수만큼 상품코드를 불러와 스트링에 누적 저장
						if (j < (act.userAt(i).getRentalCount() - 1))
							code += ", "; // 마지막 상품 이전엔 상품 추가할 때 콤마도 추가
					}
					arr[2] = code; // 대여한 상품 스트링
					arr[3] = act.userAt(i).getRentalDay();
					arr[4] = act.userAt(i).getReturnDay();
					
					model2.addRow(arr);
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e.getStackTrace(), "Save error", JOptionPane.WARNING_MESSAGE); 
			}
			
		
			JScrollPane userListPanel = new JScrollPane(userTable_manager);
			userListPanel.setBounds(12, 44, 512, 285);
			add(userListPanel);
			
			mainBtn_userManager = new JButton("메인");
			mainBtn_userManager.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
			mainBtn_userManager.setBounds(458, 12, 66, 23);
			mainBtn_userManager.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			add(mainBtn_userManager);
			
			revenueLabel = new JLabel("일일 매출");
			revenueLabel.setHorizontalAlignment(SwingConstants.CENTER);
			revenueLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
			revenueLabel.setBounds(12, 335, 78, 15);
			add(revenueLabel);
			
			revenuePane = new JTextPane();
			revenuePane.setBounds(91, 333, 96, 21);
			revenuePane.setEditable(false);
			String revenue = String.valueOf(act.getRevenue());
			revenue = revenue.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ","); // 원단위 자릿수 콤마
			revenuePane.setText(revenue + "원"); 
			add(revenuePane);
			
		}
	}
	
	
	
}	
	
		
	
	
	
	
	
