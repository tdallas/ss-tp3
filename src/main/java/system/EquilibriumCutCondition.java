package system;

import engine.CutCondition;
import engine.Particle;
import lombok.Getter;

import java.util.List;

@Getter
public class EquilibriumCutCondition implements CutCondition {
    private final List<Particle> particles;
    private final double xLength;
    private final double balanceLimit;
    private int particlesOnRight;
    private int particlesOnLeft;


    public EquilibriumCutCondition(List<Particle> particles, double xLength, double percentage) {
        this.particles = particles;
        this.xLength = xLength;
        this.balanceLimit = (int)(particles.size() * percentage);
        calculateParticlesOnSides();
    }

    @Override
    public boolean isFinished() {
        calculateParticlesOnSides();
        return Math.abs(particlesOnLeft - particlesOnRight) < balanceLimit;
    }

    private void calculateParticlesOnSides(){
        particlesOnLeft = 0;
        particlesOnRight = 0;
        for(Particle p : particles){
            if(p.getXPosition() < xLength / 2){
                particlesOnLeft++;
            }
            else{
                particlesOnRight++;
            }
        }
    }
}
