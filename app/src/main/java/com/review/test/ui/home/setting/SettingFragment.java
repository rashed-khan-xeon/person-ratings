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
import android.widget.ListView;
import android.widget.TextView;

import com.review.test.R;
import com.review.test.common.BaseFragment;
import com.review.test.config.ApiUrl;
import com.review.test.core.RatingsApplication;
import com.review.test.data.implementation.HttpRepository;
import com.review.test.data.model.Category;
import com.review.test.data.model.User;
import com.review.test.data.model.UserSetting;
import com.review.test.ui.home.common.BaseView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends BaseFragment implements SettingContract.SettingView {
    private Context context;
    private View settingView;
    private CheckBox chkHasAddress, chkHasEmail, chkHasPhone, chkHasReview, chkHasRatings, chkProfileImage;
    private Button btnSettingSaveChanges, btnSettingsSetCat;
    private ListView lvCategories;
    private SettingContract.SettingPresenter presenter;

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
        btnSettingsSetCat = settingView.findViewById(R.id.btnSettingsSetCat);
        chkHasAddress = settingView.findViewById(R.id.chkHasAddress);
        chkHasEmail = settingView.findViewById(R.id.chkHasEmail);
        chkHasPhone = settingView.findViewById(R.id.chkHasPhone);
        chkHasReview = settingView.findViewById(R.id.chkHasReview);
        chkHasRatings = settingView.findViewById(R.id.chkHasRatings);
        chkProfileImage = settingView.findViewById(R.id.chkProfileImage);
        presenter.getCategoriesByUserType(ApiUrl.getInstance().getCategoriesByUserTypeId(RatingsApplication.getInstant().getRatingsPref().getUser().getUserTypeId()));
    }

    @Override
    public void showSuccessMessage(String msg) {

    }

    @Override
    public void showErrorMessage(String msg) {

    }

    @Override
    public void setCategories(List<Category> categories) {
        if (categories != null) {
            if (categories.size() > 0) {
                UserCategoryAdapter adapter = new UserCategoryAdapter(categories);
                lvCategories.setAdapter(adapter);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private class UserCategoryAdapter extends BaseAdapter {

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
            CheckBox tvUserCategory = row.findViewById(R.id.chkUserCategory);
            if (!TextUtils.isEmpty(getItem(i).getName())) {
                tvUserCategory.setText(categories.get(i).getName());
            }
            return row;
        }
    }

}
