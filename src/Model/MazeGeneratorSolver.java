package Model;

import Client.Client;
import Client.IClientStrategy;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MazeGeneratorSolver {

    private static Maze mazeGenrator;
    private static Solution mazeSolve;

    public static void main(String[] args) {
        MazeGeneratorSolver generator = new MazeGeneratorSolver();
        Maze maze = generator.generateRandomMaze(5, 5);
        System.out.println((maze));
    }

    public Maze generateRandomMaze(int rows, int cols){
        Server mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        mazeGeneratingServer.start();
        CommunicateWithServer_MazeGenerating(rows, cols);
        mazeGeneratingServer.stop();
        return mazeGenrator;
    }

    public Maze generateLoadedMaze(Maze maze){
        mazeGenrator = maze;
        return mazeGenrator;
    }

    public Solution solveRandomMaze(){
        Server solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        solveSearchProblemServer.start();
        CommunicateWithServer_SolveSearchProblem();
        solveSearchProblemServer.stop();
        return mazeSolve;
    }

    private static void CommunicateWithServer_MazeGenerating(int rows, int cols) {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{rows, cols};
                        toServer.writeObject(mazeDimensions); //send maze dimensions to server
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with  MyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[mazeDimensions[0]*mazeDimensions[1]+24 /*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed   maze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        Maze maze = new Maze(decompressedMaze);
                        mazeGenrator = maze;
//                        maze.print();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private static void CommunicateWithServer_SolveSearchProblem() {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
//                        MyMazeGenerator mg = new MyMazeGenerator();
                        Maze maze = mazeGenrator;
//                        maze.print();
                        toServer.writeObject(maze); //send maze to server
                        toServer.flush();
                        Solution mazeSolution = (Solution) fromServer.readObject(); //read generated maze (compressed with  MyCompressor) from server%s", mazeSolution));
//Print Maze Solution retrieved from the server
                        mazeSolve = mazeSolution;
//                        System.out.println(String.format("Solution steps: %s", mazeSolution));
//                        ArrayList<AState> mazeSolutionSteps = mazeSolution.getSolutionPath();
//                        for (int i = 0; i < mazeSolutionSteps.size(); i++) {
//                            System.out.println(String.format("%s. %s", i, mazeSolutionSteps.get(i).toString()));
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
