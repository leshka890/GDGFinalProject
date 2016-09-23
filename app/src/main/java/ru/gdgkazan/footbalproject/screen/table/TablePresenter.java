package ru.gdgkazan.footbalproject.screen.table;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.Comparator;

import ru.arturvasilov.rxloader.LifecycleHandler;
import ru.gdgkazan.footbalproject.R;
import ru.gdgkazan.footbalproject.model.content.Standings;
import ru.gdgkazan.footbalproject.repository.RepositoryProvider;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mikes on 21.09.16.
 */

public class TablePresenter implements TableContract.Presenter {

    private final TableContract.View mView;
    private final LifecycleHandler mLifeCycleHandler;

    private final int SORT_BY_POINTS_FROM_A_TO_Z = 123;
    private final int SORT_BY_POINTS_FROM_Z_TO_A = 132;
    private final int SORT_BY_SCORED_GOALS_FROM_A_TO_Z = 213;
    private final int SORT_BY_SCORED_GOALS_FROM_Z_TO_A = 231;
    private final int SORT_BY_AGAINST_GOALS_FROM_A_TO_Z = 312;
    private final int SORT_BY_AGAINST_GOALS_FROM_Z_TO_A = 321;
    private final int SORT_NONE = 333;

    public TablePresenter(@NonNull TableContract.View view, @NonNull LifecycleHandler lifecycleHandler){
        mView = view;
        mLifeCycleHandler = lifecycleHandler;
    }

    @Override
    public void load() {
        getData(false, SORT_NONE);
    }

    @Override
    public void reload() {
        getData(true, SORT_NONE);
    }

    @Override
    public void onClickSortByPointsFromAToZ() {
        getData(true, SORT_BY_POINTS_FROM_A_TO_Z);
    }

    @Override
    public void onClickSortByPointsFromZToA() {
        getData(true, SORT_BY_POINTS_FROM_Z_TO_A);
    }

    @Override
    public void onClickSortByScoredGoalsFromAToZ() {
        getData(true, SORT_BY_SCORED_GOALS_FROM_A_TO_Z);
    }

    @Override
    public void onClickSortByScoredGoalsFromZToA() {
        getData(true, SORT_BY_SCORED_GOALS_FROM_Z_TO_A);
    }

    @Override
    public void onClickSortByAgainstGoalsFromAToZ() {
        getData(true, SORT_BY_AGAINST_GOALS_FROM_A_TO_Z);
    }

    @Override
    public void onClickSortByAgainstGoalsFromZToA() {
        getData(true, SORT_BY_AGAINST_GOALS_FROM_Z_TO_A);
    }

    private Comparator<Standings> sortByScoredGoalsFromAToZ = (standings1, standings2) -> {
        if(standings1.getGoals() > standings2.getGoals()){
            return -1;
        }
        return 1;
    };

    private Comparator<Standings> sortByScoredGoalsFromZToA = (standings1, standings2) -> {
        if(standings1.getGoals() < standings2.getGoals()){
            return -1;
        }
        return 1;
    };

    private Comparator<Standings> sortByAgainstGoalsFromAToZ = (standings1, standings2) -> {
        if(standings1.getGoalsAgainst() > standings2.getGoalsAgainst()){
            return -1;
        }
        return 1;
    };

    private Comparator<Standings> sortByAgainstGoalsFromZToA = (standings1, standings2) -> {
        if(standings1.getGoalsAgainst() < standings2.getGoalsAgainst()){
            return -1;
        }
        return 1;
    };

    private Comparator<Standings> sortByPointsFromAToZ = (standings1, standings2) -> {
        if(standings1.getPoints() > standings2.getPoints()){
            return -1;
        }
        return 1;
    };

    private Comparator<Standings> sortByPointsFromZToA = (standings1, standings2) -> {
        if(standings1.getPoints() < standings2.getPoints()){
            return -1;
        }
        return 1;
    };

    private void getData(boolean isReload, int SORT_TYPE) {
        RepositoryProvider.provideFootballRepository().standingsList()
                .map(standingsList -> {
                    switch(SORT_TYPE){
                        case SORT_BY_POINTS_FROM_A_TO_Z:
                            Collections.sort(standingsList, sortByPointsFromAToZ);
                            break;
                        case SORT_BY_POINTS_FROM_Z_TO_A:
                            Collections.sort(standingsList, sortByPointsFromZToA);
                            break;
                        case SORT_BY_SCORED_GOALS_FROM_A_TO_Z:
                            Collections.sort(standingsList, sortByScoredGoalsFromAToZ);
                            break;
                        case SORT_BY_SCORED_GOALS_FROM_Z_TO_A:
                            Collections.sort(standingsList, sortByScoredGoalsFromZToA);
                            break;
                        case SORT_BY_AGAINST_GOALS_FROM_A_TO_Z:
                            Collections.sort(standingsList, sortByAgainstGoalsFromAToZ);
                            break;
                        case SORT_BY_AGAINST_GOALS_FROM_Z_TO_A:
                            Collections.sort(standingsList, sortByAgainstGoalsFromZToA);
                            break;
                    }
                    return standingsList;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> {
                    if (!isReload) mView.showLoadingIndicator();
                })
                .doOnTerminate(isReload ? mView::hideSwipeRefreshing
                                        : mView::hideLoadingIndicator)
                .compose(isReload ? mLifeCycleHandler.reload(R.id.standings_list_request)
                                  : mLifeCycleHandler.load(R.id.standings_list_request))
                .subscribe(mView::showTable, throwable -> mView.showError());
    }

}
