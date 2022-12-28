package enums;

public enum State
{
    IDLE(0),
    SIFS_before_emitCTS(3),
    emitCTS(15),
    SIFS_before_rcvPKT(3),
    rcvPKT(61),
    SIFS_before_emitACk(3),
    emitACK(10),

    DIFS_beforeCountdown(5),
    Countdown(5),
    emitRTS(15),
    SIFS_before_revCTS(3),
    rcvCTS(16),
    SIFS_before_emitPKT(3),
    emitPKT(60),
    SIFS_before_rcvACK(3),
    rcvACK(11);
    public final int label;
    private State(int label) {
        this.label = label;
    }

}