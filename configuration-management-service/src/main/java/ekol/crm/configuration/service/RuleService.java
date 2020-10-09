package ekol.crm.configuration.service;

import java.io.IOException;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.*;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import ekol.crm.configuration.domain.*;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RuleService {

	private static final ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	private ApplicationContext applicationContext;
	private OptionService optionService;

	public List<Object> evaluate(String code, Long subsidiaryId, Map<String, Object> facts) throws IOException {
		Option option = optionService.get(code, subsidiaryId);
		if (Objects.isNull(option) || ValueType.valueOf(option.getValueType()) != ValueType.RULE) {
			return Collections.emptyList();
		}

		StandardEvaluationContext context = new StandardEvaluationContext(facts);
		context.addPropertyAccessor(new MapAccessor());
		context.setBeanResolver(new BeanFactoryResolver(applicationContext));

		Rule[] rules = OBJECT_MAPPER.readValue(String.valueOf(option.getValue()), Rule[].class);

		List<Object> results = new ArrayList<>();
		for (Rule rule : rules) {
			if (StringUtils.isNotBlank(rule.getWhen())) {
				if (EXPRESSION_PARSER.parseExpression(rule.getWhen()).getValue(context, Boolean.class).booleanValue()) {
					results.add(rule.getThen());
				}
			} else if(CollectionUtils.isEmpty(results)) {
				return Arrays.asList(rule.getThen());
			}
		}
		return results;
	}
	
}
