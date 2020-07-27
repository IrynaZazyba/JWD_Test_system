package by.jwd.testsys.dao.util;

import by.jwd.testsys.bean.Result;

import java.util.Comparator;

public class ResultComparator implements Comparator<Result> {


    @Override
    public int compare(Result o1, Result o2) {
        if (o1.getId() > o2.getId())
            return 1;
        else if (o1.getId() < o2.getId())
            return -1;
        else
            return 0;
    }
}
