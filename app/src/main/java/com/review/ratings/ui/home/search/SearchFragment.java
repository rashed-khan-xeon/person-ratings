package com.review.ratings.ui.home.search;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.rashedkhan.ratings.R;
import com.review.ratings.common.BaseFragment;
import com.review.ratings.config.ApiUrl;
import com.review.ratings.core.RatingsApplication;
import com.review.ratings.core.RtClients;
import com.review.ratings.data.implementation.HttpRepository;
import com.review.ratings.data.model.User;
import com.review.ratings.ui.home.justify.JustifyFragment;
import com.review.ratings.util.Util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends BaseFragment implements SearchContract.SearchView {
    private Transfer transfer;
    private RecyclerView rcvPersonList;
    private SearchContract.SearchPresenter presenter;
    private EditText etUserNameOrEmail;
    PersonListAdapter adapter;
    private List<User> users;


    View homeView;
    private CardView demoPerson;
    private Button btnSearch;

    public SearchFragment() {
        // Required empty public constructor
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
        btnSearch = homeView.findViewById(R.id.btnSearch);
        demoPerson = homeView.findViewById(R.id.demoPerson);
        etUserNameOrEmail = homeView.findViewById(R.id.etUserNameOrEmail);
        rcvPersonList = homeView.findViewById(R.id.rcvPersonList);
        btnSearch.setOnClickListener(view -> {
            if (!getUrl().isEmpty()) {
                hideKeyboard();
                Util.get().showProgress(getActivity(), true, "Searching...");
                presenter.searchUser(ApiUrl.getInstance().getSearchUserUrl(getUrl()));
            }
        });
        adView.loadAd(new AdRequest.Builder().build());

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
    public void setUserListToView(List<User> userList) {
        users = userList;
        Util.get().showProgress(getActivity(), false, null);
        if (users != null) {
            adapter = new PersonListAdapter(users, getActivity());
            RecyclerView.LayoutManager lm = new LinearLayoutManager(getActivity());
            rcvPersonList.setLayoutManager(lm);
            rcvPersonList.setItemAnimator(new DefaultItemAnimator());
            rcvPersonList.setAdapter(adapter);
            adapter.setOnItemClickListener(position -> {
//                Intent i = new Intent(getActivity(), JustifyActivity.class);
//                i.putExtra("user", RtClients.getInstance().getGson().toJson(users.get(position)));
//                getActivity().startActivity(i);
//                getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                if (users.get(position).getUserId() == RatingsApplication.getInstant().getRatingsPref().getUser().getUserId()) {
                    Util.get().showToastMsg(getActivity(), "You can't justify yourself !");
                    return;
                }
                JustifyFragment jsf = new JustifyFragment();
                Bundle bundle = new Bundle();
                bundle.putString("user", RtClients.getInstance().getGson().toJson(users.get(position)));
                jsf.setArguments(bundle);
                transfer.transferFragment(jsf);
            });
        }
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
            return new Holder(LayoutInflater.from(context).inflate(R.layout.person_list_item_row, parent, false));
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
                                    holder.ivUserImageRow.setImageDrawable(context.getDrawable(R.drawable.avatar));
                                }
                            }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        holder.ivUserImageRow.setImageDrawable(context.getDrawable(R.drawable.avatar));
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
