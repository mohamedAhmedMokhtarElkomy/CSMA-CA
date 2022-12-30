package enums;

public enum StationState
{
    IDLE(00),
    SIFS_before_emitCTS(30),
    emitCTS(150),
    SIFS_before_rcvPKT(30),
    rcvPKT(610),
    SIFS_before_emitACk(30),
    emitACK(100),

    DIFS_beforeCountdown(50),
    Countdown(50),
    emitRTS(150),
    SIFS_before_rcvCTS(30),
    rcvCTS(160),
    SIFS_before_emitPKT(30),
    emitPKT(600),
    SIFS_before_rcvACK(30),
    rcvACK(110);
    public final int time;
    StationState(int time) {
        this.time = time;
    }

}