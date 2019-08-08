package com.tur.job1.models;



import java.io.Serializable;


public class Skills implements Serializable {


    private Integer id;


    private Integer skillId;




    private JobsSeeker jobSeekerId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSkillId() {
        return skillId;
    }

    public void setSkillId(Integer skillId) {
        this.skillId = skillId;
    }

    public JobsSeeker getJobSeekerId() {
        return jobSeekerId;
    }

    public void setJobSeekerId(JobsSeeker jobSeekerId) {
        this.jobSeekerId = jobSeekerId;
    }
}
