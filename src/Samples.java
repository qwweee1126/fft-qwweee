import java.util.Observable;

/////////////////////////////////////////////////////////////////////////////
// 取值
class Samples extends Observable {
    public float values[];
    public int origin;
    public Samples(int length, int origin) {
        this.origin = origin;
        values = new float[length];
        zero();
    }
    public void setLength(int length) {
        if (length==values.length) return;
        values = new float[length];
        zero();
    }
    public void zero() {
        for (int i=0; i<values.length; ++i) values[i] = 0.0f;
    }
    public void rotate(int n) {
        int length = values.length;
        float temp[] = new float[length];
        int j = n%length;
        for (int i=0; i<length; ++i,++j) {
            if (j<0) j += length;
            if (j>=length) j -= length;
            temp[j] = values[i];
        }
        values = temp;
    }
    public void notifyObservers() {
        setChanged();
        super.notifyObservers();
    }
}