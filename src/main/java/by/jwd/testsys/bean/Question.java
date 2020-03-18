package by.jwd.testsys.bean;

import java.time.LocalDate;
import java.util.Set;

public class Question {

    private int id;
    private String question;
    private LocalDate deletedAt;
    private Test test;
    private Set<Answer> answers;

}
