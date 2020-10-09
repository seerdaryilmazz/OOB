package ekol.crm.quote.controller;

import java.nio.file.*;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import ekol.crm.quote.domain.enumaration.QuoteType;
import ekol.crm.quote.domain.model.quote.*;
import ekol.crm.quote.service.QuoteService;
import ekol.crm.quote.util.Utils;
import ekol.exceptions.*;

@RestController
public class FileDownloadController {

    private Path fileRepoPath;
    private QuoteService quoteService;

    @Autowired
    public FileDownloadController(
            @Value("${oneorder.fileRepoPath}") String fileRepoPathString,
            QuoteService quoteService) {
        this.fileRepoPath = Paths.get(fileRepoPathString);
        this.quoteService = quoteService;
    }

    @GetMapping("/download/{type}/{id}")
    public void download(HttpServletResponse response, @PathVariable String type, @PathVariable String id) {

        String fileName;

        if (type.equals("spotQuote")) {

            Long spotQuoteId = Long.valueOf(id);
            Quote quote = quoteService.getById(spotQuoteId);

            if (!quote.getType().equals(QuoteType.SPOT)) {
                throw new BadRequestException("Quote type is not {0}.", QuoteType.SPOT.name());
            }

            SpotQuote spotQuote = (SpotQuote) quote;
            fileName = Utils.generatePdfFileName(spotQuote);

        } else {
            throw new ApplicationException("No implementation for {0}", type);
        }

        Utils.downloadFile(fileRepoPath.resolve(fileName).toString(), false, response);
    }
}
