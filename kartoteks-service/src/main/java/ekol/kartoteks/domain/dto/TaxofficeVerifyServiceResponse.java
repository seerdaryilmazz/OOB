package ekol.kartoteks.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TaxofficeVerifyServiceResponse {

    /**
     * "success" veya "error"
     */
    private String status;
    private String errorMessage;
    private Boolean errorCausedByInvalidInput;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Boolean getErrorCausedByInvalidInput() {
        return errorCausedByInvalidInput;
    }

    public void setErrorCausedByInvalidInput(Boolean errorCausedByInvalidInput) {
        this.errorCausedByInvalidInput = errorCausedByInvalidInput;
    }
}
