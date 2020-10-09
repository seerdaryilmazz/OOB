package ekol.crm.search.service;

import ekol.crm.search.domain.dto.CustomPageImpl;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CompanySearchIterator implements Iterator<Long> {

    private CompanyService companyService;
    private String city;
    private String district;
    private boolean searchOnlyInDefaultLocations;
    private int pageSize;
    private int nextPageNumber;
    private Iterator<Long> internalIterator;
    private boolean noMorePage;

    public CompanySearchIterator(CompanyService companyService, String city, String district, boolean searchOnlyInDefaultLocations, int pageSize) {
        this.companyService = companyService;
        this.city = city;
        this.district = district;
        this.searchOnlyInDefaultLocations = searchOnlyInDefaultLocations;
        this.pageSize = pageSize;
        this.internalIterator = Collections.emptyIterator();
    }

    private void loadNextPage() {
        if (!noMorePage) {
            CustomPageImpl<Long> page = companyService.searchCompanyIdsByCityAndDistrict(city, district, searchOnlyInDefaultLocations, pageSize, nextPageNumber);
            nextPageNumber++;
            List<Long> pageContent = page.getContent();
            if (CollectionUtils.isNotEmpty(pageContent)) {
                internalIterator = pageContent.iterator();
            } else {
                internalIterator = Collections.emptyIterator();
                noMorePage = true;
            }
        }
    }

    @Override
    public boolean hasNext() {
        if (internalIterator.hasNext()) {
            return true;
        } else {
            loadNextPage();
            return internalIterator.hasNext();
        }
    }

    @Override
    public Long next() {
        if (internalIterator.hasNext()) {
            return internalIterator.next();
        } else {
            loadNextPage();
            return internalIterator.next();
        }
    }
}
