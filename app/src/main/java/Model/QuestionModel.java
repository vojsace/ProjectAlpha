package Model;

public class QuestionModel {
    private String question;
    private String q_color;

    public QuestionModel() {
    }

    public QuestionModel(String question, String q_color) {

        this.question = question;
        this.q_color = q_color;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQ_color() {
        return q_color;
    }

    public void setQ_color(String q_color) {
        this.q_color = q_color;
    }
}
