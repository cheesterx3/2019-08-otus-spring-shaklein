package ru.otus.study.spring.service;

import org.junit.jupiter.api.Test;
import ru.otus.study.spring.dao.TaskDao;
import ru.otus.study.spring.domain.Answer;
import ru.otus.study.spring.domain.StudentAnswer;
import ru.otus.study.spring.domain.StudentTask;
import ru.otus.study.spring.service.i18n.LocalizationService;

import java.io.ByteArrayInputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class UserInteractionServiceImplTest {
    final StudentTask task1 = new StudentTask("Question1");
    final Answer firstAnswer = new Answer("Answer1");
    final Answer correctAnswer = new Answer("Answer2");
    final List<Answer> answers = Arrays.asList(firstAnswer, correctAnswer, new Answer("Answer2"));
    final List<Answer> nullAnswers = Arrays.asList(null, null, null);

    final Map<StudentTask, List<Answer>> taskAnswers = new HashMap<StudentTask, List<Answer>>() {{
        put(task1, answers);
    }};

    @Test
    void askTask() {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream("1".getBytes());
        final PrintStream printStream = getPrintStream();
        final MockTaskDao taskDao = new MockTaskDao(Collections.singletonList(task1), answers, Collections.singletonList(correctAnswer));
        final IOServiceImpl ioService = new IOServiceImpl(inputStream, printStream);
        UserInteractionServiceImpl service = new UserInteractionServiceImpl(taskDao, ioService, new MockLocalizationService());
        final StudentAnswer answer = service.askTask(task1);
        assertEquals(answer.getStudentTask(), task1);
        assertTrue(answer.getReadOnlyAnswers().contains(firstAnswer));
    }

    @Test
    void askTaskWithNullAnswers() {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream("1".getBytes());
        final PrintStream printStream = getPrintStream();
        final MockTaskDao taskDao = new MockTaskDao(Collections.singletonList(task1), nullAnswers, Collections.emptyList());
        final IOServiceImpl ioService = new IOServiceImpl(inputStream, printStream);
        UserInteractionServiceImpl service = new UserInteractionServiceImpl(taskDao, ioService, new MockLocalizationService());
        final StudentAnswer answer = service.askTask(task1);
        assertEquals(answer.getStudentTask(), task1);
        assertTrue(answer.getReadOnlyAnswers().isEmpty());
    }

    private PrintStream getPrintStream() {
        final MockOutputStringStream outputStringStream = new MockOutputStringStream();
        return new PrintStream(outputStringStream);
    }

    private static class MockLocalizationService implements LocalizationService {

        @Override
        public String getLocalized(String ident) {
            return "";
        }

        @Override
        public String getLocalized(String ident, Object[] var2) {
            return "";
        }
    }


    private class MockTaskDao implements TaskDao {
        private final List<StudentTask> tasks;
        private final List<Answer> answers;
        private final List<Answer> correctAnswers;

        private MockTaskDao(List<StudentTask> tasks, List<Answer> answers, List<Answer> correctAnswers) {
            this.tasks = tasks;
            this.answers = answers;
            this.correctAnswers = correctAnswers;
        }

        @Override
        public List<StudentTask> getTasks() {
            return tasks;
        }

        @Override
        public List<Answer> getAnswerVariants(StudentTask task) {
            return answers;
        }

        @Override
        public List<Answer> getCorrectAnswers(StudentTask studentTask) {
            return correctAnswers;
        }
    }
}