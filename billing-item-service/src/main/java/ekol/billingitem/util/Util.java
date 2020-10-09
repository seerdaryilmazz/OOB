package ekol.billingitem.util;

import ekol.billingitem.domain.HasId;

import java.util.HashSet;

public class Util {

    public static <E> HashSet<E> hashSet(E... args) {

        HashSet<E> set = new HashSet<>();

        for (E obj : args) {
            set.add(obj);
        }

        return set;
    }

    /**
     * Veritabanı açısından bakarak, bir alanın değişip değişmediği anlamak için kullanıyoruz.
     */
    public static boolean isDifferent(HasId o1, HasId o2) {
        if (o1 != null && o2 != null) {
            return !o1.getId().equals(o2.getId());
        } else if (o1 == null && o2 == null) {
            return false;
        } else {
            return true;
        }
    }
}
