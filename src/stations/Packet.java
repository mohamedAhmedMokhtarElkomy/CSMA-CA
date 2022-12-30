package stations;

import enums.PacketType;

public class Packet {
    private String owner;
    private PacketType type;

    private int nav;

    private String message;

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public void setType(PacketType type) {
        this.type = type;
    }

    public PacketType getType() {
        return type;

    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }


    public void setNav(int nav) {
        this.nav = nav;
    }

    public int getNav() {
        return nav;
    }


    boolean isCorrupted() {
        return false;
    }


}
