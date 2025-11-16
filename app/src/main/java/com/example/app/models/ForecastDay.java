package com.example.app.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ForecastDay {
    @SerializedName("date")
    private String date;

    @SerializedName("date_epoch")
    private long dateEpoch;

    @SerializedName("day")
    private Day day;

    @SerializedName("astro")
    private Astro astro;

    @SerializedName("hour")
    private List<Hour> hour;

    // Constructors
    public ForecastDay() {}

    public ForecastDay(String date, long dateEpoch, Day day, Astro astro, List<Hour> hour) {
        this.date = date;
        this.dateEpoch = dateEpoch;
        this.day = day;
        this.astro = astro;
        this.hour = hour;
    }

    // Getters and Setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getDateEpoch() {
        return dateEpoch;
    }

    public void setDateEpoch(long dateEpoch) {
        this.dateEpoch = dateEpoch;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public Astro getAstro() {
        return astro;
    }

    public void setAstro(Astro astro) {
        this.astro = astro;
    }

    public List<Hour> getHour() {
        return hour;
    }

    public void setHour(List<Hour> hour) {
        this.hour = hour;
    }

    // Inner Classes
    public static class Day {
        @SerializedName("maxtemp_c")
        private double maxtempC;

        @SerializedName("maxtemp_f")
        private double maxtempF;

        @SerializedName("mintemp_c")
        private double mintempC;

        @SerializedName("mintemp_f")
        private double mintempF;

        @SerializedName("avgtemp_c")
        private double avgtempC;

        @SerializedName("avgtemp_f")
        private double avgtempF;

        @SerializedName("maxwind_mph")
        private double maxwindMph;

        @SerializedName("maxwind_kph")
        private double maxwindKph;

        @SerializedName("totalprecip_mm")
        private double totalprecipMm;

        @SerializedName("totalprecip_in")
        private double totalprecipIn;

        @SerializedName("avgvis_km")
        private double avgvisKm;

        @SerializedName("avgvis_miles")
        private double avgvisMiles;

        @SerializedName("avghumidity")
        private double avghumidity;

        @SerializedName("daily_will_it_rain")
        private int dailyWillItRain;

        @SerializedName("daily_chance_of_rain")
        private int dailyChanceOfRain;

        @SerializedName("daily_will_it_snow")
        private int dailyWillItSnow;

        @SerializedName("daily_chance_of_snow")
        private int dailyChanceOfSnow;

        @SerializedName("condition")
        private WeatherData.Condition condition;

        @SerializedName("uv")
        private double uv;

        // Constructors
        public Day() {}

        // Getters and Setters
        public double getMaxtempC() {
            return maxtempC;
        }

        public void setMaxtempC(double maxtempC) {
            this.maxtempC = maxtempC;
        }

        public double getMaxtempF() {
            return maxtempF;
        }

        public void setMaxtempF(double maxtempF) {
            this.maxtempF = maxtempF;
        }

        public double getMintempC() {
            return mintempC;
        }

        public void setMintempC(double mintempC) {
            this.mintempC = mintempC;
        }

        public double getMintempF() {
            return mintempF;
        }

        public void setMintempF(double mintempF) {
            this.mintempF = mintempF;
        }

        public double getAvgtempC() {
            return avgtempC;
        }

        public void setAvgtempC(double avgtempC) {
            this.avgtempC = avgtempC;
        }

        public double getAvgtempF() {
            return avgtempF;
        }

        public void setAvgtempF(double avgtempF) {
            this.avgtempF = avgtempF;
        }

        public double getMaxwindMph() {
            return maxwindMph;
        }

        public void setMaxwindMph(double maxwindMph) {
            this.maxwindMph = maxwindMph;
        }

        public double getMaxwindKph() {
            return maxwindKph;
        }

        public void setMaxwindKph(double maxwindKph) {
            this.maxwindKph = maxwindKph;
        }

        public double getTotalprecipMm() {
            return totalprecipMm;
        }

        public void setTotalprecipMm(double totalprecipMm) {
            this.totalprecipMm = totalprecipMm;
        }

        public double getTotalprecipIn() {
            return totalprecipIn;
        }

        public void setTotalprecipIn(double totalprecipIn) {
            this.totalprecipIn = totalprecipIn;
        }

        public double getAvgvisKm() {
            return avgvisKm;
        }

        public void setAvgvisKm(double avgvisKm) {
            this.avgvisKm = avgvisKm;
        }

        public double getAvgvisMiles() {
            return avgvisMiles;
        }

        public void setAvgvisMiles(double avgvisMiles) {
            this.avgvisMiles = avgvisMiles;
        }

        public double getAvghumidity() {
            return avghumidity;
        }

        public void setAvghumidity(double avghumidity) {
            this.avghumidity = avghumidity;
        }

        public int getDailyWillItRain() {
            return dailyWillItRain;
        }

        public void setDailyWillItRain(int dailyWillItRain) {
            this.dailyWillItRain = dailyWillItRain;
        }

        public int getDailyChanceOfRain() {
            return dailyChanceOfRain;
        }

        public void setDailyChanceOfRain(int dailyChanceOfRain) {
            this.dailyChanceOfRain = dailyChanceOfRain;
        }

        public int getDailyWillItSnow() {
            return dailyWillItSnow;
        }

        public void setDailyWillItSnow(int dailyWillItSnow) {
            this.dailyWillItSnow = dailyWillItSnow;
        }

        public int getDailyChanceOfSnow() {
            return dailyChanceOfSnow;
        }

        public void setDailyChanceOfSnow(int dailyChanceOfSnow) {
            this.dailyChanceOfSnow = dailyChanceOfSnow;
        }

        public WeatherData.Condition getCondition() {
            return condition;
        }

        public void setCondition(WeatherData.Condition condition) {
            this.condition = condition;
        }

        public double getUv() {
            return uv;
        }

        public void setUv(double uv) {
            this.uv = uv;
        }
    }

    public static class Astro {
        @SerializedName("sunrise")
        private String sunrise;

        @SerializedName("sunset")
        private String sunset;

        @SerializedName("moonrise")
        private String moonrise;

        @SerializedName("moonset")
        private String moonset;

        @SerializedName("moon_phase")
        private String moonPhase;

        @SerializedName("moon_illumination")
        private String moonIllumination;

        // Constructors
        public Astro() {}

        public Astro(String sunrise, String sunset, String moonrise, String moonset, String moonPhase, String moonIllumination) {
            this.sunrise = sunrise;
            this.sunset = sunset;
            this.moonrise = moonrise;
            this.moonset = moonset;
            this.moonPhase = moonPhase;
            this.moonIllumination = moonIllumination;
        }

        // Getters and Setters
        public String getSunrise() {
            return sunrise;
        }

        public void setSunrise(String sunrise) {
            this.sunrise = sunrise;
        }

        public String getSunset() {
            return sunset;
        }

        public void setSunset(String sunset) {
            this.sunset = sunset;
        }

        public String getMoonrise() {
            return moonrise;
        }

        public void setMoonrise(String moonrise) {
            this.moonrise = moonrise;
        }

        public String getMoonset() {
            return moonset;
        }

        public void setMoonset(String moonset) {
            this.moonset = moonset;
        }

        public String getMoonPhase() {
            return moonPhase;
        }

        public void setMoonPhase(String moonPhase) {
            this.moonPhase = moonPhase;
        }

        public String getMoonIllumination() {
            return moonIllumination;
        }

        public void setMoonIllumination(String moonIllumination) {
            this.moonIllumination = moonIllumination;
        }
    }

    public static class Hour {
        @SerializedName("time_epoch")
        private long timeEpoch;

        @SerializedName("time")
        private String time;

        @SerializedName("temp_c")
        private double tempC;

        @SerializedName("temp_f")
        private double tempF;

        @SerializedName("is_day")
        private int isDay;

        @SerializedName("condition")
        private WeatherData.Condition condition;

        @SerializedName("wind_mph")
        private double windMph;

        @SerializedName("wind_kph")
        private double windKph;

        @SerializedName("wind_degree")
        private int windDegree;

        @SerializedName("wind_dir")
        private String windDir;

        @SerializedName("pressure_mb")
        private double pressureMb;

        @SerializedName("pressure_in")
        private double pressureIn;

        @SerializedName("precip_mm")
        private double precipMm;

        @SerializedName("precip_in")
        private double precipIn;

        @SerializedName("humidity")
        private int humidity;

        @SerializedName("cloud")
        private int cloud;

        @SerializedName("feelslike_c")
        private double feelslikeC;

        @SerializedName("feelslike_f")
        private double feelslikeF;

        @SerializedName("windchill_c")
        private double windchillC;

        @SerializedName("windchill_f")
        private double windchillF;

        @SerializedName("heatindex_c")
        private double heatindexC;

        @SerializedName("heatindex_f")
        private double heatindexF;

        @SerializedName("dewpoint_c")
        private double dewpointC;

        @SerializedName("dewpoint_f")
        private double dewpointF;

        @SerializedName("will_it_rain")
        private int willItRain;

        @SerializedName("chance_of_rain")
        private int chanceOfRain;

        @SerializedName("will_it_snow")
        private int willItSnow;

        @SerializedName("chance_of_snow")
        private int chanceOfSnow;

        @SerializedName("vis_km")
        private double visKm;

        @SerializedName("vis_miles")
        private double visMiles;

        @SerializedName("gust_mph")
        private double gustMph;

        @SerializedName("gust_kph")
        private double gustKph;

        @SerializedName("uv")
        private double uv;

        // Constructors
        public Hour() {}

        // Getters and Setters
        public long getTimeEpoch() {
            return timeEpoch;
        }

        public void setTimeEpoch(long timeEpoch) {
            this.timeEpoch = timeEpoch;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public double getTempC() {
            return tempC;
        }

        public void setTempC(double tempC) {
            this.tempC = tempC;
        }

        public double getTempF() {
            return tempF;
        }

        public void setTempF(double tempF) {
            this.tempF = tempF;
        }

        public int getIsDay() {
            return isDay;
        }

        public void setIsDay(int isDay) {
            this.isDay = isDay;
        }

        public WeatherData.Condition getCondition() {
            return condition;
        }

        public void setCondition(WeatherData.Condition condition) {
            this.condition = condition;
        }

        public double getWindMph() {
            return windMph;
        }

        public void setWindMph(double windMph) {
            this.windMph = windMph;
        }

        public double getWindKph() {
            return windKph;
        }

        public void setWindKph(double windKph) {
            this.windKph = windKph;
        }

        public int getWindDegree() {
            return windDegree;
        }

        public void setWindDegree(int windDegree) {
            this.windDegree = windDegree;
        }

        public String getWindDir() {
            return windDir;
        }

        public void setWindDir(String windDir) {
            this.windDir = windDir;
        }

        public double getPressureMb() {
            return pressureMb;
        }

        public void setPressureMb(double pressureMb) {
            this.pressureMb = pressureMb;
        }

        public double getPressureIn() {
            return pressureIn;
        }

        public void setPressureIn(double pressureIn) {
            this.pressureIn = pressureIn;
        }

        public double getPrecipMm() {
            return precipMm;
        }

        public void setPrecipMm(double precipMm) {
            this.precipMm = precipMm;
        }

        public double getPrecipIn() {
            return precipIn;
        }

        public void setPrecipIn(double precipIn) {
            this.precipIn = precipIn;
        }

        public int getHumidity() {
            return humidity;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }

        public int getCloud() {
            return cloud;
        }

        public void setCloud(int cloud) {
            this.cloud = cloud;
        }

        public double getFeelslikeC() {
            return feelslikeC;
        }

        public void setFeelslikeC(double feelslikeC) {
            this.feelslikeC = feelslikeC;
        }

        public double getFeelslikeF() {
            return feelslikeF;
        }

        public void setFeelslikeF(double feelslikeF) {
            this.feelslikeF = feelslikeF;
        }

        public double getWindchillC() {
            return windchillC;
        }

        public void setWindchillC(double windchillC) {
            this.windchillC = windchillC;
        }

        public double getWindchillF() {
            return windchillF;
        }

        public void setWindchillF(double windchillF) {
            this.windchillF = windchillF;
        }

        public double getHeatindexC() {
            return heatindexC;
        }

        public void setHeatindexC(double heatindexC) {
            this.heatindexC = heatindexC;
        }

        public double getHeatindexF() {
            return heatindexF;
        }

        public void setHeatindexF(double heatindexF) {
            this.heatindexF = heatindexF;
        }

        public double getDewpointC() {
            return dewpointC;
        }

        public void setDewpointC(double dewpointC) {
            this.dewpointC = dewpointC;
        }

        public double getDewpointF() {
            return dewpointF;
        }

        public void setDewpointF(double dewpointF) {
            this.dewpointF = dewpointF;
        }

        public int getWillItRain() {
            return willItRain;
        }

        public void setWillItRain(int willItRain) {
            this.willItRain = willItRain;
        }

        public int getChanceOfRain() {
            return chanceOfRain;
        }

        public void setChanceOfRain(int chanceOfRain) {
            this.chanceOfRain = chanceOfRain;
        }

        public int getWillItSnow() {
            return willItSnow;
        }

        public void setWillItSnow(int willItSnow) {
            this.willItSnow = willItSnow;
        }

        public int getChanceOfSnow() {
            return chanceOfSnow;
        }

        public void setChanceOfSnow(int chanceOfSnow) {
            this.chanceOfSnow = chanceOfSnow;
        }

        public double getVisKm() {
            return visKm;
        }

        public void setVisKm(double visKm) {
            this.visKm = visKm;
        }

        public double getVisMiles() {
            return visMiles;
        }

        public void setVisMiles(double visMiles) {
            this.visMiles = visMiles;
        }

        public double getGustMph() {
            return gustMph;
        }

        public void setGustMph(double gustMph) {
            this.gustMph = gustMph;
        }

        public double getGustKph() {
            return gustKph;
        }

        public void setGustKph(double gustKph) {
            this.gustKph = gustKph;
        }

        public double getUv() {
            return uv;
        }

        public void setUv(double uv) {
            this.uv = uv;
        }

        // Utility method to get hour from time string
        public String getHourOnly() {
            if (time != null && time.length() >= 16) {
                return time.substring(11, 16); // Extract "HH:mm" from "yyyy-MM-dd HH:mm"
            }
            return "";
        }
    }
}
