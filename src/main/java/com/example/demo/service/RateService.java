package com.example.demo.service;

import com.example.demo.configuration.RateConfiguration;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class RateService {
    private final Map<String, BigDecimal> rateMap = new HashMap<>();

    @Autowired
    public RateService(RateConfiguration configuration) throws IOException {
        var rateFile = configuration.getResource()
                                    .getFile();
        processRateFile(rateFile);
    }

    @SneakyThrows
    private void processRateFile(File rateFile) {
        var reader = new BufferedReader(new FileReader(rateFile));
        //skip headers
        String line = reader.readLine();
        // read content
        while ((line = reader.readLine()) != null) {
            var columns = line.split(",");
            rateMap.put(generateCurrencyPair(columns[0], columns[1]), new BigDecimal(columns[2]));
        }
        log.info("Initialize the rate map with {} entries.", rateMap.size());
    }

    public BigDecimal getRate(String base, String quote) {
        return getDefaultRateFromFile(base, quote);
    }

    private BigDecimal getDefaultRateFromFile(String base, String quote) {
        var rate = rateMap.get(generateCurrencyPair(base, quote));
        if (rate == null) {
            rate = rateMap.get(generateCurrencyPair(quote, base));
            rate = getInvertedRate(rate);
        }
        return rate;
    }

    private static BigDecimal getInvertedRate(BigDecimal rate) {
        return BigDecimal.ONE.divide(rate, new MathContext(rate.precision()));
    }

    private static String generateCurrencyPair(String base, String quote) {
        return base + "/" + quote;
    }
}
