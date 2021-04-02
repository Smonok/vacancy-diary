package pro.inmost.vacancydiary.response.status;

public enum EmailStatus implements ResponseStatus {
    SUCCESS,
    NO_SUCH_VACANCIES_FOR_USER,
    NO_MATCHING_STATUS_CHANGE_DATES
}
