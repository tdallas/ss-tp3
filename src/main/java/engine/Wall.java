package engine;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class Wall {
    private final WallType wallType;
    private final double xPosition;
    private final double yPosition;
    private final double length;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wall wall = (Wall) o;
        return Double.compare(wall.xPosition, xPosition) == 0 &&
                Double.compare(wall.yPosition, yPosition) == 0 &&
                Double.compare(wall.length, length) == 0 &&
                wallType == wall.wallType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(wallType, xPosition, yPosition, length);
    }
}
