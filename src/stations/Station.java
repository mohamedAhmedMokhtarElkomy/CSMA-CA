package stations;


import GUI.MainFrame;
import enums.PacketType;
import enums.StationState;

import java.util.concurrent.TimeUnit;

public abstract class Station implements Runnable {
    protected Packet packet;
    protected StationState stationState;
    protected String owner;
    protected int nav; //it is approximation of how long the channel is going to be busy

    public String getOwner() {
        return owner;
    }
//    Channel channel;    //TODO

    protected Station() {
        this.stationState = StationState.IDLE;
        this.packet = new Packet();
        this.nav = 0;
    }

    protected void changeState(StationState newState) {
        stationState = newState;
        updateFrameLabels();
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
            TimeUnit.MILLISECONDS.sleep(i * 500L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if(nav > 0){
            nav-=i;
        }
        if (nav < 0)
            nav = 0;
        updateFrameLabels();
    }

    protected void updateFrameLabels(){}

    protected void sendPacket(PacketType packetType){

    }

}
