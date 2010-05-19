import java.awt.Choice;
import java.awt.Label;
import java.awt.Panel;

class LabeledChoice extends Panel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public Choice choice;
    public LabeledChoice(String label) {
        add(new Label(label,Label.RIGHT));
        choice = new Choice();
        add(choice);
    }
}