package com.tur.job1.models;



import java.io.Serializable;

public class LoginInformationResponse implements Serializable {

    private boolean userExist;
    private boolean userSobseeker;
    private JobSeekerModel jobSeekerModel;
    private CompanyModel companyModel;

    public boolean isUserExist() {
        return userExist;
    }

    public void setUserExist(boolean userExist) {
        this.userExist = userExist;
    }

    public JobSeekerModel getJobSeekerModel() {
        return jobSeekerModel;
    }

    public void setJobSeekerModel(JobSeekerModel jobSeekerModel) {
        this.jobSeekerModel = jobSeekerModel;
    }

    public boolean isUserSobseeker() {
        return userSobseeker;
    }

    public void setUserSobseeker(boolean userSobseeker) {
        this.userSobseeker = userSobseeker;
    }

    public CompanyModel getCompanyModel() {
        return companyModel;
    }

    public void setCompanyModel(CompanyModel companyModel) {
        this.companyModel = companyModel;
    }
}
