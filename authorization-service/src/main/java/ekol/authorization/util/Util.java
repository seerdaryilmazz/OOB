package ekol.authorization.util;

import ekol.authorization.dto.IdNamePair;

import java.util.HashSet;
import java.util.Set;

public class Util {

    public static Set<Long> getIds(Set<IdNamePair> pairs) {
        Set<Long> ids = new HashSet<>();
        for (IdNamePair pair : pairs) {
            ids.add(pair.getId());
        }
        return ids;
    }
}
