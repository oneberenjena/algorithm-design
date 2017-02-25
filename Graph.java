import java.util.Iterator;
import java.util.*;
import java.util.NoSuchElementException;



public class Graph {
    private static final String NEWLINE = System.getProperty("line.separator");
    private int V;
    private int E;
    private boolean[][] adj;
    private float[][] profit;
    private float[][] cost;
    private boolean[][] visited;
    private float[][] benefits;
    
    // empty graph with V vertices
    public Graph(int V, int E) {
        if (V < 0) throw new RuntimeException("Number of vertices must be nonnegative");
        this.V = V;
        this.E = E;
        this.adj = new boolean[V][V];
        this.profit = new float[V][V];
        this.cost = new float[V][V];
        this.visited = new boolean[V][V];
        this.benefits = new float[V][V];
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

    public void benefitsMatrix(){
        // float[][] matrix = new float[V][V];
        for (int i = 0; i < V; i++){
            for (int j = 0; j < V; j++){
                benefits[i][j] = profit[i][j] - cost[i][j];
            }
        }
        // return matrix;
    }

    public void visitEdge(int i, int j){
        visited[i][j] = true;
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

    public void printmatrix(){
        for (int i = 0; i<V ; i++) {
            System.out.println("");
            for (int j = 0; j<V  ; j++) {
                System.out.print(benefits[i][j] + "     ");
            }
        }
    }

    public int findMaxVecino(int i){
        float max = -Float.MAX_VALUE;
        float tmpmax = max;
        int tmpj = 0;
        for (int j = 0; j < V; j++ ) {
            if (adj[i][j]){
                max = (visited[i][j]) ? Math.max(max, cost[i][j]):
                                        Math.max(max, benefits[i][j]);
            }
            if (tmpmax != max){
                tmpj = j;
                tmpmax = max;
            }
        }
        return tmpj;
    }

    public boolean adjacent_visited(int i){
        for (int j = 0; j<V; j++) {
            if (!visited[i][j]) {return false;}
        }
        return true;
    }

    public void algoritmode(int di){
        // printmatrix(benefitsMatrix());   

        // Desde el nodo deposito di, voy a buscar el camino
        // de maximo beneficio en el grafo
        benefitsMatrix();
        Map<Integer, Integer> path = new HashMap<>();
        path = resolvePath(di);
    }   

    public int recursionAlgoritmode(int i){
        visited[i][i] = true;
        
        if (!adjacent_visited(i)){
            System.out.println("Caso en el que hay al menos un nodo por visitar");
        } else {
            System.out.println("Caso en el que ya no queda una mierda");
        }

        return 0;
    }

    public Map<Integer,Integer> resolvePath(int i){
        visited[i][i] = true;
        Map<Integer, Integer> path = new HashMap<>();
        if (!adjacent_visited(i)){
            int maxSig = findMaxVecino(i);
            System.out.println(maxSig + " HAHAAAA");
            path.put(i, maxSig);         
            path.putAll(resolvePath(maxSig));
            System.out.println(path);
            return path;
        } else {
            Map<Integer, Integer> nothing = new HashMap<>();
            return nothing;
        }
    }

}