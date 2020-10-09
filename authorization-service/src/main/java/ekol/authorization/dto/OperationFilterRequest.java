package ekol.authorization.dto;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by kilimci on 27/04/2017.
 */
public class OperationFilterRequest {

    private String name;
    private String description;
    private String startDate;
    private String endDate;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public String getName() {
        return name;
    }

    public String getNameStartsWith() {
        return name.toLowerCase() + "%";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public String getDescriptionLike() {
        return "%" + description.toLowerCase() + "%";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public LocalDate getStartDateAsLocalDate() {
        if(StringUtils.isNotBlank(startDate)){
            return LocalDate.parse(startDate, DATE_FORMATTER);
        }
        return null;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public LocalDate getEndDateAsLocalDate() {
        if(StringUtils.isNotBlank(endDate)){
            return LocalDate.parse(endDate, DATE_FORMATTER);
        }
        return null;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public static final class OperationFilterRequestBuilder {
        private String name;
        private String description;
        private String startDate;
        private String endDate;

        private OperationFilterRequestBuilder() {
        }

        public static OperationFilterRequestBuilder anOperationFilterRequest() {
            return new OperationFilterRequestBuilder();
        }

        public OperationFilterRequestBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public OperationFilterRequestBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public OperationFilterRequestBuilder withStartDate(String startDate) {
            this.startDate = startDate;
            return this;
        }

        public OperationFilterRequestBuilder withEndDate(String endDate) {
            this.endDate = endDate;
            return this;
        }

        public OperationFilterRequest build() {
            OperationFilterRequest operationFilterRequest = new OperationFilterRequest();
            operationFilterRequest.setName(name);
            operationFilterRequest.setDescription(description);
            operationFilterRequest.setStartDate(startDate);
            operationFilterRequest.setEndDate(endDate);
            return operationFilterRequest;
        }
    }
}
