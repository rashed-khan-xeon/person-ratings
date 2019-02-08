package com.rashedkhan.ratings.ui.home.feature;

import android.app.Dialog;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AssignFeatureFragment extends BaseFragment {

    private OnFragmentInteractionListener mListener;
    private EditText etName;
    private Spinner spnFeature;
    private ListView lvCategories;
    private LinearLayout llUser;
    private Button btnCreate, btnCreateCategory, btnShowCategories;
    private int featureId = 0;
    private IHttpRepository repository;
    private Dialog dialog;

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
        llUser = view.findViewById(R.id.llUser);
        spnFeature = view.findViewById(R.id.spnFeature);
        btnCreateCategory = view.findViewById(R.id.btnCreateCategory);
        btnShowCategories = view.findViewById(R.id.btnShowCategory);
        btnCreate = view.findViewById(R.id.btnCreate);
        lvCategories = view.findViewById(R.id.lvCategories);
        repository = new HttpRepository(getActivity());
        int userId = RatingsApplication.getInstant().getRatingsPref().getUser().getUserId();
        if (featureId == 0) {
            getFeatureList();
            generateCategory(userId);
        } else {

        }
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                AssignFeatureFragment.this.submitData();
            }
        });
        btnCreateCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view12) {
                AssignFeatureFragment.this.addCategory();
            }
        });
        btnShowCategories.setOnClickListener(view13 -> showCategory(userId));
    }

    private void submitData() {
        if (etName.getText().toString().isEmpty()) {
            etName.setError("Required");
            return;
        }
        FeatureUser user = new FeatureUser();
        Map.Entry<String, String> item = (Map.Entry<String, String>) spnFeature.getSelectedItem();
        int fId = Integer.parseInt(item.getKey());
        if (fId == 0) {
            Toast.makeText(getActivity(), "Please select a feature ", Toast.LENGTH_LONG).show();
            return;
        }
        user.setFeatureId(fId);
        user.setName(etName.getText().toString());
        List<Integer> cats = new ArrayList<>();
        for (Category ct : catList) {
            cats.add(ct.getCatId());
        }
        user.setCategoryId(cats);
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));

        repository.post(ApiUrl.getInstance().crateFeatureUser(), Object.class, RtClients.getInstance().getGson().toJson(user), header, new ResponseListener<Object>() {
            @Override
            public void success(Object response) {
                Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_LONG).show();
                featureId = fId;
                clearView();
            }

            @Override
            public void error(Throwable error) {
                Toast.makeText(getActivity(), Util.get().getMessage((VolleyError) error), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void clearView() {
        etName.setText("");
        spnFeature.setSelection(0);
        generateCategory(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId());
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

    private LinkedList<Category> catList = new LinkedList<>();

    private void setCategories(List<Category> categoryList) {
        catList.clear();
        UserCategoryAdapter adapter = new UserCategoryAdapter(categoryList, false);
        lvCategories.setAdapter(adapter);
        adapter.setItemCheckedListener(new SearchFragment.ClickListener() {
            @Override
            public void onItemClick(int position) {
                if (!catList.contains(categoryList.get(position)))
                    catList.add(categoryList.get(position));
            }
        });
        adapter.setItemUnCheckedListener(new SearchFragment.ClickListener() {
            @Override
            public void onItemClick(int position) {
                catList.remove(categoryList.get(position));
            }
        });

    }

    private class UserCategoryAdapter extends BaseAdapter {
        private SearchFragment.ClickListener checkClickListener;
        private SearchFragment.ClickListener unCheckClickListener;
        private List<Category> categories;
        private boolean forUpdate = false;

        public UserCategoryAdapter(List<Category> categories, boolean forUpdate) {
            this.categories = categories;
            this.forUpdate = forUpdate;
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
            if (forUpdate) {
                chkUserCategory.setChecked(categories.get(i).getActive());
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
        repository.getAll(ApiUrl.getInstance().getActiveFeatureList(RatingsApplication.getInstant().getUser().getUserId()), Feature[].class, header, new ResponseListener<List<Feature>>() {
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

    private void addCategory() {
        dialog = new Dialog(getActivity());
        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.add_category, null);
        dialog.setContentView(layout);
        dialog.show();
        Button btnAddCategory = layout.findViewById(R.id.btnAddCategory);
        EditText etCategoryName = layout.findViewById(R.id.etCategoryName);
        btnAddCategory.setOnClickListener(view -> {
            if (etCategoryName.getText().toString().isEmpty()) {
                Util.get().showToastMsg(getActivity(), "Please type category name");
                return;
            }
            int userId = RatingsApplication.getInstant().getRatingsPref().getUser().getUserId();
            Category category = new Category();
            category.setCatId(0);
            category.setActive(1);
            category.setName(etCategoryName.getText().toString());
            category.setUserId(userId);
            category.setUserTypeId(0);
            Map<String, String> header = new HashMap<>();
            header.put("Content-Type", "application/json");
            header.put("accessToken", String.valueOf(userId));
            repository.post(ApiUrl.getInstance().getAddCategoryUrl(), Category.class, RtClients.getInstance().getGson().toJson(category), header,
                    new ResponseListener<Category>() {
                        @Override
                        public void success(Category response) {
                            Toast.makeText(getActivity(), "Category Added", Toast.LENGTH_LONG).show();
                            generateCategory(response.getUserId());
                            if (dialog != null) {
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void error(Throwable error) {
                            if (dialog != null) {
                                dialog.dismiss();
                            }
                            Toast.makeText(getActivity(), Util.get().getMessage((VolleyError) error), Toast.LENGTH_LONG).show();
                        }
                    });
        });


    }

    private void showCategory(int userId) {
        dialog = new Dialog(getActivity());
        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.show_category, null);
        dialog.setContentView(layout);
        dialog.show();
        ListView lvShowCategories = layout.findViewById(R.id.lvShowCategories);
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(userId));
        String url = ApiUrl.getInstance().getAllCategoriesByUserIdUrl(userId);
        repository.getAll(url, Category[].class, header, new ResponseListener<List<Category>>() {
            @Override
            public void success(List<Category> response) {
                UserCategoryAdapter adapter = new UserCategoryAdapter(response, true);
                lvShowCategories.setAdapter(adapter);
                adapter.setItemCheckedListener(new SearchFragment.ClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        changeActiveStatusOfCat(response.get(position).getCatId(), response.get(position).getUserId(), 1);
                    }
                });
                adapter.setItemUnCheckedListener(new SearchFragment.ClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        changeActiveStatusOfCat(response.get(position).getCatId(), response.get(position).getUserId(), 0);
                    }
                });
            }

            @Override
            public void error(Throwable error) {
                //    view.showErrorMessage(Util.get().getMessage((VolleyError) error));
            }
        });
    }

    private void changeActiveStatusOfCat(int catId, int userId, int active) {
        Category rc = new Category();
        rc.setUserId(userId);
        rc.setCatId(catId);
        rc.setActive(active);
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(userId));
        String url = ApiUrl.getInstance().getAddCategoryUrl();
        repository.post(url, Object.class, RtClients.getInstance().getGson().toJson(rc), header, new ResponseListener<Object>() {
            @Override
            public void success(Object response) {
                Toast.makeText(getActivity(), "Category Updated", Toast.LENGTH_LONG).show();
                generateCategory(userId);
            }

            @Override
            public void error(Throwable error) {
                Toast.makeText(getActivity(), Util.get().getMessage((VolleyError) error), Toast.LENGTH_LONG).show();
            }
        });
    }
}
