package ekol.kartoteks.domain.exchange;

/**
 * Created by kilimci on 27/05/16.
 */
public class SectorExchangeData {

    private String code;
    private boolean isDefault;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
