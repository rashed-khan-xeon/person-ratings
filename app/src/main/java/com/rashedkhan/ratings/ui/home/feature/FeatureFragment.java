package com.rashedkhan.ratings.ui.home.feature;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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
import com.rashedkhan.ratings.common.BaseFragment;
import com.rashedkhan.ratings.common.adapter.ExpandedListView;
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

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeatureFragment extends BaseFragment {

    private OnFragmentInteractionListener mListener;
    private Button btnCreateFeature, btnAssign;
    private ListView lvFeatureList;
    private TextView tvNoContent;
    private IHttpRepository repository;
    private Dialog dialog;

    public FeatureFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feature, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        repository = new HttpRepository(getActivity());
        btnAssign = view.findViewById(R.id.btnAssign);
        tvNoContent = view.findViewById(R.id.tvNoContent);
        btnCreateFeature = view.findViewById(R.id.btnCreateFeature);
        lvFeatureList = view.findViewById(R.id.lvFeatureList);
        btnCreateFeature.setOnClickListener(view1 -> {
            createFeature();
        });
        btnAssign.setOnClickListener(view12 -> {
            mListener.assignUserToFeature(0);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getFeatureList();
    }

    private void getFeatureList() {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));
        repository.getAll(ApiUrl.getInstance().getFeatureList(RatingsApplication.getInstant().getUser().getUserId()), Feature[].class, header, new ResponseListener<List<Feature>>() {
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
                Toast.makeText(getActivity(), Util.get().getMessage((VolleyError) error), Toast.LENGTH_LONG).show();
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

    public interface OnFragmentInteractionListener {
        void assignUserToFeature(int featureId);
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
                vw = LayoutInflater.from(getActivity()).inflate(R.layout.feature_row, null);
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
        dialog = new Dialog(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.create_feature, null);
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
            submitFeature(feature);
        });


    }

    private void submitFeature(Feature feature) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));
        String url = ApiUrl.getInstance().createFeature();
        repository.post(url, Feature.class, RtClients.getInstance().getGson().toJson(feature), header, new ResponseListener<Feature>() {
            @Override
            public void success(Feature response) {
                if (dialog != null)
                    dialog.dismiss();
                Toast.makeText(getActivity(), "Done ", Toast.LENGTH_LONG).show();
                getFeatureList();
            }

            @Override
            public void error(Throwable error) {
                if (dialog != null)
                    dialog.dismiss();
                Toast.makeText(getActivity(), Util.get().getMessage((VolleyError) error), Toast.LENGTH_LONG).show();
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
                showUserList(title, response);
            }

            @Override
            public void error(Throwable error) {
                Toast.makeText(getActivity(), Util.get().getMessage((VolleyError) error), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showUserList(String title, List<User> response) {
        Dialog mShowAssign = new Dialog(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.show_feature_wise_users, null);
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
            updateUser(user);
        });
        adapter.setItemUnCheckedListener(position -> {
            User user = response.get(position);
            user.setActive(false);
            updateUser(user);
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
        Dialog dialog = new Dialog(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_show_feature_user_reviews, null);
        dialog.setContentView(view);
        dialog.show();
        RecyclerView lvUserReviewList = view.findViewById(R.id.lvUserReviewList);

        ReviewHistoryFragment.UserReviewAdapter adapter = new ReviewHistoryFragment.UserReviewAdapter(response, getActivity());
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getActivity());
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
        Dialog dialog = new Dialog(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_show_feature_user_rating_summary, null);
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
        RatingAdapter ratingAdapter = new RatingAdapter(getActivity(), response);
        lvOverallRatings.setAdapter(ratingAdapter);
    }

    private void updateUser(User user) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("accessToken", String.valueOf(RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()));
        String url = ApiUrl.getInstance().getUpdateFeatureUser();
        repository.post(url, User.class, RtClients.getInstance().getGson().toJson(user), header, new ResponseListener<User>() {
            @Override
            public void success(User response) {
                if (dialog != null)
                    dialog.dismiss();
                Toast.makeText(getActivity(), "Done ", Toast.LENGTH_LONG).show();
            }

            @Override
            public void error(Throwable error) {
                if (dialog != null)
                    dialog.dismiss();
                Toast.makeText(getActivity(), Util.get().getMessage((VolleyError) error), Toast.LENGTH_LONG).show();
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
                vw = LayoutInflater.from(getActivity()).inflate(R.layout.featurewise_users_row, null);
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
