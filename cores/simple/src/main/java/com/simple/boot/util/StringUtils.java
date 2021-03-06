package com.simple.boot.util;

import java.util.regex.PatternSyntaxException;

public class StringUtils {
    static public boolean  isMatches(String str,String regex){
        boolean sw = false;
        try {
            if (str.matches(regex)){
                sw = true;
            }else{
                sw = false;
            }
        } catch (PatternSyntaxException e) { // 정규식에 에러가 있다면
        }
        return sw;

    }
}
