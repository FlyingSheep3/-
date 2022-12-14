
public class Node implements Comparable<Node> {
    Byte data;//���Ȩֵ
    int weight;//���ִ���
    Node left;//ָ�����ӽ��
    Node right;//ָ�����ӽڵ�

    @Override
    public int compareTo(Node o) {
        return this.weight - o.weight;
    }

    public void preOrder() {
        System.out.println("data=" + this.data + " weight=" + this.weight);
        if (left != null) left.preOrder();
        if (right != null) right.preOrder();
    }

    public Node(Byte data, int weight) {
        this.data = data;
        this.weight = weight;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return "Node{" +
                "value=" + data +
                ", weight=" + weight +
                ", left=" + left +
                ", right=" + right +
                '}';
    }
}
