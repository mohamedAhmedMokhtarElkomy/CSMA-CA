package stations;


import GUI.MainFrame;
import enums.PacketType;
import enums.StationState;

import java.util.concurrent.TimeUnit;

public abstract class Station implements Runnable {
    protected Packet packet;
    protected StationState stationState;

//    Channel channel;    //TODO

    protected Station() {
        this.stationState = StationState.IDLE;
    }

    protected void changeState(StationState newState) {
        stationState = newState;
    }
    protected StationState getStationState() {
        return stationState;
    }


    //This should handle most state changes, sending packets, and timeouts
    protected void activeAction() {
    }

    //is called whenever a packet is received.
    // It must handle responding to packets by changing state.
    // It should not be sending any packets.
    protected void receptionAction(Packet packet) {
    }

    protected void doNothing() {
        return;
    }

    protected void elapsedTime(int i) {
        try {
            TimeUnit.MILLISECONDS.sleep(i * 50L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    protected void updateFrameLabels(){}

    protected void sendPacket(PacketType packetType){
        packet.setType(packetType);
        MainFrame.mainChannel.forward(packet);
    }

}
