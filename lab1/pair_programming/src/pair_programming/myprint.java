package pair_programming;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

class myprint extends JPanel {

	public void paintcomponents(Graphics g, Graph G) throws IOException {
		super.paint(g); // 清理画板
		super.paintComponents(g);
		Graphviz gv = new Graphviz();
		gv.addln(gv.start_graph());

		for (int i = 0; i < G.count(); i++) {
			String str = G.point_name()[i];
			for (int j = 0; j < G.count(); j++) {
				if (G.adjacency_matrix()[i][j] > 0) {
					String temp = str + " -> " + G.point_name()[j] + "[ label = " + G.adjacency_matrix()[i][j] + " ];";
					gv.addln(temp);
				}
			}
		}
		gv.addln(gv.end_graph());
		ByteArrayInputStream in = new ByteArrayInputStream(gv.getGraph(gv.getDotSource(), "jpg"));// byte[]格式转image
		BufferedImage image = ImageIO.read(in);

		int width, height, width1, height1;
		if (image.getWidth() > 600) {
			width = 0;
			width1 = 600;
		} else {
			width = (this.getWidth() - image.getWidth()) / 2;
			width1 = image.getWidth();
		}
		if (image.getHeight() > 570) {
			height = 0;
			height1 = 570;
		} else {
			height = (this.getHeight() - image.getHeight()) / 2;
			height1 = image.getHeight();
		}
		G.image = image;
		g.drawImage(image, width, height, width1, height1, null);

	}

	public void highlight(int[] a, Graphics g, Graph G) throws IOException {
		super.paint(g);
		super.paintComponents(g);
		String[] color_set = { "black", "red", "brown", "indigo", "skyblue", "yellow", "navy", "darkorange", "gray",
				"lightblue", "olive", "crimson", "hotpink", "darkmagenta" };
		Graphviz gv = new Graphviz();
		gv.addln(gv.start_graph());
		int[][] ag = new int[G.count()][G.count()]; // ag矩阵中存储要高亮的边
		for (int i = 0; i < a.length - 1; i++) {
			ag[a[i]][a[i + 1]] = 1;
		}
		for (int i = 0; i < G.count(); i++) {
			String str = G.point_name()[i];
			for (int j = 0; j < G.count(); j++) {
				if (G.adjacency_matrix()[i][j] > 0) {
					gv.addln(str + " -> " + G.point_name()[j] + "[ color = " + color_set[ag[i][j]] + ", label = "
							+ G.adjacency_matrix()[i][j] + " ];");
				}
			}
		}

		gv.addln(gv.end_graph());
		ByteArrayInputStream in = new ByteArrayInputStream(gv.getGraph(gv.getDotSource(), "jpg"));// byte[]格式转image
		BufferedImage image = ImageIO.read(in);
		int width, height, width1, height1;
		if (image.getWidth() > 600) {
			width = 0;
			width1 = 600;
		} else {
			width = (this.getWidth() - image.getWidth()) / 2;
			width1 = image.getWidth();
		}
		if (image.getHeight() > 570) {
			height = 0;
			height1 = 570;
		} else {
			height = (this.getHeight() - image.getHeight()) / 2;
			height1 = image.getHeight();
		}
		G.image = image;
		g.drawImage(image, width, height, width1, height1, null);

	}

	public BufferedImage Screenshot(int x, int y) {
		try {
			BufferedImage image = new Robot().createScreenCapture(
					new Rectangle(x + 5, y + 66, (int) this.getWidth() - 10, (int) this.getHeight()));
			return image;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("error");
		}
		return null;
	}
}
