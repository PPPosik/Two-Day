package com.example.khj_pc.twoday;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daquexian.flexiblerichtextview.FlexibleRichTextView;
import com.nishant.math.MathView;

public class ViewerPlaceholderFragment extends Fragment {


    private FlexibleRichTextView mathView;

    public ViewerPlaceholderFragment() {
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