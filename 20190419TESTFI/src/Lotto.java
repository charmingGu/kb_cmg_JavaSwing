import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONException;
import org.json.JSONObject;
import javax.net.ssl.SSLSession;

//http, ssl 보안 관련 해결 class.
 class JsonReader {

	  private String readAll(Reader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    int cp;
	    while ((cp = rd.read()) != -1) {
	      sb.append((char) cp);
	    }
	    return sb.toString();
	  }

	  public JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		  HostnameVerifier allHostsValid = new HostnameVerifier() {
	          public boolean verify(String hostname, SSLSession session) {
	              return true;
	          }
		  };
	    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	    InputStream is = new URL(url).openStream();
	    try {
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	      String jsonText = readAll(rd);
	      JSONObject json = new JSONObject(jsonText);
	      return json;
	    } finally {
	      is.close();
	    }
  }
}

//로또번호조회 프로그램의 틀을 만들고 기능을 구현한 class.
@SuppressWarnings("serial")
public class Lotto extends JFrame implements MouseListener, ActionListener{
	
	static private JTextField textField_6 = new JTextField();
	
	static MyButton mbtn1 = new MyButton("");
	static MyButton mbtn2 = new MyButton("");
	static MyButton mbtn3 = new MyButton("");
	static MyButton mbtn4 = new MyButton("");
	static MyButton mbtn5 = new MyButton("");
	static MyButton mbtn6 = new MyButton("");
	static MyButton mbtn7 = new MyButton("");
	
	JLabel label_6 = new JLabel("최근 로또 번호");
	JLabel number_label = new JLabel("");
	
	JButton number_btn = new JButton("확인");
	JButton btnNewButton = new JButton("해당 회차로 조회");
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private final JTextField textField_8 = new JTextField();
	
	public static void setUIFont(javax.swing.plaf.FontUIResource f){
		Enumeration<Object> keys = UIManager.getDefaults().keys();
		while(keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if(value instanceof FontUIResource)
				UIManager.put(key, f);
		}
	}
	
	public void init() {
		getContentPane().setLayout(null);
		
		label_6.setBounds(33, 26, 137, 30);
		getContentPane().add(label_6);
// 출력 버튼 창.
		mbtn1.setBounds(33,66,80,80);
		getContentPane().add(mbtn1);
		
		mbtn2.setBounds(125,66,80,80);
		getContentPane().add(mbtn2);
		
		mbtn3.setBounds(217,66,80,80);
		getContentPane().add(mbtn3);
		
		mbtn4.setBounds(309,66,80,80);
		getContentPane().add(mbtn4);
		
		mbtn5.setBounds(401,66,80,80);
		getContentPane().add(mbtn5);
		
		mbtn6.setBounds(493,66,80,80);
		getContentPane().add(mbtn6);
		
		mbtn7.setBounds(650,66,80,80);
		getContentPane().add(mbtn7);
		
		textField_6 = new JTextField();
		textField_6.setColumns(10);
		textField_6.setBounds(180, 310, 132, 42);
		getContentPane().add(textField_6);
		
		number_label.setBounds(198, 20, 99, 42);
		getContentPane().add(number_label);
		
		number_btn.setBounds(335, 310, 116, 42);
		getContentPane().add(number_btn);
		
		textField = new JTextField();
		textField.setBounds(27, 202, 101, 42);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(140, 202, 116, 42);
		getContentPane().add(textField_1);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(273, 202, 116, 42);
		getContentPane().add(textField_2);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(401, 202, 116, 42);
		getContentPane().add(textField_3);
		
		textField_4 = new JTextField();
		textField_4.setColumns(10);
		textField_4.setBounds(529, 202, 116, 42);
		getContentPane().add(textField_4);
		
		textField_5 = new JTextField();
		textField_5.setColumns(10);
		textField_5.setBounds(657, 202, 116, 42);
		getContentPane().add(textField_5);
		textField_8.setText("결과 출력창");
		
		textField_8.setBounds(27, 254, 900, 46);
		getContentPane().add(textField_8);
		
		JLabel label = new JLabel("+");
		label.setFont(new Font("굴림", Font.PLAIN, 50));
		label.setBounds(594, 82, 31, 46);
		getContentPane().add(label);
		
		btnNewButton.setBounds(463, 309, 191, 42);
		getContentPane().add(btnNewButton);
	}
	
	public void event() {
		number_btn.addActionListener(this);
		btnNewButton.addActionListener(this);
	}
	//최근 로또 번호 생성을 위한 함수.
	public void setLottoNum() throws IOException, JSONException {
		Calendar cal = Calendar.getInstance();
		JsonReader tempdata = new JsonReader();
		long times = (long)cal.getTimeInMillis();
		int day = (86400000*7); //하루를 밀리초 단위로 환산하여 7을 곱하여 일주일치 계산.
		long baseDay = 1555156800000L; //2019.04.13  토요일 21시 854회 기준 시.
		int baseRound = 854; // 기준이 되는 회차
		long roundDay = baseRound + ((times-baseDay)/day); // 최근 회차를 구하는 공식.
		String inputDay = Long.toString(roundDay);
		number_label.setText(inputDay+"회차");
		String url = "https://www.nlotto.co.kr/common.do?method=getLottoNumber&drwNo="+inputDay;
		JSONObject json = tempdata.readJsonFromUrl(url);
		
		mbtn1.setText(json.getString("drwtNo1"));
	    mbtn2.setText(json.getString("drwtNo2"));
	    mbtn3.setText(json.getString("drwtNo3"));
	    mbtn4.setText(json.getString("drwtNo4"));
	    mbtn5.setText(json.getString("drwtNo5"));
	    mbtn6.setText(json.getString("drwtNo6"));
	    mbtn7.setText(json.getString("bnusNo"));
	    textField_6.setText(inputDay);
	}
	
	
	
	Lotto() throws Exception{
		super("로또번호 조회");
		textField_8.setColumns(10);
		
		init();
		event();
		setLottoNum(); //최근 로또번호 생성을 위한 함수.
		
		setVisible(true);
		setSize(1000, 406);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
//	로또회차 정보와 로또 번호를 비교해주는 함수.
	public void searchLottoNum() {
		JsonReader esc = new JsonReader();
		String turn = textField_6.getText();
		String url = "https://www.nlotto.co.kr/common.do?method=getLottoNumber&drwNo="+turn;
		try {
			number_label.setText(turn+"회차");
			label_6.setText("로또 번호 조회");
			int count = 0;
			JSONObject json = esc.readJsonFromUrl(url);
		    System.out.println(json.toString());
//		    각 값들을 담기위한 ArrayList 자료형.
		    ArrayList<String> LottoNum = new ArrayList<String>();
		    ArrayList<String> SelectNum = new ArrayList<String>();
//		    조회값 b1~b7
		    String b1 = json.getString("drwtNo1");
		    String b2 = json.getString("drwtNo2");
		    String b3 = json.getString("drwtNo3");
		    String b4 = json.getString("drwtNo4");
		    String b5 = json.getString("drwtNo5");
		    String b6 = json.getString("drwtNo6");
		    String b7 = json.getString("bnusNo");
//		    입력값 t1~t6
		    String t1 = textField.getText();
		    String t2 = textField_1.getText();
		    String t3 = textField_2.getText();
		    String t4 = textField_3.getText();
		    String t5 = textField_4.getText();
		    String t6 = textField_5.getText();
		    
		    mbtn1.setText(b1);
		    mbtn2.setText(b2);
		    mbtn3.setText(b3);
		    mbtn4.setText(b4);
		    mbtn5.setText(b5);
		    mbtn6.setText(b6);
		    mbtn7.setText(b7);
//		    ArrayList에 추가.
		    LottoNum.add(b1);
		    LottoNum.add(b2);
		    LottoNum.add(b3);
		    LottoNum.add(b4);
		    LottoNum.add(b5);
		    LottoNum.add(b6);
		    
		    SelectNum.add(t1);
		    SelectNum.add(t2);
		    SelectNum.add(t3);
		    SelectNum.add(t4);
		    SelectNum.add(t5);
		    SelectNum.add(t6);
		    
			/*
			 * 순서대로 6개 다 맞으면 1등. 5개만 맞고 보너스 번호 1개를 맞추면 2등. 즉 같은 count 여도 순서대로 다 맞아서 6개냐, 
			 * 보너스 번호로 6개냐에 따라 1등과 2등이 달라짐.
			 */
	    	if(LottoNum.removeAll(SelectNum)){
	    		count = LottoNum.size();
	    		if(count==0) {
	    			textField_8.setText("축하합니다 1등입니다. 일치하는 개수는" +String.valueOf(6-count)+"개 입니다.");
	    			return;
	    		}
	    	}
    		if(count==1) {
    			if(SelectNum.contains(b7)) {
    				count = 0;
    				textField_8.setText("축하합니다 2등입니다. 일치하는 개수는 보너스 번호 포함" +String.valueOf(6-count)+"개 입니다.");
    				return;
    			}
    			else {
    				textField_8.setText("축하합니다 3등입니다. 일치하는 개수는" +String.valueOf(6-count)+"개 입니다.");
    				return;
    			}
	    		}
	    		if(count==2) {
	    			textField_8.setText("축하합니다 4등입니다. 일치하는 개수는" +String.valueOf(6-count)+"개 입니다.");
	    			return;
	    		}
	    		if(count==3) {
	    			textField_8.setText("축하합니다 5등입니다. 일치하는 개수는" +String.valueOf(6-count)+"개 입니다.");
	    			return;
	    		}
	    		else{
	    			System.out.println(LottoNum.size());
	    			textField_8.setText("아쉽지만 순위권 밖입니다. 일치하는 개수는" +String.valueOf(6-LottoNum.size())+"개 입니다.");
	    		}
	    	
//	    	System.out.println(count);
//	    	textField_8.setText("일치하는 개수는" +String.valueOf(count));
		    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			textField_8.setText("정보가 올바르지 않습니다. 다시 확인해주세요.");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			textField_8.setText("정보가 올바르지 않습니다. 다시 확인해주세요.");
		}
	}
	
	public void outputLottoNum() {
		JsonReader esc = new JsonReader();
		String turn = textField_6.getText();
		String url = "https://www.nlotto.co.kr/common.do?method=getLottoNumber&drwNo="+turn;
		try {
			number_label.setText(turn+"회차");
			label_6.setText("로또 번호 조회");
			JSONObject json = esc.readJsonFromUrl(url);
		    System.out.println(json.toString());
//			각 값들을 담기위한 ArrayList 자료형.
//			조회값 b1~b7
		    String b1 = json.getString("drwtNo1");
		    String b2 = json.getString("drwtNo2");
		    String b3 = json.getString("drwtNo3");
		    String b4 = json.getString("drwtNo4");
		    String b5 = json.getString("drwtNo5");
		    String b6 = json.getString("drwtNo6");
		    String b7 = json.getString("bnusNo");
		    mbtn1.setText(b1);
		    mbtn2.setText(b2);
		    mbtn3.setText(b3);
		    mbtn4.setText(b4);
		    mbtn5.setText(b5);
		    mbtn6.setText(b6);
		    mbtn7.setText(b7);	
		}catch(IOException e){
			e.printStackTrace();
			textField_8.setText("정보가 올바르지 않습니다. 다시 확인해주세요.");
		}catch(JSONException e) {
			e.printStackTrace();
			textField_8.setText("정보가 올바르지 않습니다. 다시 확인해주세요.");
		}
	}
	
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == number_btn) {
			searchLottoNum();
		}
		if(e.getSource() == btnNewButton) {
			outputLottoNum();
		}
	}
	
	public static void main (String[] args) throws Exception{
		
		String fName = "BMDOHYEON_ttf.ttf";
		Font f1 = Font.createFont(Font.TRUETYPE_FONT, new File(fName));
		f1 = f1.deriveFont(20f);
		setUIFont(new FontUIResource(f1));
		new Lotto();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {

	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		mbtn1.state = 5;
		mbtn1.repaint();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		mbtn1.state = 10;
		mbtn1.repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}
