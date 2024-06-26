package com.example.springSecurity.util;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;

@Component
public class AsideUtil {
    public String getTodayQuote(String filename) {
        String result = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename), 1024);
            int index = (int) Math.floor(Math.random() * 100);
            for (int i = 0; i <= index; i++) {  // index = 10
                result = br.readLine();
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
