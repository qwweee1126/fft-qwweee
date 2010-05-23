
public class Data {
    public int index;
    public float value;
    public Data(int index, float value) {
        this.index = index;
        this.value = value;
    }
    public Data(long index, float value) {
        this.index = (int) index;
        this.value = value;
    }
}
