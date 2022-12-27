import enums.PacketType;

public class Packet {
    private String owner;
    private PacketType type;

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
