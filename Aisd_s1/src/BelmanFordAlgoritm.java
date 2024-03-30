import java.io.*;
import java.util.Arrays;
import java.util.Random;
public class BelmanFordAlgoritm {
    public static void main(String[] args) {
        Graph graph;
        Random random=new Random();
        try(ObjectInputStream objectInputStream=new ObjectInputStream(
                new FileInputStream("data.bin"))
            ;ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("data.bin"));) {
            for (int i = 0; i < 70; i++) {
                graph=(Graph) objectInputStream.readObject();
                System.out.println("grath"+" "+i+" Кол-во вершин: "+ graph.V+" кол-во ребер: "+" "+graph.E);
                graph.bellmanFord(random.nextInt(graph.V));
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }
}




// Класс для представления ребра графа
class Edge implements Serializable{
    int source, destination, weight;

    Edge(int source, int destination, int weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "source=" + source +
                ", destination=" + destination +
                ", weight=" + weight +
                '}';
    }
}

// Класс для представления графа и выполнения алгоритма Беллмана-Форда
class Graph implements Serializable {
    int V, E; // Количество вершин и рёбер
    Edge[] edges; // Массив рёбер


    // Конструктор для создания графа с V вершинами и E рёбрами
    Graph(int v, int e) {
        V = v;
        E = e;
        edges = new Edge[E];
    }

    @Override
    public String toString() {
        return "Graph{" +
                "V=" + V +
                ", E=" + E +
                ", edges=" + Arrays.toString(edges) +
                '}';
    }

    // Функция для выполнения алгоритма Беллмана-Форда
    void bellmanFord(int src) {
        int[] dist = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;
        int iterations = 0; // Переменная для подсчёта количества итераций

        long startTime = System.nanoTime(); // Замер времени начала работы алгоритма

        // Релаксация всех рёбер V-1 раз
        for (int i = 0; i < V - 1; i++) {
            for (int j = 0; j < E; j++) {
                int u = edges[j].source;
                int v = edges[j].destination;
                int weight = edges[j].weight;
                if (dist[u] != Integer.MAX_VALUE &&
                        dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                }
                iterations++; // Увеличение счётчика итераций
            }
        }

        // Проверка наличия отрицательных циклов
        for (int i = 0; i < E; i++) {
            int u = edges[i].source;
            int v = edges[i].destination;
            int weight = edges[i].weight;
            if (dist[u] != Integer.MAX_VALUE && dist[u] + weight < dist[v]) {
                System.out.println("Граф содержит отрицательный цикл");
                return;
            }
        }

        long endTime = System.nanoTime(); // Замер времени окончания работы алгоритма
        long duration = (endTime - startTime) / 1000000; // Преобразование времени в миллисекунды

        //Вывод результатов
        System.out.println("Расстояния от вершины " + src + ":");
        for (int i = 0; i < V; i++) {
            System.out.println("Вершина " + i + ": " + dist[i]);
        }

        // Выходные данные для таблицы
//        System.out.print(duration +" " );//время
//        System.out.print((iterations+E)+" ");//итерации
//        System.out.print(V*E);//вершины*ребра
//        System.out.println();

    }

}


