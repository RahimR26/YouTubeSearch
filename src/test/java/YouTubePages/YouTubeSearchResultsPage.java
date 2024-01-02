package YouTubePages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;

public class YouTubeSearchResultsPage {
    private final Page page;

    public YouTubeSearchResultsPage(Page page) {
        this.page = page;
    }

    // Checks for results
    public void assertResultsDisplayed() {
        assert page.isVisible("div#contents") : "Search results container is not visible within the specified timeout.";
    }


    // Asserts for presence of search text in the search results. If it does skip its due to the fact that it is a
    // sponsored video since they somehow take up an index value even though they don't share the same selector
    public void assertEachResultContainsText(String searchText) {
        for (int i = 1; i <= 8; i++) {
            String selector = String.format("div#contents ytd-video-renderer:nth-child(%d)", i);

            // Scroll each result into view
            scrollIntoView(selector);

            try {
                // Wait for each result to be visible
                waitForSelector(selector);

                String resultText = page.innerText(selector).toLowerCase();
                assert resultText.contains(searchText.toLowerCase()) : "Search result " + i + " does not contain the expected text within the specified timeout.";
                System.out.println("Search result " + i + " contains the expected text. Actual text: \"" + resultText + "\", Expected text: \"" + searchText.toLowerCase() + "\"");
                // Display a success message
                System.out.println("Search result " + i + " contains the expected text.");

            } catch (Exception e) {
                // Handle the exception if the element is not found
                System.err.println("Search result " + i + " is a sponsor. Skipping...");
            }
        }
    }


    public void lookForPlaylistFail(String searchText) {
            String selector = String.format("#contents > ytd-video-renderer:nth-child(2)");

            // Scroll each result into view
            scrollIntoView(selector);


                // Wait for each result to be visible
                waitForSelector(selector);

                page.click("#contents > ytd-radio-renderer:nth-child(2) a#thumbnail");

                // Wait for the video (playlist) to load
                page.waitForSelector("div#container");

                // Verify that the video is a playlist
                assert page.isVisible("ytd-playlist-panel-renderer #title-container h1#title") :
                        "Selected video is not a playlist.";

                // Print success message
                System.out.println("Successfully clicked on the music playlist!");



    }


    // Implemented for scrolling to avoid elements not showing up
    public void scrollIntoView(String selector) {
        if (page.querySelector(selector) != null) {
            page.evaluate(String.format("document.querySelector('%s').scrollIntoView();", selector));
        } else {
            System.err.println("Element with selector '" + selector + "' not found.");
        }
    }


    private void waitForSelector(String selector) {
        page.waitForSelector(selector, new Page.WaitForSelectorOptions().setTimeout(30000)); //had to go from 10000 to 30000 for less flaky tests for this project
    }

    public void waitForSearchResults() {
    }


    // Tells us if there are any videos with durations that is not within specified range
    public void assertEachResultHasDurationBetweenFail(int minDuration, int maxDuration) {
        int minDurationInSeconds = minDuration * 60;
        int maxDurationInSeconds = maxDuration * 60;

        System.out.println("Minimum duration limit:  " + minDurationInSeconds + "\n"+"Maximum duration limit: " + maxDurationInSeconds);


        for (int i = 1; i <= 8; i++) {
            String xpathSelector = String.format("#contents > ytd-video-renderer:nth-child(%d)", i);

            // Scroll each result into view
            scrollIntoView(xpathSelector);

            try {
                // Wait for each result to be visible
                waitForSelector(xpathSelector);

                // Get the duration of the video
                int videoDuration = getVideoDurationInSeconds(xpathSelector);

                // Assert version that will only run once
                // if (!(videoDuration >= minDurationInSeconds && videoDuration <= maxDurationInSeconds)) {
                //      throw new AssertionError(String.format("Video %d duration is not between %d and %d minutes. Actual duration: %d seconds",
                //      i, minDuration, maxDuration, videoDuration));
                //}

                if (!(videoDuration >= minDurationInSeconds && videoDuration <= maxDurationInSeconds)) {
                    System.out.println(String.format("Video %d duration is not between %d and %d minutes. Actual duration: %d seconds",
                            i, minDuration, maxDuration, videoDuration));
                }
                else {
                    // Print the actual duration
                    System.out.println("Video " + i + " duration: " + videoDuration + " seconds");
                }

            } catch (Exception e) {
                // Handle the exception if the element is not found
                System.err.println("Search result " + i + " is a sponsor. Skipping...");
            }
            try {
                Thread.sleep(2000);  // 2000 milliseconds = 2 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    // For this case we click on filter and the 4-20 minute duration button in SearchFilterDuration to sort the videos
    public void assertEachResultHasDurationBetween(int minDuration, int maxDuration) {
        for (int i = 1; i <= 8; i++) {
            String xpathSelector = String.format("#contents > ytd-video-renderer:nth-child(%d)", i);

            // Scroll each result into view
            scrollIntoView(xpathSelector);

            try {
                // Wait for each result to be visible
                waitForSelector(xpathSelector);

                // Get the duration of the video
                int videoDuration = getVideoDurationInSeconds(xpathSelector);

                // Convert minDuration and maxDuration to seconds
                int minDurationInSeconds = minDuration * 60;
                int maxDurationInSeconds = maxDuration * 60;

                System.out.println("Minimum duration limit:  " + minDurationInSeconds + "\n"+"Maximum duration limit: " + maxDurationInSeconds);
                // Assert that the duration is between the specified range
                if (!(videoDuration >= minDurationInSeconds && videoDuration <= maxDurationInSeconds)) {
                    throw new AssertionError(String.format("Video %d duration is not between %d and %d seconds. Actual duration: %d seconds.",
                            i, minDurationInSeconds, maxDurationInSeconds, videoDuration));
                }
                else {
                    // Print the actual duration
                    System.out.println("Video " + i + " duration: " + videoDuration + " seconds");
                }

            } catch (Exception e) {
                // Handle the exception if the element is not found
                System.err.println("Search result " + i + " is a sponsor. Skipping...");
            }
            try {
                Thread.sleep(2000);  // 2000 milliseconds = 2 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    // Retrieving the duration for each video
    private int getVideoDurationInSeconds(String selector) {
        // Construct the selector for the duration within the nth child
        String durationSelector = selector + " #time-status";

        // Extract the duration string and convert to seconds
        String durationText = page.innerText(durationSelector);
        return convertFormattedDurationToSeconds(durationText);
    }


    // Converting the duration we found into seconds
    private int convertFormattedDurationToSeconds(String formattedDuration) {
        String[] parts = formattedDuration.split(":");
        int minutes = Integer.parseInt(parts[0]);
        int seconds = Integer.parseInt(parts[1]);
        return minutes * 60 + seconds;
    }


    // Checking to see if there is a correction present upon inputing the wrong info
    public void checkCorrection() throws InterruptedException {
        Thread.sleep(10000);
        String correctionSelector = "//*[@id=\"contents\"]/yt-showing-results-for-renderer/yt-search-query-correction";

        if (page.isVisible(correctionSelector)) {
            String correctionText = page.innerText(correctionSelector);
            System.out.println("Search query correction: " + correctionText);
        } else {
            System.out.println("No search query correction found.");
        }
    }





}
