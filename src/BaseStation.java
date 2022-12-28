import enums.PacketType;
import enums.State;

public class BaseStation extends Station {
    public BaseStation() {
    }

    @Override
    public void activeAction() {
        if (stationState == State.IDLE)
            doNothing();
        else if (stationState == State.SIFS_before_emitCTS) {
            elapsedTime(State.SIFS_before_emitCTS.label);
            stationState = State.emitCTS;
        } else if (stationState == State.emitCTS) {
            elapsedTime(State.emitCTS.label);
            //TODO sendCTS myChannel.receptionAction(emmitedPacket)
            stationState = State.SIFS_before_rcvPKT;
        } else if (stationState == State.SIFS_before_rcvPKT) {
            elapsedTime(State.SIFS_before_rcvPKT.label);
            stationState = State.rcvPKT;
        } else if (stationState == State.rcvPKT) {
            //TODO NOTE that his is the case of timeout.
            //TODO if pkt is correctly received, it will be handled by receptionAction(packet)
            elapsedTime(State.rcvPKT.label);
            stationState = State.IDLE;
        }
        else if (stationState == State.SIFS_before_emitACk) {
            elapsedTime(State.SIFS_before_emitACk.label);
            stationState = State.emitACK;
        }
        else if (stationState == State.emitACK) {
            //TODO send out the ack(just like emitCTS)
            //elapsedTime(State.emitACK.label);
            stationState = State.IDLE;
        }
    }

    @Override
    public void receptionAction(Packet packet) {
        if (stationState == State.IDLE) {
            if (!packet.isCorrupted()) {
                if (packet.getType() == PacketType.RTS) {
                    currentSender = packet.getOwner();
                    stationState = State.SIFS_before_emitCTS;
                }
//                else if (packet.getType() == PacketType.PKT && currentSender.equals(packet.getOwner())) {
//                    stationState = State.SIFS_before_emitACk;
//                } else {
//                    idle = true;
//                    currentSender = "";
//                }
            }
        }
        else if(stationState == State.rcvPKT){
            if(!packet.isCorrupted()){
                if (packet.getType() == PacketType.PKT && currentSender.equals(packet.getOwner())) {
                    stationState = State.SIFS_before_emitACk;
                }
            }
        }
    }
}
