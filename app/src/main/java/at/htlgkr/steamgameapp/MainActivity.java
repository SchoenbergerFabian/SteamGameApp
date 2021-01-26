package at.htlgkr.steamgameapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import at.htlgkr.steam.Game;
import at.htlgkr.steam.ReportType;
import at.htlgkr.steam.SteamBackend;

public class MainActivity extends Activity {
    private static final String GAMES_CSV = "games.csv";

    private SteamBackend sb = new SteamBackend();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadGamesIntoListView();
        setUpReportSelection();
        setUpSearchButton();
        setUpAddGameButton();
        setUpSaveButton();
    }

    private void loadGamesIntoListView() {
        try {
            sb.loadGames(getAssets().open("games.csv"));
            ListView gamesList = findViewById(R.id.gamesList);
            gamesList.setAdapter(new GameAdapter(this,R.layout.game_item_layout,sb.getGames()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setUpReportSelection() {
        List<ReportTypeSpinnerItem> spinnerItems = new ArrayList<>();
        spinnerItems.add(new ReportTypeSpinnerItem(ReportType.NONE,SteamGameAppConstants.SELECT_ONE_SPINNER_TEXT));
        spinnerItems.add(new ReportTypeSpinnerItem(ReportType.SUM_GAME_PRICES,SteamGameAppConstants.SUM_GAME_PRICES_SPINNER_TEXT));
        spinnerItems.add(new ReportTypeSpinnerItem(ReportType.AVERAGE_GAME_PRICES,SteamGameAppConstants.AVERAGE_GAME_PRICES_SPINNER_TEXT));
        spinnerItems.add(new ReportTypeSpinnerItem(ReportType.UNIQUE_GAMES,SteamGameAppConstants.UNIQUE_GAMES_SPINNER_TEXT));
        spinnerItems.add(new ReportTypeSpinnerItem(ReportType.MOST_EXPENSIVE_GAMES,SteamGameAppConstants.MOST_EXPENSIVE_GAMES_SPINNER_TEXT));

        Spinner chooseReport = findViewById(R.id.chooseReport);
        chooseReport.setAdapter(new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,spinnerItems));
        chooseReport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ReportTypeSpinnerItem spinnerItem = (ReportTypeSpinnerItem) adapterView.getItemAtPosition(i);

                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle(spinnerItem.getDisplayText());

                switch(spinnerItem.getType()){
                    case NONE:
                        break;
                    case SUM_GAME_PRICES:
                        alert.setMessage(SteamGameAppConstants.ALL_PRICES_SUM+sb.sumGamePrices()).show();
                        break;
                    case AVERAGE_GAME_PRICES:
                        alert.setMessage(SteamGameAppConstants.ALL_PRICES_AVERAGE+sb.averageGamePrice()).show();
                        break;
                    case UNIQUE_GAMES:
                        alert.setMessage(SteamGameAppConstants.UNIQUE_GAMES_COUNT+sb.getUniqueGames().size()).show();
                        break;
                    case MOST_EXPENSIVE_GAMES:
                        List<Game> topGames = sb.selectTopNGamesDependingOnPrice(3);
                        alert.setMessage(SteamGameAppConstants.MOST_EXPENSIVE_GAMES
                                +"\n"+topGames.get(0)
                                +"\n"+topGames.get(1)
                                +"\n"+topGames.get(2)).show();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setUpSearchButton() {
        Button search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText searchTerm = new EditText(getApplicationContext());
                searchTerm.setId(R.id.dialog_search_field);

                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle(SteamGameAppConstants.ENTER_SEARCH_TERM)
                        .setView(searchTerm)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //TODO custom filter?
                                List<Game> filteredGames = sb.getGames().stream()
                                        .filter(game -> game.getName().toLowerCase().trim().contains(searchTerm.getText().toString().toLowerCase().trim()))
                                        .collect(Collectors.toList());
                                        ListView gamesList = findViewById(R.id.gamesList);
                                gamesList.setAdapter(new GameAdapter(getApplicationContext(),R.layout.game_item_layout,filteredGames));
                            }
                }).setNegativeButton("Cancel",null).show();
            }
        });
    }

    private void setUpAddGameButton() {
        Button addGame = findViewById(R.id.addGame);
        addGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout layout = new LinearLayout(getApplicationContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                EditText name = new EditText(getApplicationContext());
                name.setId(R.id.dialog_name_field);
                layout.addView(name);
                EditText date = new EditText(getApplicationContext());
                date.setId(R.id.dialog_date_field);
                layout.addView(date);
                EditText price = new EditText(getApplicationContext());
                price.setId(R.id.dialog_price_field);
                layout.addView(price);

                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle(SteamGameAppConstants.ENTER_SEARCH_TERM)
                        .setView(layout)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    sb.addGame(new Game(name.getText().toString(),new SimpleDateFormat(Game.DATE_FORMAT).parse(date.getText().toString()),Double.parseDouble(price.getText().toString())));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).setNegativeButton("Cancel",null).show();
            }
        });
    }

    private void setUpSaveButton() {
        //TODO
        // Implementieren Sie diese Methode.
    }
}
