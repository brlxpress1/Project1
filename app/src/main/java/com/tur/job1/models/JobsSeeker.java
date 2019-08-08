package com.tur.job1.models;



import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;


public class JobsSeeker implements Serializable {


    private Integer id;


    private String fullName;


    private boolean visbile;


    private String gender;


    private String email;


    private String phone;


    private String expectedSalary;


    private String experience;


    private String preferLocation;


    private LocalDate dateOfBirth;


    private String photoUrl;


    private String cvUrl;


    private Set<Skills> skillsList;


    private Set<JobsHistory> jobsHistories;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isVisbile() {
        return visbile;
    }

    public void setVisbile(boolean visbile) {
        this.visbile = visbile;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getExpectedSalary() {
        return expectedSalary;
    }

    public void setExpectedSalary(String expectedSalary) {
        this.expectedSalary = expectedSalary;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getPreferLocation() {
        return preferLocation;
    }

    public void setPreferLocation(String preferLocation) {
        this.preferLocation = preferLocation;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getCvUrl() {
        return cvUrl;
    }

    public void setCvUrl(String cvUrl) {
        this.cvUrl = cvUrl;
    }

    public Set<Skills> getSkillsList() {
        return skillsList;
    }

    public void setSkillsList(Set<Skills> skillsList) {
        this.skillsList = skillsList;
    }

    public Set<JobsHistory> getJobsHistories() {
        return jobsHistories;
    }

    public void setJobsHistories(Set<JobsHistory> jobsHistories) {
        this.jobsHistories = jobsHistories;
    }
}
