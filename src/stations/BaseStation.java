package stations;

import GUI.MainFrame;
import enums.PacketType;
import enums.StationState;

public class BaseStation extends Station {

    String currentSender;
    Packet packet;
    MobileStation mobileStation;
    public BaseStation(MobileStation mobileStation) {
        this.mobileStation = mobileStation;
        this.currentSender = "";
    }
    @Override
    public void run() {
        System.out.println("baseThread is running...");
        this.activeAction();
    }
    @Override
    protected void activeAction() {
       while(true){

           MainFrame.baseLabel.setText(convertStateToString());

           if (stationState == StationState.IDLE)
               doNothing();
           else if (stationState == StationState.SIFS_before_emitCTS) {
               elapsedTime(StationState.SIFS_before_emitCTS.label);
               changeState(StationState.emitCTS);
           } else if (stationState == StationState.emitCTS) {
               elapsedTime(StationState.emitCTS.label);
               //TODO sendCTS myChannel.receptionAction(emmitedPacket)
                packet.setType(PacketType.CTS);
                packet.setOwner("1");
                mobileStation.receptionAction(packet);
                System.out.println("base: cts sent");
               changeState(StationState.SIFS_before_rcvPKT);
           } else if (stationState == StationState.SIFS_before_rcvPKT) {
               elapsedTime(StationState.SIFS_before_rcvPKT.label);
               changeState(StationState.rcvPKT);
           } else if (stationState == StationState.rcvPKT) {
               //TODO NOTE that his is the case of timeout.
               //TODO if pkt is correctly received, it will be handled by receptionAction(packet)

               elapsedTime(StationState.rcvPKT.label);
               changeState(StationState.IDLE);
           }
           else if (stationState == StationState.SIFS_before_emitACk) {
               elapsedTime(StationState.SIFS_before_emitACk.label);
               changeState(StationState.emitACK);
           }
           else if (stationState == StationState.emitACK) {
               elapsedTime(StationState.emitACK.label);
               //TODO send out the ack(just like emitCTS)
               packet.setType(PacketType.ACK);
               packet.setOwner("1");
               mobileStation.receptionAction(packet);
               changeState(StationState.IDLE);
           }
       }
    }

    @Override
    public void receptionAction(Packet packet) {
        this.packet = packet;
        MainFrame.baseLabel.setText(convertStateToString());
        if (stationState == StationState.IDLE) {
            if (!packet.isCorrupted()) {
                if (packet.getType() == PacketType.RTS) {
                    currentSender = packet.getOwner();
                    changeState(StationState.SIFS_before_emitCTS);
                }
//                else if (packet.getType() == PacketType.PKT && currentSender.equals(packet.getOwner())) {
//                    changeState(State.SIFS_before_emitACk;
//                } else {
//                    idle = true;
//                    currentSender = "";
//                }
            }
        }
        else if(stationState == StationState.rcvPKT){
            if(!packet.isCorrupted()){
                if (packet.getType() == PacketType.PKT && currentSender.equals(packet.getOwner())) {
                    {
                        changeState(StationState.SIFS_before_emitACk);
                    }
                }
            }
        }
    }
}
