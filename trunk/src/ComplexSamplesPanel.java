import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;

//以下跟上面設定差不多
class ComplexSamplesPanel extends Panel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public ComplexSamplesPanel(SamplesView realView,
            SamplesView imagView,
            String label) {
        setLayout(new BorderLayout());
        add("North",new Label(label,Label.CENTER));
        Panel panel = new Panel();
        panel.setLayout(new GridLayout(1,2,1,1));
        panel.add(new SamplesPanel(realView,"Real"));
        panel.add(new SamplesPanel(imagView,"Imaginary"));
        add("Center",panel);
    }
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