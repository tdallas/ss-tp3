package engine;

public interface CutCondition {
    boolean isFinished(double time);
    boolean isAfterEquilibrium();
}
