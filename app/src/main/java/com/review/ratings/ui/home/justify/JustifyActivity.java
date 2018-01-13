package com.review.ratings.ui.home.justify;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.rashedkhan.ratings.R;
import com.review.ratings.common.BaseActivity;
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
//TODO this active is not yet used
public class JustifyActivity extends BaseActivity implements JustifyContract.JustifyView {
    private User user;
    private TextView tvUserId, tvUserFullName, tvUserProfession;
    private LinearLayout llRatingsArea, llReviewArea;
    private Spinner spnUserSelectedCat;
    private JustifyContract.JustifyPresenter presenter;
    private Button btnJustificationSubmit;
    private RatingBar rtRatings;
    private ExpandedListView lvSelectedRatRev;
    private EditText etComments;
    private int selectedRatingsCatId = 0;
    private List<Justify> justifies = new ArrayList<>();
    String selectedRtCatName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_justify);
        presenter = new JustifyPresenter(this, new HttpRepository(this));
        String u = getIntent().getStringExtra("user");
        if (u != null) {
            user = RtClients.getInstance().getGson().fromJson(u, User.class);
        }
        initViewComponents();
        setUserDetailsToView();
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
        }
    }

    @Override
    public void initViewComponents() {
        btnJustificationSubmit = findViewById(R.id.btnJustificationSubmit);
        llRatingsArea = findViewById(R.id.llRatingsArea);
        llReviewArea = findViewById(R.id.llReviewArea);
        tvUserId = findViewById(R.id.tvUserId);
        tvUserFullName = findViewById(R.id.tvUserFullName);
        tvUserProfession = findViewById(R.id.tvUserProfession);
        spnUserSelectedCat = findViewById(R.id.spnUserSelectedCat);
        lvSelectedRatRev = findViewById(R.id.lvSelectedRatRev);
        rtRatings = findViewById(R.id.rtRatings);
        etComments = findViewById(R.id.etComments);
        presenter.getUserSelectedCategory(ApiUrl.getInstance().getUserRatingsCategoryUrl(user.getUserId()));

        if (user != null) {
            if (user.getUserSetting() != null) {
                if (user.getUserSetting().getHasRating()) {
                    llRatingsArea.setVisibility(View.VISIBLE);
                }
                if (user.getUserSetting().getHasReview()) {
                    llReviewArea.setVisibility(View.VISIBLE);
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
            if (rtRatings.getRating() == 0 && TextUtils.isEmpty(etComments.getText())) {
                Util.get().showToastMsg(JustifyActivity.this, "Please rate or write a review !");
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
            AlertDialog.Builder ab = new AlertDialog.Builder(this);
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
        Util.get().showToastMsg(this, msg);
    }

    @Override
    public void showErrorMessage(String msg) {
        Util.get().showToastMsg(this, msg);
    }

    @Override
    public void setUserSelectedCategoryToView(List<RatingsCategory> ratingsCategories) {
        if (ratingsCategories == null) {
            btnJustificationSubmit.setVisibility(View.GONE);
        }
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put("0", "--select category--");
        for (RatingsCategory category : ratingsCategories) {
            if (category.getCategory() != null)
                data.put(String.valueOf(category.getRatingsCatId()), category.getCategory().getName());
        }
        SpinnerAdapter<String, String> adapter = new SpinnerAdapter<String, String>(this, android.R.layout.simple_spinner_item, data);
        spnUserSelectedCat.setAdapter(adapter);
    }

    @Override
    public void ratingsAdded(String msg) {
        Util.get().showToastMsg(this, msg);
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
        Util.get().showToastMsg(this, msg);
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
                row = LayoutInflater.from(JustifyActivity.this).inflate(R.layout.justify_list_item, null);
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
}
