package a02a.e1;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import a02a.e1.Diet;

public class DietFactoryImpl implements DietFactory {

    @Override
    public Diet standard() {
        return new DietGenerator(t -> (t>=1500.0 && t<=2000.0), t -> true, t -> true, t -> true, t -> true);
    }

    @Override
    public Diet lowCarb() {
        return new DietGenerator(t -> (t>=1000.0 && t<=1500.0), t -> (t<=300.0), t -> true, t -> true, t -> true);
    }

    @Override
    public Diet highProtein() {
        return new DietGenerator(t -> (t>=2000.0 && t<=2500.0), t -> (t<=300.0), t -> (t>=1300.0), t -> true, t -> true);
    }

    @Override
    public Diet balanced() {
        return new DietGenerator(t -> (t>=1600.0 && t<=2000.0), t -> (t>=600.0), t -> (t>=600.0), t -> (t>=400), t -> (t<=1100));
    }

    class DietGenerator implements Diet{
        private Map<String, Integer> calMap = new HashMap<String,Integer>();
        private Map<String, Integer> carbMap = new HashMap<String, Integer>();
        private Map<String, Integer> protMap = new HashMap<String, Integer>();
        private Map<String, Integer> fatMap = new HashMap<String, Integer>();
        Predicate<Double> testCal, testCarb, testProt, testFat, testFatProt;

        public DietGenerator (Predicate<Double> testCal, Predicate<Double> testCarb, Predicate<Double> testProt, Predicate<Double> testFat, Predicate<Double> testFatProt){
            this.testCal = testCal;
            this.testCarb = testCarb;
            this.testProt = testProt;
            this.testFat = testFat;
            this.testFatProt = testFatProt;
        }

        @Override
        public void addFood(String name, Map<Nutrient, Integer> nutritionMap) {
            Integer totCal = 0;
            for (Integer cal : nutritionMap.values()) {
                totCal = totCal + cal;
            }
            calMap.put(name, totCal);
            carbMap.put(name, nutritionMap.get(Diet.Nutrient.CARBS));
            protMap.put(name, nutritionMap.get(Diet.Nutrient.PROTEINS));
            fatMap.put(name, nutritionMap.get(Diet.Nutrient.FAT));
        }

        @Override
        public boolean isValid(Map<String, Double> dietMap) {
            Double totalCal = 0.0;
            Double totalCarb = 0.0;
            Double totalProt = 0.0;
            Double totalFat = 0.0;
            for(String food: dietMap.keySet()){
                totalCal = totalCal + dietMap.get(food)*calMap.get(food)/100;
            }
            for(String food: dietMap.keySet()){
                totalCarb = totalCarb + dietMap.get(food)*carbMap.get(food)/100;
            }
            for(String food: dietMap.keySet()){
                totalProt = totalProt + dietMap.get(food)*protMap.get(food)/100;
            }
            for(String food: dietMap.keySet()){
                totalFat = totalFat + dietMap.get(food)*fatMap.get(food)/100;
            }
            if (testCal.test(totalCal) && testCarb.test(totalCarb) && testProt.test(totalProt) && testFat.test(totalFat) && testFatProt.test(totalFat + totalProt)){
                return true;
            }
            return false;
        }
    }

}
