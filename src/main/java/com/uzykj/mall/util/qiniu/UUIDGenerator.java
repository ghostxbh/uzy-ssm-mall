package com.uzykj.mall.util.qiniu;

import java.util.UUID;

public class UUIDGenerator {
    
    private UUIDGenerator() {}
    
    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    
    public static String generateUUIDhavaline() {
    	return UUID.randomUUID().toString().toUpperCase();
    }
    public static void main(String[] args){ 
        System.out.println(generateUUIDhavaline());
        System.out.println(generateUUID());
    } 
 
}
