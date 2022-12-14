import java.io.*;
import java.util.*;

public class zipProcess {
    //���ɹ���������Ӧ�Ĺ���������
    //˼·
    //1.����������������Map<Byte,String>��
    // key���ֽ� value�����Ʊ���
    static Map<Byte, String> huffmanCodes = new HashMap();
    //2.�����ɵĹ����������ʾ�����У���Ҫȥƴ��·��������һ��StringBuilder �洢ĳ��Ҷ�ӽ���·��
    static StringBuilder stringBuilder = new StringBuilder();
    static long size;

    /**
     * ���ܣ��������node��������Ҷ�ӽ��Ĺ���������õ���������HuffmanCode��
     *
     * @param node          ������
     * @param code          ����·�� ���㣺0  �ҽڵ㣺1
     * @param stringBuilder ����ƴ��·��
     */
    private static void getCodes(Node node, String code, StringBuilder stringBuilder) {
        StringBuilder stringBuilder1 = new StringBuilder(stringBuilder);
        stringBuilder1.append(code);
        if (node.data == null) {
            getCodes(node.left, "0", stringBuilder1);
            getCodes(node.right, "1", stringBuilder1);
        } else {
            huffmanCodes.put(node.data, stringBuilder1.toString());
        }
    }

    /**
     * ����getCodes������ʹ֮���÷��㣬�ҷ��ع����������
     *
     * @param node �����
     * @return �����������
     */
    public static Map<Byte, String> getCodes(Node node) {
        if (node == null) return null;
        getCodes(node, "", stringBuilder);
        return huffmanCodes;
    }

    /**
     * �����ֽ������ȡһ����㼯��
     *
     * @param bytes
     * @return
     */
    public static List<Node> getNodes(byte[] bytes) {
        //����һ��Arraylist
        ArrayList<Node> nodes = new ArrayList<>();
        //����bytes��ͳ��ÿһ��byte���ֵĴ���->map[key,value]
        HashMap<Byte, Integer> counts = new HashMap<>();
        for (byte b : bytes
        ) {
            Integer count = counts.get(b);
            if (count == null) counts.put(b, 1);
            else counts.put(b, count + 1);
        }
        //��ÿһ����ֵ��ת��һ��Node���󣬲����뵽nodes��
        for (Map.Entry<Byte, Integer> entry : counts.entrySet()) {
            nodes.add(new Node(entry.getKey(), entry.getValue()));
        }
        return nodes;
    }

    /**
     * �����ź���Ľ�㼯�Ϲ����������������ظ��ڵ�
     *
     * @param nodes ��㼯��
     * @return �����������ڵ�
     */
    public static Node createHuffmanTree(List<Node> nodes) {
        while (nodes.size() > 1) {
            Node leftNode = nodes.get(0);
            Node rightNode = nodes.get(1);
            Node parent = new Node(null, leftNode.weight + rightNode.weight);
            parent.setLeft(leftNode);
            parent.setRight(rightNode);
            nodes.remove(leftNode);
            nodes.remove(rightNode);
            nodes.add(parent);
        }
        return nodes.get(0);
    }

    /**
     * ���ݹ������������ԭ�ֽ�����תΪѹ�������һ���ֽ�����
     *
     * @param bytes �ַ�����Ӧ���ַ�����
     * @return �������ֽ�����
     */
    private static byte[] zip(byte[] bytes) {
        //1.����huffmanCode��bytesת��Ϊ�����������Ӧ���ַ���
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            stringBuilder.append(huffmanCodes.get(b));
        }
//        System.out.println("�������������ַ���" + stringBuilder);
        int len = (stringBuilder.length() + 7) / 8;
        byte[] huffmanCodeBytes = new byte[len];
        for (int i = 0, index = 0; i < stringBuilder.length(); i += 8, index++) {
            String strByte;
            if (i + 8 > stringBuilder.length()) strByte = stringBuilder.substring(i);
            else strByte = stringBuilder.substring(i, i + 8);
            huffmanCodeBytes[index] = (byte) Integer.parseInt(strByte, 2);
        }
        return huffmanCodeBytes;
    }


    /**
     * ��װ���ɹ����������ȫ����
     *
     * @param bytes ԭ�ֽ�����
     * @return �������ֽ�����
     */
    public static byte[] bytesToHuffmanCode(byte[] bytes) {
        List<Node> nodes = getNodes(bytes);
        Collections.sort(nodes);
        Node tree = createHuffmanTree(nodes);
        getCodes(tree);
        return zip(bytes);
    }


    /**
     * ��һ��byteת��һ�������Ƶ��ַ���
     *
     * @param b    �����byte
     * @param flag ��־�Ƿ���Ҫ����λ�������true����ʾ��Ҫ����λ�������false����Ҫ��������Ҫ��ȡ
     * @return �Ǹ�b ��Ӧ�Ķ����Ƶ��ַ���
     */
    private static String byteToBitString(boolean flag, byte b) {
        //ʹ�ñ�������b
        int temp = b;
        //�������������Ҫ����λ����һ�ɽ��д˴��������һ���ֽڿ��ܲ����λ��Ϊfalse
        if (flag)//
            temp |= 256;//��λ��256 1 0000 0000
        String str = Integer.toBinaryString(temp);
        //���ص���temp��Ӧ�Ķ����Ʋ��룬�ҷ��ص�������������32λ������ֻ��Ҫ8λ
        if (flag) return str.substring(str.length() - 8);//ȡ����λ����˼
        else return str;
    }


    /**
     * ��ѹ���������������ֽ�����תΪԭ�����ֽ�����
     *
     * @param huffmanCodes �����������map
     * @param huffmanBytes ����������õ����ֽ�����
     * @return ����ԭ�����ַ�����Ӧ������
     */
    static byte[] decode(Map<Byte, String> huffmanCodes, byte[] huffmanBytes) {
        //�ȵõ�huffmanByte��Ӧ�Ķ����Ƶ��ַ�������ʽ10010110..
        StringBuilder stringBuilder = new StringBuilder();
        //��byte����ת�ɶ����Ƶ��ַ���
        for (int i = 0; i < huffmanBytes.length; i++) {
            boolean flag = (i == huffmanBytes.length - 1);
            stringBuilder.append(byteToBitString(!flag, huffmanBytes[i]));
        }
        //���ַ�������ָ���Ĺ�����������н���
        //�ѹ������������е�������Ϊ�����ѯ
        HashMap<String, Byte> stringByteHashMap = new HashMap<>();
        for (Map.Entry<Byte, String> entry : huffmanCodes.entrySet()) {
            stringByteHashMap.put(entry.getValue(), entry.getKey());
        }
        List<Byte> bytes = new ArrayList<>();

        for (int i = 0; i < stringBuilder.length(); ) {
            int count = 0;
            boolean flag = true;
            while (flag) {
                String substring = stringBuilder.substring(i, i + count);
                Byte b = stringByteHashMap.get(substring);
                if (b == null) {
                    count++;
                } else {
                    flag = false;
                    bytes.add(b);
                }
            }
            i += count;
        }
        byte[] b = new byte[bytes.size()];
        for (int i = 0; i < b.length; i++) {
            b[i] = bytes.get(i);
        }
        return b;
    }

    /**
     * ѹ���ļ�����
     *
     * @param filePath   ԭ·��
     * @param ToFilePath ѹ�����·��
     */
    public static boolean zipFile(String filePath, String ToFilePath) {
        OutputStream os = null;
        ObjectOutputStream oos = null;
        //�����ļ���������
        FileInputStream is = null;
        try {
            File fIle = new File(filePath);
            is = new FileInputStream(filePath);
            size = fIle.length();
            byte[] bytes = streamToByteArray(is);
            byte[] huffmanZip = bytesToHuffmanCode(bytes);
            os = new FileOutputStream(ToFilePath);
            oos = new ObjectOutputStream(os);
            oos.writeObject(huffmanCodes);
            int index = huffmanZip.length / 1024 + 1;
            for (int i = 0; i < index; i++) {
                if (i != index - 1) {
                    os.write(huffmanZip, i * 1024, 1024);
                    View.bar.setValue((int) (View.bar.getMaximum() * (double) i * 1024 / size / 2 + View.bar.getMaximum() / 2));   //ÿ�ο��������½�����
                } else {
                    os.write(huffmanZip, i * 1024, huffmanZip.length % 1024);
                    View.bar.setValue(View.bar.getMaximum());
                }
                View.bar.repaint();
            }
            //���������������ò��ٴο���ʹ���ֽ��������ǵ�����������map����ת���ֽ����鲻��ת������
            //�ҿռ��ͷ����ʻ������ݵ��ֽ����飬��ֻ��huffmanZip���ֽ�������
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (oos != null)
                    oos.close();
                if (os != null)
                    os.close();
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ���ܣ���������ת����byte[]
     *
     * @param is
     * @return
     * @throws Exception
     */
    public static byte[] streamToByteArray(InputStream is) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();//�������������
        byte[] b = new byte[1024];
        long current = 0;
        int len;
        while ((len = is.read(b)) != -1) {
            current += len;
            bos.write(b, 0, len);
            View.bar.setValue((int) (View.bar.getMaximum() / 2 * (double) current / size));   //ÿ�ο��������½�����
            View.bar.repaint();
        }
        byte[] array = bos.toByteArray();
        bos.close();
        return array;
    }

    //�ļ���ѹ����
    public static boolean unzip(String filePath, String ToFilePath) {
        OutputStream os = null;
        ObjectInputStream ois = null;
        FileInputStream is = null;
        try {
            is = new FileInputStream(filePath);
            ois = new ObjectInputStream(is);
            huffmanCodes = (HashMap<Byte, String>) ois.readObject();
            byte[] bytes = streamToByteArray(is);
            byte[] decode = decode(huffmanCodes, bytes);
            os = new FileOutputStream(ToFilePath);
            int index = decode.length / 1024 + 1;
            for (int i = 0; i < index; i++) {
                if (i != index - 1) {
                    os.write(decode, i * 1024, 1024);
                    View.bar.setValue((int) (View.bar.getMaximum() * (double) i * 1024 / size / 2 + View.bar.getMaximum() / 2));   //ÿ�ο��������½�����
                } else {
                    os.write(decode, i * 1024, decode.length % 1024);
                    View.bar.setValue(View.bar.getMaximum());
                }
                View.bar.repaint();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ois != null)
                    ois.close();
                if (os != null)
                    os.close();
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
