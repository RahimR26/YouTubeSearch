package Test;
import YouTubePages.YouTubeHomePage;
import YouTubePages.YouTubeSearchResultsPage;
import com.microsoft.playwright.*;

// This test case passes due to the fact that we now have clicked the YouTube's own filter and sort function using the duration of 4-20 minutes

public class SearchFilterDuration {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            //BrowserContext context = browser.newContext();
            Page page = browser.newPage();

            // Home Page
            YouTubeHomePage homePage = new YouTubeHomePage(page);
            homePage.navigateToYouTube();
            homePage.searchForQuery("BMW");


            // Click the "Filter" button
            homePage.clickFilterButton();

            //choose duration of 4-20 minutes
            homePage.selectDurationFilter("4-20 minutes");


            // Search Results Page
            YouTubeSearchResultsPage resultsPage = new YouTubeSearchResultsPage(page);
            resultsPage.waitForSearchResults();
            //resultsPage.scrollIntoView("div#contents");



            // Validate search results on the Videos page
            resultsPage.assertResultsDisplayed();
            //resultsPage.assertEachResultContainsText("Playwright");
            resultsPage.assertEachResultHasDurationBetween(4, 20);


            // Close the browser
            //browser.close();
        } catch (Exception e) {
            // Handle exceptions
            e.printStackTrace();
        }
    }
}
