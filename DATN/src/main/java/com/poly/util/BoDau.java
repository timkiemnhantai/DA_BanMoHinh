package com.poly.util;



public class BoDau {
    public static String removeVietnameseTone(String str) {
        str = str.toLowerCase();
        str = java.text.Normalizer.normalize(str, java.text.Normalizer.Form.NFD);
        str = str.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return str.replaceAll("đ", "d");
    }
}
