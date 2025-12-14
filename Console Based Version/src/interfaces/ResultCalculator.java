package interfaces;
import java.io.Serializable;

public interface ResultCalculator extends Serializable{
    // Attributes
    public double passMarks = 50;

    // Methods
    double calculateGrade();

    double calculateTotal();

    double calculatePercentage();
}
