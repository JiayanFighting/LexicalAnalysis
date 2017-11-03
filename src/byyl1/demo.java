package byyl1;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Toolkit;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RowSorter;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JTextPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.JLabel;
import java.awt.Font;

public class demo {

	private JFrame frame;
	private JTextArea textArea;
	private JTable table;
	private DefaultTableModel tokenListTbModel;
	private DefaultTableModel dfaListTbModel;
	private DefaultTableModel errorListTbModel;
	private String filepath2 ;
	private JScrollPane jScrollPane1;
	//运算符 operaters
//	String[] operaters={ ">",">=","<","<=","==","!=","|","&","||","&&","!","^","+","-","*","/","%","++","--","+=","-=","*=","%="};
	//界符 divideLine
	//String[] divideLines={ ",","=",";","[","]","(",")","{",";","}",".","\"","\'"};
	//关键字 keywords 
	String[] keywords={"char","long","short","float","double","const","boolean","void","null","false","true","enum","int",
			           "do","while","if","else","for","then","break","continue","class","static","final","extends","new","return","signed"
			           ,"struct","union","unsigned","goto","switch","case","default","auto","extern","register","sizeof","typedef","volatile"
	                 };
	String[] tokens={"char","long","short","float","double","const","boolean","void","null","false","true","enum","int",
		           "do","while","if","else","for","then","break","continue","class","static","final","extends","new","return","signed"
		           ,"struct","union","unsigned","goto","switch","case","default","auto","extern","register","sizeof","typedef","volatile",
		           ">",">=","<","<=","==","!=","|","&","||","&&","!","^","+","-","*","/","%","++","--","+=","-=","*=","/=",
		           ",","=",";","[","]","(",")","{","}",".","\"","'"
			
	};

	/**
	 * Launch the application.
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		DFATable dfa[]=new readDFATable().addDFA();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					demo window = new demo();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws Exception 
	 */
	public demo() throws Exception {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws Exception 
	 */
	private void initialize() throws Exception {
		frame = new JFrame();
		frame.setBounds(100, 100,1098,926);
		Dimension   screensize   =   Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize=frame.getSize();
		frame.setLocation((screensize.width-frameSize.width)/2,(screensize.height-frameSize.height)/2);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(32, 178, 170));
		panel.setBounds(0, 0, 1080, 879);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(102, 204, 204));
		panel_1.setBounds(23, 13, 1043, 853);
		panel.add(panel_1);
		panel_1.setLayout(null);
		
		jScrollPane1 = new JScrollPane();
		
		textArea = new JTextArea();
		textArea.setBackground(new Color(220, 220, 220));
		
		//jScrollPane1.setViewportView(textArea);
		//jScrollPane1.setRowHeaderView(new LineNumberHeaderView());
		textArea.setBounds(14, 77, 285, 372);
		panel_1.add(textArea);
		textArea.setColumns(10);
		
		final JButton btnNewButton_2 = new JButton("选择文件");
		btnNewButton_2.setFont(new Font("宋体", Font.BOLD, 15));
		btnNewButton_2.setBackground(new Color(32, 178, 170));
		btnNewButton_2.addActionListener(new ActionListener() {        
			public void actionPerformed(ActionEvent e) {  
				JFileChooser chooser = new JFileChooser();             //设置选择器  
				 chooser.setMultiSelectionEnabled(true);             //设为多选  
				int returnVal = chooser.showOpenDialog(btnNewButton_2);
				if (returnVal == JFileChooser.APPROVE_OPTION) {          //如果符合文件类型  
					String filepath = chooser.getSelectedFile().getAbsolutePath();      //获取绝对路径  
					System.out.println(filepath);  
					filepath2=filepath.replaceAll("\\\\", "/");
					File file = new File(filepath2);
					textArea.setText(txt2String(file));
				}  
			}  
			}); 
		btnNewButton_2.setBounds(42, 30, 107, 34);
		panel_1.add(btnNewButton_2);
		
		JButton btnNewButton_1 = new JButton("\u6D4B\u8BD5");
		btnNewButton_1.setFont(new Font("宋体", Font.BOLD, 15));
		btnNewButton_1.setBackground(new Color(32, 178, 170));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String str= textArea.getText(); 
			    String str2 = str.replaceAll("\r|\n","");  //去掉换行符空格
			    try {
			    	System.out.println("进入ActionEvent==========================================");
			    	lexicalAnalysis(str2);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_1.setBounds(186, 30, 92, 34);
		panel_1.add(btnNewButton_1);
		 
//种别码表格
	    tokenListTbModel = new DefaultTableModel(
				new Object[][] {},
				new String[] {
						"字符串","类别","种别码","value"
				}
			);
	    
	JTable tokenListTb = new JTable();
	tokenListTb.setBackground(new Color(224, 255, 255));
	tokenListTb.setFillsViewportHeight(true);
	tokenListTb.setModel(tokenListTbModel);
	
	//tokenListTbModel.addRow(new Object[] { "字符串", "类别","种别码" ,"value"});
	
    RowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(tokenListTbModel);
	tokenListTb.setRowSorter(sorter); 
	JScrollPane tokenSP = new JScrollPane();
	tokenSP.setViewportView(tokenListTb);
	tokenSP.setBounds(313, 77, 461, 372);
	panel_1.add(tokenSP);
	
//DFA表格
    dfaListTbModel = new DefaultTableModel(
			new Object[][] {},
			new String[] {
					"","","","","","","","","","","","","","","","",""
			}
		);
    String[][] result=new readDFATable().showDFA();
    for(int i=1;i<result.length;i++) {
        for(int j=1;j<result[i].length-2;j++) {
        	result[i][j]=result[i][j].replaceAll("-1", " ");
        }
    }
    for(int i=0;i<result.length;i++) {
        	dfaListTbModel.addRow(new Object[] { result[i][0],result[i][1],result[i][2],result[i][3],result[i][4],result[i][5],result[i][6],result[i][7],result[i][8],result[i][9]
        			,result[i][10],result[i][11],result[i][12],result[i][13],result[i][14],result[i][15],result[i][16],result[i][17]});
        
    }
	RowSorter<DefaultTableModel> sorter1 = new TableRowSorter<DefaultTableModel>(dfaListTbModel);
//出错表格
    errorListTbModel = new DefaultTableModel(
			new Object[][] {},
			new String[] {
					"出错符号","出错地方","出错原因"
			}
		);
    
	JTable errorListTb = new JTable();
	errorListTb.setBackground(new Color(224, 255, 255));
	errorListTb.setFillsViewportHeight(true);
	errorListTb.setModel(errorListTbModel);
	
	//errorListTbModel.addRow(new Object[] { "出错", "类别","出错原因" ,"value"});
	
	RowSorter<DefaultTableModel> sorter2 = new TableRowSorter<DefaultTableModel>(errorListTbModel);
	errorListTb.setRowSorter(sorter2); 
	JScrollPane errorSP = new JScrollPane();
	errorSP.setViewportView(errorListTb);
	errorSP.setBounds(788, 77, 241, 372);
	panel_1.add(errorSP);
	
	JLabel lblToken = new JLabel("TOKEN TABLE");
	lblToken.setFont(new Font("宋体", Font.BOLD, 30));
	lblToken.setBounds(462, 30, 254, 34);
	panel_1.add(lblToken);
	
	JLabel lblErrorTable = new JLabel("ERROR TABLE");
	lblErrorTable.setFont(new Font("宋体", Font.BOLD, 30));
	lblErrorTable.setBounds(811, 33, 203, 41);
	panel_1.add(lblErrorTable);
	
	JPanel panel_2 = new JPanel();
	panel_2.setBackground(new Color(255, 204, 204));
	panel_2.setBounds(0, 462, 1043, 404);
	panel_1.add(panel_2);
	panel_2.setLayout(null);
	
	JTable dfaListTb = new JTable();
	dfaListTb.setBackground(new Color(250, 240, 230));
	dfaListTb.setFillsViewportHeight(true);
	dfaListTb.setModel(dfaListTbModel);
	dfaListTb.setRowSorter(sorter1); 
	JScrollPane dfaSP = new JScrollPane();
	dfaSP.setViewportView(dfaListTb);
	dfaSP.setBounds(14, 70, 1015, 321);
	panel_2.add(dfaSP);
	
	JLabel lblDfa = new JLabel("DFA\u8F6C\u6362\u8868");
	lblDfa.setBounds(408, 10, 285, 47);
	panel_2.add(lblDfa);
	lblDfa.setFont(new Font("宋体", Font.BOLD, 42));
	}
	
	//读文件
	public static String txt2String(File file){
        StringBuilder result = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                result.append(s+System.lineSeparator());
            }
            br.close();    
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }
	
/*
 * 主要函数	
 */
	public void lexicalAnalysis(String str) throws Exception{
		while(tokenListTbModel.getRowCount()>0){
			tokenListTbModel.removeRow(0);
		}
		while(errorListTbModel.getRowCount()>0){
			errorListTbModel.removeRow(0);
		}
		//将字符串转化为字符数组
		char[] strline = str.toCharArray();
		String currentString="";
		int currentState=0; //当前状态
		int lastState=0; //上一状态
		
		DFATable dfa[]=new readDFATable().addDFA();
		DFATableState DFAstate[]=new readDFATable().showDFAState();
		for(int i=0;i<strline.length;i++)
		{
			System.out.println(strline[i]);
			currentString=currentString.concat(String.valueOf(strline[i]));
			System.out.println("currentString:"+currentString);//x,
			System.out.println("i : "+i);//x,
			lastState=currentState;
			
			if(currentString==" "||strline[i]==' '){
				currentString=currentString.replaceAll(" ", "");
				if(isKeyword(currentString)){
					tokenListTbModel.addRow(new Object[] {currentString,"关键字",tokenID(currentString),"   "});
				}
				else{
					tokenListTbModel.addRow(new Object[] {
							currentString,findTypeByState(lastState,DFAstate),tokenID(currentString),
							findTypeByState(lastState,DFAstate).equals("注释")||findTypeByState(lastState,DFAstate).equals("运算符")||findTypeByState(lastState,DFAstate).equals("界符")?"   ":currentString
							});
				}
				currentString="";
				currentState=0;
				continue ;
			}
			currentState=stateChange(strline[i],currentState,dfa);
			System.out.println(" 当前状态  =  "+currentState);
			System.out.println(" 上一状态  =  "+lastState);
			if(currentState==-1||currentState==-2)//现在这个字符在线路上混不下去了
			{
				currentString=currentString.substring(0, currentString.length()-1);
				//判断当前状态是否为终止状态  如不是  报错
				if(!isFinishByState(lastState,DFAstate)){
					errorListTbModel.addRow(new Object[] { currentString,"第"+i+"字符", "非法字符"});
					tokenListTbModel.addRow(new Object[] { currentString,"非法字符","无","   "});
				}
				else{
					if(isKeyword(currentString)){
						tokenListTbModel.addRow(new Object[] {currentString,"关键字",tokenID(currentString),"   "});
					}
					else{
						tokenListTbModel.addRow(new Object[] {
							currentString,
							findTypeByState(lastState,DFAstate),tokenID(currentString),
							findTypeByState(lastState,DFAstate).equals("注释")||findTypeByState(lastState,DFAstate).equals("运算符")||findTypeByState(lastState,DFAstate).equals("界符")?"   ":currentString
							});
					}
				}
				if(currentState==-2){
					tokenListTbModel.addRow(new Object[] {strline[i],"非法字符","无","   "});
					errorListTbModel.addRow(new Object[] {strline[i],"第"+i+"字符", "非法字符"});
					i++;
					//errorListTbModel.addRow(new Object[] {"/*", "注释未封闭"});
				}
				currentString="";
				currentState=0;
				i--;
			}
			//最后一个符号
			if(i==strline.length-1)
			{
				if(!isFinishByState(lastState,DFAstate)){
					errorListTbModel.addRow(new Object[] { currentString,"第"+i+"字符", "非法字符"});
					tokenListTbModel.addRow(new Object[] { currentString,"非法字符","无","   "});
				}
				else{
					if(isKeyword(currentString)){
						tokenListTbModel.addRow(new Object[] {currentString,"关键字",tokenID(currentString),"   "});
					}
					else{
						tokenListTbModel.addRow(new Object[] {
							currentString,
							findTypeByState(currentState,DFAstate),tokenID(currentString),
							findTypeByState(currentState,DFAstate).equals("注释")||findTypeByState(currentState,DFAstate).equals("运算符")||findTypeByState(currentState,DFAstate).equals("界符")?"   ":currentString
							});
					}
				}
				if(currentState==-2){
					tokenListTbModel.addRow(new Object[] {strline[i],"非法字符","无","   "});
					errorListTbModel.addRow(new Object[] {strline[i],"第"+i+"字符", "非法字符"});
					i++;
					//errorListTbModel.addRow(new Object[] {"/*", "注释未封闭"});
				}
			}
			
		}
		
	}
	
	public int stateChange(char currentChar,int currentState,DFATable[] dfa){
		System.out.println("=====currentState = "+currentState);
		boolean isInput=false;
		
		for(int i=0;i<dfa.length;i++)
		{
			if(isList(dfa[i].getInput(),currentChar)){
				isInput=true;//存在该输入
				if(dfa[i].getState()==currentState&&dfa[i].getNextState()!=-1)//当前状态一样 输入有 下一状态不为空 依然在当前自动机中
				{
					return dfa[i].getNextState();
				}
				
			}
		}
		if(isInput==false)
		{
			return -2;
		}
		//在状态9 10 待太久没有结束  注释未封闭 报错
		return -1;
	}
	public static boolean isList(String[] arr, char currentChar) {
		for(int i=0;i<arr.length;i++)
		{
			//System.out.print("  arr[i]   "+arr[i]);
			if(arr[i].equals(String.valueOf(currentChar)))
			{
				return true;
			}
		}
	    return false;
	}
	
	//根据state 返回该状态的类型
	public String findTypeByState(int state,DFATableState[] dfaState){
		for(int i=0;i<dfaState.length;i++)
		{
			if(dfaState[i].getState()==state)
			{
				return dfaState[i].getType();
			}
		}
		return "error"; 
	}
	
	//根据state 返回该状态的是否为终止状态
	public boolean isFinishByState(int state,DFATableState[] dfaState){
		for(int i=0;i<dfaState.length;i++)
		{
			if(dfaState[i].getState()==state)
			{
				return dfaState[i].isFinish();
			}
		}
		return true; 
	}
	
	//判断当前即将要输出的字符串是否为关键字
		public boolean isKeyword(String str){
			for(int i=0;i<keywords.length;i++)
			{
				if(keywords[i].equals(str)){
					return true;
				}
			}
			return false;
		}
		
	public int tokenID(String str){
		boolean flag=false;
		for(int i=0;i<tokens.length;i++)
		{
			if(tokens[i].equals(str)){
				flag=true;
				return i;
			}
		}
		if(flag==false)
		{
			tokens=Arrays.copyOf(tokens, tokens.length+1);
			tokens[tokens.length-1]=str;
		    return tokens.length;
		}
		return -1;
	}
}
