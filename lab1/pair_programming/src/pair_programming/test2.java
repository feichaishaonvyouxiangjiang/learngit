package pair_programming;

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class test2 {

	private JFrame jFrame = null;
	private JPanel jPanel = null;

	// 下侧右侧面板画图时的参数
	private int startX, startY, endX, endY;

	public void showGui() {
		jFrame = new JFrame();
		jPanel = new JPanel();

		// 设置jPanel大小
		jPanel.setPreferredSize(new Dimension(400, 400));
		jPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		jPanel.addMouseListener(new MouseAdapter() {
			// 鼠标按下事件，重置startX，startY
			public void mousePressed(MouseEvent e) {
				startX = e.getX();
				startY = e.getY();
			}

			// 双击面板是保存绘图区
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					savePic("D:/pic.jpg");
					jPanel.repaint();

				}
			}
		});

		// 鼠标拖动事件，自由画图
		jPanel.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e) {
				Graphics graphics = jPanel.getGraphics();
				endX = e.getX();
				endY = e.getY();
				((Graphics2D) graphics).setColor(new Color(144, 242, 41));
				((Graphics2D) graphics).setStroke(new BasicStroke(10));
				((Graphics2D) graphics).drawLine(startX, startY, endX, endY);
				startX = endX;
				startY = endY;
			}
		});
		// 设置jFrame的位置
		jFrame.setLocation(400, 200);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.add(jPanel);
		jFrame.pack();
		jFrame.setVisible(true);
	}

	public void savePic(String path) {
		BufferedImage myImage = null;
		try {
			myImage = new Robot().createScreenCapture(
					new Rectangle(jFrame.getX() + 8, jFrame.getY() + 30, jPanel.getWidth(), jPanel.getHeight()));
			ImageIO.write(myImage, "jpg", new File(path));
		} catch (AWTException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		test2 drawAndSave = new test2();
		drawAndSave.showGui();
	}

}
