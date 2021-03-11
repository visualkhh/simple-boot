package test;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JavaTest {

    @Getter
    @Setter
    @Builder
    @ToString
    public static class User {
        String name;
        String number;
    }

    @Test
    public void test() {

        List<User> datas = Arrays.asList(
                User.builder().name("name").number("10006").build(),
                User.builder().name("name").number("10005").build(),
                User.builder().name("name").number("10004").build(),
                User.builder().name("name").number("AG01").build(),
                User.builder().name("name").number("AG02").build(),
                User.builder().name("name").number("AG03").build());

        Map<Object, List<User>> collect = datas.stream().collect(Collectors.groupingBy((User it) -> it.getNumber().startsWith("AG") ? "AG" : it.getNumber()));
        collect.forEach((k, v) -> {
            System.out.println("key: " + k + "\t\tvalue:" + v);
        });

//        Map<String, String> collect = datas.stream().collect(Collectors.toMap(e -> e, e -> e));
//        Map<String, String> collect = datas.stream().collect(Collectors.toMap(e -> e, e -> e, (x, y) -> y, LinkedHashMap::new));

//        collect.forEach((k, v) -> {
//            System.out.println("key: " + k + "\t\tvalue:"+v);
//        });


//        return query.parameters().entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().get(0)));
//        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y, LinkedHashMap::new));
//        return getSims((e) -> {
//            return null != e.getKey() && null != e.getValue() && null!=klass && klass.isAssignableFrom(e.getKey());
//        }).entrySet().stream().collect(Collectors.toMap((it) -> {
//            return (Class<T>) it.getKey();
//        }, (


    }
}
