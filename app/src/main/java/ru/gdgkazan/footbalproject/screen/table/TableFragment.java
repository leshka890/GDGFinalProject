package ru.gdgkazan.footbalproject.screen.table;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.arturvasilov.rxloader.LifecycleHandler;
import ru.arturvasilov.rxloader.LoaderLifecycleHandler;
import ru.gdgkazan.footbalproject.R;
import ru.gdgkazan.footbalproject.model.content.Standings;
import ru.gdgkazan.footbalproject.screen.loading.LoadingDialog;
import ru.gdgkazan.footbalproject.screen.loading.LoadingView;
import ru.gdgkazan.footbalproject.screen.table.adapter.StandingsAdapter;
import ru.gdgkazan.footbalproject.screen.team.TeamActivity;
import ru.gdgkazan.footbalproject.widget.DividerItemDecoration;

/**
 * Created by Alexey Antonchik on 18.09.16.
 */
public class TableFragment extends Fragment
        implements TableContract.View, StandingsAdapter.OnItemClick, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.recyclerViewStandings)
    RecyclerView mRecyclerViewStandings;

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private LoadingView mLoadingView;

    private StandingsAdapter mAdapter;

    private TablePresenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_table, null);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        mLoadingView = LoadingDialog.view(getActivity().getSupportFragmentManager());

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mRecyclerViewStandings.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewStandings.addItemDecoration(new DividerItemDecoration(getActivity()));
        mAdapter = new StandingsAdapter(new ArrayList<>(), this);
        mRecyclerViewStandings.setAdapter(mAdapter);

        LifecycleHandler mLifeCycleHandler = LoaderLifecycleHandler.create(getActivity(), getLoaderManager());
        mPresenter = new TablePresenter(this, mLifeCycleHandler);
        mPresenter.load();

        return view;
    }

    @Override
    public void showTable(List<Standings> standingsList) {
        mAdapter.changeDataSet(standingsList);
    }

    @Override
    public void showError() {
        Snackbar.make(mRecyclerViewStandings, getResources().getString(R.string.loading_error), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideSwipeRefreshing() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onItemClick(@NonNull Standings standings) {
        TeamActivity.navigate((AppCompatActivity) getActivity(), standings.getTeamName());
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
    public void onRefresh() {
        mPresenter.reload();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.table_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.table_sort_by_points_a_z:
                mPresenter.onClickSortByPointsFromAToZ();
                return true;
            case R.id.table_sort_by_points_z_a:
                mPresenter.onClickSortByPointsFromZToA();
                return true;
            case R.id.table_sort_by_scored_goals_a_z:
                mPresenter.onClickSortByScoredGoalsFromAToZ();
                return true;
            case R.id.table_sort_by_scored_goals_z_a:
                mPresenter.onClickSortByScoredGoalsFromZToA();
                return true;
            case R.id.table_sort_by_against_goals_a_z:
                mPresenter.onClickSortByAgainstGoalsFromAToZ();
                return true;
            case R.id.table_sort_by_against_goals_z_a:
                mPresenter.onClickSortByAgainstGoalsFromZToA();
                return true;
            default:
                return false;
        }
    }

}
