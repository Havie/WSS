import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		GameController gameCont = new GameController();		
		NodeLoader nLoad = new NodeLoader(gameCont);
		nLoad.load();
		gameCont.run();
	}
}
