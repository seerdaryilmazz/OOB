package ekol.kartoteks.domain;

/**
 * Created by kilimci on 11/12/2017.
 */
public class CompanyExportQueueFilter {

    private ExportQueueStatus status;
    private Long companyId;
    private String applicationCompanyId;
    private int page;
    private int size;

    public static CompanyExportQueueFilter with(ExportQueueStatus status, Long companyId, String applicationCompanyId, Integer page, Integer size){
        CompanyExportQueueFilter filter = new CompanyExportQueueFilter();
        filter.setCompanyId(companyId);
        filter.setStatus(status);
        filter.setApplicationCompanyId(applicationCompanyId);
        filter.setPage(page != null ? page : 1);
        filter.setSize(size != null ? size : 20);
        return filter;
    }

    public ExportQueueStatus getStatus() {
        return status;
    }

    public void setStatus(ExportQueueStatus status) {
        this.status = status;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getApplicationCompanyId() {
        return applicationCompanyId;
    }

    public void setApplicationCompanyId(String applicationCompanyId) {
        this.applicationCompanyId = applicationCompanyId;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
