package com.example.help_m5.reviews;

public class ReviewItem {
    private String userName;
    private String userEmail;
    private String reportedUserEmail;
    private String userDate;
    private String userDescription;
    private double userRate;

    private int upVoteCount;
    private int downVoteCount;
    private String upVoteId;
    private String downVoteId;

    private String title;
    private int facilityId;
    private int facilityType;
    private boolean post;

    public ReviewItem(String userName, String userEmail, String reportedUserEmail, String userDate, String userDescription, double userRate, int upVoteCount, int downVoteCount, String upVoteId, String downVoteId, String title, int facilityId, int facilityType, boolean post) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.reportedUserEmail = reportedUserEmail;
        this.userDate = userDate;
        this.userDescription = userDescription;
        this.userRate = userRate;
        this.upVoteCount = upVoteCount;
        this.downVoteCount = downVoteCount;
        this.upVoteId = upVoteId;
        this.downVoteId = downVoteId;
        this.title = title;
        this.facilityId = facilityId;
        this.facilityType = facilityType;
        this.post = post;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getReportedUserEmail() {
        return reportedUserEmail;
    }

    public String getUserDate() {
        return userDate;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public double getUserRate() {
        return userRate;
    }

    public int getUpVoteCount() {
        return upVoteCount;
    }

    public int getDownVoteCount() {
        return downVoteCount;
    }

    public String getTitle() {
        return title;
    }

    public int getFacilityId() {
        return facilityId;
    }

    public int getFacilityType() {
        return facilityType;
    }

    public boolean isPost() {
        return post;
    }

    public String getUpVoteId() {
        return upVoteId;
    }

    public String getDownVoteId() {
        return downVoteId;
    }
}
