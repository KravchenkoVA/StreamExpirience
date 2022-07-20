import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        People grisha = People.builder().surname("yeger").age(45).name("grisha").sex("m").build();
        People eren = People.builder().surname("yeger").age(26).name("Eren").sex("m").build();
        People karla = People.builder().surname("yeger").age(54).name("Karla").sex("w").build();
        People karla2 = People.builder().surname("yeger").age(54).name("Karla").sex("w").build();
        List<People> peopleList = new ArrayList<>();
        peopleList.add(grisha);
        peopleList.add(eren);
        peopleList.add(karla);
        peopleList.add(karla2);
        System.out.println(peopleList.stream().filter((p) -> p.getAge() >= 18 && p.getAge() < 27 && p.getSex().equals("m"))
                .collect(Collectors.toList()));
        System.out.println(peopleList.stream().filter((p) -> p.getSex().equals("m"))
                .mapToInt(People::getAge).average().getAsDouble());
        System.out.println(peopleList.stream().filter((p) ->
                (p.getSex().equals("m") && p.getAge() < 60) || (p.getSex().equals("w") && p.getAge() < 55)
        ).count());
        System.out.println(peopleList.stream().map(People::getName).anyMatch("grisha"::equals));
        System.out.println(peopleList.stream().map(People::getName).allMatch(p -> p.contains("a")));
        System.out.println(peopleList.stream().map(People::getName).noneMatch("grisha"::equals));
        System.out.println(peopleList.stream().count());
        peopleList.stream().skip(1).limit(1).forEach(System.out::println);
        peopleList.stream().map(People::getAge).reduce((s1, s2) -> (s1 + s2)).orElse(-1);
        System.out.println(LocalDateTime.now());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        String jsonCountries = "[{\n" +
                "\"country_id\": \"1\",\n" +
                "\"countryName\": \"Belarus\",\n" +
                "\"creationDate\": \"2022-07-05T13:10:35.157\",\n" +
                "\"cities\": [\"Gomel\", \"Minsk\", \"Grodno\", \"Vitebsk\"]\n" +
                "},\n" +
                "{\n" +
                "  \"country_id\": \"2\",\n" +
                "  \"countryName\": \"United Kingdom\",\n" +
                "  \"creationDate\": \"2022-07-05T13:10:35.157\",\n" +
                "  \"cities\": [\"London\", \"West Hopefort\", \"Manchester\"]\n" +
                "},\n" +
                "{\n" +
                "  \"country_id\": \"3\",\n" +
                "  \"countryName\": \"Russia\",\n" +
                "  \"creationDate\": \"2022-07-05T13:10:35.157\",\n" +
                "  \"cities\": [\"Moscow\", \"SPB\", \"Novosibirsk\", \"Vitebsk\"]\n" +
                "}\n" +
                "]\n";
        List<Country> myObjects = objectMapper.readValue(jsonCountries, new TypeReference<List<Country>>() {
        });
        myObjects.stream().map(Country::getCities).forEach(System.out::println);
        myObjects.stream()
                .map(Country::getCities)
                .flatMap(Collection::stream)
                .forEach(System.out::println);

        assertTrue(myObjects.stream()
                .map(Country::getCities)
                .flatMap(Collection::stream)
                .anyMatch("West Hopefort"::equals));
        assertTrue(myObjects.size()>10,"Count more then 10");

        Supplier<Stream<String>> sup = () -> myObjects.stream().map(Country::getCities)
                .flatMap(Collection::stream);

        List<String> nonEmptyStrings = sup.get().filter(s -> !s.isEmpty()).collect(Collectors.toList());

        Set<String> uniqueStrings = sup.get().collect(Collectors.toSet());
    }


}
