package com.chenyi.jdk8newfeatures.lambda;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Lambda {

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName()+"新线程创建了");
            }
        }).start();

        //lambda
        new Thread(()->System.out.println(Thread.currentThread().getName()+"新线程创建了")).start();
    }


}


/**
 * 1.1无参数，无返回
 */
interface Cook {
    public abstract void makeFood();
}

class Demo01Cook {

    public static void main(String[] args) {
        invokeCook(new Cook() {
            public void makeFood() {
                System.out.println("做饭。。。");
            }
        });
        //使用Lambda
        invokeCook(()->{
            System.out.println("做饭。。。");
        });

        //优化lambda
        invokeCook(()-> System.out.println("做饭。。。"));
    }

    public static void invokeCook(Cook cook){
        cook.makeFood();
    }
}


/**
 * 1.2 有参数，无返回
 */
class Demo2 {

    public static void main(String[] args) {
        //Consumer<String> consumer = x -> System.out.println(x);
        //consumer.accept("有参数无返回");

        List<Person> lisiList = new ArrayList<>();
        Consumer<Person> consumer  = x -> {
            if (x.getName().equals("lisi")){
                lisiList.add(x);
            }
        };
        consumer = consumer.andThen(
                x -> lisiList.removeIf(y -> y.getAge() < 23)
        );
        Stream.of(
                new Person(21,"zhangsan"),
                new Person(22,"lisi"),
                new Person(23,"wangwu"),
                new Person(24,"wangwu"),
                new Person(23,"lisi"),
                new Person(26,"lisi"),
                new Person(26,"zhangsan")
        ).forEach(consumer);

        System.out.println(JSON.toJSONString(lisiList));

    }
}

@Data
@Accessors(chain = true)
@AllArgsConstructor
class Person {
    private Integer age;
    private String name;
}


/**
 * 1.3 有参数，有返回
 */
class MyArrays {

    public static void main(String[] args) {
        Person[] arr = {new Person(40,"陈奕迅"),
                new Person(39,"钟汉良"),
                new Person(38,"杨千嬅")};

        //对年龄进行排序
        Arrays.sort(arr, new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o1.getAge()-o2.getAge();
            }
        });

        //使用lambda
        Arrays.sort(arr,(Person p1,Person p2)->{
            return p1.getAge()-p2.getAge();
        });

        //优化lambda
        Arrays.sort(arr,(p1,p2)->p1.getAge()-p2.getAge());

        Arrays.sort(arr,Comparator.comparing(Person::getAge));

        for (Person p:arr){
            System.out.println(p);
        }
    }
}

class ForkORJoin{
    public static void main(String[] args) {
        long l = System.currentTimeMillis();
        //顺序流
        long reduce1 = LongStream.rangeClosed(0, 10000000000L).reduce(0, Long::sum);
        //并行流
        //long reduce = LongStream.rangeClosed(0, 10000000000L).parallel().reduce(0, Long::sum);
        //5050
        long l1 = System.currentTimeMillis();
        System.out.println(l1-l);
        System.out.println(reduce1);
    }
}