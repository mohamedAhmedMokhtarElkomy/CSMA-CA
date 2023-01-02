package stations;

import enums.PacketType;

import java.util.ArrayList;
import java.util.List;

public class Channel {

    BaseStation baseStation;

    public Channel(BaseStation baseStation, MobileStation mobileStation1, MobileStation mobileStation2, MobileStation mobileStation3) {
        this.baseStation = baseStation;
        this.mobileStation1 = mobileStation1;
        this.mobileStation2 = mobileStation2;
        this.mobileStation3 = mobileStation3;
    }

    MobileStation mobileStation1;
    MobileStation mobileStation2;
    MobileStation mobileStation3;

    private boolean isBusy = false;

    public boolean isBusy() {
        if(isBusy)
            System.out.println("===========================channel is busy========================");
        return isBusy;
    }

    public void setBusy(boolean busy) {
        isBusy = busy;
    }

    public void forward(Packet packet){
        baseStation.receptionAction(packet);
    }

    public void fromBase(Packet packet){
//        if (packet.getType() == PacketType.CTS || packet.getType() == PacketType.ACK){
            mobileStation1.receptionAction(packet);
            mobileStation2.receptionAction(packet);
            mobileStation3.receptionAction(packet);
            if (packet.getType() == PacketType.CTS)
                isBusy = true;
            else if (packet.getType() == PacketType.ACK)
                isBusy = false;
//        }
//        else {
//            if(packet.getOwner().equals(mobileStation1.getOwner()))
//                mobileStation1.receptionAction(packet);
//            else if(packet.getOwner().equals(mobileStation2.getOwner()))
//                mobileStation1.receptionAction(packet);
//            else if(packet.getOwner().equals(mobileStation3.getOwner()))
//                mobileStation1.receptionAction(packet);
//        }
    }
}

