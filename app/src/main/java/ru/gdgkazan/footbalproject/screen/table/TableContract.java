package ru.gdgkazan.footbalproject.screen.table;

import java.util.List;

import ru.gdgkazan.footbalproject.model.content.Standings;
import ru.gdgkazan.footbalproject.screen.loading.LoadingView;

/**
 * Created by mikes on 21.09.16.
 */

public interface TableContract {

    interface View extends LoadingView{

        void showTable(List<Standings> standingsList);

        void showError();

        void showToastMessage(String message);

        void hideSwipeRefreshing();

    }

    interface Presenter {

        void load();

        void reload();

        void onClickSortByPointsFromAToZ();

        void onClickSortByPointsFromZToA();

        void onClickSortByScoredGoalsFromAToZ();

        void onClickSortByScoredGoalsFromZToA();

        void onClickSortByAgainstGoalsFromAToZ();

        void onClickSortByAgainstGoalsFromZToA();

    }

}
