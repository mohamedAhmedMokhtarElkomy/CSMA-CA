package stations;

import GUI.MainFrame;
import enums.PacketType;
import enums.StationState;

import java.util.Random;

public class BaseStation extends Station {

    private String currentSender;

    public BaseStation() {
        this.currentSender = "";
        owner = "base";
    }

    @Override
    public void run() {
        System.out.println("base thread is running...");
        this.activeAction();
    }

    @Override
    protected void activeAction() {
        while (true) {
            updateFrameLabels();

            if (stationState == StationState.IDLE)
                doNothing();
            else if (stationState == StationState.SIFS_before_emitCTS) {
                elapsedTime(StationState.SIFS_before_emitCTS.time);
                changeState(StationState.emitCTS);
            } else if (stationState == StationState.emitCTS) {
                elapsedTime(StationState.emitCTS.time);
                sendPacket(PacketType.CTS);
                changeState(StationState.SIFS_before_rcvPKT);
            } else if (stationState == StationState.SIFS_before_rcvPKT) {
                elapsedTime(StationState.SIFS_before_rcvPKT.time);
                changeState(StationState.rcvPKT);
                elapsedTime(StationState.rcvPKT.time);
            } else if (stationState == StationState.rcvPKT) { //if pkt is correctly received, it will be handled by receptionAction(packet)
                changeState(StationState.IDLE);
            } else if (stationState == StationState.SIFS_before_emitACk) {
                elapsedTime(StationState.SIFS_before_emitACk.time);
                changeState(StationState.emitACK);
            } else if (stationState == StationState.emitACK) {
                elapsedTime(StationState.emitACK.time);
                sendPacket(PacketType.ACK);
                changeState(StationState.IDLE);
            }
        }
    }

    @Override
    public void receptionAction(Packet packet) {
        System.out.println("base: received " + packet.getType().toString());

        this.packet = packet;
        if (stationState == StationState.IDLE) {
            if (!packet.isCorrupted()) {
                if (packet.getType() == PacketType.RTS) {
                    currentSender = packet.getOwner();
                    changeState(StationState.SIFS_before_emitCTS);
                }
            }
        } else if (stationState == StationState.rcvPKT)
            if (!packet.isCorrupted())
                if (packet.getType() == PacketType.PKT && currentSender.equals(packet.getOwner()))
                    changeState(StationState.SIFS_before_emitACk);
        updateFrameLabels();
    }

    @Override
    protected void updateFrameLabels() {
        MainFrame.baseLabel.setText(stationState.toString());
    }

    @Override
    protected void sendPacket(PacketType packetType) {
        packet.setOwner(currentSender);
        packet.setType(packetType);
        packet.setNav(new Random().nextInt(60, 100));
        //rand.nextInt(100);
        MainFrame.mainChannel.fromBase(packet);
        System.out.println("base: " + packetType.toString() + " sent to " + packet.getOwner());
    }
}
