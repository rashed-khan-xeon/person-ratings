package com.review.ratings.ui.home.profile;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.rashedkhan.ratings.R;
import com.review.ratings.common.adapter.ExpandedListView;
import com.review.ratings.config.ApiUrl;
import com.review.ratings.core.RatingsApplication;
import com.review.ratings.core.RtClients;
import com.review.ratings.data.implementation.HttpRepository;
import com.review.ratings.data.model.RatingSummary;
import com.review.ratings.data.model.RatingsCategory;
import com.review.ratings.data.model.User;
import com.review.ratings.ui.home.search.SearchFragment;
import com.review.ratings.util.Util;

import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment implements ProfileContract.ProfileView {
    private Context context;
    private ExpandedListView lvOverallRatings;
    private ProfilePresenter presenter;
    private int userId = 0;
    private TextView tvUserNameInProfile, tvDesignation, tvOrgName, tvAddress, tvProfession, tvUserEmail, tvPhoneNumber, tvUserCategories, tvEditProfile;
    private SearchFragment.Transfer transfer;
    private CircleImageView civEfProfilePicture;

    public ProfileFragment() {

    }

    View profileView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        profileView = inflater.inflate(R.layout.fragment_profile, container, false);
        presenter = new ProfilePresenter(this, new HttpRepository(getActivity()));
        if (getArguments() != null)
            userId = Integer.parseInt(getArguments().getString("userId"));
        initViewComponents();
        if (userId != 0) {
            getUserDetails(userId);
        } else {
            if (RatingsApplication.getInstant().getRatingsPref().getUser() != null) {
                getUserDetails(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId());
            }
        }
        tvEditProfile.setOnClickListener(view -> {
            transfer.transferFragment(new EditProfileFragment());
        });
        return profileView;
    }

    private void getUserDetails(int userId) {
        presenter.getUserDetails(ApiUrl.getInstance().getUserDetailsUrl(userId));
        presenter.getUserSelectedCategory(ApiUrl.getInstance().getUserRatingsCategoryUrl(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.transfer = (SearchFragment.Transfer) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void setUserDetailsToView(User uDetails) {
        if (!TextUtils.isEmpty(uDetails.getFullName())) {
            tvUserNameInProfile.setText(uDetails.getFullName());
        }
        if (!TextUtils.isEmpty(uDetails.getAddress())) {
            tvAddress.setText(uDetails.getAddress());
        }
        if (!TextUtils.isEmpty(uDetails.getDesignation())) {
            tvDesignation.setText(uDetails.getDesignation());
        }
        if (!TextUtils.isEmpty(uDetails.getEmail())) {
            tvUserEmail.setText(uDetails.getEmail());
        }
        if (!TextUtils.isEmpty(uDetails.getOrgName())) {
            tvOrgName.setText(uDetails.getOrgName());
        }
        if (!TextUtils.isEmpty(uDetails.getPhoneNumber())) {
            tvPhoneNumber.setText(uDetails.getPhoneNumber());
        }
        if (uDetails.getUserType() != null)
            if (!TextUtils.isEmpty(uDetails.getUserType().getName())) {
                tvProfession.setText(uDetails.getUserType().getName());
            }

        if (uDetails.getImage() != null) {
            RtClients.getInstance().getImageLoader(getActivity()).get(ApiUrl.getInstance().getUserImageUrl(RatingsApplication.getInstant().getRatingsPref().getUser().getImage()), new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    civEfProfilePicture.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        civEfProfilePicture.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.avatar));
                        Log.d(getClass().getSimpleName(), Arrays.toString(error.getStackTrace()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        presenter.getUserAvgRatingByCategory(ApiUrl.getInstance().getAvgRatingUrl(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));

    }

    @Override
    public void setUserSelectedCategory(List<RatingsCategory> ratingsCategories) {
        StringBuilder sb = new StringBuilder();
        for (RatingsCategory rc : ratingsCategories) {
            sb.append("* ");
            sb.append(rc.getCategory().getName());
            sb.append("\n");
        }
        tvUserCategories.setText(sb.toString());
    }

    @Override
    public void setUserAvgRatingsToView(List<RatingSummary> ratingsCategories) {
        if (ratingsCategories != null)
            if (ratingsCategories.size() > 0) {
                RatingAdapter adapter = new RatingAdapter(ratingsCategories);
                lvOverallRatings.setAdapter(adapter);
                lvOverallRatings.setExpanded(true);
            }
    }

    @Override
    public void initViewComponents() {
        tvEditProfile = profileView.findViewById(R.id.tvEditProfile);
        tvUserNameInProfile = profileView.findViewById(R.id.tvUserNameInProfile);
        tvUserEmail = profileView.findViewById(R.id.tvUserEmail);
        tvAddress = profileView.findViewById(R.id.tvAddress);
        tvDesignation = profileView.findViewById(R.id.tvDesignation);
        tvOrgName = profileView.findViewById(R.id.tvOrgName);
        tvProfession = profileView.findViewById(R.id.tvProfession);
        tvPhoneNumber = profileView.findViewById(R.id.tvPhoneNumber);
        lvOverallRatings = profileView.findViewById(R.id.lvOverallRatings);
        tvUserCategories = profileView.findViewById(R.id.tvUserCategories);
        civEfProfilePicture = profileView.findViewById(R.id.civEfProfilePicture);

    }

    @Override
    public void showSuccessMessage(String msg) {

    }

    @Override
    public void showErrorMessage(String msg) {

    }

    private class RatingAdapter extends BaseAdapter {
        private List<RatingSummary> ratingSummary;

        public RatingAdapter(List<RatingSummary> ratingSummary) {
            this.ratingSummary = ratingSummary;
        }

        @Override
        public int getCount() {
            return ratingSummary.size();
        }

        @Override
        public RatingSummary getItem(int i) {
            return ratingSummary.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View vw;
            if (view == null) {
                vw = LayoutInflater.from(context).inflate(R.layout.user_profile_rating_view_list_item, null);
            } else {
                vw = view;
            }
            TextView tvRatItemTitle = vw.findViewById(R.id.tvRatItemTitle);
            TextView tvUserCatStatus = vw.findViewById(R.id.tvUserCatStatus);
            TextView tvRatingsCount = vw.findViewById(R.id.tvRatingsCount);
            RatingBar itemRatings = vw.findViewById(R.id.itemRatings);
            tvRatItemTitle.setText(ratingSummary.get(i).getCategory());
            itemRatings.setRating(ratingSummary.get(i).getAvgRatings());
            tvRatingsCount.setText(String.valueOf(ratingSummary.get(i).getAvgRatings()));
            tvUserCatStatus.setText(Util.get().generateUserStatusFromRatings(ratingSummary.get(i).getAvgRatings()));
            switch ((int) ratingSummary.get(i).getAvgRatings()) {
                case 1:
                    tvUserCatStatus.setTextColor(Color.RED);
                    break;
                case 2:
                    tvUserCatStatus.setTextColor(Color.YELLOW);
                    break;
                case 3:
                    tvUserCatStatus.setTextColor(Color.parseColor("#E57373"));
                    break;
                case 4:
                    tvUserCatStatus.setTextColor(Color.parseColor("#B71C1C"));
                    break;
                case 5:
                    tvUserCatStatus.setTextColor(Color.BLUE);
                    break;
                case 6:
                    tvUserCatStatus.setTextColor(Color.MAGENTA);
                    break;
                case 7:
                    tvUserCatStatus.setTextColor(Color.parseColor("#4527A0"));
                    break;
                case 8:
                    tvUserCatStatus.setTextColor(Color.parseColor("#4CAF50"));
                    break;
                case 9:
                    tvUserCatStatus.setTextColor(Color.parseColor("#00E676"));
                    break;
                case 10:
                    tvUserCatStatus.setTextColor(Color.GREEN);
                    break;
                default:
                    tvUserCatStatus.setTextColor(Color.WHITE);
                    break;
            }
            return vw;
        }
    }

}
