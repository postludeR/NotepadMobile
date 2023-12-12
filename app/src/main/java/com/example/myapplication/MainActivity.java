package com.example.myapplication;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private String currentFileName = "Untitled";
    private EditText searchEditText;
    private CheckBox caseSensitiveCheckBox;
    private CheckBox circleCheckBox;
    private RadioButton upCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        Button newButton = findViewById(R.id.newButton);
        Button openButton = findViewById(R.id.openButton);
        Button saveButton = findViewById(R.id.saveButton);
        Button cutButton = findViewById(R.id.cutButton);
        Button copyButton = findViewById(R.id.copyButton);
        Button pasteButton = findViewById(R.id.pasteButton);
        // Find views by their IDs
        searchEditText = findViewById(R.id.searchEditText);
        Button searchButton = findViewById(R.id.searchButton);
        caseSensitiveCheckBox = findViewById(R.id.caseSensitiveCheckBox);
        circleCheckBox = findViewById(R.id.circleCheckBox);
        upCheckBox = findViewById(R.id.upCheckBox);

        editText.setSelection(0);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Functions.highlightJavaKeywords(editable);
            }
        });

        searchButton.setOnClickListener(view -> {
            editText.requestFocus();
            if (editText.getSelectionStart() < 0) {
                editText.setSelection(0);
            }

            String searchQuery = searchEditText.getText().toString();

            boolean isCaseSensitive = caseSensitiveCheckBox.isChecked();
            boolean isCircle = circleCheckBox.isChecked();
            boolean isUp = upCheckBox.isChecked();

            performSearch(searchQuery, isCaseSensitive, isCircle, isUp);
        });

        newButton.setOnClickListener(view -> {
            editText.setText("");
            currentFileName = "Untitled";
        });

        openButton.setOnClickListener(view -> {
            openFile();
        });

        saveButton.setOnClickListener(view -> {
            saveFile();
        });

        cutButton.setOnClickListener(view -> {
            String selectedText = editText.getText().toString().substring(editText.getSelectionStart(), editText.getSelectionEnd());
            editText.getText().replace(editText.getSelectionStart(), editText.getSelectionEnd(), "");
            setClipboard(selectedText);
        });

        copyButton.setOnClickListener(view -> {
            String selectedText = editText.getText().toString().substring(editText.getSelectionStart(), editText.getSelectionEnd());
            setClipboard(selectedText);
        });

        pasteButton.setOnClickListener(view -> {
            String clipboardText = getClipboard();
            int cursorPosition = editText.getSelectionStart();
            editText.getText().insert(cursorPosition, clipboardText);
        });
    }

    private void performSearch(String query, boolean caseSensitive, boolean circle, boolean up) {
        String text = editText.getText().toString();

        int searchStart;
        int searchEnd;

        if (up) {
            int cursorPos = editText.getSelectionStart() - 1;
            searchStart = 0;
            searchEnd = cursorPos;
        } else {
            searchStart = editText.getSelectionStart() + 1;
            searchEnd = text.length();
        }

        if (circle) {
            if (up) {
                searchStart = text.length();
            } else {
                searchEnd = 0;
            }
        }

        int index = Functions.findNextOccurrence(text, query, caseSensitive, circle, searchStart, searchEnd, up);

        if (index >= 0) {
            int selectionEnd = index + query.length();
            editText.setSelection(index, selectionEnd);
        } else {
            Toast.makeText(this, "Cannot find '" + query + "'", Toast.LENGTH_SHORT).show();
        }
    }

    private void setClipboard(String text) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
    }

    private String getClipboard() {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        if (clipboard.hasPrimaryClip()) {
            return Objects.requireNonNull(clipboard.getPrimaryClip()).getItemAt(0).getText().toString();
        }
        return "";
    }

    // Request code for file picker
    private static final int REQUEST_CODE_OPEN_FILE = 1;

    private void openFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE_OPEN_FILE);
    }

    // Handle the result from the file picker
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_OPEN_FILE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri fileUri = data.getData();

                currentFileName = Functions.getFileNameFromUri(fileUri);

                try {
                    ContentResolver contentResolver = getContentResolver();
                    assert fileUri != null;
                    InputStream inputStream = contentResolver.openInputStream(fileUri);

                    if (inputStream != null) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        String line;
                        StringBuilder fileContent = new StringBuilder();

                        while ((line = reader.readLine()) != null) {
                            fileContent.append(line).append("\n");
                        }
                        editText.setText(fileContent.toString());

                        reader.close();
                        inputStream.close();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static final int REQUEST_CODE_SAVE_FILE = 2;

    private void saveFile() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Set the generic type
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_TITLE, currentFileName);

        startActivityForResult(intent, REQUEST_CODE_SAVE_FILE);
    }


}
