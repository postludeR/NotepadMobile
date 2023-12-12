package com.example.myapplication;

import android.graphics.Color;
import android.net.Uri;
import android.text.Editable;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;

import java.io.File;

public class Functions {
    static void highlightJavaKeywords(Editable editable) {
        String text = editable.toString();
        String[] words = text.split("\\s+");  // Split the text into words
        int start = 0;

        for (String word : words) {
            int startIndex = text.indexOf(word, start);
            int endIndex = startIndex + word.length();
            int color = SyntaxKeyword.keyword(word);

            if (color != Color.BLACK) {
                editable.setSpan(new ForegroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            start = endIndex + 1;
        }
    }

    static int findNextOccurrence(String text, String query, boolean caseSensitive, boolean circle, int start, int end, boolean up) {
        String searchText = caseSensitive ? text : text.toLowerCase();
        String queryText = caseSensitive ? query : query.toLowerCase();
        int index;

        if (up) {
            index = searchText.lastIndexOf(queryText, end);
            if (index == -1 && circle) {
                index = searchText.lastIndexOf(queryText, text.length());
            }
        } else {
            index = searchText.indexOf(queryText, start);
            if (index == -1 && circle) {
                index = searchText.indexOf(queryText);
            }
        }

        return index;
    }

    static String getFileNameFromUri(Uri uri) {
        String fileName = "Untitled"; // Default if extraction fails

        try {
            String path = uri.getLastPathSegment();
            if (path != null) {
                fileName = new File(path).getName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileName;
    }
}
