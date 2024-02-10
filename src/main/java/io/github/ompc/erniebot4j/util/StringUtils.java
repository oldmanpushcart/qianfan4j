package io.github.ompc.erniebot4j.util;

import java.util.Objects;

/**
 * 字符串工具类
 */
public class StringUtils {

    public static boolean isBlank(String string) {
        return !isNotBlank(string);
    }

    /**
     * 是否为空字符串
     *
     * @param string 字符串
     * @return TRUE | FALSE
     */
    public static boolean isNotBlank(String string) {
        return Objects.nonNull(string)
                && !string.isBlank();
    }

    private static final char[] HEX_UPPER_CHARS = "0123456789ABCDEF".toCharArray();
    private static final char[] HEX_LOWER_CHARS = "0123456789abcdef".toCharArray();

    public static String encodeHex(byte[] data, boolean isLowerCase) {

        // null to null
        if (data == null) {
            return null;
        }

        final var hexChars = isLowerCase ? HEX_LOWER_CHARS : HEX_UPPER_CHARS;
        final var length = data.length;

        // 两个字符表示一个字节，所以长度要乘以2
        final var out = new char[length << 1];

        for (int i = 0, j = 0; i < length; i++) {

            // 取高4位
            out[j++] = hexChars[(0xF0 & data[i]) >>> 4];

            // 取低4位
            out[j++] = hexChars[0x0F & data[i]];

        }

        return new String(out);
    }

    /**
     * 转半角的函数(DBC case)
     * <p>
     * <li>全角空格为12288，半角空格为32</li>
     * <li>全角句号为12290，半角句号为46</li>
     * <li>其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248</li>
     * </p>
     *
     * @param string 任意字符串
     * @return 半角字符串
     */
    public static String toDBC(String string) {
        char[] c = string.toCharArray();
        for (int i = 0; i < c.length; i++) {

            //全角空格为12288，半角空格为32
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }

            // 全角句号为12290，半角句号为46
            if (c[i] == 12290) {
                c[i] = (char) 46;
            }

            //其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
            if (c[i] > 65280 && c[i] < 65375)

                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 转换为蛇形命名字符串
     *
     * @param string 目标字符串
     * @return 蛇形命名字符串
     */
    public static String toSnake(String string) {
        if (isBlank(string)) {
            return string;
        }
        final var result = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            char ch = string.charAt(i);
            if (Character.isUpperCase(ch)) {
                if (i != 0) {
                    result.append("_");
                }
                result.append(Character.toLowerCase(ch));
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }

}
