package me.donghun.vanilla;

public class Test {

    public static void main(String[] args) {
        String str = "abc'de";
        System.out.println(str);
        System.out.println(str.replaceAll("'", "\\'"));
    }
}
