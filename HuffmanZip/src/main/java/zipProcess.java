import java.io.*;
import java.util.*;

public class zipProcess {
    //生成哈夫曼树对应的哈夫曼编码
    //思路
    //1.将哈夫曼编码存放在Map<Byte,String>中
    // key：字节 value二进制编码
    static Map<Byte, String> huffmanCodes = new HashMap();
    //2.在生成的哈夫曼编码表示过程中，需要去拼接路径，定义一个StringBuilder 存储某个叶子结点的路径
    static StringBuilder stringBuilder = new StringBuilder();
    static long size;

    /**
     * 功能：将传入的node结点的所有叶子结点的哈夫曼编码得到，并放入HuffmanCode中
     *
     * @param node          传入结点
     * @param code          传入路径 左结点：0  右节点：1
     * @param stringBuilder 用于拼接路径
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
     * 重载getCodes方法，使之调用方便，且返回哈夫曼编码表
     *
     * @param node 根结点
     * @return 哈夫曼编码表
     */
    public static Map<Byte, String> getCodes(Node node) {
        if (node == null) return null;
        getCodes(node, "", stringBuilder);
        return huffmanCodes;
    }

    /**
     * 根据字节数组获取一个结点集合
     *
     * @param bytes
     * @return
     */
    public static List<Node> getNodes(byte[] bytes) {
        //创建一个Arraylist
        ArrayList<Node> nodes = new ArrayList<>();
        //遍历bytes，统计每一个byte出现的次数->map[key,value]
        HashMap<Byte, Integer> counts = new HashMap<>();
        for (byte b : bytes
        ) {
            Integer count = counts.get(b);
            if (count == null) counts.put(b, 1);
            else counts.put(b, count + 1);
        }
        //把每一个键值对转成一个Node对象，并加入到nodes中
        for (Map.Entry<Byte, Integer> entry : counts.entrySet()) {
            nodes.add(new Node(entry.getKey(), entry.getValue()));
        }
        return nodes;
    }

    /**
     * 根据排好序的结点集合构建哈夫曼树，返回根节点
     *
     * @param nodes 结点集合
     * @return 哈夫曼树根节点
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
     * 根据哈夫曼编码表，将原字节数组转为压缩过后的一个字节数组
     *
     * @param bytes 字符串对应的字符数组
     * @return 哈夫曼字节数组
     */
    private static byte[] zip(byte[] bytes) {
        //1.利用huffmanCode将bytes转变为哈夫曼编码对应的字符串
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            stringBuilder.append(huffmanCodes.get(b));
        }
//        System.out.println("哈夫曼二进制字符串" + stringBuilder);
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
     * 封装生成哈夫曼编码的全过程
     *
     * @param bytes 原字节数组
     * @return 哈夫曼字节数组
     */
    public static byte[] bytesToHuffmanCode(byte[] bytes) {
        List<Node> nodes = getNodes(bytes);
        Collections.sort(nodes);
        Node tree = createHuffmanTree(nodes);
        getCodes(tree);
        return zip(bytes);
    }


    /**
     * 将一个byte转成一个二进制的字符串
     *
     * @param b    传入的byte
     * @param flag 标志是否需要补高位，如果是true，表示需要补高位，如果是false则不需要补，但是要截取
     * @return 是该b 对应的二进制的字符串
     */
    private static String byteToBitString(boolean flag, byte b) {
        //使用变量保存b
        int temp = b;
        //如果是正数则需要补高位，故一律进行此处理，但最后一个字节可能不足八位，为false
        if (flag)//
            temp |= 256;//按位与256 1 0000 0000
        String str = Integer.toBinaryString(temp);
        //返回的是temp对应的二进制补码，且返回的是完整的完整32位，我们只需要8位
        if (flag) return str.substring(str.length() - 8);//取最后八位的意思
        else return str;
    }


    /**
     * 解压操作，将哈夫曼字节数组转为原来的字节数组
     *
     * @param huffmanCodes 哈夫曼编码表map
     * @param huffmanBytes 哈夫曼编码得到的字节数组
     * @return 就是原来的字符串对应的数组
     */
    static byte[] decode(Map<Byte, String> huffmanCodes, byte[] huffmanBytes) {
        //先得到huffmanByte对应的二进制的字符串，形式10010110..
        StringBuilder stringBuilder = new StringBuilder();
        //将byte数组转成二进制的字符串
        for (int i = 0; i < huffmanBytes.length; i++) {
            boolean flag = (i == huffmanBytes.length - 1);
            stringBuilder.append(byteToBitString(!flag, huffmanBytes[i]));
        }
        //把字符串按照指定的哈夫曼编码进行解码
        //把哈夫曼编码表进行调换，因为反向查询
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
     * 压缩文件操作
     *
     * @param filePath   原路径
     * @param ToFilePath 压缩后的路径
     */
    public static boolean zipFile(String filePath, String ToFilePath) {
        OutputStream os = null;
        ObjectOutputStream oos = null;
        //创建文件的输入流
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
                    View.bar.setValue((int) (View.bar.getMaximum() * (double) i * 1024 / size / 2 + View.bar.getMaximum() / 2));   //每次拷贝都更新进度条
                } else {
                    os.write(huffmanZip, i * 1024, huffmanZip.length % 1024);
                    View.bar.setValue(View.bar.getMaximum());
                }
                View.bar.repaint();
            }
            //迭代进度条，不得不再次考虑使用字节流，考虑到哈夫曼编码map集合转成字节数组不好转回来，
            //且空间大头大概率还是数据的字节数组，故只做huffmanZip的字节流传递
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
     * 功能：将输入流转换成byte[]
     *
     * @param is
     * @return
     * @throws Exception
     */
    public static byte[] streamToByteArray(InputStream is) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();//创建输出流对象
        byte[] b = new byte[1024];
        long current = 0;
        int len;
        while ((len = is.read(b)) != -1) {
            current += len;
            bos.write(b, 0, len);
            View.bar.setValue((int) (View.bar.getMaximum() / 2 * (double) current / size));   //每次拷贝都更新进度条
            View.bar.repaint();
        }
        byte[] array = bos.toByteArray();
        bos.close();
        return array;
    }

    //文件解压操作
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
                    View.bar.setValue((int) (View.bar.getMaximum() * (double) i * 1024 / size / 2 + View.bar.getMaximum() / 2));   //每次拷贝都更新进度条
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
