import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Graphics;

class SamplesView extends Canvas {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    //宣告
    public static int EDIT_NONE = 0;
    public static int EDIT_DRAW = 1;
    public static int EDIT_ZERO = 2;
    public static int EDIT_NEGATE = 3;
    public static int EDIT_SHIFT = 4;
    public Samples samples;
    public SamplesView(Samples s) {
        samples = s;
        setSampleValue(1.0f);
        updateDrawingSizes();
        setBackground(Color.white);
    }
    @SuppressWarnings("deprecation")
    public void setSampleValue(float v) {
        int height = size().height;
        sampleValue = (v!=0.0f)?v:1.0f;
        sampleScale = -0.25f*height/sampleValue;
    }
    public void setEditMode(int mode) {
        editMode = mode;
    }
    public void paint(Graphics g) {
        updateDrawingSizes();
        drawSamples(g);
    }
    public Dimension minimumSize() {
        return new Dimension(100,50);
    }
    public Dimension preferredSize() {
        return minimumSize();
    }
    public boolean mouseDown(Event e, int x, int y) {
        if (editMode==EDIT_NONE) return true;
        lastDrag = -1;
        return mouseDrag(e,x,y);
    }
    // 利用滑鼠控制
    @SuppressWarnings("deprecation")
    public boolean mouseDrag(Event e, int x, int y) {
        if (editMode==EDIT_NONE) return true;
        if (x<0 || x>size().width) return true;
        if (y<sampleRadius || y>size().height-sampleRadius) return true;
        int i = (int)((float)(x-sampleStart)/(float)sampleWidth+0.5);
        if (i<0) i = 0;
        if (i>=samples.values.length) i = samples.values.length-1;
        if (editMode==EDIT_NEGATE && i==lastDrag) return true;
        Graphics g = getGraphics();
        if (editMode==EDIT_SHIFT) {
            if (i!=lastDrag && lastDrag>=0) {
                g.setColor(getBackground());
                drawSamples(g);
                samples.rotate(i-lastDrag);
                g.setColor(getForeground());
                drawSamples(g);
            }
            lastDrag = i;
            return true;
        }
        g.setColor(getBackground());
        drawOneSample(g,i);
        if (editMode==EDIT_ZERO) {
            samples.values[i] = 0.0f;
        } else if (editMode==EDIT_NEGATE) {
            samples.values[i] = -samples.values[i];
        } else {
            samples.values[i] = (float)(y-sampleBase)/sampleScale;
        }
        g.setColor(getForeground());
        drawOneSample(g,i);
        lastDrag = i;
        return true;
    }
    public boolean mouseUp(Event e, int x, int y) {
        if (editMode!=EDIT_NONE) samples.notifyObservers();
        return true;
    }
    private int editMode = EDIT_DRAW;
    private int sampleStart,sampleBase,sampleWidth,sampleRadius;
    private float sampleScale,sampleValue;
    private int lastDrag;
    private void drawOneSample(Graphics g, int i) {
        int x = sampleStart+i*sampleWidth;
        int y = sampleBase;
        int r = sampleRadius;
        int w = sampleWidth;
        int h = (int)(samples.values[i]*sampleScale);
        int shiftpoint = 10;
        if (((int)samples.values[i]) < 0) {
            shiftpoint = (int) -Math.sqrt(h)-30;
        }
        g.drawLine(x-w/2,y,x+w/2,y);
        g.drawLine(x,y,x,y+h);
        if (i==samples.origin) {
            g.drawOval(x-r,y+h-r,2*r,2*r);
            g.drawString(String.valueOf((int)(samples.values[i]+0.5)), x-r, y+h-r-shiftpoint);
            //把點設為圓形 空心
        }
        else { //圓心實心
            g.fillOval(x-r,y+h-r,2*r+1,2*r+1);
            g.drawString(String.valueOf((int)(samples.values[i]+0.5)), x-r, y+h-r-shiftpoint);
        }
    }
    @SuppressWarnings("deprecation")
    private void drawSamples(Graphics g) {
        int x = size().width / 2;
        int y = (int) (sampleBase*1.8);
        for (int i=0; i<samples.values.length; ++i) {
            drawOneSample(g,i);
        }
        String str = String.format("時間單位 %d ms", FftLab.trange);
        x = x - str.length()*2;
        g.drawString(str, x, y);
    }
    @SuppressWarnings("deprecation")
    private void updateDrawingSizes() {
        int width = size().width;
        int height = size().height;
        int nSamples = samples.values.length;
        sampleWidth = (int)((float)width/(float)(nSamples+1));
        sampleStart = (width-(nSamples-1)*sampleWidth)/2;
        sampleBase = (int)(0.5f*height);
        sampleScale = -0.25f*height/sampleValue;
        sampleRadius = (int)(0.4f*sampleWidth);
        int maxRadius = (int)(0.5f*height);
        if (sampleRadius>maxRadius) sampleRadius = maxRadius;
    }
}