import enums.PacketType;
import enums.StationState;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        StationState x = StationState.Countdown;

        System.out.print(StationState.emitACK.toString());
    }
}