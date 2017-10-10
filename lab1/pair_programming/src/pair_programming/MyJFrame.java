package pair_programming;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.List;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Rectangle;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.IntPredicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.lang.model.element.VariableElement;
import javax.security.auth.x500.X500Principal;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.OptionPaneUI;

import java.awt.image.BufferedImage; 
public class MyJFrame extends JFrame{
	private Graph G;
	private JFrame frame;
    private MenuBar bar;// 定义菜单栏
    private JTextArea ta;
    private Menu fileMenu,actionMenu;// 定义"文件"和"子菜单"菜单
    private MenuItem openItem, saveItem,showItem,queryItem,generateItem;// 定义子条目菜单项
    private MenuItem calcItem,randomItem;
    private FileDialog openDia, saveDia;// 定义“打开、保存”对话框
    private File file;//定义文件	
	
    private myprint background;//画布
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MyJFrame window = new MyJFrame();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public MyJFrame(){
		initGUI();
	}
	public void initGUI(){
		frame = new JFrame();
		frame.setVisible(true);//可视
		frame.getContentPane().setLayout(null);
		frame.setSize(600, 800);//大小
		frame.setLocationRelativeTo(null);//居中
		frame.setResizable(false);//不可改变大小
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);//点X关闭
		
		
        bar = new MenuBar();// 创建菜单栏
        ta = new JTextArea();// 创建文本域
        fileMenu = new Menu("文件");// 创建“文件”菜单
        actionMenu= new Menu("功能");// 创建“功能”菜单
        
        openItem = new MenuItem("打开");// 创建“打开"菜单项
        saveItem = new MenuItem("保存");// 创建“保存"菜单项
        fileMenu.add(openItem);// 将“打开”菜单项添加到“文件”菜单上
        fileMenu.add(saveItem);// 将“保存”菜单项添加到“文件”菜单上
        
        showItem=new MenuItem("展示有向图");
        queryItem=new MenuItem("查询桥接词");
        generateItem=new MenuItem("生成新文本");
        calcItem=new MenuItem("最短路径");
        randomItem=new MenuItem("随机游走");
        
        actionMenu.add(showItem);
        actionMenu.add(queryItem);
        actionMenu.add(generateItem);
        actionMenu.add(calcItem);
        actionMenu.add(randomItem);
        
        bar.add(fileMenu);// 将文件添加到菜单栏上
        bar.add(actionMenu);
        frame.setMenuBar(bar);// 将此窗体的菜单栏设置为指定的菜单栏。
        
        openDia = new FileDialog(frame, "打开", FileDialog.LOAD);
        saveDia = new FileDialog(frame, "保存", FileDialog.SAVE);
        
        ta.setBounds(0, 600, 594, 151);//设置文本框大小和位置
        ta.setBackground(Color.white);
        ta.setBorder(new LineBorder(Color.black, 2, false));
        ta.setLineWrap(true);//自动换行
        ta.setEditable(false);//不可编辑
        frame.add(ta);// 将文本域添加到窗体内
        
        background= new myprint();
        background.setBounds(0, 20, 600, 570);
        frame.add(background);

        myEvent();// 加载事件处理
        
        frame.setVisible(true);// 设置窗体可见
	}
	private void myEvent() {
	        // 打开菜单项监听
	        openItem.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                
	                openDia.setVisible(true);//显示打开文件对话框
	                
	                String dirpath = openDia.getDirectory();//获取打开文件路径并保存到字符串中。
	                String fileName = openDia.getFile();//获取打开文件名称并保存到字符串中
	                
	                if (dirpath == null || fileName == null)//判断路径和文件是否为空
	                    return;
	                else
	                    ta.setText(null);//文件不为空，清空原来文件内容。
	                file = new File(dirpath, fileName);//创建新的路径和名称
	                try {
	                    BufferedReader bufr = new BufferedReader(new FileReader(file));//尝试从文件中读东西
	                    String line = null;//变量字符串初始化为空
	                    String s="";   //要传入的字符串
	                    while ((line = bufr.readLine()) != null) {
	                        s=s+line+"\r\n";
	                        ta.append(line + "\r\n");//显示每一行内容
	                    }
	                    G=createDirectedGraph(s);
	                    bufr.close();//关闭文件
	                } catch (FileNotFoundException e1) {
	                    // 抛出文件路径找不到异常
	                    e1.printStackTrace();
	                } catch (IOException e1) {
	                    // 抛出IO异常
	                    e1.printStackTrace();
	                }
	            }
	        });
	     // 保存菜单项监听
	        saveItem.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            		if(G.image==null){
	            			JOptionPane.showMessageDialog(null, "未生成图像！", "错误",JOptionPane.ERROR_MESSAGE);
	            			return;
	            		}
	                    saveDia.setVisible(true);//显示保存文件对话框
	                    String dirpath = saveDia.getDirectory();//获取保存文件路径并保存到字符串中。
	                    String fileName = saveDia.getFile();////获取打保存文件名称并保存到字符串中
	                    String[] test=fileName.split("\\.");
	                    if(!test[test.length-1].equals("jpg"))          //加后缀名
	                    	fileName=fileName+".jpg";           
	                    if (dirpath == null || fileName == null)//判断路径和文件是否为空
	                        return;//空操作
	                    else
	                        file=new File(dirpath,fileName);//文件不为空，新建一个路径和名称
	                
	                    try {
	                    	ImageIO.write(G.image, "jpg", new File((dirpath+fileName)));
	                    	
	             /*
	                        BufferedWriter bufw = new BufferedWriter(new FileWriter(file));
	                        
	                        String text = ta.getText();//获取文本内容
	                        bufw.write(text);//将获取文本内容写入到字符输出流
	                        
	                        bufw.close();//关闭文件*/
	                    } catch (IOException e1) {
	                        //抛出IO异常
	                        e1.printStackTrace();
	                    }
	            }
	        });    
	        showItem.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            	if(ta.getText().length()==0){
	            		JOptionPane.showMessageDialog(null, "未读入文本！", "错误",JOptionPane.ERROR_MESSAGE);       //错误提示
	            	}
	            	else{
	            		try {
							background.paintcomponents(background.getGraphics(),G);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}//画图
	            	}
	            }

	        });
	        queryItem.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            	if(ta.getText().length()==0){
	            		JOptionPane.showMessageDialog(null, "未读入文本！", "错误",JOptionPane.ERROR_MESSAGE);
	            		
	            	}
	            	else{
	            		String word1 = JOptionPane.showInputDialog("请输入word1"); 
	            		String word2 = JOptionPane.showInputDialog("请输入word2"); 
	            		JOptionPane.showMessageDialog(null, querybRIDGEwORDS(G,word1,word2), "结果",JOptionPane.INFORMATION_MESSAGE);
	            	}
	            }

	        });
	        generateItem.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            	if(ta.getText().length()==0){
	            		JOptionPane.showMessageDialog(null, "未读入文本！", "错误",JOptionPane.ERROR_MESSAGE);  
	            	}
	            	else{
	            		try{
	            		String inputtext = JOptionPane.showInputDialog("请输入原句"); 
	            		JOptionPane.showMessageDialog(null, generateNewText(G, inputtext),"结果", JOptionPane.INFORMATION_MESSAGE);
	            		}catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
	            	}
	            }

	        });
	        calcItem.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            	if(ta.getText().length()==0){
	            		JOptionPane.showMessageDialog(null, "未读入文本！", "错误",JOptionPane.ERROR_MESSAGE);
	            	}
	            	else{
	            		String word1,word2;
	            		try{
	            			word1 = JOptionPane.showInputDialog("请输入word1"); 
	            			word2 = JOptionPane.showInputDialog("请输入word2"); 
	            		}catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							return;
						}
	            		String[] now=G.calcShortestPath(word1, word2);
	            		if (now[0].equals("INPUT_ERROR")){
	            			JOptionPane.showMessageDialog(null, "输入有误！", "错误",JOptionPane.ERROR_MESSAGE);
	            			return;
	            		}
	            		if (now[0].equals("NOT_FIND")){
	            			JOptionPane.showMessageDialog(null, "单词不存在！", "错误",JOptionPane.ERROR_MESSAGE);
	            			return;
	            		}
	            		if (now[0].equals("REACH_ERROR")){
	            			JOptionPane.showMessageDialog(null, "不可达！", "错误",JOptionPane.ERROR_MESSAGE);
	            			return;
	            		}
	            		int[][] ans= new int [now.length][];
	            		for (int i=0;i<now.length;i++){
	            			String[] st = now[i].split(" ");
	            			ans[i]=new int [st.length];
	            			for (int j=0;j<st.length;j++){
	            				ans[i][j]=Integer.parseInt(st[j]);
	            			}
	            		}
	            		for (int i=0;i<ans.length;i++){
	            			if (ans[i].length==0) continue;
	            			else{
	            				try {
									background.highlight(ans[i],background.getGraphics(),G);
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
	            				String a="";a=a+G.point_name()[ans[i][0]];
	            				int length=0;
	            				for (int j=1;j<ans[i].length;j++){
	            					a=a+"->"+G.point_name()[ans[i][j]];
	            					length+=G.a[ans[i][j-1]][ans[i][j]];
	            				}
	            				int result=JOptionPane.showConfirmDialog(null, "最短路径为： "+a+"\n长度为："+length+"\n是否继续？", "结果",JOptionPane.YES_NO_CANCEL_OPTION);
	            				if (result!=JOptionPane.YES_OPTION) return;
	            			}
	            		}
	            	}

	            }
	        });
	        randomItem.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            	if(ta.getText().length()==0){
	            		JOptionPane.showMessageDialog(null, "未读入文本！", "错误",JOptionPane.ERROR_MESSAGE);
	            	}
	            	else{
	            		String[] ans=randomWalk(G).split(" ");
	            		int i=0;
	            		String s=ans[i];
	            		int result=JOptionPane.showConfirmDialog(null, "结果为： "+s+"\n是否继续？","结果",JOptionPane.YES_NO_CANCEL_OPTION);
	            		while (result==JOptionPane.YES_OPTION){
	            			i++;
	            			if (i==ans.length){
	            				break;
	            			}
	            			s=s+" "+ans[i];
	            			result=JOptionPane.showConfirmDialog(null, "结果为： "+s+"\n是否继续？","结果",JOptionPane.YES_NO_CANCEL_OPTION);
	            		}
	            		JOptionPane.showMessageDialog(null, "最终结果为： "+s, "结果",JOptionPane.INFORMATION_MESSAGE);
	            	}
	            }
	        });
	        
	}
	private Graph createDirectedGraph(String filename){
		Graph ans =new Graph(filename);
		return ans;
	}
	private void showDirectedGraph(Graph G){
		
	}
	private String querybRIDGEwORDS(Graph G,String word1,String word2){
		return G.queryBridgeWords(word1, word2);
	}
	private String generateNewText(Graph G,String inputText){
		return G.generateNewText(inputText);
	}
	
	private String calcShortestPath(Graph G,String word1,String word2){
		return null;
	}
	private String randomWalk(Graph G){
		return G.randomWalk();
	}
}
