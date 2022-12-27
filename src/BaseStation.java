import enums.PacketType;
import enums.State;

public class BaseStation extends Station {
    public BaseStation() {
    }

    @Override
    public void activeAction() {

    }

    @Override
    public void receptionAction(Packet packet) {
        if (idle) {
            idle = false;
            if (!packet.isCorrupted()) {
                if (packet.getType() == PacketType.RTS) {
                    currentSender = packet.getOwner();
                    stationState = State.SIFS_before_emitCTS;
                } else if (packet.getType() == PacketType.PKT && currentSender == packet.getOwner()) {
                    stationState = State.SIFS_before_emitACk;
                }

                else {
                    idle = true;
                    currentSender = "";
                }
            }
        }
    }


}
