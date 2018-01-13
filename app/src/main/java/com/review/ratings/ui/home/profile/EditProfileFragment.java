package com.review.ratings.ui.home.profile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.rashedkhan.ratings.R;
import com.review.ratings.config.ApiUrl;
import com.review.ratings.core.RatingsApplication;
import com.review.ratings.core.RtClients;
import com.review.ratings.data.implementation.HttpRepository;
import com.review.ratings.data.model.RatingsPref;
import com.review.ratings.data.model.User;
import com.review.ratings.util.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment implements ProfileContract.EditProfileView {
    private CheckBox chkEfActive;
    private EditText etEfDesignation, etEfOrgName, etEfAddress, etEfPhoneNumber, etEfFullName;
    private ImageButton ibtnSelectImage;
    private Button btnUpdateProfile;
    private View contentView;
    private ProfileContract.EditProfilePresenter presenter;

    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contentView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        presenter = new EditProfilePresenter(this, new HttpRepository(getActivity()));
        initViewComponents();
        setCurrentDateToView();
        return contentView;
    }

    private void setCurrentDateToView() {
        User user = null;
        if (RatingsApplication.getInstant().getRatingsPref() != null) {
            if (RatingsApplication.getInstant().getRatingsPref().getUser() != null) {
                user = RatingsApplication.getInstant().getRatingsPref().getUser();
            }
        }
        if (user == null) {
            return;
        }
        if (user.getFullName() != null) {
            etEfFullName.setText(user.getFullName());
        }
        if (user.getAddress() != null) {
            etEfAddress.setText(user.getAddress());
        }
        if (user.getDesignation() != null) {
            etEfDesignation.setText(user.getDesignation());
        }
        if (user.getOrgName() != null) {
            etEfOrgName.setText(user.getOrgName());
        }
        if (user.getPhoneNumber() != null) {
            etEfPhoneNumber.setText(user.getPhoneNumber());
        }
        if (user.getActive() == 1) {
            chkEfActive.setChecked(true);
        } else {
            chkEfActive.setChecked(false);
        }
        if (user.getUserType() != null) {
            if (user.getUserType().getIsBusiness()) {
                etEfFullName.setHint("Business Name");
                etEfOrgName.setHint("Company Name");
                etEfDesignation.setVisibility(View.GONE);
            } else if (user.getUserType().getIsService()) {
                etEfOrgName.setHint("Company Name");
                etEfFullName.setHint("Service Name");
                etEfDesignation.setVisibility(View.GONE);
            } else if (user.getUserType().getIsPerson()) {
                etEfFullName.setHint("Full Name");
                etEfOrgName.setHint("Organization");
            }
        }


    }

    @Override
    public void initViewComponents() {
        btnUpdateProfile = contentView.findViewById(R.id.btnUpdateProfile);
        ibtnSelectImage = contentView.findViewById(R.id.ibtnSelectImage);
        chkEfActive = contentView.findViewById(R.id.chkEfActive);
        etEfDesignation = contentView.findViewById(R.id.etEfDesignation);
        etEfOrgName = contentView.findViewById(R.id.etEfOrgName);
        etEfAddress = contentView.findViewById(R.id.etEfAddress);
        etEfPhoneNumber = contentView.findViewById(R.id.etEfPhoneNumber);
        etEfFullName = contentView.findViewById(R.id.etEfFullName);
        btnUpdateProfile.setOnClickListener(view -> {

            updateUser();
        });
    }

    private void updateUser() {
        User user = null;
        if (RatingsApplication.getInstant().getRatingsPref() != null) {
            if (RatingsApplication.getInstant().getRatingsPref().getUser() != null) {
                user = RatingsApplication.getInstant().getRatingsPref().getUser();
            }
        }
        if (user != null) {
            user.setFullName(etEfFullName.getText().toString());
            user.setActive(chkEfActive.isChecked() ? 1 : 0);
            user.setOrgName(etEfOrgName.getText().toString());
            user.setDesignation(etEfDesignation.getText().toString());
            user.setPhoneNumber(etEfPhoneNumber.getText().toString());
            presenter.updateUserProfile(ApiUrl.getInstance().getUserUpdateUrl(), RtClients.getInstance().getGson().toJson(user));
        }
    }

    @Override
    public void profileUpdated(User user) {
        if (user != null) {
            RatingsPref pref = RatingsApplication.getInstant().getRatingsPref();
            pref.setUser(user);
            RatingsApplication.getInstant().setPreference(pref);
        }
        Util.get().showToastMsg(getActivity(), "Your profile has been updated !");
    }

    @Override
    public void showSuccessMessage(String msg) {
        Util.get().showToastMsg(getActivity(), msg);
    }

    @Override
    public void showErrorMessage(String msg) {
        Util.get().showToastMsg(getActivity(), msg);
    }
}
