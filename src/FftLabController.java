import java.util.Observable;
import java.util.Observer;

class FftLabController implements Observer {
    public Samples fReal,fImag,gReal,gImag;
    public SamplesView fRealView,fImagView,gRealView,gImagView;
    //宣告
    public FftLabController() {
        int origin = (originCentered)?length/2:0;
        fReal = new Samples(length,origin);
        fImag = new Samples(length,origin);
        gReal = new Samples(length,origin);
        gImag = new Samples(length,origin);
        initSamples();
        fReal.addObserver(this);
        fImag.addObserver(this);
        gReal.addObserver(this);
        gImag.addObserver(this);
        fRealView = new SamplesView(fReal);
        fImagView = new SamplesView(fImag);
        gRealView = new SamplesView(gReal);
        gImagView = new SamplesView(gImag);
        updateSampleValues(fRealView,fImagView);
        updateSampleValues(gRealView,gImagView);
    }
    //抓取mode(Editing裡面)的值
    public int getEditMode() {
        return editMode;
    }
    //把mode的值給圖
    public void setEditMode(int mode) {
        editMode = mode;
        fRealView.setEditMode(mode);
        fImagView.setEditMode(mode);
        gRealView.setEditMode(mode);
        gImagView.setEditMode(mode);
    }
    //抓取Lengh的值
    public int getLength() {
        return length;
    }
    //把Lengh的值給圖
    public void setLength(int length) {
        this.length = length;
        updateLengths();
        updateOrigins();
        initSamples();
        updateSampleValues(fRealView,fImagView);
        updateSampleValues(gRealView,gImagView);
        repaintViews();
    }
    //使用OriginCentered
    public boolean getOriginCentered() {
        return originCentered;
    }
    public void setOriginCentered(boolean centered) {
        if (centered==originCentered) return;
        originCentered = centered;
        updateOrigins();
        repaintViews();
    }
    //使用zeroAll
    public void zeroAll() {
        fReal.zero();
        fImag.zero();
        gReal.zero();
        gImag.zero();
        repaintViews();
    }
    //重新選擇的時候
    public void update(Observable o, Object arg) {
        Samples s = (Samples)o;
        if (s==fReal || s==fImag) {
            transform(1,fReal,fImag,gReal,gImag);
            updateSampleValues(gRealView,gImagView);
            gRealView.repaint();
            gImagView.repaint();
        } else {
            transform(-1,gReal,gImag,fReal,fImag);
            updateSampleValues(fRealView,fImagView);
            fRealView.repaint();
            fImagView.repaint();
        }
    }
    //預設
    private int editMode = SamplesView.EDIT_DRAW;
    private int length = 32; //預設為16點
    private boolean originCentered = false;
    //計算樣本的值(上面的)
    private float computeSampleValue(Samples real, Samples imag) {
        float sv = 0.0f;
        float v[];
        v = real.values;
        for (int i=0; i<length; ++i) {
            float si = v[i];
            if (-si>sv) sv = -si;
            else if (si>sv) sv = si;
        }
        v = imag.values;
        for (int i=0; i<length; ++i) {
            float si = v[i];
            if (-si>sv) sv = -si;
            else if (si>sv) sv = si;
        }
        return sv;
    }
    private void updateSampleValues(SamplesView realView, SamplesView
            imagView) {
        float sv =
            computeSampleValue(realView.samples,imagView.samples);
        realView.setSampleValue(sv);
        imagView.setSampleValue(sv);
    }
    private void transform(int sign,
            Samples sar, Samples sai,
            Samples bar, Samples bai) {
        float ar[] = sar.values;
        float ai[] = sai.values;
        float br[] = bar.values;
        float bi[] = bai.values;
        for (int i=0; i<length; ++i) {
            br[i] = ar[i];
            bi[i] = ai[i];
        }
        if (originCentered) {
            for (int i=1; i<length; i+=2) {
                br[i] = -br[i];
                bi[i] = -bi[i];
            }
        }
        Fft.complexToComplex(sign,length,br,bi);
        if (originCentered) {
            for (int i=1; i<length; i+=2) {
                br[i] = -br[i];
                bi[i] = -bi[i];
            }
        }
    }
    public void updatedata() {
        transform(1,fReal,fImag,gReal,gImag);
        fReal.notifyObservers();
        fImag.notifyObservers();
        gReal.notifyObservers();
        gImag.notifyObservers();
    }
    private void initSamples() {
        //fReal.values[fReal.origin+1] = 1.0f;
        //transform(1,fReal,fImag,gReal,gImag);
        for (int i = 0 ; i < fReal.values.length ; i ++) {
            fReal.values[i] = 0.0f;
        }
        transform(1,fReal,fImag,gReal,gImag);
    }
    private void updateLengths() {
        int length = this.length;
        fReal.setLength(length);
        fImag.setLength(length);
        gReal.setLength(length);
        gImag.setLength(length);
    }
    private void updateOrigins() {
        int origin = (originCentered)?length/2:0;
        int shift = origin-fReal.origin;
        fReal.origin = origin;
        fImag.origin = origin;
        gReal.origin = origin;
        gImag.origin = origin;
        fReal.rotate(shift);
        fImag.rotate(shift);
        gReal.rotate(shift);
        gImag.rotate(shift);
    }
    private void repaintViews() {
        fRealView.repaint();
        fImagView.repaint();
        gRealView.repaint();
        gImagView.repaint();
    }
    @SuppressWarnings("unused")
    private void shiftSamples(Samples s, int shift) {
        float temp[] = new float[length];
        int j = shift%length;
        for (int i=0; i<length; ++i,++j) {
            if (j<0) j += length;
            if (j>=length) j -= length;
            temp[j] = s.values[i];
        }
        s.values = temp;
    }
}