package ru.gdgkazan.footbalproject.screen.table;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import ru.gdgkazan.footbalproject.R;
import ru.gdgkazan.footbalproject.model.content.Standings;
import ru.gdgkazan.footbalproject.screen.table.adapter.StandingsAdapter;
import ru.gdgkazan.footbalproject.utils.Dialog;

/**
 * Created by Alexey Antonchik on 18.09.16.
 */
public class TableFragment extends Fragment implements TableContract.View, StandingsAdapter.OnItemClick {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table, null);
        return view;
    }

    @Override
    public void showTable(List<Standings> standingsList) {
        //TODO: show table in recycler view
    }

    @Override
    public void showLoadingProgress() {
        //TODO: show loading progress
    }

    @Override
    public void hideLoadingProgress() {
        //TODO: hide loading progress
    }

    @Override
    public void showError() {
        Dialog.showWithPositiveButton(
                getActivity(),
                getString(R.string.error),
                getString(R.string.error_please_try_again_refresh_your_table),
                getString(R.string.ok));
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(@NonNull Standings standing) {
        Toast.makeText(getActivity(), "Pressed on standings!", Toast.LENGTH_SHORT).show();
    }
}