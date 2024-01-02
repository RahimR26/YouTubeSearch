package Test;
import YouTubePages.YouTubeHomePage;
import YouTubePages.YouTubeSearchResultsPage;
import com.microsoft.playwright.*;

/* This case just checks to see if the word BMW is present on the page once we give a search.
   I did have it printing the text of each video just to make sure It was working correctly
   We can easily get rid of that by commenting out the print statement in YoutubeSearchResultsPage*/


public class SearchTestPass {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            //BrowserContext context = browser.newContext();
            Page page = browser.newPage();
            String userInput = "BMW";

            // Home Page
            YouTubeHomePage homePage = new YouTubeHomePage(page);
            homePage.navigateToYouTube();
            homePage.searchForQuery(userInput);

            // We can click the "Videos" button for a more accurate reading as well
            // homePage.clickVideosButton();


            // Search Results Page
            YouTubeSearchResultsPage resultsPage = new YouTubeSearchResultsPage(page);
            resultsPage.waitForSearchResults();


            // Validate search results on the Videos page
            resultsPage.assertResultsDisplayed();
            resultsPage.assertEachResultContainsText(userInput);

            // Close the browser
            browser.close();
        } catch (Exception e) {
            // Handle exceptions
            e.printStackTrace();
        }
    }
}
