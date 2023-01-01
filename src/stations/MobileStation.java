package stations;

import GUI.MainFrame;
import enums.PacketType;
import enums.StationState;

import java.util.Random;

public class MobileStation extends Station {


    private int packetsToSend;
    private int CWorder; // TODO should be incremented each time you have to back off
    private int backoffValue;

    //nav greater than zero mean guessing that the channel is busy
    private int nav; //it is approximation of how long the channel is going to be busy
    private final Random rand;
    private String owner;


    public MobileStation(String name) {
        this.packetsToSend = 0;
        this.CWorder = 0;
        this.backoffValue = 0;
        this.nav = 0;
        this.owner = "";

        this.rand = new Random();
        this.owner = name;

        this.packet = new Packet();

    }

    @Override
    public void run() {
        System.out.println("Mobile " + owner + " Thread is running...");
        activeAction();
    }


    @Override
    protected void sendPacket(PacketType packetType) {
        super.sendPacket(packetType);
        packet.setOwner(owner);
        packet.setMessage("hello base");
        MainFrame.mainChannel.forward(packet);
        System.out.println(owner + ": " + packetType.toString() + " sent");
    }

    @Override
    protected void activeAction() {
        while (true) {

//            System.out.println(convertStateToString() + ", packets= " + packetsToSend);
            updateFrameLabels();

            if (stationState == StationState.IDLE) {
                if (packetsToSend > 0) {
                    if (MainFrame.mainChannel.isBusy() || nav > 0)
                        BEB();
                    changeState(StationState.DIFS_beforeCountdown);

                }
            } else if (stationState == StationState.DIFS_beforeCountdown) {
                elapsedTime(StationState.DIFS_beforeCountdown.time);
                changeState(StationState.Countdown);
                //TODO check this again
                if (MainFrame.mainChannel.isBusy() || nav > 0) {
                    //TODO will it need BEB()?
                    BEB();
                    changeState(StationState.DIFS_beforeCountdown);
                }
            } else if (stationState == StationState.Countdown) {//TODO check this whole condition
                elapsedTime(StationState.Countdown.time);
                backoffValue--;
                changeState(StationState.Countdown);

                //TODO i think this should be removed
                if (packetsToSend > 0) {
                    if (MainFrame.mainChannel.isBusy() || nav > 0) {
                        changeState(StationState.IDLE);
                    }
                }

                //TODO TOCHECK this condition
                if (backoffValue <= 0)
                    changeState(StationState.emitRTS);
            } else if (stationState == StationState.emitRTS) {
                elapsedTime(StationState.emitRTS.time);
                sendPacket(PacketType.RTS);
                changeState(StationState.SIFS_before_rcvCTS);

            } else if (stationState == StationState.SIFS_before_rcvCTS) {
                elapsedTime(StationState.SIFS_before_rcvCTS.time);
                changeState(StationState.rcvCTS);
                elapsedTime(StationState.rcvCTS.time);
            } else if (stationState == StationState.rcvCTS) {
                changeState(StationState.DIFS_beforeCountdown);
                BEB();
            } else if (stationState == StationState.SIFS_before_emitPKT) {
                elapsedTime(StationState.SIFS_before_emitPKT.time);
                changeState(StationState.emitPKT);

            } else if (stationState == StationState.emitPKT) {
                elapsedTime(StationState.emitPKT.time);
                sendPacket(PacketType.PKT);
                changeState(StationState.SIFS_before_rcvACK);

            } else if (stationState == StationState.SIFS_before_rcvACK) {
                elapsedTime(StationState.SIFS_before_rcvACK.time);
                changeState(StationState.rcvACK);
                elapsedTime(StationState.rcvACK.time);
            } else if (stationState == StationState.rcvACK) {

                BEB();
                changeState(StationState.DIFS_beforeCountdown);
            }

        }

    }

    @Override
    protected void changeState(StationState newState) {
        super.changeState(newState);
        updateFrameLabels();
    }

    @Override
    public void receptionAction(Packet packet) {
        System.out.println(owner + ": received " + packet.getType().toString());
        if (stationState == StationState.IDLE || stationState == StationState.DIFS_beforeCountdown || stationState == StationState.Countdown) {
            if (!packet.isCorrupted() && packet.getNav() > nav)
                nav = packet.getNav();
        } else if (stationState == StationState.rcvCTS) {
            if (!packet.isCorrupted() && packet.getOwner().equals(owner) && packet.getType() == PacketType.CTS) {
                changeState(StationState.SIFS_before_emitPKT);
            } else {
                BEB();
                changeState(StationState.DIFS_beforeCountdown);
            }
        } else if (stationState == StationState.rcvACK) {
            if (!packet.isCorrupted() && packet.getOwner().equals(owner) && packet.getType() == PacketType.ACK) {
                packetsToSend--;
                if (packetsToSend > 0) {
                    CWorder = 3;
                    BEB();
                    changeState(StationState.DIFS_beforeCountdown);
                } else
                    changeState(StationState.IDLE);
            }
        }
    }

    public void BEB() {
        System.out.println("BEB is called");
        CWorder++;
        backoffValue = rand.nextInt((int) (Math.pow(2, CWorder) + 1));
        //TODO changeState(StationState.DIFS_beforeCountdown);
    }

    public void hitButton() {
        packetsToSend++;
        updateFrameLabels();
    }

    @Override
    protected void updateFrameLabels() {
        if (owner == "1")
            MainFrame.mobile1Label.setText(stationState.toString() + " packets to send: " + packetsToSend);
        else if (owner == "2")
            MainFrame.mobile2Label.setText(stationState.toString() + " packets to send: " + packetsToSend);
        else if (owner == "3")
            MainFrame.mobile3Label.setText(stationState.toString() + " packets to send: " + packetsToSend);
    }
}
