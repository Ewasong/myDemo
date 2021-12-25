package top.sorie.hello;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelloWorld {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("1", "2");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }
}
