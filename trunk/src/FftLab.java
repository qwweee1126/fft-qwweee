import java.awt.*; //設定類別
import javax.swing.JFrame;
public class FftLab extends java.applet.Applet {
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    MainPanel mainPanel;
	ControlPanel controlPanel;
	public void init() {
		FftLabController controller = new FftLabController();
		//設定版面的位置
		setLayout(new BorderLayout());
		//宣告四個圖的畫面為mainPanel
		mainPanel = new MainPanel(controller.fRealView,
				controller.fImagView,
				controller.gRealView,
				controller.gImagView);
		//增加mainPanel為中間
		add("Center",mainPanel);
		controlPanel = new ControlPanel(controller);
		//增加controlPanel(下面的控制列)為下面
		add("South",controlPanel);
	}
	@SuppressWarnings("deprecation")
    public void start() {
		mainPanel.enable();
		controlPanel.enable();
	}
	@SuppressWarnings("deprecation")
    public void stop() {
		mainPanel.disable();
		controlPanel.disable();
	}
	public boolean handleEvent(Event e) {
		if (e.id==Event.WINDOW_DESTROY) {
			System.exit(0);
		}
		return false;
	}
	@SuppressWarnings("deprecation")
    public static void main(String args[]) {
		//設定畫面右上角的名字為FFT
		JFrame frame = new JFrame(" FFT ");
		FftLab fftLab = new FftLab();
		fftLab.init();
		fftLab.start();
		frame.add("Center",fftLab);
		frame.resize(1024,768);
		frame.show();
		frame.setAlwaysOnTop(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
}