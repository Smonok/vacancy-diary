package pro.inmost.vacancydiary.mapper;

import java.util.List;
import java.util.Set;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import pro.inmost.vacancydiary.model.Vacancy;
import pro.inmost.vacancydiary.model.dto.VacancyDto;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface VacancyMapper {
    VacancyDto map(Vacancy vacancy);

    Vacancy map(VacancyDto vacancy);

    List<VacancyDto> map(List<Vacancy> vacancies);

    Set<VacancyDto> map(Set<Vacancy> vacancies);

    Page<VacancyDto> map(Page<Vacancy> vacancies);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(VacancyDto vacancyDto, @MappingTarget Vacancy vacancy);
}
