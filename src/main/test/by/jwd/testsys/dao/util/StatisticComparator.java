package by.jwd.testsys.dao.util;

import by.jwd.testsys.bean.Statistic;

import java.util.Comparator;

public class StatisticComparator implements Comparator<Statistic> {


    @Override
    public int compare(Statistic o1, Statistic o2) {
        return o1.getTestTitle().compareTo(o2.getTestTitle());
    }
}
