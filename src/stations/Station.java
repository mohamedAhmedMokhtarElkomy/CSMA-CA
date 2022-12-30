package stations;


import enums.StationState;

import java.util.concurrent.TimeUnit;

public abstract class Station implements Runnable {
    String debugVar = "base";

    public StationState getStationState() {
        return stationState;
    }

    StationState stationState = StationState.IDLE;
    Channel channel;

    //TODO Channel channel;

    //This should handle most state changes, sending packets, and timeouts
    protected void activeAction() {
    }

    //is called whenever a packet is received.
    // It must handle responding to packets by changing state.
    // It should not be sending any packets.
    void receptionAction(Packet packet) {
    }

    void doNothing() {
        return;
    }

    void elapsedTime(int i) {
        try {
            TimeUnit.MILLISECONDS.sleep(i * 10L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String convertStateToString() {
        switch (stationState) {
            case IDLE:
                return "IDLE";

            case SIFS_before_emitCTS:
                return "SIFS_before_emitCTS";

            case emitCTS:
                return "emitCTS";

            case SIFS_before_rcvPKT:
                return "SIFS_before_rcvPKT";

            case rcvPKT:
                return "rcvPKT";

            case SIFS_before_emitACk:
                return "SIFS_before_emitACk";

            case emitACK:
                return "emitACK";

            case DIFS_beforeCountdown:
                return "DIFS_beforeCountdown";

            case Countdown:
                return "Countdown";

            case emitRTS:
                return "emitRTS";

            case SIFS_before_rcvCTS:
                return "SIFS_before_rcvCTS";

            case rcvCTS:
                return "rcvCTS";

            case SIFS_before_emitPKT:
                return "SIFS_before_emitPKT";

            case emitPKT:
                return "emitPKT";

            case SIFS_before_rcvACK:
                return "SIFS_before_rcvACK";

            case rcvACK:
                return "rcvACK";

            default:
                return "";
        }
    }

    void changeState(StationState newState) {
        System.out.println(debugVar + ": " + newState);
        stationState = newState;
    }
}
