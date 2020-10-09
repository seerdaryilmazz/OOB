package ekol.crm.activity.sf.controller;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/sf")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SfTransferController {

	private JobLauncher jobLauncher;
	private Job activityTransfer;
	
	@GetMapping("/transfer")
	public String transfer(@RequestParam String param) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException{
		JobParameters jobParameters = new JobParametersBuilder().addString("param", param).toJobParameters();
		jobLauncher.run(activityTransfer, jobParameters);
		return "started";
	}
}
