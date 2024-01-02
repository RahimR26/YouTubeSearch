package Test;
import YouTubePages.YouTubeHomePage;
import YouTubePages.YouTubeSearchResultsPage;
import com.microsoft.playwright.*;

/* For this test case as you can see in YoutubeSearchResultsPage I have used an if statement.
That way it tells me if the test cases are over the duration limit or under.
If you look on the YoutubeSearchResultsPage for SearchFilterDurationFail you can see I did not use assert
specifically because it stops the execution after the first iteration due to the fact that first video is over the time limit.
Easy fix to this would be to filter videos using YouTubes own filter function where we click filter and then sort by duration 4-20 minutes
*/

public class SearchFilterDurationFail {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            //BrowserContext context = browser.newContext();
            Page page = browser.newPage();

            // Home Page
            YouTubeHomePage homePage = new YouTubeHomePage(page);
            homePage.navigateToYouTube();
            homePage.searchForQuery("BMW");


            // Click the "Videos" button
            homePage.clickVideosButton();

            // Search Results Page
            YouTubeSearchResultsPage resultsPage = new YouTubeSearchResultsPage(page);
            resultsPage.waitForSearchResults();
            //resultsPage.scrollIntoView("div#contents");



            // Validate search results on the Videos page
            resultsPage.assertResultsDisplayed();
            // Providing the limit for the duration
            resultsPage.assertEachResultHasDurationBetweenFail(4, 20);


            // Close the browser
            //browser.close();
        } catch (Exception e) {
            // Handle exceptions
            e.printStackTrace();
        }
    }
}
