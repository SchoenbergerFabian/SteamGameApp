package at.htlgkr.steamgameapp;


import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import at.htlgkr.steam.Game;

public class GameAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private int listViewItemLayoutId;
    private List<Game> games;

    public GameAdapter(Context context, int listViewItemLayoutId, List<Game> games) {
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listViewItemLayoutId = listViewItemLayoutId;
        this.games = games;
    }

    @Override
    public int getCount() {
        return games.size();
    }

    @Override
    public Object getItem(int position) {
        return games.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View givenView, ViewGroup parent) {
        View view = (givenView==null) ? inflater.inflate(this.listViewItemLayoutId,null) : givenView;

        Game game = (Game) getItem(position);

        TextView name = view.findViewById(R.id.name);
        TextView releaseDate = view.findViewById(R.id.releaseDate);
        TextView price = view.findViewById(R.id.price);

        name.setText(game.getName());
        SimpleDateFormat format = new SimpleDateFormat(Game.DATE_FORMAT);
        releaseDate.setText(format.format(game.getReleaseDate()));
        price.setText(""+game.getPrice());

        return view;
    }
}
