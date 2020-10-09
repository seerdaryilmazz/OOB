package ekol.location.validator;

import ekol.exceptions.BadRequestException;
import ekol.location.domain.location.terminal.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by kilimci on 28/04/2017.
 */
@Component
public class TerminalValidator {

    @Autowired
    private PlaceValidator placeValidator;

    public void validate(Terminal terminal){
        placeValidator.validate(terminal);

        if (terminal.getWorkingHours() == null || terminal.getWorkingHours().size() == 0) {
            throw new BadRequestException("Working hours can not be empty");
        }

        if(terminal.getAssets() != null) {
            terminal.getAssets().forEach( asset -> {
                if(asset.getType() == null) {
                    throw new BadRequestException("Asset Type can not be Empty.");
                }
                int count = 0;
                terminal.getAssets().forEach( asset1 -> {
                    if(asset.getType() == asset1.getType() && asset != asset1) {
                        throw new BadRequestException("Same Asset Type is defined more than once.");
                    }
                });
            });
        }
    }
}
