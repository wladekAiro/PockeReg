package com.example.wladek.pockeregapp.pojo;

import com.example.wladek.pockeregapp.enums.SchoolStatus;

/**
 * Created by wladek on 8/21/16.
 */
public class School {
    private Long id;
    private String schoolName;
    private String schoolCode;
    private SchoolStatus status;

    public School(){
    }

    public Long getId() {
        return id;
    }

    public SchoolStatus getStatus() {
        return status;
    }

    public void setStatus(SchoolStatus status) {
        this.status = status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolCode() {
        return schoolCode;
    }

    public void setSchoolCode(String schoolCode) {
        this.schoolCode = schoolCode;
    }
}
