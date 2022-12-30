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
        return false;
    }

    public void setBusy(boolean busy) {
        isBusy = busy;
    }

    public void forward(Packet packet){
        baseStation.receptionAction(packet);
        mobileStation1.receptionAction(packet);
        mobileStation2.receptionAction(packet);
        mobileStation3.receptionAction(packet);
    }
}

