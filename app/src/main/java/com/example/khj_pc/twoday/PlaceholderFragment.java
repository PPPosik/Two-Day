package com.example.khj_pc.twoday;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class PlaceholderFragment extends Fragment {

    private EditText editText;

    public PlaceholderFragment() {
    }

    public String getText() {
        return editText.getText().toString();
    }

    public void setText(String s) {
        editText.setText(s);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tex, container, false);
        editText = rootView.findViewById(R.id.editText);
        editText.setCustomSelectionActionModeCallback(new CustomCallback(getContext(), editText));

        return rootView;
    }
}