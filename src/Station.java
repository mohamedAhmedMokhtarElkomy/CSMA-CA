import enums.State;

import java.util.concurrent.TimeUnit;

public abstract class Station {

    boolean idle = true;
    String currentSender = "";
    State stationState;

    //TODO Channel channel;
    Packet packet = new Packet();
    //This should handle most state changes, sending packets, and timeouts
    void activeAction(){}
    //is called whenever a packet is received.
    // It must handle responding to packets by changing state.
    // It should not be sending any packets.
    void receptionAction(Packet packet){}

    void doNothing(){
        while (idle)
            continue;
    }

    void elapsedTime(int i){
        try {
            TimeUnit.MILLISECONDS.sleep(i);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
