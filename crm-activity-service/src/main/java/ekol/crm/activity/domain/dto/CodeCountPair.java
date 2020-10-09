package ekol.crm.activity.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CodeCountPair {

    /**
     * Bu sınıfı mongodb aggregation için kullanırken @Id annotation'ı önemli, bu annotation olmazsa hata oluşur.
     */
    @Id
    private String code;

    private Long count;

    public CodeCountPair() {
    }

    public CodeCountPair(String code, Long count) {
        this.code = code;
        this.count = count;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
