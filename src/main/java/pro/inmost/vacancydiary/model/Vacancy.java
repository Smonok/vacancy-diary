package pro.inmost.vacancydiary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.sql.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table
@DynamicUpdate
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

    @ManyToMany(mappedBy = "vacancies")
    @JsonIgnore
    private List<User> users;

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
