import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Main {
	public static void main(String[] args){	
		Graph G = buildGraph(args[0]);
		G.runResolvePath(0);
	}

	public static Graph buildGraph(String arg){
		File test=new File(arg);
		try{
	        Scanner sc = new Scanner(test);
	        int V = sc.nextInt();
	        int E = sc.nextInt();
	        Graph G = new Graph(V, E);
	        int w, v, p, c;
	        while(sc.hasNextInt()){
	        	v = sc.nextInt();
	        	w = sc.nextInt();
	        	c = sc.nextInt();
	        	p = sc.nextInt();
	        	G.addEdge(v-1, w-1, p, c);
	        }  
	        sc.close();

	        return G;

		}catch (FileNotFoundException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
}