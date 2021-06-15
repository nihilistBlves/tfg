package org.tfg.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sendgrid.SendGrid;

@Configuration
public class SendGridConfig {

	@Value("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")
	private String appKey;
	@Bean
	public SendGrid getSendGrid() {
		return new SendGrid(appKey);
		
	}
}

