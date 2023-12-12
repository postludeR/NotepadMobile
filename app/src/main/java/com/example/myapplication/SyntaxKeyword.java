package com.example.myapplication;

import static android.graphics.Color.*;

import android.graphics.Color;

public class SyntaxKeyword {

    //These groups of keyword was defined when I accomplished one of the assignment in Software Engineering Class. I also do some edit on it.

    public static String[] orange = {"private", "protected", "public", "try", "catch", "throw", "import", "package", "true", "false", "register", "reinterpret_cast", "namespace"
            , "and", "@author", "@version", "@date", "/**", "*", "*/", "or", "as", "assert", "async",
            "await", "class", "def", "del", "cout", "cin", "endl", "println"};

    public static String[] blue = {"void", "byte", "char", "short", "Short", "integer", "long", "float", "double","String[]","String","File",
            "boolean", "String", "Float", "Double", "Long", "Integer", "Boolean", "bool", "char", "int", "unsigned", "signed",
            "short", "long", "from", "global", "in", "is", "List", "Tuple", "Dictionary", "Set", "Number", "delete", "do", "dynamic_cast", "else", "enum", "explicit", "export",
            "for", "friend", "JFrame", "JPane", "JLabel", "TextFiled", "JTextPanel", "JButton","JMenu", "JMenuItem", "JPopMenu","JMenuBar","JFileChooser", "JScrollPane","Button", "EditText"};

    public static String[] cyan = {"abstract", "class", "extends", "final", "implements", "interface", "native", "new", "static", "lambda", "None",
            "nonlocal", "not", "inlines", "mutable", "new", "operator", "System"};

    public static String[] pink = {"break", "continue", "return", "do", "while", "if", "elif", "else", "for", "instanceof", "switch", "case", "default"
            , "try", "typedef", "typeid", "virtual", "void", "volatile", "union"};

    public static String[] magenta = {"super", "this","using", "std",
            "complex", "bool", "pass", "with", "yield", "enum", "explicit", "#include", "@Override"};


    public static int keyword(String str) {
        int color = BLACK;

        for (String s : orange) {
            if (str.trim().equals(s)) {
                color = Color.parseColor("#FFA500");
                break;
            }
        }

        for (String s : blue) {
            if (str.trim().equals(s)) {
                color = BLUE;
                break;
            }
        }

        for (String s : cyan) {
            if (str.trim().equals(s)) {
                color = CYAN;
                break;
            }
        }

        for (String s : pink) {
            if (str.trim().equals(s)) {
                color = Color.parseColor("#FFC0CB");
                break;
            }
        }

        for (String s : magenta) {
            if (str.trim().equals(s)) {
                color = MAGENTA;
                break;
            }
        }
        return color;
    }
}

