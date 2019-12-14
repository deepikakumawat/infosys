package com.infosys.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.infosys.BaseActivity;
import com.infosys.R;
import com.infosys.model.InfoData;
import com.infosys.model.Row;
import com.infosys.network.Service;
import com.infosys.utility.VerticalSpaceItemDecoration;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements MainActivityView, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView list;
    @Inject
    public Service service;
    ProgressBar progressBar;
    private List<Row> dataList;
    private final int VERTICAL_ITEM_SPACE = 8;
    private MainActivityPresenter presenter;
    private TextView tvTitle;
    private SwipeRefreshLayout pullDownRefreshCall;
    private MainActivityAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDependency().inject(this);

        renderView();
        init();

        presenter = new MainActivityPresenter(service, this);
        callAPI();
    }

    public void renderView() {
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.list);
        tvTitle = findViewById(R.id.txt_title);
        progressBar = findViewById(R.id.progress);
        pullDownRefreshCall = (SwipeRefreshLayout) findViewById(R.id.pullDownRefreshCall);
        pullDownRefreshCall.setOnRefreshListener(this);
        pullDownRefreshCall.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimary);

    }

    public void init() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        list.addItemDecoration(new VerticalSpaceItemDecoration((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, VERTICAL_ITEM_SPACE, metrics)));
        list.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
        setPullToRefreshFalse();
    }

    @Override
    public void onFailure(String appErrorMessage) {
        showToast(appErrorMessage);
    }

    @Override
    public void getListSuccess(InfoData data) {
        if (data != null) {
            dataList = data.getRows();
            String title = data.getTitle();
            if (!TextUtils.isEmpty(title)) {
                tvTitle.setVisibility(View.VISIBLE);
                tvTitle.setText(title);
            }else{
                tvTitle.setVisibility(View.GONE);
            }
            if (!dataList.isEmpty()) {
                adapter = new MainActivityAdapter(getApplicationContext(), dataList);
                list.setAdapter(adapter);
            } else {
                showToast(getString(R.string.no_data));
            }
        }

    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

    }



    @Override
    public void onRefresh() {

        dataList.clear();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        callAPI();

    }

    private void callAPI() {
        presenter.getList();
    }

    private void setPullToRefreshFalse() {
        if (pullDownRefreshCall.isRefreshing()) {
            pullDownRefreshCall.setRefreshing(false);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onStop();
    }

}
