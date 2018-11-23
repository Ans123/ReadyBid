package net.readybid.test_utils;

import net.readybid.entities.Id;
import org.apache.commons.lang3.RandomStringUtils;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class RbRandom {

    private static Random random = new Random();

    public static String name() {
        return randomAlphabetic(1, 50);
    }

    public static String emailAddress() {
        return String.format("%s.%s@%s.%s", randomAlphabetic(1, 100), new ObjectId(), randomAlphabetic(1, 100), randomAlphabetic(2, 5));
    }

    public static ObjectId oid() {
        return new ObjectId();
    }

    public static String phone() {
        return numerics(10);
    }

    public static int randomInt(int end){
        return random.nextInt(end);
    }

    public static int randomInt(int min, int max){
        int i = randomInt(max);
        while(i < min) {
            i = randomInt(max);
        }
        return i;
    }

    public static Long randomLong(long end) {
        final long l = random.nextLong();
        final double d = l / end;
        return Math.round((d - Math.round(d)) * end);
    }

    public static String alphanumeric(int maxSize, boolean fixed) {
        return RandomStringUtils.randomAlphanumeric(fixed ? maxSize : randomInt(1, maxSize));
    }

    public static String alphanumeric(int maxSize) {
        return alphanumeric(maxSize, false);
    }

    public static String alphanumeric() {
        return alphanumeric(50);
    }

    private static String numerics(int size) {
        return RandomStringUtils.randomNumeric(size);
    }

    public static String randomAlphabetic(int maxSize) {
        return randomAlphabetic(1, maxSize);
    }

    private static String randomAlphabetic(int minSize, int maxSize) {
        return RandomStringUtils.randomAlphabetic(randomInt(minSize, maxSize));
    }

    public static <T> T randomFromArray(T[] values) {
        return values[randomInt(values.length)];
    }

    public static <T> T randomFromList(List<T> list) {
        return list.get(randomInt(list.size()));
    }

    public static <T extends Enum<T>> T randomEnum(Class<T> tClass) {
        final T[] ts = tClass.getEnumConstants();
        return ts[randomInt(ts.length)];
    }

    public static double randomDouble() {
        return randomLong(Long.MAX_VALUE) + random.nextDouble();
    }

    public static Date date() {
        return new Date( new Date().getTime() - randomLong(100000L));
    }

    public static LocalDate localDate() {
        return LocalDate.now().minus(randomLong(1000), ChronoUnit.DAYS);
    }

    public static String idAsString() {
        return oid().toString();
    }

    public static Boolean bool() {
        return randomInt(50) % 2 == 1;
    }

    public static List<String> listOfIdsAsStrings() {
        final List<String> list = new ArrayList<>();
        int i = randomInt(1, 10);
        while (i-- > 0) list.add(idAsString());
        return list;
    }

    public static List<Id> listOfIds() {
        final List<Id> list = new ArrayList<>();
        int i = randomInt(1, 10);
        while (i-- > 0) list.add(id());
        return list;
    }

    public static Id id() {
        return new Id(idAsString());
    }

}
