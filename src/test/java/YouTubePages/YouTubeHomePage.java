package YouTubePages;

import com.microsoft.playwright.Page;


// POM page to keep some of the commonly used elements within this framework

public class YouTubeHomePage {
    private final Page page;

    public YouTubeHomePage(Page page) {
        this.page = page;
    }

    public void navigateToYouTube() {
        page.navigate("https://www.youtube.com");
    }

    public void searchForQuery(String query) {
        page.type("input[id='search']", query);
        page.press("input[id='search']", "Enter");
    }


    public void selectDurationFilter(String duration) {
        // Use a CSS selector for the duration filter option
        page.click("div[title='Search for 4 - 20 minutes']");
    }


    public void clickFilterButton() {
        // Use a CSS selector for the "Filter" button
        page.click("div[id='filter-button']");
    }


    public void clickVideosButton() {
        // Use a more specific selector if needed
        page.click("yt-formatted-string[title='Videos']");
    }

}
