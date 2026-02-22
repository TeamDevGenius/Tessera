// Import necessary classes
import some.package.Client;
import some.package.TesseraGame;
import some.package.World;
import some.package.GameScene;
import some.package.ResourceUtils;
import some.package.ClientWindow;

public class TesseraApp {

    public static void main(String[] args) {
        // Initialize resources
        ResourceUtils.init();

        // Create game components
        Client client = new Client();
        TesseraGame game = new TesseraGame();
        World world = new World();
        GameScene scene = new GameScene(world);

        // Set the scene to the game
        game.setScene(scene);

        // Start the rendering loop
        ClientWindow clientWindow = new ClientWindow(client);
        clientWindow.setGame(game);

        // Start the game rendering loop
        while (true) {
            game.update(); // Update game logic
            clientWindow.render(); // Render the game scene
            // Handle frame rate control
            try {
                Thread.sleep(16); // Roughly 60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
