import java.util.Iterator;
import java.util.NoSuchElementException;


public class Graph {
    private static final String NEWLINE = System.getProperty("line.separator");
    private int V;
    private int E;
    private boolean[][] adj;
    private float[][] profit;
    private float[][] cost;
    private boolean[][] visited;
    
    // empty graph with V vertices
    public Graph(int V, int E) {
        if (V < 0) throw new RuntimeException("Number of vertices must be nonnegative");
        this.V = V;
        this.E = E;
        this.adj = new boolean[V][V];
        this.profit = new float[V][V];
        this.cost = new float[V][V];
    }

    // number of vertices and edges
    public int V() { return V; }
    public int E() { return E; }

    public boolean[][] gAdj() { return adj; }

    // add undirected edge v-w
    public void addEdge(int v, int w, float p, float c) {
        if (!adj[v][w]) E++;
        adj[v][w] = true;
        adj[w][v] = true;
        addEdgeProfit(v, w, p);
        addEdgeCost(v, w, c);
    }

    public void addEdgeProfit(int v, int w, float p){
        profit[v][w] = p;
        profit[w][v] = p;
    }

    public void addEdgeCost(int v, int w, float c){
        cost[v][w] = c;
        cost[w][v] = c;
    }

    // does the graph contain the edge v-w?
    public boolean contains(int v, int w) {
        return adj[v][w];
    }   

    // return list of neighbors of v
    public Iterable<Integer> adj(int v) {
        return new AdjIterator(v);
    }

    // support iteration over graph vertices
    private class AdjIterator implements Iterator<Integer>, Iterable<Integer> {
        private int v;
        private int w = 0;

        AdjIterator(int v) {
            this.v = v;
        }

        public Iterator<Integer> iterator() {
            return this;
        }

        public boolean hasNext() {
            while (w < V) {
                if (adj[v][w]) return true;
                w++;
            }
            return false;
        }

        public Integer next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return w++;
        }

        public void remove()  {
            throw new UnsupportedOperationException();
        }
    }

    // string representation of Graph - takes quadratic time
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(V + "  " + E + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            for (int w : adj(v)) {
                s.append(w + " ");
            }
            s.append(NEWLINE);
        }
        s.append(NEWLINE);
        s.append(" Edge     Profit  Cost" + NEWLINE);
        for (int v = 0; v < V; v++){
            for (int w : adj(v)) {
                if (contains(v,w)){
                    s.append((v+1) + " <-> " + (w+1) + "    " + profit[v][w] + "    " + cost[v][w]);
                    s.append(NEWLINE);
                }
            }
        }
        return s.toString();
    }

    


}