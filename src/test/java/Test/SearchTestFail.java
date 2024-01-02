package Test;
import YouTubePages.YouTubeHomePage;
import YouTubePages.YouTubeSearchResultsPage;
import com.microsoft.playwright.*;


/* Fail Scenario where we are trying to look for the music playlist when searching for music.
   It is failing due to the fact that the selector is incorrect "#contents > ytd-video-renderer:nth-child(2)"
   Where as the correct selector will be "#contents > ytd-radio-renderer:nth-child(2)"
   Pretty similar and both in main contents but yet a small difference of one word.*/


public class SearchTestFail {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            //BrowserContext context = browser.newContext();
            Page page = browser.newPage();
            String userInput = "Music";

            // Home Page
            YouTubeHomePage homePage = new YouTubeHomePage(page);
            homePage.navigateToYouTube();
            homePage.searchForQuery(userInput);



            // Search Results Page
            YouTubeSearchResultsPage resultsPage = new YouTubeSearchResultsPage(page);
            resultsPage.waitForSearchResults();
            //resultsPage.scrollIntoView("div#contents");



            // Validate search results on the Videos page
            resultsPage.assertResultsDisplayed();

            //Running our method to fail
            resultsPage.lookForPlaylistFail(userInput);

            // Close the browser
            browser.close();
        } catch (Exception e) {
            // Handle exceptions
            e.printStackTrace();
        }
    }
}
