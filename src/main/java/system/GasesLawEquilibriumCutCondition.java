package system;

import engine.Particle;

import java.util.List;

public class GasesLawEquilibriumCutCondition extends EquilibriumCutCondition{
    private double timeAfterEquilibrium;
    private Double timeAtEquilibrium;

    public GasesLawEquilibriumCutCondition(List<Particle> particles, double xLength, double percentage, double timeAfterEquilibrium) {
        super(particles, xLength, percentage);
        this.timeAfterEquilibrium = timeAfterEquilibrium;
        this.timeAtEquilibrium = null;
    }

    @Override
    public boolean isFinished(double time){
        if(timeAtEquilibrium == null){
            if(super.isFinished(time)){
                timeAtEquilibrium = time;
            }
            return false;
        }
        else{
            super.isFinished(time);
            return time > (timeAtEquilibrium + timeAfterEquilibrium);
        }
    }

    @Override
    public boolean isAfterEquilibrium() {
        return timeAtEquilibrium == null;
    }
}
