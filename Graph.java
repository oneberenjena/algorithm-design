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
    private int optimalValue;
    
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
        this.optimalValue = 0;
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
        for (int i = 0; i < V; i++){
            for (int j = 0; j < V; j++){
                benefits[i][j] = profit[i][j] - cost[i][j];
            }
        }
    }

    public void visitEdge(int i, int j){
        visited[i][j] = true;
    }

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
                System.out.print(benefits[i][j] + "      ");
            }
        }
    }

    // Funcion que retorna el indice del nodo vecino cuyo
    // camino sea el de mayor beneficio
    public int findMaxVecino(int i){
        float max = -Float.MAX_VALUE;
        float tmpmax = max;
        int tmpj = 0;
        for (int j : adj(i)){
            max = Math.max(max, benefits[i][j]);
            if (tmpmax != max){
                tmpj = j;
                tmpmax = max;
            }
        }
        return tmpj;
    }

    // public boolean adjacent_visited(int i){
    //     for (int j : adj(i) ) {
    //         if (!visited[i][j] && i != j) {return false;}
    //     }
    //     return true;
    // }

    // Funcion que retorna true si los caminos pertenecen al
    // conjunto P, es decir, aristas cuyo beneficio es negativo
    // retorna falso de existir alguna arista de costo positivo
    public boolean adjacentP_edges(int i){
        for (int j : adj(i) ) {
            if (benefits[i][j] >= 0) {return false;}
        }
        return true;
    }

    // Funcion que retorna true si todos los nodos vecinos
    // han sido visitados. De existir alguno que no haya
    // sido visitado retorna false
    public boolean adjacent_visitedVertex(int i){
        for (int j : adj(i)){
            if (!visited[j][j]) {return false;}
        }
        return true;
    }

    // Funcion usada al cruzar una arista por primera vez,
    // actualiza el beneficio de una arista a su costo
    public void actMatrix(int i, int j){
        benefits[i][j] = -cost[i][j];
        benefits[j][i] = -cost[i][j];
    }

    // Funcion que actualiza el valor optimo del camino
    // al deposito por el ciclo
    public void actOptimalValue(int i, int j){
        optimalValue += benefits[i][j];
    }

    // Funcion para cruzar aristas utilizada en el primer
    // algoritmo. Permite visitar los caminos, actualiza
    // el valor optimo y actualiza la matriz
    public void crossEdge1(int i, int j){
        visited[i][j] = true;
        visited[j][i] = true;
        actOptimalValue(i,j);
        actMatrix(i, j);
    }

    // Funcion para cruzar aristas utilizada en el segundo
    // algoritmo. Difieren en que solo marca como visitado
    // un camino si no ha sido visitado, de lo contrario
    // solo actualiza el valor optimo del camino
    public void crossEdge2(int i, int j){
        if (!visited[i][j]){
            visited[i][j] = true;
            visited[j][i] = true;
        }
        actOptimalValue(i,j);
    }

    // Funcion utilizada para marcar un nodo como visitado
    public void visitVertex(int i){
        visited[i][i] = true;
    }

    // Funcion principal del algoritmo que determina el ciclo
    // de costo maximo que pasa por el deposito
    public void runResolvePath(int di){
        benefitsMatrix();   // Se determina la matriz de beneficios
        Map<Integer, Integer> path1 = new HashMap<>();
        Map<Integer, Integer> path2 = new HashMap<>();
        path1 = resolvePath(di);  
        System.out.println(path1);
        path2 = minCostPath(2,di); //cableado, necesito el ultimo nodo visitado por el primer algoritmo
    }   

    // Primer algoritmo para calcular el camino de maximo beneficio
    // partiendo del deposito, retorna un diccionario con el par nodo
    // de salida y llegada.
    public Map<Integer,Integer> resolvePath(int i){        
        // El nodo entrante se marca como visitado y se inicializa el camino
        visitVertex(i);
        System.out.println("Visited vertex " + i);
        Map<Integer, Integer> path = new HashMap<>();
        // Si no hay nodos vecinos por visitar y todas las aristas son de costo negativo,
        // finaliza la recursion
        if (adjacent_visitedVertex(i) && adjacentP_edges(i)){
            Map<Integer, Integer> nothing = new HashMap<>();
            System.out.println("nothing else, all bad edges");
            return nothing;
        } else {
            // Se encuentra el nodo vecino de maximo beneficio, se marca el camino del
            // nodo origen al vecino encontrado y se realiza la recursion con este nuevo nodo
            int maxSig = findMaxVecino(i);
            System.out.println("Found maxSig: " + maxSig);
            System.out.println("Benefit for maxSig is: " + benefits[i][maxSig] + "\n");
            path.put(i, maxSig);         
            crossEdge1(i, maxSig);
            path.putAll(resolvePath(maxSig));
            return path;
        }
    }

    // Estoy pegado con esta mierda
    public Map<Integer,Integer> minCostPath(int i, int j){
        System.out.println("");
        System.out.println("Starting from vertex " + i);
        int maxSig = findMaxVecino(i);
        System.out.println("Detected min weight edge " + maxSig);
        Map<Integer,Integer> minPath = new HashMap<>();
        if (contains(i,j) ){
            System.out.println("Detected deposit on sight");
            crossEdge2(i,j);
            minPath.put(i,j);
            return minPath;
        } else {
            System.out.println("Crossing edge to next vertex");
            crossEdge2(i,maxSig);
            minPath.putAll(minCostPath(maxSig, j));
            return minPath;
        }
    }
}