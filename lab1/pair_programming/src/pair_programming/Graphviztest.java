package pair_programming;

import java.io.File;

public class Graphviztest {

	private void start() {
		Graphviz gv = new Graphviz();
		gv.addln(gv.start_graph());
		gv.addln("A -> B;");
		gv.addln("A -> C;");
		gv.addln(gv.end_graph());
		System.out.println(gv.getDotSource());

		String type = "gif";
		File out = new File("D:/out." + type); // Windows
		gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
	}

	/**
	 * Read the DOT source from a file, convert to image and store the image in
	 * the file system.
	 */
	private void start2() {
		String input = "D:/simple.dot"; // Windows

		Graphviz gv = new Graphviz();
		gv.readSource(input);
		System.out.println(gv.getDotSource());

		String type = "gif";
		File out = new File("D:/simple." + type); // Windows
		gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
	}

}
