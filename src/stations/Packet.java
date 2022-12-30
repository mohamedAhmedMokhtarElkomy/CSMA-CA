package stations;

import enums.PacketType;

public class Packet {
    private String owner;
    private PacketType type;

    private int nav;

    private String message;

    MobileStation mobileStation;

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setType(PacketType type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getNav() {
        return nav;
    }

    public void setNav(int nav) {
        this.nav = nav;
    }

    boolean isCorrupted()
    {
        return false;
    }

    public String getOwner() {
        return owner;
    }

    public PacketType getType(){
        return type;

    }
}
