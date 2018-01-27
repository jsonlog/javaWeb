package org.smart4j.chapter2.util;

public final class CastUtil {
    public static String castString(Object obj) {
        return castString(obj,"");
    }

    public static String castString(Object obj,String defaultValue) {
        return obj!=null?String.valueOf(obj):defaultValue;
    }


    public static double castDouble(Object obj) {
        return castDouble(obj,0);
    }

    public static double castDouble(Object obj,double defaultValue) {
        double value = defaultValue;
        if(obj!=null) {
            String strValue = castString(obj);
            if(StringUtil.isNotEmpty(strValue)) {
                try {
                    value = Double.parseDouble(strValue);
                }catch(NumberFormatException e) {
                    value = defaultValue;
                }
            }
        }
        return value;
    }


    public static long castLong(Object obj) {
        return castLong(obj,0);
    }

    public static long castLong(Object obj,long defaultValue) {
        long value = defaultValue;
        if(obj!=null) {
            String strValue = castString(obj);
            if(StringUtil.isNotEmpty(strValue)) {
                try {
                    value = Long.parseLong(strValue);
                }catch(NumberFormatException e) {
                    value = defaultValue;
                }
            }

        }
        return value;
    }


    public static int castInt(Object obj) {
        return castInt(obj,0);
    }

    public static int castInt(Object obj,int defaultValue) {
        int value = defaultValue;
        if(obj!=null) {
            String strValue = castString(obj);
            if(StringUtil.isNotEmpty(strValue)) {
                try {
                    value = Integer.parseInt(strValue);
                }catch(NumberFormatException e) {
                    value = defaultValue;
                }
            }
        }
        return value;
    }


    public static boolean castBoolean(Object obj) {
        return castBoolean(obj,false);
    }

    public static boolean castBoolean(Object obj,boolean defaultValue) {
        boolean value = defaultValue;
        if(obj!=null) {
            String strValue = castString(obj);
            if(StringUtil.isNotEmpty(strValue)) {
                try {
                    value = Boolean.parseBoolean(strValue);
                }catch(NumberFormatException e) {
                    value = defaultValue;
                }
            }
        }
        return value;
    }

}
