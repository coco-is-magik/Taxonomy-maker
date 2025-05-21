package cocoismagik.main;

public class Node {
    String data, data2;
    public Node(String data){
        this.data = data;
        this.data2 = "";
    }
    public Node(String data1, String data2){
        this.data = data1;
        this.data2 = data2;
    }
    public String toString() {
        return data + (data2.isEmpty() ? "" : " (" + data2 + ")");
    }
}
