package ru.otus.study.spring.domain;

import java.util.*;

public class StudentAnswer {
    private final StudentTask studentTask;
    private final Collection<Answer> answers;

    public StudentAnswer(StudentTask studentTask, Set<Answer> answers) {
        this.studentTask = studentTask;
        if (Objects.nonNull(answers))
            this.answers=Collections.unmodifiableSet(answers);
        else
            this.answers=Collections.emptySet();
    }

    public StudentTask getStudentTask() {
        return studentTask;
    }

    public Collection<Answer> getReadOnlyAnswers(){
        return answers;
    }


}
