package ru.otus.study.spring.dao;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import ru.otus.study.spring.domain.Answer;
import ru.otus.study.spring.domain.StudentTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;


public class TaskDaoImpl implements TaskDao {
    private final static String CORRECT_POSTFIX = "#correct";
    private final String dataUrl;
    private final Map<StudentTask, List<Answer>> tasks = new HashMap<>();
    private final Map<StudentTask, List<Answer>> taskCorrectAnswersMapping = new HashMap<>();

    public TaskDaoImpl(String dataUrl) {
        this.dataUrl = dataUrl;
        try {
            readDataFromCsvResource();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readDataFromCsvResource() throws IOException {
        final InputStream stream = getClass().getResourceAsStream(dataUrl);
        CSVParser parser = new CSVParserBuilder().withSeparator('|').build();
        try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(stream)).withCSVParser(parser).build()) {
            List<String[]> data = reader.readAll();
            for (String[] line : data) {
                CsvDataParser csvDataParser = new CsvDataParser(line).invoke();
                StudentTask task = csvDataParser.getTask();
                tasks.put(task, csvDataParser.getAnswers());
                taskCorrectAnswersMapping.put(task, csvDataParser.getCorrectAnswers());
            }
        }
    }

    @Override
    public List<StudentTask> getTasks() {
        return new ArrayList<>(tasks.keySet());
    }

    @Override
    public List<Answer> getAnswerVariants(StudentTask task) {
        return tasks.get(task);
    }

    @Override
    public List<Answer> getCorrectAnswers(StudentTask studentTask) {
        return taskCorrectAnswersMapping.get(studentTask);
    }

    private static class CsvDataParser {
        private String[] line;
        private StudentTask task;
        private List<Answer> answers;
        private List<Answer> correctAnswers;

        CsvDataParser(String... line) {
            this.line = line;
        }

        StudentTask getTask() {
            return task;
        }

        List<Answer> getAnswers() {
            return answers;
        }

        List<Answer> getCorrectAnswers() {
            return correctAnswers;
        }

        CsvDataParser invoke() {
            task = new StudentTask(line[0]);
            answers = new ArrayList<>();
            correctAnswers = new ArrayList<>();
            for (int i = 1; i < line.length; i++) {
                Answer answer = new Answer(normalizeAnswer(line[i]));
                answers.add(answer);
                if (isAnswerCorrect(line[i])) {
                    correctAnswers.add(answer);
                }
            }
            return this;
        }

        private String normalizeAnswer(String answer) {
            return answer.replaceAll(CORRECT_POSTFIX, "");
        }

        private boolean isAnswerCorrect(String answer) {
            return answer != null && answer.contains(CORRECT_POSTFIX);
        }
    }
}
