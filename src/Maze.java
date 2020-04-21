import sun.plugin.javascript.navig.Array;

import java.util.Arrays;
import java.util.Random;

public class Maze {

    private int MAZE_WIDTH;
    private int MAZE_HEIGHT;

    MazeCell[][] maze;// Cells Map
    private Player player;
    public enum MazeCell{EMPTY,WALL,PLAYER,PATH}

    public Maze(int height, int width) {
        MAZE_HEIGHT = (height - 1) / 2;
        MAZE_WIDTH = (width - 1) / 2;


        //generate the maze
        generateMaze(generateAdjacencyMatrix());

    }

    public int getWidth() {
        return MAZE_WIDTH*2+1;
    }

    public int getHeight() {
        return MAZE_HEIGHT*2+1;
    }

    private void generateMaze(byte [][] matrix) {
        maze = new MazeCell[MAZE_HEIGHT * 2 + 1][MAZE_WIDTH * 2 + 1];
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if ((i % 2 == 0 && j % 2 == 0) || i==0 || j==0 || i == maze.length -1 || j == maze[0].length -1) {
                    maze[i][j] = MazeCell.WALL;
                } else if (i % 2 == 1 && j % 2 == 1) {
                    maze[i][j] = MazeCell.EMPTY;
                } else {
                    if (i % 2 == 1) {
                        int index1 = ((((i + 1) / 2) - 1) * (MAZE_WIDTH) + (j / 2 - 1));
                        int index2 = index1 + 1;
                        maze[i][j] = matrix[index1][index2] != 1 ? MazeCell.WALL : MazeCell.EMPTY;
                    } else {
                        maze[i][j] = matrix[( ( (i/2)-1 ) * MAZE_WIDTH + ((((j+1)/2)-1)) )][( ( (i/2)-1 ) * MAZE_WIDTH + ((((j+1)/2)-1)) ) + MAZE_WIDTH] != 1 ? MazeCell.WALL : MazeCell.EMPTY;
                    }
                }

            }

        }
        matrix = null;
        //Open start and exit blocks
        maze[0][1] = MazeCell.EMPTY;
//        maze[maze.length - 1][maze[0].length - 2] = MazeCell.EMPTY;
        //Place player
        maze[maze.length - 1][maze[0].length - 2] = MazeCell.PLAYER;
        player = new Player(maze[0].length - 2, maze.length - 1);

    }

    private byte [][] generateAdjacencyMatrix() {

        byte[][] matrix;// Adjacency matrix
        matrix = new byte[MAZE_HEIGHT * MAZE_WIDTH][MAZE_HEIGHT * MAZE_WIDTH];
        //initialize the adjacency matrix
        for (int i = 0; i < matrix.length; i++) {
            Arrays.fill(matrix[i], Byte.MAX_VALUE);
            matrix[i][i] = 0;
        }

        //set random weights on neighbour nodes to generate the graph
        Random r = new Random();
        for (int i = 0; i < matrix.length; i++) {
            // Right neighbour if not on right edge
            if ((i+1) % MAZE_WIDTH != 0)
                matrix[i][i + 1] = (byte) (r.nextInt(100)+2);
            // Left neighbour if not on left edge
            if (i % MAZE_WIDTH != 0)
                matrix[i][i - 1] = matrix[i - 1][i];
            // Top neighbour if Not at the top row
            if (i - MAZE_WIDTH >= 0)
                matrix[i][i - MAZE_WIDTH] = matrix[i - MAZE_WIDTH][i];
            // Bottom neighbour if not at the bottom row
            if (i + MAZE_WIDTH < MAZE_HEIGHT * MAZE_WIDTH)
                matrix[i][i + MAZE_WIDTH] =(byte) (r.nextInt(100)+2);
        }
        // Find minimum spanning tree for the graph
        prim(matrix);
        //Eliminate nodes not in minimum spanning tree
        removeExtraEdges(matrix);
        return matrix;
    }

    private static void removeExtraEdges(byte [][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[i][j]!=0 && matrix[i][j]!=1)
                    matrix[i][j] = Byte.MAX_VALUE;
            }
        }
    }


    private static void printMatrix(byte [][] matrix) {
        System.out.print("      ");
        for (int i = 0; i < matrix.length; i++) {
            System.out.printf("%6d", i);
        }
        System.out.println("");
        for (int i = 0; i < matrix.length; i++) {
            System.out.printf("%4d:  [ ",i);
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.printf( "%4s, ",matrix[i][j] == Byte.MAX_VALUE ? "" : String.valueOf(matrix[i][j]));
            }
            System.out.println(" ]");
        }
    }

    public void setPlayerPosition(int x, int y) {
        if (x > maze[0].length || y > maze.length || x < 0 || y < 0) {
            System.out.println("Position error");
            return;
        }
        if (maze[y][x] != MazeCell.WALL) {
            maze[y][x] = MazeCell.PLAYER;
            maze[player.getY()][player.getX()] = MazeCell.EMPTY;
            player.setPosition(x,y);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void displayMaze() {
        for (int i = 0; i < maze.length; i++) {
            if(maze.length % 2 == 0 && i == maze.length - 2)
                continue;
            for (int j = 0; j < maze[0].length; j++) {
                System.out.print(maze[i][j] == MazeCell.EMPTY ? "  " : "\u2588\u2588");
            }
            System.out.println();
        }
    }

    public void colorCell(int x, int y) {
        maze[y][x] = MazeCell.PATH;
    }

    private void prim(byte [][] matrix) {
        byte [] cost = Arrays.copyOf(matrix[0],matrix[0].length);
        int [] through = new int[matrix.length];
        boolean [] added = new boolean[matrix.length];

        Arrays.fill(added,false);
        added[0] = true;
        Arrays.fill(through,0);
        for (int i = 0; i < matrix.length-1; i++) {
            //find min weight in cost that is not added yet
            int minNode=1,min;
            while (added[minNode]) minNode++;
            min = cost[minNode];
            for (int j = 1; j < cost.length; j++) {
                if (!added[j] && cost[j] < min && cost[j]!=0) {
                    min = cost[j];
                    minNode = j;
                }
            }
            //add node to added
            added[minNode] = true;
            matrix[through[minNode]][minNode] = 1;
            matrix[minNode][through[minNode]] = 1;
            //update cost and through
            for (int j = 0; j < cost.length; j++) {
                if ( matrix[minNode][j]!=0 && matrix[minNode][j] < cost[j]) {
                    cost[j] = matrix[minNode][j];
                    through[j] = minNode;
                }
            }

        }
    }

    public void djikstra(int s, int des) {

        System.out.println("Running dijkstra from: "+s+" To: "+des);
//      Construct adjacency matrix
        byte [][] c = new byte[maze.length * maze[0].length ][maze.length * maze[0].length ];

        for (int i = 0; i < maze.length * maze[0].length; i++)
            Arrays.fill(c[i], Byte.MAX_VALUE);

        for (int i = 0; i < maze.length * maze[0].length; i++) {
            if (maze[i / maze[0].length][i % maze[0].length] == MazeCell.WALL)
                continue;
            for (int j = 0; j < maze.length * maze[0].length; j++) {
                if (maze[j / maze[0].length][j % maze[0].length] == MazeCell.WALL)
                    continue;
                else if (j == i)
                    c[i][j] = 0;
//                else if (j == i + dimension || j == i - dimension || (j == i + 1 && !(j % dimension == 0)) || (j == i - 1 && !(j % dimension == dimension - 1)) || (j == i - dimension + 1 && !(j % dimension == 0)) || (j == i - dimension - 1 && !(j % dimension == dimension - 1)) || (j == i + dimension +1 && !(j % dimension == 0 )) || (j == i + dimension - 1 && !(j % dimension == dimension - 1)))
                else if (j == i + maze[0].length || j == i - maze[0].length || (j == i + 1 && !(j % maze[0].length == 0)) || (j == i - 1 && !(j % maze[0].length == maze[0].length - 1)))
                    c[i][j] = 1;
            }
        }

        //Run dijkstra
//        printMatrix(c);
        short[] cost = new short[c[0].length];//Arrays.copyOf(c[s],c[s].length);
        short [] through = new short[c.length];
        boolean [] added = new boolean[c.length];

        for (int i = 0; i < cost.length; i++)
            cost[i] = c[s][i]==Byte.MAX_VALUE ? Short.MAX_VALUE : c[s][i];


        Arrays.fill(added,false);
        added[s] = true;
        Arrays.fill(through,(short)s);

        for (int i = 0; i < c.length-1; i++) {
            //find min weight in cost that is not added yet
            short minNode=0,min;
            while (added[minNode]) minNode++;
            min = cost[minNode];
            for (short j = 0; j < cost.length; j++) {
                if (!added[j] && cost[j] < min && cost[j]!=0) {
                    min = cost[j];
                    minNode = j;
                }
            }
            //add node to added
            added[minNode] = true;
            //update cost and through
            for (int j = 0; j < cost.length; j++) {
                if ( c[minNode][j] != 0 &&  c[minNode][j] != Byte.MAX_VALUE && c[minNode][j] + cost[minNode] < cost[j]) {
                    cost[j] = (short)(c[minNode][j] + cost[minNode]);
                    through[j] = (short)minNode;
                }
            }

            if (minNode == des) {
                break;
            }

        }
        short [] tmp = Arrays.copyOf(cost,cost.length);
        Arrays.sort(tmp);
        System.out.println(Arrays.toString(tmp));
        int k = des;
        while (through[k] != s) {
            colorCell(through[k]%getWidth(), through[k]/getWidth());
            k = through[k];
        }
        System.out.println("Path found");
    }

//    public int[][] getAdjacencyMatrix() {
//        return matrix;
//    }

    public MazeCell[][] getMaze() {
        return maze;
    }
}
