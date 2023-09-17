package com.example.demo.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.example.demo.entity.Coffee;
import com.example.demo.listener.JobCompletionNotificationListener;
import com.example.demo.processor.CoffeeItemProsser;

import lombok.Value;

@Configuration

@EnableBatchProcessing

public class BatchConfiguration {

	@Autowired

	public JobBuilderFactory jobBuilderFactory;

	@Autowired

	public StepBuilderFactory stepBuilderFactory;
	private String fileInput;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Bean
		

	public FlatFileItemReader reader() {

		return new FlatFileItemReaderBuilder().name("coffeeItemReader")

				.resource(new ClassPathResource(fileInput))

				.delimited()

				.names(new String[] { "brand", "origin", "characteristics" })

				.fieldSetMapper(new BeanWrapperFieldSetMapper() {
					{

						setTargetType(Coffee.class);

					}
				})

				.build();

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean

	public JdbcBatchItemWriter<Object> writer(DataSource dataSource) {

		return new JdbcBatchItemWriterBuilder()

				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>()).build();

	}

	@Bean
	public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {

		return jobBuilderFactory.get("importUserJob")

				.incrementer(new RunIdIncrementer())

				.listener(listener)

				.flow(step1)

				.end()

				.build();

	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean

	public Step step1(JdbcBatchItemWriter writer) {

		return stepBuilderFactory.get("step1")

				.<Coffee, Coffee>chunk(10)

				.reader(reader())

				.processor(processor())

				.writer(writer)

				.build();

	}

	@Bean

	public CoffeeItemProsser processor() {

		return new CoffeeItemProsser();

	}

}
