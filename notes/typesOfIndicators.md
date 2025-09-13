## 🟢 Price-Only Indicators

*(Use only close/open/high/low — volume not needed)*

These are by far the most common indicators.

**Examples:**

* **Trend / Moving averages**

    * `SMAIndicator`
    * `EMAIndicator`
    * `WMAIndicator`
    * `ZLEMAIndicator`
    * `HMAIndicator`
    * `LWMAIndicator`
* **Momentum / Oscillators**

    * `RSIIndicator`
    * `StochasticOscillatorKIndicator`, `StochasticOscillatorDIndicator`
    * `WilliamsRIndicator`
    * `CCIIndicator`
    * `ROCIndicator` (Rate of Change)
    * `MomentumIndicator`
* **Volatility**

    * `ATRIndicator` (needs high/low)
    * `DonchianChannel*Indicator`
    * `BollingerBands*Indicator` (wraps a SMA)
    * `StandardDeviationIndicator`
* **Others**

    * `MACDIndicator`
    * `PPOIndicator`
    * `DPOIndicator`
    * `AwesomeOscillatorIndicator`
    * `Ichimoku*Indicator`

📌 **Needed fields:**
`openPrice`, `highPrice`, `lowPrice`, `closePrice`
(usually only `closePrice` is mandatory; some like ATR also need high/low)

---

## 🟡 Volume-Based Indicators

*(Need volume + price)*

These are the **only indicators that require `volume`**.

**Examples:**

* `OnBalanceVolumeIndicator` (OBV)
* `ChaikinMoneyFlowIndicator` (CMF)
* `AccumulationDistributionIndicator` (ADL)
* `MoneyFlowIndexIndicator` (MFI)
* `VWAPIndicator`

📌 **Needed fields:**
`closePrice` (or high/low for MFI) **and** `volume`

---

## 🔴 Indicators that use `amount` or `trades`

**None of the built-in indicators use them.**

* `bar.getAmount()` and `bar.getTrades()` exist only as optional metadata
* Used only if **you write your own custom indicators**.

📌 **Default:**
You can safely pass `DecimalNum.valueOf(0)` for amount and `0L` for trades.

---

## ✅ Summary Table

| Type                   | Needs Price | Needs Volume | Needs Amount | Needs Trades |
| ---------------------- | ----------- | ------------ | ------------ | ------------ |
| Trend / Moving Avg     | ✅           | ❌            | ❌            | ❌            |
| Momentum / Oscillators | ✅           | ❌            | ❌            | ❌            |
| Volatility             | ✅           | ❌            | ❌            | ❌            |
| Volume-based           | ✅           | ✅            | ❌            | ❌            |
| Custom only            | ✅           | ✅/❌          | ✅            | ✅            |

---

If you want, I can also give you a **ready-made helper method** like
`getPriceForIndicator(BarSeries series, IndicatorType type)` that automatically chooses the right data (close vs high/low vs volume) depending on the indicator you want to run.

Would you like me to create that helper?
