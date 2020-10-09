package ekol.crm.account.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HomePageStatistics {

    private Long totalCount;

    private List<AccountTypeBasedCount> accountTypeBasedCounts;

    public HomePageStatistics() {
    }

    public HomePageStatistics(Long totalCount, List<AccountTypeBasedCount> accountTypeBasedCounts) {
        this.totalCount = totalCount;
        this.accountTypeBasedCounts = accountTypeBasedCounts;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public List<AccountTypeBasedCount> getAccountTypeBasedCounts() {
        return accountTypeBasedCounts;
    }

    public void setAccountTypeBasedCounts(List<AccountTypeBasedCount> accountTypeBasedCounts) {
        this.accountTypeBasedCounts = accountTypeBasedCounts;
    }
}
