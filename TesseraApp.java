// Properly initializing the game engine, Client, World, and GameScene

public class TesseraApp {
    private GameEngine gameEngine;
    private Client client;
    private World world;
    private GameScene gameScene;

    public TesseraApp() {
        initializeGameComponents();
        startGameLoop();
    }

    private void initializeGameComponents() {
        gameEngine = new GameEngine();
        client = new Client(gameEngine);
        world = new World();
        gameScene = new GameScene(world);

        gameEngine.setScene(gameScene);
        client.initialize();
    }

    private void startGameLoop() {
        gameEngine.start();
    }

    public static void main(String[] args) {
        new TesseraApp();
    }
}