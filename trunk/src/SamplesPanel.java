import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;

class SamplesPanel extends Panel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public SamplesPanel(SamplesView view, String label) {
        setLayout(new BorderLayout());
        add("North",new Label(label,Label.CENTER));
        add("Center",view);
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