package com.review.ratings.ui.home.profile;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.gson.JsonObject;
import com.rashedkhan.ratings.R;
import com.review.ratings.config.ApiUrl;
import com.review.ratings.core.RatingsApplication;
import com.review.ratings.core.RtClients;
import com.review.ratings.data.implementation.HttpRepository;
import com.review.ratings.data.model.RatingsPref;
import com.review.ratings.data.model.User;
import com.review.ratings.util.Util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

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
    private CircleImageView civEfProfilePic;
    private String userTitlePhoto;

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
        if (user.getImage() != null) {
            RtClients.getInstance().getImageLoader(getActivity()).get(ApiUrl.getInstance().getUserImageUrl(RatingsApplication.getInstant().getRatingsPref().getUser().getImage()), new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    civEfProfilePic.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(getClass().getSimpleName(), Arrays.toString(error.getStackTrace()));
                }
            });
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
        civEfProfilePic = contentView.findViewById(R.id.civEfProfilePic);
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
        ibtnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhotoFromFile();
            }
        });
    }

    private void takePhotoFromFile() {
        if (Environment.getExternalStorageState().equals("mounted")) {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select Image :"), 0);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            try {
                Uri uri = data.getData();
                InputStream image_stream = getActivity().getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(image_stream);
                //  Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                civEfProfilePic.setImageBitmap(bitmap);
                bitmap = getResizedBitmap(bitmap, 250);
                if (bitmap != null)
                    new GetImageAsync().execute(bitmap);
            } catch (Exception | OutOfMemoryError exception) {
                Log.d(getClass().getSimpleName(), "File Not found");
            }
        }
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
            user.setAddress(etEfAddress.getText().toString());
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
        if (userTitlePhoto != null) {
            JsonObject jb = new JsonObject();
            jb.addProperty("userImageByteString", userTitlePhoto);
            jb.addProperty("userId", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));
            presenter.uploadPhoto(ApiUrl.getInstance().getUserImageUploadUrl(), RtClients.getInstance().getGson().toJson(jb));
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

    private class GetImageAsync extends AsyncTask<Bitmap, String, String> {
        @Override
        protected String doInBackground(Bitmap... params) {
            return toByte(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            userTitlePhoto = s;
        }
    }

    private String toByte(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}
