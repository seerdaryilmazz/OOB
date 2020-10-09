package ekol.crm.history.domain;

import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private Long id;
    private String type;

}
