import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args){	
		Graph G = buildGraph();
	}

	public static Graph buildGraph(){
		File test=new File("test1");
		try{
	        Scanner sc = new Scanner(test);
	        int V = sc.nextInt();
	        Graph G = new Graph(V);
	        int w, v, p, c;
	        while(sc.hasNextInt()){
	        	v = sc.nextInt();
	        	w = sc.nextInt();
	        	p = sc.nextInt();
	        	c = sc.nextInt();	        	
	        	G.addEdge(v, w, p, c);
	        }
	        sc.close();
	        System.out.println(G);
	        return G;
		}catch (FileNotFoundException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
}