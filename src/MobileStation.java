import enums.PacketType;
import enums.State;

import java.util.Random;

public class MobileStation extends Station{
    private int packetsToSend = 0;
    private int CWorder = 0; // TODO should be incremented each time you have to back off
    private int backoffValue = 0;

    //nav greater than zero mean guessing that the channel is busy
    private int nav = 0; //it is approximation of how long the channel is going to be busy
    private Random rand;
    private String owner = ""; //TODO give owner a name
    public MobileStation() {
        rand = new Random();
    }

    @Override
    public void activeAction() {
        if (stationState == State.IDLE){
            if(packetsToSend > 0)
            {
                if(channel.isBusy() || nav > 0)
                {
                    BEB();
                    stationState = State.DIFS_beforeCountdown;
                }
            }
        }
        else if(stationState == State.DIFS_beforeCountdown) {
            elapsedTime(State.DIFS_beforeCountdown.label);
            stationState = State.Countdown;
            //TODO check this again
            if(channel.isBusy() || nav > 0)
            {
                //TODO will it need BEB()?
                BEB();
                stationState = State.DIFS_beforeCountdown;
            }
        }
        else if(stationState == State.Countdown){
            elapsedTime(State.Countdown.label);
            backoffValue--;
            stationState = State.Countdown;

            if(packetsToSend > 0)
            {
                if(channel.isBusy() || nav > 0)
                {
                    stationState = State.IDLE;
                }
            }
        }
        else if(stationState == State.emitRTS){
            elapsedTime(State.emitRTS.label);
            sendPacket();
            stationState = State.SIFS_before_rcvCTS;

        }
        else if(stationState == State.SIFS_before_rcvCTS){
            elapsedTime(State.SIFS_before_rcvCTS.label);
            stationState = State.rcvCTS;
        }
        else if(stationState == State.rcvCTS){
            elapsedTime(State.rcvCTS.label);
            BEB();
            stationState = State.DIFS_beforeCountdown;
        }
        else if(stationState == State.SIFS_before_emitPKT){
            elapsedTime(State.SIFS_before_emitPKT.label);
            stationState = State.emitPKT;

        }
        else if(stationState == State.emitPKT){
            elapsedTime(State.emitPKT.label);
            sendPacket();
            stationState = State.SIFS_before_rcvACK;

        }
        else if(stationState == State.SIFS_before_rcvACK){
            elapsedTime(State.SIFS_before_rcvACK.label);
            stationState = State.rcvACK;

        }
        else if(stationState == State.rcvACK){
            elapsedTime(State.rcvACK.label);
            BEB();
            stationState = State.DIFS_beforeCountdown;

        }


    }

    @Override
    public void receptionAction(Packet packet) {
        if(stationState == State.IDLE || stationState == State.DIFS_beforeCountdown || stationState == State.Countdown){
            if (!packet.isCorrupted() && packet.getNav() > nav)
                nav = packet.getNav();
        }
        else if(stationState == State.rcvCTS){
            if(!packet.isCorrupted() && packet.getOwner().equals(owner) && packet.getType() == PacketType.CTS){
                stationState = State.SIFS_before_emitPKT;
            }
            else {
                BEB();
                stationState = State.DIFS_beforeCountdown;
            }
        }

        else if(stationState == State.rcvACK){
            if(!packet.isCorrupted() && packet.getOwner().equals(owner) && packet.getType() == PacketType.ACK)
            {
                packetsToSend--;
                if(packetsToSend > 0){
                    CWorder = 3;
                    BEB();
                    stationState = State.DIFS_beforeCountdown;
                }
                else
                    stationState = State.IDLE;
            }
        }
    }

    public void BEB() {
        CWorder++;
        backoffValue = rand.nextInt((int) (Math.pow(2, CWorder) + 1));
    }

    void hitButton()
    {
        packetsToSend++;
    }
}
