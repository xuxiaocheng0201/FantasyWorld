package HeadLibs.DataStructures;

public interface IUpdatable {
    boolean getUpdated();
    void setUpdated(boolean updated);

    static long getSerialVersionUID(long sourceUID) {
        return sourceUID;
    }
}
