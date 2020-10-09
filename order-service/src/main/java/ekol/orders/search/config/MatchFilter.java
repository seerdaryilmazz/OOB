package ekol.orders.search.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchFilter {

    private String name;
    private String val;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
