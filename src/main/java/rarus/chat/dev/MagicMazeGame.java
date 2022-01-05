package rarus.chat.dev;

public class MagicMazeGame extends MazeGame{
    @Override
    protected Room makeRoom() {
        return new MagicRoom();
    }
}
