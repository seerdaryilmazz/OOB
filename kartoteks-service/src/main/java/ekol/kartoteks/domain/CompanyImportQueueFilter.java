package ekol.kartoteks.domain;

import ekol.kartoteks.utils.DateUtils;

import java.time.LocalDate;

/**
 * Created by fatmaozyildirim on 4/13/16.
 */
public class CompanyImportQueueFilter {
    private ImportQueueStatus status;
    private Long applicationId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String companyName;
    private String customerCompanyCode;
    private String orderCode;
    private String type;
    private int page;
    private int size;


    public CompanyImportQueueFilter() {
        //Default constructor
    }

    public static CompanyImportQueueFilter withStatus(ImportQueueStatus status) {
        CompanyImportQueueFilter filter = new CompanyImportQueueFilter();
        return filter.status(status);
    }

    public CompanyImportQueueFilter status(ImportQueueStatus status){
        this.setStatus(status);
        return this;
    }

    public CompanyImportQueueFilter startDate(LocalDate startDate){
        this.setStartDate(startDate);
        return this;
    }
    public CompanyImportQueueFilter startDate(String startDate){
        this.setStartDate(DateUtils.parse(startDate));
        return this;
    }

    public CompanyImportQueueFilter endDate(LocalDate endDate){
        this.setEndDate(endDate);
        return this;
    }
    public CompanyImportQueueFilter endDate(String endDate){
        this.setEndDate(DateUtils.parse(endDate));
        return this;
    }

    public CompanyImportQueueFilter companyName(String companyName){
        this.setCompanyName(companyName);
        return this;
    }
    public CompanyImportQueueFilter customerCompanyCode(String customerCompanyCode){
        this.setCustomerCompanyCode(customerCompanyCode);
        return this;
    }
    public CompanyImportQueueFilter orderCode(String orderCode){
        this.setOrderCode(orderCode);
        return this;
    }
    public CompanyImportQueueFilter page(Integer page){
        this.setPage(page);
        return this;
    }

    public CompanyImportQueueFilter size(Integer size){
        this.setSize(size);
        return this;
    }

    public CompanyImportQueueFilter type(String type){
        this.setType(type);
        return this;
    }

    public ImportQueueStatus getStatus() {
        return status;
    }

    public void setStatus(ImportQueueStatus status) {
        this.status = status;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCustomerCompanyCode() {
        return customerCompanyCode;
    }

    public void setCustomerCompanyCode(String customerCompanyCode) {
        this.customerCompanyCode = customerCompanyCode;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
