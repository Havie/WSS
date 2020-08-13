public class Main {
	public static void main(String[] args) {		
		try {
			GameController gameCont = new GameController();		
			gameCont.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
