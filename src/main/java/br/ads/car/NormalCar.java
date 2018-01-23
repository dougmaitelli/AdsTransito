package br.ads.car;

import br.ads.obstacles.Blockage;
import br.ads.obstacles.Obstacle;
import br.ads.obstacles.Reduction;
import br.ads.utils.AdsUtils;

public class NormalCar extends Car {

    public NormalCar() {
        super();
        MAX_SPEED = AdsUtils.oscilateDouble(MAX_SPEED, 0.75);

        // Initial speed is 50% of entrance speed
        addSpeed(MAX_SPEED * 0.5);
    }

    @Override
    public void reactionCarAhead() {
        double reduction = -(getSpeed() * 0.1);
        reduction = Math.min(-1, reduction);
        addSpeed(reduction);
        if (getSpeed() < MAX_SPEED - 3d) {
            int newLane = getLane() + (AdsUtils.getNextFloat() > 0.5 ? 1 : -1);
            if (getCloseCars().getCarsLane(newLane).size() == 0) {
                setLane(newLane);
            }
        }
    }

    @Override
    public void reactionObstacleAhead(Obstacle obstacle) {
        if (obstacle instanceof Blockage) {
            int newLane = getLane() + (AdsUtils.getNextFloat() > 0.5 ? 1 : -1);

            if (getCloseCars().getCarsLane(newLane).size() == 0) {
                setLane(newLane);
            }

            addSpeed(-1);
        } else if (obstacle instanceof Reduction) {
            if (getSpeed() > obstacle.calculateMaxSpeed(this, getSpeed())) {
                double reduce = Math.min(-1, -(getSpeed() * 0.1d));
                addSpeed(reduce);
            } else {
                addSpeed(1);
            }
        }
    }

    @Override
    public void reactionCarBehind() {
        // TODO Auto-generated method stub
    }

    @Override
    public void reactionFreeWay() {
        addSpeed(0.2);
    }

}
