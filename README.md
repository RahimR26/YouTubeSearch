# YouTube Search Test Automation Framework 

This repository contains a test automation framework for testing YouTube search functionality. The framework is built using Playwright and Java.

## Test Cases

### FYI: Sometimes when running the test case for the first time, if it takes too long for the query to type into the search box, try re-running the test case.

### 1. SearchCorrection Test

In this test, an intentionally incorrect word is provided for search, and the framework checks if YouTube provides a correct suggestion after the search. It will print the correction by youtube as a result.

```java
resultsPage.checkCorrection();
```

### 2. SearchFilterDuration Test

In this test, We are checking to see if the durations of the videos correspond with the 4-20 minute duration filter from youtube. It prints the max and min seconds of duration we can have followed by duration of the videos.

```java
resultsPage.assertEachResultHasDurationBetween(4, 20);
```

We retrieve the duration from each video then convert it to seconds.

```java
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
```

### 3. SearchFilterDurationFail Test

In this test, We are checking to see if the durations of the videos correspond with the 4-20 minute duration WITHOUT the filter from YouTube by using an if statement instead of an assertion. The reason why I used if instead of assert was due to the fact that with Java in playwright it apparently only allows for hard assertions which halt the process after the first iteration.

```java
resultsPage.assertEachResultHasDurationBetweenFail(4, 20);
```


### 4. SearchTestFail Test

Fail Scenario where we are trying to look for the music playlist when searching for music.
It is failing due to the fact that the selector is incorrect "#contents > ytd-video-renderer:nth-child(2)"
Where as the correct selector will be "#contents > ytd-radio-renderer:nth-child(2)"
Pretty similar and both in main contents but yet a small difference of one word.

```java
resultsPage.lookForPlaylistFail(userInput);
```


### 5. SearchTestPass Test

This case just checks to see if the word BMW is present on the page once we give a search.
I did have it printing the text of each video just to make sure It was working correctly
We can easily get rid of that by commenting out the print statement in YoutubeSearchResultsPage. For certain videos it will say that the first element is not found I couldnt figure out why BUT it will also say that followed by "Search result # is a sponsor. Skipping...
" and skips to the next video. This is due to the YouTube structure where sponsors and videos are in the same content page and selectors for sponsors take up an index value of the videos they are in place of.

```java
resultsPage.assertEachResultContainsText(userInput);
```

## Test Page Classes

### 1. YoutubeHomePage

Contains our commonly used actions for the framework.

### 2. YouTubeSearchResultsPage 

Contains our implementation of the logic for our test cases as well as the assertions and checks for our results.

## How to run the tests:

### 1. Clone the repository
### 2. Open the project in preferred Java IDE
### 3. Run the desired test from Test package

# Rahim Rustam