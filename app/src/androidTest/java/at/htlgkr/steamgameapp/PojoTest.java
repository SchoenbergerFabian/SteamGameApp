package at.htlgkr.steamgameapp;

import android.provider.ContactsContract;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.atomic.DoubleAccumulator;

import at.htlgkr.steam.Game;
import at.htlgkr.steam.SteamBackend;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

public class PojoTest {
    @Test
    public void loadGames_size() {
        String data = GameDataCsvString.ALL_DATA_HEADER + GameDataCsvString.ALL_DATA;

        InputStream testInputStream = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));

        SteamBackend instance = new SteamBackend();
        instance.loadGames(testInputStream);

        List<Game> actual = instance.getGames();
        List<Game> expected = DataHelper.getAllGames();

        assertEquals(expected.size(), actual.size());
    }

    @Test
    public void loadGames_equalLists() {
        String data = GameDataCsvString.ALL_DATA_HEADER + GameDataCsvString.ALL_DATA;

        InputStream testInputStream = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));

        SteamBackend instance = new SteamBackend();
        instance.loadGames(testInputStream);

        List<Game> actual = instance.getGames();
        List<Game> expected = DataHelper.getAllGames();

        DataHelper.assertEqualsList(expected, actual);
    }

    @Test
    public void storeGames_returnsSomething() {
        SteamBackend steamBackend = new SteamBackend();
        steamBackend.setGames(DataHelper.smallGameList());

        ByteArrayOutputStream outputTest = new ByteArrayOutputStream();

        steamBackend.store(outputTest);

        String actual = new String(outputTest.toByteArray());

        assertNotNull(actual);
        assertTrue(actual.length() > 0);
    }

    @Test
    public void storeGames_equals() {
        SteamBackend steamBackend = new SteamBackend();
        steamBackend.setGames(DataHelper.smallGameList());

        ByteArrayOutputStream outputTest = new ByteArrayOutputStream();

        steamBackend.store(outputTest);

        String actual = new String(outputTest.toByteArray());

        assertEquals(GameDataCsvString.ALL_DATA_5, actual);
    }

    @Test
    public void gamesGetterSetterTest() {
        SteamBackend steamBackend = new SteamBackend();
        steamBackend.setGames(DataHelper.smallGameList());
        List<Game> actual = steamBackend.getGames();

        assertEquals(DataHelper.smallGameList(), actual);
    }

    @Test
    public void sumGamePrices_returnsSomething() {
        SteamBackend steamBackend = new SteamBackend();
        steamBackend.setGames(DataHelper.getAllGames());
        double actual = steamBackend.sumGamePrices();

        assertTrue(actual > 0);
    }

    @Test
    public void sumGamePrices_returnsCorrectResult() {
        SteamBackend steamBackend = new SteamBackend();
        steamBackend.setGames(DataHelper.getAllGames());
        double actual = steamBackend.sumGamePrices();

        assertEquals(8926, actual, 0.5);
    }

    @Test
    public void averageGamePrice_returnsSomething() {
        SteamBackend steamBackend = new SteamBackend();
        steamBackend.setGames(DataHelper.getAllGames());
        double actual = steamBackend.averageGamePrice();

        assertTrue(actual > 0);
    }

    @Test
    public void averageGamePrice_returnsCorrectResult() {
        SteamBackend steamBackend = new SteamBackend();
        steamBackend.setGames(DataHelper.getAllGames());
        double actual = steamBackend.averageGamePrice();

        assertEquals(17.5, actual, 0.5);
    }

    @Test
    public void getUniqueGames_returnsSomething() {
        SteamBackend steamBackend = new SteamBackend();
        steamBackend.setGames(DataHelper.getAllGames());
        List<Game> actual = steamBackend.getUniqueGames();

        assertNotNull(actual);
        assertTrue(actual.size() > 0);
    }

    @Test
    public void getUniqueGames_returnsCorrectResult() {
        SteamBackend steamBackend = new SteamBackend();
        steamBackend.setGames(DataHelper.getAllGames());
        List<Game> actual = steamBackend.getUniqueGames();

        assertEquals(499, actual.size());
        for(Game cGame : actual) {
            assertEquals(1, DataHelper.gameInListCount(cGame, actual));
        }
        DataHelper.assertEqualsList(actual, DataHelper.getAllGames());
    }

    @Test
    public void selectTopNGamesDependingOnPrice_returnsSomething() {
        SteamBackend steamBackend = new SteamBackend();
        steamBackend.setGames(DataHelper.getAllGames());
        List<Game> actual = steamBackend.selectTopNGamesDependingOnPrice(5);

        assertNotNull(actual);
        assertTrue(actual.size() > 0);
    }

    @Test
    public void selectTopNGamesDependingOnPrice_listSize() {
        SteamBackend steamBackend = new SteamBackend();
        steamBackend.setGames(DataHelper.getAllGames());
        List<Game> actual = steamBackend.selectTopNGamesDependingOnPrice(5);

        assertEquals(5, actual.size());
    }

    @Test
    public void selectTopNGamesDependingOnPrice_correctResult() {
        SteamBackend steamBackend = new SteamBackend();
        steamBackend.setGames(DataHelper.getAllGames());
        List<Game> actual = steamBackend.selectTopNGamesDependingOnPrice(5);
        List<Game> expected = DataHelper.getTop5Games();

        DataHelper.assertEqualsList(actual, expected);
    }

    @Test
    public void addGame_onEmpty() {
        SteamBackend steamBackend = new SteamBackend();
        steamBackend.addGame(new Game());
        assertEquals(1, steamBackend.getGames().size());
    }

    @Test
    public void addGame_onFull() {
        SteamBackend steamBackend = new SteamBackend();
        steamBackend.setGames(DataHelper.getAllGames());
        steamBackend.addGame(new Game());
        assertEquals(DataHelper.getAllGames().size() + 1, steamBackend.getGames().size());
    }

    @Test
    public void game_getterSetterTest() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Game test = getTestGame();
        assertEquals("testGame", test.getName());
        assertTrue(DataHelper.dateEquals(test.getReleaseDate(), sdf.parse("12.12.2018")));
        assertTrue(DataHelper.doubleEquals(test.getPrice(), 13.37d));
    }

    @Test
    public void game_testToString() throws ParseException {
        Game test = getTestGame();
        assertEquals("[12.12.2018] testGame 13.37", test.toString());
    }

    @Test
    public void game_testEquals() {
        Game test1 = getTestGame();
        Game test2 = getTestGame();
        Game test3 = getTestGame();
        test3.setName("test2");

        assertTrue(test1.equals(test2));
        assertFalse(test2.equals(test3));
    }

    @Test
    public void hashCode_testCorrect() {
        Game cGame =getTestGame();
        Game cGame2 =getTestGame();
        Game cGame3 = getTestGame();
        cGame3.setName("test2");

        assertEquals(cGame.hashCode(), cGame2.hashCode());
        assertNotEquals(cGame.hashCode(), cGame3.hashCode());
    }

    private Game getTestGame() {
        Game game_1 = new Game();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        game_1.setName("testGame");
        try {
            game_1.setReleaseDate(sdf.parse("12.12.2018"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        game_1.setPrice(13.37d);
        return game_1;
    }
}

