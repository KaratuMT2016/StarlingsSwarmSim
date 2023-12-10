/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author musak
 */
public class Graph {
    
    private int V;   // No. of vertices
    private LinkedList<Boid> adj[]; //Adjacency Lists
 
    // Constructor
    Graph(int v)
    {
        V = v;
        adj = new LinkedList[v];
        for (int i=0; i<v; ++i)
            adj[i] = new LinkedList();
    }
 
    // Function to add an edge into the graph
    void addEdge(Boid v, Boid w)
    {
        adj[v.boidID].add(w);
    }
 
    // prints BFS traversal from a given source s
    List<Boid> BFS(Boid s)
    {
        // Mark all the vertices as not visited(By default
        // set as false)
        //boolean visited[] = new boolean[V];
        List<Boid> connectedBoids = new ArrayList<>();
 
        // Create a queue for BFS
        LinkedList<Integer> queue = new LinkedList<Integer>();
 
        // Mark the current node as visited and enqueue it
        //visited[s]=true;
        s.visited = true;
        connectedBoids.add(s);
        queue.add(s.boidID);
 
        while (queue.size() != 0)
        {
            // Dequeue a vertex from queue and print it
            Integer boidID = queue.poll();
            //System.out.print(boidID+" ");
 
            // Get all adjacent vertices of the dequeued vertex s
            // If a adjacent has not been visited, then mark it
            // visited and enqueue it
            Iterator<Boid> i = adj[boidID].listIterator();
            while (i.hasNext())
            {
                Boid boid = i.next();
                if (!boid.visited)
                {
                    //visited[n] = true;
                    boid.visited = true;
                    connectedBoids.add(boid);
                    queue.add(boid.boidID);
                }
            }
        }
        
        return connectedBoids;
    }
    
    // Driver method to
    public static void main(String args[])
    {
        Graph g = new Graph(100);
        
        Boid b1 = new Boid(null, 0, 0, 0, 1);
        Boid b2 = new Boid(null, 0, 0, 0, 2);
        Boid b3 = new Boid(null, 0, 0, 0, 3);
        Boid b4 = new Boid(null, 0, 0, 0, 4);
 
        g.addEdge(b1, b2);
        g.addEdge(b2, b1);
        g.addEdge(b3, b4);
        g.addEdge(b4, b3);
 
        System.out.println("Following is Breadth First Traversal");
 
        g.BFS(b3);
        System.out.println("b1.visited: " + b1.visited);
        System.out.println("b2.visited: " + b2.visited);
        System.out.println("b3.visited: " + b3.visited);
        System.out.println("b4.visited: " + b4.visited);
    }
}
