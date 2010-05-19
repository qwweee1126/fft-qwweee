import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Panel;

/////////////////////////////////////////////////////////////////////////////
//下面那排設定參數的 class
class ControlPanel extends Panel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    //增加選項
    public ControlPanel(FftLabController c) {
        this.c = c;
        add(new Checkbox("Origin Centered"));
        length = new LabeledChoice("Length:");
        length.choice.addItem("16");
        length.choice.addItem("32");
        length.choice.addItem("64");
        length.choice.addItem("128");
        length.choice.addItem("256");
        length.choice.select("16");
        add(length);
        mode = new LabeledChoice("Editing:");
        mode.choice.addItem("Draw");
        mode.choice.addItem("Negate");
        mode.choice.addItem("Zero");
        mode.choice.addItem("Shift");
        mode.choice.addItem("None");
        mode.choice.select("Draw");
        add(mode);
        add(new Button("Zero All"));
    }
    @SuppressWarnings("deprecation")
    public void paint(Graphics g) {
        Dimension d = size();
        g.setColor(Color.white);
        g.draw3DRect(0,0,d.width-1,d.height-1,true);
    }
    public Insets insets() {
        return new Insets(1,1,1,1);
    }
    public boolean handleEvent(Event e) {
        //如果用按鈕就會執行zeroAll()
        if (e.target instanceof Button) {
            c.zeroAll();
            return true;
            //如果選擇Checkbox就直行OriginCentered()
        } else if (e.target instanceof Checkbox) {
            Checkbox cb = (Checkbox)e.target;
            c.setOriginCentered(cb.getState());
            return true;
            //使用選擇的選項
        } else if (e.target instanceof Choice) {
            //選擇Length
            if (e.target==length.choice) {
                String item = length.choice.getSelectedItem();
                c.setLength(Integer.parseInt(item));
                return true;
                //選擇Editing
            } else if (e.target==mode.choice) {
                String item = mode.choice.getSelectedItem();
                if (item=="None") {
                    c.setEditMode(SamplesView.EDIT_NONE);
                } else if (item=="Draw") {
                    c.setEditMode(SamplesView.EDIT_DRAW);
                } else if (item=="Negate") {
                    c.setEditMode(SamplesView.EDIT_NEGATE);
                } else if (item=="Zero") {
                    c.setEditMode(SamplesView.EDIT_ZERO);
                } else if (item=="Shift") {
                    c.setEditMode(SamplesView.EDIT_SHIFT);
                }
                return true;
            }
        }
        return false;
    }
    private FftLabController c;
    private LabeledChoice length;
    private LabeledChoice mode;
}