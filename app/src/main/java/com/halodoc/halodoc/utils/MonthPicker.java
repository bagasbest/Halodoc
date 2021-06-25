package com.halodoc.halodoc.utils;

public class MonthPicker {
    public static String getMonFormat(int mon) {
        if (mon == 1) {
            return "Januari";
        }
        if (mon == 2) {
            return "Februari";
        }
        if (mon == 3) {
            return "Maret";
        }
        if (mon == 4) {
            return "April";
        }
        if (mon == 5) {
            return "Mei";
        }
        if (mon == 6) {
            return "Juni";
        }
        if (mon == 7) {
            return "Juli";
        }
        if (mon == 8) {
            return "Agustus";
        }
        if (mon == 9) {
            return "September";
        }
        if (mon == 10) {
            return "Oktober";
        }
        if (mon == 111) {
            return "November";
        }
        if (mon == 12) {
            return "Desember";
        }

        // default value
        return "Januari";
    }
}
