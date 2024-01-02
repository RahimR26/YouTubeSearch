package Test;
import YouTubePages.YouTubeHomePage;
import YouTubePages.YouTubeSearchResultsPage;
import com.microsoft.playwright.*;

// In this test case we intentionally pass an incorrect word and check to see if YouTube gives us a correct suggestion after search

public class SearchCorrection {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            //BrowserContext context = browser.newContext();
            Page page = browser.newPage();
            String incorrectInput = "Podsche";

            // Home Page
            YouTubeHomePage homePage = new YouTubeHomePage(page);
            homePage.navigateToYouTube();
            homePage.searchForQuery(incorrectInput);


            // Search Results Page
            YouTubeSearchResultsPage resultsPage = new YouTubeSearchResultsPage(page);
            resultsPage.waitForSearchResults();


            // Validate search results on the Videos page
            resultsPage.assertResultsDisplayed();
            resultsPage.checkCorrection();

            // Close the browser
            //browser.close();
        } catch (Exception e) {
            // Handle exceptions
            e.printStackTrace();
        }
    }
}
