package stations;

import GUI.MainFrame;
import enums.PacketType;
import enums.StationState;

import java.util.Random;

public class MobileStation extends Station{
    public void setBaseStation(BaseStation baseStation) {
        this.baseStation = baseStation;
    }

    private BaseStation baseStation;
    private int packetsToSend = 0;
    private int CWorder = 0; // TODO should be incremented each time you have to back off
    private int backoffValue = 0;

    //nav greater than zero mean guessing that the channel is busy
    private int nav = 0; //it is approximation of how long the channel is going to be busy
    private final Random rand;
    private String owner = ""; //TODO give owner a name

    private Channel channel;

    private Packet packet;


    public MobileStation(String name, BaseStation baseStation) {
        rand = new Random();
        owner = name;
        channel = new Channel();
        this.baseStation = baseStation;
        packet = new Packet();
        debugVar = name;
    }

    @Override
    public void run() {
        System.out.println("Mobile " + owner + " Thread is running...");
        activeAction();
    }


    private void sendPacket() {
        packet.setOwner(owner);
        packet.setMessage("hello base");
        baseStation.receptionAction(packet);
        System.out.println("Packet sent");
    }

    @Override
    protected void activeAction() {
        while (true) {

//            System.out.println(convertStateToString() + ", packets= " + packetsToSend);
            if(owner == "1")
                MainFrame.mobile1Label.setText(convertStateToString() + " packets to send: " + packetsToSend);
            else if(owner == "2")
                MainFrame.mobile2Label.setText(convertStateToString() + " packets to send: " + packetsToSend);
            else if(owner == "3")
                MainFrame.mobile3Label.setText(convertStateToString() + " packets to send: " + packetsToSend);

            if (stationState == StationState.IDLE) {
                if (packetsToSend > 0) {
                    if (channel.isBusy() || nav > 0)
                        BEB();
                    changeState(StationState.DIFS_beforeCountdown);

                }
            } else if (stationState == StationState.DIFS_beforeCountdown) {
                elapsedTime(StationState.DIFS_beforeCountdown.label);
                changeState(StationState.Countdown);
                //TODO check this again
                if (channel.isBusy() || nav > 0) {
                    //TODO will it need BEB()?
                    BEB();
                    changeState(StationState.DIFS_beforeCountdown);
                }
            } else if (stationState == StationState.Countdown) {
                elapsedTime(StationState.Countdown.label);
                backoffValue--;
//                System.out.println("backoff: " + backoffValue);
                changeState(StationState.Countdown);

                if (packetsToSend > 0) {
                    if (channel.isBusy() || nav > 0) {
                        changeState(StationState.IDLE);
                    }
                }

                //TODO TOCHECK this condition
                if (backoffValue <= 0)
                    changeState(StationState.emitRTS);
            } else if (stationState == StationState.emitRTS) {
                elapsedTime(StationState.emitRTS.label);
                packet.setType(PacketType.RTS);
                sendPacket();
                changeState(StationState.SIFS_before_rcvCTS);

            } else if (stationState == StationState.SIFS_before_rcvCTS) {
                elapsedTime(StationState.SIFS_before_rcvCTS.label);
                changeState(StationState.rcvCTS);
            } else if (stationState == StationState.rcvCTS) {
                elapsedTime(StationState.rcvCTS.label);
                if(stationState == StationState.rcvCTS) {
                    changeState(StationState.DIFS_beforeCountdown);
                    BEB();
                }
            } else if (stationState == StationState.SIFS_before_emitPKT) {
                elapsedTime(StationState.SIFS_before_emitPKT.label);
                changeState(StationState.emitPKT);

            } else if (stationState == StationState.emitPKT) {
                System.out.println("1:packet send(emitpkt)");
                elapsedTime(StationState.emitPKT.label);
                packet.setType(PacketType.PKT);
                sendPacket();
                changeState(StationState.SIFS_before_rcvACK);

            } else if (stationState == StationState.SIFS_before_rcvACK) {
                elapsedTime(StationState.SIFS_before_rcvACK.label);
                changeState(StationState.rcvACK);

            } else if (stationState == StationState.rcvACK) {
                elapsedTime(StationState.rcvACK.label);
                if (stationState == StationState.rcvACK) {
                    BEB();
                    changeState(StationState.DIFS_beforeCountdown);
                }

            }

        }
    }

    @Override
    public void receptionAction(Packet packet) {
        if (stationState == StationState.IDLE || stationState == StationState.DIFS_beforeCountdown || stationState == StationState.Countdown) {
            if (!packet.isCorrupted() && packet.getNav() > nav)
                nav = packet.getNav();
        } else if (stationState == StationState.rcvCTS) {
            if (!packet.isCorrupted() && packet.getOwner().equals(owner) && packet.getType() == PacketType.CTS) {
                changeState(StationState.SIFS_before_emitPKT);
                System.out.println("=============================================== rcvcts");
            } else {
                System.out.println("++++++++++++++++++++++++++why reach here++++++++++++++++++++++++");
                BEB();
                changeState(StationState.DIFS_beforeCountdown);
            }
        } else if (stationState == StationState.rcvACK) {
            System.out.println("1: rcvAck");
            if (!packet.isCorrupted() && packet.getOwner().equals(owner) && packet.getType() == PacketType.ACK) {
                System.out.println("1: conf rcvAck");
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
    }

    public void hitButton() {
        packetsToSend++;
//        System.out.println(owner + " hit the button " + "number of packets is: " + packetsToSend);
    }
}
