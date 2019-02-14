package com.rashedkhan.ratings.ui.home.feature;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.rashedkhan.ratings.data.model.FeatureType;
import com.rashedkhan.ratings.data.repository.IHttpRepository;
import com.rashedkhan.ratings.ui.home.search.SearchFragment;
import com.rashedkhan.ratings.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeatureTypeAssignment extends BaseFragment {

    private ListView lvFeatureTypeList;
    private Button btnCreateFType;
    IHttpRepository repository;
    private Dialog dialog;
    private ImageButton btnBack;

    public FeatureTypeAssignment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feature_type, container, false);
        init(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getFeatureTypes();
    }

    private void getFeatureTypes() {
        dialog = new Dialog(getActivity());
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));
        String url = ApiUrl.getInstance().getFeatureTypes(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId());
        repository.getAll(url, FeatureType[].class, header, new ResponseListener<List<FeatureType>>() {
                    @Override
                    public void success(List<FeatureType> response) {
                        if (dialog != null)
                            dialog.dismiss();
                        FeatureTypeAdapter adapter = new FeatureTypeAdapter(response);
                        lvFeatureTypeList.setAdapter(adapter);
                        adapter.setItemCheckedListener(position -> {
                            response.get(position).setActive(1);
                            updateFeatureType(response.get(position));
                        });
                        adapter.setItemUnCheckedListener(position -> {
                            response.get(position).setActive(0);
                            updateFeatureType(response.get(position));
                        });
                        adapter.onItemClickListener(new SearchFragment.ClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                Intent intent = new Intent(getActivity(), FeatureActivity.class);
                                intent.putExtra(FeatureActivity.FEATURE_TYPE_ID, String.valueOf(response.get(position).getFeatureTypeId()));
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void error(Throwable error) {
                        if (dialog != null)
                            dialog.dismiss();
                        Toast.makeText(getActivity(), Util.get().getMessage((VolleyError) error), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    private void updateFeatureType(FeatureType featureType) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));
        String url = ApiUrl.getInstance().createFeatureType();
        repository.post(url, FeatureType.class, RtClients.getInstance().getGson().toJson(featureType), header, new ResponseListener<FeatureType>() {
            @Override
            public void success(FeatureType response) {
                if (dialog != null)
                    dialog.dismiss();
                Toast.makeText(getActivity(), "Done ", Toast.LENGTH_LONG).show();
                getFeatureTypes();
            }

            @Override
            public void error(Throwable error) {
                if (dialog != null)
                    dialog.dismiss();
                Toast.makeText(getActivity(), Util.get().getMessage((VolleyError) error), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void createFeatureType() {
        dialog = new Dialog(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.create_feature_type, null);
        dialog.setContentView(view);
        dialog.show();
        EditText etFeatureTitle = view.findViewById(R.id.etFeatureTitle);

        Button btnFeatureCreate = view.findViewById(R.id.btnFeatureCreate);
        btnFeatureCreate.setOnClickListener(view1 -> {
            if (RatingsApplication.getInstant().getUser() == null) {
                return;
            }
            if (etFeatureTitle.getText().toString().isEmpty()) {
                etFeatureTitle.setError("Required");
                return;
            }
            FeatureType feature = new FeatureType(0, RatingsApplication.getInstant().getUser().getUserId(), etFeatureTitle.getText().toString(), 1);
            updateFeatureType(feature);
        });
    }

    private void init(View view) {
        repository = new HttpRepository(getActivity());
        lvFeatureTypeList = view.findViewById(R.id.lvFeatureTypeList);
        btnCreateFType = view.findViewById(R.id.btnCreateFType);
        btnCreateFType.setOnClickListener(view1 -> createFeatureType());
    }

    private class FeatureTypeAdapter extends BaseAdapter {
        private List<FeatureType> features;

        private SearchFragment.ClickListener checkClickListener;
        private SearchFragment.ClickListener unCheckClickListener;
        private SearchFragment.ClickListener itemClick;

        public FeatureTypeAdapter(List<FeatureType> features) {
            this.features = features;
        }

        @Override
        public int getCount() {
            return features.size();
        }

        @Override
        public FeatureType getItem(int i) {
            return features.get(i);
        }

        @Override
        public long getItemId(int i) {
            return getItem(i).getFeatureTypeId();
        }

        public void onItemClickListener(SearchFragment.ClickListener listener) {
            this.itemClick = listener;
        }

        public void setItemCheckedListener(SearchFragment.ClickListener listener) {
            this.checkClickListener = listener;
        }

        public void setItemUnCheckedListener(SearchFragment.ClickListener listener) {
            this.unCheckClickListener = listener;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View vw;
            if (view == null) {
                vw = LayoutInflater.from(getActivity()).inflate(R.layout.feature_type_row, null);
            } else {
                vw = view;
            }
            TextView tvFeatureTitle = vw.findViewById(R.id.tvFeatureTitle);
            CheckBox chkUpdateFeature = vw.findViewById(R.id.chkUpdateFeature);
            chkUpdateFeature.setChecked(features.get(i).getActive());
            chkUpdateFeature.setOnClickListener(view1 -> {
                if (chkUpdateFeature.isChecked()) {
                    checkClickListener.onItemClick(i);
                } else {
                    unCheckClickListener.onItemClick(i);
                }
            });
            tvFeatureTitle.setText(getItem(i).getTitle());
            vw.setOnClickListener(view1 -> itemClick.onItemClick(i));
            return vw;
        }
    }
}
