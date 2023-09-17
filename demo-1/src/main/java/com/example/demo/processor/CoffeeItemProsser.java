package com.example.demo.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.example.demo.entity.Coffee;

public class CoffeeItemProsser implements ItemProcessor<Coffee, Coffee> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CoffeeItemProsser.class);

	@Override

	public Coffee process(final Coffee coffee) throws Exception {

		String brand = coffee.getBrand().toUpperCase();

		String origin = coffee.getOrigin().toUpperCase();

		String chracteristics = coffee.getCharacteristics().toUpperCase();

		Coffee transformedCoffee = new Coffee(brand, origin, chracteristics);

		LOGGER.info("Converting ( {} ) into ( {} )", coffee, transformedCoffee);

		return transformedCoffee;

	}
}