package com.rashedkhan.ratings.ui.home.search;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.rashedkhan.ratings.R;
import com.rashedkhan.ratings.common.BaseFragment;
import com.rashedkhan.ratings.common.adapter.ExpandedListView;
import com.rashedkhan.ratings.common.adapter.RatingAdapter;
import com.rashedkhan.ratings.common.adapter.SpinnerAdapter;
import com.rashedkhan.ratings.config.ApiUrl;
import com.rashedkhan.ratings.core.RatingsApplication;
import com.rashedkhan.ratings.core.RtClients;
import com.rashedkhan.ratings.data.implementation.HttpRepository;
import com.rashedkhan.ratings.data.model.Feature;
import com.rashedkhan.ratings.data.model.RatingSummary;
import com.rashedkhan.ratings.data.model.User;
import com.rashedkhan.ratings.ui.home.justify.JustifyFragment;
import com.rashedkhan.ratings.ui.home.setting.SettingFragment;
import com.rashedkhan.ratings.util.Util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends BaseFragment implements SearchContract.SearchView {
    private Transfer transfer;
    private RecyclerView rcvPersonList;
    private SearchContract.SearchPresenter presenter;
    private EditText etUserNameOrEmail;
    private ExpandedListView lvOverallRatings;
    private PersonListAdapter adapter;
    private List<User> users;
    private LinearLayout cvOverallRate;
    private TextView tvNoRatingsMsg;
    private Spinner spnFeature;
    View homeView;
    private Button btnSearch;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeView = inflater.inflate(R.layout.fragment_home, container, false);
        presenter = new SearchPresenter(new HttpRepository(getActivity()), this);
        initViewComponents();
        return homeView;
    }

    private String getUrl() {
        String keyWord = "";
        if (!TextUtils.isEmpty(etUserNameOrEmail.getText())) {
            keyWord = keyWord.concat(etUserNameOrEmail.getText().toString());
        }
        try {
            return URLEncoder.encode(keyWord, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return keyWord;
    }

    @Override
    public void initViewComponents() {
        AdView adView = homeView.findViewById(R.id.adView);
        lvOverallRatings = homeView.findViewById(R.id.lvOverallRatings);
        cvOverallRate = homeView.findViewById(R.id.cvOverallRate);
        btnSearch = homeView.findViewById(R.id.btnSearch);
        etUserNameOrEmail = homeView.findViewById(R.id.etUserNameOrEmail);
        spnFeature = homeView.findViewById(R.id.spnFeature);
        tvNoRatingsMsg = homeView.findViewById(R.id.tvNoRatingsMsg);

        btnSearch.setOnClickListener(view -> {
            searchUser();
        });
        if (RatingsApplication.getInstant().getUser() != null)
            presenter.getUserAvgRatingByCategory(ApiUrl.getInstance().getAvgRatingUrl(RatingsApplication.getInstant().getUser().getUserId()));
        adView.loadAd(new AdRequest.Builder().build());
        getFeatures();
        spnFeature.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Map.Entry<String, String> item = (Map.Entry<String, String>) spnFeature.getSelectedItem();
                int itemId = Integer.parseInt(item.getKey());
                if (itemId != 0)
                    searchFeature(itemId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getFeatures() {
        presenter.getFeatureListForUser(ApiUrl.getInstance().getAllFeatureListForUser());
    }

    private void searchUser() {
        if (!getUrl().isEmpty()) {
            hideKeyboard();
            Util.get().showProgress(getActivity(), true, "Searching...");
            presenter.searchUser(ApiUrl.getInstance().getSearchUserUrl(getUrl()));
        }
    }

    private void searchFeature(int featureId) {
        Util.get().showProgress(getActivity(), true, "Getting result...");
        presenter.searchFeatureUser(ApiUrl.getInstance().getFeatureWiseAssignList(featureId));
    }

    @Override
    public void showSuccessMessage(String msg) {
        Util.get().showProgress(getActivity(), false, null);
        Util.get().showToastMsg(getActivity(), msg);
    }

    @Override
    public void showErrorMessage(String msg) {
        Util.get().showProgress(getActivity(), false, null);
        Util.get().showToastMsg(getActivity(), msg);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        transfer = (Transfer) context;
    }

    @Override
    public void setFeaturesToView(List<Feature> features) {
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put("0", "--Select Feature--");
        for (Feature feature : features) {
            if (feature.getTitle() != null)
                data.put(String.valueOf(feature.getFeatureId()), feature.getTitle());
        }
        SpinnerAdapter<String, String> adapter = new SpinnerAdapter<String, String>(getActivity(), android.R.layout.simple_spinner_item, data);
        spnFeature.setAdapter(adapter);
    }

    @Override
    public void setFeatureUserListToView(List<User> userList) {
        Util.get().showProgress(getActivity(), false, null);
        if (userList != null) {
            Dialog dialog = new Dialog(getActivity());
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_search_result, null);
            dialog.setContentView(view);
            dialog.show();
            rcvPersonList = view.findViewById(R.id.rcvPersonList);
            adapter = new PersonListAdapter(userList, getActivity());
            RecyclerView.LayoutManager lm = new LinearLayoutManager(getActivity());
            rcvPersonList.setLayoutManager(lm);
            rcvPersonList.setItemAnimator(new DefaultItemAnimator());
            rcvPersonList.setAdapter(adapter);
            adapter.setOnItemClickListener(position -> {
                if (userList.get(position).getUserId() == RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()) {
                    Util.get().showToastMsg(getActivity(), "You can't justify yourself !");
                    return;
                }
                dialog.dismiss();
                JustifyFragment jsf = new JustifyFragment();
                Bundle bundle = new Bundle();
                bundle.putString("user", RtClients.getInstance().getGson().toJson(userList.get(position)));
                jsf.setArguments(bundle);
                transfer.transferFragment(jsf);
            });
        }
    }

    @Override
    public void setUserListToView(List<User> userList) {
        users = new ArrayList<>();
        for (User user : userList) {
            if (user.getActive() && user.hasVerified() && user.getFeatureId() == 0) {
                users.add(user);
            }
        }
        Util.get().showProgress(getActivity(), false, null);
        if (users != null) {
            Dialog dialog = new Dialog(getActivity());
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_search_result, null);
            dialog.setContentView(view);
            dialog.show();
            rcvPersonList = view.findViewById(R.id.rcvPersonList);
            adapter = new PersonListAdapter(users, getActivity());
            RecyclerView.LayoutManager lm = new LinearLayoutManager(getActivity());
            rcvPersonList.setLayoutManager(lm);
            rcvPersonList.setItemAnimator(new DefaultItemAnimator());
            rcvPersonList.setAdapter(adapter);
            adapter.setOnItemClickListener(position -> {
                if (users.get(position).getUserId() == RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()) {
                    Util.get().showToastMsg(getActivity(), "You can't justify yourself !");
                    return;
                }
                dialog.dismiss();
                JustifyFragment jsf = new JustifyFragment();
                Bundle bundle = new Bundle();
                bundle.putString("user", RtClients.getInstance().getGson().toJson(users.get(position)));
                jsf.setArguments(bundle);
                transfer.transferFragment(jsf);
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //  Dialog dialog = new Dialog(getActivity());
        //  View view = LayoutInflater.from(getActivity()).inflate(R.layout.opening_dialog, null);
        // dialog.setContentView(view);
        // dialog.show();
    }

    @Override
    public void setUserAvgRatingsToView(List<RatingSummary> ratingsCategories) {
        if (ratingsCategories != null) {
            if (ratingsCategories.size() > 0) {
                cvOverallRate.setVisibility(View.VISIBLE);
                RatingAdapter adapter = new RatingAdapter(getContext(), ratingsCategories);
                lvOverallRatings.setAdapter(adapter);
                lvOverallRatings.setExpanded(true);
            }
        }
    }

    @Override
    public void noUserRatings(String msg) {
        tvNoRatingsMsg.setVisibility(View.VISIBLE);
    }

    @Override
    public void noUserFound(String msg) {
        Util.get().showProgress(getActivity(), false, null);
        Util.get().showToastMsg(getActivity(), msg);
    }

    public interface Transfer {
        void transferFragment(Fragment fragment);
    }

    public interface ClickListener {
        void onItemClick(int position);
    }

    private class PersonListAdapter extends RecyclerView.Adapter<PersonListAdapter.Holder> {
        private List<User> users;
        private Context context;
        ClickListener clickListener;

        public PersonListAdapter(List<User> users, Context context) {
            this.users = users;
            this.context = context;
        }

        public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public TextView tvUserNameRow,
                    tvUserEmailRow;
            public CircleImageView ivUserImageRow;

            public Holder(View itemView) {
                super(itemView);
                tvUserNameRow = itemView.findViewById(R.id.tvUserNameRow);
                tvUserEmailRow = itemView.findViewById(R.id.tvUserEmailRow);
                ivUserImageRow = itemView.findViewById(R.id.ivUserImageRow);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                clickListener.onItemClick(getAdapterPosition());
            }
        }

        public void setOnItemClickListener(ClickListener listener) {
            clickListener = listener;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_list_item_row, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            if (!TextUtils.isEmpty(users.get(position).getFullName())) {
                holder.tvUserNameRow.setText(users.get(position).getFullName());
            }
            if (!TextUtils.isEmpty(users.get(position).getEmail())) {
                if (users.get(position).getUserSetting() != null) {
                    if (users.get(position).getUserSetting().getEmailVisible()) {
                        holder.tvUserEmailRow.setText(users.get(position).getEmail());
                    }
                }
            }
            if (users.get(position).getImage() != null) {
                RtClients.getInstance().getImageLoader(context).get(ApiUrl.getInstance().getUserImageUrl(users.get(position).getImage()), new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                        if (users.get(position) != null)
                            if (users.get(position).getUserSetting() != null) {
                                if (users.get(position).getUserSetting().getImageVisible()) {
                                    holder.ivUserImageRow.setImageBitmap(response.getBitmap());
                                } else {
                                    holder.ivUserImageRow.setImageDrawable(context.getResources().getDrawable(R.drawable.avatar));
                                }
                            }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        holder.ivUserImageRow.setImageDrawable(context.getResources().getDrawable(R.drawable.avatar));
                        Log.d(getClass().getSimpleName(), Arrays.toString(error.getStackTrace()));
                    }
                });
            }

        }


        @Override
        public long getItemId(int i) {
            return users.get(i).getUserId();
        }

        @Override
        public int getItemCount() {
            return users.size();
        }


    }
}
