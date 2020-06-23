package by.jwd.testsys.bean;

import lombok.*;

import java.time.LocalDateTime;

public @Getter
@Setter
@ToString
@EqualsAndHashCode
class Result {

    private int id;
    private LocalDateTime dateStart;
    private LocalDateTime dateEnd;
    private int rightCountQuestion;
    private Assignment assignment;
    private int countTestQuestion;
    private Test test;
    private User user;


    public Result() {
    }

    public static class Builder {

        private Result newResult;

        public Builder(){
            newResult=new Result();
        }

        public Builder withId(int id){
            newResult.id=id;
            return this;
        }

        public Builder withDateStart(LocalDateTime dateStart){
            newResult.dateStart=dateStart;
            return this;
        }
        public Builder withDateEnd(LocalDateTime dateEnd){
            newResult.dateEnd=dateEnd;
            return this;
        }
        public Builder withRightCountQuestion(int rightCountQuestion){
            newResult.rightCountQuestion=rightCountQuestion;
            return this;
        }

        public Builder withAssignment(Assignment assignment){
            newResult.assignment=assignment;
            return this;
        }

        public Builder withCountTestQuestion(int countTestQuestion){
            newResult.countTestQuestion=countTestQuestion;
            return this;
        }

        public Builder withTest(Test test){
            newResult.test=test;
            return this;
        }

        public Builder withUser(User user){
            newResult.user=user;
            return this;
        }

        public Result build(){
            return newResult;
        }



    }


}
