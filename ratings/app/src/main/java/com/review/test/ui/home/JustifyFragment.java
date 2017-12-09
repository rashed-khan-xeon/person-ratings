package com.review.test.ui.home;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.review.test.R;
import com.review.test.common.BaseFragment;
import com.review.test.common.ViewInitializer;

/**
 * A simple {@link Fragment} subclass.
 */
public class JustifyFragment extends BaseFragment implements ViewInitializer {
    private Context context;

    public JustifyFragment() {
        // Required empty public constructor
    }

    View justifyView;
    private Button btnJustificationSubmit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        justifyView = inflater.inflate(R.layout.fragment_justify, container, false);
        initViewComponent();
        return justifyView;
    }

    @Override
    public void initViewComponent() {
        btnJustificationSubmit = justifyView.findViewById(R.id.btnJustificationSubmit);
        btnJustificationSubmit.setOnClickListener(view -> {
            Toast.makeText(context, "Your justification has been submitted", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(context, HomeActivity.class));
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
