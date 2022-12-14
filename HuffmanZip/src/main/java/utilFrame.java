
import javax.swing.*;
import java.awt.*;

public class utilFrame {

    public static void frameLocation(JFrame frame, int x, int y) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();  //获取到屏幕尺寸
        frame.setSize(x, y);
        int xx = (int) ((screenSize.getWidth() - frame.getWidth()) / 2);   //居中位置就是：屏幕尺寸/2 - 窗口尺寸/2
        int yy = (int) ((screenSize.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(xx, yy);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setResizable(false);
    }
}
