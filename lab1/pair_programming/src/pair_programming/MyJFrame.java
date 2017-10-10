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
    private MenuBar bar;// ����˵���
    private JTextArea ta;
    private Menu fileMenu,actionMenu;// ����"�ļ�"��"�Ӳ˵�"�˵�
    private MenuItem openItem, saveItem,showItem,queryItem,generateItem;// ��������Ŀ�˵���
    private MenuItem calcItem,randomItem;
    private FileDialog openDia, saveDia;// ���塰�򿪡����桱�Ի���
    private File file;//�����ļ�	
	
    private myprint background;//����
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
		frame.setVisible(true);//����
		frame.getContentPane().setLayout(null);
		frame.setSize(600, 800);//��С
		frame.setLocationRelativeTo(null);//����
		frame.setResizable(false);//���ɸı��С
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);//��X�ر�
		
		
        bar = new MenuBar();// �����˵���
        ta = new JTextArea();// �����ı���
        fileMenu = new Menu("�ļ�");// �������ļ����˵�
        actionMenu= new Menu("����");// ���������ܡ��˵�
        
        openItem = new MenuItem("��");// ��������"�˵���
        saveItem = new MenuItem("����");// ����������"�˵���
        fileMenu.add(openItem);// �����򿪡��˵�����ӵ����ļ����˵���
        fileMenu.add(saveItem);// �������桱�˵�����ӵ����ļ����˵���
        
        showItem=new MenuItem("չʾ����ͼ");
        queryItem=new MenuItem("��ѯ�ŽӴ�");
        generateItem=new MenuItem("�������ı�");
        calcItem=new MenuItem("���·��");
        randomItem=new MenuItem("�������");
        
        actionMenu.add(showItem);
        actionMenu.add(queryItem);
        actionMenu.add(generateItem);
        actionMenu.add(calcItem);
        actionMenu.add(randomItem);
        
        bar.add(fileMenu);// ���ļ���ӵ��˵�����
        bar.add(actionMenu);
        frame.setMenuBar(bar);// ���˴���Ĳ˵�������Ϊָ���Ĳ˵�����
        
        openDia = new FileDialog(frame, "��", FileDialog.LOAD);
        saveDia = new FileDialog(frame, "����", FileDialog.SAVE);
        
        ta.setBounds(0, 600, 594, 151);//�����ı����С��λ��
        ta.setBackground(Color.white);
        ta.setBorder(new LineBorder(Color.black, 2, false));
        ta.setLineWrap(true);//�Զ�����
        ta.setEditable(false);//���ɱ༭
        frame.add(ta);// ���ı�����ӵ�������
        
        background= new myprint();
        background.setBounds(0, 20, 600, 570);
        frame.add(background);

        myEvent();// �����¼�����
        
        frame.setVisible(true);// ���ô���ɼ�
	}
	private void myEvent() {
	        // �򿪲˵������
	        openItem.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                
	                openDia.setVisible(true);//��ʾ���ļ��Ի���
	                
	                String dirpath = openDia.getDirectory();//��ȡ���ļ�·�������浽�ַ����С�
	                String fileName = openDia.getFile();//��ȡ���ļ����Ʋ����浽�ַ�����
	                
	                if (dirpath == null || fileName == null)//�ж�·�����ļ��Ƿ�Ϊ��
	                    return;
	                else
	                    ta.setText(null);//�ļ���Ϊ�գ����ԭ���ļ����ݡ�
	                file = new File(dirpath, fileName);//�����µ�·��������
	                try {
	                    BufferedReader bufr = new BufferedReader(new FileReader(file));//���Դ��ļ��ж�����
	                    String line = null;//�����ַ�����ʼ��Ϊ��
	                    String s="";   //Ҫ������ַ���
	                    while ((line = bufr.readLine()) != null) {
	                        s=s+line+"\r\n";
	                        ta.append(line + "\r\n");//��ʾÿһ������
	                    }
	                    G=createDirectedGraph(s);
	                    bufr.close();//�ر��ļ�
	                } catch (FileNotFoundException e1) {
	                    // �׳��ļ�·���Ҳ����쳣
	                    e1.printStackTrace();
	                } catch (IOException e1) {
	                    // �׳�IO�쳣
	                    e1.printStackTrace();
	                }
	            }
	        });
	     // ����˵������
	        saveItem.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            		if(G.image==null){
	            			JOptionPane.showMessageDialog(null, "δ����ͼ��", "����",JOptionPane.ERROR_MESSAGE);
	            			return;
	            		}
	                    saveDia.setVisible(true);//��ʾ�����ļ��Ի���
	                    String dirpath = saveDia.getDirectory();//��ȡ�����ļ�·�������浽�ַ����С�
	                    String fileName = saveDia.getFile();////��ȡ�򱣴��ļ����Ʋ����浽�ַ�����
	                    String[] test=fileName.split("\\.");
	                    if(!test[test.length-1].equals("jpg"))          //�Ӻ�׺��
	                    	fileName=fileName+".jpg";           
	                    if (dirpath == null || fileName == null)//�ж�·�����ļ��Ƿ�Ϊ��
	                        return;//�ղ���
	                    else
	                        file=new File(dirpath,fileName);//�ļ���Ϊ�գ��½�һ��·��������
	                
	                    try {
	                    	ImageIO.write(G.image, "jpg", new File((dirpath+fileName)));
	                    	
	             /*
	                        BufferedWriter bufw = new BufferedWriter(new FileWriter(file));
	                        
	                        String text = ta.getText();//��ȡ�ı�����
	                        bufw.write(text);//����ȡ�ı�����д�뵽�ַ������
	                        
	                        bufw.close();//�ر��ļ�*/
	                    } catch (IOException e1) {
	                        //�׳�IO�쳣
	                        e1.printStackTrace();
	                    }
	            }
	        });    
	        showItem.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            	if(ta.getText().length()==0){
	            		JOptionPane.showMessageDialog(null, "δ�����ı���", "����",JOptionPane.ERROR_MESSAGE);       //������ʾ
	            	}
	            	else{
	            		try {
							background.paintcomponents(background.getGraphics(),G);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}//��ͼ
	            	}
	            }

	        });
	        queryItem.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            	if(ta.getText().length()==0){
	            		JOptionPane.showMessageDialog(null, "δ�����ı���", "����",JOptionPane.ERROR_MESSAGE);
	            		
	            	}
	            	else{
	            		String word1 = JOptionPane.showInputDialog("������word1"); 
	            		String word2 = JOptionPane.showInputDialog("������word2"); 
	            		JOptionPane.showMessageDialog(null, querybRIDGEwORDS(G,word1,word2), "���",JOptionPane.INFORMATION_MESSAGE);
	            	}
	            }

	        });
	        generateItem.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            	if(ta.getText().length()==0){
	            		JOptionPane.showMessageDialog(null, "δ�����ı���", "����",JOptionPane.ERROR_MESSAGE);  
	            	}
	            	else{
	            		try{
	            		String inputtext = JOptionPane.showInputDialog("������ԭ��"); 
	            		JOptionPane.showMessageDialog(null, generateNewText(G, inputtext),"���", JOptionPane.INFORMATION_MESSAGE);
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
	            		JOptionPane.showMessageDialog(null, "δ�����ı���", "����",JOptionPane.ERROR_MESSAGE);
	            	}
	            	else{
	            		String word1,word2;
	            		try{
	            			word1 = JOptionPane.showInputDialog("������word1"); 
	            			word2 = JOptionPane.showInputDialog("������word2"); 
	            		}catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							return;
						}
	            		String[] now=G.calcShortestPath(word1, word2);
	            		if (now[0].equals("INPUT_ERROR")){
	            			JOptionPane.showMessageDialog(null, "��������", "����",JOptionPane.ERROR_MESSAGE);
	            			return;
	            		}
	            		if (now[0].equals("NOT_FIND")){
	            			JOptionPane.showMessageDialog(null, "���ʲ����ڣ�", "����",JOptionPane.ERROR_MESSAGE);
	            			return;
	            		}
	            		if (now[0].equals("REACH_ERROR")){
	            			JOptionPane.showMessageDialog(null, "���ɴ", "����",JOptionPane.ERROR_MESSAGE);
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
	            				int result=JOptionPane.showConfirmDialog(null, "���·��Ϊ�� "+a+"\n����Ϊ��"+length+"\n�Ƿ������", "���",JOptionPane.YES_NO_CANCEL_OPTION);
	            				if (result!=JOptionPane.YES_OPTION) return;
	            			}
	            		}
	            	}

	            }
	        });
	        randomItem.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            	if(ta.getText().length()==0){
	            		JOptionPane.showMessageDialog(null, "δ�����ı���", "����",JOptionPane.ERROR_MESSAGE);
	            	}
	            	else{
	            		String[] ans=randomWalk(G).split(" ");
	            		int i=0;
	            		String s=ans[i];
	            		int result=JOptionPane.showConfirmDialog(null, "���Ϊ�� "+s+"\n�Ƿ������","���",JOptionPane.YES_NO_CANCEL_OPTION);
	            		while (result==JOptionPane.YES_OPTION){
	            			i++;
	            			if (i==ans.length){
	            				break;
	            			}
	            			s=s+" "+ans[i];
	            			result=JOptionPane.showConfirmDialog(null, "���Ϊ�� "+s+"\n�Ƿ������","���",JOptionPane.YES_NO_CANCEL_OPTION);
	            		}
	            		JOptionPane.showMessageDialog(null, "���ս��Ϊ�� "+s, "���",JOptionPane.INFORMATION_MESSAGE);
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
