package ekol.kartoteks.utils;

import ekol.kartoteks.domain.*;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class ObjectUtils {

    public static final Comparator<ContactDepartment> DEPARTMENT_COMPARATOR = new Comparator<ContactDepartment>() {
        @Override
        public int compare(ContactDepartment o1, ContactDepartment o2) {
            return o1.getId().compareTo(o2.getId());
        }
    };

    public static final Comparator<ContactTitle> TITLE_COMPARATOR = new Comparator<ContactTitle>() {
        @Override
        public int compare(ContactTitle o1, ContactTitle o2) {
            return o1.getId().compareTo(o2.getId());
        }
    };

    public static final Comparator<Iterable<PhoneNumberWithType>> PHONE_NUMBERS_COMPARATOR = new Comparator<Iterable<PhoneNumberWithType>>() {
        @Override
        public int compare(Iterable<PhoneNumberWithType> o1, Iterable<PhoneNumberWithType> o2) {
            List<String> actualPhoneNumbers1 = new ArrayList<>();
            List<String> actualPhoneNumbers2 = new ArrayList<>();
            for (PhoneNumberWithType elem : o1) {
                actualPhoneNumbers1.add(elem.toString());
            }
            for (PhoneNumberWithType elem : o2) {
                actualPhoneNumbers2.add(elem.toString());
            }
            Collections.sort(actualPhoneNumbers1);
            Collections.sort(actualPhoneNumbers2);
            String s1 = StringUtils.join(actualPhoneNumbers1, "&");
            String s2 = StringUtils.join(actualPhoneNumbers2, "&");
            return s1.compareTo(s2);
        }
    };

    public static final Comparator<Iterable<EmailWithType>> EMAILS_COMPARATOR = new Comparator<Iterable<EmailWithType>>() {
        @Override
        public int compare(Iterable<EmailWithType> o1, Iterable<EmailWithType> o2) {
            List<String> actualEmails1 = new ArrayList<>();
            List<String> actualEmails2 = new ArrayList<>();
            for (EmailWithType elem : o1) {
                actualEmails1.add(elem.toString());
            }
            for (EmailWithType elem : o2) {
                actualEmails2.add(elem.toString());
            }
            Collections.sort(actualEmails1);
            Collections.sort(actualEmails2);
            String s1 = StringUtils.join(actualEmails1, "&");
            String s2 = StringUtils.join(actualEmails2, "&");
            return s1.compareTo(s2);
        }
    };

    public static final Comparator<Iterable<BusinessSegmentType>> BUSINESS_SEGMENT_TYPES_COMPARATOR = new Comparator<Iterable<BusinessSegmentType>>() {
        @Override
        public int compare(Iterable<BusinessSegmentType> o1, Iterable<BusinessSegmentType> o2) {
            List<Long> ids1 = new ArrayList<>();
            List<Long> ids2 = new ArrayList<>();
            for (BusinessSegmentType elem : o1) {
                ids1.add(elem.getId());
            }
            for (BusinessSegmentType elem : o2) {
                ids2.add(elem.getId());
            }
            Collections.sort(ids1);
            Collections.sort(ids2);
            String s1 = StringUtils.join(ids1, "&");
            String s2 = StringUtils.join(ids2, "&");
            return s1.compareTo(s2);
        }
    };

    public static final Comparator<Iterable<CompanySector>> COMPANY_SECTOR_COMPARATOR = (o1, o2) -> {
        List<String> actualCompanySectors1 = new ArrayList<>();
        List<String> actualCompanySectors2 = new ArrayList<>();
        for (CompanySector elem : o1) {
            actualCompanySectors1.add(elem.getId().toString());
            actualCompanySectors1.add(elem.getSector().getId().toString());
            actualCompanySectors1.add(elem.getSector().getParent().getId().toString());
            actualCompanySectors1.add(Boolean.toString(elem.isDefault()));
        }
        for (CompanySector elem : o2) {
            actualCompanySectors2.add(Optional.ofNullable(elem.getId()).map(id -> id.toString()).orElse(""));
            actualCompanySectors2.add(elem.getSector().getId().toString());
            actualCompanySectors2.add(elem.getSector().getParent().getId().toString());
            actualCompanySectors2.add(Boolean.toString(elem.isDefault()));
        }
        Collections.sort(actualCompanySectors1);
        Collections.sort(actualCompanySectors2);
        String s1 = StringUtils.join(actualCompanySectors1, "&");
        String s2 = StringUtils.join(actualCompanySectors2, "&");
        return s1.compareTo(s2);
    };

    public static <T extends Comparable> boolean areDifferent(T t1, T t2) {
        if (t1 != null && t2 != null) {
            return t1.compareTo(t2) != 0;
        } else if (t1 == null && t2 == null) {
            return false;
        } else {
            return true;
        }
    }

    public static <T> boolean areDifferent(T t1, T t2, Comparator<T> comparator) {
        if (t1 != null && t2 != null) {
            return comparator.compare(t1, t2) != 0;
        } else if (t1 == null && t2 == null) {
            return false;
        } else {
            return true;
        }
    }
}
