package ekol.currency.domain.dto;

import java.util.Comparator;

public class MissingDay {
    
    public static final Comparator<MissingDay> SORTER = new Comparator<MissingDay>() {
        @Override
        public int compare(MissingDay o1, MissingDay o2) {
            int firstResult = o1.getPublisher().compareTo(o2.getPublisher());
            if (firstResult == 0) {
                return o1.getPublishDate().compareTo(o2.getPublishDate());
            } else {
                return firstResult;
            }
        }
    };

    private String publisher;

    private String publishDate;

    public MissingDay() {
    }

    public MissingDay(String publisher, String publishDate) {
        this.publisher = publisher;
        this.publishDate = publishDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }
}
