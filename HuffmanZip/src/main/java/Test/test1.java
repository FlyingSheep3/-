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
//    JProgressBar bar = new JProgressBar();   //��������ʾ�ļ���������
//bar.setMaximum(1000);
//bar.setBounds(20, 50, 300, 10);
//
//    JButton button = new JButton("�����ʼ");   //�����ť��ʼ�����ļ�
//button.setBounds(20, 100, 100, 30);
//button.addActionListener(e -> new Thread(() -> {
//        //ע�⣬����ֱ��������߳����洦����Ϊ����߳��Ǹ���ͼ�ν���ģ��õ�������һ���̴߳�������ͼ�ν���Ῠ��
//        File file = new File("in.iso");
//        try(FileInputStream in = new FileInputStream(file);
//            FileOutputStream out = new FileOutputStream("out.iso")){
//            long size = file.length(), current = 0;
//            int len;
//            byte[] bytes = new byte[1024];
//            while ((len = in.read(bytes)) > 0) {
//                current += len;
//                bar.setValue((int) (bar.getMaximum() * (double)current / size));   //ÿ�ο��������½�����
//                bar.repaint();  //��Ϊ������ÿ�θ���ֵ����ʹ��������»��ƣ�����Ӿ��ϱȽϿ�������ÿ�ο��������»������
//                out.write(bytes, 0, len);
//            }
//        } catch (IOException exception) {
//            exception.printStackTrace();
//        }
//    }).start());
//}
