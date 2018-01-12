package com.review.test.ui.home.setting;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.review.test.R;
import com.review.test.common.BaseFragment;
import com.review.test.config.ApiUrl;
import com.review.test.core.RatingsApplication;
import com.review.test.core.RtClients;
import com.review.test.data.implementation.HttpRepository;
import com.review.test.data.model.Category;
import com.review.test.data.model.RatingsCategory;
import com.review.test.data.model.RatingsPref;
import com.review.test.data.model.User;
import com.review.test.data.model.UserSetting;
import com.review.test.ui.home.search.SearchFragment;
import com.review.test.util.Util;

import java.util.ArrayList;
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
    private ListView lvCategories;
    private SettingContract.SettingPresenter presenter;
    private List<Category> categoryList;
    private List<RatingsCategory> ratingsCategories;

    public SettingFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        settingView = inflater.inflate(R.layout.fragment_setting, container, false);
        presenter = new SettingPresenter(this, new HttpRepository(context));
        initViewComponents();
        setPrivacyToView();
        return settingView;
    }

    private void setPrivacyToView() {
        if (RatingsApplication.getInstant().getRatingsPref() != null) {
            if (RatingsApplication.getInstant().getRatingsPref().getUser() != null) {
                User user = RatingsApplication.getInstant().getRatingsPref().getUser();
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
    }

    @Override
    public void initViewComponents() {
        lvCategories = settingView.findViewById(R.id.lvCategories);
        btnSettingSaveChanges = settingView.findViewById(R.id.btnSettingSaveChanges);
        chkHasAddress = settingView.findViewById(R.id.chkHasAddress);
        chkHasEmail = settingView.findViewById(R.id.chkHasEmail);
        chkHasPhone = settingView.findViewById(R.id.chkHasPhone);
        chkHasReview = settingView.findViewById(R.id.chkHasReview);
        chkHasRatings = settingView.findViewById(R.id.chkHasRatings);
        chkProfileImage = settingView.findViewById(R.id.chkProfileImage);
        if (RatingsApplication.getInstant().getRatingsPref() != null)
            if (RatingsApplication.getInstant().getRatingsPref().getUser() != null) {
                presenter.getCategoriesByUserType(ApiUrl.getInstance().getCategoriesByUserTypeIdUrl(RatingsApplication.getInstant().getRatingsPref().getUser().getUserTypeId()));
                presenter.getUserRatingsCategory(ApiUrl.getInstance().getUserRatingsCategoryUrl(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));
            }
        btnSettingSaveChanges.setOnClickListener(view -> {
            updateSetting();
        });
    }

    private void updateSetting() {
        UserSetting setting = new UserSetting();
        setting.setUserId(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId());
        setting.setAddressVisible(chkHasAddress.isChecked() ? 1 : 0);
        setting.setEmailVisible(chkHasEmail.isChecked() ? 1 : 0);
        setting.setHasRating(chkHasRatings.isChecked() ? 1 : 0);
        setting.setHasReview(chkHasReview.isChecked() ? 1 : 0);
        setting.setImageVisible(chkProfileImage.isChecked() ? 1 : 0);
        setting.setPhoneNumberVisible(chkHasPhone.isChecked() ? 1 : 0);
        if (RatingsApplication.getInstant().getRatingsPref() != null)
            if (RatingsApplication.getInstant().getRatingsPref().getUser() != null)
                presenter.updateUserSetting(ApiUrl.getInstance().getUpdateUserSettingsUrl(), RtClients.getInstance().getGson().toJson(setting));


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
                categoryList = categories;
                UserCategoryAdapter adapter = new UserCategoryAdapter(categoryList);
                lvCategories.setAdapter(adapter);
                int userId = RatingsApplication.getInstant().getRatingsPref().getUser().getUserId();
                adapter.setItemCheckedListener(position -> {
                    RatingsCategory rc = new RatingsCategory();
                    rc.setUserId(userId);
                    rc.setCatId(categoryList.get(position).getCatId());
                    rc.setActive(1);
                    presenter.addRatingsCategory(ApiUrl.getInstance().getAddRatingsCategoriesUrl(), RtClients.getInstance().getGson().toJson(rc));
                });
                adapter.setItemCheckedRemovedListener(position -> {
                    RatingsCategory rc = new RatingsCategory();
                    rc.setUserId(userId);
                    rc.setCatId(categoryList.get(position).getCatId());
                    rc.setActive(0);
                    presenter.addRatingsCategory(ApiUrl.getInstance().getAddRatingsCategoriesUrl(), RtClients.getInstance().getGson().toJson(rc));
                });
            }
        }
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

        public void setItemCheckedRemovedListener(SearchFragment.ClickListener listener) {
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
            if (ratingsCategories != null)
                for (RatingsCategory rtc : ratingsCategories) {
                    if (rtc.getCatId() == categories.get(i).getCatId()) {
                        chkUserCategory.setChecked(true);
                    }
                }
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
