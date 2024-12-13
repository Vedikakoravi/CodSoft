import java.util.*;
import java.util.concurrent.*;

class Question {
    private String questionText;
    private List<String> options;
    private int correctOptionIndex;

    public Question(String questionText, List<String> options, int correctOptionIndex) {
        this.questionText = questionText;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public boolean isCorrect(int userAnswer) {
        return userAnswer == correctOptionIndex;
    }
}

public class QuizApplication {
    private List<Question> questions;
    private int score;
    private Scanner scanner;
    private ScheduledExecutorService scheduler;

    public QuizApplication(List<Question> questions) {
        this.questions = questions;
        this.score = 0;
        this.scanner = new Scanner(System.in);
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    public void start() {
        for (Question question : questions) {
            askQuestion(question);
        }
        displayResults();
        scheduler.shutdown();
    }

    private void askQuestion(Question question) {
        System.out.println(question.getQuestionText());
        List<String> options = question.getOptions();
        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ". " + options.get(i));
        }

        Future<Integer> future = scheduler.schedule(() -> {
            System.out.println("\nTime's up!");
            return -1;
        }, 10, TimeUnit.SECONDS);

        int userAnswer = getUserAnswer(future);
        if (userAnswer != -1 && question.isCorrect(userAnswer - 1)) {
            score++;
            System.out.println("Correct!\n");
        } else {
            System.out.println("Incorrect.\n");
        }
    }

    private int getUserAnswer(Future<Integer> future) {
        try {
            while (!future.isDone()) {
                if (scanner.hasNextInt()) {
                    int answer = scanner.nextInt();
                    future.cancel(true);
                    return answer;
                }
            }
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            return -1;
        }
    }

    private void displayResults() {
        System.out.println("Quiz Over!");
        System.out.println("Your score: " + score + "/" + questions.size());
    }

    public static void main(String[] args) {
        List<Question> questions = Arrays.asList(
            new Question("What is the capital of India?", Arrays.asList("Mumbai", "Pune", "New Delhi", "Keral"), 2),
            new Question("Which planet is known as the Red Planet?", Arrays.asList("Earth", "Mars", "Jupiter", "Saturn"), 1),
            new Question("Who wrote 'To be, or not to be'?", Arrays.asList("Shakespeare", "Hemingway", "Frost", "Dickens"), 0)
        );

        QuizApplication quiz = new QuizApplication(questions);
        quiz.start();
    }
}