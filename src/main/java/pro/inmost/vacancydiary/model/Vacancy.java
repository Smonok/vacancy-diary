package pro.inmost.vacancydiary.model;

import java.sql.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Vacancy {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column
    private String companyName;

    @Column
    private String position;

    @Column
    private int expectedSalary;

    @Column
    private String link;

    @Column
    private String recruiterContacts;

    @Column
    private String status;

    @Column
    private Date lastStatusChange;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Vacancy vacancy = (Vacancy) o;
        return id == vacancy.id &&
            expectedSalary == vacancy.expectedSalary &&
            Objects.equals(companyName, vacancy.companyName) &&
            Objects.equals(position, vacancy.position) &&
            Objects.equals(link, vacancy.link) &&
            Objects.equals(recruiterContacts, vacancy.recruiterContacts) &&
            Objects.equals(status, vacancy.status) &&
            Objects.equals(lastStatusChange, vacancy.lastStatusChange);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, companyName);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getExpectedSalary() {
        return expectedSalary;
    }

    public void setExpectedSalary(int expectedSalary) {
        this.expectedSalary = expectedSalary;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getrecruiterContacts() {
        return recruiterContacts;
    }

    public void setrecruiterContacts(String recruiterContacts) {
        this.recruiterContacts = recruiterContacts;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getlastStatusChange() {
        return lastStatusChange;
    }

    public void setlastStatusChange(Date lastStatusChange) {
        this.lastStatusChange = lastStatusChange;
    }

    public Vacancy() {
    }

    public Vacancy(String companyName, String position, int expectedSalary, String link,
        String recruiterContacts, String status, Date lastStatusChange) {
        this.companyName = companyName;
        this.position = position;
        this.expectedSalary = expectedSalary;
        this.link = link;
        this.recruiterContacts = recruiterContacts;
        this.status = status;
        this.lastStatusChange = lastStatusChange;
    }
}
