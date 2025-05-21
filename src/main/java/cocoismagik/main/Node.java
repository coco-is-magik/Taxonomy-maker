package cocoismagik.main;

import java.io.Serializable;

public class Node implements Serializable{
    String data, data2, data3;
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
