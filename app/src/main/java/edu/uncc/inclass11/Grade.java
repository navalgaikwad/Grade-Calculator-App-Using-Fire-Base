package edu.uncc.inclass11;

public class Grade {

    String grade;
    String creditHr;
    int gradePoints;
    double gpa;
    int gradePointTotal;
    int totalhr;
    String courseNo;
    String courseName;
    String courseId;
    String created_by_uid;

    public String getCreated_by_uid() {
        return created_by_uid;
    }

    public void setCreated_by_uid(String created_by_uid) {
        this.created_by_uid = created_by_uid;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseNo() {
        return courseNo;
    }

    public void setCourseNo(String courseNo) {
        this.courseNo = courseNo;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getGrade() {

        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getCreditHr() {
        return creditHr;
    }

    public void setCreditHr(String creditHr) {
        this.creditHr = creditHr;
    }

    public int getGradePoints() {
        return gradePoints;
    }

    public void setGradePoints(int gradePoints) {
        this.gradePoints = gradePoints;
    }

    public double getGpa() {

        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public int getGradePointTotal() {
        if (creditHr!=null && grade!=null ) {

            gradePointTotal = Integer.valueOf((int) (gradePointTotal + (Double.parseDouble(creditHr) * setGradeTotal(grade))));
        }
        return gradePointTotal;
    }

    public void setGradePointTotal(int gradePointTotal) {
        this.gradePointTotal = gradePointTotal;
    }

    public int getTotalhr() {
        if (creditHr!=null && !creditHr.isEmpty()) {
            totalhr = Integer.valueOf((int) (totalhr + Double.parseDouble(creditHr)));
        }
        return totalhr;
    }

    public void setTotalhr(int totalhr) {
        this.totalhr = totalhr;
    }

  int  setGradeTotal(String grade){
        if (grade.equals("A")){
            return 4;
        }else if (grade.equals("B")){
            return 3;
        }else if (grade.equals("C")){
            return 2;
        }else if (grade.equals("D")){
            return 1;
        }else{
            return 0;
        }
    }

}
