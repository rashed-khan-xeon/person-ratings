package com.review.ratings.ui.home.justify;


import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.rashedkhan.ratings.R;
import com.review.ratings.common.BaseFragment;
import com.review.ratings.common.adapter.ExpandedListView;
import com.review.ratings.common.adapter.SpinnerAdapter;
import com.review.ratings.config.ApiUrl;
import com.review.ratings.core.RatingsApplication;
import com.review.ratings.core.RtClients;
import com.review.ratings.data.implementation.HttpRepository;
import com.review.ratings.data.model.RatingsCategory;
import com.review.ratings.data.model.User;
import com.review.ratings.data.model.UserRatings;
import com.review.ratings.data.model.UserReview;
import com.review.ratings.ui.home.search.SearchFragment;
import com.review.ratings.util.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class JustifyFragment extends BaseFragment implements JustifyContract.JustifyView {
    private Context context;
    private User user;
    private TextView tvUserId, tvUserFullName, tvRatingsText, tvUserProfession, tvUserPhoneNUmber, tvUserAddress, tvProfessionLabel, tvOrgLabel, tvDesignationLabel, tvUserDesignation, tvUserOrgName, tvUserEmail;
    private LinearLayout llDesignation;
    private TableRow tRPhoneNumber, trUserEmail;
    private LinearLayout llRatingsArea, llReviewArea, llAddress;
    private Spinner spnUserSelectedCat;
    private JustifyContract.JustifyPresenter presenter;
    private Button btnJustificationSubmit;
    private RatingBar rtRatings;
    private ExpandedListView lvSelectedRatRev;
    private EditText etComments;
    private int selectedRatingsCatId = 0;
    private List<Justify> justifies = new ArrayList<>();
    String selectedRtCatName;
    private ImageView imgUserImage;

    public JustifyFragment() {
        // Required empty public constructor
    }

    View justifyView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        justifyView = inflater.inflate(R.layout.fragment_justify, container, false);
        presenter = new JustifyPresenter(this, new HttpRepository(context));

        String u = getArguments().getString("user");
        if (u != null) {
            user = RtClients.getInstance().getGson().fromJson(u, User.class);
        }
        initViewComponents();
        setUserDetailsToView();
        rtRatings.setOnRatingBarChangeListener((ratingBar, v, b) -> {
            tvRatingsText.setText(Util.get().generateUserStatusFromRatings(v));
            switch ((int) v) {
                case 1:
                    tvRatingsText.setTextColor(Color.RED);
                    break;
                case 2:
                    tvRatingsText.setTextColor(Color.YELLOW);
                    break;
                case 3:
                    tvRatingsText.setTextColor(Color.parseColor("#E57373"));
                    break;
                case 4:
                    tvRatingsText.setTextColor(Color.parseColor("#B71C1C"));
                    break;
                case 5:
                    tvRatingsText.setTextColor(Color.BLUE);
                    break;
                case 6:
                    tvRatingsText.setTextColor(Color.MAGENTA);
                    break;
                case 7:
                    tvRatingsText.setTextColor(Color.parseColor("#4527A0"));
                    break;
                case 8:
                    tvRatingsText.setTextColor(Color.parseColor("#4CAF50"));
                    break;
                case 9:
                    tvRatingsText.setTextColor(Color.parseColor("#00E676"));
                    break;
                case 10:
                    tvRatingsText.setTextColor(Color.GREEN);
                    break;
                default:
                    tvRatingsText.setTextColor(Color.WHITE);
                    break;
            }
        });
        return justifyView;
    }


    private void setUserDetailsToView() {
        if (user != null) {
            if (!TextUtils.isEmpty(user.getFullName())) {
                tvUserFullName.setText(user.getFullName());
            }
            if (!TextUtils.isEmpty(String.valueOf(user.getUserId()))) {
                tvUserId.setText(String.valueOf(user.getUserId()));
            }
            if (user.getUserType() != null)
                if (!TextUtils.isEmpty(user.getUserType().getName())) {
                    tvUserProfession.setText(user.getUserType().getName());
                }

            if (user.getEmail() != null) {
                tvUserEmail.setText(user.getEmail());
            }
            if (user.getPhoneNumber() != null) {
                tvUserPhoneNUmber.setText(user.getPhoneNumber());
            }
            if (user.getOrgName() != null) {
                tvUserOrgName.setText(user.getOrgName());
            }
            if (user.getDesignation() != null) {
                tvUserDesignation.setText(user.getDesignation());
            }
            if (user.getAddress() != null) {
                tvUserAddress.setText(user.getAddress());
            }

        }
    }

    @Override
    public void initViewComponents() {
        tvRatingsText = justifyView.findViewById(R.id.tvRatingsText);
        tvUserPhoneNUmber = justifyView.findViewById(R.id.tvUserPhoneNUmber);
        tvUserAddress = justifyView.findViewById(R.id.tvUserAddress);
        llAddress = justifyView.findViewById(R.id.llAddress);
        tvUserEmail = justifyView.findViewById(R.id.tvUserEmail);
        trUserEmail = justifyView.findViewById(R.id.trUserEmail);
        imgUserImage = justifyView.findViewById(R.id.imgUserImage);
        tvDesignationLabel = justifyView.findViewById(R.id.tvDesignationLabel);
        tvUserOrgName = justifyView.findViewById(R.id.tvUserOrgName);
        tvUserDesignation = justifyView.findViewById(R.id.tvUserDesignation);
        tvOrgLabel = justifyView.findViewById(R.id.tvOrgLabel);
        tvProfessionLabel = justifyView.findViewById(R.id.tvProfessionLabel);
        tRPhoneNumber = justifyView.findViewById(R.id.tRPhoneNumber);
        llDesignation = justifyView.findViewById(R.id.llDesignation);
        btnJustificationSubmit = justifyView.findViewById(R.id.btnJustificationSubmit);
        llRatingsArea = justifyView.findViewById(R.id.llRatingsArea);
        llReviewArea = justifyView.findViewById(R.id.llReviewArea);
        tvUserId = justifyView.findViewById(R.id.tvUserId);
        tvUserFullName = justifyView.findViewById(R.id.tvUserFullName);
        tvUserProfession = justifyView.findViewById(R.id.tvUserProfession);
        spnUserSelectedCat = justifyView.findViewById(R.id.spnUserSelectedCat);
        lvSelectedRatRev = justifyView.findViewById(R.id.lvSelectedRatRev);
        rtRatings = justifyView.findViewById(R.id.rtRatings);
        etComments = justifyView.findViewById(R.id.etComments);
        presenter.getUserSelectedCategory(ApiUrl.getInstance().getUserRatingsCategoryUrl(user.getUserId()));

        if (user != null) {
            if (user.getUserSetting() != null) {
                if (user.getUserSetting().getHasRating()) {
                    llRatingsArea.setVisibility(View.VISIBLE);
                }
                if (user.getUserSetting().getHasReview()) {
                    llReviewArea.setVisibility(View.VISIBLE);
                }
                if (user.getUserSetting().getEmailVisible()) {
                    trUserEmail.setVisibility(View.VISIBLE);
                }
                if (user.getUserSetting().getPhoneNumberVisible()) {
                    tRPhoneNumber.setVisibility(View.VISIBLE);
                }
                if (user.getUserSetting().getImageVisible()) {
                    imgUserImage.setVisibility(View.VISIBLE);
                }
                if (user.getUserSetting().getAddressVisible()) {
                    llAddress.setVisibility(View.VISIBLE);
                }

            }
            if (user.getUserType() != null) {
                if (user.getUserType().getIsPerson()) {
                    tvProfessionLabel.setText("Profession:");
                    llDesignation.setVisibility(View.VISIBLE);
                    tvOrgLabel.setText("Organization:");
                } else if (user.getUserType().getIsService()) {
                    tvProfessionLabel.setText("Service:");
                    llDesignation.setVisibility(View.VISIBLE);
                    tvOrgLabel.setText("Company:");
                } else if (user.getUserType().getIsBusiness()) {
                    tvProfessionLabel.setText("Business:");
                    tvOrgLabel.setText("Company :");
                }

            }
        }
        spnUserSelectedCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spnUserSelectedCat.setSelection(i);
                Map.Entry<String, String> item = (Map.Entry<String, String>) spnUserSelectedCat.getSelectedItem();
                selectedRatingsCatId = Integer.parseInt(item.getKey());
                selectedRtCatName = item.getValue();
                rtRatings.setRating(0);
                etComments.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnJustificationSubmit.setOnClickListener(view -> {
            Justify justify = new Justify();
            if (selectedRatingsCatId == 0) {
                Util.get().showToastMsg(getActivity(), "Please select a category !");
                return;
            }
            if (rtRatings.getRating() == 0 && TextUtils.isEmpty(etComments.getText())) {
                Util.get().showToastMsg(context, "Please rate or write a review !");
                return;
            }
            if (selectedRatingsCatId != 0) {
                if (user == null)
                    return;
                if (RatingsApplication.getInstant().getRatingsPref().getUser() == null) {
                    return;
                }
                UserRatings ur = new UserRatings();
                ur.setRatedByUserId(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId());
                ur.setUserId(user.getUserId());
                ur.setRatingsDate(new Date());
                ur.setRatingsCatId(selectedRatingsCatId);
                ur.setRatings(rtRatings.getRating());
                justify.setUserRatings(ur);
                if (justifies.size() > 0) {
                    for (Justify js : justifies) {
                        if (js.getUserRatings() != null) {
                            if (js.getUserRatings().getRatingsCatId() == ur.getRatingsCatId()) {
                                js.setUserRatings(ur);
                            }
                        }
                    }
                }
            }
            if (!TextUtils.isEmpty(etComments.getText())) {
                if (user == null)
                    return;
                UserReview uRv = new UserReview();
                uRv.setComments(etComments.getText().toString());
                uRv.setRatingsCatId(selectedRatingsCatId);
                uRv.setReviewDate(new Date());
                uRv.setUserId(user.getUserId());
                uRv.setReviewedByUserId(user.getUserId());
                justify.setUserReview(uRv);
                if (justifies.size() > 0) {
                    for (Justify js : justifies) {
                        if (js.getUserReview() != null) {
                            if (js.getUserReview().getRatingsCatId() == uRv.getRatingsCatId()) {
                                js.setUserReview(uRv);
                            }
                        }
                    }
                }
            }
            justify.setRtCatName(selectedRtCatName);
            JustifyAdapter adapter = new JustifyAdapter(justifies);
            lvSelectedRatRev.setAdapter(adapter);
            lvSelectedRatRev.setExpanded(true);
            boolean isFound = false;
            for (Justify js : justifies) {
                if (js.getRtCatName().equals(selectedRtCatName)) {
                    isFound = true;
                }
            }
            if (!isFound) {
                justifies.add(justify);
                adapter.notifyDataSetChanged();
            }
            adapter.delete(position -> {
                justifies.remove(position);
                adapter.notifyDataSetChanged();
            });
            AlertDialog.Builder ab = new AlertDialog.Builder(context);
            ab.setMessage("Do you want add another category ?");
            ab.setPositiveButton("Yes", (dialogInterface, i) -> {
                rtRatings.setRating(0);
                etComments.setText("");
                spnUserSelectedCat.setSelection(0);
            }).setNegativeButton("No, Continue", (dialogInterface, i) -> {
                submit();
            }).create().show();

        });
    }

    private void submit() {
        Util.get().showProgress(getActivity(), true, "Processing...");
        for (Justify justify : justifies) {
            if (justify.getUserRatings() != null) {
                presenter.submitRatings(ApiUrl.getInstance().getAddUserRatingsUrl(), RtClients.getInstance().getGson().toJson(justify.getUserRatings()));

            }
            if (justify.getUserReview() != null) {
                presenter.submitReview(ApiUrl.getInstance().getAddUserReviewUrl(), RtClients.getInstance().getGson().toJson(justify.getUserReview()));

            }
        }
    }

    @Override
    public void showSuccessMessage(String msg) {
        Util.get().showToastMsg(context, msg);
        Util.get().showProgress(context, false, null);
    }

    @Override
    public void showErrorMessage(String msg) {
        Util.get().showToastMsg(context, msg);
        Util.get().showProgress(context, false, null);
    }

    @Override
    public void setUserSelectedCategoryToView(List<RatingsCategory> ratingsCategories) {
        if (ratingsCategories == null) {
            btnJustificationSubmit.setVisibility(View.GONE);
        }
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put("0", "--Choose category--");
        for (RatingsCategory category : ratingsCategories) {
            if (category.getCategory() != null)
                data.put(String.valueOf(category.getRatingsCatId()), category.getCategory().getName());
        }
        SpinnerAdapter<String, String> adapter = new SpinnerAdapter<String, String>(context, android.R.layout.simple_spinner_item, data);
        spnUserSelectedCat.setAdapter(adapter);
    }

    @Override
    public void ratingsAdded(String msg) {
        Util.get().showToastMsg(context, msg);
        Util.get().showProgress(context, false, null);
        clearView();
    }

    private void clearView() {
        if (selectedRatingsCatId != 0) {
            selectedRatingsCatId = 0;
            spnUserSelectedCat.setSelection(0);
        }
        if (!TextUtils.isEmpty(etComments.getText())) {
            etComments.setText("");
        }
        if (justifies.size() > 0) {
            justifies.clear();
            JustifyAdapter adapter = (JustifyAdapter) lvSelectedRatRev.getAdapter();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void reviewAdded(String msg) {
        Util.get().showToastMsg(context, msg);
        Util.get().showProgress(context, false, null);
        clearView();
    }

    private class Justify {
        private UserRatings userRatings;
        private UserReview userReview;
        private String rtCatName;

        public UserRatings getUserRatings() {
            return userRatings;
        }

        public void setUserRatings(UserRatings userRatings) {
            this.userRatings = userRatings;
        }

        public UserReview getUserReview() {
            return userReview;
        }

        public void setUserReview(UserReview userReview) {
            this.userReview = userReview;
        }

        public String getRtCatName() {
            return rtCatName;
        }

        public void setRtCatName(String rtCatName) {
            this.rtCatName = rtCatName;
        }
    }

    private class JustifyAdapter extends BaseAdapter {
        List<Justify> justifies;
        private SearchFragment.ClickListener clickListener;

        public JustifyAdapter(List<Justify> justifies) {
            this.justifies = justifies;
        }

        @Override
        public int getCount() {
            return justifies.size();
        }

        @Override
        public Justify getItem(int i) {
            return justifies.get(i);
        }

        public void delete(SearchFragment.ClickListener listener) {
            clickListener = listener;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View row;
            if (view == null) {
                row = LayoutInflater.from(context).inflate(R.layout.justify_list_item, null);
            } else {
                row = view;
            }
            TextView tvJustifyRatingsCatName = row.findViewById(R.id.tvJustifyRatingsCatName);
            TextView tvJsDel = row.findViewById(R.id.tvJsDel);
            if (!TextUtils.isEmpty(justifies.get(i).getRtCatName())) {
                tvJustifyRatingsCatName.setText(justifies.get(i).getRtCatName());
            }
            tvJsDel.setOnClickListener(view1 -> clickListener.onItemClick(i));
            return row;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
