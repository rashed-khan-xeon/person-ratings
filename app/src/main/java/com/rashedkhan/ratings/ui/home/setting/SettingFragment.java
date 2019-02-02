package com.rashedkhan.ratings.ui.home.setting;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.rashedkhan.ratings.R;
import com.rashedkhan.ratings.common.BaseFragment;
import com.rashedkhan.ratings.common.adapter.ExpandedListView;
import com.rashedkhan.ratings.config.ApiUrl;
import com.rashedkhan.ratings.core.RatingsApplication;
import com.rashedkhan.ratings.core.RtClients;
import com.rashedkhan.ratings.data.implementation.HttpRepository;
import com.rashedkhan.ratings.data.model.Category;
import com.rashedkhan.ratings.data.model.RatingsCategory;
import com.rashedkhan.ratings.data.model.RatingsPref;
import com.rashedkhan.ratings.data.model.User;
import com.rashedkhan.ratings.data.model.UserSetting;
import com.rashedkhan.ratings.ui.home.auth.LoginActivity;
import com.rashedkhan.ratings.ui.home.search.SearchFragment;
import com.rashedkhan.ratings.util.Util;

import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends BaseFragment implements SettingContract.SettingView {
    private Context context;
    private View settingView;
    private CheckBox chkHasAddress, chkHasEmail, chkHasPhone, chkHasReview, chkHasRatings, chkProfileImage;
    private Button btnSettingSaveChanges;
    private ExpandedListView lvCategories;
    private SettingContract.SettingPresenter presenter;
    private List<Category> categoryList;
    private List<RatingsCategory> ratingsCategories;
    private InterstitialAd mInterstitialAd;
    private TextView tvCreateYourCategory, tvCheckCategory;
    Dialog dialog;
    private User user;

    public SettingFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        settingView = inflater.inflate(R.layout.fragment_setting, container, false);
        if (RatingsApplication.getInstant().getUser() != null) {
            user = RatingsApplication.getInstant().getUser();
        } else {
            RatingsApplication.getInstant().removePreference();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
        presenter = new SettingPresenter(this, new HttpRepository(context));
        initViewComponents();
        setPrivacyToView();
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getString(R.string.admob_ad_id));
        // mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        return settingView;
    }

    private void setPrivacyToView() {

        if (user != null) {
            if (user.getUserSetting() != null) {
                UserSetting us = user.getUserSetting();
                chkHasAddress.setChecked(us.getAddressVisible());
                chkHasEmail.setChecked(us.getEmailVisible());
                chkHasPhone.setChecked(us.getPhoneNumberVisible());
                chkHasRatings.setChecked(us.getHasRating());
                chkHasReview.setChecked(us.getHasReview());
                chkProfileImage.setChecked(us.getImageVisible());
            }
        }

    }

    @Override
    public void initViewComponents() {
        tvCheckCategory = settingView.findViewById(R.id.tvCheckCategory);
        tvCreateYourCategory = settingView.findViewById(R.id.tvCreateYourCategory);
        lvCategories = settingView.findViewById(R.id.lvCategories);
        btnSettingSaveChanges = settingView.findViewById(R.id.btnSettingSaveChanges);
        chkHasAddress = settingView.findViewById(R.id.chkHasAddress);
        chkHasEmail = settingView.findViewById(R.id.chkHasEmail);
        chkHasPhone = settingView.findViewById(R.id.chkHasPhone);
        chkHasReview = settingView.findViewById(R.id.chkHasReview);
        chkHasRatings = settingView.findViewById(R.id.chkHasRatings);
        chkProfileImage = settingView.findViewById(R.id.chkProfileImage);
        if (user != null) {
            presenter.getCategoriesByUserId(ApiUrl.getInstance().getCategoriesByUserIdUrl(user.getUserId()));
            presenter.getUserRatingsCategory(ApiUrl.getInstance().getUserRatingsCategoryUrl(user.getUserId()));
        }
        btnSettingSaveChanges.setOnClickListener(view -> {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }
            updateSetting();
        });
        tvCreateYourCategory.setOnClickListener(view -> {
            addCategory();
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
            Category category = new Category();
            category.setCatId(0);
            category.setActive(1);
            category.setName(etCategoryName.getText().toString());
            category.setUserId(user.getUserId());
            category.setUserTypeId(user.getUserTypeId());
            presenter.addCategory(ApiUrl.getInstance().getAddCategoryUrl(), RtClients.getInstance().getGson().toJson(category));
        });


    }

    private void updateSetting() {
        if (user != null) {
            UserSetting setting = new UserSetting();
            setting.setUserId(user.getUserId());
            setting.setAddressVisible(chkHasAddress.isChecked() ? 1 : 0);
            setting.setEmailVisible(chkHasEmail.isChecked() ? 1 : 0);
            setting.setHasRating(chkHasRatings.isChecked() ? 1 : 0);
            setting.setHasReview(chkHasReview.isChecked() ? 1 : 0);
            setting.setImageVisible(chkProfileImage.isChecked() ? 1 : 0);
            setting.setPhoneNumberVisible(chkHasPhone.isChecked() ? 1 : 0);
            presenter.updateUserSetting(ApiUrl.getInstance().getUpdateUserSettingsUrl(), RtClients.getInstance().getGson().toJson(setting));
        }
    }

    @Override
    public void settingsUpdated(UserSetting setting) {
        Util.get().showToastMsg(getActivity(), "Settings updated !");
        RatingsPref rtp = RatingsApplication.getInstant().getRatingsPref();
        if (rtp != null) {
            if (rtp.getUser() != null) {
                User user = rtp.getUser();
                user.setUserSetting(setting);
                rtp.setUser(user);
                RatingsApplication.getInstant().setPreference(rtp);
            }
        }
    }

    @Override
    public void showSuccessMessage(String msg) {
        Util.get().showToastMsg(getActivity(), msg);
    }

    @Override
    public void showErrorMessage(String msg) {
        Util.get().showToastMsg(getActivity(), msg);
    }

    @Override
    public void setCategories(List<Category> categories) {
        if (categories != null) {
            if (categories.size() > 0) {
                tvCheckCategory.setVisibility(View.VISIBLE);
                categoryList = categories;
                UserCategoryAdapter adapter = new UserCategoryAdapter(categoryList);
                lvCategories.setAdapter(adapter);
                lvCategories.setExpanded(true);
                User user = RatingsApplication.getInstant().getRatingsPref().getUser();
                adapter.setItemCheckedListener(position -> activeRatingsCat(categories.get(position), user.getUserId()));
                adapter.setItemUnCheckedListener(position -> inActiveRatingsCat(categories.get(position), user.getUserId()));
            }
        }
    }

    @Override
    public void categoryAdded(Category category) {
        if (dialog != null) {
            dialog.dismiss();
        }
        Util.get().showToastMsg(getActivity(), "Added");
        if (categoryList == null) {
            categoryList = new LinkedList<>();
        }
        this.categoryList.add(category);
        UserCategoryAdapter adapter = (UserCategoryAdapter) lvCategories.getAdapter();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else {
            tvCheckCategory.setVisibility(View.VISIBLE);
            UserCategoryAdapter newAdapter = new UserCategoryAdapter(categoryList);
            lvCategories.setAdapter(newAdapter);
            lvCategories.setExpanded(true);
            newAdapter.setItemCheckedListener(position -> activeRatingsCat(category, user.getUserId()));
            newAdapter.setItemUnCheckedListener(position -> inActiveRatingsCat(category, user.getUserId()));
        }
        activeRatingsCat(category, category.getUserId());
    }

    private void activeRatingsCat(Category cat, int userId) {
        RatingsCategory rc = new RatingsCategory();
        rc.setUserId(userId);
        rc.setCatId(cat.getCatId());
        rc.setActive(1);
        presenter.addRatingsCategory(ApiUrl.getInstance().getAddRatingsCategoriesUrl(), RtClients.getInstance().getGson().toJson(rc));
    }

    private void inActiveRatingsCat(Category cat, int userId) {
        RatingsCategory rc = new RatingsCategory();
        rc.setUserId(userId);
        rc.setCatId(cat.getCatId());
        rc.setActive(0);
        presenter.addRatingsCategory(ApiUrl.getInstance().getAddRatingsCategoriesUrl(), RtClients.getInstance().getGson().toJson(rc));

    }

    @Override
    public void setUserRatingsCategories(List<RatingsCategory> ratingsCategories) {
        this.ratingsCategories = ratingsCategories;
        UserCategoryAdapter adapter = (UserCategoryAdapter) lvCategories.getAdapter();
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    @Override
    public void ratingsCategoryAdded(String msg) {
        Util.get().showToastMsg(getActivity(), msg);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
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
                row = LayoutInflater.from(context).inflate(R.layout.user_category_list_item, null);
            } else {
                row = view;
            }
            CheckBox chkUserCategory = row.findViewById(R.id.chkUserCategory);
            if (!TextUtils.isEmpty(getItem(i).getName())) {
                chkUserCategory.setText(categories.get(i).getName());
            }
            if (ratingsCategories != null) {
                for (RatingsCategory rtc : ratingsCategories) {
                    if (rtc.getCatId() == categories.get(i).getCatId()) {
                        if (rtc.getActive() == 1)
                            chkUserCategory.setChecked(true);
                    }
                }
            }
//            else {
//                if (categories != null) {
//                    for (Category rtc : categories) {
//                        if (!rtc.getIsDefault())
//                            if (!chkUserCategory.isChecked())
//                                chkUserCategory.setChecked(true);
//
//                    }
//                }
//            }
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

}
