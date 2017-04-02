package com.example.hp.gameapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class GameListFragment extends ListFragment {
    interface GameListListener {
        void itemClicked(long id);
    }

    private GameListListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String[] games = this.getResources().getStringArray(R.array.games);

        ArrayAdapter<String> gameListAdapter = new ArrayAdapter<>(
                inflater.getContext(), android.R.layout.simple_list_item_1, games);
        setListAdapter(gameListAdapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity)
            this.listener = (GameListListener) context;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (listener != null) {
            listener.itemClicked(id);
        }
    }
}
