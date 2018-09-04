package com.example.khj_pc.twoday;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nishant.math.MathView;

public class ViewerPlaceholderFragment extends Fragment {


    private MathView mathView;

    public ViewerPlaceholderFragment() {
    }

    public String getText() {
        return mathView.getText();
    }

    public void setText(String s) {
        mathView.setText(s);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_viewer_tex, container, false);
        mathView = rootView.findViewById(R.id.math_view);
        return rootView;
    }
}