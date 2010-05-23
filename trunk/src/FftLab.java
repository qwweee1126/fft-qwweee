import java.awt.*; //設定類別
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JFrame;

import utils.SQLUtil;

import db.DatabaseFactory;
public class FftLab extends java.applet.Applet {
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    MainPanel mainPanel;
	ControlPanel controlPanel;
	float[] result;
	public void init() {
	    initDB();
		FftLabController controller = new FftLabController();
		controller.setLength(result.length);
		controller.fReal.setData(result);
		controller.updatedata();
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
	private void initDB() {
	    String query = "SELECT `flow`.`dOctets`, `flow`.`Stamp` FROM `10.10.32.154`.`flow` LIMIT 256;";
	    query = "SELECT `flow`.`dOctets`, `flow`.`Stamp` FROM `10.10.32.154`.`flow` WHERE `flow`.`DstPort` = '6667';";
        DatabaseFactory.setDatabaseSettings
        ("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/project?useUnicode=true&characterEncoding=utf8",
                "root", "ji394su3", 100);
        try {
            DatabaseFactory.getInstance();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ArrayList<Data> list = new ArrayList<Data>();
        Date base = null, others = null;
        int size = 0;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement(query);
            rs = pstm.executeQuery();
            rs.next();
            if (rs.next()) {
                Float data = rs.getFloat(1);
                base = rs.getTimestamp(2);
                list.add(new Data(base.getMinutes()-base.getMinutes(), data.floatValue()));
                System.out.println(base.toLocaleString()+"\t"+(base.getMinutes()-base.getMinutes()));
//                list.add(new Data(base.getTime()-base.getTime(), data.floatValue()));
//                System.out.println(base.toLocaleString()+"\t"+(base.getMinutes()-base.getMinutes()));
            }
            while (rs.next()) {
                Float data = rs.getFloat(1);
                others = rs.getTimestamp(2);
                list.add(new Data(others.getMinutes()-base.getMinutes(), data.floatValue()));
                System.out.println(others.toLocaleString()+"\t"+(others.getMinutes()-base.getMinutes()));
                size = others.getMinutes()-base.getMinutes();
//                list.add(new Data(others.getTime()-base.getTime(), data.floatValue()));
//                System.out.println(others.toLocaleString()+"\t"+(others.getMinutes()-base.getMinutes()));
//                size = (int) (others.getTime()-base.getTime());
                
            }
            rs.close();
            pstm.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SQLUtil.close(rs, pstm, con);
        }
        int count = 0;
        while (true) {
            if ( Math.pow(2, count)>= size ) {
                break;
            }
            count++;
        }
        result = new float[(int) Math.round(Math.pow(2, count))];
        for (int i = 0 ; i < list.size() ; i ++) {
            result[(int)(list.get(i).index)] = list.get(i).value;
            System.out.println(list.get(i).index);
        }
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
		frame.resize(1280,1024);
		frame.show();
		frame.setAlwaysOnTop(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
}