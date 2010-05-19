import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Panel;

class MainPanel extends Panel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public MainPanel(SamplesView fRealView, SamplesView fImagView,
            SamplesView gRealView, SamplesView gImagView) {
        //設定版面的配置(2列1欄寬度各是1)
        setLayout(new GridLayout(2,1,1,1));
        add(new ComplexSamplesPanel(fRealView,
                fImagView,
        "f(x)"));
        add(new ComplexSamplesPanel(gRealView,
                gImagView,
        "F(k)"));
    }
    //設定邊線為紅色
    @SuppressWarnings("deprecation")
    public void paint(Graphics g) {
        Dimension d = size();
        g.setColor(Color.red);
        g.draw3DRect(0,0,d.width-1,d.height-1,true);
    }
    public Insets insets() {
        return new Insets(1,1,1,1);
    }
}