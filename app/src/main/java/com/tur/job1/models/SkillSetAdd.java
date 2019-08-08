package com.tur.job1.models;

import java.util.List;

public class SkillSetAdd {

    private Integer userrId;
    private List<Integer> skillId;

    public Integer getUserrId() {
        return userrId;
    }

    public void setUserrId(Integer userrId) {
        this.userrId = userrId;
    }

    public List<Integer> getSkillId() {
        return skillId;
    }

    public void setSkillId(List<Integer> skillId) {
        this.skillId = skillId;
    }
}
