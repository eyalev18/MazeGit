package Model;

import Client.Client;
import Client.IClientStrategy;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MazeGeneratorSolver {

    private static Maze mazeGenrator;
    private static Solution mazeSolve;
    private final Logger log = LogManager.getLogger();

    public Maze generateRandomMaze(int rows, int cols){
        Server mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        log.info("Starting server at port 5400");
        mazeGeneratingServer.start();
        log.info("Generate maze");
        CommunicateWithServer_MazeGenerating(rows, cols);
        log.info("Stopping server at port 5400");
        mazeGeneratingServer.stop();
        return mazeGenrator;
    }

    public Maze generateLoadedMaze(Maze maze){
        mazeGenrator = maze;
        return mazeGenrator;
    }

    public Solution solveRandomMaze(){
        Server solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        log.info("Starting server at port 5401");
        solveSearchProblemServer.start();
        log.warn("Solve maze");
        CommunicateWithServer_SolveSearchProblem();
        log.info("Stopping server at port 5401");
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
                        byte[] decompressedMaze = new byte[mazeDimensions[0]*mazeDimensions[1]+24]; //allocating byte[] for the decompressed
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        Maze maze = new Maze(decompressedMaze);
                        mazeGenrator = maze;
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
                        Maze maze = mazeGenrator;
                        toServer.writeObject(maze); //send maze to server
                        toServer.flush();
                        Solution mazeSolution = (Solution) fromServer.readObject(); //read generated maze (compressed with  MyCompressor) from server%s", mazeSolution));
                        mazeSolve = mazeSolution;
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
