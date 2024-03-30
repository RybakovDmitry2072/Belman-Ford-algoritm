import java.io.*;
import java.util.*;

public class RandomWeightedGraph implements Serializable{
    // Функция для создания случайного связного, ориентированного, взвешенного графа
    public static Graph generateRandomGraph(int V, int E, int maxWeight) {
        Random random = new Random();
        Set<String> addedEdges = new HashSet<>(); // Хранение добавленных рёбер

        List<Edge> edges = new ArrayList<>(); // Список рёбер

        // Создание связного графа алгоритмом Прима
        Set<Integer> visited = new HashSet<>();
        PriorityQueue<Edge> minHeap = new PriorityQueue<>(Comparator.comparingInt(edge -> edge.weight));
        int startVertex = random.nextInt(V);
        visited.add(startVertex);
        minHeap.addAll(getAdjacentEdges(startVertex, edges));
        while (!minHeap.isEmpty() && visited.size() < V) {
            Edge edge = minHeap.poll();
            int nextVertex = edge.destination;
            if (!visited.contains(nextVertex)) {
                visited.add(nextVertex);
                edges.add(edge);
                minHeap.addAll(getAdjacentEdges(nextVertex, edges));
            }
        }

        // Добавление дополнительных рёбер для каждой вершины
        for (int v = 0; v < V; v++) {
            List<Edge> outgoingEdges = getOutgoingEdges(v, edges);
            if (outgoingEdges.isEmpty()) {
                int destination = random.nextInt(V);
                while (destination == v) {
                    destination = random.nextInt(V);
                }
                String edgeKey = v + "-" + destination;
                if (!addedEdges.contains(edgeKey)) {
                    int weight = random.nextInt(maxWeight + 1) - 30;
                    edges.add(new Edge(v, destination, weight));
                    addedEdges.add(edgeKey);
                }
            }

            List<Edge> incomingEdges = getIncomingEdges(v, edges);
            if (incomingEdges.isEmpty()) {
                int source = random.nextInt(V);
                while (source == v) {
                    source = random.nextInt(V);
                }
                String edgeKey = source + "-" + v;
                if (!addedEdges.contains(edgeKey)) {
                    int weight = random.nextInt(maxWeight + 1) - 30;
                    edges.add(new Edge(source, v, weight));
                    addedEdges.add(edgeKey);
                }
            }
        }

        // Создание объекта Graph из списка рёбер
        Graph graph = new Graph(V, edges.size());
        graph.edges = edges.toArray(new Edge[0]);
        return graph;
    }

    private static List<Edge> getAdjacentEdges(int vertex, List<Edge> edges) {
        List<Edge> adjacentEdges = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.source == vertex || edge.destination == vertex) {
                adjacentEdges.add(edge);
            }
        }
        return adjacentEdges;
    }

    private static List<Edge> getOutgoingEdges(int vertex, List<Edge> edges) {
        List<Edge> outgoingEdges = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.source == vertex) {
                outgoingEdges.add(edge);
            }
        }
        return outgoingEdges;
    }

    private static List<Edge> getIncomingEdges(int vertex, List<Edge> edges) {
        List<Edge> incomingEdges = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.destination == vertex) {
                incomingEdges.add(edge);
            }
        }
        return incomingEdges;
    }


    public static void main(String[] args) {
        Random random=new Random();
        try(ObjectOutputStream outputStream=new ObjectOutputStream(new FileOutputStream("data.bin"))) {
            for (int i = 0; i < 70; i++) {
                int V = random.nextInt(10000)+100; // Количество вершин
                int E = random.nextInt(V*(V-1))+V; // Количество рёбер
                int maxWeight = 130; // Максимальный вес рёбер
                Graph randomGraph = generateRandomGraph(V, E, maxWeight);
                outputStream.writeObject(randomGraph);
                System.out.println("Grath" + i);
                for (Edge edge : randomGraph.edges) {

                   System.out.println("Источник: " + edge.source + " Назначение: " + edge.destination + " Вес: " + edge.weight);
                }
                System.out.println();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
