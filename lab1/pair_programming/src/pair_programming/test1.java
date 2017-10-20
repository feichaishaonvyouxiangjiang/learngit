package pair_programming;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;

import javax.swing.JFrame;

public class test1 {
	public static void main(String[] args) {
		new DrawLine();
	}
}

class DrawLine extends JFrame {

	Point start;
	Point end;
	Container p;

	public DrawLine() {
		p = getContentPane();
		setBounds(200, 200, 800, 400);
		setVisible(true);
		p.setBackground(Color.BLACK);
		setLayout(null);
		paintComponents(this.getGraphics());
		setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void paintComponents(Graphics gg) {
		gg.drawLine(10, 100, 200, 400);
		final Graphics g = gg;
		start = new Point(0, 80);
		end = new Point(0, 80);
		g.setColor(Color.red);
		Runnable run = new Runnable() {
			Point temp = null;
			int x = 0;

			public void run() {
				int d = 1;
				while (true) {
					try {
						temp = new Point(x, 80 + (int) (40 * Math.sin(Math.PI * (x - 80) / 30)));
						g.drawLine(start.x, start.y, end.x, end.y);
						// g.drawLine(80, 80, 720, 80);
						g.drawLine(790 - start.x, start.y + 120, 790 - end.x, end.y + 120);
						g.drawLine(start.x, start.y + 240, end.x, end.y + 240);
						start = end;
						end = temp;
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (x > 800 || x < 0) {
						d = -d;
						Color c = g.getColor();
						if (c == Color.RED) {
							g.setColor(Color.BLUE);
						} else {
							g.setColor(Color.RED);
						}
					}
					x += d;
				}
			}
		};
		new Thread(run).start();
	}

	class Point {
		int x, y;

		public Point(int _x, int _y) {
			x = _x;
			y = _y;
		}
	}
}
