package com.rashedkhan.ratings.ui.home.feature;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.rashedkhan.ratings.R;
import com.rashedkhan.ratings.common.BaseFragment;
import com.rashedkhan.ratings.common.adapter.SpinnerAdapter;
import com.rashedkhan.ratings.config.ApiUrl;
import com.rashedkhan.ratings.core.RatingsApplication;
import com.rashedkhan.ratings.core.ResponseListener;
import com.rashedkhan.ratings.core.RtClients;
import com.rashedkhan.ratings.data.implementation.HttpRepository;
import com.rashedkhan.ratings.data.model.Category;
import com.rashedkhan.ratings.data.model.Feature;
import com.rashedkhan.ratings.data.model.FeatureUser;
import com.rashedkhan.ratings.data.model.RatingsCategory;
import com.rashedkhan.ratings.data.repository.IHttpRepository;
import com.rashedkhan.ratings.ui.home.search.SearchFragment;
import com.rashedkhan.ratings.ui.home.setting.SettingFragment;
import com.rashedkhan.ratings.util.Util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AssignFeatureFragment extends BaseFragment {

    private OnFragmentInteractionListener mListener;
    private EditText etName;
    private Spinner spnFeature;
    private ListView lvCategories, llUserList;
    private LinearLayout llUser;
    private Button btnCreate, btnCreateUser;
    private int featureId = 0;
    private IHttpRepository repository;

    public AssignFeatureFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_assign_feature, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        etName = view.findViewById(R.id.etName);
        llUserList = view.findViewById(R.id.llUserList);
        llUser = view.findViewById(R.id.llUser);
        spnFeature = view.findViewById(R.id.spnFeature);
        btnCreateUser = view.findViewById(R.id.btnCreateUser);
        btnCreate = view.findViewById(R.id.btnCreate);
        lvCategories = view.findViewById(R.id.lvCategories);
        repository = new HttpRepository(getActivity());
        if (featureId == 0) {
            getFeatureList();
            generateCategory(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId());
        } else {
            getUserListByFeatureId();
        }
        btnCreate.setOnClickListener(view1 -> {
            submitData();
        });
        btnCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llUserList.setVisibility(View.GONE);
                llUser.setVisibility(View.VISIBLE);
            }
        });
    }

    private void submitData() {
        FeatureUser user = new FeatureUser();
        Map.Entry<String, String> item = (Map.Entry<String, String>) spnFeature.getSelectedItem();
        int fId = Integer.parseInt(item.getKey());
        user.setFeatureId(fId);
        user.setName(etName.getText().toString());
        user.setCategoryId(catList);
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));

        repository.post(ApiUrl.getInstance().crateFeatureUser(), Object.class, RtClients.getInstance().getGson().toJson(user), header, new ResponseListener<Object>() {
            @Override
            public void success(Object response) {
                Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_LONG).show();
                featureId = fId;
                getUserListByFeatureId();
            }

            @Override
            public void error(Throwable error) {
                Toast.makeText(getActivity(), Util.get().getMessage((VolleyError) error), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getUserListByFeatureId() {
        llUser.setVisibility(View.GONE);
        llUserList.setVisibility(View.VISIBLE);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void generateCategory(int userId) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(userId));
        String url = ApiUrl.getInstance().getDefaultCategoriesByUrl(userId);
        repository.getAll(url, Category[].class, header, new ResponseListener<List<Category>>() {
            @Override
            public void success(List<Category> response) {
                setCategories(new LinkedList<>(response));
            }

            @Override
            public void error(Throwable error) {
                //    view.showErrorMessage(Util.get().getMessage((VolleyError) error));
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

    public void setFeatureId(int featureId) {
        this.featureId = featureId;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private LinkedList<Integer> catList = new LinkedList<>();

    private void setCategories(List<Category> categoryList) {
        UserCategoryAdapter adapter = new UserCategoryAdapter(categoryList);
        lvCategories.setAdapter(adapter);
        adapter.setItemCheckedListener(position -> {
            catList.add(categoryList.get(position).getCatId());
        });
        adapter.setItemUnCheckedListener(position -> {
            catList.remove(categoryList.get(position).getCatId());
        });

    }

    private class UserCategoryAdapter extends BaseAdapter {
        private SearchFragment.ClickListener checkClickListener;
        private SearchFragment.ClickListener unCheckClickListener;
        private List<Category> categories;

        public UserCategoryAdapter(List<Category> categories) {
            this.categories = categories;
        }

        @Override
        public int getCount() {
            return categories.size();
        }

        @Override
        public Category getItem(int i) {
            return categories.get(i);
        }

        public void setItemCheckedListener(SearchFragment.ClickListener listener) {
            this.checkClickListener = listener;
        }

        public void setItemUnCheckedListener(SearchFragment.ClickListener listener) {
            this.unCheckClickListener = listener;
        }

        @Override
        public long getItemId(int i) {
            return getItem(i).getCatId();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View row;
            if (view == null) {
                row = LayoutInflater.from(getActivity()).inflate(R.layout.user_category_list_item, null);
            } else {
                row = view;
            }
            CheckBox chkUserCategory = row.findViewById(R.id.chkUserCategory);
            if (!TextUtils.isEmpty(getItem(i).getName())) {
                chkUserCategory.setText(categories.get(i).getName());
            }
            if (chkUserCategory != null)
                chkUserCategory.setOnClickListener(view1 -> {
                    if (chkUserCategory.isChecked()) {
                        checkClickListener.onItemClick(i);
                    } else {
                        unCheckClickListener.onItemClick(i);
                    }
                });

            return row;
        }
    }

    private void getFeatureList() {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));
        repository.getAll(ApiUrl.getInstance().getFeatureList(RatingsApplication.getInstant().getUser().getUserId()), Feature[].class, header, new ResponseListener<List<Feature>>() {
            @Override
            public void success(List<Feature> response) {
                LinkedHashMap<String, String> data = new LinkedHashMap<>();
                data.put("0", "--Choose Feature--");
                for (Feature feature : response) {
                    if (feature.getTitle() != null)
                        data.put(String.valueOf(feature.getFeatureId()), feature.getTitle());
                }
                SpinnerAdapter<String, String> adapter = new SpinnerAdapter<String, String>(getActivity(), android.R.layout.simple_spinner_item, data);
                spnFeature.setAdapter(adapter);
            }

            @Override
            public void error(Throwable error) {
                Toast.makeText(getActivity(), Util.get().getMessage((VolleyError) error), Toast.LENGTH_LONG).show();
            }
        });
    }
}
