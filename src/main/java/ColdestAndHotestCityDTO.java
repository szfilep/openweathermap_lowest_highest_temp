/**
 * Created by pollakt on 2016.07.14..
 */
public class ColdestAndHotestCityDTO {
    private CityData coldestCity;
    private CityData hottestCity;

    public ColdestAndHotestCityDTO(CityData coldestCity, CityData hottestCity) {
        this.coldestCity = coldestCity;
        this.hottestCity = hottestCity;
    }

    public CityData getColdestCity() {
        return coldestCity;
    }

    public CityData getHottestCity() {
        return hottestCity;
    }
}
