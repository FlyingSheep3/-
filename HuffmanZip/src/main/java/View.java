
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class View {
    static {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
    }

    public static JFrame frame = new JFrame("Huffman Zip");
    private JLabel label1 = new JLabel("ԭ·��:");
    private JLabel label2 = new JLabel("��·��:");
    private JLabel label3 = new JLabel("˵���������ļ���С100M����");
    private JTextField textField1 = new JTextField();
    private JTextField textField2 = new JTextField();
    private JButton button1 = new JButton("��");
    private JButton button2 = new JButton("��");
    private JButton button3 = new JButton("ѹ��");
    private JButton button4 = new JButton("��ѹ");
    private JButton button5 = new JButton("����");
    public static JProgressBar bar = new JProgressBar();

    public View() {

        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        utilFrame.frameLocation(frame, 500, 250);
        label1.setBounds(50, 40, 70, 25);
        label2.setBounds(50, 80, 70, 25);
        label3.setBounds(300, 180, 200, 25);
        textField1.setBounds(120, 40, 250, 25);
        textField2.setBounds(120, 80, 250, 25);
        button1.setBounds(380, 40, 40, 25);
        button1.setMargin(new Insets(0, 0, 0, 0));
        button2.setBounds(380, 80, 40, 25);
        button2.setMargin(new Insets(0, 0, 0, 0));
        button3.setBounds(150, 150, 40, 25);
        button3.setMargin(new Insets(0, 0, 0, 0));
        button4.setBounds(300, 150, 40, 25);
        button4.setMargin(new Insets(0, 0, 0, 0));
        button5.setBounds(440, 10, 30, 20);
        button5.setMargin(new Insets(0, 0, 0, 0));
        bar.setMaximum(100);    //�趨�����������ֵ
        bar.setBounds(60, 125, 380, 10);
        bar.setVisible(false);
        frame.add(label1);
        frame.add(label2);
        frame.add(label3);
        frame.add(textField1);
        frame.add(textField2);
        frame.add(button1);
        frame.add(button2);
        frame.add(button3);
        frame.add(button4);
        frame.add(button5);
        frame.add(bar);
        action();
    }

    private void action() {
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                jfc.showDialog(new JLabel(), "ѡ��");
                File file = jfc.getSelectedFile();
                if (file != null) {
                    textField1.setText(file.getAbsolutePath());
                    System.out.println("�ļ�:" + file.getAbsolutePath());
                }
            }
        });
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                jfc.showDialog(new JLabel(), "ѡ��");
                File file = jfc.getSelectedFile();
                if (file != null) {
                    textField2.setText(file.getAbsolutePath() + "\\huffman.zip");
//                    System.out.println("�ļ�:" + file.getAbsolutePath());
                }
            }
        });

        button3.addActionListener(e -> new Thread(() -> {
            bar.setVisible(true);
            String FilePath = textField1.getText();
            String TOFilePath = textField2.getText();
            boolean b = zipProcess.zipFile(FilePath, TOFilePath);
            if (b) {
                JOptionPane.showMessageDialog(frame, "ѹ���ɹ�");
                bar.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(frame, "ѹ��ʧ��");
                bar.setVisible(false);
            }
        }).start());


        button4.addActionListener(e -> new Thread(() -> {
            bar.setVisible(true);
            String FilePath = textField1.getText();
            String TOFilePath = textField2.getText();
            boolean b = zipProcess.unzip(FilePath, TOFilePath);
            System.out.println("123");
            if (b) {
                JOptionPane.showMessageDialog(frame, "��ѹ�ɹ�");
                bar.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(frame, "��ѹʧ��");
                bar.setVisible(false);
            }
        }).start());

        button5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (button5.getText().equals("����")) {
                    try {
                        UIManager.setLookAndFeel(new FlatLightLaf());
                        SwingUtilities.updateComponentTreeUI(frame);
                        button5.setText("�ص�");
                    } catch (Exception ex) {
                        System.err.println("Failed to initialize LaF");
                    }
                } else if (button5.getText().equals("�ص�")) {
                    try {
                        UIManager.setLookAndFeel(new FlatDarkLaf());
                        SwingUtilities.updateComponentTreeUI(frame);
                        button5.setText("����");
                    } catch (Exception ex) {
                        System.err.println("Failed to initialize LaF");
                    }
                }
            }
        });
        //�ر�Ӧ�ý����Զ��رճ��򣬱����̨ռ����Դ
        frame.addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
            }
        });

    }
}


