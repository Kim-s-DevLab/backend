package eightplusone.bit.fit.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {

	private static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);

	@Bean
	public ObjectMapper objectMapper() {
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
		builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
		builder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
		builder.simpleDateFormat(DATETIME_FORMAT);
		return builder.build();
	}
}
