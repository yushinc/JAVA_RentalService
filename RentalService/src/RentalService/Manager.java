package RentalService;
import java.util.*;
import java.io.*;

public class Manager {
	private ArrayList<Product> productList; // 상품 객체 arraylist
	private ArrayList<User> userList;  // 유저 객체 arraylist
	private Integer revenue; // 일일 매출 총액 변수
	
	// 인수 없는 생성자
	Manager () { 
		productList = new ArrayList<Product>(); // 상품 객체 배열 새성
		userList = new ArrayList<User>(); // 유저 객체 배열 생성
		revenue = (Integer) new Integer(0);
	}
	
	// 인수 있는 생성자
	Manager (ObjectInputStream ois) throws Exception {
		productList = new ArrayList<Product>(); // 상품 객체 배열 생성
		userList = new ArrayList<User>(); // 유저 객체 배열 생성
		revenue = (Integer) ois.readObject();

		try { 
			Integer productCount = (Integer) ois.readObject();
			
			// i < productList.size() 사용시 i++될떄마다 size()로 리스트의 크기를 계산하므로 복잡 count변수 사용 추천
			for (int i = 0; i < productCount.intValue(); i++) { 
				Product p = (Product) ois.readObject();
				productList.add(p); // 상품 객체 배열에 추가
			}
			
			Integer userCount = (Integer) ois.readObject();
			for (int i = 0; i < userCount.intValue(); i++) {
				User u = (User) ois.readObject();
				userList.add(u); // 유저 객체 배열에 추가
			}
		} catch (IOException ioe) {
		
		} catch (Exception e) {
			throw new Exception("파일을 읽을 수 없습니다.");
		}
	}
		
	// file save
	public void saveFile(ObjectOutputStream oos) throws Exception {
		try {
			oos.writeObject(revenue); // 총 수익 저장
			// productList.size()를 사용해도 되지만 for문내부에 size 사용시 반복할 때마다 size()함수 호출해야하므로
			// 연산 횟수가 늘어나게된다. 연산 횟수를 줄이기위해 변수 정의하여 사용하는 것이 더욱 효율적
			Integer productCount = new Integer(productList.size()); // 상품 객체 배열 크기 read (읽을때만 지역변수 사용)
			
			// read할 때 몇개를 읽을지 count변수값도 파일에 작성해야한다. 
			// 배열 작성 이전에 count 작성하여 read시 순서 고려
			oos.writeObject(productCount); 
			for (int i = 0; i < productCount.intValue(); i++)
				oos.writeObject(productList.get(i));
			
			Integer userCount = new Integer(userList.size()); // 유저 객체 배열 크기 read (읽을때만 지역변수 사용)
			// read할 때 몇개를 읽을지 count변수값도 파일에 작성해야한다. 
			// 배열 작성 이전에 count 작성하여 read시 순서 고려
			oos.writeObject(userCount); 
			for (int i = 0; i < userCount.intValue(); i++)
				oos.writeObject(userList.get(i));
		}
		catch (Exception e) { // 예외 처리
			throw new Exception ("파일로 출력할 수 없습니다.");
		}
	}
	
	// 코드 중복 검색 (오버라이딩한 equals 사용하여 중복된 코드가 있는지 체크)
	public void checkCode(Product p) throws Exception {
		
		if (productList.indexOf(p) >= 0)
			throw new Exception("잘못된 상품 등록입니다.");
	}
	
	// 상품 등록
	public void add(Product p) throws Exception {
		try{
			checkCode(p); // 코드 중복 검색
			productList.add(p); // 상품 추가
		}
		catch (Exception e) {
			throw new Exception ("잘못된 상품 등록입니다.");
		}
	}

	// 상품 삭제
	public void delete(String productCode) throws Exception {
		
		try{
			int number = searchCode(productCode); // 상품 배열에서 검색하기
			productList.remove(number); // 상품 배열에서 삭제하기
		}
		catch (Exception e) {
			throw new Exception ("존재하지 않는 상품입니다.");
		}
	}
	
	// 상품 객체 검색
	public int searchCode(String productCode) throws Exception {
		
		// 입력받은 코드와 일치하는 상품이 있는지 검색
		int index = productList.indexOf(new Product(productCode));
		// 있다면
		if (index >= 0) 
			return index; // 해당 상품 인덱스 반환
		else
			throw new Exception("일치하는 코드를 찾을 수 없습니다.");
	}

	// 상품 배열 i번째 리턴
	public Product productAt(int i) throws Exception {
		try {
			return productList.get(i); // 상품 배열 i번째 상품 객체 return
		} catch (IndexOutOfBoundsException iobe) { // arrayList에 어떠한 원소도 존재하지 않는다면 예외발생
			return null; // null로 리턴해주어 UI에서 예외가 발생하지 않도록 처리
		}
	} 
	
	// productCount 값 반환
	public int getProductCount() {
		return productList.size();
	}
	
	// 전화번호 중복 검색
	public void checkPhone(User u) throws Exception {
		 // 전화번호가 이미 있다면 예외 발생
		if (userList.indexOf(u) >= 0)
			throw new Exception("이미 존재하는 번호입니다.");
	}
	
	// 상품 대여하기 (재고 개수에서 대여 개수 제외)
	public void subStock(User u) throws Exception {
		// 재고 개수에서 대여 개수 제외하기
		for(int i = 0; i < u.getRentalCount(); i++)
		{
			String code = u.codeAt(i); // 해당 User 객체의 i번째 대여 물품 코드
			int searchIndex;
			try {
				searchIndex = searchCode(code); // productList에서 해당 코드의 인덱스 번호 검색
			} 
			catch (Exception e) {
				throw new Exception("잘못된 방법의 체크인입니다.");
			}
			
			Product p = productList.get(searchIndex); //해당 인덱스의 product 객체
			p.subNumber(); // 대여가 가능한지 확인 후 빌리기 (재고 1개 삭제, 재고가 0개이면 대여 불가)
		}
	}
	
	// 체크인 (= 상품 대여)
	public void checkIn(User u) throws Exception {
		try {
			checkPhone(u); // 전화번호 중복 검색
			subStock(u); // 재고 개수에서 대여 개수 제외
			userList.add(u); // 유저 배열에 대여한 유저 정보 넣기
		}
		catch(Exception e) {
			throw new Exception("잘못된 방법입니다.");
		}
	}
	
	// userCount 값 반환
	public int getUserCount() {
		return userList.size();
	}
	
	// 유저 객체 배열 i번째 리턴
	public User userAt(int i)
	{
		try {
			return userList.get(i); // 대여 배열 i번째 유저 객체 return
		} catch (IndexOutOfBoundsException iobe) {
			return null;
		}
	}
	
	public int searchUser(String phone) throws Exception {
		
		// 입력받은 전화번호와 일치하는 사용자가 있는지 검색
		int index = userList.indexOf(new User(phone));
		// 있다면
		if (index >= 0)
			return index; // 해당 사용자 인덱스 반환
		else
			throw new Exception("일치하는 회원정보가 존재하지 않습니다.");
	}
	
	//  상품 반납 = 상품 재고 다시 추가
	public void addStock(int index) throws Exception {
		User u = userAt(index); // 입력받은 index의 유저객체 찾아서 새로 생성한 u객체에 전달
		for(int i = 0; i < u.getRentalCount(); i++) {
			String code = u.codeAt(i); // 해당 User 객체의 i번째 대여 물품 코드
			try {
				int number = searchCode(code); // productList에서 해당 코드의 인덱스 번호 검색
				productAt(number).addNumber(); //해당 인덱스의 product 객체의 재고 추가하기
			}
			catch (Exception e) {
				throw new Exception ("잘못된 방법의 체크아웃입니다.");
			}
		}
	}
	
	
	// 체크아웃
	public void checkOut(int index) throws Exception {
		try{
			addStock(index);// 반납 후 상품 재고 다시 추가하기
			int money = userList.get(index).pay();// 금액 반환받기
			userList.remove(index); // arrayList는 배열의 중간이 삭제되어도 자동으로 뒤의 원소 인덱스-- 하여 빈공간 채워줌
			revenue += money; // 매출에 추가하기
		}
		catch(Exception e) {
			throw new Exception ("잘못된 방법의 체크아웃입니다.");
		}
	}
	
	
	
	// 상품 재고 조정 (재고 추가)
	public void orderStock(int index, int n) throws Exception {
		Product p = productList.get(index);
		
		for (int i = 0; i < n; i++) 
			p.addNumber(); // n개 만큼 재고 추가
	}
	
	// 상품 재고 조정 (재고 삭제)
	public void deleteStock(int index, int n) throws Exception {
		Product p = productList.get(index);
		if (p.getNumber() > n) { // 입력받은 인덱스에 해당하는 상품의 재고가 n개 이상이면
			for (int i = 0; i < n; i++) 
				p.subNumber(); // n개 만큼 재고 줄여주기
		}
		else 
			throw new Exception ("상품의 재고가" + n + "개보다 적습니다.");
	}
	
	// 매출 반환
	public int getRevenue() {
		return revenue.intValue();
	}
	
	
	
}