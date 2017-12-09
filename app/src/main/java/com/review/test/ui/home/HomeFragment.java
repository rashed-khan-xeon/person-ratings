package com.review.test.ui.home;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.review.test.R;
import com.review.test.common.BaseFragment;
import com.review.test.common.ViewInitializer;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment implements ViewInitializer {
    Transfer transfer;

    public HomeFragment() {
        // Required empty public constructor
    }

    View homeView;
    private CardView demoPerson;
    private Button btnSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeView = inflater.inflate(R.layout.fragment_home, container, false);
        initViewComponent();
        return homeView;
    }

    @Override
    public void initViewComponent() {
        btnSearch = homeView.findViewById(R.id.btnSearch);
        demoPerson = homeView.findViewById(R.id.demoPerson);
        btnSearch.setOnClickListener(view -> {
            demoPerson.setVisibility(View.VISIBLE);
        });
        demoPerson.setOnClickListener((v) -> {
            transfer.onSearchItemClickInSearch(JustifyFragment.class);
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        transfer = (Transfer) context;
    }

    public interface Transfer {
        void onSearchItemClickInSearch(Class c);
    }
}
