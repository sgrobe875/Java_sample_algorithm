// Sarah Grobe
// CS 224
// Homework 6 Programming
// Prim's Algorithm

import java.util.Scanner;
import java.io.*;


public class PrimMST 
{
   // To start, create the nodes and edges of the graph as class level variables
   
   //         index numbers:    0   1   2   3   4   5   6   7
   static char [] nodeNames = {'a','b','c','d','e','f','g','h'};
   static Node [] nodes = new Node[nodeNames.length];
   static Node [] Q = new Node[nodeNames.length];
  
   

   
   // arryas of corresponding edge weights (in same order as above)
   static int [] aWts = {0, 9, 14, 15};
   static int [] bWts = {0, 9, 24};
   static int [] cWts = {0, 24, 6, 2, 18, 19};
   static int [] dWts = {0, 6, 11, 6};
   static int [] eWts = {0, 2, 11, 30, 20, 16};
   static int [] fWts = {0, 14, 18, 30, 5};
   static int [] gWts = {0, 15, 20, 5, 44};
   static int [] hWts = {0, 19, 6, 16, 44};
   
   // array of the arrays of weights (in alphabetical order)
   static int [][] wts = {aWts, bWts, cWts, dWts, eWts, fWts, gWts, hWts};
   
   
   // array of edges
   // Note: all edges appear in this array twice in each order of the nodes for ease of searching
   // e.g. the edge "ad" is in the array, as well as the edge "da"
   static Edge [] edges = new Edge[30];
   
  
      
   public static void main(String[] args) 
   {
      // construct Nodes and Q
      BuildPQ();
      
      // initialize graph's adjacency list 
      // list of adjacent nodes for each node, starting with the node itself
      Node [] aAdj = {nodes[0], nodes[1], nodes[5], nodes[6]};
      Node [] bAdj = {nodes[1], nodes[0], nodes[2]};
      Node [] cAdj = {nodes[2], nodes[1], nodes[3], nodes[4], nodes[5], nodes[7]};
      Node [] dAdj = {nodes[3], nodes[2], nodes[4], nodes[7]};
      Node [] eAdj = {nodes[4], nodes[2], nodes[3], nodes[5], nodes[6], nodes[7]};
      Node [] fAdj = {nodes[5], nodes[0], nodes[2], nodes[4], nodes[6]};
      Node [] gAdj = {nodes[6], nodes[0], nodes[4], nodes[5], nodes[7]};
      Node [] hAdj = {nodes[7], nodes[2], nodes[3], nodes[4], nodes[6]};
      
      
      // array of the adjacency arrays (in alphabetical order)
      Node [][] adjs = {aAdj, bAdj, cAdj, dAdj, eAdj, fAdj, gAdj, hAdj};
      
      
      // loop through all nodes and assign adjacency arrays accordingly
      for (int i = 0; i < nodes.length; ++i)
      {
         nodes[i].setAdj(adjs[i]);
      }

      
      // create edges using end nodes and corresponding weights
      int edgeCount = 0;
      for (int i = 0; i < adjs.length; ++i)
      {
         for (int j = 1; j < adjs[i].length; ++j)
         {
            edges[edgeCount] = new Edge(adjs[i][0], adjs[i][j], wts[i][j]);
            ++edgeCount;
         }  
      }
      
      
      // give data structure for graph, and print
      System.out.println("The graph is represented as an adjacency list:");
      for (int i = 0; i < nodes.length; ++i)
      {
         System.out.print(nodeNames[i] + ": ");
         for (int j = 1; j < nodes[i].getAdj().length; ++j)
         {
            System.out.print(nodes[i].getAdj()[j].getName());
            
            if (j < nodes[i].getAdj().length - 1)
            {
               System.out.print(", ");
            }
         }
         System.out.println();
      }

      System.out.println();
          
          
          
      // obtain user input for start node  
      char startNodeChar;
      
      // loop through until an accepted character is submitted
      do {
         Scanner input = new Scanner(System.in);
         System.out.print("Enter start node: ");
         startNodeChar = input.nextLine().charAt(0);
      } while (startNodeChar != 'a' && startNodeChar != 'b' && startNodeChar != 'c' && startNodeChar != 'd' &&
               startNodeChar != 'e' && startNodeChar != 'f' && startNodeChar != 'g' && startNodeChar != 'h');
      
      
      
      Node startNode = new Node('j');   // initialize to avoid error; this will never be used 
      
      // loop through nodeNames to match the inputted character to its corresponding node
      for (int i = 0; i < nodeNames.length; ++i)
      {
         if (nodeNames[i] == startNodeChar)
         {
            startNode = nodes[i];
            i = nodeNames.length;   // drop out of the loop once found
         }
      }  
      
      // initialize S and T
      Node [] S = new Node [nodes.length];      // n nodes
      Edge [] T = new Edge [nodes.length-1];    // n-1 edges
      
      // find startNode's location in Q, set pi to 0
      for (int i = 0; i < Q.length; ++i)
      {
         if (Q[i].equals(startNode))
         {
            Q[i].setPi(0);
            i = Q.length;    // drop out of loop
         }
      }
      
      // S and T counters to help with insertion
      int SCounter = 0;
      int TCounter = 0;
      Node v = startNode;   // by default
      
      
      
      // print results before first iteration
      
      // contents of S
      System.out.print("S = {");
      for (int i = 0; i < S.length; ++i)
      {
         if (S[i] != null)
         {
            System.out.print(S[i].getName() + " ");
         }
      }
      System.out.println("}");
      
      // contents of T
      System.out.print("T = {");
      for (int i = 0; i < T.length; ++i)
      {
         if (S[i] != null)
         {
            System.out.print(T[i].getName() + " ");
         }
      }
      System.out.println("}");
      
      // contents of Q
      System.out.print("Q = {");
      for (int i = 0; i < Q.length; ++i)
      {
         if (Q[i] != null)
         {
            System.out.print("(" + Q[i].getName() + ", ");
            if (Q[i].getPi() == 1000)
            {
               System.out.print("inf) ");
            }
            else
            {
               System.out.print(Q[i].getPi() + ") ");
            }
         }
      }
      System.out.println("}");
      
      
      
      boolean QnotNull = true;
      while (QnotNull == true)
      {
         v = ExtractMin(Q);   // find min value from Q
         S[SCounter] = v;     // insert to S
         
         
         if (!startNode.equals(v))
         {  
            String eName = Character.toString(v.getPred().getName()) + Character.toString(v.getName());
            
            // find the edge corresponding to v and the node that came before
            for (int i = 0; i < edges.length; ++i)
            {
               if (eName.equals(edges[i].getName()))
               {
                  T[TCounter] = edges[i];   // add edge to T
                  ++TCounter;
                  i = edges.length;   // fall out of the loop
               }
            }
         }
         
         // find the adjacency list that matches v
         for (int i = 0; i < adjs.length; ++i)
         {
            char c = adjs[i][0].getName();
            if (v.getName() == c)
            {
               // loop through all elements of that adjacency list
               for (int j = 1; j < adjs[i].length; ++j)
               {
                  // check if any nodes adjacent to v are already within S
                  int k;
                  
                  // loop thru contents of S
                  for (k = 0; k <= SCounter; ++k)
                  {
                     // loop thru adjacent nodes
                     for (int l = 1; l < v.getAdj().length; ++l)
                     {
                        // if not equal:
                        if (v.getAdj()[l] != S[k])
                        {
                           Node w = new Node('j');    // dummy node to initialize & avoid error
                           for (int m = 0; m < nodes.length; ++m)
                           {
                              // find the Node corresponding to the other end of the edge
                              if (v.getAdj()[l].equals(nodes[m]))
                              {
                                 w = nodes[m];
                                 m = nodes.length;    // drop out of the loop
                              }
                           }
                           // find the edge made by v and w
                           String edgeName = Character.toString(v.getName()) + Character.toString(w.getName());
                           Edge currentEdge = new Edge();
                           for (int n = 0; n < edges.length; ++n)
                           {
                              if (edgeName.equals(edges[n].getName()))
                              {
                                 currentEdge = edges[n];
                                 n = edges.length;    // drop out of loop
                              }
                           }
                           // if cost is less than pi of the node
                           if ((v.getPi() + currentEdge.getWeight()) < w.getPi() || w.getPi() == 0)       
                           {
                              // update the node and Q accordingly  
                              w.setPred(v);
                              w.setPi(currentEdge.getWeight() + v.getPi());
                              ChangeKey(Q, w, w.getPi());
                           } 
                        }   
                     }
                  }   
               }
            }
         }
         
         ++SCounter;
         
         
         
         // print results after each iteration
      
         // contents of S
         System.out.println();
         System.out.print("S = {");
         for (int i = 0; i < S.length; ++i)
         {
            if (S[i] != null)
            {
               System.out.print(S[i].getName() + " ");
            }
         }
         System.out.println("}");
         
         // contents of T
         System.out.print("T = {");
         for (int i = 0; i < T.length; ++i)
         {
            if (T[i] != null)
            {
               System.out.print(T[i].getName() + " ");
            }
         }
         System.out.println("}");
         
         // contents of Q
         System.out.print("Q = {");
         for (int i = 0; i < Q.length; ++i)
         {
            if (Q[i] != null)
            {
               System.out.print("(" + Q[i].getName() + ", ");
               if (Q[i].getPi() == 1000)
               {
                  System.out.print("inf) ");
               }
               else
               {
                  System.out.print(Q[i].getPi() + ") ");
               }
            }
         }
         System.out.println("}");
         
         // check that Q is not empty
         
         // assume empty, if one non-null value is found, then mark as true
         QnotNull = false;
         for (int z = 0; z < Q.length; ++z)
         {
            if (Q[z] != null)
               QnotNull = true;
         }
      }    
   }
   
   // method for constructing Nodes and populating Q
   public static void BuildPQ()
   {
      for (int i = 0; i < nodeNames.length; ++i)
      {
         nodes[i] = new Node(nodeNames[i]);
         nodes[i].setPi(1000);     // use 1000 to represent infinity
         
         Q[i] = nodes[i];
      }
   }
   
   public static Node ExtractMin(Node [] Q)
   {
      // set minNode to first non-null element by default
      Node minNode = Q[0]; // this line will initialize and avoid errors      
      for (int i = 0; i < Q.length; ++i)
      {
         if (Q[i] != null)
         {
            minNode = Q[i];
            i = Q.length;   // drop out of loop once one is found
         }
      }
      
      
      // loop through Q to find the minimum
      for (int i = 0; i < Q.length; ++i)
      {
         // skip over any null values
         if (Q[i] == null)
         {
         }
         
         else if (Q[i].getPi() < minNode.getPi())
            minNode = Q[i];
      }

      // remove minimum node from Q   
      for (int i = 0; i < Q.length; ++i)
      {
         // ignore null values
         if (Q[i] == null)
         {
         }
         
         // change to null once located
         else if (Q[i].equals(minNode))
         {
            Q[i] = null;
            i = Q.length;
         }
      } 
      return minNode;
   }
   
   // method for updating Q by looping through nodes adjacent to node w
   public static void ChangeKey(Node [] Q, Node w, int pi)
   {
      // loop through nodes adjacent to w
      Node [] a = w.getAdj();
      String edgeName;
      int edgeLen = 0;
      for (int i = 1; i < a.length; ++i)
      {
         edgeName = Character.toString(w.getName()) + Character.toString(a[i].getName());
         
         for (int j = 0; j < edges.length; ++j)
         {
            // once edge is located
            if (edgeName.equals(edges[j].getName()))
            {
               // calculate the new weight
               edgeLen = pi + edges[j].getWeight();
               j = edges.length;
               
               // if this weight is lower than current value, update it
               if (edgeLen < a[i].getPi())
               {
                  a[i].setPi(edgeLen);
                  a[i].setPred(w);
               }
            }
         }
      }            
   }
}

/* the Node class represents each node in the graph
   each node has a name, piValue, previous Node (pred), and array
   of adjacent nodes  */
class Node
{
   // attributes
   char name;
   int piValue;
   Node pred;
   Node [] adjacents;
   
   // constructor
   public Node(char c)
   {
      name = c;
   }
   
   // setters
   public void setPi(int n)
   {
      piValue = n;
   }
   
   public void setPred(Node n)
   {
      pred = n;
   }
   
   public void setAdj(Node [] a)
   {
      adjacents = a;
   }
   
   // getters
   public char getName()
   {
      return name;
   }
   
   public int getPi()
   {
      return piValue;
   }
   
   public Node getPred()
   {
      return pred;
   }
   
   public Node[] getAdj()
   {
      return adjacents;
   }
   
   // equality method for two Nodes
   public boolean equals(Node n)
   {
      return name == n.getName();
   }
}

/* the Edge class represents each edge in the graph
   each edge consists of an array of its two end Nodes, its name (the two end nodes as a string),
   and an integer weight value  */
class Edge
{
   // attributes
   Node [] endNodes = new Node [2];
   String edgeName;
   int weight;
   
   // constructors
   // this is the only constructor that will be used in practice
   public Edge(Node n1, Node n2, int w)
   {
      endNodes[0] = n1;
      endNodes[1] = n2;
      
      weight = w;
      
      edgeName = Character.toString(endNodes[0].getName()) + Character.toString(endNodes[1].getName());
   }
   
   // this constructor is used to create dummy variables, and is only used to avoid errors
   public Edge()
   {
      
   }
   
   // getters
   public int getWeight()
   {
      return weight;
   }
   
   public String getName()
   {
      return edgeName;
   }
   
   // equals method for two Edges
   public boolean equals(Edge e)
   {
      return edgeName.equals(e.getName());
   }
}
