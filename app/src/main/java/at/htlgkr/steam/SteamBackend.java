package at.htlgkr.steam;


import android.util.Log;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.Buffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class SteamBackend {

    private final String SEPARATOR = ";";
    private List<Game> games;

    public SteamBackend() {
        games = new ArrayList<>();
    }

    public void loadGames(InputStream inputStream) {
        games.clear();
        SimpleDateFormat format = new SimpleDateFormat(Game.DATE_FORMAT);

        try(BufferedReader in = new BufferedReader(new InputStreamReader(inputStream))){
            String line;
            while((line=in.readLine())!=null){
                String[] arguments = line.split(SEPARATOR);
                try {
                    games.add(new Game(arguments[0],format.parse(arguments[1]),Double.parseDouble(arguments[2])));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void store(OutputStream fileOutputStream) {
        SimpleDateFormat format = new SimpleDateFormat(Game.DATE_FORMAT);

        PrintWriter out = new PrintWriter(new OutputStreamWriter(fileOutputStream));
        games.forEach(game -> {
            out.println(game.getName()+SEPARATOR+format.format(game.getReleaseDate())+SEPARATOR+game.getPrice());
            out.flush();
        });
        out.close();
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games.clear();
        this.games.addAll(games);
    }

    public void addGame(Game newGame) {
        this.games.add(newGame);
    }

    public double sumGamePrices() {
        return games.stream().mapToDouble(Game::getPrice).sum();
    }

    public double averageGamePrice() {
        return games.stream().mapToDouble(Game::getPrice).average().orElse(0.0);
    }

    public List<Game> getUniqueGames() {
        return games.stream().distinct().collect(Collectors.toList());
    }

    public List<Game> selectTopNGamesDependingOnPrice(int n) {
        return games.stream().sorted((game, t1) -> {
                    if(game.getPrice()==t1.getPrice()){
                        return 0;
                    }else if(game.getPrice()<t1.getPrice()){
                        return 1;
                    }else{
                        return -1;
                    }

                }).limit(n)
                .collect(Collectors.toList());
    }
}
