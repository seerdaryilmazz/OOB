package ekol.currency.domain.dto;

import java.util.List;

// TODO: Bunun yerine org.springframework.data.domain.Slice kullansak daha iyi.
public class PagedDataHolder<T> {

    List<T> currentPageContent;

    long numberOfElements;

    public PagedDataHolder(List<T> currentPageContent, long numberOfElements) {
        this.currentPageContent = currentPageContent;
        this.numberOfElements = numberOfElements;
    }

    public List<T> getCurrentPageContent() {
        return currentPageContent;
    }

    public void setCurrentPageContent(List<T> currentPageContent) {
        this.currentPageContent = currentPageContent;
    }

    public long getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(long numberOfElements) {
        this.numberOfElements = numberOfElements;
    }
}
