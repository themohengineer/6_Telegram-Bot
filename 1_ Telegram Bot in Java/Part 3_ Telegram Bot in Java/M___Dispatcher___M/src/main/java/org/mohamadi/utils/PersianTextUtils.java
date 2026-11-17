package org.mohamadi.utils;

public final class PersianTextUtils {

    private PersianTextUtils() {
    }

    public static String normalize(String text) {
        if (text == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder(text.length() + 2);
        sb.append((char) 8206);

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            sb.append(switch (c) {
                case 'ي' -> 'ی';
                case 'ك' -> 'ک';
                case 'ة' -> 'ه';
                case 'ۀ' -> 'ه';
                case 'أ', 'إ', 'آ' -> 'ا';
                case 'ؤ' -> 'و';
                case 'ئ' -> 'ی';
                case '٠' -> '۰';
                case '١' -> '۱';
                case '٢' -> '۲';
                case '٣' -> '۳';
                case '٤' -> '۴';
                case '٥' -> '۵';
                case '٦' -> '۶';
                case '٧' -> '۷';
                case '٨' -> '۸';
                case '٩' -> '۹';
                default -> c;
            });
        }

        sb.append((char) 8206);
        return sb.toString();
    }
}
