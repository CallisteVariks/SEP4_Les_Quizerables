package debugit.lesquizerables;

/**
 * Created by Vakha on 12/13/2017.
 */

public class Question {
    private String questionText;
    private String a;
    private String b;
    private String c;
    private String d;
    private String correctAnswer;
    private boolean creditAlreadyGiven;

    public Question() {
        //empty constructor needed by Firebase deserializer
    }

    public Question(String questionText, String a, String b, String c, String d, String correctAnswer) {
        this.questionText = questionText;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.correctAnswer = correctAnswer;
        //initially, no credit has been given for this question
        this.creditAlreadyGiven = false;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public boolean isCreditAlreadyGiven() {
        return creditAlreadyGiven;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setCreditAlreadyGiven(boolean creditAlreadyGiven) {
        this.creditAlreadyGiven = creditAlreadyGiven;
    }

    public boolean isCorrectAnswer(String selectedAnswer) {
        return (selectedAnswer.equals(correctAnswer));
    }

    public String toString() {
        return questionText;
    }

}
