package com.dynamics.portfolio_service.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
public class AssetPriceService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final Map<String, String> ASSET_NAME_MAP;

    static {
        ASSET_NAME_MAP = new HashMap<>();
        ASSET_NAME_MAP.put("bitcoin", "bitcoin");
        ASSET_NAME_MAP.put("Bitcoin", "bitcoin");
        ASSET_NAME_MAP.put("ethereum", "ethereum");
        ASSET_NAME_MAP.put("Ethereum", "ethereum");
        ASSET_NAME_MAP.put("Solana", "solana");
        ASSET_NAME_MAP.put("solana", "solana");
        ASSET_NAME_MAP.put("TSLA", "TSLA");
        ASSET_NAME_MAP.put("Tesla", "TSLA");
        ASSET_NAME_MAP.put("VWRL", "VWRL");
        ASSET_NAME_MAP.put("FTSE All World", "VWRL.AS");
        ASSET_NAME_MAP.put("FTSE All World (acc)", "VWRL.AS");
        ASSET_NAME_MAP.put("FTSE All World (dis)", "VWRL.AS");
        ASSET_NAME_MAP.put("Microstrategy", "MSTR");
        ASSET_NAME_MAP.put("microstrategy", "MSTR");

    }

    public BigDecimal getCurrentPrice(String assetName) {
        String normalizedAssetName = ASSET_NAME_MAP.get(assetName);

        if (normalizedAssetName == null) {
            throw new RuntimeException("Unknown asset: " + assetName);
        }

        return switch (normalizedAssetName) {
            case "bitcoin", "ethereum", "solana" -> getCurrentCryptoPriceInEur(normalizedAssetName);
            case "TSLA", "VWCE.AS", "VWRL.AS", "MSTR" -> getCurrentStockPriceInEur(normalizedAssetName);
            default -> throw new RuntimeException("Unknown asset: " + assetName);
        };
    }

    private BigDecimal getCurrentCryptoPriceInEur(String cryptoId) {
        String apiUrl = "https://api.coingecko.com/api/v3/simple/price?ids=" + cryptoId + "&vs_currencies=eur";
        Map<String, Object> response = restTemplate.getForObject(apiUrl, Map.class);
        if (response != null && response.containsKey(cryptoId)) {
            Map<String, Object> cryptoData = (Map<String, Object>) response.get(cryptoId);
            return new BigDecimal(cryptoData.get("eur").toString()).setScale(1, RoundingMode.HALF_UP);
        } else {
            throw new RuntimeException("Could not fetch price for cryptocurrency: " + cryptoId);
        }
    }

    private BigDecimal getCurrentStockPriceInEur(String symbol) {
        BigDecimal usdPrice = getCurrentStockPriceInUsd(symbol);
        return usdPrice.multiply(getUsdToEurRate()).setScale(1, RoundingMode.HALF_UP);
    }

    private BigDecimal getCurrentStockPriceInUsd(String symbol) {
        String apiKey = "your_api_key";
        String apiUrl = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" + apiKey;
        Map<String, Object> response = restTemplate.getForObject(apiUrl, Map.class);
        if (response != null && response.containsKey("Global Quote")) {
            Map<String, Object> quote = (Map<String, Object>) response.get("Global Quote");
            return new BigDecimal(quote.get("05. price").toString()).setScale(1, RoundingMode.HALF_UP);
        } else {
            throw new RuntimeException("Could not fetch price for stock: " + symbol);
        }
    }

    private BigDecimal getUsdToEurRate() {
        String apiUrl = "https://api.exchangerate-api.com/v4/latest/USD";
        Map<String, Object> response = restTemplate.getForObject(apiUrl, Map.class);
        if (response != null && response.containsKey("rates")) {
            Map<String, Object> rates = (Map<String, Object>) response.get("rates");
            return new BigDecimal(rates.get("EUR").toString()).setScale(1, RoundingMode.HALF_UP);
        } else {
            throw new RuntimeException("Could not fetch exchange rate from USD to EUR");
        }
    }
}
