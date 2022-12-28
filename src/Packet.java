import enums.PacketType;

public class Packet {
    private String owner;
    private PacketType type;

    private int nav;

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
