package by.jwd.testsys.bean;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;

public @Getter
@Setter
@ToString
@EqualsAndHashCode
class Statistic implements Serializable {


    private static final long serialVersionUID = 887666526432335145L;
    private String testTitle;
    private LocalTime timeOnTest;
    private LocalDateTime testStart;
    private LocalDateTime testEnd;
    private long minutesSpentOnTest;
    private int rightCountQuestion;
    private int allCountQuestion;

    public Statistic() {
    }

    public static class Builder {

        private Statistic newStatistic;

        public Builder(){
            newStatistic=new Statistic();
        }

        public Builder withTimeOnTest(LocalTime timeOnTest){
            newStatistic.timeOnTest=timeOnTest;
            return this;
        }

        public Builder withTestStart(LocalDateTime testStart){
            newStatistic.testStart=testStart;
            return this;
        }

        public Builder withTestEnd(LocalDateTime testEnd){
            newStatistic.testEnd=testEnd;
            return this;
        }

        public Builder withMinutesSpentOnTest(int minutesSpentOnTest){
            newStatistic.minutesSpentOnTest=minutesSpentOnTest;
            return this;
        }

        public Builder withRightCountQuestion(int rightCountQuestion){
            newStatistic.rightCountQuestion=rightCountQuestion;
            return this;
        }

        public Builder withAllCountQuestion(int allCountQuestion){
            newStatistic.allCountQuestion=allCountQuestion;
            return this;
        }

        public Builder withTestTitle(String testTitle){
            newStatistic.testTitle=testTitle;
            return this;
        }

        public Statistic build(){
            return newStatistic;
        }


    }


}
