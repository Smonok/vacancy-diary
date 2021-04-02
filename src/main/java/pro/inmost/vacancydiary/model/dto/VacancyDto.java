package pro.inmost.vacancydiary.model.dto;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class VacancyDto {
    private long id;

    private String companyName;

    private String position;

    private Integer expectedSalary;

    private String link;

    private String recruiterContacts;

    private String status;

    private Timestamp lastStatusChange;
}
