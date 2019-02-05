package com.rashedkhan.ratings.ui.home.feature;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.rashedkhan.ratings.R;
import com.rashedkhan.ratings.common.BaseFragment;
import com.rashedkhan.ratings.config.ApiUrl;
import com.rashedkhan.ratings.core.RatingsApplication;
import com.rashedkhan.ratings.core.ResponseListener;
import com.rashedkhan.ratings.core.RtClients;
import com.rashedkhan.ratings.data.implementation.HttpRepository;
import com.rashedkhan.ratings.data.model.Feature;
import com.rashedkhan.ratings.data.repository.IHttpRepository;
import com.rashedkhan.ratings.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeatureFragment extends BaseFragment {

    private OnFragmentInteractionListener mListener;
    private Button btnCreateFeature, btnAssign;
    private ListView lvFeatureList;
    private IHttpRepository repository;
    private Dialog dialog;

    public FeatureFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feature, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        repository = new HttpRepository(getActivity());
        btnAssign = view.findViewById(R.id.btnAssign);
        btnCreateFeature = view.findViewById(R.id.btnCreateFeature);
        lvFeatureList = view.findViewById(R.id.lvFeatureList);
        btnCreateFeature.setOnClickListener(view1 -> {
            createFeature();
        });
        btnAssign.setOnClickListener(view12 -> {
            mListener.assignUserToFeature(0);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getFeatureList();
    }

    private void getFeatureList() {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));
        repository.getAll(ApiUrl.getInstance().getFeatureList(RatingsApplication.getInstant().getUser().getUserId()), Feature[].class, header, new ResponseListener<List<Feature>>() {
            @Override
            public void success(List<Feature> response) {
                lvFeatureList.setAdapter(new FeatureAdapter(response));
                lvFeatureList.setOnItemClickListener((adapterView, view, i, l) -> {
                    mListener.assignUserToFeature(response.get(i).getFeatureId());
                });
            }

            @Override
            public void error(Throwable error) {
                Toast.makeText(getActivity(), Util.get().getMessage((VolleyError) error), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void assignUserToFeature(int featureId);
    }

    private class FeatureAdapter extends BaseAdapter {
        private List<Feature> features;

        public FeatureAdapter(List<Feature> features) {
            this.features = features;
        }

        @Override
        public int getCount() {
            return features.size();
        }

        @Override
        public Feature getItem(int i) {
            return features.get(i);
        }

        @Override
        public long getItemId(int i) {
            return getItem(i).getFeatureId();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View vw;
            if (view == null) {
                vw = LayoutInflater.from(getActivity()).inflate(R.layout.feature_row, null);
            } else {
                vw = view;
            }
            TextView tvFeatureTitle = vw.findViewById(R.id.tvFeatureTitle);
            tvFeatureTitle.setText(getItem(i).getTitle());
            return vw;
        }
    }

    private void createFeature() {
        dialog = new Dialog(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.create_feature, null);
        dialog.setContentView(view);
        dialog.show();
        EditText etFeatureTitle = view.findViewById(R.id.etFeatureTitle);
        Button btnFeatureCreate = view.findViewById(R.id.btnFeatureCreate);
        btnFeatureCreate.setOnClickListener(view1 -> {
            if (RatingsApplication.getInstant().getUser() == null) {
                return;
            }
            Feature feature = new Feature();
            feature.setCreatedUserId(RatingsApplication.getInstant().getUser().getUserId());
            feature.setTitle(etFeatureTitle.getText().toString());
            submitFeature(feature);
        });


    }

    private void submitFeature(Feature feature) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));
        String url = ApiUrl.getInstance().createFeature();
        repository.post(url, Feature.class, RtClients.getInstance().getGson().toJson(feature), header, new ResponseListener<Feature>() {
            @Override
            public void success(Feature response) {
                if (dialog != null)
                    dialog.dismiss();
                getFeatureList();
            }

            @Override
            public void error(Throwable error) {
                if (dialog != null)
                    dialog.dismiss();
                Toast.makeText(getActivity(), Util.get().getMessage((VolleyError) error), Toast.LENGTH_LONG).show();
            }
        });
    }

}
