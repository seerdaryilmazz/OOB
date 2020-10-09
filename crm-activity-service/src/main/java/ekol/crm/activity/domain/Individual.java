package ekol.crm.activity.domain;

import lombok.*;


@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor

public class Individual {
    private Long id;
    private String name;
    private String username;
    private String emailAddress;

}
