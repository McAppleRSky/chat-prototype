package rarus.chat.dev;

public class OrdinaryMazeGame extends MazeGame{
    @Override
    protected Room makeRoom() {
        return new OrdinaryRoom();
    }
}
