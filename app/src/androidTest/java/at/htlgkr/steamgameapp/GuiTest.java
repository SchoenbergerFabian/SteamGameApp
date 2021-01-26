package at.htlgkr.steamgameapp;

import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.ListView;
import android.widget.Spinner;

import junit.framework.TestCase;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.htlgkr.steam.Game;
import at.htlgkr.steam.ReportType;
import at.htlgkr.steam.SteamBackend;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.StringStartsWith.startsWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class GuiTest {
    /* Kommentieren Sie diesen Code ein wenn die Oberfläche fertig ist
    @Rule
    public ActivityTestRule<MainActivity> myActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test(timeout = 10000)
    public void loadGamesTest_size() {
        ListView gamesListView = myActivityRule.getActivity().findViewById(R.id.gamesList);

        assertEquals(500, gamesListView.getAdapter().getCount());
    }

    @Test(timeout = 10000)
    public void loadGamesTest_equalLists() {
        List<Game> expected = DataHelper.getAllGames();
        List<Game> actual = new ArrayList<>();
        ListView gamesListView = myActivityRule.getActivity().findViewById(R.id.gamesList);

        for(int i = 0; i < gamesListView.getAdapter().getCount(); i++) {
            assertTrue(gamesListView.getAdapter().getItem(i) instanceof Game);

            Game cGame = (Game) gamesListView.getAdapter().getItem(i);

            actual.add(cGame);
        }

        DataHelper.assertEqualsList(expected, actual);
    }

//    Spinner tests für die Bonusaufgabe einkommentieren!
//    @Test(timeout = 10000)
//    public void spinnerTest_dataInSpinner_correctSize() {
//        Spinner spinner = myActivityRule.getActivity().findViewById(R.id.chooseReport);
//        assertEquals(5, spinner.getAdapter().getCount());
//    }
//
//    @Test(timeout = 10000)
//    public void spinnerTest_dataInSpinner_correctData() {
//        Spinner spinner = myActivityRule.getActivity().findViewById(R.id.chooseReport);
//        ReportTypeSpinnerItem first = (ReportTypeSpinnerItem) spinner.getAdapter().getItem(0);
//        assertEquals("Select one", first.getDisplayText());
//    }
//
//    @Test(timeout = 10000)
//    public void spinnerTest_spinnerFunctionality_sumGamePrices_dialogDisplayed() {
//        checkDialogDisplayed(SteamGameAppConstants.SUM_GAME_PRICES_SPINNER_TEXT);
//    }
//
//    @Test(timeout = 10000)
//    public void spinnerTest_spinnerFunctionality_sumGamePrices_showsAnyAnswer() {
//        checkIfDialogContainsText(SteamGameAppConstants.SUM_GAME_PRICES_SPINNER_TEXT, SteamGameAppConstants.ALL_PRICES_SUM);
//    }
//
//    @Test(timeout = 10000)
//    public void spinnerTest_spinnerFunctionality_sumGamePrices_showsCorrectAnswer() {
//        checkIfDialogContainsText(SteamGameAppConstants.SUM_GAME_PRICES_SPINNER_TEXT, SteamGameAppConstants.ALL_PRICES_SUM + "8926");
//    }
//
//    @Test(timeout = 10000)
//    public void spinnerTest_spinnerFunctionality_averageGamePrices_dialogDisplayed() {
//        checkDialogDisplayed(SteamGameAppConstants.AVERAGE_GAME_PRICES_SPINNER_TEXT);
//    }
//
//    @Test(timeout = 10000)
//    public void spinnerTest_spinnerFunctionality_averageGamePrices_showsAnyAnswer() {
//        checkIfDialogContainsText(SteamGameAppConstants.AVERAGE_GAME_PRICES_SPINNER_TEXT, SteamGameAppConstants.ALL_PRICES_AVERAGE);
//    }
//
//    @Test(timeout = 10000)
//    public void spinnerTest_spinnerFunctionality_averageGamePrices_showsCorrectAnswer() {
//        checkIfDialogContainsText(SteamGameAppConstants.AVERAGE_GAME_PRICES_SPINNER_TEXT, SteamGameAppConstants.ALL_PRICES_AVERAGE + "17.8");
//    }
//
//    @Test(timeout = 10000)
//    public void spinnerTest_spinnerFunctionality_uniqueGamesCount_dialogDisplayed() {
//        checkDialogDisplayed(SteamGameAppConstants.UNIQUE_GAMES_SPINNER_TEXT);
//    }
//
//    @Test(timeout = 10000)
//    public void spinnerTest_spinnerFunctionality_uniqueGamesCount_showsAnyAnswer() {
//        checkIfDialogContainsText(SteamGameAppConstants.UNIQUE_GAMES_SPINNER_TEXT, SteamGameAppConstants.UNIQUE_GAMES_COUNT);
//    }
//
//    @Test(timeout = 10000)
//    public void spinnerTest_spinnerFunctionality_uniqueGamesCount_showsCorrectAnswer() {
//        checkIfDialogContainsText(SteamGameAppConstants.UNIQUE_GAMES_SPINNER_TEXT, SteamGameAppConstants.UNIQUE_GAMES_COUNT + "499");
//    }
//
//    @Test(timeout = 10000)
//    public void spinnerTest_spinnerFunctionality_mostExpensiveGames_dialogDisplayed() {
//        checkDialogDisplayed(SteamGameAppConstants.MOST_EXPENSIVE_GAMES_SPINNER_TEXT);
//    }
//
//    @Test(timeout = 10000)
//    public void spinnerTest_spinnerFunctionality_mostExpensiveGames_showsAnyAnswer() {
//        checkIfDialogContainsText(SteamGameAppConstants.MOST_EXPENSIVE_GAMES_SPINNER_TEXT, SteamGameAppConstants.MOST_EXPENSIVE_GAMES);
//    }
//
//    @Test(timeout = 10000)
//    public void spinnerTest_spinnerFunctionality_mostExpensiveGames_showsCorrectAnswer() {
//        onView(withId(R.id.chooseReport)).perform(click());
//        onData(hasToString(startsWith(SteamGameAppConstants.MOST_EXPENSIVE_GAMES_SPINNER_TEXT))).perform(click());
//
//        List<Game> top5Expensive = DataHelper.getTop5Games();
//
//        onView(withText(containsString(top5Expensive.get(0).toString()))).inRoot(isDialog()).check(matches(isDisplayed()));
//        onView(withText(containsString(top5Expensive.get(1).toString()))).inRoot(isDialog()).check(matches(isDisplayed()));
//        onView(withText(containsString(top5Expensive.get(2).toString()))).inRoot(isDialog()).check(matches(isDisplayed()));
//    }

    @Test(timeout = 10000)
    public void searchTest_dialogOpens() {
        onView(withId(R.id.search)).perform(click());
        onView(withText(SteamGameAppConstants.ENTER_SEARCH_TERM)).inRoot(isDialog()).check(matches(isDisplayed()));
    }

    @Test(timeout = 10000)
    public void searchTest_canEnterSearchTerm() {
        onView(withId(R.id.search)).perform(click());
        onView(withId(R.id.dialog_search_field)).perform(typeText("test"));
        onView(withId(R.id.dialog_search_field)).check(matches(withText("test")));
    }

    @Test(timeout = 10000)
    public void searchTest_canClickSearch() {
        onView(withId(R.id.search)).perform(click());
        onView(withId(R.id.dialog_search_field)).perform(typeText("counter"));
        onView(withId(android.R.id.button1)).perform(click());
    }

    @Test(timeout = 10000)
    public void searchTest_returnsCorrectCountValid() {
        onView(withId(R.id.search)).perform(click());
        onView(withId(R.id.dialog_search_field)).perform(typeText("counter"));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.gamesList)).check(ViewAssertions.matches(withListSize(2)));
    }

    @Test(timeout = 10000)
    public void searchTest_returnsCorrectCountInvalid() {
        onView(withId(R.id.search)).perform(click());
        onView(withId(R.id.dialog_search_field)).perform(typeText("test"));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.gamesList)).check(ViewAssertions.matches(withListSize(0)));
    }

    @Test(timeout = 10000)
    public void searchTest_returnsCorrectResult() {
        onView(withId(R.id.search)).perform(click());
        onView(withId(R.id.dialog_search_field)).perform(typeText("counter"));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.gamesList)).check(ViewAssertions.matches(correctData(DataHelper.getCounterGames())));
    }

    @Test(timeout = 10000)
    public void searchTest_canCancelClick() {
        onView(withId(R.id.search)).perform(click());
        onView(withId(android.R.id.button2)).perform(click());
    }

    @Test(timeout = 10000)
    public void searchTest_cancelClickListSize() {
        onView(withId(R.id.search)).perform(click());
        onView(withId(R.id.dialog_search_field)).perform(typeText("counter"));
        onView(withId(android.R.id.button2)).perform(click());
        onView(withId(R.id.gamesList)).check(ViewAssertions.matches(withListSize(500)));
    }

    @Test(timeout = 10000)
    public void searchTest_resetSearchListSize() {
        onView(withId(R.id.search)).perform(click());
        onView(withId(R.id.dialog_search_field)).perform(typeText("counter"));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.search)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.gamesList)).check(ViewAssertions.matches(withListSize(500)));
    }

    @Test(timeout = 10000)
    public void searchTest_resetSearchListCorrectList() {
        onView(withId(R.id.search)).perform(click());
        onView(withId(R.id.dialog_search_field)).perform(typeText("counter"));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.search)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.gamesList)).check(ViewAssertions.matches(correctData(DataHelper.getAllGames())));
    }

    @Test(timeout = 10000)
    public void addGameTest_dialogOpens() {
        onView(withId(R.id.addGame)).perform(click());
        onView(withText(SteamGameAppConstants.NEW_GAME_DIALOG_TITLE)).inRoot(isDialog()).check(matches(isDisplayed()));
    }

    @Test(timeout = 10000)
    public void addGameTest_canSetTextFields() {
        onView(withId(R.id.addGame)).perform(click());
        onView(withText(SteamGameAppConstants.NEW_GAME_DIALOG_TITLE)).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withId(R.id.dialog_name_field)).perform(typeText("testGame"));
        onView(withId(R.id.dialog_date_field)).perform(typeText("12.12.2018"));
        onView(withId(R.id.dialog_price_field)).perform(typeText("13.37"));
    }

    @Test(timeout = 10000)
    public void addGameTest_addGameClick() {
        addGame();
    }

    @Test(timeout = 10000)
    public void addGameTest_listLonger() {
        addGame();
        onView(withId(R.id.gamesList)).check(ViewAssertions.matches(withListSize(501)));
    }

    @Test(timeout = 10000)
    public void addGameTest_correctAdd() throws ParseException {
        addGame();
        List<Game> expected = DataHelper.getAllGames();
        Game game_1 = createSearchGame();
        expected.add(game_1);

        onView(withId(R.id.gamesList)).check(ViewAssertions.matches(correctData(expected)));
    }

    @Test(timeout = 10000)
    public void addGameTest_canSearchForAddedGame_size() {
        addGame();
        onView(withId(R.id.search)).perform(click());
        onView(withId(R.id.dialog_search_field)).perform(typeText("testGame"));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.gamesList)).check(ViewAssertions.matches(withListSize(1)));
    }

    @Test(timeout = 10000)
    public void addGameTest_canSearchForAddedGame_correct() throws ParseException {
        addGame();
        onView(withId(R.id.search)).perform(click());
        onView(withId(R.id.dialog_search_field)).perform(typeText("testGame"));
        onView(withId(android.R.id.button1)).perform(click());

        List<Game> expected = new ArrayList<>();
        expected.add(createSearchGame());

        onView(withId(R.id.gamesList)).check(ViewAssertions.matches(correctData(expected)));
    }

    @Test(timeout = 10000)
    public void saveTest_writesSomething() {
        File file = deleteFileIfExists();
        onView(withId(R.id.save)).perform(click());
        assertTrue(file.exists());
    }

    @Test(timeout = 10000)
    public void saveTest_writesCorrect() throws IOException {
        File file = deleteFileIfExists();
        onView(withId(R.id.save)).perform(click());
        assertTrue(file.exists());

        String allData = GameDataCsvString.ALL_DATA;

        int lineCount = checkFile(file, allData, false);

        assertEquals(500, lineCount);
    }


    @Test(timeout = 10000)
    public void saveTest_addGame_size() throws IOException {
        File file = deleteFileIfExists();
        addGame();
        onView(withId(R.id.save)).perform(click());
        assertTrue(file.exists());
        String allData = GameDataCsvString.ALL_DATA;
        int lineCount = checkFile(file, allData, true);
        assertEquals(501, lineCount);
    }

    @Test(timeout = 10000)
    public void saveTest_addGame_correct() throws IOException {
        File file = deleteFileIfExists();
        addGame();
        onView(withId(R.id.save)).perform(click());
        assertTrue(file.exists());
        String allData = GameDataCsvString.ALL_DATA;
        allData += "testGame;12.12.2018;13.37\n";
        int lineCount = checkFile(file, allData, false);
        assertEquals(501, lineCount);
    }

    private int checkFile(File file, String allData, boolean onlyLineCount) throws IOException {
        String cLine;
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        int lineCount = 0;

        while((cLine = br.readLine()) != null) {
            String searchLine = cLine.replace("\n", "");

            if(searchLine.length() == 0) {
                continue;
            }

            if(!onlyLineCount)
                assertTrue(allData.contains(searchLine));
            lineCount++;
        }
        return lineCount;
    }

    private File deleteFileIfExists() {
        File file = new File(myActivityRule.getActivity().getFilesDir(), SteamGameAppConstants.SAVE_GAMES_FILENAME);
        if (file.exists()) {
            file.delete();
        }
        return file;
    }


    private void addGame() {
        onView(withId(R.id.addGame)).perform(click());
        onView(withText(SteamGameAppConstants.NEW_GAME_DIALOG_TITLE)).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withId(R.id.dialog_name_field)).perform(typeText("testGame"));
        onView(withId(R.id.dialog_date_field)).perform(typeText("12.12.2018"));
        onView(withId(R.id.dialog_price_field)).perform(typeText("13.37"));
        onView(withId(android.R.id.button1)).perform(click());
    }


    private Game createSearchGame() throws ParseException {
        Game game_1 = new Game();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        game_1.setName("testGame");
        game_1.setReleaseDate(sdf.parse("12.12.2018"));
        game_1.setPrice(13.37d);
        return game_1;
    }

    public Matcher<View> correctData(List<Game> expected) {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                ListView listView =  ((ListView) item);

                List<Game> gamesInList = new ArrayList<>();

                for(int i = 0; i < listView.getCount(); i++) {
                    gamesInList.add((Game) listView.getItemAtPosition(i));
                }


                return DataHelper.assertEqualsListBoolean(expected, gamesInList);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText ("Given list does not contain expected results");
            }
        };
    }

    public Matcher<View> withListSize(int listSize) {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                return ((ListView) item).getCount() == listSize;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText ("ListView should have " + listSize + " items");
            }
        };
    }

    private void checkDialogDisplayed(String dialogHeader) {
        onView(withId(R.id.chooseReport)).perform(click());
        onData(hasToString(startsWith(dialogHeader))).perform(click());
        onView(withText(dialogHeader)).inRoot(isDialog()).check(matches(isDisplayed()));
    }

    private void checkIfDialogContainsText(String dialogHeader, String text) {
        onView(withId(R.id.chooseReport)).perform(click());
        onData(hasToString(startsWith(dialogHeader))).perform(click());
        onView(withText(containsString(text))).inRoot(isDialog()).check(matches(isDisplayed()));
    }*/
}
