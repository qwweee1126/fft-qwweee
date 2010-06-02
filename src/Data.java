
public class Data {
    public int index;
    public int value;
    public Data(int index, int value) {
        this.index = index;
        this.value = value;
    }
    public Data(long index, int value) {
        this.index = (int) index;
        this.value = value;
    }
}
