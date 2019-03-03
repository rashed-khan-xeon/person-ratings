package com.rashedkhan.ratings.ui.home.feature;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.rashedkhan.ratings.R;
import com.rashedkhan.ratings.common.BaseActivity;
import com.rashedkhan.ratings.common.adapter.RatingAdapter;
import com.rashedkhan.ratings.config.ApiUrl;
import com.rashedkhan.ratings.core.RatingsApplication;
import com.rashedkhan.ratings.core.ResponseListener;
import com.rashedkhan.ratings.core.RtClients;
import com.rashedkhan.ratings.data.implementation.HttpRepository;
import com.rashedkhan.ratings.data.model.Feature;
import com.rashedkhan.ratings.data.model.RatingSummary;
import com.rashedkhan.ratings.data.model.User;
import com.rashedkhan.ratings.data.model.UserReview;
import com.rashedkhan.ratings.data.repository.IHttpRepository;
import com.rashedkhan.ratings.ui.home.history.ReviewHistoryFragment;
import com.rashedkhan.ratings.ui.home.search.SearchFragment;
import com.rashedkhan.ratings.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeatureActivity extends BaseActivity {
    private Button btnCreateFeature, btnAssign;
    private ListView lvFeatureList;
    private TextView tvNoContent;
    private IHttpRepository repository;
    private Dialog dialog;
    private int featureTypeId = 0;
    public static String FEATURE_TYPE_ID = "featureTypeId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_feature);
        init();
    }

    private void init() {
        repository = new HttpRepository(this);
        btnAssign = findViewById(R.id.btnAssign);
        tvNoContent = findViewById(R.id.tvNoContent);
        btnCreateFeature = findViewById(R.id.btnCreateFeature);
        lvFeatureList = findViewById(R.id.lvFeatureList);
        btnCreateFeature.setOnClickListener(view1 -> {
            createFeature();
        });
        btnAssign.setOnClickListener(view12 -> {
            assignUserToFeature();
        });
    }

    private void assignUserToFeature() {
        Intent intent = new Intent(this, AssignFeatureActivity.class);
        intent.putExtra(FEATURE_TYPE_ID, String.valueOf(featureTypeId));
        startActivity(intent);
    }


    @Override
    public void onResume() {
        super.onResume();
        featureTypeId = Integer.parseInt(getIntent().getStringExtra(FEATURE_TYPE_ID));
        getFeatureList();
    }

    private void getFeatureList() {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));
        repository.getAll(ApiUrl.getInstance().getFeatureListByTypeId(featureTypeId), Feature[].class, header, new ResponseListener<List<Feature>>() {
            @Override
            public void success(List<Feature> response) {
                FeatureAdapter adapter = new FeatureAdapter(response);
                lvFeatureList.setAdapter(adapter);
                adapter.onItemClickListener(position -> {
                    getFeatureWiseAssignList(response.get(position).getFeatureId(), response.get(position).getTitle());
                });
                adapter.setItemCheckedListener(position -> {
                    Feature feature = response.get(position);
                    feature.setActive(1);
                    submitFeature(feature);
                });
                adapter.setItemUnCheckedListener(position -> {
                    Feature feature = response.get(position);
                    feature.setActive(0);
                    submitFeature(feature);
                });
                if (response.size() == 0) {
                    lvFeatureList.setVisibility(View.GONE);
                    tvNoContent.setVisibility(View.VISIBLE);
                } else {

                    lvFeatureList.setVisibility(View.VISIBLE);
                    tvNoContent.setVisibility(View.GONE);
                }
            }

            @Override
            public void error(Throwable error) {
                lvFeatureList.setVisibility(View.GONE);
                tvNoContent.setVisibility(View.VISIBLE);
                Toast.makeText(FeatureActivity.this, Util.get().getMessage((VolleyError) error), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void goBack(View view) {
        onBackPressed();
    }

    private class FeatureAdapter extends BaseAdapter {
        private List<Feature> features;

        private SearchFragment.ClickListener checkClickListener;
        private SearchFragment.ClickListener unCheckClickListener;
        private SearchFragment.ClickListener itemClick;

        public FeatureAdapter(List<Feature> features) {
            this.features = features;
        }

        @Override
        public int getCount() {
            return features.size();
        }

        @Override
        public Feature getItem(int i) {
            return features.get(i);
        }

        @Override
        public long getItemId(int i) {
            return getItem(i).getFeatureId();
        }

        public void onItemClickListener(SearchFragment.ClickListener listener) {
            this.itemClick = listener;
        }

        public void setItemCheckedListener(SearchFragment.ClickListener listener) {
            this.checkClickListener = listener;
        }

        public void setItemUnCheckedListener(SearchFragment.ClickListener listener) {
            this.unCheckClickListener = listener;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View vw;
            if (view == null) {
                vw = LayoutInflater.from(FeatureActivity.this).inflate(R.layout.feature_row, null);
            } else {
                vw = view;
            }
            TextView tvFeatureTitle = vw.findViewById(R.id.tvFeatureTitle);
            CheckBox chkUpdateFeature = vw.findViewById(R.id.chkUpdateFeature);
            chkUpdateFeature.setChecked(features.get(i).getActive());
            chkUpdateFeature.setOnClickListener(view1 -> {
                if (chkUpdateFeature.isChecked()) {
                    checkClickListener.onItemClick(i);
                } else {
                    unCheckClickListener.onItemClick(i);
                }
            });
            tvFeatureTitle.setText(getItem(i).getTitle());
            vw.setOnClickListener(view1 -> itemClick.onItemClick(i));
            return vw;
        }
    }

    private void createFeature() {
        dialog = new Dialog(FeatureActivity.this);
        View view = LayoutInflater.from(FeatureActivity.this).inflate(R.layout.create_feature, null);
        dialog.setContentView(view);
        dialog.show();
        EditText etFeatureTitle = view.findViewById(R.id.etFeatureTitle);


        Button btnFeatureCreate = view.findViewById(R.id.btnFeatureCreate);
        btnFeatureCreate.setOnClickListener(view1 -> {
            if (RatingsApplication.getInstant().getUser() == null) {
                return;
            }
            if (etFeatureTitle.getText().toString().isEmpty()) {
                etFeatureTitle.setError("Required");
                return;
            }
            Feature feature = new Feature();
            feature.setCreatedUserId(RatingsApplication.getInstant().getUser().getUserId());
            feature.setTitle(etFeatureTitle.getText().toString());
            feature.setActive(1);
            feature.setFeatureTypeId(featureTypeId);
            submitFeature(feature);
        });
    }

    private void submitFeature(Feature feature) {
        Util.get().showProgress(FeatureActivity.this, true, "Processing...");
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));
        String url = ApiUrl.getInstance().createFeature();
        repository.post(url, Feature.class, RtClients.getInstance().getGson().toJson(feature), header, new ResponseListener<Feature>() {
            @Override
            public void success(Feature response) {
                if (dialog != null)
                    dialog.dismiss();
                Util.get().showProgress(FeatureActivity.this, false, null);
                Toast.makeText(FeatureActivity.this, "Done ", Toast.LENGTH_LONG).show();
                getFeatureList();
            }

            @Override
            public void error(Throwable error) {
                if (dialog != null)
                    dialog.dismiss();
                Util.get().showProgress(FeatureActivity.this, false, null);
                Toast.makeText(FeatureActivity.this, Util.get().getMessage((VolleyError) error), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getFeatureWiseAssignList(int featureId, String title) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));
        repository.getAll(ApiUrl.getInstance().getFeatureWiseAssignList(featureId), User[].class, header, new ResponseListener<List<User>>() {
            @Override
            public void success(List<User> response) {
                showFeatureWiseAssignList(title, response);
            }

            @Override
            public void error(Throwable error) {
                Toast.makeText(FeatureActivity.this, Util.get().getMessage((VolleyError) error), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showFeatureWiseAssignList(String title, List<User> response) {
        Dialog mShowAssign = new Dialog(FeatureActivity.this);
        View view = LayoutInflater.from(FeatureActivity.this).inflate(R.layout.show_feature_wise_users, null);
        mShowAssign.setContentView(view);
        mShowAssign.show();
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        ListView lvFUsers = view.findViewById(R.id.lvFUsers);
        tvTitle.setText(title);
        UserListAdapter adapter = new UserListAdapter(response);
        lvFUsers.setAdapter(adapter);
        adapter.setItemCheckedListener(position -> {
            User user = response.get(position);
            user.setActive(true);
            updateFeatureWiseAssignList(user);
        });
        adapter.setItemUnCheckedListener(position -> {
            User user = response.get(position);
            user.setActive(false);
            updateFeatureWiseAssignList(user);
        });
        adapter.setOnSummaryClickListener(position -> {
            getUserRatingSummary(response.get(position).getUserId());

        });
        adapter.setOnReviewClickListener(position -> {
            getReviws(response.get(position).getUserId());
        });

    }

    private void getReviws(int userId) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(userId));
        String url = ApiUrl.getInstance().getUserReviewList(userId, 0, 50);
        repository.getAll(url, UserReview[].class, header, new ResponseListener<List<UserReview>>() {
            @Override
            public void success(List<UserReview> response) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                showReviewDialog(response);
            }

            @Override
            public void error(Throwable error) {

            }
        });
    }

    private void showReviewDialog(List<UserReview> response) {
        Dialog dialog = new Dialog(FeatureActivity.this);
        View view = LayoutInflater.from(FeatureActivity.this).inflate(R.layout.layout_show_feature_user_reviews, null);
        dialog.setContentView(view);
        dialog.show();
        RecyclerView lvUserReviewList = view.findViewById(R.id.lvUserReviewList);

        ReviewHistoryFragment.UserReviewAdapter adapter = new ReviewHistoryFragment.UserReviewAdapter(response, FeatureActivity.this);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(FeatureActivity.this);
        lvUserReviewList.setLayoutManager(lm);
        lvUserReviewList.setItemAnimator(new DefaultItemAnimator());
        lvUserReviewList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lvUserReviewList.setAdapter(adapter);
    }

    private void getUserRatingSummary(int userId) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(userId));
        String url = ApiUrl.getInstance().getAvgRatingUrl(userId);
        repository.getAll(url, RatingSummary[].class, header, new ResponseListener<List<RatingSummary>>() {
            @Override
            public void success(List<RatingSummary> response) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                showRatingSummaryDialog(response);
            }

            @Override
            public void error(Throwable error) {

            }
        });
    }

    private void showRatingSummaryDialog(List<RatingSummary> response) {
        Dialog dialog = new Dialog(FeatureActivity.this);
        View view = LayoutInflater.from(FeatureActivity.this).inflate(R.layout.layout_show_feature_user_rating_summary, null);
        dialog.setContentView(view);
        dialog.show();
        TextView tvNoRatingsMsg = view.findViewById(R.id.tvNoRatingsMsg);
        LinearLayout cardView = view.findViewById(R.id.cvOverallRate);
        if (response.size() == 0) {
            tvNoRatingsMsg.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.GONE);
            return;
        } else {
            tvNoRatingsMsg.setVisibility(View.GONE);
            cardView.setVisibility(View.VISIBLE);
        }

        ListView lvOverallRatings = view.findViewById(R.id.lvOverallRatings);
        RatingAdapter ratingAdapter = new RatingAdapter(FeatureActivity.this, response);
        lvOverallRatings.setAdapter(ratingAdapter);
    }

    private void updateFeatureWiseAssignList(User user) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));
        String url = ApiUrl.getInstance().getUpdateFeatureUser();
        repository.post(url, User.class, RtClients.getInstance().getGson().toJson(user), header, new ResponseListener<User>() {
            @Override
            public void success(User response) {
                if (dialog != null)
                    dialog.dismiss();
                Toast.makeText(FeatureActivity.this, "Done ", Toast.LENGTH_LONG).show();
            }

            @Override
            public void error(Throwable error) {
                if (dialog != null)
                    dialog.dismiss();
                Toast.makeText(FeatureActivity.this, Util.get().getMessage((VolleyError) error), Toast.LENGTH_LONG).show();
            }
        });
    }

    private class UserListAdapter extends BaseAdapter {
        private SearchFragment.ClickListener checkClickListener;
        private SearchFragment.ClickListener unCheckClickListener;
        private SearchFragment.ClickListener summaryClick;
        private SearchFragment.ClickListener reviewClick;
        private List<User> response;

        private UserListAdapter(List<User> users) {
            this.response = users;
        }

        public void setOnSummaryClickListener(SearchFragment.ClickListener checkClickListener) {
            this.summaryClick = checkClickListener;
        }

        public void setOnReviewClickListener(SearchFragment.ClickListener checkClickListener) {
            this.reviewClick = checkClickListener;
        }

        @Override
        public int getCount() {
            return response.size();
        }

        @Override
        public User getItem(int i) {
            return response.get(i);
        }

        @Override
        public long getItemId(int i) {
            return getItem(i).getUserId();
        }

        public void setItemCheckedListener(SearchFragment.ClickListener listener) {
            this.checkClickListener = listener;
        }

        public void setItemUnCheckedListener(SearchFragment.ClickListener listener) {
            this.unCheckClickListener = listener;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View vw;
            if (view == null) {
                vw = LayoutInflater.from(FeatureActivity.this).inflate(R.layout.featurewise_users_row, null);
            } else {
                vw = view;
            }
            TextView tvUserName = vw.findViewById(R.id.tvUserName);
            TextView tvSummary = vw.findViewById(R.id.tvSummary);
            TextView tvReviewSummary = vw.findViewById(R.id.tvReviewSummary);
            CheckBox chkUser = vw.findViewById(R.id.chkUser);
            tvUserName.setText(getItem(i).getFullName());

            chkUser.setChecked(getItem(i).getActive());
            chkUser.setOnClickListener(view1 -> {
                if (chkUser.isChecked()) {
                    checkClickListener.onItemClick(i);
                } else {
                    unCheckClickListener.onItemClick(i);
                }
            });

            tvSummary.setOnClickListener(view12 -> {
                summaryClick.onItemClick(i);
            });
            tvReviewSummary.setOnClickListener(view1 -> {
                reviewClick.onItemClick(i);
            });
            return vw;
        }
    }
}
