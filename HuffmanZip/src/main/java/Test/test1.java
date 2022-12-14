//package Test;
//
//import javax.swing.*;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//public class test1 extends JFrame{
//
//    JProgressBar bar = new JProgressBar();   //进度条显示文件拷贝进度
//bar.setMaximum(1000);
//bar.setBounds(20, 50, 300, 10);
//
//    JButton button = new JButton("点击开始");   //点击按钮开始拷贝文件
//button.setBounds(20, 100, 100, 30);
//button.addActionListener(e -> new Thread(() -> {
//        //注意，不能直接在这个线程里面处理，因为这个线程是负责图形界面的，得单独创建一个线程处理，否则图形界面会卡死
//        File file = new File("in.iso");
//        try(FileInputStream in = new FileInputStream(file);
//            FileOutputStream out = new FileOutputStream("out.iso")){
//            long size = file.length(), current = 0;
//            int len;
//            byte[] bytes = new byte[1024];
//            while ((len = in.read(bytes)) > 0) {
//                current += len;
//                bar.setValue((int) (bar.getMaximum() * (double)current / size));   //每次拷贝都更新进度条
//                bar.repaint();  //因为并不是每次更新值都会使得组件重新绘制，如果视觉上比较卡，可以每次拷贝都重新绘制组件
//                out.write(bytes, 0, len);
//            }
//        } catch (IOException exception) {
//            exception.printStackTrace();
//        }
//    }).start());
//}
