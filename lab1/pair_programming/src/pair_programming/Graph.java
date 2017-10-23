package pair_programming;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.regex.*;

public class Graph {
	int[][] a;
	String[] pointname;
	String[][] bridgewordset;
	BufferedImage image = null;

	private int match(String text) {
		if (text == null)
			return -1;
		for (int i = 0; i < pointname.length; i++) {
			if (pointname[i].equals(text.toLowerCase()))
				return i;
		}
		return -1;
	}

	public Graph(String s) { // 根据字符串S，生成有向图
		String s1;
		Pattern p = Pattern.compile("[!?,.\n ]");
		Matcher m = p.matcher(s);
		s1 = m.replaceAll(" ");
		p = Pattern.compile("[^a-zA-Z ]");
		m = p.matcher(s1);
		s1 = m.replaceAll("");
		p = Pattern.compile(" +");
		m = p.matcher(s1);
		s1 = m.replaceAll(" ");
		String[] sset = s1.split(" ");
		ArrayList<String> pointname1 = new ArrayList<String>();
		int[][] b = new int[sset.length][sset.length];
		for (int i = 0; i < sset.length; i++) {
			if (!pointname1.contains(sset[i].toLowerCase()))
				pointname1.add(sset[i].toLowerCase());
			if (i > 0) {
				b[pointname1.indexOf(sset[i - 1].toLowerCase())][pointname1.indexOf(sset[i].toLowerCase())] += 1;
			}
		}
		a = new int[pointname1.size()][pointname1.size()];
		pointname = new String[pointname1.size()];
		bridgewordset = new String[pointname1.size()][pointname1.size()];
		for (int i = 0; i < pointname1.size(); i++) {
			pointname[i] = pointname1.get(i);
			for (int j = 0; j < pointname1.size(); j++) {
				a[i][j] = b[i][j];
			}
		}
		for (int i = 0; i < a.length; i++) {
			for (int k = 0; k < a.length; k++) {
				for (int j = 0; j < a.length; j++) {
					if (bridgewordset[i][j] == null)
						bridgewordset[i][j] = "";
					if (a[i][k] == 1 && a[k][j] == 1)
						if (bridgewordset[i][j] == null)
							bridgewordset[i][j] = "" + pointname[k];
						else
							bridgewordset[i][j] = bridgewordset[i][j] + pointname[k] + " ";
				}
			}
		}
	}

	public String queryBridgeWords(String word1, String word2) {// 桥接词
		int word1order = -1, word2order = -1;
		for (int i = 0; i < a.length; i++) {
			if (pointname[i].equals(word1.toLowerCase()))
				word1order = i;
			if (pointname[i].equals(word2.toLowerCase()))
				word2order = i;
		}
		if (word1order == -1 || word2order == -1)
			return "No " + word1.toLowerCase() + " or " + word2.toLowerCase() + " in the graph!";
		else if (!bridgewordset[word1order][word2order].equals("")) {
			return "The bridge words from word1 to word2 are: " + bridgewordset[word1order][word2order];
		} else
			return "No bridge words from " + word1.toLowerCase() + " to " + word2.toLowerCase() + "!";
	}

	public String generateNewText(String inputText) {// 新文本
		String ans = "";
		String[] str1 = inputText.split(" ");
		String[] str2 = new String[str1.length];
		for (int i = 0; i < str1.length - 1; i++) {
			if (i == str1.length) {
				str2[i] = "";
				break;
			}
			if (match(str1[i]) == -1 || match(str1[i + 1]) == -1)
				str2[i] = "";
			else
				str2[i] = bridgewordset[match(str1[i])][match(str1[i + 1])];
		}
		for (int i = 0; i < str1.length - 1; i++) {
			ans = ans + str1[i] + " " + str2[i] + " ";
		}
		ans = ans + str1[str1.length - 1];
		Pattern p = Pattern.compile(" +");
		Matcher m = p.matcher(ans);
		ans = m.replaceAll(" ");
		return ans;
	}

	public String[] calcShortestPath(String word1, String word2) {// 最短路径
		boolean judge1 = true, judge2 = true;
		if (word1.equals("") || word1 == null)
			judge1 = false;
		if (word2.equals("") || word2 == null)
			judge2 = false;
		int start = -1, end = -1;
		if (!judge1 && !judge2) {
			return new String[] { "INPUT_ERROR" };
		}
		if (judge1 && !judge2) {
			start = match(word1);
			if (start == -1)
				return new String[] { "NOT_FIND" };
		}
		if (!judge1 && judge2) {
			start = match(word2);
			if (start == -1)
				return new String[] { "NOT_FIND" };
		}
		if (judge1 && judge2) {
			start = match(word1);
			end = match(word2);
			if (start == -1 || end == -1)
				return new String[] { "NOT_FIND" };
		}
		int INF = 999999;
		int[][] dis = new int[a.length][a.length];
		int[] distance = new int[a.length];
		point_set[] path = new point_set[a.length];
		point_set[] path2 = new point_set[a.length];
		for (int i = 0; i < a.length; i++) {
			path[i] = new point_set();
			path2[i] = new point_set();
			distance[i] = INF;
			if (i == start)
				distance[i] = 0;
			for (int j = 0; j < a.length; j++) {
				if (i == j) {
					dis[i][j] = INF;
				} else if (a[i][j] == 0)
					dis[i][j] = INF;
				else {
					dis[i][j] = a[i][j];
				}
			}
		}
		Queue<Integer> qu = new LinkedList<Integer>();
		qu.add(start);
		path[start].path.add("" + start);
		while (!qu.isEmpty()) {
			int temp = qu.remove();
			for (int i = 0; i < a.length; i++) {
				if (i == temp)
					continue;
				if (distance[temp] + dis[temp][i] < distance[i]) {
					distance[i] = distance[temp] + dis[temp][i];
					path[i].path.clear();
					for (int j = 0; j < path[temp].path.size(); j++) {
						if (!path[i].path.contains("" + temp))
							path[i].path.add("" + temp);
					}
					qu.add(i);
				}
				if (distance[temp] + dis[temp][i] == distance[i]) {
					if (distance[i] >= INF)
						continue;
					for (int j = 0; j < path[temp].path.size(); j++) {
						if (!path[i].path.contains("" + temp))
							path[i].path.add("" + temp);
					}
				}
			}
		}
		ArrayList<String> ans_temp = new ArrayList<String>();
		if (end == -1) {
			for (int i = 0; i < a.length; i++) {
				findpath(start, i, path, path2);
				ans_temp.addAll(path2[i].path);
			}
		} else {
			findpath(start, end, path, path2);
			ans_temp.addAll(path2[end].path);
		}
		String[] ans = new String[ans_temp.size()];
		for (int i = 0; i < ans_temp.size(); i++) {
			ans[i] = ans_temp.get(i);
		}
		if (ans.length == 0)
			return new String[] { "REACH_ERROR" };
		return ans;
	}

	private void findpath(int start, int temp, point_set[] path, point_set[] path2) {
		if (path2[temp].path.size() != 0)
			return;
		if (temp == start)
			path2[temp].path.add("" + temp);
		else {
			for (String x : path[temp].path) {
				findpath(start, Integer.parseInt(x), path, path2);
				for (String y : path2[Integer.parseInt(x)].path) {
					path2[temp].path.add(y + " " + temp);
				}
			}
		}
	}

	class point_set {
		public ArrayList<String> path;

		public point_set() {
			path = new ArrayList<String>();
		}
	}

	public String randomWalk() {// 随机游走，返回整个游走的字符串
		String ans = "";
		int[][] b = new int[a.length][a[0].length];
		int[] c = new int[a.length];
		for (int i = 0; i < a.length; i++) {
			c[i] = 0;
			for (int j = 0; j < a.length; j++) {
				b[i][j] = 0;
				if (a[i][j] > 0)
					c[i] += 1;
			}
		}
		Random r = new Random();
		int start = r.nextInt(a.length);
		ans = "" + pointname[start];
		int rand = r.nextInt(a.length);
		while (c[start] > 0) {
			if (a[start][rand] > 0 && b[start][rand] != 1) {
				ans = ans + " " + pointname[rand];
				c[start] -= 1;
				b[start][rand] = 1;
				start = rand;
			} else {
				b[start][rand] = 1;
			}
			rand = r.nextInt(a.length);
		}
		return ans;
	}

	public int[][] adjacency_matrix() {// 返回邻接矩阵，矩阵边长与count（）返回值一致
		return a;
	}

	public String[] point_name() {// 返回点名称的数组（与邻接矩阵对应），数组长度与count（）返回值一致
		return pointname;
	}

	public int count() {// 返回点数目
		return a.length;
	}

}
