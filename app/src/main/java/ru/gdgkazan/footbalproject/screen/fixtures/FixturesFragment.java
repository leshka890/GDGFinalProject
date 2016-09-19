package ru.gdgkazan.footbalproject.screen.fixtures;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.arturvasilov.rxloader.LifecycleHandler;
import ru.arturvasilov.rxloader.LoaderLifecycleHandler;
import ru.gdgkazan.footbalproject.R;
import ru.gdgkazan.footbalproject.model.content.Fixture;
import ru.gdgkazan.footbalproject.screen.loading.LoadingDialog;
import ru.gdgkazan.footbalproject.screen.loading.LoadingView;
import ru.gdgkazan.footbalproject.widget.DividerItemDecoration;

/**
 * Created by Alexey Antonchik on 18.09.16.
 */
public class FixturesFragment extends Fragment implements FixturesView{


    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private FixturesAdapter mFixturesAdapter;
    private LifecycleHandler mLifecycleHandler;
    private FixturesPresenter mFixturesPresenter;
    private LoadingView mLoadingView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fixtures, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mLoadingView = LoadingDialog.view(getChildFragmentManager());

        mFixturesAdapter = new FixturesAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        mRecyclerView.setAdapter(mFixturesAdapter);

        mLifecycleHandler = LoaderLifecycleHandler.create(getActivity(), getLoaderManager());
        mFixturesPresenter = new FixturesPresenter(this, mLifecycleHandler);
        mFixturesPresenter.init();
    }

    @Override
    public void setFixtures(List<Fixture> fixtures) {
        mFixturesAdapter.setData(fixtures);
    }

    @Override
    public void clickFixture(Fixture fixture) {

    }

    @Override
    public void showLoadingIndicator() {
        mLoadingView.showLoadingIndicator();
    }

    @Override
    public void hideLoadingIndicator() {
        mLoadingView.hideLoadingIndicator();
    }

    @Override
    public void showError() {
        Snackbar.make(mRecyclerView, getResources().getString(R.string.loading_error), Snackbar.LENGTH_SHORT).show();
    }
}
