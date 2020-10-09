package ekol.crm.history.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.history.domain.Item;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ItemJson {
    private Long id;
    private String type;

    public Item toEntity(){
        return Item.builder()
                .id(getId())
                .type(getType()).build();

    }
    public static ItemJson fromEntity(Item item){
        return new ItemJsonBuilder()
                .id(item.getId())
                .type(item.getType()).build();
    }
}
