
import javax.swing.*;
import java.awt.*;

public class utilFrame {

    public static void frameLocation(JFrame frame, int x, int y) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();  //��ȡ����Ļ�ߴ�
        frame.setSize(x, y);
        int xx = (int) ((screenSize.getWidth() - frame.getWidth()) / 2);   //����λ�þ��ǣ���Ļ�ߴ�/2 - ���ڳߴ�/2
        int yy = (int) ((screenSize.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(xx, yy);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setResizable(false);
    }
}
